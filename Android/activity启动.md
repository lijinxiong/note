## ActivityThread#main ##

![](http://i.imgur.com/ysj3zcg.jpg)    

在ActivityThread中值得关注的成员变量   

	
	//Binder本地对象，ams 与 应用程序进行通信的中介
	final ApplicationThread mAppThread = new ApplicationThread();
		
	final H mH = new H();
	private class H extends Handler......

## ActivityThread#attach ##

![](http://i.imgur.com/yoj5BRZ.jpg)      

## ActivityManagerProxy#attachApplication ##

![](http://i.imgur.com/gKX4xUU.jpg)     

将ApplicationThread 对象写入到Parcel对象的data中，然后ActivityManagerProxy的内部Binder对象mRemote向ActivityManagerService发送一个*ATTACH_APPLICATION_TRANSACTION*进程间通信请求   

上面的动作都是在Application 进程中执行的，下面的就会在ams中执行了      

..   
........   
...........   
..............    
.................   
[Android系统源代码情景分析](https://book.douban.com/subject/19986441/)     

然后ams向我们的应用进程发送一个*LAUNCH_ACTIVITY_TRANSACTION*进程通信请求     

然后我们的应用进程的ApplicationThread的scheduleLaunchActivity方法处理这个请求    
## ApplicationThread#scheduleLaunchActivity ##
   
![](http://i.imgur.com/j4WsTLv.jpg)   

最终通过mH(Handler实例)发送Message    

## ActivityThread#mH#handleMessage ##

![](http://i.imgur.com/MS6SZ9c.jpg)    

## ActivityThread#handleLaunchActivity ##

![](http://i.imgur.com/VXpZWFb.jpg)    

1. unscheduleGcIdler   

	从主线程的MessageQueue中的IdleHandler list中移除 用于gc的IdleHandler   
2.  WindowManagerGlobal.initialize();
	
	初始化wms，得到wms实例       
	![](http://i.imgur.com/6xCYMQN.jpg)   
 
## ActivityThread#performLaunchActivity ##

![](http://i.imgur.com/yLcV1dV.jpg)    

1. ActivityInfo  
		

		/**
		 * Information you can retrieve about a particular application
		 * activity or receiver. This corresponds to information collected
		 * from the AndroidManifest.xml's &lt;activity&gt; and
		 * &lt;receiver&gt; tags.
		 */
包含一个特殊activity/receiver 的一些信息，跟你在AndroidManifest文件写的是一样的   
就是描述启动Activity的一些信息咯    
2. ComponentName  	
		
		
		/**
		 * Identifier for a specific application component
		 * ({@link android.app.Activity}, {@link android.app.Service},
		 * {@link android.content.BroadcastReceiver}, or
		 * {@link android.content.ContentProvider}) that is available.  Two
		 * pieces of information, encapsulated here, are required to identify
		 * a component: the package (a String) it exists in, and the class (a String)
		 * name inside of that package.
		 * 
		 */
描述着Activity/BroadcastReceiver/ContentProvider/Service 其中一个的信息，分别是

	
		private final String mPackage;//描述包的路径   
	    private final String mClass;//类名
3. Instrumentation    
	
	[介绍Instrumentation](http://blog.csdn.net/nemo__/article/details/50528249)
	
	
		 public Activity newActivity(ClassLoader cl, String className,
            Intent intent)
            throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
     		   	return (Activity)cl.loadClass(className).newInstance();
    	}
通过反射创建了一个Activity    

4. Application app = r.packageInfo.makeApplication(false, mInstrumentation);   
	packageInfo 是一个LoadedApk的实例    
	然后再这个方法里面   

		 ContextImpl appContext = ContextImpl.createAppContext(mActivityThread, this);
            app = mActivityThread.mInstrumentation.newApplication(
                    cl, appClass, appContext);
	创建了一个全局的Context对象，还有一个Application对象      
	在newApplication方法中调用了Application的attach方法，将Context对象赋值给ContextWrapper的成员变量mBase
	
		
		 static public Application newApplication(Class<?> clazz, Context context)
            throws InstantiationException, IllegalAccessException, 
            ClassNotFoundException {
        	Application app = (Application)clazz.newInstance();
    		........    	
        	return app;
    	}	
	
5.  activity.attach  
	我们直接分析attach方法   
	
	![](http://i.imgur.com/CqW9LG6.jpg)    

6. mInstrumentation.callActivityOnCreate   
	
	
		prePerformCreate(activity);
        activity.performCreate(icicle);
        postPerformCreate(activity);
		//------
		final void performCreate(Bundle icicle) {
        	restoreHasCurrentPermissionRequest(icicle);
        	onCreate(icicle);//常见?
        	mActivityTransitionState.readState(icicle);
        	performCreateCommon();
    	}

------------------------
然后再回到ActivityThread#handleLaunchActivity中    

![](http://i.imgur.com/VXpZWFb.jpg)   
来到最后一个handleResumeActivity方法
## handleResumeActivity ##
在handleResumeActivity中调用*Activity的performResume方法*，然后在*performResumeActivity方法*调用Activity的performRestart，在*performRestart*调用Activity的*performStart*，然后再*performStart*调用Activity的onStart方法。   
然后回到*Activity的performResume方法*中，再通过Instrumentation调用Activity的onResume方法    
然后回到 *handleResumeActivity*，在Activity调用*onResume*之后   
	
![](http://i.imgur.com/xu2Rxoz.jpg)     

	
	void makeVisible() {
        if (!mWindowAdded) {
            ViewManager wm = getWindowManager();
            wm.addView(mDecor, getWindow().getAttributes());
            mWindowAdded = true;
        }
        mDecor.setVisibility(View.VISIBLE);
    }
然后我们才能看到我们的界面   
## 最后 ##

带我目的性去看才有意义
	
			
		
	


	
