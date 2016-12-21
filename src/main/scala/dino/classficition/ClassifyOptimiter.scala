package dino.classficition

import dino.common.traits.defaultSession
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.mllib.classification.{LogisticRegressionWithLBFGS, ClassificationModel, LogisticRegressionWithSGD}
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.mllib.feature.StandardScaler
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.optimization.{SquaredL2Updater, SimpleUpdater, Updater}
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD

/**
 * Created by Dino on 2016/11/6.
 *  分类算法参数优化，且对模型进行迭代
 *
 *
 *  ROC问题  二分类预测的原始集分为 正类、负类，
 *  根据预测结果区分为 真、假，即：
 *    如果为正类：预测对了为真正类（正确的肯定类） 如果本身为负类，但被预测为正类假正类（误报）
 *    如果为负类：预测对了为真负类(正确的拒绝类)，本身为正类预测为负类则为假负类(漏报)
 *   真正类率(TPR) = (真正类)/(真正类+假负类)
 *   真负类率(FNR) = 真负类/(真负类+假正类)
 * 在同一信号下对不同的判断标准的反应，其目标应当是尽量偏离(0.0)和(1,1)对角线且往(0,1)上靠
 *
 * 具体的衡量指标为AUC（Area under curve），即ROC曲线下的面积,总面积为1，
 *  如果面积只有0.5表示完全随机,无诊断价值
 *  小于0.5表示不符合真是情况，一般很少出现
 *  0.7~0.9 有一定的准确性
 *  0.9以上时为比较准确
 *
 * 逻辑回归模型中常用损失函数或者代价函数来度量预测错误程度
 */
object ClassifyOptimiter {

  def main(args:Array[String]): Unit ={
    val sc = defaultSession.getSparkSession().sparkContext

    // val train = sc.textFile("/data/classfication/train.tsv")
    val train = sc.textFile("data/train.tsv")
    var isfirst=false
    val records=train.filter(x=>
      if(!isfirst){
        isfirst=true
        false
        } else isfirst)
      .map(_.split("\t"))

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
    scaledDataCats.cache()

    //次数迭代
/*    val iterResult = Seq(1,5,10,50).map{param=>
      val model = trainWithParams(scaledDataCats,0.0,param,new SimpleUpdater,1.0)
      createMetrics(s"$param iterations",scaledDataCats,model)
    }
    iterResult.foreach{case (param,auc)=>println(f"$param ,AUC =${auc * 100}%2.2f%%")}*/
/*
1 iterations ,AUC =64.70%
5 iterations ,AUC =66.88%
10 iterations ,AUC =67.24%
50 iterations ,AUC =67.14%
* */

    // 正则化
    val regResult = Seq(0.001,0.01,0.1,1.0,10.0).map{param=>
      val model = trainWithParams(scaledDataCats,param,20,new SquaredL2Updater,1.0)
      createMetrics(s"$param L2 regularization parameter",scaledDataCats,model)
    }
    regResult.foreach{case (x,y)=>println(f"$x,AUC=${y*100}%2.2f%%")}
    /*
  0.001 L2 regularization parameter,AUC=66.27%
  0.01 L2 regularization parameter,AUC=66.31%
  0.1 L2 regularization parameter,AUC=66.28%
  1.0 L2 regularization parameter,AUC=65.94%
  10.0 L2 regularization parameter,AUC=35.32%
    */





  }

  // 定义辅助函数,根据给定的输入训练模型
  def trainWithParams(input:RDD[LabeledPoint],regParam:Double,numIterations:Int
                       ,updater:Updater,stepSize:Double):ClassificationModel={
    val lr = new LogisticRegressionWithSGD
    lr.optimizer.setStepSize(stepSize).setNumIterations(numIterations)
    .setUpdater(updater).setRegParam(regParam)
    lr.run(input)
  }
  // 定义第二个辅助函数没根据输入数据和分类模型，计算相关的AUC
  def  createMetrics(label:String,data:RDD[LabeledPoint],model:ClassificationModel): (String,Double) ={
    val scoreAndLabels = data.map{point=>(model.predict(point.features),point.label)}
    val metrics = new BinaryClassificationMetrics(scoreAndLabels)
    (label,metrics.areaUnderROC())
  }





}
