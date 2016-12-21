package dino.test

import dino.common.traits.defaultSession
import org.apache.spark.mllib.linalg.Vectors

import org.apache.spark.mllib.regression.{LabeledPoint, LinearRegressionWithSGD}


/**
 * Created by Dino on 2016/9/7.
 *
 * 线性回归
 * 损失函数 估计值与实际值的平方差的和  目标:得到极小值
 *  梯度下降法(批量梯度下载法，随机梯度下降法)
 *    步长：迭代步长 过大容易跳过极小值，过小效率过慢
 *  批量梯度下降法
 *    每迭代一次的复杂度为O(mn),复杂度高
 *
 *  随机梯度下降法
 *    选择一个或者一小批进行迭代并更新结果
 *
 *   MLLIB采用的是随机梯度下降法
 *    在分布式中，会随机抽取一定比例的样本作为当前迭代的计算样本，
 *    对抽取的每一个样本分别计算梯度，之后再通过聚合函数对样本梯度进行累加
 *
 */
object MlibLR {
  //Logger.getRootLogger.setLevel(Level.WARN)
  def main(args:Array[String]): Unit ={

    val sc = defaultSession.getSparkSession().sparkContext
    val data  = sc.textFile("/")
    val examples = data.map { line =>
      val parts = line.split(",")
      LabeledPoint(parts(0).toDouble,Vectors.dense(parts(1).split(" ").map(_.toDouble)))
    }.map(f=>LabeledPoint(f.label,Vectors.dense(f.features(0)/5000,f.features(1)/5))).cache()


    val numExemple = examples.count()

    val stepSize = 1
    val minBatchFraction =0.3

    val model =LinearRegressionWithSGD.train(examples,100,stepSize,minBatchFraction)


  println("weight:"+model.weights+"  intercept:"+model.intercept)
    val  lr = new LinearRegressionWithSGD() //初始数据、偏置向量




  }



}
