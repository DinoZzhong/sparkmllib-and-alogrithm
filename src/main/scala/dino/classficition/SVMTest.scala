package dino.classficition

import dino.common.traits.defaultSession


import org.apache.spark.mllib.classification.SVMWithSGD
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint

/**
 * Created by Dino on 2016/9/5.
 *SVMWithSGD类中参数说明：
      stepSize: 迭代步长，默认为1.0
      numIterations: 迭代次数，默认为100
      regParam: 正则化参数，默认值为0.0
      miniBatchFraction: 每次迭代参与计算的样本比例，默认为1.0
      gradient：HingeGradient ()，梯度下降；
      updater：SquaredL2Updater ()，正则化，L2范数；
      optimizer：GradientDescent (gradient, updater)，梯度下降最优化计算。
 */
object SVMTest {
def main(args:Array[String]): Unit ={

  val sc = defaultSession.getSparkSession()
  val data = sc.sparkContext.textFile("data/mllib/sample_svm_data.txt")

  val datas = data.map { line =>
    val parts = line.split(" ")
      LabeledPoint(parts(0).toDouble,Vectors.dense(parts.tail.map(x=>x.toDouble).toList.toArray))
  }.randomSplit(Array(0.6,0.4))
  val parseData = datas(0)
  val testData = datas(1)
  //  parseData.foreach(println)

  val numIterators:Int = 20

  val model = SVMWithSGD.train(parseData,numIterators,1.0,0.02)

  // 测试数据
  val labelAndPrediction = testData.map { point =>
    val prediction  = model.predict(point.features)
    (point.label,prediction)
  }

  val train = labelAndPrediction.filter(r=>r._1!=r._2).count.toDouble /parseData.count()
  println("Training Error:"+train)

  sc.stop()
}
}
