![](http://upload-images.jianshu.io/upload_images/2038754-6a85beec5c3b6bba.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)      
## 导语 ##
上一篇学习了Kotlin的函数，这一篇介绍函数的类。记得在一个包内(一个kt文件中)，我们可以直接声明类，对象，属性，接口，函数...
## 任务 ##

1. 类(接口)声明

##类(接口)声明##
声明一个类（接口）很简单，**class**是修饰类的标识符   
	
	
	package xian.yu.lao

	/**
	 * Created by jinxiong on 2017/5/30.
	 */
	
	public class Test1 {}//默认可见度是public，可不写public
	
	private class Test2 {}//私有
	internal class Test3 {}//module可见
	//protected class Test4{}//protected不可修饰顶级类（前面说过）
	final class Test4 {}//默认是final ，不可继承
	
	open class Test5 {}//可被其他类继承
	
	abstract class Test6 {}//默认是open（可继承）
	interface Test7 {}//默认是open（可实现）
	
	data class DataClass{}//数据类
	//枚举类，每个枚举常数都是一个一个对象(Test8的对象),而不能再new出Test8 的对象
	//不可继承，不可用open修饰
	enum class Test8() {
	    A, B, C, D
	}
	
	//封闭类，算是枚举类的扩展
	//不可被外部继承，不可open修饰，不可再创建一个实例
	sealed class Test9 {
		//继承后面再说
	    open class Const() : Test9() {}//继承Test9,可用open修饰，可被外部继承
	
	    open class Sum() : Test9() {}//继承Test9
	}
	
	//嵌套类
	class Test10 {
	    val a: Int = 1
	
	    class Nested {
	        fun tt(): Unit {
	//            println(a)//不可访问外部类的变量
	        }
	    }
	}
	
	//内部类
	class Test11 {
	    val a: Int = 1
	
	    inner class Nested {
	        fun tt(): Unit {
	            println(a)//可以访问外部类的变量
	        }
	    }
	}
	
	fun test(listener: Test7): Unit {
	
	}
	
	fun main(args: Array<String>) {
	    
	    test(object : Test7 {})//创建了一个匿名类的实例，类似于java中new 一个接口一样
	}
上面列举了，final类，open类，接口，抽象类，枚举类，封闭类，嵌套类，内部类，匿名类，数据类     
下面讲下感觉比较陌生的类
### 封闭类 ###
用来表示类层次的限制，就是限定某个值的类型只能是提供的一堆类型中的一个，不能是其他类型。可以说是枚举类的的扩展，枚举类的值数量是有限的，而封闭类中的子类也是有限的，而对于枚举类中的常量值，它代表着一个枚举类的实例（也就是说这个枚举类的实力个数基本定死），而对于封闭类，它子类可以被创建不定数量的实例     
封闭类用于**when**表达式非常的好，先介绍kotlin中的**when**    
#### when ####
kotlin的**when**相当于Java中的**switch**     
	
	
	when(变量){
		变量所属类型的一个值或一个范围 -> 后续代码(块)
		..
		..
		..
		else -> 后续代码(块)//
	}
	//demo
	val a = 12
    when (a) {
        in 1..9 -> {
            println()
        }
        10 -> 12
        in 10..20 -> {
            println()
        }
    }
当when作为表达式的时候必须要加上**else**（相当于switch的**default**）   
	
		
	fun ty(a: Int): Int = when (a) {
	    12 -> 11
	    else -> 10
	}
*上面中的 in是一个标识符，用于表示是否在某个集合中，在for循环中也有用到，后续再说for循环*      

*而-> 就相当于 如果符合啥条件就执行我后面的代码吧*   

  
--------------
回到sealed类中，在上面的sealed类的定义中，我们说可以限定某个值只能是给定的一堆类型中的一中类型，在when中使用的好处就是无论什么情况都不用写**else**，因为那个值的属性只能是给定的集合中的某一个类型

	
	//对应上面的枚举类和封闭类
	//对于枚举类，就是判断变量是哪个实例	
	fun ty(a: Test8): Int = when (a) {
	    Test8.A -> 1
	    Test8.B -> 2
	    Test8.C -> 3
	    Test8.D -> 4
	}
	//对于封闭类，就是这个变量实例属于哪个子类
	fun tts(a: Test9): Int = when (a) {
	    is Test9.Const -> 2
	    is Test9.Sum -> 2
	}   

### 内部类 ###
内部类要讲的主要是this的问题   

	
	//内部类
	class Test11 {
	    val a: Int = 1
	
	    inner class Nested {
	        fun tt(): Unit {
	            println(a)//可以访问外部类的变量
	        }
	    }
	}
我们知道在内部类中能够访问到外部类的变量，是因为在我们的内部类中存在着一个外部类的实例，对吧，那么当我们在内部类中使用**this**的时候是代表内部类还是外部类呢？   
还记得前面说过的**return**吗，带标签和不带标签。在这里是否也是可以这样解决这问题？   
so，也是可以使用标签解决的    
	
	
	class Test11 {
    	val a: Int = 1

    	inner class Nested {
    	    val a = 2
    	    fun tt(): Unit {
    	        println(this@Test11.a)//访问Test11 中的a
    	        println(this.a)//访问inner中的a
    	    }
    	}
	}
规定在inner class中使用**this**是指inner实例，而使用外部类的this，就需要在this后面加上@类名，特别的，对于访问inner class中的成员(属性和函数)，this可以省略，当和外部类命名冲突时，不加标签使用的都是inner class的成员   
### 匿名类 ###
我们在Java中写匿名类是这么写的   
	
		
	
	View.setOnClickListener( new OnClickListener(){
		
		void onClick(View v){
			....
		}
	
	});
而在kotlin是使用**object expression**对象表达式来   
	
	
	interface Test7 {}//默认是open（可实现）
	fun test(listener: Test7): Unit {
	
	}
	
	fun main(args: Array<String>) {
	    
	    test(object : Test7 {})//创建了一个匿名类的实例，类似于java中new 一个接口一样
	}
而对于抽象类则是**object : Test6(){.....}**,其实这个很好理解嘛...    
因为kotlin中没有new关键字，而对于抽象类和接口，我们不能像其他普通类那样创建对象，so，就要使用到object 这个关键字   
	

	val  a = object{}//创建了一个匿名类的实例
	val b = object:Test6(),Test7{}//创建一个匿名类，这个类继承Test6和Test7

### 数据类 ###
数据类的作用没啥，就是用来保存数据，就好像我们拿到服务器中的json，将它转为对象保存着(方便操作这些数据)。kotlin称这些类为数据类，用**data**标识 
  

	data class Person(var name:String,var age Int){}
系统会为数据类生成一下的函数   

- equals()和hashCode()   
- toString,输出格式为 Person(name="xxx",age=xx)    
- componentN() 函数群,这些函数群是为了对象解构（后面讲），N对应着属性声明的顺序，这个函数暂时不管，没涉及到，知道就好   
- copy函数   
   
而成为数据类需要什么要求？     

1. 主构造器至少有一个参数   
2. 主构造器的参数要用var / val 修饰      
3. 不能是抽象类和open类，也不能是sealed 类和 inner类     
4. 数据类不能继承任何类，但是可以实现接口       

对于1 2 点的主构造器先不管，下篇讲这个。而对于不能是抽象类，很好理解，就是创建数据类的时候可能不是通过正常的创建实例的方式，而不能输open类是确保这个数据类就是基类，不会导致子类对象转型问题啥的（假想..）     
## 总结 ##
1. 一般的类默认的可见度修饰符是public，而且是不可被继承的，加上**open**才能被继承   
2. 抽象类和接口默认是open和public的   
3. 数据有**data** 标识符  
4. 枚举类(enum)中的每个常量值都是该类的实例，该类不能再被实例化了   
5. sealed类算是枚举类的扩展，封闭类中的子类可以被实例化  
6. 嵌套类不能访问外部类中的成员，也就是说嵌套类中不包含有外部类的实例  
7. 内部类(inner)中可以访问外部类的成员，内部类有外部类的实例   
8. 使用**object expression**来创建一个匿名类的对象   
	代码:   

		val a = object{...}

9. **when**相当于Java中的switch，当**when**作为表达式使用的时候else必须有   
	代码:   

		fun ty(a: Int): Int = when (a) {
   			12 -> 11
    		else -> 10
		}		
	
10. 但是当when中的变量类型是enum 或者是sealed的话，可以省去else，但是把enum 或者是sealed中的常量或子类列出来    
11. **->** 的用法有三    
	1. **when**中的条件选择   
	2. 函数类型   **(参数类型)->返回值类型** 
	3. lambda表达式中   **参数名称 -> 函数体**
12. 带标签的this，在inner类中使用this会有歧义，使用时需要带上标签比较好   
	代码：   
		
		class Test11 {
		    val a: Int = 1
		
		    inner class Nested {
		        val a = 2
		        fun tt(): Unit {
		            println(this@Test11.a)//访问Test11 中的a
		            println(this.a)//访问inner中的a
		        }
		    }
		}


------
2017/5/30 20:47:23 