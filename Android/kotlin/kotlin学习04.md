![](http://upload-images.jianshu.io/upload_images/2038754-6a85beec5c3b6bba.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)   
## 前程回顾 ##
上一篇学习了函数的定义声明，还有函数的返回值，参数，还有扩展函数，中缀函数，今天呢，就继续把函数讲完吧!!   
## 任务 ##

1. 局部函数   
2. 高阶函数和Lambda表达式   
3. 内联函数(Inline Function)   

### 局部函数 ###
在kotlin中，函数除了顶级函数（直接声明在包内），成员函数，扩展函数，还有一种就是局部函数了。既然是局部函数，那么当然是声明在某个局部那里，那就是声明在另一个函数内   

	
	fun outter(): Unit {
		val a = 12
	    fun inner(): Unit {
	        print("inner" + a )
	    }
	    //invoke
	    inner()
	}
那么inner函数就是局部函数，对于在outter声明的变量，在inner中可以使用外部函数的局部变量(闭包)     
### 高阶函数和Lambda表达式 ###
啥是高阶函数啊，我的Java都没有....   
高阶函数就是有一个函数作为参数，或者是返回值是一个函数   
也就是有了一个叫做函数类型的东西     
	
	
	// 函数参数为函数类型，此函数类型为：没有参数，返回值为Unit
	fun funType(a: () -> Unit): Unit {

    	//声明一个函数类型的变量，此函数类型为：参数为Int，返回值为Int
    	val b: (Int) -> Int
	}
**(parameterType) -> T** , 函数类型的就是这种格式。**在kotlin中所有东西都可以看作是对象**   
	
	
	fun funType(a: () -> Unit): Unit {
	    //invoke 1
	    a()
	    //invoke 2
	    a.invoke()
	}
	
	fun main(args: Array<String>) {
	
	    //局部函数
	    fun inner() = println("hello world")//单表达式函数
	    funType(::inner)//调用高阶函数
	    
	}
不懂单表达式函数可以去我上一篇看看(偷笑.jpg)   
**::**然后加上函数名就可以将函数以参数的形式传递过去，而作为函数类型的参数，调用这个参数也有有如上两种方法   
	
	
	fun funType(a: () -> Unit): Unit {
	}

	fun funType1(b: Int, a: (Int) -> Unit): Unit {
	}

	fun funType2(a: (String) -> Unit): Unit {
	}

	fun main(args: Array<String>) {

    	val a = 12
    	funType({ print("hello") })//Lambda
    	funType1(1) { it -> print(it) }//将花括号提到外面
    	funType2 { it -> print(it) }//参数只有一个函数类型的时候可以将()省略掉
    	funType2 { print(it) }//单一函数类型参数的隐含名称
	}
可以直接使用Lambda表达式，甚至你可以将这个匿名函数的定义放在()之外，而**->**是啥，参数 -> 函数体 ，在-> 前面的代表着这个函数的参数名，而后面就是函数体  
    
而当你的高阶函数只有一个参数的时候（只有函数类型的参数），你可以直接省去(),而这个it是什么东西呢，就是当你的函数类型中只有一个参数的时候，那个it就是它，比如**(String) -> Unit**，那么it就是就是这个参数的类型String的一个实例    
#### lambda表达式和匿名函数 ####
这两个都可以说是一种“函数字面值”，一个没有声明的函数，可以作为表达式传递出去   
	

	funType({ print("hello") })
Lambda表达式的完整语法形式，也就是函数字面值  
		
	
	val sum = { x: Int, y: Int -> x + y }
或者

	val sum: (Int, Int) -> Int = { x, y -> x + y }  
因为变量声明了函数类型是两个参数类型为Int，返回值为Int，那么lambda表达式的最后一条表达式就会作为返回值。因为变量已经表明了参数的类型所以不需要想上一条那样在->之前声明参数的类型        
继续函数之前插入一个小知识：   
标签

	
	loop@ for(..){
	
		for(..){
			if(...)break@loop
		}		
	}
在Java中我们的**break**只能是退出内层的for循环，而当我们使用标签之后，我们就可以直接退出道外层的循环   
而标签可以加在kotlin的任何表达式中，**标签名@**,而使用就是**@标签名**   
	

	    test@val list = arrayListOf<Int>()
而函数会有默认的标签，这个标签名就是函数名    
	
	
	fun main(args: Array<String>) {
    	return@main Unit
	}
在我们使用lambda表达式作为函数类型的参数传递给高阶函数的时候，当这个函数类型是一个非Unit返回值的，我们lambda表达的最后一条表达式作为返回值，但是我们需要显示的使用**return**的时候该怎么用了？难道不是直接像平常那样吗？
	
	
	fun funType2(a: (Unit) -> Unit): Unit {
	}

	fun main(args: Array<String>) {
    	funType2 {
    	    println("hello world")
    	    return@funType2 Unit
    	}
	}
这就需要使用到标签了，因为高阶函数funType2只是一个普通的函数(后面会介绍不同于这种的的函数（内联函数）)，so，这里的返回是非局部返回。如果不使用标签就会直接退出main函数，而且编译不能通过  

下面讲讲匿名函数   
匿名函数相对于lambda表达式，它在声明的时候可以指定函数的返回类型不像lambda只能指定参数和函数体**参数->函数体**,而且，匿名函数是局部返回的   
	
	
	fun(a:Int,b:Int):Int = a + b
其实跟普通的函数没啥区别就是没了函数名   

	
	fun funType2(a: () -> Unit): Unit {
	}
	funType2(fun() { return Unit })
    funType2(fun() = print("hello"))
对于匿名函数，作为参数它必须是在**()**之内，不能像lambda那样任性    
### 内联函数(Inline Function) ###
刚刚在lambda非局部返回的时候说过，当高阶函数只是普通的声明的时候，是非局部返回的，不带标签返回甚至不能通过编译，而内联函数却不一样   
	
	
	inline fun funType(): Unit {
	    println("inline，不是高阶函数")
	}
	
	fun funType1(): Boolean {
	    funType()
	    funType2 { return true }//直接退出funType1()
	    return false //不会执行
	}
	
	inline fun funType2(a: () -> Unit): Unit {
	    println("inline 高阶函数")
	    a.invoke()
	}   
	fun main(args: Array<String>): Unit {
	    println(funType1())
	}
打印结果:
		
		
	inline，不是高阶函数
	inline 高阶函数
	true
 那什么是inline函数呢，上面的funType1相当于下面的代码   
	
	
	fun funType1(): Boolean {
		println("inline，不是高阶函数")
		println("inline 高阶函数")
		return true
		return false 
	}
在funType1已经不在调用funType 和 funType2 函数了   
	
	
	funType2 { return true }//直接退出funType1()
	return false //不会执行
	/*******/
	inline fun funType2(a: () -> Unit)
很多人可能不能理解这个，为什么我funType2声明的参数的函数类型是返回的是Unit，而你调用我的时候传过来的函数类型返回值是Boolean的？编译器遇到inline函数就会把inline函数的函数体拿出来放到调用者的函数体中，但当你这个函数类型的参数被调用的时候，返回值却不是我调用者函数的返回值，这不是扯蛋吗。这就是为什么在funType1中最后有两个return，其实编译器不知道**funType2**函数有没有调用那个函数类型的参数，如果没有调用，那么我这个函数得有个Boolean返回值啊是吧，所以确保万一，就在最后加上return，而当你**funType2**调用了那个函数类型的参数的时候，这个参数的return可是直接结束调用函数**funType1**的，当然也要确保返回值是Boolean吧    

有inline当然也会有noinline，noinline函数只能使用在inline高阶函数中修饰函数类型的参数
	
	
	inline fun funType2(a: () -> Unit, noinline b: () -> String): Unit {
    	println("inline 高阶函数")
    	b.invoke()
		a.invoke()
	}
	
	//在funType1中调用
	funType2({
        println("return true 之前")
        return true//直接退出funType1()
    }, {
        if (true) "df"
        else "df"

    })
	//相当于
	fun funType1(): Boolean {
		b()//调用函数
		println("return true 之前")
        return true//直接退出funType1()
	}

所以对于函数类型参数b，它就是不会插入到调用者的函数体中，会存在着函数调用   
## 总结 ##

1. 局部函数就是定义在另一个函数中的函数，局部函数可以使用外部函数定义的变量   
2. 高阶函数：以一个函数作为参数或者返回一个函数的 函数   
3. 函数类型：**(参数的类型) -> 返回值**   
4. 函数类型的参数：**val a : () -> Unit = {print("hello")}**   
5. 调用高阶函数的方式  
	
	①**::** 双冒号     
		fun funType(a: () -> Unit): Unit {}  
		fun inner() = println("hello world")//单表达式函数  
    	funType(::inner)//调用高阶函数   
	②Lambda   
		funType({ print("hello") })//Lambda   
		funType(){print("hello") }//{}放到外面   
		funType{print("hello")}  //只有一个函数类型的参数的时候直接省去()   
		//   
		fun test(a:(Int)->Unit){}   
		test { a -> println(a) }//当函数类型参数有参数的时候  参数名->函数体 		
6. lambda表达式和匿名函数都是一种函数字面值，一个没有声明的函数，而又可以作为表达式传递出去    
7. 标签：@标签名，有名函数默认是含有标签名，就是函数名     
	

		loop@ for(..){

		    for(..){
		        if(...)break@loop
		    }       
		}

		fun main(args: Array<String>) {
    		return@main Unit
		}
8. 单纯使用**renturn**(无标签return)只能退出一个有名称的函数或者匿名函数     
9. lambda表达式是不能存在无标签的return的，只能使用有标签的return   
10. 匿名函数：跟普通函数的差别就是没了函数名称，跟lambda表达式的差别就是能显式声明返回值  
		

		fun funType2(a: () -> Unit): Unit {
		}
		funType2(fun() { return Unit })
		funType2(fun() = print("hello"))	  
11. inline函数：调用inline高阶函数，作为参数的lambda表达式可以使用无标签的return，而return之后就是退出高阶函数，所以return的返回值类型跟高阶函数的返回值类型一致   
		
		
		fun funType1(): Boolean {
		
    		funType2 { return true }//直接退出funType1()
    		return false //不会执行
		}

		inline fun funType2(a: () -> Unit): Unit {
	    	println("inline 高阶函数")
    		a.invoke()
		}   


12. inline函数会将函数体插入到调用者函数的函数体内，使其失去函数调用，不存在函数调用   
13. noinline只能使用在inline函数中，用于修饰函数类型的参数，使其不再是inline，变成普通的函数类型参数    



2017/5/29 12:26:22    
这是更新，昨晚太累，写不完....