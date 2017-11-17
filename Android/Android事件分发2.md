![](http://i.imgur.com/NyI6fuy.jpg)     

--------
先来看看两个结论吧，再看源代码  
   
![](http://i.imgur.com/fsetm0o.png)   

还有一个就是在默认的情况下，一个事件序列(down，move，up)只能交由给一个view消费   
因为代码太长不好分析，只好截图分析了   
分析down事件的
	
![](http://i.imgur.com/QPSkutc.png)          
     
分析move事件     

![](http://i.imgur.com/JsVyxI3.png)     


2017/5/28 20:11:45 

本来早上写着的是事件分发的流程的，从Activity到view，但是后来发现，主要的还是在ViewGroup中   


