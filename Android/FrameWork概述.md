**此文为[Android内核剖析](https://book.douban.com/subject/6811238/)学习**
## 导语 ##
任何控制程序都有一个入口，C程序的入口只能是main函数，并且只能有一个main函数，Java的入口静态函数main      

依赖于操作系统的程序，客户端除了包含一个入口外，还需要与相关的系统服务一起运行，以完成指定的任务，win32 程序需要和Gui 系统服务一起实现带窗口的功能      

那么Android 程序也是不例外，程序入口在哪呢      
## Framework 框架 ##

Framewoke 定义了客户端组件和服务端组件功能以及接口       
框架中包含三个主要部分，服务端，客户端，Linux驱动      

### 服务端 ###
服务端最主要两个类*WindowManagerService(WmS)*和ActivityManagerService(AmS)     
wms负责各个窗口的叠放次序，隐藏或显示窗口     
ams 管理所有应用程序的Activity     

### 客户端 ###

- ActivityThread 该类为应用程序主线程类，所有的apk 有且仅有一个ActivityThread类，程序的入口为该类的static main 函数。ActivityThread 所在的线程称为UI线程/主线程        
- Activity 该类作为apk程序最小的运行单元，换句话说就是主线程动态加载可执行代码的最小单元类，一个apk可包含多个Activity，ActivityThread会根据用户动作选择打开相应的Activity    
- PhoneWindow 继承Window 包含了一个DecorView对象，DecorView父类为FrameLayout，因此PhoneWindow包含一个View对象，并提供一组通用窗口操作API    
- Window 类，提供一组通用的窗口操作API，这里的窗口仅仅是客户端程序层面上的，Wms所管理的窗口并不是Widow类，而是一个View 或ViewGroup类，对于PhoneWindow而言就是DecorView，Window是abstract    
- ViewRoot(ViewRootImpl) ：wms管理客户端窗口的时候，需要通过IPC通知客户端进行某种操作，而客户端收到这个通知会转换为一个异步调用，那么久需要使用到Handler了。ViewRoot就是继承Handler的(ViewRootImpl内含一个Handler类对象)，作用就是把IPC调用转化为本地异步调用（[飞机票](https://android.googlesource.com/platform/frameworks/base.git/+/b2a3dd88a53cc8c6d19f6dc8ec4f3d6c4abd9b54/core/java/android/view/ViewRoot.java)）      
- W类：继承Binder，是ViewRoot的一个内部类 Wms通过ipc通知客户端的时候就要调用到Binder，然后Binder内部发送一个Handler消息，进行异步处理    
- WindowManager：客户端申请创建一个窗口，需要通过windowManager 然后wms是具体的创建一个窗口
  
### Linux驱动 ###
主要的部分是SurfaceFlingger(SF)和Binder，每一个窗口对应一个Surface，而sf的作用就是把各个Surface显示在同一个屏幕上。Binder的作用就是进程间通信的'桥梁'    
     
## APK程序的运行过程 ##
[飞机票](http://www.jianshu.com/p/fb4dfb53e579)

## 客户端中的线程 ##
客户端至少包含三个线程,ApplicationThread（继承Binder），RootView.W(继承Binder)，这两个类在app启动的时候被实例化，然后开启各自开启一个线程用于接收ipc通信(一个与ams，一个与wms)，还有一个线程就是ui线程了     
![](http://i.imgur.com/7HEP7Wb.png)    

## 几个常见的问题 ##
### Activity之间如何传递消息 ###

这个提问本身有问题，一般来说“传递消息”一般是指多个线程之间，而Activity本身并不是线程，只是一个类而已(比较特殊的类而已)。     

那么对于类，怎么传递消息？     

1. 聚合/组合呗    
2. 第三方类保存两个Activity之间的需要传递的数据

然而对于Activity一般不能直接由程序员实例化(Framework实例化)，那么只能是第二种方法    
*Intent*  类就是这个第三方类    

### 窗口相关的概念 ###

- 窗口(非Window类)：这是一个纯语义的说法，即程序猿所看到的屏幕上的独立的界面，比如一个带有TitleBar 的Activity界面，一个对话框，一个Menu菜单等。这些都能称为窗口。但是从wms角度来说，窗口时接收用户消息的最小单元，wms内部用一个特定的类表示窗口，而给wms中添加一个窗口就是调用wm的addView方法。从wms来说添加一个窗口就是添加一个View。wms收到用户的消息，判断是哪个窗口也就是View对象的，通过IPc传个ViewRoot的W类    
- Window类：一个abstract 类，该类抽象了“客户端窗口”的基本操作，并且定义了一组Callback接口，Activity实现了这些接口才有机会获得处理用户消息的机会。消息一开始是由wms传递给View对象的    
- ViewRoot 客户端用于跟wms交互，wms没管理一个View都会创建一个ViewRoot（我们单纯的一个Activity只对应一个ViewRoot对象）     
- W 类是ViewRoot类的一个内部类 ，Binder子类，wms IPc通信    


> [http://blog.csdn.net/android_jiangjun/article/details/45798221](http://blog.csdn.net/android_jiangjun/article/details/45798221)
