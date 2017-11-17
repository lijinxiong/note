![](http://upload-images.jianshu.io/upload_images/2038754-6a85beec5c3b6bba.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)     

## 属性 ##

### 顶级属性 ###

我们可以直接在包内直接声明对象，属性，函数，接口，类。在包内直接声明的就是就是顶级属性   


	var property01: String = ""
    	get() = field
    	set(value) {
        	field = value
    	}
	val property02
    	get() = 12
var/val 修饰 ， 属性类型，初始器，setter/getter都是可选的   
可见度默认为public，可在任何地方访问到着两个属性    

#### 属性的后端域变量backing fields ####

**field**就是属性的后端域变量，只存在于setter和getter方法中。其实就是属性的一份copy，因为kotlin是不支持域变量的，所以当你在setter/getter中直接访问属性的话，其实就是一直在递归
	
	
	var property01: String = ""
    	get() = property01
	fun main(args:Array<String>){
		println(property01)
	}
当你使用这个属性的时候，就会无限递归，爆栈。任何地方使用属性都是通过getter和setter方法的    

### 委托属性 ###

####  ReadWriteProperty<Any?,String>####


	//委托类，可继承系统提供的接口
	class Test: ReadWriteProperty<Any?,String> {
    	
		override fun getValue(thisRef: Any?, property: KProperty<*>): String {
    	}

    	override fun setValue(thisRef: Any?, property: KProperty<*>, value: String{
    	}
	}
对于var来说，委托类中必须有getValue和setValue这两个方法，而val中只需要有getValue就可以了    
	

	var a : String by Test()  
那么访问这个属性和修改这个属性就不再是调用原本的getter 和setter方法了，而是代理对象Test()的getValue 和setValue方法了   
下面参照了**ObservableProperty**实现   
	
	
	class Test<T>(initValue: T) : ReadWriteProperty<Any?, T> {
    	private var value = initValue
    	override fun getValue(thisRef: Any?, property: KProperty<*>): T {
    	    return value;
    	}
	
    	override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
    	    this.value = value
    	}
	
	}


#### lazy() ####



	private class SynchronizedLazyImpl<out T>(initializer: () -> T, lock: Any? = null) : Lazy<T>, Serializable {
    private var initializer: (() -> T)? = initializer
    @Volatile private var _value: Any? = UNINITIALIZED_VALUE
    // final field is required to enable safe publication of constructed instance
    private val lock = lock ?: this

    override val value: T
        get() {
            val _v1 = _value
            if (_v1 !== UNINITIALIZED_VALUE) {
                @Suppress("UNCHECKED_CAST")
                return _v1 as T
            }

            return synchronized(lock) {
                val _v2 = _value
                if (_v2 !== UNINITIALIZED_VALUE) {
                    @Suppress("UNCHECKED_CAST") (_v2 as T)
                }
                else {
                    val typedValue = initializer!!()
                    _value = typedValue
                    initializer = null
                    typedValue
                }
            }
        }
	//使用
	val property04 by lazy {"sdfsdf"}
	
使用后端私有属性，第一次访问的时候才去执行传过去的lambda表达式，保存着lambda的返回值，再访问的时候直接取出后端私有属性的值返回    

#### Delegates.observable() ####
这个就是实现了ReadWriteProperty接口再增加监听的功能   
	
	
	var property03: String by Delegates.observable(" xian yu lao") {
    	property, oldValue, newValue ->
    	println("$oldValue -> $newValue")
	}
每次我们改变都会调用我们传递过去的lambda表达式    
#### 将多个属性保存在map中 ####
使用map实例作为属性的委托   
	
	
	class User(val map: Map<String, Any?>) {
		val name: String by map
		val age: Int by map
	}
	val user = User(mapOf(
		"name" to "John Doe",
		"age" to 25
	))
### class中的属性 ###
class中的属性可以在class主体中声明也可以在主构造函数中   
	
	
	class Human1 constructor(var name: String, var age: Int) {
	}
	class Human2(var name: String, var age: Int) {
	}
	class Human3() {
    	var name: String = ""
    	var age: Int = 0
	}
以上三种都可以声明类的属性，其中声明在类主体部分的需要进行初始化(可以加上延迟初始化标识符不用马上初始化或者抽象属性不需要初始化)     
#### lateinit ####
	
	
	
	class Human4() {
	    lateinit var name: String
	    fun initName(name: String) {
	        this.name = name
	    }
	//    lateinit var age: Int // 不允许是基本数据类型
	}
	
	fun main(args: Array<String>) {
	
	    val human = Human4()
	    human.initName("xian yu lao")
	    println(human.name)
	
	}
想要被lateint 修饰符修饰，必须是class主体中声明的非null 的var属性，还有就是默认的setter和getter，还有不能是基本数据类型。使用这个属性的时候必须已经是初始化了的   
### 后端属性 ###
这个没啥好扯的   
	
	
	private var _table: Map<String, Int>? = null
	public val table: Map<String, Int>
		get() {
			if (_table == null) {
			_table = HashMap() // 类型参数可以自动推断得到, 不必指定
			}
			return _table ?: throw AssertionError("Set to null by another thread")
		}
后端私有属性的取值方法与设值方法都使用默认实现, 我们对这个属性的访问将被编译器优化, 变为直接读写后端域变量, 因此不会发生不必要的函数调用, 导致性能损失.   
其实是不是我自己在写一个方法获取_table属性也是可以的？   
	
	
	private var _table: Map<String, Int>? = null
    fun getTable(): Map<String, Int> {

        fun createMap(): Map<String, Int> {

            _table = HashMap()
            return _table as Map<String, Int>
        }
        return _table ?: createMap()
    }


## getter setter ##

声明属性的完整语法   

	
	var <propertyName>: <PropertyType> [= <property_initializer>]
		[<getter>]
		[<setter>]
我们可以自定义setter和getter，setter和getter的可见度默认跟属性一样，可以设置为小于等于属性的可见度   
## 构造器 ##
一个主构造器多个次构造器   
	
	
	class Person constructor(var firstName: String ="") {
	}
	class Person (var firstName: String) {
	}
当没有可见度修饰符和注解修饰constructor的时候，可以省去   
存在默认值，跟函数的默认参数差不多    	
主构造器中不包含任何代码，除了类属性的定义，除此就要放到初始化代码块中   
	
	
	class Test12(var name: String) {

    	init {
    	    println(name)
    	}
	
    	val age: Int = 0
	}
次构造器     
	
	
	class Test12(var name: String) {
    	init {
    	    println(name)
    	}
	
    	constructor(name: String, age: Int) : this(name) {
    	    this.age = age
    	}
	
    	var age: Int = 0
	}
次构造器必须委托给主构造器(如果有的话)     
非抽象类中没有声明任何构造器，系统会生成一个无参可见度为public的主构造器，如果不想可见度为public，有种就自己改    
	
		
	class DontCreateMe private constructor () {
	}
