![](https://i.imgur.com/pSyL0aC.png)       

1. 最上层的服务并不是mysql 所独有的，大多数基于网络的客户端/服务器的工具或者服务都有类似的架构。比如连接处理，授权认证，安全等等    
2. 第二层是mysql 核心服务功能，包括查询解析，分析，优化，缓存以及所有的内置函数，例如日期时间，数学和加密函数，所有跨存储引擎的功能都在这一层实现，存储过程，触发器，视图    
3. 包含了存储引擎，负责mysql中数据的存储和提取       


--------------------------
### 连接管理与安全性 ###
每个客户端都会在服务器进程中拥有一个线程，这个连接查询只会在单独的线程中执行，该线程只能轮流在某个cpu中运行。服务器会负责缓存线程，因此不需要为每个新建的连接创建或者销毁线程    
当客户端连接到mysql服务器时，服务器需要对其进行认证，认证基于用户名 原始主机信息 密码 。一旦用户连接成功，服务器会继续验证该客户端是否具有执行某个特定查询的权限    

### 优化与执行 ###
mysql 会解析查询，并创建内部数据结构（解析树），然后对其进行各种优化，包括重写查询，决定表的读取顺序，以及选择合适的索引。用户可以通过特殊关键字提示优化器，影响它的决策过程。也可以请求优化器解析优化过程的各个因素，使用户可以知道服务器是如何进行优化决策，并提供一个参考基准，便于用户重构查询和schema 修改相关配置     

优化器并不关心表使用的是什么存储引擎，但存储引擎对于优化查询时有影响的。优化器会请求存储引擎所提供容量或某个具体的开销信息，以及表数据的统计信息     

对于select 语句，在解析查询之前，服务器会先检查查询缓存，如果能够在其中找到对应的查询，服务器就不必再执行查询缓存，优化和执行的整个过程，而是直接返回查询缓存的结果    

### 并发控制 ###
无论何时，只要有多个查询需要在同一时刻修改数据，都会产生并发控制问题   

#### 读写锁 ####
读锁是共享的，多个客户在同一个时刻可以同时读取同一个资源，互不干扰。写锁是排他的，也就是说一个写锁会阻塞其他的写锁和读锁，这是出于安全考虑    

#### 锁粒度 ####
一种提高共享资源并发性的方式就是让锁定对象更具有选择性，尽量锁定需要修改的部分数据，而不是所有的资源，更理想的是，只会对修改的数据片进行精确的锁定。任何时候，在给定资源上，锁定的数据量越少，则系统的并发度越高，只要相互之间不发生冲突    

问题是加锁本身也是需要消耗资源。锁的各种操作，包括获得锁，检查锁是否已经解除，释放锁，都会增加系统的开销    

所谓的锁策略，就是在锁的开销和数据安全性之间寻找平衡，大多数商业系统数据库，一般是在表上是假行级锁，并以各种复杂的方式来实现，以便在锁较多的情况下尽可能地提供更好的性能      

而mysql 则提供了多种选择，每种mysql存储引起都可以实现自己的锁策略和锁粒度。在存储引擎的设计中，锁管理是一个非常重要的决定，将锁的粒度固定在某个级别，可以为特定的应用场景提供更好的性能，但是同时失去灵活性，但是mysql支持多个存储引擎架构。      

1. 表锁：表锁是mysql 中最基本的锁策略，并且是开销最小的策略。它会锁定整张表，一个用户在对表进行写操作前，先获得写锁，这回阻塞其他用户对该表的所有读写操作，只有没有写锁时，其他读取用户才能获得读锁，读锁之间是不会相互阻塞的。
写锁也比读锁具有更高的优先级，因此一个写锁请求可能会被插入到读锁队列前面（写锁可以插到锁队列中读锁的前面，反之读锁不能插入到写锁的前面）     
    
2. 行级锁：行级锁可以最大程度地支持并发处理（同时也带来了最大的锁开销） 行级锁只在存储引擎实现，而在mysql的服务器层没有实现，服务器层完成不了解存储引擎中的锁实现      

### 事务 ###
acid 原子性atomicity 一致性consistency 隔离性 isolation 持久性 durability ，一个运行良好的事务处理系统，必须具备这些标准特征     

- 原子性 一个事务必须被视为一个不可分割的最小工作单元，整个事务中所有操作要么全部提交成功，要么全部失败回滚，对于一个事务来说，不可能只执行其中一部分操作，这就是事务的原子性    
- 一致性  数据库总是从一个一致性状态转换到另一个一致性的状态    
- 隔离性 **通常来说**，一个事务所做的修改在最终提交以前，对其他事务时不可见的。
- 持久性 一旦失误提交，则其所做的修改就会永久保存到数据库中，此时即使系统崩溃，修改的数据也不会丢失    


-------------------
#### 隔离级别 ####
四种隔离级别     

-----------------------------
**read uncommitted 未提交读**事务中的修改，即使没有提交，对其他事务也都是可见的。事务读取到未提交的数据，这被称为脏读 dirty read ，这个级别会导致很多问题    
 
---------------
**read committed 提交读** 一个事务从开始直到提交之前，所做的任何修改对其他事务都是不可见的，这个级别有时候也叫做不可重复读，因为两次执行同样的查询，可能会得到不一样的结果   

---------

**repeatableread 可重复读** 该级别保证了同一个事务中多次读取同样记录的结果是一致的。但是理论上，可重复读隔离级别还是无法解决另一个幻读的问题。所谓的幻读，指的是当某个事务在读取某个范围内的记录时，另外一个事务又在该范围内插入新的纪录，当之前的事务再次读取该范围的记录时，会产生幻行。innodb 存储引擎通过多版本并发控制解决了幻读问题    

-----------
**serializable 可串行化**通过强制事务串行执行，避免了前面说的幻读的问题，简单来说，serializable 会在读取的每一行上加上锁，所以导致大量的超时和锁争用的问题。实际上很少用到这个隔离级别         

--------------------
#### 死锁 ####
死锁是指两个或者多个事务在同一资源上相互占用，并请求锁定对方占用的资源，从而导致的恶性循环的现象。

#### 事务日志 ####
事务日志可以帮助提高事务的效率。使用事务日志，存储引擎在修改表的数据时只需要修改其内存拷贝，再把该修改行为记录到硬盘的事务日志中，而不用每次修改的数据本身持久到磁盘。事务日志采用的是追加的方式，因此写日志的操作就是磁盘上一小块区域内的顺序I/O，而不像随机IO需要在磁盘的多个地方移动磁头，所以采用日志的方式相对来说很快  

#### mysql 中的事务 ####
提供两种支持事务的存储引起  InnoDB 和NDB Cluster      

自动提交   
默认采用自动提交模式，也就是说不显式地开始一个事务，则每个查询都被当作一个事务执行提交操作，可以设置这个参数值    

-------------------------
隐式和显式锁定       
innodb 采用的是两阶段锁定协议。在事务执行过程中，随时都可以执行锁定，锁只用在commit 或者 rollback 的时候才会释放，并且所有锁都是在同一时刻释放，这就是隐式锁定，Innodb 会根据隔离级别在需要的时候自动加锁       
也支持显示锁定     
	
	SELECT * FROM user LOCK IN SHARE MODE ;


### 多版本并发控制 ###
mysql 的多数事务型存储引擎实现的都不是简单的行级锁。基于并发性能的考虑，一般都是实现了多版本并发控制MVCC       
可以认为MVCC 是行级锁的一个变种，但是它在很多情况下避免了加锁操作，因此开销更低，虽然实现机制有所不同，但大都实现了非阻塞的读操作，写操作也只锁定了必要的行     

MVCC 的实现，是通过保存数据在某个时间点快照来实现的，也就是，不管需要执行多长时间，每个事物看到的数据都是一致的，根据事务开始的时间不同，每个事务对同一张表，同一时刻看到的数据可能不一样，         

不同存储引擎实现MVCC 是不同的，典型的有乐观并发控制和悲观并发控制 。通过InnoDB 简化版说明MVCC 工作    

InnoDb 的MVCC 是通过在每行记录后保存两个隐藏的列来实现的，这两个列，一个保存了行的创建时间，一个保存行的过期时间（删除时间）。当然存储的不是实际的时间值，而是系统版本号。  每开始一个新的事务，系统版本号都会自动增加，事务开始时刻的版本系统版本号会作为事务的版本号，用来和查询到的每行记录的版本进行比较，在repeatable read 隔离级别，MVCC 操作      


- select   
	InnoDB 会根据以下两个条件检查每行记录    
	
		1. InnoDB 只查找版本早于当前事务版本的数据行（也就是行的系统版本号小于或等于事务的系统版本号），这样可以确保事务读取的行，要么是在事务开始前已经存在，要么是事务自身插入或者修改过的
		2. 行的删除版本要么没有定义，要么大于当前事务的版本号，这可以确保事务读取到的行，在事务开始之前未被删除    
	只用符合以上才可以作为查询结果    
- Insert   
	InnoDB为新插入的每一行保存当前系统版本号作为行版本号     
- delete    
	Innodb 为删除的每一行保存当前系统版本号作为行删除标识    
- update   
	InnoDb 为插入一行新的纪录，保存当前系统版本号作为行版本号，同时保存当前系统版本号到原来的行作为删除标识    

-------------------

保存这两个额外系统版本号，使大多数读操作都可以不用加锁，这样设计使得数据操作很简单，性能很好，并且能够保证只会读取到符合标准的行，不足之处是每行记录都需要额外的存储空间，需要做更多的检查工作，以及一些额外的维护工作       

MVCC 只在repeatableread 和 committed read 两个隔离级别下工作，其他两个隔离级别都和MVCC 不兼容，因为read uncommitted 总是读取到最新的数据行，而serializable 则会对所有读取的行都加锁     

### Mysql 存储引擎 ###
使用*show table status like 'tableName'*     
![](https://i.imgur.com/apRv1B0.png)      

- name 表名    
- engine 表的存储引擎类型    
- version 表 frm文件的版本号   
- row-format 行的格式，对于myisam表，可选的值为dynamic fixed 或者compressed。dynamic 的行长度是可变的，一般包含可变长的字段，如varchar 或者blob。fixed的行长度则是固定的，只包含固定长度的列，如char或者integer。compressed 的行则只在压缩表中存在     
- rows 行的数目，部分存储引擎如myisam 存储精确的数目，对于其他存储引擎比如InnoDB 这个值只是一个大约数，与实际值可能相差40-50% 直接使用select count(*)准确。对于INFORMATION_SCHEMA数据库中的表，rows 的值是null      
- avg_row_length 平均的行长度,单位是字节     
- data_length   数据文件的长度 ，单位是字节       
- max_data_length 数据文件的最大长度，mysql5 之后的版本所能支持的最大存储容量是非常大，表的大小主要是受限于操作系统，如果是InnoDB的话，显示的是0 不知道为啥    
- index_length  索引的大小 ，字节为单位    
- data_free 被整序，但是没有被使用的字节数目    
- auto_increment 下一个自增的值   
- create_time 表创建的时间   
- update_time 表数据的最后修改时间    
- check_time 使用check table 命令 或者myisamchk 工具最后一次检查表的时间   
- collation校对字符集   
- checksum 活性校验和值   
- create_options 创建表的额外选项    
- comment 创建表的额外注释    

----------

#### InnoDB存储引擎 ####
InnoDB的数据存储在表空间中，表空间是由InnoDB管理的一个黑盒子，由一系列的数据文件组成    
InnoDB 采用MVCC 来支持高并发，并且实现了四个标识的隔离级别，默认的是repeatable read 可重复读，并且通过 间隙锁策略防止幻读的出现，间隙锁使得InnoDB不仅仅锁定查询涉及的行，还会对索引中的间隙进行锁定，以防止幻影行的插入       

InnoDB 表示基于聚簇索引建立的，InnoDB