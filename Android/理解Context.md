**此文为[Android内核剖析](https://book.douban.com/subject/6811238/)学习**    

## Context是啥 ##
一个Context意味着一个场景，一个场景就是用户和操作系统的交互的过程。比如你打电话时，场景包括电话程序对应的界面，以及隐藏在界面后面的数据；当你看短信的时候，场景包括短信界面，以及隐藏在后面的数据     

一个Activity，Service 也是一个Context     

用户和系统的每一次交互都是一个场景，比如打电话，发短信，这些都是有界面的场景，也有没有界面的场景（service）。一个应用程序可以认为是一个工作环境，用户可以在这个工作环境中切换到不同的场景
 
Activity，Service 都是基于Context的。Activity除了基于Context类外，还实现了其他的重要接口，从设计的角度来看，interface仅仅是现实了某些功能，而extends才是类的本质   

## 一个应用进程包含几个Context对象 ##

Context数量 = Activity数量+Service数量+ 应用进程数量

## Context相关类的继承关系 ##

![](http://i.imgur.com/YK8cSCg.png)            


- Context abstract类  
- ContextImpl 真正实现了Context中的所有函数，应用程序调用的各个Context类方法都是来自该类的实现       
- ContextWrapper Context包装类，里面包含着一个Context对象，可以通过attachBaseContext方法指定。这个包含关系用了组合关系在UML图中(感觉比聚合适合)
	![](http://i.imgur.com/IeGMxwd.png)     

- ContextThemeWrapper 其内部增加了一些与主题相关的接口，这里的主题指的是Application/Activity中的主题     

### Application的Context ###
[我的相关文章](http://www.jianshu.com/p/fb4dfb53e579)    

1.ActivityThread#performLaunchActivity   

	
	Application app = r.packageInfo.makeApplication(false, 
	mInstrumentation);     
2.LoadedApk#makeApplication

![](http://i.imgur.com/tNBRMes.png)    

3.Instrumentation#newApplication   


	Application app = (Application)clazz.newInstance();
	app.attach(context);
	-----
	//Application.attach()
	final void attach(Context context) {
        attachBaseContext(context);//设置ContextWrapper的mBase,上面说过
        mLoadedApk = ContextImpl.getImpl(context).mPackageInfo;
    }
4.*appContext.setOuterContext(app);*这里是调用ContextImpl中的方法，设置双向关联吧，有些处理可能需要回到ContextWrapper的子类中进行    
  
### Activity的Context ###
1..ActivityThread#performLaunchActivity 
	

	//创建Activity实例
	activity = mInstrumentation.newActivity(
                    cl, component.getClassName(), r.intent);

	//创建ContextImpl并赋值个Activity的父类ContextWrapper中的mBase
	Context appContext = createBaseContextForActivity(r, activity);
	
	//调用Activity的attach方法
	activity.attach(appContext.....);
	//attach中
	attachBaseContext(context);
跟Application差不多    

### Service的Context ###
ActivityThread#handleCreateService
	
	
	service = (Service) cl.loadClass(data.info.name).newInstance();
	ContextImpl context = ContextImpl.createAppContext(this, packageInfo);
	service.attach(context, this, data.info.name, data.token, app,
                    ActivityManagerNative.getDefault());
	//Service attach
	attachBaseContext(context);
三个ContextWrapper子类的流程基本是一致的     
