package com.lagou.mr.homework;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
/*
获取map输出的内容并依照大小且进行  升序排序
 */
public class SortReducer extends Reducer<IntWritable,NullWritable,IntWritable,IntWritable> {
    //初始序号为1
    private IntWritable num = new IntWritable(1);
    //reduce方法重写

    @Override
    protected void reduce(IntWritable key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        for (NullWritable value : values) {
            context.write(num, key);
            num = new IntWritable(num.get() + 1);
        }
    }
}
