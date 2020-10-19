### 作业：RedisCluster的安装、部署、扩容和 Java客户端调用

![](https://gitee.com/jiangweiyu/typora-pic/raw/master/20201019203406.jpg)

如图

#### 搭建Redis5.0集群，要求三主三从，记录下安装步骤

1. 下载redis

   ```shell
   cd /opt/lagou/software
   wget http://download.redis.io/releases/redis-5.0.5.tar.gz
   ```

2. 编译安装Redis

   ```shell
   # 先安装gcc编译器
   yum -y install gcc
   # 解压redis安装
   tar -zxvf /opt/lagou/software/redis-5.0.5.tar.gz -C /usr/redis
   cd /usr/redis
   make
   ```

3. 创建节点

   ```shell
   mkdir /usr/redis-cluster
   cd /usr/redis-cluster
   # 将redis/bin/目录下redis.conf复制到9000
   mkdir 9000 9001
   cp /usr/redis/bin/redis.conf /usr/redis-cluster/9000
   ```

4. 依据端口号修改配置文件redis.conf

   ```shell
   # 开启网络，保证其他网络可以访问该机子
   bind 0.0.0.0
   # 端口号
   port 9000
   # 守护线程 ，后台运行redis
   daemonize yes
   pidfile /var/run/redis_9000.pid
   # aof日志每一个操作都记录模式
   appendonly yes
   # 开启集群模式
   cluster-enabled yes
   # 集群节点的配置
   cluster-config-file nodes_9000.conf
   ```

   复制上述文件到9001下

   ```shell
   cp /usr/redis-cluster/9000/redis.conf /usr/redis-cluster/9001
   ```

5. 分发/usr/redis    /usr/redis-cluster到另外两台机器

   ```shell
   rsync-script /usr/redis
   rsync-script /usr/redis-cluster
   ```

6. 启动每台机器上的9000 9001节点

   ```shell
   cd /usr/redis/bin
   redis-server /usr/redis-cluster/9000/redis.conf
   redis-server /usr/redis-cluster/9001/redis.conf
   ```

7. 查看结果

   ```shell
   ps -ef | grep redis
   ```

8. 创建集群

   ```shell
   # 三主三从
   cd /usr/redis/bin
   redis-cli --cluster create linux121:9000 linux122:9000 linux123:9000 linux121:9001 linux122:9001 linux123:9001 --cluster-replicas 1
   ```

9. 搭建成功

   ![搭建成功](https://gitee.com/jiangweiyu/typora-pic/raw/master/20201019223638.jpg)

   

#### 能够添加一主一从（Master4和Slaver4），记录下安装步骤

1. 添加节点

   ```shell
   cd /usr/redis-cluster
   mkdir 9002 9003
   ```

2. 复制redis.conf 到9002 9003下 并修改

   ```shell
   cp /usr/redis/bin/redis.conf /usr/redis-cluster/9002
   cp /usr/redis/bin/redis.conf /usr/redis-cluster/9003
   ```

   ```shell
   # 开启网络，保证其他网络可以访问该机子
   bind 0.0.0.0
   # 端口号
   port 9002
   # 守护线程 ，后台运行redis
   daemonize yes
   pidfile /var/run/redis_9002.pid
   # aof日志每一个操作都记录模式
   appendonly yes
   # 开启集群模式
   cluster-enabled yes
   # 集群节点的配置
   cluster-config-file nodes_9002.conf
   ```

3. 启动新节点Master4 Slaver4

   ```shell
   [root@linux123 bin]# ./redis-server /usr/redis-cluster/9002/redis.conf 
   1852:C 19 Oct 2020 21:55:20.786 # oO0OoO0OoO0Oo Redis is starting oO0OoO0OoO0Oo
   1852:C 19 Oct 2020 21:55:20.786 # Redis version=5.0.5, bits=64, commit=00000000, modified=0, pid=1852, just started
   1852:C 19 Oct 2020 21:55:20.786 # Configuration loaded
   [root@linux123 bin]# ./redis-server /usr/redis-cluster/9003/redis.conf 
   1857:C 19 Oct 2020 21:55:24.608 # oO0OoO0OoO0Oo Redis is starting oO0OoO0OoO0Oo
   1857:C 19 Oct 2020 21:55:24.608 # Redis version=5.0.5, bits=64, commit=00000000, modified=0, pid=1857, just started
   1857:C 19 Oct 2020 21:55:24.608 # Configuration loade
   ```

4. 添加新节点

   ```
   [root@linux121 bin]# ./redis-cli --cluster add-node 10.0.0.61:9002 10.0.0.61:9003
   
   ```

   

（3）能够通过JedisCluster向RedisCluster添加数据和取出数据

