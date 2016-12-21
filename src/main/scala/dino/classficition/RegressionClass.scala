package dino.classficition

import dino.common.traits.defaultSession
import org.apache.spark.mllib.classification.{LogisticRegressionModel, LogisticRegressionWithSGD, NaiveBayes, SVMWithSGD}
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.tree.DecisionTree
import org.apache.spark.mllib.tree.configuration.Algo
import org.apache.spark.mllib.tree.impurity.Entropy
import org.apache.spark.rdd.RDD

/**
 * Created by Dino on 2016/9/29.
 *每一列说明
url urlid boilerplate alchemy_category alchemy_category_score avglinksize
commonlinkratio_1 commonlinkratio_2 commonlinkratio_3 commonlinkratio_4
compression_ratio embed_ratio framebased frameTagRatio hasDomainLink
html_ratio image_ratio is_news lengthyLinkDomain linkwordscore
news_front_page non_markup_alphanum_characters numberOfLinks numwords_in_url
paramet


 书：spark mllib机器学习
 深入博客：http://blog.csdn.net/jjfnjit/article/details/49584899

 ROC曲线,针对分类器的真阳性-假阳性率的图形化解释
 */
object RegressionClass {
  def main(args:Array[String]): Unit ={
    val sc = defaultSession.getSparkSession().sparkContext
    //
   // val train = sc.textFile("/data/classfication/train.tsv")
    val train = sc.textFile("data/train.tsv")
    val records=train.map(_.split("\t"))
    //记录测试
    records.first().map(_.replace("\"","")).slice(0,10).foreach(x=>println(x.substring(0,if(x.length<=10)x.length else 10)))
    val data = records.map{r=>
      val trimmed=r.map(_.replace("\"",""))
      val label = trimmed(r.size-1).toInt  // 最后一列
      val features = trimmed.slice(4,r.size-1).map(d=>
      if(d =="?")0.0 else d.toDouble)
      LabeledPoint(label,Vectors.dense(features))
    }
    val Array(x,y) = data.randomSplit(Array(0.7,0.3)) // x 为训练样本，y为测试样本

    val  nbData = records.map{r=>
      val trims=  r.map(_.replace("\"",""))
      val label = trims(r.size-1).toInt
      val feature = trims.slice(4,r.size-1).map(d=>if(d =="?")0.0 else d.toDouble)
        .map(d=> if(d<0)0.0 else d)
      LabeledPoint(label,Vectors.dense(feature))
      }
    val Array(m,n) = nbData.randomSplit(Array(0.7,0.3)) // m 为训练样本，n为测试样本
    data.cache()
    nbData.cache()
    data.take(1)(0).features.size // 特征数

    val numIt = 100

    val lrModel = LogisticRegressionWithSGD.train(x,numIt)//逻辑回归随机梯度下降
    lrModel.save(sc,"/data/savemodel/sections5/lr.model")

    val lr=LogisticRegressionModel.load(sc,"/data/savemodel/sections5/lr.model")

    val svmModel = SVMWithSGD.train(x,numIt) // svm
    svmModel.save(sc,"/data/savemodel/sections5/svm.model")
    val nbModel = NaiveBayes.train(m)  // 朴素贝叶斯，要求无负特征
    nbModel.save(sc,"/data/savemodel/sections/naviebayes.model")
    val dtModel = DecisionTree.train(x,Algo.Classification,Entropy,5) // 决策树，5为树的深度
    dtModel.save(sc,"/data/savemodel/sections/decisiontree.model")


    //模型预测
     // 单条测试数据
    val first = data.first()
    val prediction = lrModel.predict(first.features)

    // 测试逻辑回归模型 准确率49.2%
    y.map{point=>if(lrModel.predict(point.features)==point.label) 1 else 0}.sum()/y.count()

    // 测试svm
    y.map(point=>if(svmModel.predict(point.features).toInt==point.label)1 else 0).sum()/y.count()
    m.map(point=>if(nbModel.predict(point.features).toInt==point.label)1 else 0).sum()/m.count()  // 58%
    //0.6185840707964602
    y.map(point=>if(dtModel.predict(point.features).toInt==point.label)1 else 0).sum()/y.count()



    //  优化  转换成矩阵进行查看 如果均值、方差的值在不同类之间差距较大，此时说明可能不符合标准高斯分布，那么可能需要做标准化
    import org.apache.spark.mllib.linalg.distributed.RowMatrix
    val vectors = x.map(lp=> lp.features)
    val martix = new RowMatrix(vectors)
    val matrixSummary = martix.computeColumnSummaryStatistics()
    println(matrixSummary.mean)


    // 归一标准化  公式(特征值-均值)/sqrt(标准差)  针对每一个feature进行的计算
    import org.apache.spark.mllib.feature.StandardScaler
    val scaler = new StandardScaler(withMean = true,withStd = true).fit(vectors)  // 是否从数据中减去均值，是否应用标准差缩放
    val scaledData = x.map(lp=>LabeledPoint(lp.label,scaler.transform(lp.features)))
    println(x.first().features)
    println(scaledData.first().features)

    // 测试数据处理
    val lrModelScaled = LogisticRegressionWithSGD.train(scaledData,numIt)
    val numData = y.count()
    val testScaledData = y.map(lp=>LabeledPoint(lp.label,scaler.transform(lp.features)))
    val lrTotalCorrectScaled =testScaledData.map{point =>if(lrModelScaled.predict(point.features)==point.label) 1 else 0}.sum
    // 测试数据 train效果
    val lrAccuracyScaled = lrTotalCorrectScaled / numData  // 准确率提升到 62%
    val lrPredictionsVsTrue = testScaledData.map(point=>(lrModelScaled.predict(point.features),point.label))
    val lrMetricsScaled = new BinaryClassificationMetrics(lrPredictionsVsTrue) //转换成二分类度量对象

    val lrPr = lrMetricsScaled.areaUnderPR()
    val lrRoc = lrMetricsScaled.areaUnderROC()

    //通过添加类别特征进行优化 特征做 1-of-k
    val categories= records.map(r=>r(3)).distinct.collect.zipWithIndex.toMap
    val dataCategories = records.map{r=>
    val trimmed = r.map(_.replaceAll("\"",""))
    val label = trimmed(r.size-1).toInt
      val categroryIdx = categories(r(3))
      val categoryFeatures = Array.ofDim[Double](categories.size)
      categoryFeatures(categroryIdx) = 1.0
      val otherFeatures = trimmed.slice(4,r.size-1).map(d=>if(d=="?")0.0 else d.toDouble)
      val features = categoryFeatures ++ otherFeatures
      LabeledPoint(label,Vectors.dense(features))
    }

    val scalerCats = new StandardScaler(true,true).fit(dataCategories.map(lp=>lp.features))
    val Array(scaledDataCats,testScaledDataCats) = dataCategories.map(lp=>LabeledPoint(lp.label,scalerCats.transform(lp.features)))
    .randomSplit(Array(0.7,0.3))


    scaledData.first.features  // 前14维虽然原本是稀疏的，但在标准化之后将得到一个非稀疏的特征向量表示，
    // 这个时候丢失了数据的稀疏性，如果在大数据量下，意味着计算量和内存的爆炸性增长。

  val lrModelScaledCats = LogisticRegressionWithSGD.train(scaledDataCats,numIt)

    // 结果验证，这里直接取训练数据
    val lrTotalCorrectScaledCats = scaledDataCats.map(point =>
     if(lrModelScaledCats.predict(point.features)==point.label)1.0 else 0.0
    )
    val lrAccuracyScaledCats = lrTotalCorrectScaledCats.sum/scaledDataCats.count  // 增加类型变量后准确率提升到67%


    //  以上是回归模型，模型性能的另一个关键部分是对每个模型使用正确的数据格式。数值向量应用朴素贝叶斯模型得到非常差差的结果
    // 多项式模型，可以处理技术形式的数据，包括二元表示的类型特征(1 of k) 或者频率数据

    // 仅用频率数据 训练朴素贝叶斯模型 使用正确格式的输入数据，对模型有正向作用
    val dataNB = dataCategories.map{r=>LabeledPoint(r.label,Vectors.dense(r.features.toArray.slice(0,13)))}
    dataNB.cache()
    val nbModelCats = NaiveBayes.train(dataNB)
    val nbTotalCorrectCats = dataNB.map{point=>
      if(nbModelCats.predict(point.features)==point.label) 1.0 else 0.0
    }
    nbTotalCorrectCats.sum()/dataNB.count()  // 提升到60.6%

    val nbPredictsVsTrueCats = dataNB.map{points=>(nbModelCats.predict(points.features),points.label)}

    val nbMetricsCats = new BinaryClassificationMetrics(nbPredictsVsTrueCats)
    nbMetricsCats.areaUnderPR()
    nbMetricsCats.areaUnderROC()
/*
    // 空值测试 即针对map之类函数 空值不会抛出异常
    val emptyMap:Map[String,String] = Map.empty
    val empty = sc.parallelize(Seq(emptyMap))
    empty.flatMap(t=>t).map(r=>r._1).distinct().collect()
*/

  }

}
