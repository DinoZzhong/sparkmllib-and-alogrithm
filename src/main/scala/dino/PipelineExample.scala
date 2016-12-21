package dino

import dino.common.traits.defaultSession
import org.apache.spark.ml.{PipelineModel, Pipeline}
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.feature.{HashingTF, Tokenizer}
import org.apache.spark.ml.linalg.Vector
import org.apache.spark.sql.Row

/**
 * Created by Dino on 2016/9/4.
 *
 * 从本地文件中获取模型结果并进行结果测试
 *
 */
object PipelineExample {
  def main(args:Array[String]): Unit ={

    val ss = defaultSession.getSparkSession()

 /*   val training = ss.createDataFrame(Seq(
      (0L, "a b c d e spark", 1.0),
      (1L, "b d", 0.0),
      (2L, "spark f g h", 1.0),
      (3L, "hadoop mapreduce", 0.0)
    )).toDF("id", "text", "label")


    val tokenizer= new Tokenizer()
      .setInputCol("text")
      .setOutputCol("words")

    val hashingTF = new HashingTF()
      .setNumFeatures(1000)
      .setInputCol(tokenizer.getOutputCol)
    .setOutputCol("features")

    val lr = new LogisticRegression()
      .setMaxIter(10)
      .setRegParam(0.01)

    val pipeline = new Pipeline()
      .setStages(Array(tokenizer,hashingTF,lr))*/

/*    val model = pipeline.fit(training)

    model.write.overwrite().save("/tmp/spark-logistic-regession-model")

    pipeline.write.overwrite().save("/tmp/unfit-lr-model")*/

    val sameModel = PipelineModel.load("/tmp/spark-logistic-regession-model")


    val test = Seq((4L,"spark ij ks"),(5L,"l m b"),
      (6l,"mapreduce spark"),(7L,"apache hadoop"))

    val testDF = ss
      .createDataFrame(test).toDF("id","text")

/*    model.transform(testDF)
    .select("id","text","probability","prediction")
    .collect()
      .foreach { case Row(id: Long, text: String, prob: Vector, prediction: Double) =>
      println(s"($id, $text) --> prob=$prob, prediction=$prediction")
    }*/
    /*  [4,spark ij ks,[0.5406433544852302,0.45935664551476996],0.0]
    [5,l m b,[0.9636243529994646,0.036375647000535465],0.0]
    [6,mapreduce spark,[0.7799076868204318,0.22009231317956823],0.0]
    [7,apache hadoop,[0.9768636139518375,0.023136386048162483],0.0]*/
  println("====================================")
    sameModel.transform(testDF)
      .select("id","text","probability","prediction")
      .collect().foreach(println)


    ss.stop()
  }
}
