package dino.common.traits

import org.apache.spark.sql.SparkSession

/**
 * Created by Dino on 2016/8/27.
 *
 */
trait SparkConfigSingleton {
  @transient var session:SparkSession = _
  val lockID=1L
  def getInstance(): SparkSession ={
    if(session == null){
      session = getSparkSession()
    }
     session
  }

  def getSparkSession(): SparkSession ={
    val spark = SparkSession.builder().appName(getClassName)
      .config("spark.sql.warehouse.dir", "file:///c:/tmp/spark-warehouse")
      .master("local[12]")
      .getOrCreate()
    spark
  }


  /**
   * 继承的类名,当做key去数据库查询对应的结果
   **/
  val getClassName = {
    this.getClass.getName.split("\\$")(0).split("\\.").last
  }

}

object defaultSession extends SparkConfigSingleton
