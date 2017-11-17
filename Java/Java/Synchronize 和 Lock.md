摘抄自[http://www.cnblogs.com/dolphin0520/p/3923167.html](http://www.cnblogs.com/dolphin0520/p/3923167.html)    
## synchronize 的缺陷 ##
synchronize 是Java 中的一个关键字，也就是说是Java 语言内置的特性   
当一个代码块synchronize 修饰了，当一个线程获取了对应的锁，并执行该代码块时，其他线程便只能一直等待，等待获取锁的线程释放锁，而这里获取锁的线程释放锁只会有两种情况   
 
- 获取锁的线程执行完了该代码块，然后线程释放对锁的占有
- 线程执行发生异常，此时JVM 会让线程自动释放锁    
那么如果这个获取了锁的线程由于要等待IO 或其他原因（比如sleep）被阻塞了，但是有没有释放锁，其他线程便只能一直等待，非常影响程序执行效率       

## 使用Lock注意 ##
1. Lock 不是Java 语言内置的，synchronize 是Java 语言的关键字，内置特性，Lock是一个类，通过这个类可以实现同步访问   
2. synchronize 不需要用户手动去释放锁，而Lock 则必须要用户手动释放锁，如果没有主动释放锁，就会死锁    

## Lock ##
![](https://i.imgur.com/zYgvmde.png)     

### lock() ###
获取锁，如果锁已经被其他线程获取，那么就进行等待，通常将锁的释放放在finally中    
	
	lock.lock();
	try{
	     
	}catch(Exception ex){
	     
	}finally{
	    lock.unlock();   //释放锁
	}
### tryLock() ###
尝试获取锁，如果成功，则马上返回true，如果失败（锁已经被其他线程持有），则马上返回false   

### tryLock(long time, TimeUnit unit) throws InterruptedException ###
尝试获取锁，在给定时间内能拿到锁，则返回true，超出给定时间还尚未获取到锁，返回false，等待的过程中被中断抛出异常

### lockInterruptibly() throws InterruptedException ###
类似于lock，但是这个响应中断，返回的结果，要么是拿到锁，要么是被中断返回。这个相对于synchronize ，synchronize 是不能响应中断的   
     
## 锁的相关介绍 ##
### 可重入锁 ###
如果锁具备可重入性，则称为可重入锁。像synchronize和ReentranceLock 都是可重入锁。可重入性表明了锁的分配机制：基于线程分配，而不是基于方法调用分配的。   
例子： 两个synchronize方法A 和 B ，在A 方法中调用方法B，那么当执行到调用B 方法的时候，线程不必要去重新申请锁，而是直接执行方法B 。假如当前线程执行到方法B 再去申请锁，是会陷入死锁的，因为这个锁现在就被当前这个线程锁持有    

### 可中断锁 ###
可以响应中断的锁，synchronize 是不响应中断的，而Lock 可以选择响应中断锁    

### 公平锁 ###
公平锁尽量以请求锁的顺序来获取锁，先来先到的意思    
非公平锁无法保证获取锁的顺序是按照请求锁的顺序来的，可能导致某一线程永远得不到锁    
synchronize 是 非公平锁   
对于ReentrantLock 和 ReentrantReadWriteLock 默认是非公平锁，可以在构造函数设置参数，变为公平锁    


### 读写锁 ###
将对资源的访问分为读和写，进而产生出读锁和写锁    
多个线程之间的读操作不会冲突    
ReadWriteLock 是个借口，ReentrantReadWriteLock是具体实现，可以通过readLock 获取读锁，writeLock 获取写锁     


## 理解Condition ##
[http://www.importnew.com/9281.html](http://www.importnew.com/9281.html)     

