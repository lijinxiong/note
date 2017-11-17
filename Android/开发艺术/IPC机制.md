## 简介 ##
IPC inter-Process Communicate ，进程间通信，是指两个进程间交换数据的过程   
线程:CPU调度的最小单元，同时线程是一种有限的系统资源     
进程：一个执行单元，在pc和移动设备指的是一个程序或者一个应用     
一个进程可以包含多个线程，进程和线程的关系是包含和被包含的关系    
最简单的情况，一个进程只有一个线程，主线程，在Android中叫做UI线程，在UI线程才能操作界面元素        
IPC 不是Android中特有的，任何操作系统都有相应的IPC机制，比如Window上通过剪切板，管道..进行进程间通信 ；Linux通过命名管道，共享内存，信号量进行进程间通信    
在Android中最有特色的进程间通信就是Binder，通过Binder实现进程间通信(Socket也可以实现进程间通信)      
   
## Android中的多进程模式 ##

android:process

	
	<activity
            android:name=".first.MainActivity"
            android:configChanges="orientation"
            android:excludeFromRecents="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN"/>

                <category android:name="android.intent.category.LAUNCHER"/>
            </intent-filter>
        </activity>
        <activity android:name=".second.Main2Activity"
                android:process=":remote"
            >
        </activity>
        <activity android:name=".second.Main3Activity"
                    android:process="asd.dsf.sdf.gd"
            >
        </activity>
*:remote* 在当前的进程名加上当前的包名，私有进程     
*asd.dsf.sdf.gd*  进程名字就是这个，共有进程，通过ShareUID其他应用可以跟他在同一个进程运行    

---------
Android 为每一个进程分配了一个独立的虚拟机，不同虚拟机在内存分配上有不同的地址空间      


多进程带来的问题 ： 

1. 静态成员和单例模式完全失效   
2. 线程同步机制完全失效    
3. SharedPreferences可靠性下降   
4. Application多次创建    

## IPC基础概念介绍 ##

### Serializable ###
Serializable是Java提供的序列化接口，空接口，为对象提供序列化和反序列化     
使用Serializable 来实现序列化非常简单，只要在类声明中指定一下的标识可以实现默认的序列化过程     

	
	public class User implements Serializable {
	    private static final long serialVersionUID = 4564654646L;    
	}


	public class User implements Serializable {
	    private static final long serialVersionUID = 4564654646L;
	
	    private String name;
	    private int age;
	
	    public User method1() throws IOException, ClassNotFoundException {
	
	        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
	
	        ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
	
	        objectOutputStream.writeObject(this);
	
	
	        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrayOutputStream.toByteArray());
	
	        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
	
	        return (User) objectInputStream.readObject();
	
	    }
	
	}
serialVersionUID作用：序列化的时候系统会把当前的serialVersionUID写入到序列化文件中，当反序列化的时候系统回去检测文件中的serialVersionUID，看是否一致，如果一致说明版本是一致的。否则可能就是当前类修改某些属性，就会导致无法反序列化     
那么不为这个serialVersionUID赋值会发生啥，当我们没有赋值的时候，系统在反序列化的时候，假设我们的类修改了，那么系统会重新计算当前类的hash值并赋值给serialVersionUID，那么直接导致我们反序列失败，但是我们指定一个固定值，当我们假设删去某个属性的时候，serialVersionUID不修改，还是能最大限度的反序列化对象       
*transient*修饰的不参与反序列    

可以重写writeObject/readObject改变系统的序列/反序列过程    

## Parcelable ##
android 提供一种也可序列化的接口。实现此接口的，还可以通过Intent/Binder传递    

	

	public class Person implements Parcelable {
	
	    private String name;
	    private int age;
	    private Book book;
	
	    public Person(String name, int age, Book book) {
	        this.name = name;
	        this.age = age;
	        this.book = book;
	    }
	
	    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
	        @Override
	        public Person createFromParcel(Parcel source) {
	            return new Person(source);
	        }
	
	        @Override
	        public Person[] newArray(int size) {
	            return new Person[size];
	        }
	    };
	
	    public Person(Parcel source) {
	
	        name = source.readString();
	        age = source.readInt();
	        //当前线程的上下文类加载器
	        book = source.readParcelable(Thread.currentThread().getContextClassLoader());
	    }
	
	
	    @Override
	    public int describeContents() {
	        return 0;//大多数是0
	    }
	
	    @Override
	    public void writeToParcel(Parcel dest, int flags) {
	        dest.writeString(name);
	        dest.writeInt(age);
	        dest.writeParcelable(book, 0);
	    }
	}

有个叫Android Parcelable code generator的插件   
Intent Bundle ，Bitmap 都是可直接序列化的。List，Map 也可以只要它的元素都是可序列化的      

-------
比较    
Serializable：简单开销大，大量IO操作    
Parcelable：适合Android 平台，使用麻烦效率高   
将对象序列化到存储设备中或将序列化后通过网络传输，建议Serializable    
    

## Binder ##

Binder是Android中的一个类，实现了IBinder接口     

从IPc角度看，Binder是Android中的一种跨进程通信的方式     

从FrameWork 角度看，Binder是Service Manager 连接各种Manager(ActivityManager，WindowManager，等等)和相应ManagerService的桥梁    

从应用层看，Binder是客户端和服务端进行通信的媒介，当bindService的时候，服务端会返回一个包含服务端调用的Binder对象，通过这个Binder对象，客户端就可以获取服务端提供的服务/数据      


![](http://i.imgur.com/jSfxiGB.png)      
   
![](http://i.imgur.com/DyZgG0F.png)     
	
	//Book.aidl
	package lao.yu.xian.developart02;
	parcelable Book;
	//IBookManager
	// IBookManager.aidl
	package lao.yu.xian.developart02;
	import lao.yu.xian.developart02.Book;
	// Declare any non-default types here with import statements
	
	interface IBookManager {
	
	
	   List<Book> getBookList();
	   void addBook(in Book book);
	
	}

生成的IBookManager.java     
![](http://i.imgur.com/B7v22Xq.png)    
这个.java文件还是一个接口，继承了android.os.IInterface 这个接口 ，所有在Binder中传输的接口都要继承android.os.IInterface 接口       
声明了我们在aidl中声明的两个方法，还声明了一个抽象类，继承Binder，实现了IBookManager这个接口    
而在Stub这个抽象类中，有着两个代表getBookList 和 addBook方法的常数    


	static final int TRANSACTION_getBookList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
        static final int TRANSACTION_addBook = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);   

用于标识在IPC过程中调用的是哪个方法        

	