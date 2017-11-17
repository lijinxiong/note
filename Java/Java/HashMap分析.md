![](http://www.xoomtrainings.com/blog/wp-content/uploads/2016/03/java2.jpg)     

## HashMap ##
从类文件中的注解可以得到以下的一些信息    
	
1. HashMap继承了**Map**接口    
2. 运行**null**作为key/value   
3. HashMap较于HashTable，除了HashMap不支持同步(线程不安全)和允许插入**null**外，跟HashTable没啥差了    
4. HashMap不保证存放数据的顺序  
5. 如果hash函数可以合适地将元素分散到各个存放点(buckets),那么**get**和**put**操作的执行时间是常数级别的  
6. 而迭代遍历元素的时间是和HashMap的容量相挂钩的，所以如果迭代遍历对于程序很重要，那么HashMap的**capacity**容量 就不要太高，或者**load factor**负载因子不要太低    
7. 实例化HashMap的时候有两个参数，一个是**capacity**和**load factor**。capacity代表着HashMap中数组的长度，而load factor 则是用于测量这个容量是否满了，满了就要自动增加capacity的值    
8. **load factor**的默认值是0.75，这个值是时间和容量上的平衡点   
9. 如果存放的元素到达load factor * capacity 的值，HashMap会自动的增加容量，元素会重新hash和排列   
10. **fail-fast**：当你创建了iterator遍历的时候，但是在另一个线程改变了这个map的结构(增加一个元素，或者删去一个元素)，就会抛出*ConcurrentModificationException*     

hashmap的结构     

![](http://i.imgur.com/c3h6xdp.png)   
 

### new HashMap ###


	
    public HashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                                               initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " +
                                               loadFactor);
        this.loadFactor = loadFactor;
        this.threshold = tableSizeFor(initialCapacity);
    }

    public HashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public HashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
    }

    public HashMap(Map<? extends K, ? extends V> m) {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        putMapEntries(m, false);
    }
HashMap四个构造函数，没有传入capacity和 load factor 的默认是 16 和 0.75    
随后一个构造函数传入一个Map实例，后面再写    

上面出现一个**threshold**变量，它等于 capacity * load factor(第一次初始化的时候等于capacity)，之后随着HashMap的扩容，它每次在原来的基础上扩大两倍     	
	
	
	newThr = oldThr << 1; // double threshold
	threshold = newThr;
而函数**tableSizeFor**，会根据你传入的参数，返回一个大于(最接近参数)或等于参数的数，并且这个数十2 的倍数    
	
	
	    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
比如说传入的参数cap为13，那么返回的值只会是16    

###  public V put(K key, V value) ###



	public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }

*V put(K key, V value)*  假如这个key已经存在于这个map中(相当于更新value)，那么返回值就是原来的value，这个value可能为null，因为HashMap 允许value的值为null。还有一种返回null的情况，就是这个key是第一次插入到这个hashmap中的     

*hash(key)*      	
	
	
	static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
当这个key的值为null的时候返回0，否则就返回 *(h = key.hashCode()) ^ (h >>> 16)*    

再看看putVal这个方法   

![](http://i.imgur.com/SCR6j0W.jpg)    

Node<K,V> 类   

![](http://i.imgur.com/rAmOZEm.jpg)    

这个类就是HashMap中数组存放的对象(JDK1.8，1.7貌似只是Entry)，Node类继承自Entry，在类中有四个属性，一个是key，value，还有一个是根据key生成的hash，还有一个是Node的实例next，用于形成一个链表    

![](http://i.imgur.com/aSXATxS.jpg)    


### public V get(Object key)  ###
这是一个反过程    
	
![](http://i.imgur.com/RCTnfV3.jpg)	      

### 回到构造函数 ###

![](http://i.imgur.com/6c8bDQM.jpg)    

###  ConcurrentModificationException###
	
	
	HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("12", "12");
        hashMap.put("123", "123");
        
        new Thread(() -> hashMap.put("1234", "1234")).start();

        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Iterator<String> iterator = hashMap.keySet().iterator();
        while (iterator.hasNext()) {

            System.out.println(iterator.next());
        }
运行抛出*ConcurrentModificationException*    
原因   

![](http://i.imgur.com/B7K4144.jpg)    

我们拿到的*keySet().iterator()*就是KeyIterator的实例    
modCount这个变量分析put方法的时候出现过，在remove方法，clear方法都有，只要对hashmap修改都会导致这个值自加      

正确使用线程安全的map   

		
	HashMap<String, String> map = new HashMap<>();
        map.put("12", "12");
        map.put("123", "123");

        Map<String, String> hashMap = Collections.synchronizedMap(map);

        new Thread(() -> hashMap.put("1234", "1234")).start();

        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        synchronized (hashMap) {
            
            Iterator<String> iterator = hashMap.keySet().iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
        }




		

	
