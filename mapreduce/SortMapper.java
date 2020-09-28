package com.lagou.mr.homework;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class SortMapper extends Mapper<LongWritable, Text, IntWritable, NullWritable> {
    // 重写map方法
    IntWritable t = new IntWritable();
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        final String[] numbers = value.toString().split("\n");
        //map输出每个数据不需要value
        for (String num : numbers) {
            t.set(Integer.valueOf(num));
            context.write(t,NullWritable.get());
        }
    }
}
