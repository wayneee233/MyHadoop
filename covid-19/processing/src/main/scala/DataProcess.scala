import bean.CovidData
import com.alibaba.fastjson.JSON
import org.apache.spark.SparkContext
import org.apache.spark.sql.streaming.Trigger
import org.apache.spark.sql.{Dataset, Row, SparkSession}

import scala.collection.JavaConverters._

object DataProcess {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().master("local[*]").appName("DataProcess").getOrCreate()
    val sc: SparkContext = spark.sparkContext
    sc.setLogLevel("WARN")

    import spark.implicits._
    import org.apache.spark.sql.functions._
    import collection.JavaConverters._

    // Subscribe to 1 topic
    val df = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "47.74.5.215:9092")
      .option("subscribe", "covid19")
      .load()

    // df >> ds
    val jsonStrDs: Dataset[String] = df.selectExpr("CAST(value AS STRING)")
      .as[String]

    // 将json转成bean类
    val covidDataDS: Dataset[CovidData] = jsonStrDs.map(jsonStr => {
      JSON.parseObject(jsonStr, classOf[CovidData])
    })

    // 分离各地的数据
    val provinceData = covidDataDS
      .select('date,'name_jp,'npatients)
      .orderBy('date)

    val dailyData = covidDataDS
      .select('name_jp)
      .groupBy('date)
      .agg(sum('npatients) as "patients")


    val query = covidDataDS.writeStream
      .foreach(
        new BaseJDBC("replace into covid19(date,name_jp,npatients) values(?,?,?)") {
          override def realProcess(sql: String, row: CovidData): Unit = {
            val date = row.date
            val name_jp = row.name_jp
            val npatients = row.npatients

            println(date + "\t" + name_jp + "\t" + npatients)
            ps = conn.prepareStatement(sql)
            ps.setDate(1, date)
            ps.setString(2, name_jp)
            ps.setInt(3, npatients)
            ps.executeUpdate()
          }
        })
      // 输出模式 1.append 默认 只输出新增 只支持简单的查询不支持聚合
      //        2.complete 完整输出 所有数据都会输出 必须包含聚合
      //        3.update 只输出变换的数据
      .outputMode("append") //输出模式 默认append显示新增
      .trigger(Trigger.ProcessingTime(0))
      .option("truncate", value = false) // 列名过长不会阶段
      .start()
    query
      .awaitTermination()
  }
}
