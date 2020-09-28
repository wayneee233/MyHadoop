package com.lagou.mr.homework;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class SortDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //1. 获取配置⽂件对象，获取job对象实例
        final Configuration conf = new Configuration();
        final Job job = Job.getInstance(conf, "SortDriver");
        //2. 指定程序jar的本地路径
        job.setJarByClass(SortDriver.class);
        //3. 指定Mapper/Reducer类
        job.setMapperClass(SortMapper.class);
        job.setReducerClass(SortReducer.class);
        //4. 指定Mapper输出的kv数据类型
        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(NullWritable.class);
        //5. 指定最终输出的kv数据类型 reduce输出的数据类型
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(IntWritable.class);
        //6. 指定job处理的原始数据路径 通过fileInput指定
        FileInputFormat.setInputPaths(job, new Path("C:\\homework\\mr"));
        //7. 指定job输出结果路径
        FileOutputFormat.setOutputPath(job, new Path("C:\\homework\\mr\\output"));
        //8. 提交作业
        final boolean flag = job.waitForCompletion(true);

        System.exit(flag ? 0 : 1);

    }
}
