毕设使用activiti，各种坑     

## 关于delegate ##

	<process id="delegateService" isClosed="false" isExecutable="true" name="delegateService" processType="None">
	    <startEvent id="_2" name="StartEvent"/>
	    <endEvent id="_3" name="EndEvent"/>
	    <serviceTask activiti:class="cn.scau.jinxiong.activiti.test.DelegateService" activiti:exclusive="true" id="_4" name="ServiceTask"/>
	    <sequenceFlow id="_5" sourceRef="_2" targetRef="_4"/>
	    <sequenceFlow id="_6" sourceRef="_4" targetRef="_3"/>
  	</process>
	
	@Component(value = "delegateService")
	public class DelegateService implements JavaDelegate{
	
	    @Autowired
	    private SysParamService sysParamService;
	
	    @Override
	    public void execute(DelegateExecution execution) {
	        System.out.println(this.toString());
	        System.out.println("**************");
	        System.out.println(sysParamService);
	    }
	}
测试类

	@SpringBootTest
	@RunWith(SpringRunner.class)
	public class DelegateServiceTest {
	
	    @Autowired
	    private ActivitiService activitiService;
	
	    @Autowired
	    private RuntimeService runtimeService;
	
	    @Autowired
	    private DelegateService delegateService;
	
	    @Test
	    public void testDelegate() {
	
	        activitiService.deployment("delegateService");
	        runtimeService.startProcessInstanceByKey("delegateService");
	        System.out.println("*************");
	        System.out.println(delegateService.toString());
	
	    }
	    
	}
一切看起来是没什么问题的    
但是   

	
	cn.scau.jinxiong.activiti.test.DelegateService@68303c3e
	**************
	null
	*************
	cn.scau.jinxiong.activiti.test.DelegateService@194224ca
在delegate 中打印出来对象的hash是跟在测试中获取到的不一样的，then sysParamService 是为null 的，Spring 没有注入进去    

解释   
	当使用 activiti:class 把一个class指定给ServiceTask时,需要实现JavaDelegate接口，activiti引擎将会在内部用Class.newInstance(..)方法创建一个该类的对象，这个对象并不spring容器管理，所以无法获取spring容器给我们生成的bean；
验证   
	
	public DelegateService() {
        System.out.println("-------------");
        System.out.println(this.toString());
        System.out.println("create delegate");
    }
	//打印
	-------------
	cn.scau.jinxiong.activiti.test.DelegateService@54275b5d
	create delegate
	-------------
	cn.scau.jinxiong.activiti.test.DelegateService@58647985
	create delegate
	cn.scau.jinxiong.activiti.test.DelegateService@58647985
	**************
	null
	*************
	cn.scau.jinxiong.activiti.test.DelegateService@54275b5d
创建了两次
### 使用delegate expression 方式 ###
	
	-------------
	cn.scau.jinxiong.activiti.test.DelegateService@38054d01
	create delegate
	cn.scau.jinxiong.activiti.test.DelegateService@38054d01
	**************
	cn.scau.jinxiong.service.impl.SysParamServiceImpl@254e9709
	*************
	cn.scau.jinxiong.activiti.test.DelegateService@38054d01
Spring 正常注入    
### 使用expression ###
也是正常的    

[相关链接](http://blog.csdn.net/authork/article/details/43966835)
	