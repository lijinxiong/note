## 关闭线程 ##
Java 中如何关闭一个线程，已经废弃的方法Thread.stop()会造成不可预知的bug，[官方说明](https://docs.oracle.com/javase/6/docs/technotes/guides/concurrency/threadPrimitiveDeprecation.html "官方说明")    
## 中断线程 ##
### 中断的原理 ###
Java 中断机制是一种协作机制，就是说不能通过中断来直接终止另一个线程，而需要被中断线程自己去处理中断。好比在家父母叮嘱在外的子女注意身体，但子女是否注意身体，怎么注意身体取决于自己       
Java 中断模型很简单，每个线程对象里都有一个boolean 类型的标识（不一定就要是Thread类的字段，实际上的确也不是，这几个方法最终都是通过native方法来完成的），代表着是否有中断请求（该请求可以来自所有线程，包括被中断的线程本身）    
### Thread 类设置中断状态 ###
![](https://i.imgur.com/cOQ5J8c.png)      
### 不同状态的线程对中断的处理 ###
![](https://i.imgur.com/UTA74qP.jpg)     
#### 运行状态 ####
如果线程在运行状态，interrupt方法只是设置线程的中断标记位，没有任何的其他作用。线程应该在运行过程中**合适的位置**检查中断标志位    


	@Override
    public void run() {

        System.out.println("run");

        while (isInterrupted()) {
            //run
        }
        
        System.out.println("stop");
    } 

#### 等待阻塞/sleep/join 导致的阻塞 ####
处于这些状态的线程，对线程对象调用interrupt方法会使得线程抛出InterruptException，需要注意的是，抛出异常后，中断标志位会被清空（有true 重设为false）

可以这样尝试处理

	try {
            while (!isInterrupted()) { //处于运行状态被中断直接
            }
        } catch (InterruptedException e) {
            //假如处于sleep wait join 阻塞状态，直接跳出while 循环
        }
#### 处于同步阻塞状态 ####
如果线程正在等待锁，对象线程对象调用interrupt方法只是会设置线程的中断标志位，线程依然会处于blocked 状态
#### New /Dead 状态 ####
线程尚未启动/线程已经结束，interrupt方法对它没有任何效果，中断标志位也不会被设置

#### IO操作 ####
如果线程在等待IO操作，尤其是网络IO，会有一些特殊处理
![来源：极乐科技](https://i.imgur.com/qf4ScyN.png)     

### 正确地取消关闭线程 ###
- interrupt方法不会真正中断线程，他只是一种协作机制，不能贸然调用interrupt方法，因为这样其实并不能一定取消掉线程    
- 对于以线程为基础向外提供服务的模块，应该单独提供取消/关闭的方法给调用者，而不应该暴露interrupt方法，像Future的cancel，ExecutorService的shutdown方法   

----------
主要参考   
[http://www.infoq.com/cn/articles/java-interrupt-mechanism](http://www.infoq.com/cn/articles/java-interrupt-mechanism)     
[https://zhuanlan.zhihu.com/p/27857336](https://zhuanlan.zhihu.com/p/27857336)   
[http://wangkuiwu.github.io/2012/08/01/threads-basic/](http://wangkuiwu.github.io/2012/08/01/threads-basic/)    
[http://xiezhaodong.me/2017/02/19/%E7%BA%BF%E7%A8%8B%E7%9A%84%E4%B8%AD%E6%96%AD(interrupt)%E6%9C%BA%E5%88%B6/](http://xiezhaodong.me/2017/02/19/%E7%BA%BF%E7%A8%8B%E7%9A%84%E4%B8%AD%E6%96%AD(interrupt)%E6%9C%BA%E5%88%B6/)     
   