## ThreadLocal##

关于创建线程局部变量的一个类   
在Thread中有这么一个成员变量   

	
	/* ThreadLocal values pertaining to this thread. This map is maintained
     * by the ThreadLocal class. */
    ThreadLocal.ThreadLocalMap threadLocals = null;
ThreadLocal 属于当前这个线程，这个map由ThreadLocal维护    
### demo ###
ThreadLocal    

![](http://i.imgur.com/K1AEqhA.jpg)     

输出：


	sub thread2   null
	sub thread1   null
	
	main   main
	sub thread1   sub thread1
	sub thread2   sub thread2
	
一个三个线程，main ，sub thread1 和 sub thread2。我们只是在main线程中创建一个ThreadLocal实例，并在main线程中调用set方法将线程名称保存起来，但是在后续的其他两个线程并没有能够拿到main线程set的值    


普通变量    

![](http://i.imgur.com/m79Orod.jpg)    

输出   
	
	
	xian yu lao
	xian yu zi 
	xian yu x

对于普通的变量，任何线程都能访问修改它的内容(final 对象跟修改内容无关)    

### set() ###
代码
	

	public void set(T value) {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null)
            map.set(this, value);
        else
            createMap(t, value);
    }
	//----------------
	ThreadLocalMap getMap(Thread t) {
        return t.threadLocals;//
    }
	//----------------创建ThreadLocalMap
	void createMap(Thread t, T firstValue) {
        t.threadLocals = new ThreadLocalMap(this, firstValue);
    }
	///set
	 private void set(ThreadLocal<?> key, Object value) {

            // We don't use a fast path as with get() because it is at
            // least as common to use set() to create new entries as
            // it is to replace existing ones, in which case, a fast
            // path would fail more often than not.

            Entry[] tab = table;
            int len = tab.length;
            int i = key.threadLocalHashCode & (len-1);

            for (Entry e = tab[i];
                 e != null;
                 e = tab[i = nextIndex(i, len)]) {
                ThreadLocal<?> k = e.get();

                if (k == key) {
                    e.value = value;
                    return;
                }

                if (k == null) {
                    replaceStaleEntry(key, value, i);
                    return;
                }
            }

            tab[i] = new Entry(key, value);
            int sz = ++size;
            if (!cleanSomeSlots(i, sz) && sz >= threshold)
                rehash();
        }



ThreadLocalMap     

![](http://i.imgur.com/gVk5eJU.jpg)     

ThreadLocalMap构造方法中创建了一个容量为16的数组，然后根据key的hash值和容量大小-1(15,二进制位1111)相与得到数组的下标，然后创建了一个Entry对象将ThreadLocal实例和vaule值保存起来。

### get() ###
	
	
	 public T get() {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null) {
            ThreadLocalMap.Entry e = map.getEntry(this);
            if (e != null) {
                @SuppressWarnings("unchecked")
                T result = (T)e.value;
                return result;
            }
        }
        return setInitialValue();
    }
是一个相反的过程，只是当你这个线程的threadLocals为null的时候，会调用*setInitialValue*方法   


	private T setInitialValue() {
        T value = initialValue();
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null)
            map.set(this, value);
        else
            createMap(t, value);
        return value;
    }
可以重写这个方法决定当我们没有在调用set方法之前调用get方法返回什么样的值   



调用ThreadLocal的实例的get/set方法，首先去当前线程中找到threadLocals实例，这个实例中有一个Entry数组，Entry类中就保存着对应的value值。所以线程之间不能相互的修改访问其他线程的threadLocal设置的值。      
	
## InheritableThreadLocal ##

在Thread中还有一个成员变量   


	/*
     * InheritableThreadLocal values pertaining to this thread. This map is
     * maintained by the InheritableThreadLocal class.
     */
    ThreadLocal.ThreadLocalMap inheritableThreadLocals = null;

*InheritableThreadLocal<T>*是ThreadLocal的子类，不同点就是子线程在没有set的时候通过get的方法可以访问到父线程set的值    


	InheritableThreadLocal<String> threadLocal = new InheritableThreadLocal();
        threadLocal.set(Thread.currentThread().getName());
        
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(threadLocal.get());
                threadLocal.set("sub thread1");
                System.out.println(threadLocal.get());
            }
        });

        thread.setName("sub thread1");
        thread.start();
        
        try {
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(threadLocal.get());
	//输出就是
	main
	sub thread1
	main


![](http://i.imgur.com/Erl1tXF.jpg)   
而ThreadLocal.createInheritedMap会调用ThreadLocal的构造函数        

![](http://i.imgur.com/YpATflw.jpg)     

将main线程的数组里面的Entry实例一个个复制到子线程的数组中    

*InheritableThreadLocal*只是重写父类的某些方法，其实原理是跟父类一样的   
InheritableThreadLocal类：   

![](http://i.imgur.com/Ob9As9O.jpg)    

## 最后 ##

![](http://i.imgur.com/hP1S1dS.jpg)    
