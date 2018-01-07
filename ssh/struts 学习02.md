## 接受页面传过来的参数 ##
### 同名参数 ###
页面中传递过来的参数名和对应Action类中的成员变量名一样，并且为这些成员变量提供set/get方法，页面将参数传递过来之后struts2 会完成参数和成员变量之间的类型转换，传值完成之后可以直接在execute 中直接使用   
代码 

		<%--
	  Created by IntelliJ IDEA.
	  User: jinxiong
	  Date: 2017/12/8
	  Time: 20:55
	  To change this template use File | Settings | File Templates.
	--%>
	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<html>
	<head>
	    <title>Title</title>
	</head>
	<body>
	
	<form action="/formAction" method="post">
	    <input type="text" name="name">
	    <input type="text" name="age">
	    <input type="text" name="sex">
	    <input type="submit">
	    
	</form>
	
	</body>
	</html>

	public class FormAction01 extends ActionSupport {


	    private String name;
	    private int age;
	    private String sex;
	
	
	    public String getName() {
	        return name;
	    }
	
	    public void setName(String name) {
	        this.name = name;
	    }
	
	    public int getAge() {
	        return age;
	    }
	
	    public void setAge(int age) {
	        this.age = age;
	    }
	
	    public String getSex() {
	        return sex;
	    }
	
	    public void setSex(String sex) {
	        this.sex = sex;
	    }
	
	    @Override
	    public String execute() throws Exception {
	
	        System.out.println(name + age + sex);
	        return NONE;
	    }
	
	}
### 域模型 ###
将传递过来的参数封装成一个javabean，在Action类中作为成员变量，提供get/set方法，在提交的页面中，参数的名字为  Action类中javabean的变量名.属性名   
代码

	<form action="/formAction" method="post">
	    <input type="text" name="user.name">
	    <input type="text" name="user.age">
	    <input type="text" name="user.sex">
	    <input type="submit">
	</form>
	
	private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String execute() throws Exception {

        System.out.println(user.toString());
        return NONE;
    }
### 模型驱动 ###
实现接口ModelDriven中的getModel方法    

	
		public class ModelAction extends ActionSupport implements ModelDriven<User> {
	
	    private User user;
	
	
	    @Override
	    public User getModel() {
	        if (user == null) {
	            user = new User();
	        }
	        return user;
	    }
	
	    @Override
	    public String execute() throws Exception {
	        System.out.println(getModel().toString());
	        return NONE;
	    }
	}
	<form action="/modelAction" method="post">
	    <input type="text" name="name">
	    <input type="text" name="age">
	    <input type="text" name="sex">
	    <input type="submit">
	</form>
## action 中的跳转方式 ##

	<result name="" type="">..</result>
1. name 属性指的是跳转的名字（默认是success），也就是action返回的字符串
2. type 属性指的是跳转的类型（默认是dispatcher），常用到的有以下四种

### 内部跳转 ###

> dispatcher(acation -> jsp)   

从一个action里面服务器跳转到一个页面中，默认值    

> chain(action -> action)    

从一个action里面的服务器内部跳转到另一个action中    

	
        <action name="action02" class="com.action.Action02"></action>
        <action name="action01" class="com.action.Action01">
            <result type="chain" name="none">action02</result>
        </action>
### 外部跳转 ###

> redirect(actio ->jsp)

从一个action 里面客户端重定向到一个页面中      

	<action name="action03" class="com.action.Action03">
            <result type="redirect" name="none">http://www.jianshu.com/p/a884504a8863</result>
    </action>

> redirectAction

	    <action name="action03" class="com.action.Action03">
            <result type="redirectAction" name="none">action01</result>
        </action>

### 配置全局的跳转 ###
作用：配置一个全局的成功页面跳转或者其他，当action中没有配置时，就会在全局配置中查找并且跳转到对应的页面中


	<global-results>
            <result>/index.jsp</result>
    </global-results>

###  配置package中默认的action###
如果地址访问栏中访问到了当前package下面不存的action的时候，正常情况下是直接报错，而这个就是在这种情况下去访问到这个配置的action 中    

	<default-action-ref name="action01"></default-action-ref>

