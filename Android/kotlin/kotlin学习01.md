![](http://i.imgur.com/FmZevuj.png)   
## 任务 ##
- 开发ide
- helloworld
- 基本数据类型
- 定义局部变量
- 基本数据类型的包装类型
- 数组
### 开发ide ###
 intellij idea
### helloworld ###
![](http://i.imgur.com/TikpELX.png)  
![](http://i.imgur.com/6r4DZS3.png)   

在src目录下点击new->Kotlin File/Class ,创建一个后缀为kt的文件     

![](http://i.imgur.com/C9BmRep.png)  

输入main，按下enter键 

	fun main(args: Array<String>) {
	}
快捷键      

![](http://i.imgur.com/plwZ4L3.png)    

上面的main函数就相当于java的main函数，是程序的入口函数   

	//java main 函数
	public static void main(String[] args) {      
    }

-----
下面解释下kotlin的main函数   

- **fun**：声明函数的关键字   
- **main**：函数名称
- **args**：args就是参数名，跟java代码中的args一样
- **:**  冒号，在这里就是参数名称和类型之间的间隔
- **Array< String >**:Array< T >,相当于java的数组(略微不同)，T泛型（代表里面存什么类型数据）。而String和Java里面的String一样（略微不同），整个意思跟java中的一样，一个存放String类型的数组  

--------   
写代码   	
	
	fun main(args: Array<String>) {
    	println("Hello world")//打印功能函数
	}
在kotlin中分号是可选的，当你一行只写一段单独可执行的代码段的时候。当然你可以一行写两个   

	fun main(args: Array<String>) {
    	println("Hello world");println("Hello world")
	}
那么分号是必须的（但是很少这么干）   
### 基本数据类型 ###
![](http://i.imgur.com/bdjKqDF.png)   

Kotlin 和 Java的基本数据类型对应关系  

![](http://i.imgur.com/2dfrfel.png)   

我们可以看到这些基本数据类型都是继承Number，都是放在以个Primitives的kt文件中     
对于Java中，一个byte的变量可以直接赋值给int类型的变量，其中涉及的就是隐式转型（就是不用手动的显式地**int d = (int)c;**）     
	
	byte c = 12;
    int d = c;
但是在kotlin中已经是不行了，
	
	var var_float: Float = 1.0f
    var var_double: Double = 1.0

	//    var_float = var_double
	//    var_double = var_float
在kotlin中，已经没有这种隐式转型了，只能显式的转型    

![](http://i.imgur.com/iQ39RxM.png)  

Number抽象类已经定义好这些显示转型的方法了
	
	var_float = var_double.toFloat()

###定义局部变量 ###
关键字 **var** 和 **val**  

- **var** 被它修饰的变量属性可读可写  
- **val** 被它修饰的变量属性可读，但是只能被赋值一次（相当于java的final）   

声明变量需要在变量名称前加上上面两个关键字中的一个修饰
	
	//局部变量

    var var_byte: Byte = 1
    var var_short: Short = 1
    var var_int: Int//没有进行初始化
    var_int = 1//进行初始化
    var var_long = 1L //会推断出是Long
    var var_char:Char = '1'
    var var_double: Double = 1.0
    var var_float:Float = 1.0f
    //可多次赋值
    var_byte = 2

	/****************/
    val val_byte: Byte = 1
    val val_short: Short = 1
    val val_int: Int = 1
    val val_long:Long = 1L
    val val_char:Char = '1'
    val val_double: Double = 1.0
    val val_float:Float = 1.0f
	//    val_byte = 2 报错，只能赋值一次

	print(var_byte)
	print(val_int)
	
	 var no_value:Int
	//println(no_value)//使用没赋值的变量会提示报错

	
格式：    **var** / **val**  变量名 : **[类型]** = [初始值]  

其中的类型和初始值是可选的，初始值可往后再写，不一定在声明的时候写上     
### 基本数据类型的包装类型 ###
*和上面的基本类型一样，不能进行隐式的转型，只能显示转型*  

在Java中int 对应的包装类型是Integer，那么在kotlin怎么对应呢   

![](http://i.imgur.com/fqdSiF2.png)   

图片中的?是什么意思呢

拥有这个 **?** 标识的类型的变量它的值可以为null，那么没这个?的就是不能吗，答案是的     
在kotlin中，类型分为nullable 和 non-null，**?**就是标识符   
	
	    var var_int: Int? = null
	// val val_int:Int = null //null 不能成为一个non-null类型Int的值
所以一开始main函数上的java的String和kotlin的String有点不同，Java中的String类型等于kotlin的类型是String?,而不是String

既然这些值可以为空，那么怎么进行检验调用才不会npe呢？
#### ==null ####
跟java一样
	
	if (var_int == null) {
    }
#### 安全调用 ####
kotlin号称空安全，就是有这个安全调用   
	
	var_int?.inc()
**?.**的意思就是：假如这个变量不是null的话就调用后面的函数（例子就是调用inc函数），否则就不调用    
#### elvis操作符 ####
在java中是不是有这样的三元操作符  
	
	    int a = 12;
        int b = a > 12 ? a : 12;
在kotlin的判空调用就有类似的elvis操作符   
	
	    val a = var_int?.inc() ?: -1
当var_int不是null的时候就调用inc(),假如是null的话，就返回-1给变量a   
是不是跟java很像   
#### !! ####
	
	var_int!!.inc()
这个操作符的结果就是：当你的var_int 不是null的时候就正常调用inc(),是null的时候就抛出异常（npe）
### 数组 ###
在Java中数组用 **[]**表示，在kotlin中并没有这个符号。kotlin中使用Array代表着数组，但是又有点不同，但是对于基本数据类型，kotlin专门设计了一些类放置   
	
![](http://i.imgur.com/s6MUski.png)   

可以看到这里的IntArray，DoubleArray和 上面说的Array类没有一点关系的

	   val int_array :IntArray?= intArrayOf(1,2,3)
这样子就会产生一个一个IntArray，其他的基本类型也有类似的方法产生对应的Array。这个函数系统提供专门生成Array，很少使用构造函数生成   
上面收说到Array和Java中的数组有点差别：   
	
	//java代码
	String[] strings = new String[12];
    Object[] objects = strings;
但是呢，在Kotlin的数组单纯这样是不行的   
	
	//kotlin
	var array1: Array<Int> = arrayOf(1,2,3)
    var array: Array<Number> = array1
    //我们知道Int是Number的子类
为什么，下一篇介绍   

---
这篇涉及的知识点：  

1. kotlin的入口函数是main  
2. 函数的声明关键字是**fun**
3. **;** 在kotlin并非是必要的
4.  java 基本类型 和 kotlin 对应关系
 
![](http://i.imgur.com/bdjKqDF.png)  
5. kotlin中，不管是基本数据类型还是基本数据类型对应的包装类型都不支持隐式转型了，需要显式转型   
6. **var** 可变变量修饰符  
7. **val** 不可变变量修饰符（只能赋值一次）     
8. 定义局部变量：**var / val** 变量名 : 类型 = 初始值  （类型是可选的）    
9. kotlin的类型分为两种，一种是nullable的一种是non-null,non-null变量的值不能为null   
10. 类型? 代表着 这个变量的值可能是null  （val a : Int? = null）  
11. 判断是否为空    

	①==null
	
		if(var_nullable == null)
	②安全调用   
		
		var_nullable?.inc()
	③Elvis（安全调用的的扩展）
		
		var_nullable?.inc()?:-1
	④!!
		
		var_nullable!!.inc()
**最后**：
Java中的数组在kotlin的表示Array，特别的，基本类型有对应的类（IntArray，DoubleArray，但是和Array类无关）  


尽量一天一篇!!  

2017/5/26 0:07:24 