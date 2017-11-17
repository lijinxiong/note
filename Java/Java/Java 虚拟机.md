## -verbose:class|gc|jni 参数详解 ##
1. -verbose:class   
等价于 -verbose    
监视程序运行的时候有多少类被加载，输出虚拟机装入类的信息
	
		[Opened C:\Program Files\Java\jdk1.8.0_66\jre\lib\rt.jar]
		[Loaded java.lang.Object from C:\Program Files\Java\jdk1.8.0_66\jre\lib\rt.jar]
		[Loaded java.io.Serializable from C:\Program Files\Java\jdk1.8.0_66\jre\lib\rt.jar]
		[Loaded java.lang.Comparable from C:\Program Files\Java\jdk1.8.0_66\jre\lib\rt.jar]
		[Loaded java.lang.CharSequence from C:\Program Files\Java\jdk1.8.0_66\jre\lib\rt.jar]
		[Loaded java.lang.String from C:\Program Files\Java\jdk1.8.0_66\jre\lib\rt.jar]
		[Loaded java.lang.reflect.AnnotatedElement from C:\Program Files\Java\jdk1.8.0_66\jre\lib\rt.jar]
		[Loaded java.lang.reflect.GenericDeclaration from C:\Program Files\Java\jdk1.8.0_66\jre\lib\rt.jar]
		..........

2. -verbose:gc
在虚拟机发生内存回收时在输出设备显示信息，监视虚拟机内存回收情况
	
		[GC (System.gc())  4672K->880K(125952K), 0.0046069 secs]	
		[Full GC (System.gc())  880K->813K(125952K), 0.0074580 secs]

3. -verbose:jni
输出调用本地方法的相关信息，监视虚拟机调用本地方法的情况

		[Dynamic-linking native method java.lang.Object.registerNatives ... JNI]
		[Registering JNI native method java.lang.Object.hashCode]
		[Registering JNI native method java.lang.Object.wait]
		[Registering JNI native method java.lang.Object.notify] 


## -Xms 和 -Xmx -Xmn##
**-Xms** 设置虚拟机初始的堆内存大小，默认为 物理内存的1/64      
**-Xmx** 设置虚拟机最大的堆内存大小，默认为 物理内存的1/4      
默认当空余的堆内存 大约 小于40% 时， 就会慢慢增加到 -Xmx 的值    
默认当空闲的内存 大约大于70%时，就会慢慢减少到至-Xms的值     
所以可以设置-Xms -Xmx 为相同的值，避免每次GC 之后调整堆的大小    

本机内存8G ，**jdk1.8**    
	
	Heap
	 PSYoungGen      total 38400K, used 5338K [0x00000000d5e00000, 0x00000000d8880000, 0x0000000100000000)
	  eden space 33280K, 16% used [0x00000000d5e00000,0x00000000d63368c0,0x00000000d7e80000)
	  from space 5120K, 0% used [0x00000000d8380000,0x00000000d8380000,0x00000000d8880000)
	  to   space 5120K, 0% used [0x00000000d7e80000,0x00000000d7e80000,0x00000000d8380000)
	 ParOldGen       total 87552K, used 0K [0x0000000081a00000, 0x0000000086f80000, 0x00000000d5e00000)
	  object space 87552K, 0% used [0x0000000081a00000,0x0000000081a00000,0x0000000086f80000)
	 Metaspace       used 3293K, capacity 4496K, committed 4864K, reserved 1056768K
	  class space    used 359K, capacity 388K, committed 512K, reserved 1048576K	
最小的堆的大小 8 * 1024 / 64 = 128M   

根据log 计算   (eden + from survivor + to survivor + OldGen  ) /1024= (33280+5120*2+87552)/1024 = 131072 / 1024 = 128     

(至于为什么不是PSYoungGen + ParOldGen 或者说 为什么PSYoungGen ！= eden + from survivor + to survivor后面讲)          

**-Xmn** 堆中新生代的 内存大小，而剩余的堆内存就分配给老年代
	
	-verbose:gc -Xms100m -Xmx100m -Xmn60m -XX:+PrintGCDetails
	打印信息
	Heap
	 PSYoungGen      total 53760K, used 6456K [0x00000000fc400000, 0x0000000100000000, 0x0000000100000000)
	  eden space 46080K, 14% used [0x00000000fc400000,0x00000000fca4e0a0,0x00000000ff100000)
	  from space 7680K, 0% used [0x00000000ff880000,0x00000000ff880000,0x0000000100000000)
	  to   space 7680K, 0% used [0x00000000ff100000,0x00000000ff100000,0x00000000ff880000)
	 ParOldGen       total 40960K, used 0K [0x00000000f9c00000, 0x00000000fc400000, 0x00000000fc400000)
	  object space 40960K, 0% used [0x00000000f9c00000,0x00000000f9c00000,0x00000000fc400000)
	 Metaspace       used 3293K, capacity 4496K, committed 4864K, reserved 1056768K
	  class space    used 359K, capacity 388K, committed 512K, reserved 1048576K

那么这里新生代占60m 老年代占40M
同样的也可以通过比例设置新生代和老年代的大小    
**-XX:NewRatio**     
-XX:NewRatio=2 意思就是 新生代：老年代 = 1：2，也就是新生代占堆大小的1/3，而老年代占2/3
	
	-verbose:gc
	-Xms200m
	-Xmx200m
	-XX:NewRatio=2
	-XX:+PrintGCDetails
	-XX:SurvivorRatio=6
	-XX:MetaspaceSize=100m

	打印    
	Heap
	 PSYoungGen      total 59904K, used 7240K [0x00000000fbd80000, 0x0000000100000000, 0x0000000100000000)
	  eden space 51712K, 14% used [0x00000000fbd80000,0x00000000fc492198,0x00000000ff000000)
	  from space 8192K, 0% used [0x00000000ff800000,0x00000000ff800000,0x0000000100000000)
	  to   space 8192K, 0% used [0x00000000ff000000,0x00000000ff000000,0x00000000ff800000)
	 ParOldGen       total 136704K, used 0K [0x00000000f3800000, 0x00000000fbd80000, 0x00000000fbd80000)
	  object space 136704K, 0% used [0x00000000f3800000,0x00000000f3800000,0x00000000fbd80000)
	 Metaspace       used 3185K, capacity 4496K, committed 4864K, reserved 1056768K
	  class space    used 351K, capacity 388K, committed 512K, reserved 1048576K


	
而在新生代中又分为 eden区，from survivor 和 to survivor ，默认情况下(自己测试)，eden 占新生代的0.75 ，剩下的 from 和to平分

**-XX:SurvivorRatio=8**   
这个配置的意思就是 一个Survivor  和 eden 的大小比为 1： 8，而默认的eden 占新生代的0.75 就相当于-XX:SurvivorRatio=6   
    
	-verbose:gc -Xms200m -Xmx200m -Xmn100m -XX:+PrintGCDetails -XX:SurvivorRatio=6
	Heap
	 PSYoungGen      total 89600K, used 7684K [0x00000000f9c00000, 0x0000000100000000, 0x0000000100000000)
	  eden space 76800K, 10% used [0x00000000f9c00000,0x00000000fa381110,0x00000000fe700000)
	  from space 12800K, 0% used [0x00000000ff380000,0x00000000ff380000,0x0000000100000000)
	  to   space 12800K, 0% used [0x00000000fe700000,0x00000000fe700000,0x00000000ff380000)
	 ParOldGen       total 102400K, used 0K [0x00000000f3800000, 0x00000000f9c00000, 0x00000000f9c00000)
	  object space 102400K, 0% used [0x00000000f3800000,0x00000000f3800000,0x00000000f9c00000)
	 Metaspace       used 3293K, capacity 4496K, committed 4864K, reserved 1056768K
	  class space    used 359K, capacity 388K, committed 512K, reserved 1048576K

## Minor GC  和 Full GC##
- Minor GC （新生代GC） ：指发生在新生代的垃圾收集动作，因为Java 对象大都具备朝生夕灭的特性，所以Minor GC 非常频繁，一般回收速度也比较快
- Full GC/Major GC（老年代GC）：指发生在老年代的GC，出现了Full GC 经常伴随至少一次的Minor GC（但是并非绝对，Parallel Scavenge 收集器的收集策略可以直接选择Major GC 的 策略），MajorGC 一般比Minor GC 慢10倍   







- 相关链接  
[http://www.cnblogs.com/happyPawpaw/p/3868363.html](http://www.cnblogs.com/happyPawpaw/p/3868363.html)   
[http://www.cnblogs.com/smyhvae/p/4736162.html](http://www.cnblogs.com/smyhvae/p/4736162.html)
[http://www.cnblogs.com/smyhvae/p/4744233.html](http://www.cnblogs.com/smyhvae/p/4744233.html)   
[http://hllvm.group.iteye.com/group/topic/37095](http://hllvm.group.iteye.com/group/topic/37095)    
[http://www.cnblogs.com/redcreen/archive/2011/05/04/2037057.html](http://www.cnblogs.com/redcreen/archive/2011/05/04/2037057.html)   


