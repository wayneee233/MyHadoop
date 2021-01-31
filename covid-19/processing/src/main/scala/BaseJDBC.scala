
import bean.CovidData
import org.apache.spark.sql.{ForeachWriter, Row}

import java.sql.{Connection, DriverManager, PreparedStatement}

abstract class BaseJDBC(sql: String) extends ForeachWriter[CovidData] {

  def realProcess(str: String, row: CovidData)

  var conn: Connection = _
  var ps: PreparedStatement = _

  // 开启连接
  override def open(partitionId: Long, epochId: Long): Boolean = {
    conn = DriverManager.getConnection("jdbc:mysql://47.74.5.215:3306/mybatis_db?characterEncoding=UTF-8", "root", "12345678")
    true
  }

  override def process(value: CovidData): Unit = {
    realProcess(sql, value)
  }

  override def close(errorOrNull: scala.Throwable): Unit = {
    if (conn != null) {
      conn.close()
    }
    if (ps != null) {
      ps.close()
    }
  }
}
