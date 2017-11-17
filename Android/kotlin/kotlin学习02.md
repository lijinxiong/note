![](http://i.imgur.com/Nj6ILEt.png)   

上一篇中最后中提到的一个问题   
Java中的数组 和 Kotlin中的Array究竟差别在哪，这篇将会给大家介绍     

## 任务 ##
1. 变型
2. 协变，逆变，不变
### 变型variance ###
Java Kotlin 包括很多程序设计语言的类型系统都是支持子类型的，例如，Cat是Animal的子类型，那么Cat类型的表达式可用于出现在Animal类型表达式的地方   
	
	//java Animal中有个public属性name
	public static void needAnimalParameter(Animal animal) {
        System.out.println(animal.name);
    }
    
    public static void main(String[] args) {
        needAnimalParameter(new Cat("cat"));
        needAnimalParameter(new Animal("animal"));
    }
那么什么是变型？   
变型：是指如何根据组成类型之间的子类型关系，来确定更复杂的类型之间的子类型关系。用人话说就是B是A的子类，A B 分别和C 类组成了新的两种新的类型（A 和 C ，B 和C），那么这两种新类型的子类型关系是啥，谁是谁的父类，谁是谁的子类。这里便出现了三种变异，一种是原本子类型被保持(协变)，一种是反转（逆变），还有一种就是忽略（不变）   
### 协变，逆变，不变 ###
#### 协变 ####
协变就是：保持了子类型序的关系。   
B是A的子类，A B 分别和C 类组成了新的两种新的类型（A 和 C ，B 和C），那么新类型（A和C）依旧是新类型（B 和 C）的父类   
java中的	 数组**[]**就是协变的
	
	String[] strings = new String[12];
    Object[] objects = strings;
但是这好像就会有什么问题一般????   
当我在这个数组中添加一个非String类型的对象那不是出大事了吗？没错，这样子在Java中是会在运行的时候抛出一个ArrayStoreException的异常，那我们直接不让**Object[] objects = strings**编译成功，直接吧这个协变特性去掉不就行了吗？虽然对于可写可读的数组是不安全的，但是对于只可读的数组却是安全的。    

而Kotlin 中的数组Array单纯地使用时不具备这种协变的，但是加上标识符**out**也就能像Java数组那样拥有协变的能力，而且更加的安全，因为对于协变的Array，Kotlin 是不允许向其内加入元素，因为谁知道你加进去的类型是否合适呢    
	
	val array: Array<out Int> = arrayOf(1,2,3,4)
	//    array.get(1)//返回的类型是Int
    
	//    array.set(1,1)//禁止使用
    val numberArray: Array<out Number> = array
	//    numberArray.get(1)//返回的类型是Number
而在Kotlin中，其他的不可变的集合类(接口)都是标有这个out标识符，然后再加上泛型   
	
![](http://i.imgur.com/ihqTWyb.png)    

可以说的是，单纯的泛型是不具备协变的，单纯的泛型是不变的。只有在泛型的前面加上out才算是协变的。 **out T**   
	
	//协变，不可变得collection
    val collection: Collection<Int> = arrayListOf(1, 2, 3)
    val numberCollections: Collection<Number> = collection
	//    numberArray.set(1,2)禁止
    
    //不变，可变的collection
    val mutableCollection: MutableCollection<Int> = mutableListOf(1, 2, 3)
    mutableCollection.add(1)//可添加元素
	//    val mutableCollection1: MutableCollection<Number> = mutableCollection//报错

然后我们再回到Java中，单纯的泛型List集合同样也是不具备协变的，而实现协变的话就需要通配符类型参数**? extends T**
	
	List<Integer> stringList = new ArrayList<>();
    List<? extends Object> list = stringList;
跟Kotlin 一般，我们不能够向集合中添加元素，但是换来的是协变。   
我们把这些只能读取的对象称为生产者Producer（只出不入）   
*注意的是：如果你使用一个生产者对象，比如 List<? extends Object > ，虽无法调用add 和 set 方法，但是并不能代表这个对象的*  **值不变** *比如你可以使用clear函数清空list的所有元素，因为clear不需要任何参数*，能确保的只是取出的对象是这个T或者它的子类（类型转换）的实例     
#### 逆变 ####
逆变：逆转了子类型序关系   
说句笑话就是，你爸爸喊你叫做爸爸   
B是A的子类，A B 分别和C 类组成了新的两种新的类型（A 和 C ，B 和C），那么新类型（A和C）是新类型（B 和 C）的子类   
在Java中，你可能已经想到是**? super T**,而在Kotlin中，就是**out**标识符，用法和**in**一样，而作用却是反了    

		//Java
	List<? super Object> objectList = new ArrayList<>();
    objectList.add(new Object());//可以增加所有类型对象

    List<? super Number> numberArrayList = objectList;
    numberArrayList.add(12);//Integer
    numberArrayList.add(12.0);//Double
	//numberArrayList.add(new Object())//报错
    Object a = numberArrayList.get(1);//返回的对象类型是Object
        
    List<? super Integer> intList = numberArrayList;
    intList.add(12);
	//intList.add(2.0);//报错，只能添加Integer类型
    Object s = intList.get(1);//返回对象类型是Object

自己写到这里的时候才发现自己对**? super T**的理解是有偏差的   

像上面代码中的list（只new 了一个），当**? super Object**的时候，能够add进去的是Object以及它的一切子类，当**? super Number**的时候，能add进去的只有是Number以及它的子类，这Object到Number的，add的门槛高了，然后当**? super Integer**的时候，只能是Integer 和它的子类。  
**? super T**其中的T是能进入到list的最上层的类了（按照父类在上，子类在下的话），但是不代表list里面只存在着T 以及 T的子类，也是可能存在着T的父类的    
在Java中**? extends T**叫做**Upper Bounded Wildcards**上界通配符，而**? super T**则是**Lower Bounds Wildcards**下界通配符，至于为啥   

![](http://i.imgur.com/ciDtVw0.png)	  
   	
	List<Object> stringList = new ArrayList<>();
    List<? extends Object> strings = stringList;//作为List<String> 的上界

    List<? extends Integer> extendsIntegers = new ArrayList<>();
    List<? extends Number> extendsNumbers = extendsIntegers;
    List<? extends Object> extendsObjects = extendsNumbers;

    List<? super Object> objects = new ArrayList<>();//作为的List<Object>下界(子类)，
    List<Object> objectList = objects;//

    List<? super Object> superObjects = new ArrayList<>();
    List<? super Number> superNumbers = superObjects;
    List<? super Integer> superIntegers = superNumbers;


被**? extends T**修饰的List只会成为最上层的父类，而被**? super T**修饰的List就变成了最下层的子类   
假设我们可以对List进行可读可写操作(返回的类型为T)，那么当我们   
	
	List<? super Object> superObjects = new ArrayList<>();
    List<? super Number> superNumbers = superObjects;
这个时候superNumbers调用get的方法，按照上面的想法，返回的就是Number，但是问题是这个list本身是**List<? super Object> superObjects = new ArrayList<>();**，谁知道在此期间是否有其他的类型被放进去，当你取出来的时候转型为Number就会转型异常   

所以对于逆变，我们可以调用它的set/add(T/T的子类)，但是对于get方法，返回的一定就是Object。其实这就相当于逆变的对象值拥有写入的能力，对于它那种get出来的类型，其实并不算是get，因为返回的类型都不是存进去的类型。而会把这种只能写入的对象称为消费者    
有个口诀
	
	PECS: 生产者(Producer)对应 Extends, 消费者(Consumer) 对应 Super.

都差点忘记这是讲kotlin的文章了.......
	
	val in_mutable_list: MutableList<in Number> = mutableListOf(1, 2)
    in_mutable_list.add(2, 1.0)
    in_mutable_list.add(3, 1)
    val in_mutable_list1: MutableList<in Int> = in_mutable_list

	//    in_mutable_list1.add(2.0)//不能加入Double
	//    val instance: Int = in_mutable_list1.get(1)//返回的是Any? ,相当于Object
Kotlin 中的**out**跟Java的**? super T**基本是一致的  
#### 不变 ####

其实很简单，就是List<Integer> ,List<Number>没有半毛钱关系，都是平等的，不存在谁是爸爸谁是儿子   

------
本篇涉及知识点：    

1. Java中的数组相当于Kotlin的**Array[out T]**    
2. 变型：如何根据组成类型之间的子类型关系，来确定更复杂的类型之间的子类型关系     
3. 协变：爸爸还是爸爸，儿子还是儿子  
4. 逆变：爸爸是儿子，儿子却变成了爸爸  
5. 不变：爸爸不再是儿子的爸爸，儿子不再是爸爸的儿子，关系断绝  
6. 协变：Java -> **? extends T**   kotlin ->  **out T** 只能读，不能写 生产者    
7. 逆变：Java -> **? super T**  kotlin ->  **in T** 只能写，也能读(返回Object/Any?,不算真正读)  消费者   
8. 单纯的泛型是不变的  **Array< T >**
9. pecs 口诀

参考： 
> [协变/逆变 维基百科](https://zh.wikipedia.org/wiki/%E5%8D%8F%E5%8F%98%E4%B8%8E%E9%80%86%E5%8F%98)  
> [java super /extends](https://stackoverflow.com/questions/4343202/difference-between-super-t-and-extends-t-in-java)  
> [java 官方 ?super 和 ?extends](https://docs.oracle.com/javase/tutorial/java/generics/upperBounded.html "java 官方 ?super 和 ?extends")   

明天继续!!!  
2017/5/27 0:28:54 