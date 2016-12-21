package dino

import dino.common.traits.defaultSession

/**
 * Created by Dino on 2016/11/26.
 *
 */
object CoreUserLogCheck {
  def main(args:Array[String]): Unit ={
    val sc = defaultSession.getSparkSession().sparkContext

    val coredata=sc.textFile("/user/flume/coreuser/data_date=2016112605")

    val lens = coredata.filter(_.trim!="").map{x=>
      val s= x.split("\t")
      (s.size.toString,1)
    }.reduceByKey(_+_).foreach(println)
/**
(5,1)
(23,11)
(35,9019629)
(1,3940383)
(27,360)
(9,361)
 * */
   val oneLens = coredata.filter(_!="").map{x=>
      val s = x.split("\t")
      (s.size,s)
    }.filter(_._1==1).cache()
  }

}
