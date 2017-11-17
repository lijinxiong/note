### 声明属性 ###
kotlin中属性有val 和 var 声明，var 的属性的值可变，而val 属性的值不能变   

声明的属性必须初始化


	class Human1 {
	    var name:String = ""
	    val age = 12
	}

不初始化就用abstract修饰####
	
	abstract class Human2{
    abstract val name :String
    var age = 12
	}
属性的可见度默认是public，自己也可以设置可见度   
	
	class Human3 {
    	private var name: String = ""
    	private val age = 12
	}
编译器自动会生成getter和setter方法，所以上面的属性都有getter和setter方法   

访问：     
![](http://i.imgur.com/a64k0TZ.png)   
但是最后还是这样  
![](http://i.imgur.com/EeAU9sB.png)  
所以我们还是直接使用对象.属性来访问吧，当然，对于可见度为private的是不能访问的，而val是不能够修改的，可以访问   
声明属性的完整语法
	
	var <propertyName>[: <PropertyType>] [= <property_initializer>]
    [<getter>]
    [<setter>]   
初始器和setter，getter都是可选的，如果能从初始器或者从getter返回的类型中推断出属性的类型，那么属性的类型也是可以不写的   
	
	class Human4 {
    	var name = ""//由初始器推出为String
		val name //从getter返回的类型得知是String
        	get() = ""
	}
	
官方example   
	
	var allByDefault: Int? //需要初始器就行初始化，使用默认的getter和setter
	var initialized = 1 // 推断出为Int ，使用默认的getter和setter
只读属性和可变属性在声明上有两处不同，只读属性使用val 并且只读属性没有setter方法  
	
	val simple: Int? // Int 类型，默认getter方法，最好是在constructor方法中赋值，毕竟只有一次机会赋值，每个对象都拥有同样的属性值，就没必要设置这个属性了，直接定义方法返回一个常量就好了
	val inferredType = 1 // Int 类型，默认getter
我们可以自定义setter和getter方法，和普通的函数基本没啥区别，只是它是在属性声明的右下方位  
	
		

