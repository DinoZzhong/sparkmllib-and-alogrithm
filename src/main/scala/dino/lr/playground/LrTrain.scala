package dino.lr.playground

import dino.common.traits.SparkConfigSingleton
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.feature.LabeledPoint
import org.apache.spark.ml.linalg.{Vectors,Vector}

/**
 * Created by Dino on 2016/8/27.
 *
 */
object LrTrain {
  def main(args:Array[String]): Unit ={
    case class BankruptMode(label:Double,feature:Vector)
    val ss = LrTrainConfig.getSparkSession()

    val data = ss.sparkContext.textFile("playground/Qualitative_Bankruptcy.data.txt")
    val part = data.map(line=>line.split(","))
      .map(x=>LabeledPoint(getDoubleValue(x(6))  // 结果
          ,Vectors.dense(x.slice(0,6).map(s=>getDoubleValue(s)))) // 标签数组
      )

    val parts = ss.createDataFrame(part).toDF("label","features")


    //parts.foreach(println)
    val re =parts.randomSplit(Array(0.7,0.3)) //weights 按照权重划分返回一个rdd数组，注 并非是严格的按照比例划分,每次比例也不同
    val trainData = re(0)
    val testData =re(1)


    val lr = new LogisticRegression()
    lr.setStandardization(true)// 是否标准化默认为true 将数据按比例缩放，结果值可正可负，绝对值不会太大
          // z-score(零一均值)规范化 (x-mean(x))/std(x)  mean均值，std标准差，结果符合正太分布，均值为0，标准差为1
          // min -max 标准化,(x-min)/(max-min)
    lr.setThreshold(0.5) // 二分类问题，设置阈值 默认为[0,1]
    lr.setRegParam(0.0) // 正则化系数
    lr.setMaxIter(10)
    lr.setElasticNetParam(0.0) // 正则化类型。0是L2,1是L1,O到1是混合模式，解决feature之间的高相关性问题
    val model = lr.fit(trainData)

    //val test =testData.map(x=>model.evaluate(x))

    val trainResult=model.transform(testData)

    trainResult.filter(row=>row.getAs("label")!=row.getAs("prediction")).select("features","label","probability","prediction")
      .collect().foreach(x=>println(x.toString()))

    // 计算出错率


    ss.stop()
  }

  // 将分类标签数值化
  def getDoubleValue(input:String):Double={
     val res:Double =input match{
       case "P"=>3.0;  //因变量 value=>postitive 表现正面
       case "A"=>2.0; // 因变量 value =>average  表现一般
       case "N"=>1.0; // 因变量 value =>negative 变现负面
       case "NB"=>1.0 // 是否破产，NB 未破产
       case "B" =>0.0 // 是否破产, B 破产
       case _ =>0.0
    }
    res
  }

}
object LrTrainConfig extends  SparkConfigSingleton{}