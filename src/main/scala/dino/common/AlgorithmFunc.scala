package dino.common

import breeze.linalg.DenseVector

/**
 * Created by Dino on 2016/9/21.
 *
 */
object AlgorithmFunc {

  // 计算breeze中的vector的向量之间的余弦
  def cosine(v1:DenseVector[Double],v2:DenseVector[Double]): Double ={
    val x:Double =v1.dot(v2)
    x / norm2(v1)* norm2(v2)
  }
  //计算向量的L2范数
  def norm2(v1:DenseVector[Double]): Double ={
    Math.sqrt(v1.toArray.map(x=>x*x).sum)
  }

  def main(args:Array[String]): Unit ={
    val  a1 = Array(1.0,2.0,3.0)
    val v1 = new DenseVector(a1)
    println( cosine(v1,v1))


}

}
