package dino.recomm


import breeze.linalg.{DenseMatrix, DenseVector}
import dino.common.AlgorithmFunc
import dino.common.traits.defaultSession
import org.apache.spark.mllib.recommendation.{MatrixFactorizationModel, ALS, Rating}
import org.jblas.DoubleMatrix


/**
 * Created by Dino on 2016/9/19.
 *
 * user=>user.id、 age、 gender、 occupation和ZIP code |
rat =>user id、 movie id、 rating（从1到5）和timestamp  \t
movie=>movie id、 title、 release date以及若干与IMDB link和电影分类相关的属性  |
 */

// 用户推荐，指向给定用户推荐物品，以“前K个”形式展现


object UserRecom {
  def main(args:Array[String]): Unit ={

    val sc = defaultSession.getSparkSession().sparkContext
    val rat = sc.textFile("ml-100k/u.data")
    val user=sc.textFile("ml-100k/u.user")
    val movie = sc.textFile("ml-100k/u.item")

    println(rat.first())
    val ratings = rat.map(_.split("\t").take(3))
      .map{case Array(user,movie,rating)=>Rating(user.toInt,movie.toInt,rating.toDouble)}

    //lambda 正则化过程，控制模型过拟合情况，正则参数通过用非样本的测试数据进行交叉验证来调整
    // lambda是通过测试和交叉验证法来设置的
    val model1=ALS.train(ratings,50,10,0.01)
    // 隐式反馈数据训练模型，多了一个可设置的aplha参数
    //val model1=ALS.trainImplicit(ratings,50,50,0.01,0.001)
    model1.save(sc,"ml-100k/result.model")
    val model = MatrixFactorizationModel.load(sc,"ml-100k/result.model")

    val userId=789
    val topK = 10
    // 根据训练好的模型进行预测，包括为用户推荐、找出相同的物品
    // 预测得分
    val score =model.predict(userId,123)
    val realscaore = ratings.filter(x=>x.product==123&&x.user==userId).take(1).map(_.rating)
    println("userid "+userId+" prodcutId 123"+" prediction score:"+score +" real score:"+realscaore)
    // 预测前K个推荐物品
    val topKRecs = model.recommendProducts(userId,topK)

    // 获取 789 用户推荐的电影和分数
    val titles=movie.map(line=>line.split("\\|").take(2)).map(x=>(x(0).toInt,x(1))).collectAsMap()
    // sortby中加减号表示降序，对数字有效
    println("===========Acutal Favorite ten films list ======= ")
    ratings.keyBy(_.user).lookup(userId).sortBy(-_.rating).take(topK).map(x=>(titles.get(x.product).get,x.rating)).foreach(println)
    println("============Recommend  Score the highest ten films list =======")
    topKRecs.sortBy(-_.rating).map(x=>(titles.get(x.product).get,x.rating)).foreach(println)
    println(" ==========================================")

    //物品推荐，从模型中取回其对应的因子，因子数为50，由训练模型的时候指定
    val itemid=567
    val itemFactor = model.productFeatures.lookup(itemid).head

    // 利用余弦计算各个物品的相似度 利用blas包
   /*
      val itemVector = new DoubleMatrix(itemFactor)
      val sims = model.productFeatures.map{case (id,factor)=>
      val factorVector = new DoubleMatrix(factor)
      val sim = CosineTest.cosineSimilarity(factorVector,itemVector)
      (id,sim)
    }
*/
    // 利用breeze实现
    val itemBreeze = DenseVector(itemFactor)
    val sims2 = model.productFeatures.map{case (id,factor)=>
        val factorVector = DenseVector(factor)
        val sim = AlgorithmFunc.cosine(factorVector,itemBreeze)
      (id,sim)
    }
    // 按照相似度排序取出topK,排名第一为自己本身
    val  sortSims = sims2.top(topK)(Ordering.by[(Int,Double),Double]{case (id,sim)=>
      sim})
    println("Movie 567 In the first 10 Similar and Similarity score")
    sortSims.map(x=>(x._1+":"+titles.get(x._1).get,x._2)).foreach(println)
    println("====================")


    // 在推荐系统和协同过滤模型里常用两个评估指标判断模型好坏，均方差、K值平均准确率
        // 均方差反应一个数据集的离散程度，常用于显式评级的情形
        // K均值，衡量耨个查询所返回的“前K个”对象/文档的平均相关性，越接近0表示准确率越低

    //均方差 评价模型效果，直接衡量“用户-物品”评级矩阵的重建误差，ALS是其中一种，常用语显式评级
    // 取789  查看实际分数与预测评分的分数
    val one=ratings.filter(f=>f.user==789).take(1)(0)

    // 单个计算平方误差
    val squareError = Math.pow(one.rating-model.predict(789,one.product),2.0)
    println("USER 789 squareError:"+squareError)
    // 整体均方差，相同用户相同产品的推荐分数与实际分数差的平方和，除以总记录数
    val userProduct = ratings.map{case Rating(userID,product,rating)=>((userID,product),rating)}
    val preAndReal = model.predict(userProduct.map(_._1)).map{
      case Rating(userID,product,rating)=>((userID,product),rating)
    }.join(userProduct)
    val nums = preAndReal.count()
    //均方差
    val  mse = preAndReal.map{case ((userID,product),(prediction,actual))=>Math.pow(prediction-actual,2.0)}.reduce(_+_) / nums
    // 均方根差
    val rmse = Math.sqrt(mse)
    println("Total records:"+nums+" MSE:"+mse+" RMSE:"+rmse)


    // k值平均准确率
    val itemFactors = model.productFeatures.map{case (id,factor)=>factor}.collect()
    val itemMatrix = new DoubleMatrix(itemFactors)
  //  val  itemM = DenseMatrix(itemFactors)

    val imBroadcast = sc.broadcast(itemMatrix)

    val allRecs = model.userFeatures.map{case (userId,array)=>
        val userVector = new DoubleMatrix(array)
        val scores = imBroadcast.value.mmul(userVector)  //向量乘矩阵
        val sortedWithId = scores.data.zipWithIndex.sortBy(-_._1) // 按分数排序
        val recommendedIds = sortedWithId.map(_._2+1).toSeq
      (userId,recommendedIds)
    }
    val userMovies = ratings.map(f=>(f.user,f.product)).groupBy(_._1)
    userMovies.take(2)(0)._2.size
    userMovies.take(2).zipWithIndex.foreach(x=>print(x+"|"))
    val MAPK = allRecs.join(userMovies).map{case (userId,(predicted,actualWithIds))=>
        val actual = actualWithIds.map(_._2).toSeq
       // avgPrecisionK(actual, predicted, topK)
    }

    sc.stop()
  }

}

// 物品推荐 给定一个物品，有哪些物品与它最相似
// 人为实现物品之间的相似度， 相似度衡量方法包括 皮尔森相关系数、针对实数向量的预选相似度、针对二元向量的杰卡德相似稀疏
// 余弦相似度是两个向量在N维空间里两者夹角的读书，是两个向量点击与各向量范数(或长度)的乘积的商
// 余弦相似度用的范数为L2-范数，余弦相似度是一个正则化了的点积


