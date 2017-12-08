## IO编程 ##
### 文件读写 ###
#### 打开文件 ####
open 函数用来打开文件     
	
	open(name[.mode[.buffer]])    
name 是唯一强制性参数，其他未可选，默认是读模式，无缓冲    

#### 文件模式 ####

mode 参数    

- 'r' 读模式   
- 'w' 写模式     
- 'a' 追加模式    
- 'b' 二进制模式（可添加到其他模式中使用）   
- '+' 读写模式    

#### 文件缓冲区 ####
如果参数是0 则是没有缓冲 数据直接写到硬盘上，而参数是1则是有缓冲，数据线写到内存中，调用函数flush 或者close函数才会将数据更新到磁盘    
如果输入的参数大于1，则表示是缓冲区的大小，单位为字节，而小于0则使用默认的缓冲区大小    
#### 文件读取 ####
一次性读取所有，返回的是一个str 类型的对象     

	f.read()    
	f.close()
最后调用close 方法，关闭对文件的引用    
一般放在try finally 语句块中，因为io 过程可能产生异常   

	try:
		f = open(name)
		f.read()
	finally:
		if f:
			f.close()
也可以直接使用with 语句    

	with open(name) as f:
		f.read()


#### 文件写入 ####
调用write 方法，open的时候选择写模式    
	
	f = open(name,'w')
	f.write('content')
	f.close()
可以反复的调用write 方法，最后一定要调用close方法。    
调用write 方法的时候，不是直接写入文件中的，而是在缓存中先缓存的在内存中，调用close 或者flush方法才将数据写入到文件中。文件操作中可能会出现IO 异常，所以还是推荐使用with 语句    

-----------
with 语句解释    
[http://blog.csdn.net/suwei19870312/article/details/23258495/](http://blog.csdn.net/suwei19870312/article/details/23258495/)     

### 操作文件和目录 ###
os 模块和 shutil  模块    

- 获取当前python脚本的工作目录   os.getcwd()    
- 返回指定目录下的所有文件和目录   os.listdirt([path])      
- 删除一个文件   os.remove(filepath)   
- 删除多个空目录   os.removedirs()    
- 判断是否是一个文件   os.path.isfile(filepath) 
- 判断是否是一个目录  os.path.isdir(filepath)     
- 判断是否是绝对路径 os.path.isabs()    
- 检测路径是否真的存在   os.path.exists()    
- 分离一个路径的路径名和文件名   os.path.split()    
- 分离扩展名   os.path.splitext()     
- 获取路径名  os.path.dirname(filepath)    
- 获取文件名  os.path.basename  
- 读取和设置环境变量   os.getenv()和 os.putenv()  
- 给出当前平台使用的行终止符  os.getlineseq    
- 指示真正使用的平台   os.name    
- 重命名文件或目录  os.rename(old，new)    
- 创建多级目录  os.makedirs()   
- 创建单个目录 os.mkdir()   
- 获取文件属性   os.stat()  
..........

### 序列化操作 ###
使用pickle模块来实现序列化   

	pickle.dumps(obj)#将任意对象序列化成一个str对象   
	pickle.dump(obj,f)#直接序列化后写入文件中   
而是用load或loads方法反序列化  
	
	pickle.loads(str)#返回一个str 类型的对象   
	pickle.load(f)#返回序列前的对象   
 
例子  
	
	
	my_list = [1, 2, 3, 4]
	string = pickle.dumps(my_list)
	
	print(string)
	
	string = pickle.loads(string)
	print(string)
	
	f = open(r'data.txt', 'rb')
	
	print(type(pickle.load(f)))
	f.close()
		
	# b'\x80\x03]q\x00(K\x01K\x02K\x03K\x04e.'
	# [1, 2, 3, 4]
	# <class 'list'>

## 进程和线程 ##
### 多进程 ###
实现多进程的两种方法，os模块中的fork 方法 和 multiprocessing 模块 ，前者只适用于unix 和linux 系统，windows 不支持    
#### multiprocessing 模块创建多进程 ####
**使用Process 类来描述一个进程对象**
  
创建子进程时，照顾小阳台传入一个执行函数和函数的参数，即可完成一个Process 对象的创建，使用start方法启动进程，用join方法实现进程间同步     

	
	# 使用Process 类 手动创建多个进程 
	
	import os
	from multiprocessing import Process
	
	def run_pro(name):
	    print('child process %s %s' % (name, os.getpid()))
	
	if __name__ == '__main__':
	    print('parent process %s' % os.getpid())
	
	    for i in range(1, 5):
	        p = Process(target=run_pro, args=(str(i),))
	        print('process will start')
	        p.start()
	    p.join()
	    print('end')
**使用Pool 类线程池来创建**  
 
Pool 提供指定数量数量的进程供用户调用，默认是cpu 的核数。当有新的请求提交到Pool 时，假如池还没有满，就会创建一个新的进程来执行该请求，但如果线程池中进程数已经达到最大值，那么请求就会等待，直到池中有进程结束，才会创建有进程来处理它    

	# 使用Pool 类创建多个线程
	import os, random, time
	from multiprocessing import Pool
	
	def task_run(name):
	    print('task %s ,pid = %s start ' % (name, os.getpid()))
	    time.sleep(random.random() * 3)
	    print('task %s end ' % name)
		
	if __name__ == '__main__':
	    print('current process %s' % os.getpid())
	
	    p = Pool(processes=3)
	
	    for i in range(5):
	        p.apply_async(task_run, args=(str(i),))
	
	    print('wait all subprocess')
	
	    p.close()
	    p.join()
	    print('all subprocess end')
创建了容量为3的进程池，依次添加了5个任务，但是一开始以及最多能执行的任务只有3 个，当一个任务结束后，新的任务才会被执行，而且执行它的进程依然是进程池中原有的进程      
Pool对象调用join方法会等待所有子进程执行完毕，调用join方法之前必须先调用close 方法   

**进程间的通信**   
进程间通信的方式，如Queue Pipe Value + Array等   

Queue 是多进程安全的队列，可以实现多进程之间的数据传递，有两个方法 put 和 get   

- put方法插入数据到队列中，还有两个可选参数，blocked 和 timeout。如果blocked 为true（默认值），并且timeout为正值，该方法会阻塞一定时间，直到有队列中有空余的空间可以插入，如果超时的话，就会抛出Queue.Full 异常，如果blocked 为False，但Queue满了，就会马上抛出Queue.Full 异常    
- get 方法可以从队列中读取并删除一个元素。可选参数blocked 为True 并且timeout 为正值，在等待时间内没有取到元素的话，会抛出Queue.Empty 异常，如果blocked为False，如果队列为空，那么就会马上抛出Queue.Empty 异常   

-------------

**使用 Queue 进行进程间通信**
	
	import os, random, time
	from multiprocessing import Process, Queue
	
	
	def write_fun(q, urls):
	    print('write process %s' % (os.getpid(),) + 'start ')
	    for url in urls:
	        print('write %s to queue ' % url)
	        q.put(url)
	        time.sleep(random.random() * 3)
	
	
	def read_fun(q):
	    print('read process start')
	
	    while True:
	        url = q.get(True)
	        print('get data is %s' % url)
	
	
	if __name__ == '__main__':
	    q = Queue()
	    proc_write1 = Process(target=write_fun, args=(q, ['url-1', 'url-2', 'url-3']))
	    proc_write2 = Process(target=write_fun, args=(q, ['url-1', 'url-2', 'url-3']))
	    proc_read = Process(target=read_fun, args=(q,))
	
	    proc_write1.start()
	    proc_write2.start()
	    proc_read.start()
	
	    proc_write2.join()
	    proc_write1.join()
	    proc_read.terminate()

**使用Pipe 进行通信**   
Pipe 常用于两个进程之间进行通信，两个进程分别位于管道的两端      
Pipe方法返回(conn1,conn2)代表一个管道的两个端，Pipe方法有参数duplex，如果为True（默认值），那么这个管道是双工模式的，也就是conn1和conn2均可收发。如果为False conn1就负责接收信息，conn2 只是负责发送消息，send 和recv 方法就是发送和接受消息      
如果没有消息接收，那么recv方法就会一直阻塞，如果管道关闭了，recv就会抛出EOFError   

	import os, random, time
	from multiprocessing import Process, Pipe
	
	
	def send_fun(p, urls):
	    for url in urls:
	        print('send data is %s' % url)
	        p.send(url)
	        time.sleep(random.random() * 3)
	
	
	def recv_fun(p):
	    while True:
	        print('recv data is %s' % (p.recv()))
	        time.sleep(random.random() * 3)
	
	
	if __name__ == '__main__':
	    pipe = Pipe()
	
	    send_process = Process(target=send_fun, args=(pipe[0], ['a', 'aew', 'adf', 'as', ]))
	    recv_process = Process(target=recv_fun, args=(pipe[1],))
	
	    send_process.start()
	    recv_process.start()
	
	    send_process.join()
	    # recv 进程一直在执行，不能结束
	    recv_process.join()

### 多线程 ###
python 3 提供了threading 模块实现多线程    

**使用函数产生新的线程**    

	# 使用start_new_thread()函数来产生新线程
	
	import threading
		
	def run_fun(thread_name):
	    print(thread_name)
	
	
	threading._start_new_thread(run_fun, ('thread1',))
	threading._start_new_thread(run_fun, ('thread2',))
	
	while True:
	    pass
**继承Thread 类重写函数**     

	

	from threading import Thread	
	
	class MyThread(Thread):
	
	    def __init__(self):
	        Thread.__init__(self)
	        print('init')
	
	    def run(self):
	        print("%s run"%(self.getName()))
	
	
	
	thread1 = MyThread()
	thread2 = MyThread()
	
	thread1.start()
	thread2.start()
	
	thread1.join()
	thread2.join()

**线程同步**    
如果多个线程共同对某个数据修改，则可能出现不可预料的结果，为了保证数据的正确性，需要对多个线程进行同步，使用threading的RLock 和Lock 可以实现简单的线程的同步，这两个对象都有acquire 和release 方法，对于那些每次只允许一个线程操作的数据，可以将其操作放到acquire 和release 方法之间     
对于Lock 对象而言，如果一个线程连续两次进行acquire操作，那么由于第一次acquire之后没有release，第二次acquire将会挂起线程，导致线程死锁。RLock对象允许一个线程多次对其进行acquire操作，因为其内部通过一个counter变量维护着线程acquire的次数，而每次的acquire操作必须有一个release操作对应，在所有的release操作完成之后，别的线程才能申请该RLock对象    


	from threading import RLock
	from threading import Thread
	import time
	import threading
	
	var = 1
	thread_lock = RLock()
	
	
	def run_fun(thread_name):
	    thread_lock.acquire()
	    print("%s run " % thread_name)
	    time.sleep(2)
	    global var
	    print("var value is %s" % var)
	    thread_lock.release()
	
	
	def change_fun(thread_name):
	    thread_lock.acquire()
	    print('%s run ' % thread_name)
	    global var
	    var = 12
	    print("var value is %s" % var)
	    time.sleep(3)
	    thread_lock.release()
	
	
	threading._start_new_thread(run_fun, ('read_thread',))
	threading._start_new_thread(change_fun, ('write thread',))
	
	while True:
	    pass
	
**全局解释器锁**    
尽管python 完全支持多线程编程，但是解释器的C语言实现部分在完全并行执行时并不是线程安全的。实际上，解释器被一个全局解释器锁保护着，他确保任何时候都只有一个python 线程执行。GIL（global interpreter lock）最大的问题就是python的多线程并不能利用多核cpu的优势（例如一个使用了多线程的计算密集型程序只会在一个单cpu面上运行）     
gil 只会影响到那些严重依赖cpu 的程序（比如计算型的），如果你的程序大部分只涉及到io，网络交互，那么使用多线程就很合适，因为大部分时间都是在等待。如果程序是计算型的推荐使用多进程     

### 协程 ###
协程，又称为微线程，纤程     
协程的概念很早就提出来，但直到最近两年才在某些语言（Lua）中得到广泛应用    
子程序，或者称为函数，在所有语言中都是层级调用，比如A调用B，B调用了C，C执行完毕返回，B执行返回，最后是A执行完毕    
协程看上去也是子程序，但执行过程中，在子程序内部可中断，然后转向执行别的子程序，在适当的时候再返回来执行。注意，在一个子程序中中断，去执行其他子程序，不是函数调用，有点类似CPU中断，比如程序A，B     

	def A():
	    print('1')
	    print('2')
	    print('3')
	
	def B():
	    print('x')
	    print('y')
	    print('z')
假设由协程执行，在执行A的过程中，可以随时中断，去执行B，在执行B，也可能随时中断再去执行A，结果很多种可能    
看起来AB像是多线程，但是协程的特点就是在于是在一个线程中执行     
相比多线程，优势何在   

- 协程极高的执行效率，因为子程序切换不是线程切换，而是由程序自身控制，因此，没有线程切换的开销，和多线程相比，线程数量越多，协程的性能优势就越明显    
- 不需要多线程锁机制，因为只有一个线程，也不存在同时写变量冲突，在协程中控制共享资源不加锁，只要判断状态就好了，所以效率比多线程高很多    

--------------------------
python 对协程的支持是通过generator 实现的    
消费者 生产者 模型    

	def consumer():
	    r = ''
	    while True:
	        n = yield r
	        if not n:
	            return
	        print('[CONSUMER] Consuming %s...' % n)
	        r = '200 OK'
	
	
	def produce(c):
	    c.send(None)
	    n = 0
	    while n < 5:
	        n = n + 1
	        print('[PRODUCER] Producing %s...' % n)
	        r = c.send(n)
	        print('[PRODUCER] Consumer return: %s' % r)
	    c.close()
	
	
	c = consumer()
	produce(c)

解释   
consumer 函数是一个generator ，把一个consumer 传入produce后   

1. 首先调用c.send(None) 启动生成器   
2. 然后一旦产生了东西，就通过c.send(n)切换到consumer执行   
3. consumer 通过yield 拿到消息，处理，然后又通过yield把结果传回    
4. produce 拿到consumer 处理的结果，继续生产下一条消息   
5. produce 决定不生产了，通过c.close()关闭consumer，整个过程结束    

---
一句话总结协程的特点    
**子程序就是协程的一种特例**    

> [廖雪峰](https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001432090171191d05dae6e129940518d1d6cf6eeaaa969000)     

-------------------
第三方库gevent 提供了更好的协程支持，gevent 是一个基于协程的python 网络函数库。使用greenlet 在libev事件循环顶部提供了一个有高级别并发性的api，主要特性有以下几点   

- 基于libev 的快速事件循环，Linux 上市epoll 机制   
- 基于greenlet 的轻量级执行单元   
- api复用了 python标准库里的内容   
- 支持SSL 协作式sockets   
- 可通过线程池或c-ares实现dns 查询   
- 通过monkey patching 功能使得第三方模块变成协作式   

-------------------------
gevent 对协程的支持，本质上是greenlet 在实现切换工作。greenlet 工作流程如下：假如进行访问网络的io 操作时，出现阻塞，greenlet 就显示切换到另一段，没有被阻塞的代码段执行，直到原先阻塞状况消失以后，再自动切换回来代码段继续处理，因此，greenlet 是一种合理安排的串行方式      
由于IO操作非常耗时，经常使程序处于等待状态，有了gevent 为我们自动切换协程，就保证总有greenlet 在运行，而不是等待IO，这就是协程比一般多线程高效的原因      


	from gevent import monkey;

	monkey.patch_all()
	import gevent
	import urllib.request
	
	
	def run_task(url):
	    print('visit ---> %s' % url)
	
	    response = urllib.request.urlopen(url)
	    data = response.read()
	    print("%d bytes received from %s" % (len(data), url))
	
	
	if __name__ == '__main__':
	    urls = ['https://www.baidu.com/', 'https://cn.bing.com/dict/', 'https://www.csdn.net/']
	    # 用来形成协程
	    greenlets = [gevent.spawn(run_task, url) for url in urls]
	    # 添加这些协程任务，并且启动运行
	    gevent.joinall(greenlets)
以上程序主要用了gevent中的spawn 方法和joinall 方法，spawn方法可以看做是用来形成协程，joinall 方法就是添加这些协程任务，并且启动运行。三个网络操作时并发执行的，而且结束顺序不同，但其实只有一个线程    
gevent 中也提供了对池的支持，对greenlet进行并发管理（限制并发数），就可以使用池，这在处理大量的网络和IO操作时时非常必要的       

		
	from  gevent import monkey

	import urllib.request
	from gevent.pool import Pool
	
	monkey.patch_all()
	
	
	def run_task(url):
	    print('url is %s' % url)
	
	    response = urllib.request.urlopen(url)
	    data = response.read()
	    print("%s data length is %s " % (url, len(data)))
	
	
	if __name__ == '__main__':
	    urls = ['https://www.baidu.com/', 'https://cn.bing.com/dict/', 'https://www.csdn.net/']
	    pool = Pool(2)
	    pool.map(run_task, urls)
Pool对象确实对协程的并发数量进行管理，先访问了前两个网址，当其中一个完成时，才会执行第三个    

## 分布式进程 ##
分布式进程指的是将Process进程分布到多台机器上，充分利用多态机器的性能完成复杂的任务    
分布式进程在python 中依然要用到multiprocessing 模块。multiprocessing模块不但支持多进程，其中managers子模块还支持把多进程分布到多台机器上。可以写一个服务进程作为调度者，将任务分布到其他多个进程中，依靠网络通信进行管理。例子：在做爬虫程序时，抓取某个网站的所有图片，如果使用多进程的话，一般是一个进程负责抓取图片的链接地址，将链接地址放到queue中，另外的进程负责从queue中读取链接地址进行下载和存储到本地。现在把这个过程做成分布式，一台机器上的进程负责抓取链接地址，其他机器上的进程负责系在存储。那么遇到的主要问题是将queue 暴露到网络中，让其他机器进程都可以访问，分布式进程就是将这个过程进行了封装，我们可以将这个过程称为本地队列的网络化      

![](https://i.imgur.com/OrQp1vb.png)      

要实现上面例子的功能，创建分布式进程需要分为 六个步骤   

1. 建立队列Queue ，用来进行进程间通信。服务进程创建任务队列task_queue 用来作为传递任务给任务进程的通道；服务进程创建结果队列result_queue ，作为任务进程完成任务后回复服务进程的通道。在分布式多进程环境下，必须由Queuemanager获得Queue 接口来添加任务   
2. 把第一步中建立的队列在网络上注册，暴露给其他进程（主机），注册后获得网络队列，相当于本地队列的映像   
3. 建立一个对象（Queuemanager（BaseManager））实例manager，绑定端口和验证口令   
4. 启动第三步中建立的实例，即启动管理manager，监管信息通道   
5. 通过管理实例的方法获得通过网络访问的Queue对象，即再把网络队列实体化成可以使用的本地队列   
6. 创建任务到 “本地”队列中，自动上传任务到网络队列中，分配给任务进程进行处理   


------

代码    
服务进程    
	
	
	# 服务进程
	
	from multiprocessing.managers import BaseManager
	from multiprocessing import freeze_support, Queue
	
	# 任务个数
	task_number = 10
	
	# 收发队列
	task_quue = Queue(task_number)
	result_queue = Queue(task_number)
	
	
	def get_task():
	    return task_quue
	
	
	def get_result():
	    return result_queue
	
	
	# 创建类似的queueManager
	class QueueManager(BaseManager):
	    pass
	
	
	def win_run():
	    # 注册在网络上，callable 关联了Queue 对象
	    # 将Queue对象在网络中暴露
	    QueueManager.register('get_task_queue', callable=get_task)
	    QueueManager.register('get_result_queue', callable=get_result)
	    # 绑定端口和设置验证口令
	    manager = QueueManager(address=('127.0.0.1', 8001), authkey='qiye'.encode())
	
	    # 启动管理，监听信息通道
	    manager.start()
	
	    try:
	
	        # 通过网络获取任务队列和结果队列
	        task = manager.get_task_queue()
	        result = manager.get_result_queue()
	
	        # 添加任务
	        for url in ["ImageUrl_" + str(i) for i in range(10)]:
	            print('url is %s' % url)
	            task.put(url)
	
	        print('try get result')
	        for i in range(10):
	            print('result is %s' % result.get(timeout=10))
	
	    finally:
	        manager.shutdown()
	
	
	if __name__ == '__main__':
	    freeze_support()
	    win_run()
任务进程    


	import time

	from multiprocessing.managers import BaseManager
	
	
	class QueueManager(BaseManager):
	    pass
	
	
	# 使用QueueManager 注册用于获取Queue的方法名称
	QueueManager.register('get_task_queue')
	QueueManager.register('get_result_queue')
	
	server_address = '127.0.0.1'
	# 端口 和 验证口令注意保持与服务进程完全一致
	m = QueueManager(address=(server_address, 8001), authkey='qiye'.encode())
	
	# 从网络连接
	m.connect()
	
	# 获取queue 对象
	task = m.get_task_queue()
	result = m.get_result_queue()
	
	# 从任务队列中获取任务，并把结果写入result 队列中
	while not task.empty():
	    image_url = task.get(timeout=5)
	    print('download image %s' % image_url)
	    time.sleep(1)
	    result.put('success %s' % image_url)
	print('work exit')

## 网络编程 ##

### Socket ###
Socket 是网络编程的一个抽象概念，通常我们用一个Socket表示 “打开了一个网络链接”，而打开一个Socket 需要知道目标计算机的IP 地址和端口号，再指定协议类型即可。python 提供了两个级别的网络服务    
1. 低级别的网络服务支持基本的Socket，它提供了标准的BSD Sockets API ，可以访问底层操作系统Socket 接口的全部方法    
2. 高级别的网络服务模块Socket Server ，它提供了服务器中心类，可以简化网络服务器的开发   

----------------
#### socket() 函数 ####
python中我们使用socket 函数来创建套接字，语法格式如下     

	socket.socket(family[,type[,proto]])    
参数    

- family 套接字家族可以使用AF_UNIX 或者 AF_INET 
- type 套接字类型可以根据是面向连接的还是非连接分为SCOKET_STREAM 或SOCKET_DGRAM  
- protocol 一般不填默认为0  

------------
socket 对象内建的函数     

![](https://i.imgur.com/austwBf.png)    

### 简单实例 ###
服务器端   

	# server
	import socket
	import sys
	
	# 创建socket 对象，默认是AF_INET TCP连接
	serversocket = socket.socket()
	
	# 获取本地主机名
	host = socket.gethostname()
	
	port = 9999
	
	# 绑定端口
	serversocket.bind(host, port)
	
	# 设置最大连接数，超过后排队
	serversocket.listen(5)
	
	while True:
	    clientsocket, addr = serversocket.accept()
	    print('连接地址： %s' % str(addr))
	    msg = "welcome to here"
	    clientsocket.send(msg.encode('utf-8'))
	    clientsocket.close()
	    
客户端    

	# client

	import socket
	import sys
	
	s = socket.socket()
	
	host = socket.gethostname()
	
	port = 9999
	
	s.connect((host,port))
	
	msg = s.recv(1024)
	
	s.close()
	
	print(msg.decode('utf-8'))
