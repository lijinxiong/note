## 什么是生成器 ##

- 生成器是一个iterator ，可用作for 循环和next()函数进行迭代   
- 生成器类似于返回值为list 的一个函数，这个函数可以接收参数，可以被调用，但是，不同于一般函数会一次性返回包含所有值的数组，生成器一次只产生一个值，这样消耗的内存就会大大减少，而且允许调用函数可以很快开始处理前几个返回值    

-----------

## python 中的生成器 ##
python 提供了两种基本的方式    
1. 生成器函数：跟普通函数一样，使用def 声明，但是函数内部使用**yield**关键字一次返回一个结果，然后当前函数阻塞，回到调用函数，依次循环      
2. 生成器表达式：返回一个对象，这个对象只有在需要的时候才产生结果   

------------

### 生成器函数 ###
随着时间的推移生成一个数值队列，一般的函数在执行完毕之后会返回一个值然后退出，但是生成器函数会自动挂起，然后重新拾起执行，他会利用yield 关键字挂起函数，给调用者返回一个值，同时保留了当前足够多的状态，可以是函数下次继续在挂起的位置继续执行    
拥有yield 语句的函数被编译成生成器，这类函数调用时返回一个生成器对象，但是不会马上执行该函数    


	def generator_fun():
	    print("run fun")
	    i = 0
	
	    while True:
	        yield i
	        i += 1
	
	
	def main():
	    gen = generator_fun()
	    print(gen)
	    print(generator_fun())
	
	    print("get generator")
	    print(gen.__next__())
	    print(next(gen))
	
	
	if __name__ == '__main__':
	    main()
	
	    # 输出
	    # <generator object generator_fun at 0x00C7EF60>
	    # <generator object generator_fun at 0x00C87120>
	    # get generator
	    # run fun
	    # 0
	    # 1
分析    

1. generator_fun 函数中使用yield 关键字，所以每次当调用此函数时，都会返回一个新generator 对象   
2. 调用generator_fun 时并不会触发执行该函数，只有当调用该对象的__next()__ 方法或者将该对象传递给next()方法，才会执行
3. 重复调用next方法或__next__()方法，都会从上次被挂起的地方开始执行     

------------

### 生成器表达式 ###


	test = tuple(x ** 2 for x in range(5))

	print(test)
	
	
	# 为什么((x ** 2 for x in range(5),),为什么是tuple 里面包含一个generator ，并没有像list 或者set那样，因为tuple是不可变的 )
	# {} , [] 底层都是调用生成器 的next方法
	
	test1 = {x ** 2 for x in range(5)}
	print(test1)
	
	
	test2 = [x ** 2 for x in range(5)]
	print(test2)
	
为什么tuple 不能像set 或者 list 那样使用生成器表达式？   

**tuple 不可变**    

> [大多数引用blog](https://www.cnblogs.com/cotyb/p/5260032.html)