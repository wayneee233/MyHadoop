  

### **基于Zookeeper实现简易版配置中心**

#### **要求实现以下功能：**

1. 创建一个Web项目，将数据库连接信息交给Zookeeper配置中心管理，即：当项目Web项目启动时，从Zookeeper进行MySQL配置参数的拉取
2. 要求项目通过数据库连接池访问MySQL（连接池可以自由选择熟悉的）
3. 当Zookeeper配置信息变化后Web项目自动感知，正确释放之前连接池，创建新的连接池



#### **解题思路**

1. mysql参数的获取：

   - 封装zookeeper节点下的mysql配置属性到PropertySource对象，然后把这个对象设置到environment

   - Autowired自动装配bean对象(这里是**application.properties**即**Environment**中的mysql属性)

2. 连接池

   - 通过连接池BasicDataSource类的set方法，将environment中的参数设置到连接池，并通过该类来获取Connection对象

3. 实时更新，自动感知

   - 应用上下文的ApplicationContext#getAutowireCapableBeanFactory()方法或者通过实现BeanFactoryAware，获取暴露的bean工厂

   

#### **实现步骤**

1.创建web项目zk_client，在pom.xml中添加依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.4.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.persol</groupId>
    <artifactId>demo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>zookeeper</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.12</version>
            <scope>provided</scope>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.apache.curator/curator-recipes -->
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-recipes</artifactId>
            <version>2.13.0</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.apache.curator/curator-framework -->
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-framework</artifactId>
            <version>2.13.0</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.apache.curator/curator-test -->
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-test</artifactId>
            <version>2.13.0</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
        </dependency>
        <!-- https://mvnrepository.com/artifact/commons-dbcp/commons-dbcp -->
        <dependency>
            <groupId>commons-dbcp</groupId>
            <artifactId>commons-dbcp</artifactId>
            <version>1.4</version>
        </dependency>

    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

2.实例化zkClinet这里用Zookeeper客户端Curator

```java
package com.persol.demo.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**

 * @Author way

 * @Date 2020/10/10

 * @Description utils
 */
@Slf4j
@Component
public class CuratorUtil {

    private static CuratorFramework client;
    private static String path = "/config";

    @Value("${zookeeper.client}")
    private String serverStr;

    @Autowired
    private Environment environment;

    @Autowired
    ConfigurableApplicationContext applicationContext;

    private static String mysqlPropertyName = "mysqlSource";

    private ConcurrentHashMap map = new ConcurrentHashMap();

    @PostConstruct
    public void init() throws Exception {
        //TODO 1.客户端出初始化
        client = CuratorFrameworkFactory
                .builder()
                .connectString(serverStr)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();

        client.start();

        Stat stat = client.checkExists().forPath(path);
        //TODO 2.判断节点是否存在
        if (stat == null) {
            //2.1不存在则新建
            client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).
                    forPath(path, "zookeeper config".getBytes());
            TimeUnit.SECONDS.sleep(1);
        } else {
            //2.2存在则加载节点里的parameter到spring容器中
            addNoteToSpringProperty(client, path);
        }
        //TODO 3.调用监听器
        pathChildrenCache(client,path);

    }

    private void addNoteToSpringProperty(CuratorFramework client, String path) throws Exception {
        //2.3封装zookeeper节点下的mysql配置属性到PropertySource对象，然后把这个对象设置到environment
        if (!checkExistsProperty()) {
            createMysqlProperty();
        }
        //2.4加载zookeeper里/config节点下的名为mysql属性值
        MutablePropertySources propertySources = applicationContext.getEnvironment().getPropertySources();
        PropertySource<?> propertySource = propertySources.get(mysqlPropertyName);

        ConcurrentHashMap mysqlSource = (ConcurrentHashMap) propertySource.getSource();
        List<String> strings = client.getChildren().forPath(path);
        for (String string : strings) {
            //2.5 获取mysql配置的每项属性值
            mysqlSource.put(string,new String (client.getData().forPath(path+"/"+string)));
        }

    }

    // 生成监听器，将结果实时告知environment
    private void pathChildrenCache(CuratorFramework client,String path) throws Exception {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client,path,false);
        pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);

        pathChildrenCache.getListenable().addListener((curatorFramework, pathChildrenCacheEvent) -> {
            switch (pathChildrenCacheEvent.getType()){
                case CHILD_ADDED:
                    log.info("增加了节点");
                    addEnv(pathChildrenCacheEvent.getData(),client);
                    break;
                case CHILD_UPDATED:
                    log.info("节点发生改变");
                    addEnv(pathChildrenCacheEvent.getData(),client);
                    break;
                case CHILD_REMOVED:
                    log.info("节点被删除");
                    delEnv(pathChildrenCacheEvent.getData());
                    break;
            }
        });

    }

    private void delEnv(ChildData data) {
        ChildData next = data;
        String childPath = next.getPath();

        MutablePropertySources propertySources = applicationContext.getEnvironment().getPropertySources();
        PropertySource<?> propertySource = propertySources.get(mysqlPropertyName);
        ConcurrentHashMap source = (ConcurrentHashMap)propertySource.getSource();
        source.remove(childPath.substring(path.length()+1));

    }

    private void addEnv(ChildData data, CuratorFramework client) throws Exception {
        ChildData next = data;
        String childPath = next.getPath();
        String result = new String(client.getData().forPath(childPath));

        MutablePropertySources propertySources = applicationContext.getEnvironment().getPropertySources();
        PropertySource<?> propertySource = propertySources.get(mysqlPropertyName);
        ConcurrentHashMap source = (ConcurrentHashMap)propertySource.getSource();

        source.put(childPath.substring(path.length() + 1),result);

    }

    private void createMysqlProperty() {
        MutablePropertySources propertySources = applicationContext.getEnvironment().getPropertySources();
        OriginTrackedMapPropertySource mysqlSource = new OriginTrackedMapPropertySource(mysqlPropertyName, map);
        propertySources.addLast(mysqlSource);
    }

    private boolean checkExistsProperty() {
        MutablePropertySources propertySources = applicationContext.getEnvironment().getPropertySources();
        for (PropertySource<?> propertySource : propertySources) {
            if (mysqlPropertyName.equalsIgnoreCase(propertySource.getName())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
```

3.创建一个controller

```java
package com.persol.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.dbcp.BasicDataSource;


@Slf4j
@RestController
public class ZooController {
    @Bean
    @RequestMapping("/getConfig")
    public String dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty("mysql.driver"));
        dataSource.setUrl(env.getProperty("mysql.url"));
        dataSource.setUsername(env.getProperty("mysql.username"));
        dataSource.setPassword(env.getProperty("mysql.password"));

        log.info(String.valueOf(dataSource));
        return dataSource.getDriverClassName() +" " + dataSource.getUrl() +
                " " + dataSource.getUsername() +" " + dataSource.getPassword();
    }

    @Autowired
    private Environment env;

}
```

4.启动类

```java
package com.persol.demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;


@Component@SpringBootApplication
public class ZookeeperApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZookeeperApplication.class, args);
	}

}
```



#### **测试结果**

<img src="https://gitee.com/jiangweiyu/typora-pic/raw/master/20201012231725.jpg" style="zoom: 67%;" />

当节点发生变化    >>>>    environment随即改变   >>>>  连接池BasicDataSource类的属性即时改变。