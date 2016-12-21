package dino.recomm

import org.jblas.DoubleMatrix

/**
 * Created by Dino on 2016/9/21.
 * 求余弦测试，利用org.jblas包
 *
 * 余弦夹角可以确认相似度 如果接近1表示趋向于一致
 *    夹角相似  两个向量内积(点积)除以向量长度之积
 *     向量长度计算  个元素平方和开根号
 */
object CosineTest {
  def cosineSimilarity(v1:DoubleMatrix,v2:DoubleMatrix): Double ={
    println(v1.dot(v2))
    println(v1.norm1() +"   "+ v1.norm2())
    v1.dot(v2)/(v1.norm2()*v2.norm2())
  }
  def main(args:Array[String]): Unit ={
    val a1 = Array(1.0,2.0,3.0)

    val v1 = new DoubleMatrix(a1)
    println(cosineSimilarity(v1,v1))

    println(Math.sqrt(14.0))
  }
}
