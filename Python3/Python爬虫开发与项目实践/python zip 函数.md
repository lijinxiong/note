**身体不好，补补.....**    
**基础不好，补补.....**
## 概述 ##
可以直接作用于for循环的数据类型有以下几种：   
一类是集合数据类型，如list、tuple、dict、set、str等；   
一类是generator，包括生成器和带yield的generator function。   
这些可以直接作用于for循环的对象统称为可迭代对象：**Iterable**。   
可以使用isinstance()判断一个对象是否是Iterable对象：   

	from collections import Iterable

	print(isinstance([], Iterable))
	print(isinstance({1}, Iterable))
	print(isinstance({}, Iterable))
	print(isinstance(tuple(), Iterable))
	print(isinstance('', Iterable))
	
	print(isinstance((x for x in range(5)), Iterable))
	
	generator1 = (x for x in range(5))
	print(type(generator1))
	print(next(generator1))

而generator 不但可以用于for 循环，还可以被next函数不断调用并返回下一个值，直到最后抛出StopIteration错误表示无法继续返回下一个值    
可以被next 函数调用并不断返回下一个值的对象称迭代器 **Iterator**    

list tuple set str dictionary 都只是 Iterable 但不是Iterator     

		
	print(isinstance({}, Iterator))
	print(isinstance(iter({}), Iterator))
	print(isinstance({}.__iter__(), Iterator))
可以使用iter()函数或者以上iterable 对象的__iter()__方法变成Iterator    

--------------------
为什么list、dict、str等数据类型不是Iterator？

> 这是因为Python的Iterator对象表示的是一个数据流，Iterator对象可以被next()函数调用并不断返回下一个数据，直到没有数据时抛出StopIteration错误。可以把这个数据流看做是一个有序序列，但我们却不能提前知道序列的长度，只能不断通过next()函数实现按需计算下一个数据，所以Iterator的计算是惰性的，只有在需要返回下一个数据时它才会计算。    


------------------

凡是可作用于for循环的对象都是Iterable类型；

凡是可作用于next()函数的对象都是Iterator类型，它们表示一个惰性计算的序列；

----------

> [来源:https://www.liaoxuefeng.com](https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/00143178254193589df9c612d2449618ea460e7a672a366000)      


## zip()函数 ##
zip() 函数以Iterable 作为参数，返回一个zip 对象，将参数的Iterable 对象中相应的元素打包成一个个元组。假如各个Iterable 参数的元素的个数不一样，那么返回的zip对象中包含的元组个数为参数中元素个数最少那个    

	from collections import Iterator, Iterable

	string1 = 'abc'
	list1 = [1, 2, 3]
	tuple1 = 4, 5, 6
	
	zipped = zip(string1, list1, tuple1)
	
	print(zipped)
	print(isinstance(zipped, Iterable))
	print(isinstance(zipped, Iterator))
	print(next(zipped))
	#输出
	<zip object at 0x0320EF08>
	True
	True
	('a', 1, 4)

例子    

	from collections import Iterator, Iterable

	string1 = 'abc'
	list1 = [1, 2, 3]
	tuple1 = 4, 5
	
	zipped = zip(string1, list1, tuple1)
	
	for a in zipped:
	    print(a)
	#输出
	('a', 1, 4)
	('b', 2, 5)
