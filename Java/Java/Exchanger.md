## 简介 ##
使两个线程之间传输数据，可以传输任意的数据类型      
## exchange()阻塞特性 ##
调用exchange方法后盖线程会等待其他线程来交换数据，一直阻塞等待下去，直到来交换获取    
	
	public static void main(String[] args) {
        Exchanger exchanger = new Exchanger();

        ExchangerThreadA exchangerThreadA = new ExchangerThreadA(exchanger);

        exchangerThreadA.start();
	   //ExchangerThreadB exchangerThreadB = new ExchangerThreadB(exchanger);
	   //exchangerThreadB.start();
    
	    System.out.println("main end");
	}
	/****************/
	public class ExchangerThreadA extends Thread {
	    
	    Exchanger<String> exchanger;
	
	    public ExchangerThreadA(Exchanger<String> exchanger) {
	        this.exchanger = exchanger;
	    }
	
	    @Override
	    public void run() {
	
	        try {
	            System.out.println("in Thread A get Thread B value " + exchanger.exchange("i come from Thread A"));
	            System.out.println("end ");
	
	        } catch (InterruptedException e) {
	
	        }
	
	    }
	}



---------------------
重载方法    

	public V exchange(V x, long timeout, TimeUnit unit)
        throws InterruptedException, TimeoutException

在超出时间没有线程来交换/获取的话，那么就会出现超时异常，进入catch 中，在另一种角度来说算是一种解决一直等待浪费线程的问题吧