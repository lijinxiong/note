# 基本语法 #
## 定义包 ##
包应该放置在源文件的顶部  
	
	package my.demo
	import java.util.*
	// ...
源文件不一定要和目录和包匹配，源文件可以放置在文件系统的任意位置   
## 定义函数 ##
如下函数有两个**Int**类型的参数而返回值的类型也为**Int**
	
	fun sum(a: Int, b: Int): Int {
		return a + b
	}
函数以一条表达式作为函数体，而返回类型则从表达式的结果推出   
	
	fun sum(a: Int, b: Int) = a + b
函数返回一个没有意义的值  
	
	fun printSum(a: Int, b: Int): Unit {
		println("sum of $a and $b is ${a + b}")
	}
Unit返回类型可以忽略不写  
	
	fun printSum(a: Int, b: Int) {
		println("sum of $a and $b is ${a + b}")
	}
## 定义局部变量 ##
赋值一次（只读）的局部变量   
	
	val a: Int = 1 // 立即赋值
	val b = 2 // `Int` 类型被推断出
	val c: Int // 当没有进行初始化赋值的时候，变量的类型必须声明
	c = 3 // 延迟赋值
可变变量：
	
	var x = 5 // `Int` type is inferred
	x += 1
## 注释 ##
像java 和javascript那般，Kotlin 支持单行和块注释  
	
	// 当行注释
	/*块状
		注释*/
不像Java，kotlin的块状注释可以被嵌套使用   
非官方：
Java  
	
		/**
         * /*
         */
        System.out.println("Java");
        */
最后的*/会报错   
Kotlin：  
	
	/**
     * /*
     */
    println("Kotlin")
    */
成功注释掉*println("Kotlin")*   
## 使用字符串模板 ##
	
	var a = 1
	// 变量名在模板中
	val s1 = "a is $a"
	a = 2
	// 任意的表达式在表达式中:
	val s2 = "${s1.replace("is", "was")}, but now is $a"
## 使用条件表达式 ##
	
	fun maxOf(a: Int, b: Int): Int {
		if (a > b) {
			return a
		} else {
			return b
		}
	}
使用if作为一个表达式
	
	fun maxOf(a:Int , b:Int)if(a>b) a else b 
## 使用可为null的值 和 检查 是否是null ##
一个引用必须明确的标识为可为null值当这个引用的值可能是null的时候   

返回 null 如果 String变量*str*的内容不是Int
	
	fun parseInt(str: String): Int? {
	// ...
	}
使用一个返回值可为空的函数   

	fun printProduct(arg1: String, arg2: String) {
		val x = parseInt(arg1)
		val y = parseInt(arg2)
		// Using `x * y` yields error because they may hold nulls.
		if (x != null && y != null) {
		// x and y are automatically cast to non-nullable after null check
			println(x * y)
		}
		else {
			println("either '$arg1' or '$arg2' is not a number")
		}
	}
或者
	
	// ...
	if (x == null) {
		println("Wrong number format in arg1: '${arg1}'")
		return
	}
	if (y == null) {
		println("Wrong number format in arg2: '${arg2}'")
		return
	}
	// x and y are automatically cast to non-nullable after null check
	println(x * y)
## 使用类型检查和自动转型 ##
is操作符用于检测一个实例是否是某个类型的实例