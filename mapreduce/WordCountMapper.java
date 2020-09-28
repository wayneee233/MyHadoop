package com.siyun.mr.wcsort;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

//1.继承Mapper类
//2.Mapper类的泛型参数；共四个，2对kv
//2.1 第一对KV：map输入的参数类型
//2.2 第二对KV；map输出的参数类型
// LongWritable，Text -->文本偏移量，一行文本内容
// Text，IntWriteable -->单词，1
public class WordCountMapper extends Mapper<LongWritable, Text, WordCountBean, NullWritable>{
    //3 重写Mapper类的map方法
    /*
    1 接受到文本内容，转为String类型
    2 按照空格进行切分
    3 输出<单词，1>
     */
    // LongWritable，Text -->文本偏移量，一行文本内容
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //1 重写map方法，接受文本内容强转为String
        final String[] str;
        str = value.toString().split("\n");

        for (int i=0;i<str.length;i++){
            String[] fields = str[i].split("\\s+");
            //3 输出
            WordCountBean bean = new WordCountBean(fields[0],Long.parseLong(fields[1]));
            context.write(bean,NullWritable.get());
        }
    }
}
