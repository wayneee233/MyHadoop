package com.siyun.mr.wcsort;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

// 封装任务并提交运行
public class WordCountDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        /*
        1. 获取配置⽂件对象，获取job对象实例
        2. 指定程序jar的本地路径
        3. 指定Mapper/Reducer类
        4. 指定Mapper输出的kv数据类型
        5. 指定最终输出的kv数据类型
        6. 指定job处理的原始数据路径
        7. 指定job输出结果路径
        8. 提交作业
         */

        //1. 获取配置⽂件对象，获取job对象实例
        final Configuration conf = new Configuration();
        final Job job = Job.getInstance(conf, "WordCountDriver");
        //2. 指定程序jar的本地路径
        job.setJarByClass(WordCountDriver.class);
        //3. 指定Mapper/Reducer类
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);
        //4. 指定Mapper输出的kv数据类型
        job.setMapOutputKeyClass(WordCountBean.class);
        job.setMapOutputValueClass(NullWritable.class);
        //5. 指定最终输出的kv数据类型 reduce输出的数据类型
        job.setOutputKeyClass(WordCountBean.class);
        job.setOutputValueClass(NullWritable.class);
        //5.1 使用combiner组件
        //6. 指定job处理的原始数据路径 通过fileInput指定
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        //7. 指定job输出结果路径
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        //8. 提交作业
        final boolean flag = job.waitForCompletion(true);
           System.exit(flag ? 0 : 1);
    }
}
