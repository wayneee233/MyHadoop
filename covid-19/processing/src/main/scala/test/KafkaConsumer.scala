package test

import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer

import java.util.{Collections, Properties}

/**
 * @Author jiang.weiyu
 * @Date 2021/01/30 9:02
 */
object KafkaConsumer {
  def main(args: Array[String]): Unit = {
    // 配置信息
    val prop = new Properties
    prop.put("bootstrap.servers", "47.74.5.215:9092")
    // 指定消费组
    prop.put("group.id", "myKafka")
    // 指定消费位置
    prop.put("auto.offset.reset", "latest")
    // 指定消费的key的反序列化方式
    prop.put("key.deserializer", classOf[StringDeserializer])
    // 指定消费的value的反序列化方式
    prop.put("value.deserializer", classOf[StringDeserializer])
    prop.put("enable.auto.commit", "true")
    prop.put("session.timeout.ms", "30000")

    val kafkaConsumer = new KafkaConsumer[String, String](prop)
    // 订阅topic
    kafkaConsumer.subscribe(Collections.singletonList("covid19"))

    //开始消费
    while (true) {
      // 如果Kafak中没有消息，会隔timeout这个值读一次
      //val duration = new Duration(100)
      val data: ConsumerRecords[String, String] = kafkaConsumer.poll(200)

      val iter = data.iterator()
      while (iter.hasNext) {
        val value = iter.next()
        println("消费消息" + s"partition: ${value.partition()}, offset: ${value.offset()}, key: ${value.key()}, value: ${value.value()}")
      }
    }
  }
}
