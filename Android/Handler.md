![](http://upload-images.jianshu.io/upload_images/2038754-2c9409bd1cdb8cb0.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)   

## 日常使用 ##

![](http://i.imgur.com/qcqjXAf.jpg)    

*post()*最终还是调用sendMessage方法    
	
	
	public final boolean post(Runnable r)
    {
       return  sendMessageDelayed(getPostMessage(r), 0);
    }
	private static Message getPostMessage(Runnable r) {
        Message m = Message.obtain();
        m.callback = r;
        return m;
    }

而使用Callback接口只是为了让你不再重写Handler类而已，至于图片上，只是为了说明返回值的影响，一般使用了Callback之后就不会再重写Handler的handleMessage方法，那么返回值是false也没关系，因为Handler本身的handlemessage是空实现   
### 在子线程 ###

![](http://i.imgur.com/KhxWXLJ.jpg)    


应该在Looper#loop()方法之后调用*handler.getLooper().quitSafely();*                       

如果在子线程new Handler ，假如没有调用looper.prepare(),就会抛出异常     
	
	
	if (mLooper == null) {
            throw new RuntimeException(
                "Can't create handler inside thread that has not called Looper.prepare()");
        }
*looper.prepare()*方法就是创建一个Looper的对象    
## 分析 ##
### ActivityThread#main ###

![](http://i.imgur.com/hs2vZiA.jpg)     

### Loooer#prepareMainLooper ###

![](http://i.imgur.com/nbAAulw.jpg)    


	//Looper构造函数
	private Looper(boolean quitAllowed) {
        mQueue = new MessageQueue(quitAllowed);
        mThread = Thread.currentThread();
    }
 	//------
	public static @Nullable Looper myLooper() {
        return sThreadLocal.get();
    }
	//---在声明为成员变量的时候就已经赋值了
	static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<Looper>();

[ThreadLocal](http://www.jianshu.com/p/c4705ce64b69 "ThreadLocal")介绍    
至此创建了一个Looper的实例，并且存放子啊ThreadLocal 中，并把这个Looper实例同时赋值给sMainLooper变量。而在Looper的构造函数中创建了MessageQueue实例，并把当前的线程保存下来        
### MessageQueue ###

	
	/**
 	* Low-level class holding the list of messages to be dispatched by a
 	* {@link Looper}.  Messages are not added directly to a MessageQueue,
 	* but rather through {@link Handler} objects associated with the Looper.
 	* 
 	* <p>You can retrieve the MessageQueue for the current thread with
 	* {@link Looper#myQueue() Looper.myQueue()}.
 	*/    
MessageQueue中存放的是一个Message的链表，这个链表是给Looper对象分发用的。这个Message对象不是直接添加到MessageQueue，而是通过Handler对象   
在MessageQueue中有个成员变量**Message**      
### Message ###
而在Message类中  


	
    // sometimes we store linked lists of these things
    /*package*/ Message next;
那么这个链表就通过这样的方式形成了    
### Looper#loop() ###
回到ActivityThread#main中，最后调用了Looper#looper方法    

![](http://i.imgur.com/TzNzFrx.jpg)    

在loop方法之后调用quitSafely或者quit方法，否则这个线程就会一直运行   

![](http://i.imgur.com/LyMWmhg.jpg)  

一开始的的代码加上Thread Name 才运行    
loop()方法是一个死循环，唯一退出的条件就是MessageQueue.next()返回null，而使MessageQueue.next()返回null，就必须调用Looper的quit方法，quit方法再调用MessageQueue的quit方法，继而MessageQueue的next方法才会返回null      
### MessageQueue#next() ###

![](http://i.imgur.com/bWxSk5N.jpg)    


1. MessageQueue中的Message队列是按分发的时间排列的，比如说使用handler 调用sendMessageDelay延时发送为15s ，一个为16s，那么延时16s的会排在15s的后面   
2.  Message.target 在Message 加入到MessageQueue中的时候就被赋值成发送它的Handler实例    
3. IdleHandler    


		 /**
	     * Callback interface for discovering when a thread is going to block
	     * waiting for more messages.
	     */
	    public static interface IdleHandler {
	        /**
	         * Called when the message queue has run out of messages and will now
	         * wait for more.  Return true to keep your idle handler active, false
	         * to have it removed.  This may be called if there are still messages
	         * pending in the queue, but they are all scheduled to be dispatched
	         * after the current time.
	         */
	        boolean queueIdle();
	    } 
当我们的主线程空闲的时候就会调用存在MessageQueue中list的IdleHandler对象，我们常说的GC也是通过IdlerHandler来实现的(部分)    
demo 
	
	
		Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
	                    @Override
	                    public boolean queueIdle() {
	                        Toast.makeText(HandlerActivity.this, "idle handler", Toast.LENGTH_SHORT).show();
	                        return true;
	                    }
	                });
queueIdle()方法返回true就代表这个IdleHandler对象一直存在于MessageQueue的list中，主线程一有空就会去调用里面的IdleMessage的queueIdle方法，而false就代表执行一次就被list移除掉    
上面的代码假设你不操作手机屏幕使主线程处于空闲中，那么回一直会有Toast弹出    
4. 至于最后的*pendingIdleHandlerCount = 0;*导致了最后那段for循环不再被执行，那么IdleHandler在哪执行呢?个人怀疑是底层的c那边吧，不确定。如有知道的大佬，麻烦说说，谢谢   
### Handler.dispatchMessage(msg)###
拿到Message之后，回到Looper的loop方法中，就会调用我们的Handler.dispatchMessage方法   

![](http://i.imgur.com/TzNzFrx.jpg)     


	 public void dispatchMessage(Message msg) {
        if (msg.callback != null) {
            handleCallback(msg);
        } else {
            if (mCallback != null) {
                if (mCallback.handleMessage(msg)) {
                    return;
                }
            }
            handleMessage(msg);
        }
    }
msg.callback 就是我们通过handler的post方法中的参数Runnable对象，而handleCallback(msg);就会执行Runnable对象中的run方法    
而mCallback就是*Handler(new Handler.Callback())*，这个里面的Callback就是它   
*handleMessage(msg);*最后这个就是我们Handler方法的handleMessage方法       

### msg.recycleUnchecked(); ###
loop循环中最后一句就是回收Message   


	/**
     * Recycles a Message that may be in-use.
     * Used internally by the MessageQueue and Looper when disposing of queued Messages.
     */
    void recycleUnchecked() {
        // Mark the message as in use while it remains in the recycled object pool.
        // Clear out all other details.
        flags = FLAG_IN_USE;
        what = 0;
        arg1 = 0;
        arg2 = 0;
        obj = null;
        replyTo = null;
        sendingUid = -1;
        when = 0;
        target = null;
        callback = null;
        data = null;

        synchronized (sPoolSync) {
            if (sPoolSize < MAX_POOL_SIZE) {//MAX_POOL_SIZE =50
                next = sPool;   // next 为Message 成员变量
                sPool = this;//sPool为Message静态变量
                sPoolSize++;
            }
        }
    }
形成链表，保存一些空闲的Message  
我们使用message的obtain()方法就是从sPool中获得Message实例    

### Handler.sendMessage ###
Handler.sendMessage方法最终都会调用下面的方法将message加入到MessageQueue中
	
		
	private boolean enqueueMessage(MessageQueue queue, Message msg, long uptimeMillis) {
        msg.target = this;//上面说过target就是发送这个mes的handler对象
        if (mAsynchronous) {
            msg.setAsynchronous(true);
        }
        return queue.enqueueMessage(msg, uptimeMillis);
    }
### Looper#enqueueMessage ###
	
![](http://i.imgur.com/4Rpo9eu.jpg)    

## 最后 ##
本来想画一张流程图的，但是怎么想也不满意，就简单写个流程吧   

1. 获得Message(new 或者obtain)
2. 通过handler加入到MessageQueue中   
3. Looper通过MessageQueue获得Message  
4. Looper将获得的Message交给发送Message的Handler处理    
5. Handler处理完成之后，Looper将Message放到Message池中    

上面没有提及到Looper是死循环，若加上可能就是写3--5 是一个循环，但是并不好感觉    

上面的分析流程全是自己分析，有点乱    

什么都最好自己亲自做一遍，才更好，也会发现一些新的东西   

比如IdleHandler，GC啥的	
