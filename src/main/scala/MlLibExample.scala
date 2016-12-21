import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.linalg.{Vector, Vectors}
import org.apache.spark.ml.param.{Param, ParamMap}

import org.apache.spark.sql.{Row, SparkSession}
/**
 * 逻辑回归 处理二值分类问题,二值为 {0,1}
 * */

object MlLibExample{
  def main(args:Array[String]): Unit ={

    val spark = SparkSession.builder().appName("ML Pipeline Test")
      .config("spark.sql.warehouse.dir", "file:///c:/tmp/spark-warehouse")
      .master("local[12]")
      .getOrCreate()
    val training = spark.createDataFrame(Seq(
      (1.0,Vectors.dense(0.0,1.1,0.1)),
      (0.0,Vectors.dense(2.0,1.0,-1.0)),
      (0.0,Vectors.dense(2.0,1.3,1.0)),
      (1.0,Vectors.dense(0.0,1.2,-0.5))
    )).toDF("label","features")

    val lr = new LogisticRegression() // 创建一个实例
    //println("LogisticRegression parameters:\n"+lr.explainParams()+"\n") // 查看默认的参数设置

    lr.setMaxIter(10).setRegParam(0.01) // 使用setter修改参数

    val model1 = lr.fit(training)
  //  println("model 1 was fit using parameters:"+model1.parent.extractParamMap()) // 打印参数

    val paramMap = ParamMap(lr.maxIter->20)  // 通过创建map来设置参数
      .put(lr.maxIter,30)
      .put(lr.regParam->0.1,lr.threshold->0.55)

    val paramMap2 = ParamMap(lr.probabilityCol->"myProbability")

    val paramMapCombined = paramMap++paramMap2 // 参数合并


    val model2 = lr.fit(training,paramMapCombined) // 指定参数合并,这里会覆盖之前的参数
   // println("model 2 was fit using parameters:"+model2.parent.explainParams())

    val test = spark.createDataFrame(Seq(
      (1.0,Vectors.dense(-1.0,1.5,1.3)),
      (0.0,Vectors.dense(3.0,2.0,-0.1)),
      (1.0,Vectors.dense(0.0,2.2,-1.5))
    )).toDF("label","features")


    model2.transform(test)
    .select("features","label","myProbability","prediction")
    .collect().foreach{case Row(features:Vector,label:Double,prob:Vector,prediction:Double)=>
        println(s"mode2 ($features,$label)-prob=$prob,predition=$prediction")
      }
    model1.transform(test)
      .select("features","label","probability","prediction")
      .collect().foreach{case Row(features:Vector,label:Double,prob:Vector,prediction:Double)=>
      println(s"mode1 ($features,$label)-prob=$prob,predition=$prediction")
    }
    spark.stop()
  }
}