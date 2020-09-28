package com.siyun.mr.wcsort;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

// reducer类有四个泛型参数，2对KV
// 第一对KV要与Mapper输出类一致；Text，IntWritable
// 第二对KV自己设计决定输出的结果数据是什么类型Text，IntWritable

public class WordCountReducer extends Reducer<WordCountBean, NullWritable,WordCountBean, NullWritable> {
    //1 重写Reducer方法

    //Text key：Mapper输出的key，本案列终究是单词
    //Iterable<IntWritable> values: 一组key相同的KV的value组成的集合
    /*
    假设map方法；hello 1；hello 1；hello 1
    reduce的key和value是什么
    key：hello；values： 3

    假设map方法；hello 1；mapreduce 1；hadoop 1
    reduce的key和value是什么？
    reduce方法合适调用：一组key相同的kv中的value组成集合然后调用
    第一次： key：hello values<1>
    第二次： key：。。。。。。
    。。。。。。。。。。
     */
    @Override
    protected void reduce(WordCountBean key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        for (NullWritable value : values) {
            context.write(key, value);
        }
    }

}
