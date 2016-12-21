package dino.test.breeze

import breeze.linalg._
import breeze.numerics.{round, ceil, floor}

/**
 * Created by Dino on 2016/9/6.
 * Breeze函数处理
 */
object BreezePrimary {
  def main(args:Array[String]): Unit ={
    val m0 =DenseMatrix.zeros[Double](2,3) //全零矩阵
    val m1 = DenseMatrix.ones[Double](3,5)

    val m5 = new DenseMatrix(2,3,Array(11,22,33,12,13,15))
    val v1 =DenseVector.zeros[Int](5) // 向量
    val v2 =DenseVector.range(1,10,2)

    val v3 = DenseVector(1,2,3,4,5).t  // 向量行转列

    val v4 =  DenseVector.tabulate(3)(i=>2*i)


    // ---- 指定位置获取属性
    val a = DenseVector(1,2,3,4,5,6,7,8,9,10)
    a(0)
    a(1 to 4)
    a(1 to -1)
    a(5 to 0 by -1 ) //DenseVector(6, 5, 4, 3, 2, 1)  下标第五个开始到第0个，-1 表示从后向前
    accumulate(a) //  累积和

    // -- 矩阵操作
    //val m = DenseMatrix((1.0,2.0,3.0),(3.0,4.0,5.0))  //两行三列
    val  m = DenseMatrix((1.0,2.0,3.0),(4.0,5.0,6.0),(7.0,8.0,9.0))
    m.reshape(3,3)// 转2列三行
    m.toDenseVector // 转向量
    m(1 to 2,1 to 2) // 行超出长度
    val mm = DenseMatrix((11.0,22.0,33.0),(44.0,55.0,66.0)) // 问题 转数据类型
    DenseMatrix.vertcat(m,mm)
    lowerTriangular(m) // 下三角，即对角线以上都置为默认值0.0
    upperTriangular(m)
    diag(m) // 取对角线向量


    //  -- 矩阵计数
    sum(m)
    sum(m,Axis._0)
    max(m)  // 返回最大值
    argmax(m) //返回最大值行列坐标
    trace(m) // 对角线元素和

    m(1 to 2,1 to 2):=5.0  // 重新赋值，会修改当前的矩阵

    val b1  = DenseVector(1,2,3,4)
    val b2 = DenseVector(1,1,1,1)
    DenseVector.vertcat(b1,b2)// 向量合并


    // 数值计算
    val  x = DenseMatrix((1.0,2.0,3.0),(4.0,5.0,6.0))
    val y =DenseMatrix((1.0,1.0,1.0),(2.0,2.0,2.0))
    x+y  // 元素加法
    x.t  //矩阵倒置
    x:*y  // 元素乘积
    x:/y //元素除法
    x:>y // 元素比较,返回boolean矩阵
    x:==y
    x:*=2.0  // 追乘 修改同一个矩阵
    x:+=1.0 //追加

    //线性代数

    val n = DenseMatrix.ones[Double](3,3)
    m\n //线性求解
    det(m) // 矩阵特征值
    m.size // 长度  行*列

    // 取整数
    val aa = DenseVector(1.2,0.6,-2.3)
    round(aa)
    ceil(aa)
    floor(aa)

    // 三角函数

    //对数 指数

  }
}
