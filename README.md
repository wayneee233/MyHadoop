# **Hadoop学習**

## **AliCloud**

#### **ECS**

47.91.16.103  linux121  NN,DN,Impala-server,Metastore

47.91.21.253  linux122  DN,Impala-server,Hive[beeline(./beeline),HUE(./supervisor)

47.74.5.215    linux123  2NN,DN,ResourceManager,Mysql,Hive,Impala,HiveServer2,Metastore 


#### SSH

-  NNを他のサーバに接続まだ要求するため、SSHの設定を行う

- ssh-keygen -t rsa -P " "  ras アルゴリズムで暗号を生成 " " null

- cd ~/.ssh  cat id_rsa.pub >>  authorized_keys

  生成したrsa暗号をauthorized_keysに保存

- scp authorized_keys linux121:/root/.ssh/ 

  scp authorized_keys linux123:/root/.ssh/

  他の両サーバに認証を送信。(暗号を一回のみ使用)

#### **Security Group**

**VCP**



## **Hadoopのモード**

完全分散モードFully-Distributed Mode

- 多数の装置でクラスタを構成し、分散保存・分散演算のすべての機能を実行。

1. linux121  namenode(nn)
2. linux122  2nn,datanode
3. linux123   datanode

#### **HDFSの構造**

- FS(file system)

  - HDFSに保存されるファイルは128MB(2.xバージョン~)のブロックに分けられて、分散されたサーバーに保存。

  - 200MBの場合　128MB 72MB二つのブロックになります

- Master/Slave Architecture

  - Master--NN(NameNode)  

    クライアントの要求を受け取る、メタデータ管理、データノードモニタリング、ブロック管理

  - Slave--DN(DataNode)

    クライアントがHDFSに保存するファイルをメタデータ(生成日、ファイル名など)とrowデータ(実際のデータ)に分けて、ローカルディスクに保存

  - Checkpointing Server--2NN(Secondary NameNode)

    NameNodeのメタデータのfsImage(File System Image)を周期的に3600sに更新、NameNodeの負担を軽くする

#### **MapReduce**

Map Shuffle Sort Reduce 

**Shuffle** **phase** in Hadoop transfers the map output from Mapper to a Reducer in MapReduce

**Sort phase** in MapReduce covers the merging and sorting of map outputs.

**Customize**

```java
// slipt 
System.out.println(str.replaceAll("\\p{P}", ""));         //Removes Special characters only
System.out.println(str.replaceAll("[^a-zA-Z]", ""));      //Removes space, Special Characters and digits
System.out.println(str.replaceAll("[^a-zA-Z\\s]", ""));   //Removes Special Characters and Digits
System.out.println(str.replaceAll("\\s+", ""));           //Remove spaces only
System.out.println(str.replaceAll("\\p{Punct}", ""));     //Removes Special characters only
System.out.println(str.replaceAll("\\W", ""));            //Removes space, Special Characters but not digits
System.out.println(str.replaceAll("\\p{Punct}+", ""));    //Removes Special characters only
System.out.println(str.replaceAll("\\p{Punct}|\\d", "")); //Removes Special Characters and Digits
```

## **Hive**

#### **基本**

hive --SQLをMapRedeuceタスクにチェンジするtool

1.CLI(Common Line Interface)

2.Thrift Server

3.MetaStore --jdbc:mysql://linux123:3306/hivemetadata?createDatabaseIfNotExist=true&amp;useSSL=fals

4.Driver --com.mysql.jdbc.Driver

#### **最適化**

Hive Engine:MapReduce、Tez、Spark、Flink

**Vectorization**  

(一回1024行)　File format ORC

set hive.vectorized.execution.enabled = true; 

set hive.vectorized.execution.reduce.enabled = true;

**JVM Reuse** 

Hadoopでmap or reduce を一回実行するとJVM一回起動されます、

数秒のMapまだReduceのタスクでJVM起動の時間がタスク実行より長くなってしまうため。

SET mapreduce.job.jvm.numtasks=5;

**count(distinct)** 

交流が低いため group by count(1)

## **Impala**

#### **Local Yum Repository**

**Backgroud**

Impalaをインストールするとき、rpmパッケージしかないため(依頼の配置が面倒)、そのためlocal yum resourceを作成します。

**Sepcific steps**

yum install -y httpd

systemctl start httpd

symbolic link:ln -s /cdh/5.7.6 /var/www/html/cdh57

http://47.91.16.103:80/cdh57
