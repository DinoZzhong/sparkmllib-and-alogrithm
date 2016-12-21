package dino.test.matrix

import dino.common.traits.defaultSession
import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.linalg.distributed.{IndexedRow, IndexedRowMatrix, RowMatrix}
import org.apache.spark.mllib.linalg.Vectors
/**
 * Created by Dino on 2016/9/13.
 *
 *  rowMatrix 无法按照行号访问
 */
object RowmatriTest {

  val sc = defaultSession.getSparkSession().sparkContext
  Logger.getRootLogger.setLevel(Level.WARN)

  val rdd1 = sc.parallelize(Array(Array(1.0,2.0,3.0,4.0),Array(2.0,3.0,4.0,5.0)))
    .map(x=>Vectors.dense(x))
  val rm = new RowMatrix(rdd1)
  var coordinateMatrix = rm.columnSimilarities() //计算列之间的相似度
  coordinateMatrix.entries.collect()

  rm.numCols()
  rm.numRows()
  rm.rows.take(1).mkString("|")
  rm.columnSimilarities(0.5).toRowMatrix().rows.collect()
  rm.columnSimilarities()
  val mat = rm.computeColumnSummaryStatistics() // 包含矩阵的常见统计信息
  mat.count
  mat.max
  mat.mean
  mat.normL1
  mat.normL2
  mat.numNonzeros // 每一列的非零对象

  val rdd2 = sc.parallelize(
    Array(
      Array(1.0,2.0,3.0,4.0)
      ,Array(2.0,3.0,4.0,5.0)
      ,Array(10.0,7.0,6.0,0.0)
  ))
  var i:Long =0L
 //  可以根据索引获取行
  val im = new IndexedRowMatrix(rdd2.map{x=>
    IndexedRow(x(0).toLong,Vectors.dense(x))
  })
  im.rows.filter(f=>f.index==2.0).collect()
  im.rows.collect().foreach(x=>println(x.formatted("|")))
}

