package dino.test.saprkfunctions

import dino.common.traits.defaultSession
import scala.collection.mutable.Map
/**
 * Created by Dino on 2016/10/5.
 *
 */
object TestCombineByKey {
  def main(args:Array[String]): Unit ={
    val sc = defaultSession.getInstance().sparkContext


    val a = Array(("hello", "world"),("hello", "ketty"),("hello", "Tom"),("Sam", "love"),("Sam", "sorry"),("Tom", "big"),("Tom", "shy"))

    val outInfo = sc.parallelize(Seq(a)).flatMap(x=>x).map(x=>(x._1,x._2))
    outInfo.combineByKey(createCombiner,mergeValue,mergeCombines).foreach(println)
    /*
    (Sam,(love|sorry,2))
    (Tom,(big|shy,2))
    (hello,(world|ketty|Tom,3))
    * */
    outInfo.combineByKey(
      (v:String)=>(v,1)
      ,(x:(String,Int),y)=>(x._1+"|"+y,x._2+1)
      ,(x:(String,Int),y:(String,Int))=>(x._1+"|"+y._1,x._2+y._2)
    )

  }
  val createCombiner=(x:String)=>(x,1)  //按照分区设置，每个分区中如果不存在就创建对应的key的初始化对象，这里设置为 (value,1)
  val mergeValue =(x:(String,Int),y:String)=>(x._1+"|"+y,x._2+1)    // 对应key存在了初始化对象，对该value进行处理，这里使用树杠连接，且计数加1
  val mergeCombines =(x:(String,Int),y:(String,Int))=>(x._1+"|"+y._1,x._2+y._2) // 合并每个分区的初始化对象

}
