官方:[https://developer.android.com/guide/topics/resources/drawable-resource.html#bitmap-element](https://developer.android.com/guide/topics/resources/drawable-resource.html#bitmap-element)  

定义：这是在xml中定义的一般形状   
一般放在drawable中  
语法：   

 
	<?xml version="1.0" encoding="utf-8"?>
	<shape
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape=["rectangle" | "oval" | "line" | "ring"] >
    <corners
        android:radius="integer"
        android:topLeftRadius="integer"
        android:topRightRadius="integer"
        android:bottomLeftRadius="integer"
        android:bottomRightRadius="integer" />
    <gradient
        android:angle="integer"
        android:centerX="float"
        android:centerY="float"
        android:centerColor="integer"
        android:endColor="color"
        android:gradientRadius="integer"
        android:startColor="color"
        android:type=["linear" | "radial" | "sweep"]
        android:useLevel=["true" | "false"] />
    <padding
        android:left="integer"
        android:top="integer"
        android:right="integer"
        android:bottom="integer" />
    <size
        android:width="integer"
        android:height="integer" />
    <solid
        android:color="color" />
    <stroke
        android:width="integer"
        android:color="color"
        android:dashWidth="integer"
        android:dashGap="integer" />
	</shape>
![](http://i.imgur.com/UBaMJZY.png)    
#### rectangle ####
![](http://i.imgur.com/ZsIZh54.png)   
< solid> 只有一个属性，就是color不写这个属性默认就是没有颜色     

< padding> 要应用到包含视图元素的内边距（这会填充视图内容的位置，而非形状）。意思就是会加上view中的padding，好像你的textview中的leftpadding设置为10dp，而在shape设置的left的padding为20dp，那么textView的内容到textView的左边界就是10+20dp，   
![](http://i.imgur.com/6SmwAgo.png)   
![](http://i.imgur.com/UZpWOuC.png)   
![](http://i.imgur.com/nYvqf7t.png)   
github地址：[https://github.com/lijinxiong/Style](https://github.com/lijinxiong/Style)  