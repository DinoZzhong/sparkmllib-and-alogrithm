package dino.test.blas

import breeze.linalg.DenseVector


/**
 * Created by Dino on 2016/9/7.
 *
 * 矢量-矢量运算  点几，加法乘法 绝对值
 * 矩阵-矢量  矩阵向量乘法
 * 矩阵-矩阵  矩阵乘法
 *
 * 每一种函数操作都区分不同的数据类型(单精度 双精度 复数)
 * 矩阵矩阵乘法SGEMM  向量常数SAXPY
 */
object BlasPrimary {
  def main(args:Array[String]): Unit ={
    val a1 = DenseVector(1.0,1.2,3.6)
    val a2 = DenseVector(1.0,2.0,3.6)
    val a3 = DenseVector(1,4,6,7)
  }

}
