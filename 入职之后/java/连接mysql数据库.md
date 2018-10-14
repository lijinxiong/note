[引用](https://www.cnblogs.com/xiaotiaosi/p/6394554.html "原文")   
JDBC由Java编写的类和接口组成，提供一系列的Api 用于访问数据库，对各种数 据库进行连接，然后对数据库进行增删改查。   
# JDBC简介 #
JDBC java database connectivity java 数据库连接 可以为多种数据库提供统一的访问    
## 功能 ##
1. 与数据库建立数据库连接
2. 向数据库发送SQL 命令
3. 处理数据库返回的数据

# JDBC 常用类和接口 #
驱动管理类 DriverManager 数据库连接Connection 声明类Statement 查询结果 ResultSet    

1. DriverManager    
	是JDBC的管理类，与数据库建立连接，一般可使用DriverManger.getConnection()方法获取数据库的连接对象Connection
2. Statemen  
	用于将SQL 语句发送到数据库中，有三类Statement，分别是Statement 、PreparedStatement（继承自Statement）、CallableStatement（继承自PreparedStatement）    
	1. 