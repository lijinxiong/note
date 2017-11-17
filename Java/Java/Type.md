## Java泛型 ##
在Java中，泛型技术实际上只是Java 语言的一颗语法糖，它只存在于程序的源代码中(\*.java文件中),在编译之后已经替换为原来的原生类型（raw type ），并在相应的地方插入了强制转型代码。所以对于Java 语言来说，ArrayList<Integer\> 和 ArrayList<String\> 就是同一个**类**。Java 语言的泛型实现方法称为类型擦除，基于这种方法实现的泛型称为**伪泛型**     
### 比较C# ###
C#是真正实现泛型的语言。C# 的泛型无论在程序的源代码，编译后的IL中(Intermediate language 中间语言，这时候的泛型是一个占位符)，或是运行期的CLR (common language runtime 公共语言运行时)中，都切实存在的。List<Integer\> 和 List<String\>就是两个不同的类型，它们在系统运行期生成，有自己的虚方法表和类型数据，这种实现称为**类型膨胀**，基于这种方法实现的泛型称为**真实泛型**    
## Class文件中属性表##
### Signature属性  ###
在JDK1.5增加到Class文件规范中，它是一个可选的定长属性，可以出现在类，属性表和方法表结构的属性表中。在此之后，任何类，接口，初始化方法或成员的泛型签名如果包含了类型变量（Type Variable）或参数化类型（Parameterized Type），则Signature 属性就会为它记录泛型签名信息。之所以要专门使用这样一个属性去记录泛型类型，是因为Java语言泛型是采用类型擦除实现的伪泛型。在字节码（Code属性）中，泛型信息编译（类型变量，参数化类型）之后，都通通被擦除掉。使用擦除法的好处是实现简单（主要修改Javac 编译器，虚拟机内部只做很少的改动），非常容易实现backport（向后移植），运行时也能够节省类型所占用的内存空间。但坏处就是运行期无法像C#等具有真泛型支持的语言那样，将泛型类型与用户定义的普通类型同等对待，如运行期做反射时无法获得泛型信息。Signature属性就是为了弥补这个缺陷而增设的，现在Java 的反射能够获取到泛型类型，最终的数据来源也就是这个属性    
如果当前的Signature 属性是类文件的属性，则这个结构表示类签名，如果当前的Signature 属性是方法表的属性，则这个结构表示方法类型签名，如果当前Signature属性是字段表的属性，则这个结构表示字段类型签名   
### Code属性 ###
如果把一个Java 程序中的信息分为代码（Code，方法体里面的Java 代码）和元数据（metadata，包括类，字段，方法定义以及其他信息）两部分，那么在整个Class文件中，Code属性用于描述代码，所有其他数据项都用于描述元数据      

	
	public  class Main{
	private List<String> stringList;
	
	public void method(){	
		String str = stringList.get(0);
	}
	}
![](https://i.imgur.com/KJIcllD.png)    

但是我们依然可以通过反射获得实例字段stringList传给List的类型String       

## Type体系 ##
### 介绍 ###
![](https://i.imgur.com/SSpv6LG.png)        

Type的继承扩展关系结构    
![](https://i.imgur.com/M4pIBtJ.png)     

### Class ###
Class 是Type的直接实现类，Class属于raw types（原生类型）    
![](https://i.imgur.com/DdgMoyJ.png)      

getGenericType方法   
![](https://i.imgur.com/7MEW8WQ.png)     

	//getGenericSignature方法
	//signature 为Field 中的字段
	//眼熟？
	private String getGenericSignature() {return signature;} 


FieldRepository getGenericInfo()      
![](https://i.imgur.com/2PlzVdP.png)     

FieldRepository 类    
![](https://i.imgur.com/MZtNV6q.png)    

getGenericType方法 最终拿的就是Class文件中的Signature属性中的信息，但是因为字段string的数据类型是String，并不是类型变量和参数化类型，所以signature 为空    
![](https://i.imgur.com/ItMp34g.png)     

### GenericArrayType ###
泛型数组      
![](https://i.imgur.com/nDLEqlD.png)       

GenericArrayType    
![](https://i.imgur.com/12n1ljL.png)     
凡是类型参数或者参数化类型的数组都属于GenericArrayType，方法getGenericComponentType返回的就是数组存放的数据类型    
	
	GenericArrayType genericArrayType = (GenericArrayType) tField.getGenericType();
        System.out.println(genericArrayType.getGenericComponentType().getTypeName());
	//打印为  T    //属于TypeVariable
	//假如 为List<T>[] 打印则为List<T> //属于ParameterizedType
### TypeVariable ###
类型参数    
从Filed中   	

	private T t;
	private static void typeVariable() throws NoSuchFieldException {

        Field tField = BlogType.class.getDeclaredField("t");

        Type type = tField.getGenericType();

        System.out.println(type.getTypeName());//T

        System.out.println(type.getClass().getName());//TypeVariable

    }
	
从类    
		
	public class BlogType<T,M> {
	private static void typeVariableFromClass() throws NoSuchFieldException {

        TypeVariable[] typeVariables = BlogType.class.getTypeParameters();
        for (TypeVariable typeVariable : typeVariables) {
            System.out.println(typeVariable.getTypeName());
        }
        /**
         * T
         * M
         */
    }
	}

### ParameterizedType ###
参数化类型    

	private List<T> tList;
	public static void parameterizedType() throws NoSuchFieldException {

        Field field = BlogType.class.getDeclaredField("tList");
        Type type = field.getGenericType();

        System.out.println(type.getTypeName());

        System.out.println(type.getClass().getName());
		//java.util.List<T>
		//sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl

    }						

从方法中    
	
	public static void parameterizedTypeFromMethod() throws NoSuchMethodException {

        Method method = BlogType.class.getDeclaredMethod("method", ArrayList.class);

        Type[] types = method.getGenericParameterTypes();
        for (Type type : types) {
            System.out.println(type.getTypeName());
            System.out.println(type.getClass().getName());
        }
        /**
         * java.util.ArrayList<T>
         * sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl
         */
    }
    public void method(ArrayList<T> list) {

    }

## 最后 ##
一个字段的类型(方法的参数)可以是一个原生类型，参数化类型，泛型数组类型，类型参数(类型)   
这些概念的推出只是为了进一步弥补使用泛型擦除实现泛型带来的一些问题    

----------

相关参考   
[http://www.jianshu.com/p/e8eeff12c306](http://www.jianshu.com/p/e8eeff12c306)  
[http://www.jianshu.com/p/7649f86614d3](http://www.jianshu.com/p/7649f86614d3)    
