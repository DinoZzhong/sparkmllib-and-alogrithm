package dino.recomm

import dino.common.traits.defaultSession

import scala.collection.mutable.ArrayBuffer

/**
 * Created by Dino on 2016/9/26.
 *
 * 每个用户对物品的评分，该评分可以确认不同物品之间的相似性
 *
 * 这里的相似度计算用的是 朴素相似度 即 1/(1+欧式距离)
 *    欧式距离为为sqrt((xi-yi)²)
 *
 * 另有相似度为： 余弦相似度、jaccard相似度
 */
object ItemBase {
  def main(args:Array[String]): Unit ={
    val sc = defaultSession.getInstance().sparkContext
    val re = "^[0-9]".r
    val dataClean = sc.textFile("data/itemBase.txt")
      .filter(x=>re.findFirstIn(x)!=None)
      .map(x=> {
        val tokens = x.split(",")
        (tokens(0).toLong,(tokens(1).toLong,if(tokens.size>2)tokens(2).toFloat else 0f))
      }).aggregateByKey(Array[(Long, Float)]())(_ :+ _, _ ++ _).values

    val norms = dataClean.flatMap(_.map(y=>(y._1,y._2 * y._2))).reduceByKey(_+_)
    norms.collectAsMap().foreach(x=>println("norms:"+x._1+" "+x._2))
    val normsBC = sc.broadcast(norms.collectAsMap())

    //共生矩阵
    val matrix1 = dataClean.map(list =>list.sortWith(_._1>_._1))
    matrix1.foreach(x=>println("==="+x.mkString("|")))
    val matrix= matrix1.flatMap(occMatrix).reduceByKey(_+_)

    matrix.foreach(x=>println("matrix:"+x._1._1+" "+x._1._2+"  "+x._2))
    // 计算相似度
    val sim = matrix.map(a=>(a._1._1,(a._1._2,1/(1+Math.sqrt(normsBC.value.get(a._1._1).get
      + normsBC.value.get(a._1._2).get - 2* a._2))
      )))
    sim.collect().foreach(println)
    sc.stop()
  }
  def occMatrix(a:Array[(Long,Float)]):ArrayBuffer[((Long,Long),Float)] ={
    val array =ArrayBuffer[((Long,Long),Float)]()
    println(a.size+"  "+a.mkString("|"))
    for(i <- 0 to(a.size -1);j<-(i+1)to(a.size-1)){
      println(i+" "+j)
      array += (((a(i)._1,a(j)._1),a(i)._2*a(j)._2))
    }
    array
  }

}
