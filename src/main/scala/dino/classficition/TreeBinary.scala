package dino.classficition

import dino.common.traits.defaultSession
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint

import org.apache.spark.mllib.tree.DecisionTree
import org.apache.spark.mllib.tree.configuration.Algo
import org.apache.spark.mllib.tree.impurity.{Entropy, Impurity}
import org.apache.spark.mllib.tree.model.DecisionTreeModel
import org.apache.spark.rdd.RDD

/**
 * Created by Dino on 2016/11/18.
 *
 * 决策树进行二分类计算
 */
object TreeBinary {

  def main(args:Array[String]): Unit ={

    val sc = defaultSession.getSparkSession().sparkContext

    // val train = sc.textFile("/data/classfication/train.tsv")
    val train = sc.textFile("data/train.tsv")
    var isfirst=false
    val records=train.filter(x=>
      if(!isfirst){
        isfirst=true
        false
      } else isfirst).map(_.split("\t").map(_.replace("\"","")))
    .map{line=>
      val vector = Vectors.dense(line.slice(4,line.size-1).map(x=>{
        if(x=="?")0.0 else x.toDouble
      }))
      LabeledPoint(line.last.toDouble,vector)
    }

    val Array(data,test)=records.randomSplit(Array(0.7,0.3))
  val dtResult = Seq(3,8,12,16,24).map(depth=>{
      val model = trainDTWithParams(data,depth,Entropy)
    val scoreAndLabels= test.map{point=>
      val score = model.predict(point.features)
      (if(score>0.5)1.0 else 0.0,point.label)
    }
    val metric = new BinaryClassificationMetrics(scoreAndLabels)
    (s"$depth tree depth",metric.areaUnderROC())
  })
    dtResult.foreach{case (param,auc)=>println(f"$param. AUC =${auc*100}%2.2f%%")}

  }

  // impurity用于计算信息增益的方法，可以有 Entropy/Gini
  def  trainDTWithParams(input:RDD[LabeledPoint],maxDepth:Int,
                          impurity: Impurity):DecisionTreeModel ={
    DecisionTree.train(input,Algo.Classification,impurity,maxDepth)
  }

  def predictDTResult(input:RDD[LabeledPoint]){}

}
