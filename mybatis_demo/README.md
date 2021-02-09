# Mybatis作业说明

### 运行结果

![](https://gitee.com/jiangweiyu/typora-pic/raw/master/20210209230437.jpg)

从结论说，改截图展示了Mybatis作业要求中一对多查询的结果。截图可见Spring Boot以及Spring Cloud基础入门的多条评论以及用户信息

### 懒加载

- 开启fetchType = FetchType.LAZY时 

![image-20210209234938915](https://gitee.com/jiangweiyu/typora-pic/raw/master/20210209234939.png)

- 没有开启懒加载时

![image-20210209235441557](https://gitee.com/jiangweiyu/typora-pic/raw/master/20210209235441.png)

明显可以看出开启了懒加载时，如果我们只需要t_article信息(get Id)时，mybatis不会向下继续查询，而关闭懒加载时mybatis会加载外部表的信息

### 项目结构

![image-20210209224936567](https://gitee.com/jiangweiyu/typora-pic/raw/master/20210209224936.png)   



- 如上图所示 该项目分为 domin mapper service test四个包
  - 其中domin用于存放bean类,mapper用于保存dao层接口

### 具体配置

- applicationContext.xml

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <beans xmlns="http://www.springframework.org/schema/beans"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns:context="http://www.springframework.org/schema/context"
         xmlns:tx="http://www.springframework.org/schema/tx"
         xmlns:aop="http://www.springframework.org/schema/aop"
         xsi:schemaLocation="
  http://www.springframework.org/schema/beans
  http://www.springframework.org/schema/beans/spring-beans.xsd
  http://www.springframework.org/schema/context
  http://www.springframework.org/schema/context/spring-context.xsd
  http://www.springframework.org/schema/tx
  http://www.springframework.org/schema/tx/spring-tx.xsd
  http://www.springframework.org/schema/aop
  http://www.springframework.org/schema/aop/spring-aop.xsd">
      <!--service层注解组件扫描-->
      <context:component-scan base-package="com.lagou.service"/>
      <!--加载properties文件中的数据库信息-->
      <context:property-placeholder location="classpath:jdbc.properties"/>
      <!--spring整合mybatis-->
      <bean id="datasource" class="com.alibaba.druid.pool.DruidDataSource">
          <property name="driverClassName" value="${jdbc.driver}"/>
          <property name="url" value="${jdbc.url}"/>
          <property name="username" value="${jdbc.username}"/>
          <property name="password" value="${jdbc.password}"/>
      </bean>
      <!--sqlSessionFactory的创建权交给Spring-->
      <bean class="org.mybatis.spring.SqlSessionFactoryBean">
          <property name="dataSource" ref="datasource"/>
          <property name="typeAliasesPackage" value="com.lagou.domain"/>
      </bean>
      <!--mapper 映射扫描该包名下的所有接口生成代理对象储存到IOC容器中-->
      <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
          <property name="basePackage" value="com.lagou.mapper"/>
      </bean>
  </beans>
  ```

- pom.xml

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <project xmlns="http://maven.apache.org/POM/4.0.0"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <modelVersion>4.0.0</modelVersion>
  
      <groupId>com.lagou</groupId>
      <artifactId>mybatis_demo</artifactId>
      <version>1.0-SNAPSHOT</version>
  
      <properties>
          <maven.compiler.source>11</maven.compiler.source>
          <maven.compiler.target>11</maven.compiler.target>
      </properties>
  
      <dependencies>
          <!--引入mybatis依赖-->
          <dependency>
              <groupId>org.mybatis</groupId>
              <artifactId>mybatis</artifactId>
              <version>3.5.1</version>
          </dependency>
          <dependency>
              <groupId>org.mybatis</groupId>
              <artifactId>mybatis-spring</artifactId>
              <version>1.3.1</version>
          </dependency>
          <!--引入mysql driver依赖-->
          <dependency>
              <groupId>mysql</groupId>
              <artifactId>mysql-connector-java</artifactId>
              <version>5.1.47</version>
          </dependency>
          <!--引入数据库连接池-->
          <dependency>
              <groupId>com.alibaba</groupId>
              <artifactId>druid</artifactId>
              <version>1.1.15</version>
          </dependency>
          <!--spring framework-->
          <dependency>
              <groupId>org.springframework</groupId>
              <artifactId>spring-context</artifactId>
              <version>5.1.5.RELEASE</version>
          </dependency>
          <dependency>
              <groupId>org.springframework</groupId>
              <artifactId>spring-jdbc</artifactId>
              <version>5.1.5.RELEASE</version>
          </dependency>
          <dependency>
              <groupId>org.springframework</groupId>
              <artifactId>spring-tx</artifactId>
              <version>5.1.5.RELEASE</version>
          </dependency>
          <dependency>
              <groupId>org.springframework</groupId>
              <artifactId>spring-test</artifactId>
              <version>5.1.5.RELEASE</version>
          </dependency>
           <!--lombok-->
          <dependency>
              <groupId>org.projectlombok</groupId>
              <artifactId>lombok</artifactId>
              <version>1.18.12</version>
          </dependency>
          <dependency>
              <groupId>junit</groupId>
              <artifactId>junit</artifactId>
              <version>4.13</version>
          </dependency>
  
      </dependencies>
  
  </project>
  ```

