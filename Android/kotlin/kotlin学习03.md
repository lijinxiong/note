![](http://upload-images.jianshu.io/upload_images/2038754-6a85beec5c3b6bba.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)   
## 上篇回顾 ##
在上篇中我们学习了Java，Kotlin的协变和逆变，知道了泛型是不变的和pecs口诀   
## 任务 ##

1. 包  
2. 可见度修饰符   
3. 函数声明使用      
4. 扩展函数   
5. 中缀函数  
### 包 ###
kotlin 的包 和Java的包 都是一个馅的，说白了就是一个目录，为了方便管理项目和解决方法，类命名冲突引起的问题的   
	
	package xian.yu.lao
在整个kt文件的顶部，但是和Java不一样的是，在kotlin中类，对象，方法，属性，接口都可以直接在包内声明，不像Java只有类，接口，枚举能在包内声明   
	
	package xian.yu.lao

	fun function1() {
	
	}
	val val_int: Int = 1
	
	interface interfaces  {
	    
	}
	class myClass{
	    
	}
我们称们为**顶级的**   
### 可见度修饰符 ###
Kotlin的可见度修饰符也有四种，**public**，**protected**，**private**，**internal**     

- public:在任何地方都能被访问到，不写可见度修饰符**默认**就是这个    
- protected：不能修饰顶级，只能被子类访问和所在的上下文访问到   
- private：同一个源文件内访问到   
- internal：同一个module内的任何位置被访问到（module就是在Android开发那样，你创建一个project，默认是会一个app的module，你可以再创建一个lib的module）   
  
	package xian.yu.lao

	public fun function1() {//没必要，因为默认就是public
	
	}
	private val val_int: Int = 1//只能在这个源文件被访问
	
	internal interface interfaces  {//只能在这个module中被访问
	//
	}
	//protected class myClass{//报错，顶级的声明不接受protected修饰
	//
	//}
### 函数声明使用 ###
在第一篇的时候，我们就知道函数的声明需要用**fun**标识符，
	
	fun main(args: Array<String>) {
	}
我们知道函数的参数是放在函数名后面的括号中的   
	
	fun test(a:Int,b:Int){}
多个参数之间用**,**隔开，那么返回值写在哪呢？   
	
	fun test(a:Int):Int{}
返回值写在花括号之后**: T**,冒号加上返回的类型，上面的函数返回一个Int类型的值   
那么为什么main函数它没写上返回值的？因为有些函数只是实现某项功能，不涉及对调用者做出回应那么它就不返回值，就像Java的**void**。在kotlin中，用**Unit**表示不用返回任何类型的值，而且当返回值是**Unit**的时候我们可以省略掉   
	
	fun main(args: Array<String>) :Unit{
    	println("返回Unit，可以省略掉")
	}
这样写，依旧和省略掉一样，还是kotlin的入口函数   
还有一种情况可以省略掉的就是，系统能够从你返回值的推断出你的类型是啥，那么你就可以不用写返回值。那么需要被系统推断出返回值类型需要什么样的条件，那就是函数体是单一表达式    
	
	fun singleExp(a: Int, b: Int) = a > b
而且函数体的**{}**省略掉，换**=**，上面的函数系统就能推断出返回的值是Boolean类型   
或许有人这么玩   
	
	fun singleExp(a: Int, b: Int) = if (a> b) 1 else "12j" //if在Kotlin中是一个表达式，所以kotlin没有Java中的三元运算符
那么这个函数返回啥？返回Int 或者是String咯!!  
其实为什么只能是单一表达系统才能推断出返回值的类型呢，因为不是单一表达式的话，函数内部的控制流太复杂，运行的根本就不能推断出程序的流向，结果就很难推断   
所以在Kotlin中，能省略返回值类型不写的情况只有两种，一种是返回的类型是**Unit**，一种是单一表达式   

说完返回值，再说下参数   
在Kotlin中，参数可以用默认值，   
	
	fun test(a:Int,b:Int = 0){}
那么b这个参数的默认值就是0，当我调用这个函数的时候，假设我不想传参数b过去，那么b这个参数的值就是0  
	
	fun main(args:Array<String>){
		
		test(1)
	}
当你函数的参数有默认值的时候，那么你这个参数可以说是可以不存在的了     
但是当你的函数声明定义是这样的时候   
	
	fun test(a:Int = 0,b:Int){}
那么你**test(1)**调用就会报错，因为系统会认为你这个值是传个a这个参数的，那么这个怎么解决呢   
kotlin就出现了指定参数这个玩法了   
	
	test(b = 1)
就是你使用函数声明的参数名为这个参数赋值，这样子是不是很爽?（对于所有的函数都可以指定参数传值）   

记得Java还有一个变长参数的(貌似是这么称呼)，在kotlin中，当然必须也是有的    
	
	fun test(vararg a:Int){}
	//invoke
	test(1,2,3)   
其实内部保存这些参数的是一个**Array<out T>**(*用到了第二篇的知识了，不记得什么意思的往回看吧，哈哈*)   
但是有时候我们参数本身就存在一个数组中，难道我一个个拿出来在传递这些参数吗   
		
	val array = arrayOf(1,2,3)
	test(*array,3,4,)
*叫做展开操作符，这个只能可变参数只能传数组这种集合，List是不可以的   
	
	val list:List<Int> = arrayListOf<Int>(1, 2, 3)
	//    test(*list)报错
最后讲讲函数的调用    
	
	test()//直接调用（顶级函数，或者成员函数之间的调用）
	XianYuLao.sleep()//创建一个XianYuLao的对象然后调用sleep(成员函数)
顶级的函数可以直接使用，不需要依赖类的对象调用（在kotlin中一切都是对象）   
### 扩展函数 ###
**扩展**:  
Kotlin 像C#和Gosu那样，可以让一个类拥有新的功能，而且不是通过继承类也不是通过任何的设计模式（比如：修饰模式）   
（人类本不会飞，我突然给飞这个功能给人类(打飞机（跟打的差不多的意思/机智）)，那么人类并非是继承鸟类来获得飞行能力，当然也不是通过设计模式使人类能飞）    
而扩展函数就是这个新增的能力   
为了声明一个扩展函数，我们需要在函数名的前面加上类名(上面的例子中就是加上人类的类名，也就是接收这个功能的类名)和.   
以上面的例子下个demo 
	
	class Human{
	    fun speak(){
	        println("i am kotlin")
	    }
	    
	    fun sleep(){
	        println("sleep")
	    }
	}
	
	fun Human.fly(){
	    println("i am human,i can fly")
	}
	
	fun main(args: Array<String>) {
	
	    var xiaoming = Human()
	    xiaoming.sleep()
	    xiaoming.sleep()
	    xiaoming.fly()
	}
官方demo：
	
	fun MutableList<Int>.swap(index1: Int, index2: Int) {
		val tmp = this[index1] // 'this' corresponds to the list
		this[index1] = this[index2]
		this[index2] = tmp
	}
this关键字在扩展函数中就代表着接受这个功能的类的一个实例，官方demo中指的就是MutableList<Int>的一个实例，而人类的例子中也可以在fly方法中这样  
		
	fun Human.fly() {
    	println("i am human,i can fly")
    	this.sleep()
    	this.speak()
	}
就是说这个函数里面已经有一个扩展类的实例了  
当然我们可以使所有的类都拥有这个fly能力，那就用泛型吧...   

	fun <T> T.fly() {
    	println("i am human,i can fly")
    	this.toString()
	}
那么所有类型都能飞上天和太阳肩并肩了  
如一开始说的，扩展并不是继承，也并没有真正的改变人类，我只是通过某种方式使人类能飞起来，并没有往类文件中强行真正的插入这段函数代码   
其实撤了这么一段话，是为了说扩展函数是静态分发的(好高级!!).举个Java的例子  
	
	//java
	class A {
		public static void test(){
			System.out.print("A");	
		}
	}
	class B extends A{
		public static void test(){
			System.out.print("B");	
		}
	}

	//invoke
	A instance = new B();
	instance.test();
用你夯实的java基础告诉我打印是啥，打印的是A  
kotlin的扩展函数也好像Java的静态函数一样，只在乎你声明的的类型是啥，然后去调用你这个类型的方法   
	
	open class C//kotlin 默认类都是final，不可继承的

	class D: C()//继承C
	
	fun C.foo() = "c"
	
	fun D.foo() = "d"
	
	fun printFoo(c: C) {
	    println(c.foo())
	}
	//invoke
	printFoo(D())
既然你懂了Java的静态方法，那么Kotlin的扩展函数的这个静态分发不也是一样吗   
那么有那么一天，你定义的扩展函数的函数签名在这个类中已经有了，那么系统会怎么取舍调用呢？   
	class C {
    	fun foo() { println("member") }
	}

	fun C.foo() { println("extension") }
系统会永远的优先调用成员函数   
不知道你写kotlin的时候写过这样的函数的时候，又没有这样子写过   
	
	val nullableVal: Int? = null
    nullableVal.toString()
    nullableVal?.inc()
又没有想过为什么toString不用使用安全调用(这就用到第一篇的知识了，我真不是故意的),而调用inc()却一定要安全调用，这就涉及到了toString这个函数的实现问题了。   
其实toString是一个扩展函数   
	
	/**
	 * Returns a string representation of the object. Can be called with a null receiver, in which case
	 * it returns the string "null".
	 */
	public fun Any?.toString(): String//Any? 相当于Java的Object，这又用到第一篇的知识了，我真不是故意的
这个扩展函数的接受者可以为**null**，当是一个空的接收者到时候就会返回一个"null"字符串，不像Java那样，变量为null的时候调用方法直接给个npe   
### 中缀函数###
函数也可以使用中缀表达来调用，当满足以下条件   
- 这个函数是成员函数或者是扩展函数   
- 这个函数只有一个参数  
- 有关键字infix标识

	// Define extension to Int
	infix fun Int.shl(x: Int): Int {
	...
	}
	// call extension function using infix notation
	1 shl 2
	// is the same as
	1.shl(2)
我的demo：  
	
	infix fun Int.add(a: Int): Int {
    	return this + a
	}
	
	2 add 2
    2 add (2)
## 知识总结 ##

1. 类，对象，变量，方法，接口可以直接在包内声明，称为顶级的xxx
2. 可见度修饰符   
	
	①public任何地方都能访问   
	②protected子类和所在的上下文能访问   
	③private所在的源文件能访问   
	④internal所在的module能访问     
3. 函数声明**fun**标识符
4. 函数的返回值需要显示地写出除了以下两种情况   
	
	①返回值是Unit，那么返回值类型可以略去   
	②函数体是单一表达式，系统推断出返回类型     
5. 可设置参数的的默认值**fun test(a:Int = 0,b:Int){}**   
6. 调用函数的时候可以指定参数值**test(b = 1)**
7. kotlin中的变长参数**fun test(vararg a:Int){}**，**vararg**标识符  
8. 展开操作符* ， 只能传数组进去   
	**val array = arrayOf(1,2,3)**  
	**test( * array,3,4,)**
9. 扩展函数是静态分发的（Java的静态函数的隐藏调用）   
10. 扩展函数和成员函数一样的函数签名时，成员函数的优先级高，调用的是成员函数   
11. 可空接收者**Any?.toString()**  
## 最后 ##
按这种节奏，明天还是函数的其他知识，文章看的人数还是很少，是不是写得太烂了？唉  
好吧   
2017/5/27 23:36:37 
