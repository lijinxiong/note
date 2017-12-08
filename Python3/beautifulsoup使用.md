## 简介 ##
一个python库，提供一些简单的，python 式的函数用来处理导航，搜索，修改分析树的功能    
自动将输入文档转为unicode编码，输出文档转为utf-8编码    
## 历史版本 ##
Beautiful Soup 3 已经停止开发，现在都是Beautiful Soup 4，简称为bs4   

## 解释器 ##
beautifulsoup 支持Python标准库中的HTML 解释器，也支持一些第三方解释器，默认是使用python 标准库的HTML 解释器    
![](https://i.imgur.com/yCUHL6t.png)       
## 使用 ##
	
	
	from bs4 import BeautifulSoup
	import os
	
	
	html = """
	<html><head><title>The Dormouse's story</title></head>
	<body>
	<p class="title" name="dromouse"><b>The Dormouse's story</b></p>
	<p class="story">Once upon a time there were three little sisters; and their names were
	<a href="http://example.com/elsie" class="sister" id="link1"><!-- Elsie --></a>,
	<a href="http://example.com/lacie" class="sister" id="link2">Lacie</a> and
	<a href="http://example.com/tillie" class="sister" id="link3">Tillie</a>;
	and they lived at the bottom of a well.</p>
	<p class="story">...</p>
	"""
	
	f = open('test.html', 'r')
	soup = BeautifulSoup(f, 'lxml.parser')
	# 直接传入字符串,使用默认的解释器
	# soup = BeautifulSoup(html, 'html.parser')
	
	print(type(soup))
	print(soup)
	# 打印格式更好看
	print(soup.prettify())
### 四大对象种类 ###
bs4将复杂的html文档转换成一个复杂的树形结构，每个节点都是python对象，对象类型如下

#### Tag ####
对应的就是html 的一个个标签    
我们可以直接加上html 的标签名直接访问    
	
	
	print(soup.p)
	print(soup.title)
	#打印，只会打印出第一个匹配的标签
	<p class="title" name="dromouse"><b>The Dormouse's story</b></p>
	<title>The Dormouse's story</title>

Tag 类有两个重要的属性    

- name    
str 类型 对应的就是标签的名字   

	
		print(soup.name)
		print(soup.title.name)
		#打印		
		[document]
		title

- attrs    
字典类型，对应的就是标签里面的属性名和属性值    
	
	
		print(soup.p.attrs)
		{'class': ['title'], 'name': 'dromouse'}
### NavigableString ###
可以拿到整个标签了，那么如何拿到标签之间的内容呢   

	print(soup.p.string)
	The Dormouse's story

### BeautifulSoup ###
BeautifulSoup 对象表示的是一个文档的全部内容.大部分时候,可以把它当作 Tag 对象，是一个特殊的 Tag     
### Comment ###
Comment 对象是一个特殊类型的 NavigableString 对象，其实输出的内容仍然不包括注释符号，但是如果不好好处理它，可能会对我们的文本处理造成意想不到的麻烦。    
	
	print soup.a
	print soup.a.string
	print type(soup.a.string)
	结果
	<a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>
	 Elsie 
	<class 'bs4.element.Comment'>
