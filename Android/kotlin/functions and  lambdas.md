# 函数#
## 函数声明 ##
在Kotlin中，函数的声明是有**fun**关键字  
	
	fun double(x: Int): Int {
	}
## 函数的使用 ##
调用函数使用传统的方法  
	
	val result = double(2)
调用成员函数使用.标记法  
	
	Sample().foo() // 创建一个Simple类的实例然后调用foo函数
### infix notation###
在介绍中缀表示法前先跳到**Extension**中

------------
#### Extension ####
Kotlin 像C#和Gosu那样，可以让一个类拥有新的功能，而且不是通过继承类也不是通过任何的设计模式（比如：修饰模式）   
（人类本不会飞，我突然给飞这个功能给人类，那么人类并非是继承鸟类来获得飞行能力，当然也不是通过设计模式使人类能飞）   
#### Extension Function（扩展函数 ）####
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
泛型的声明是在函数名字的前面，跟Java是差不多的样子   
后续还有介绍扩展函数的，这里的介绍应该够了   

---------------
**infix notation**   
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
### 参数Parameters ###
函数的参数用Pascal 表示法定义,例如name:type。参数间使用,分开，每一个参数都必须有个明确的类型
	
	fun powerOf(number: Int, exponent: Int) {
	...
	}
### 默认参数 ###
函数的参数也是可以有默认值的，当某个参数使用了也就意味着这个参数被省去了，这样子相对于其他的语言可以减少重载方法的数目  
	
	fun Test33(b: Int, a: Int = 10): Unit {
    	println(a)
    	println(b)
	}
	Test33(1, 2)//打印1 2 
	Test33(1)//打印1 10
  
默认值的定义：类型之后 = 对应的默认值   
覆盖一个有默认值的方法的时候，默认的参数值会从方法签名之中省去，就是子类不知道父类是否有默认参数值    

	open class Test11 {
	    open fun read(a: Int = 4, b: Int) {
	    }
	}

	class Test22 : Test11() {
	    override fun read(a: Int, b: Int) {
    	}
	}
### 指定参数 ###
函数的参数可以被指定当我们调用函数的时候，那么我们会非常的方便当我们调用参数数目非常多或者有参数默认值的函数   
	
	fun reformat(str: String,
		normalizeCase: Boolean = true,
		upperCaseFirstLetter: Boolean = true,
		divideByCamelHumps: Boolean = false,
		wordSeparator: Char = ' ') {
		...
	}
我们可以只是传入没有default值的参数   
	
	reformat(str) 
但是我们需要改变其中的一个默认参数值的时候   
	
	reformat(str,
		normalizeCase = true,
		upperCaseFirstLetter = true,
		divideByCamelHumps = false,
		wordSeparator = '_'
	)
这么些的话就是会非常的难读，而且麻烦，我们仅仅是改变wordSeparator ，但是kotlin允许下面这么写    
		
	reformat(str,wordSeparator = '_')
**Java不支持这个，因为压根没保存参数的名字**
###  函数返回值之 Unit###
如果一个函数没有返回任何有价值的值，那么返回的类型就是Unit，Unit是一种类型，并且只有一个值（Kotlin使用这个实现了Singleton模式，网址：[https://kotlinlang.org/docs/reference/object-declarations.html](https://kotlinlang.org/docs/reference/object-declarations.html)）   
这个值不需要我们显式的使用return语句返回    
	
	fun printHello(name: String?): Unit {
	
		if (name != null)
			println("Hello ${name}")
		else
			println("Hi there!")
			// `return Unit` or `return` is optional
	}
这个Unit的返回声明也是可选的，下面等价于上面  
		
	fun printHello(name: String?) {
		...
	}
### 单一表达式的函数 ###
当我们的函数返回一条表达式的时候，我们的花括号可以省去，和函数体就在= 后面  
	
	fun test(a: Int):Int = a + 1
声明返回值的类型也是可选的，
	
	fun test(a:Int) = a + 1
### 明确的返回类型 ###
函数包含函数体的必须声明其返回的类型，除了熟Unit之外。Kotlin不能再函数体中推断出返回值出来，因为函数体可能有着复杂的控制流，对于代码阅读者也显得不那么明显可以一眼看出来甚至对于编译器  
### 可变参数 ###
一个函数的参数可能被vararg（可变参数）标识符修饰  
	
	fun <T> asList(vararg ts: T): List<T> {
		val result = ArrayList<T>()
		for (t in ts) // ts is an Array
			result.add(t)
		return result
	}
允许可变数目的参数传递给函数   
	
	val list = asList(1, 2, 3)
函数中T类型的可变参数是一个包含T类型的array，上面的ts就是Array<out T>的实例   
仅仅只有一个参数可以被vararg修饰，如果那个被vararg修饰的参数不是在参数列表中的最后，那么为了能够将参数传递过去，便使用指定参数名传递，如果这个多变参数是一个函数类型(function type),那么通过lambda的传递过去(这个不懂)   
当我们调用一个含有多变参数的函数的时候，可以一个一个的传递参数*val list = asList(1, 2, 3)* 但是当我们本身就已经有一个array的时候，希望将array的数据传递给这个函数，可以使用spread操作符(在array的前面加上*)   
	
	val a = arrayOf(1, 2, 3)
	val list = asList(-1, 0, *a, 4)
## 函数的作用域 ##
在Kotlin 函数可以被声明在一个文件的顶部，意味着你不需要创建一个类去持有这个函数，就好像其他语言一样（Java C#，Scala），除了顶级的函数，科特林的函数也可以声明为局部，作为成员函数和扩展函数   
### 局部函数 ###
科特林支持局部函数，i.e函数里面有另一个函数   

	fun dfs(graph: Graph) {
		fun dfs(current: Vertex, visited: Set<Vertex>) {
			if (!visited.add(current)) return
			
			for (v in current.neighbors)
				dfs(v, visited)
		}
		dfs(graph.vertices[0], HashSet())
	}
局部函数可以获取到外部函数的局部变量（闭包），所以在上面的例子，visited可以作为外部函数的局部变量   	
	
	fun dfs(graph: Graph) {
		val visited = HashSet<Vertex>()
		fun dfs(current: Vertex) {
			if (!visited.add(current)) return
			for (v in current.neighbors)
				dfs(v)
		}
		dfs(graph.vertices[0])
	}
### 成员函数 ###
成员函数是定义在一个类或者一个object里面的   
 
	class Simple(){
		fun foo(){print("Foo")}
	}
	//object 中
	var obj = object{
		fun foo(){print("Foo")}
	}
成员函数可以使用.表示法调用
	
	Simple.foo()//创建一个Simple的实例然后调用foo函数
## 泛型函数 ##
函数可以有泛型参数，泛型声明在函数名前面   
	
	fun <T> singletonList(item: T): List<T> {
	// ...
	}
不展开，涉及泛型东西比较多   
## 内联函数 ##

