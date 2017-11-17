## 典型生命周期 ##

1. onCreate ：生命周期的第一个方法，做一些初始化的工作，如setContentView()，初始化Activity所需的数据    
2. onRestart：Activity重新启动，Activity从不可见转为可见，onStart被调用，onPause和onStop被调用，用户重新回到这个Activity    
3. onStart：不可见      
4. onResume：这个方法之后decor才添加到wms中(添加之后才可见)，不一定非得和onStart比较    
5. onPause：表示Activity正在停止，一般接着调用onStop，但是当你打开新的Activity是透明的，onStop并不会执行。当前的Activity执行完这个方法，下个Activity才会执行onCreate      
6. onStop：即将停止，可以做一些重量级回收，这个方法的前一个方法是保存Activity状态的方法*onActivitySaveInstanceState*     
7. onDestory：Activity生命周期最后一个方法，释放回收资源，特别是Activity的引用   

![书本图片](http://i.imgur.com/0iZxg5H.png)     

1. 第一次启动：onCreate->onStart()->onResume
2. 打开新的Activity onPause->onStop ，新Activity是透明主题就不会执行onStop
3. 回到原Activity，onRestart->onStart->onResume
4. back ,onPause->onStop->onDestroy    
5. 熄灭屏幕调用onPause -> onStop   
6. onPause 调用之后，当前Activity就不在前台了，可能可见(不能与用户交互),onStop执行之后，不可见（可见说的是对用户而言）     
7. 旧Activity onPause  到新的onCreate->onStart()->onResume 然后旧的onStop(有必要的话)   
8. 异常结束的Activity 会通过*onActivitySaveInstanceState*把数据保存在Bundle中，重建的时候在把这个Bundle给onRestoreInstanceState和onCreate，就可以取出数据恢复。Activity    
的*onActivitySaveInstanceState*会委托Window去保存数据，然后DecorView，一层一层下去  
9. *onRestoreInstanceState*被调用Bundle一定不会空，onCreate就不一定了，所以在*onRestoreInstanceState*恢复数据      
10. 资源不足导致低优先级的Activity被杀死，高到低，前台-可见-后台，当系统内存不足，就会按优先级杀死低的Activity所在的进程。然后还是会通过onActivitySaveInstanceState和onRestoreInstanceState 保存和回复数据 ，因此一个没有四大组件的进程很容易被杀死    
11.  我们可以设置*android:configChanges="orientation"*来使当手机配置发生改变的时候不会重新创建一个Activity    

## Activity的启动模式 ##

- standard：默认模式，无论栈中是否有实例，依旧创建实例    
- singleTop：栈顶复用模式，如果将要启动的Activity在栈顶，那么不会再创建新的    
- singleTask ：栈内复用，当将要启动的Activity在栈内，不会再创建新的，而且在这个Activity之上的Activity都会出栈    
- singleInstance 加强的SingleTask模式，它必须单独存在一个任务栈中     

-------------

1. 特殊情况   
	![](http://i.imgur.com/t9BW2a6.png)     

------------
### Activity的Flags ###

- FLAG_ACTIVITY_NEW_TASK    
	singleTask启动模式
- FLAG_ACTIVITY_SINGLE_TOP  
	SingleTop
- FLAG_ACTIVITY_CLEAR_TOP    
	与FLAG_ACTIVITY_NEW_TASK配合使用，已经存在的话清除以上的Activity还会调用newIntent()   
	而启动的Activity的启动模式是Standard，那么连同它会出栈然后重新启动Activity重新入栈   
-  FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
	我们打开任务菜单的时候会不显示在最近打开的应用中(试过但是没效果，原因没找)    
 
   
## IntentFilter的匹配规则 ##

1. 一个过滤表中可以有action，category，data 可以有多个   
2. 一个Activity可以有多个过滤表(intent-filter)   
3. 同时匹配action，category，data才算完全匹配    

#### action 的匹配规则 ####
action是字符串       
匹配成功就是字符串值完全一样    
只要Intent的action能和intent-filter中一个action相同就能够匹配成功    
区分大小写     

#### category的匹配规则 ####
category是一个字符串    
如果Intent出现了category，那么在Intent中的category必须都要出现在intent-filter中才算匹配成功     
默认是有个*android.intent.category.DEFAULT*    

#### data的匹配规则 ####
和action匹配规则一样，一个中就可以了   
由两部分组成:mimeType 和 URI    
	
	
	<scheme>://<host>:<port>/<path>
	content://com.example.project:200/folder/subfolder/etc
	http://baidu.com:80/search/info     

scheme:URI的模式，http，file，content，如果不指定，URI无效    
host: 主机名,例如www.baidu.com  ，不指定URI无效     
port:URI中的端口号，当指定了scheme和host才有效     
path:路径   

## 其他 ##
PackageManager.resolveActivity/Activity.resolveActivity返回最佳的匹配Intent的Activity     
PackageManager.queryIntentActivities 返回所有匹配成功的      



	


      