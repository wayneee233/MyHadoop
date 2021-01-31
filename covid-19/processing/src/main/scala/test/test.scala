package test

import org.apache.spark.{SparkConf, SparkContext}

import java.sql.{Connection, DriverManager, PreparedStatement}

/**
 * @Author jiang.weiyu
 * @Date 2021/01/30 23:31
 */
object test {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("test").setMaster("local[*]")
    val sc = new SparkContext(conf)
    var conn: Connection = null
    var ps: PreparedStatement = null
    val sql = "insert into  "
    try {
      conn = DriverManager.getConnection("jdbc:mysql://47.74.5.215:3306/mybatis_db?characterEncoding=UTF-8", "root", "12345678")
      ps = conn.prepareStatement(sql)
      val rs = ps.executeQuery()
      //直接获取
      while (rs.next()) {
        println(rs.getString("name_jp") /*+"-------"+rs.getInt("npatients")*/);
      }
    } catch {
      case e: Exception => println("myException")
    } finally {
      if (conn != null)
        conn.close()
      if (ps != null) {
        ps.close()
      }
    }
    sc.stop()
  }
}
