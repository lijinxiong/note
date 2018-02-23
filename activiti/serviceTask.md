serviceTask 有三个配置Type的方式，分别是Class,Expression DelegateExpression   

## Class ##
    <serviceTask activiti:class="cn.scau.jinxiong.activiti.service.impl.TestDelegateServiceImpl" 
	activiti:exclusive="true" id="_3" name="ServiceTask">   
TestDelegateServiceImpl 实现了activiti 包的JavaDelegate 接口   
当流程执行到此处时会调用JavaDelegate的execute 方法    

## DelegateExpression ##

	<serviceTask activiti:delegateExpression="${testDelegateServiceImpl}" activiti:exclusive="true" id="_3" name="ServiceTask"/>
主要是在Spring Spring Boot 等具有IOC能力框架中，声明一个bean名为testDelegateServiceImpl，这个类实现JavaDelegate      

## Expression ##

	
	    <serviceTask activiti:exclusive="true" activiti:expression="${activitiService.testActivitiMethod()}" id="_3" name="ServiceTask"/>
在IOC中注册的bean ，每当流程来到就会执行testActivitiMethod    

包含参数    
	
	    <serviceTask activiti:exclusive="true" activiti:expression="${activitiService.testActivitiMethod(name)}" id="_3" name="ServiceTask"/>
在启动流程或者在上一个流程中将参数name 传递给这个任务   

## 关于field  ##
	
	<activiti:field name="test" stringValue="${activitiService.testActivitiMethod()}"/>
	<activiti:field name="hah" expression="${activitiService.testActivitiMethod()}"/>
在对应的Delegate 实现类中就要用相应 的域 类型为Expression     

	System.out.println(test.getExpressionText());
	System.out.println(test.getValue(execution).toString());
	//${activitiService.testActivitiMethod()}
	
	System.out.println(hah.getExpressionText());
	System.out.println(hah.getValue(execution).toString());
	//activitiService.testActivitiMethod()
