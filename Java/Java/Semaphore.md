## 简介 ##
中文含义就是信号，信号系统，此类的主要作用就是限制线程的并发数，就如在日常生活中，每到节假日，每个热门的旅游景点都会限制进入的人数，因为太多人会加大发生事故的概率      

## 介绍 ##
### 构造方法 ###
	
	 public Semaphore(int permits) {
        sync = new NonfairSync(permits);
    }
	
	public Semaphore(int permits, boolean fair) {
        sync = fair ? new FairSync(permits) : new NonfairSync(permits);
    }

- permits 就是允许多少个线程同时执行 acquire 和 release 之间的代码   
- fair 就是是否是公平锁，默认是非公平锁，公平锁就是尽可能地按请求的顺序来获取执行权，但是只是尽可能，并不能完全保证    

		
		public class Service {
				
		    private Semaphore semaphore = new Semaphore(1);
		
		    public void testMethod() {
		
		//        System.out.println("进入 testMethod" + Thread.currentThread().getName());
		        try {
		            semaphore.acquire();
		
		            System.out.println("get  " + Thread.currentThread().getName());
		            Thread.sleep(5000);
		            semaphore.release();
		
		        } catch (InterruptedException e) {
		            System.out.println("exception block");
		        }
		    }
		}	
		/---------------/
		public class ThreadA extends Thread{

		    private Service service;
		
		    public ThreadA(Service service) {
		        this.service = service;
		    }
		
		    @Override
		    public void run() {
		        service.testMethod();
		    }
		}

		/**********--------*/
		    public static void main(String[] args) {

	        Service service = new Service();
	        ThreadA a = new ThreadA(service);
	        ThreadA b = new ThreadA(service);
	        ThreadA c = new ThreadA(service);

	        a.start();
	        b.start();
	        c.start();	
	    }	

### 其他方法 ###
	
	public void acquireUninterruptibly() {
        sync.acquireShared(1);
    }
当我们通过acquire 方法去获取一个许可的时候，当许可已被其他线程获取完，那么我们就会进入阻塞状态，而在这个时候，该线程可能被中断，而通过acquireUninterruptibly 方法则可以让当前阻塞的线程不被打断，或者说不去响应中断    

	   public static void main(String[] args) throws InterruptedException {

	        Service service = new Service();
	        ThreadA a = new ThreadA(service);
	        ThreadA b = new ThreadA(service);
	
	        a.setName("a");
	        b.setName("b");
	
	        a.start();
	        b.start();
	
	        Thread.sleep(1000);
	        b.interrupt();
	
	    }	
	}
	
	//-----------------/
	public class Service {

	    private Semaphore semaphore = new Semaphore(1);
	
	    public void testMethod() {
	        
			try {
	            semaphore.acquire();
	            System.out.println("get  " + Thread.currentThread().getName());
	            Thread.sleep(5000);
	            semaphore.release();
	
	        } catch (InterruptedException e) {
	            System.out.println(Thread.currentThread().getName() + "   exception block");
	        }
	    }
那么线程b就会进入到catch 代码块中，放弃了获取许可，而使用acquireUninterruptibly 需要也会将该线程的中断标志位设置为true ，但是该线程(b)并不会去响应这个中断    

-----------------------

	public boolean tryAcquire() {
        return sync.nonfairTryAcquireShared(1) >= 0;
    }
	
	public boolean tryAcquire(long timeout, TimeUnit unit)
        throws InterruptedException {
        return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
    }
通过这两个方法尝试去获取许可，但是当没有可用许可并不会阻塞该线程     

timeout  当没有可用许可的时候，等待多长时间再返回，而在这段等待的时间可能获取到许可，那么久提前返回，在这个等待的时间段，可能会被中断    


----------------

	public int availablePermits() {
        return sync.getPermits();
    }
	public int drainPermits() {
        return sync.drainPermits();
    }
availablePermits 查看剩余可用的许可     
drainPermits  获取剩余可用的许可并返回获取到的许可，调用这个方法之后，剩余的许可变为0   


-------
	
	//是否有等待许可的线程	
	public final boolean hasQueuedThreads() {
        return sync.hasQueuedThreads();
    }
	//有多少个等待许可的线程
	public final int getQueueLength() {
        return sync.getQueueLength();
    }
	
	//等待许可的线程的集合
	protected Collection<Thread> getQueuedThreads() {
        return sync.getQueuedThreads();
    }
		
	


     
