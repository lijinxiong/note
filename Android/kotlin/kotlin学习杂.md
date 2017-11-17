## Standard.kt 中的let ##
	
	
	/**
	 * Calls the specified function [block] with `this` value as its 	argument and returns its result.
	 */
	@kotlin.internal.InlineOnly
	public inline fun <T, R> T.let(block: (T) -> R): R = block(this)
let 函数是inline函数，泛型扩展函数    
let函数的参数为函数类型，函数类型的参数为let函数接受者类型，返回值为R。   
let函数的返回值为R    
	
	
	"abc".let { print(it) }//abc
反编译的Java代码   
	
	
   	 public static final void main(@NotNull String[] args) {
      Intrinsics.checkParameterIsNotNull(args, "args");
      String a = "abc";
      System.out.print(a);
   	 }

## Standard.kt 中的apply ##
	
	
	/**
     *Calls the specified function [block] with `this` value as its receiver and returns `this` value.
     *public inline fun <T> T.apply(block: T.() -> Unit): T { block(); return this }
     */
    "abc".apply {
        this.toUpperCase()
    }
apply函数是inline函数，泛型扩展函数     
apply的参数为函数类型，函数类型是一个扩展函数，无参数，返回为Unit    
apply的返回值为apply函数的接收者   
## 然而 ##
![](http://i.imgur.com/QoD6vdk.png)   

发觉这六个函数的签名是完全不一样的，应用的场景或许是不同的吧，至于函数名为什么是这些，我也是蒙逼。只能真正使用的时候才能决定哪个，毕竟现在写得有点少kotlin的代码    

## 关于return 和inline函数 ##
inline函数和普通函数区别   
	
	
	
	fun normalFun(): Int {
	    println("i am normal fun ")
	    return 1
	}
	
	fun nonInlineFun(a: () -> Unit) {
	    println(a.invoke())
	    println("non inline fun print")
	
	}
	
	inline fun inlineFun(a: () -> Int) {
	    println(a.invoke())
	    println("inline fun print")
	}
	
	nonInlineFun { normalFun() }

    inlineFun { normalFun() }

非inline函数 反编译的Java代码
	
	
	public static final void main(@NotNull String[] args) {
      Intrinsics.checkParameterIsNotNull(args, "args");
      nonInlineFun((Function0)null.INSTANCE);
    }
inline函数   
	
		
	public static final void main(@NotNull String[] args) {
      Intrinsics.checkParameterIsNotNull(args, "args");
      int $i$a$1$inlineFun = normalFun();
      System.out.println($i$a$1$inlineFun);
      String $i$a$1$inlineFun1 = "inline fun print";
      System.out.println($i$a$1$inlineFun1);
    }
在调用处，不存在调用inlineFun函数，基本上直接将inlineFun中的代码复制过来，而且，下面的代码，针对同样是normalFun函数返回Int类型，反编译之后的nonInlineFun和inlineFun应对的策略也是不一样的       
	
	
	public static final int normalFun() {
      String var0 = "i am normal fun ";
      System.out.println(var0);
      return 1;
    }

    public static final void nonInlineFun(@NotNull Function0 a) {
      Intrinsics.checkParameterIsNotNull(a, "a");
      Object var1 = a.invoke();
      System.out.println(var1);
      String var2 = "non inline fun print";
      System.out.println(var2);
    }

    public static final void inlineFun(@NotNull Function0 a) {
      Intrinsics.checkParameterIsNotNull(a, "a");
      int var2 = ((Number)a.invoke()).intValue();
      System.out.println(var2);
      String var3 = "inline fun print";
      System.out.println(var3);
    }	
而对于使用lambda表达式传递函数类型的字面值，是直接将字面值也复制到调用处。使用lambda的好处相比较于传递一个函数过去，也是省略了一个函数调用过程    

而对于return，在lambda表达式中，除了是传给inline函数的其他的lambda表达式都是不允许出现无标签的return的，而在lambda中使用无标签的return的时候，是直接退出inline函数的外部函数，而加上inline函数名标签的return才是退出inline函数。而反编译的结构直接是最后的结果。看得出转换非常智能    


