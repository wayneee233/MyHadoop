# Azkaban作业

### **作业需求**

现有用户点击行为数据文件，每天产生会上传到hdfs目录，按天区分目录，现在我们需要每天凌晨两点定时导入Hive表指定分区中，并统计出今日活跃用户数插入指标表中。

日志文件 clicklog

```txt
## 用户点击行为数据，三个字段是用户id,点击时间，访问页面
userId   click_time             index
uid1	2020-06-21	12:10:10	a.html 
uid2	2020-06-21	12:15:10	b.html 
uid1	2020-06-21	13:10:10	c.html 
uid1	2020-06-21	15:10:10	d.html 
uid2	2020-06-21	18:10:10	e.html
```

hdfs目录会以日期划分文件，例如：

```shell
/user_clicks/20200621/clicklog.dat
/user_clicks/20200622/clicklog.dat
/user_clicks/20200623/clicklog.dat
...
```

创建Hive表

原始数据分区表

```shell
create table user_clicks(id string,click_time string index string) partitioned by(dt string) row format delimited fields terminated by '\t' ;
```

需要开发一个import.job每日从hdfs对应日期目录下同步数据到该表指定分区。（日期格式同上或者自定义）

指标表

```shell
create table user_info(active_num string,date string) row format delimited fields terminated by '\t' ;
```

需要开发一个analysis.job依赖import.job执行，统计出每日活跃用户(一个用户出现多次算作一次)数并插入user_inof表中。

#### **作业**

开发以上提到的两个job，job文件内容和sql内容需分开展示，并能使用azkaban调度执行。



### **实现步骤**

1. 导入数据脚本

```
## import.job
type=command
command=sh /root/shells/load_dat.sh
```

1. 1从hdfs导入数据到hive表的脚本

[root@linux121 ~]# vim /root/shells/load_dat.sh

```shell
#!/bin/bash

## 定义前一天的源文件路径
last_day=`date -d "1 day ago" '+%Y%m%d'`
OLD_LOG=/user_clicks/$last_day/clicklog.dat
sql_file=/root/job/load_dat.sql

## 不存在就创建
if [[ ! -e $sql_file ]];then
    mkdir -p /root/job
    touch /root/job/load_dat.sql
fi

## 编写一个获取数据的.sql
sql='load data inpath "'${OLD_LOG}'" overwrite into table default.user_clicks partition(dt='$last_day');'
echo $sql > ${sql_file}

## hive执行.sql脚本
${HIVE_HOME}/bin/hive -f /root/job/load_dat.sql
```



2. 分析用户数据的脚本

[root@linux121 ~]# vim /root/shells/analysis.sh

```shell
#!/bin/bash

## 各种定义
last_day=`date -d "1 day ago" '+%Y%m%d'`
sql_file=/root/job/analysis.sql

## sql_file不存在就创建
if [[ ! -e $sql_file ]];then
    mkdir -p /root/job
    touch /root/job/analysis.sql
fi

## 编写分析数据的sql（按日活找出用户插入user_info table中）
sql='insert into table defult.user_info(active_num,dte) select id, click_time from default.user_click where dt='$last_day' group by click_time,id;'
echo $sql > ${sql_file}

## hive执行sql脚本
${HIVE_HOME}/bin/hive -f /root/job/analysis.sql
```

2. 1analysis.jop

```
type=command
dependencies=import
command=sh /root/shells/analysis.sh
```

3. 打包上传

<img src="https://gitee.com/jiangweiyu/typora-pic/raw/master/20201013232944.jpg" style="zoom: 33%;" />



### **结果验证**

执行成功

<img src="https://gitee.com/jiangweiyu/typora-pic/raw/master/20201014034020.jpg" style="zoom: 50%;" />

查看结果

<img src="https://gitee.com/jiangweiyu/typora-pic/raw/master/20201014034742.jpg" style="zoom: 50%;" />

<img src="https://gitee.com/jiangweiyu/typora-pic/raw/master/20201014034928.jpg" style="zoom: 50%;" />