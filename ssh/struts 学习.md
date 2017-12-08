## 概述  ##

> 什么是Struts2的框架   

Struts2是Struts1的下一代产品，是在 struts1和WebWork的技术基础上进行了合并的全新的Struts 2框架。
其全新的Struts 2的体系结构与Struts 1的体系结构差别巨大。
Struts 2以WebWork为核心，采用拦截器的机制来处理用户的请求，这样的设计也使得**业务逻辑控制器**能够与ServletAPI完全脱离开，所以Struts 2可以理解为WebWork的更新产品。
虽然从Struts 1到Struts 2有着太大的变化，但是相对于WebWork，Struts 2的变化很小。
Struts2是一个基于MVC设计模式的Web层框架    

> 常见的Web层的框架   

Struts1   
Struts2  
Webwork  
SpringMVC   

> Web层框架的特点   

都是一个特点，前端控制器模式  
记住：前端控制器（核心的控制器）  
Struts2框架前端的控制器就是过滤器   

> [官网下载](https://struts.apache.org/)    

## itellj idea 创建项目##

![](https://i.imgur.com/r3WslOp.png)     

然后next，输入项目名字，finish     

结构目录    
![](https://i.imgur.com/8GKhbfs.png)   
启动tomcat 马上就能访问到index.jsp 的内容    

默认创建的web.xml


	<?xml version="1.0" encoding="UTF-8"?>
	<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
	         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
	         version="3.1">
	    <filter>
	        <filter-name>struts2</filter-name>
	        <filter-class>org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter</filter-class>
	    </filter>
	    <filter-mapping>
	        <filter-name>struts2</filter-name>
	        <url-pattern>/*</url-pattern>
	    </filter-mapping>
	</web-app>

StrutsPrepareAndExecuteFilter 类是一个前端控制器的类    

### 编写Action 类01 ###
Action 类


	package com.action;
	
	public class HelloAction {
	
	    public String sayHello() {
	        System.out.println("sayHello method invoke");
	        return "ok";
	    }
	}
编写一个名为ok.jsp 的文件在web 目录下，然后在struts.xml配置    


	<?xml version="1.0" encoding="UTF-8"?>
	
	<!DOCTYPE struts PUBLIC
	        "-//Apache Software Foundation//DTD Struts Configuration 2.5//EN"
	        "http://struts.apache.org/dtds/struts-2.5.dtd">
	
	<struts>
	    <!--包的名称，要求是唯一的，管理action配置-->
	    <package name="firstPackage" extends="struts-default" namespace="/">
	
	        <action name="sayHi" class="com.action.HelloAction" method="sayHello">
	            <result name="ok">/ok.jsp</result>
	        </action>
	    </package>
	
	</struts>   

启动tomcat，访问网址  [http://localhost:8080/sayHi](http://localhost:8080/sayHi)    
返回ok.jsp     
 
### 编写Action 类02 实现Action 接口 ###
Action类   

	package com.action;

	import com.opensymphony.xwork2.Action;
	
	public class HelloAction02 implements Action {
	
	    @Override
	    public String execute() throws Exception {
	        return "ok";
	    }
	}
同样配置struts.xml文件    

重新启动tomcat，访问对应的网址    

### 编写Action 类03 继承ActionSupport ###
Action类   
    

	package com.action;

	import com.opensymphony.xwork2.ActionSupport;	
	public class HelloAction03 extends ActionSupport{
	
	    @Override
	    public String execute() throws Exception {
	        return "ok";
	    }
	}
#### Action接口中的产量 ####


	package com.opensymphony.xwork2;

	public interface Action {
	    java.lang.String SUCCESS = "success";
	    java.lang.String NONE = "none";
	    java.lang.String ERROR = "error";
	    java.lang.String INPUT = "input";
	    java.lang.String LOGIN = "login";
	
	    java.lang.String execute() throws java.lang.Exception;
	}

## Struts 的执行流程 ##

1. 编写的页面，点击链接，请求提交到服务器   
2. 请求会先经过Struts的核心过滤器StrutsPrepareAndExecuteFilter（过滤器的功能就是完成一部分功能，其实也就是一系列的拦截器执行了，进行一些处理功能，这些默认的拦截器可以在struts-default.xml中看到）![](https://i.imgur.com/Cu3ni97.png)    
3. 拦截器执行完毕之后，就会在struts.xml中找到与请求对应的配置，找到对应的Action类，通过反射调用其处理的方法   

-----------------
> JSP页面-->StrutsPrepereAndExecuteFilter过滤器-->执行一系列拦截器（完成了部分代码）-->执行到目标Action-->返回字符串-->结果页面（result）-->页面跳转     
 
## Struts2 加载配置文件的顺序 ##

Struts2 的核心是StrutsPrepareAndExecuteFilter 过滤器，该过滤器有两个主要的功能   
1. prepare ：预加载，加载核心的配置文件   
2. Execute ：执行，让部分的拦截器执行    


加载顺序   
1. default.properties 里面的内容就是一些默认的配置，都是常量值![](https://i.imgur.com/rKcWj2k.png)    
2. struts-default.xml，struts-plugin.xmlstruts.xml。![](https://i.imgur.com/6o4EKql.png)，其中一个struts-plugin文件![](https://i.imgur.com/L4N0MGI.png)，里面的常量都是描述对应插件的某个功能开关    
3. 加载自定义的struts.properties，这个文件跟struts.xml是一样的，只是文件格式不一样   
4. 加载用户自定义配置提供者     
5. 加载web.xml    


**后加载的配置文件会覆盖掉之前加载的配置！！**

-----


## struts.xml 配置文件 ##

### package ###
如果要配置<Action\>的标签，那么必须要先配置<package\>标签，代表的包的概念      
包含的属性   

- name ：包的名称，任意但是全局唯一，管理action配置
- extends： 继承，可以继承其他的包，只要继承了，那么该包就包含了其他包的功能，一般都是继承struts-default   
- namespace 名称空间，一般与<action>标签中的name属性共同决定访问路径（通俗话：怎么来访问action），常见的配置如下    
	- namespace="/"     -- 根名称空间
	- namespace="/aaa"  -- 带有名称的名称空间
- abstract 抽象的。这个属性基本很少使用，值如果是true，那么编写的包是被继承的    


### <action\>标签 ###
代表配置action类，包含的属性    

- name 和<package\>标签的namespace属性一起来决定访问路径的    
- class 配置Action类的全路径（默认值是ActionSupport类）
- method Action类中执行的方法，如果不指定，默认值是execute    

### <result\>标签 ###
action 标签中的子标签    

- name  结果页面逻辑视图名称
- type  结果类型（默认值是转发，也可以设置其他的值）    


## Struts2配置常量 ##
在struts.xml文件<struts\>中

    <constant name="struts.i18n.encoding" value="utf-8"/>



常用配置   

 * struts.i18n.encoding=UTF-8            -- 指定默认编码集,作用于HttpServletRequest的setCharacterEncoding方法 
 * struts.action.extension=action,,      -- 该属性指定需要Struts 2处理的请求后缀，该属性的默认值是action，即所有匹配*.action的请求都由Struts2处理。如果用户需要指定多个请求后缀，则多个后缀之间以英文逗号（,）隔开
 * struts.serve.static.browserCache=true     -- 设置浏览器是否缓存静态内容,默认值为true(生产环境下使用),开发阶段最好关闭 
 * struts.configuration.xml.reload=false     -- 当struts的配置文件修改后,系统是否自动重新加载该文件,默认值为false(生产环境下使用) 
 * struts.devMode = false                    -- 开发模式下使用,这样可以打印出更详细的错误信息 

## 其他标签 ##

1. 使用include 标签引入其他配置文件   


## Action的访问 ##
1. 通过<action\>标签中的method属性，访问到Action中的具体的方法.传统的配置方式，配置更清晰更好理解！但是扩展需要修改配置文件等！   
	页面代码

	
		<a href="${pageContext.request.contextPath}/addBook.action">添加图书</a>
        <a href="${pageContext.request.contextPath}/deleteBook.action">删除图书</a>
	配置文件的代码   
		
		
		<package name="demo2" extends="struts-default" namespace="/">
                <action name="addBook" class="cn.itcast.demo2.BookAction" method="add"></action>
                <action name="deleteBook" class="cn.itcast.demo2.BookAction" method="delete"></action>
         </package>
	
	Action的代码
	
	
		public String add(){
              System.out.println("添加图书");
              return NONE;
           }
        public String delete(){
               System.out.println("删除图书");
               return NONE;
           }

2. 通配符的访问方式:(访问的路径和方法的名称必须要有某种联系.)  通配符就是 * 代表任意的字符     
	页面代码   

		<a href="${pageContext.request.contextPath}/order_add.action">添加订单</a>
        <a href="${pageContext.request.contextPath}/order_delete.action">删除订单</a>
	配置文件代码
     	
		<action name="order_*" class="cn.itcast.demo2.OrderAction" method="{1}"></action>
	{1}代表 * 的取值     
	
	具体理解：在JSP页面发送请求，http://localhost/struts2_01/order_add.action，配置文件中的order_\*可以匹配该请求，\*就相当于变成了add，method属性的值使用{1}来代替，{1}就表示的是第一个*号的位置！！所以method的值就等于了add，那么就找到Action类中的add方法，那么add方法就执行了！
3. 动态方法访问的方式（有的开发中也会使用这种方式）   
	将下面的常量设置为true   
		
		<constant name="struts.enable.DynamicMethodInvocation" value="true"/>
	代码
		
		<a href="${pageContext.request.contextPath}/product!add.action">添加商品</a>
		<a href="${pageContext.request.contextPath}/product!delete.action">删除商品</a>
		<action name="product" class="cn.itcast.demo2.ProductAction"></action>
		public class ProductAction extends ActionSupport{
             public String add(){
				System.out.println("添加订单");
				return NONE;
              }
              public String delete(){
                 System.out.println("删除订单");
                 return NONE;
               }
        }
	
## 在Struts2框架中使用Servlet的API ##
### 使用ActionContext ###
![](https://i.imgur.com/zdTh4hf.png)     
### 使用ServletActionContext ###
![](https://i.imgur.com/KjJIOal.png)    

> [原文](http://www.jianshu.com/p/4110d8ddcdb7)