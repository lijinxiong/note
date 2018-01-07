## 带有复杂类型的web 服务 ##

	
	package ch01.team;

	import java.util.List;
	import javax.jws.WebService;
	import javax.jws.WebMethod;
	
	@WebService
	public class Teams {
	    private TeamsUtility utils;
	
	    public Teams() {
	        utils = new TeamsUtility();
	        utils.make_test_teams();
	    }
	
	    @WebMethod
	    public Team getTeam(String name) {
	        return utils.getTeam(name);
	    }
	
	    @WebMethod
	    public List<Team> getTeams() {
	        return utils.getTeams();
	    }
	}
web 服务只是由一个单独的类实现，返回的类型也不是简单类型  
没有声明    
	
	@SOAPBinding(style = SOAPBinding.Style.RPC)//默认是SOAPBinding.Style.DOCUMENT
RPC只允许服务使用像字符串，整型那样的简单类型       


py测试（js 和py搞混了，zzzzz~~~~）     
	
	import suds

	url = "http://localhost:9876/teams?wsdl"
	
	client = suds.Client(url)
	team_list = client.service.getTeams()
	
	for team in team_list:
	    print(team)

## 多线程端点服务发布 ##


	package ch01.team;
	
	import javax.xml.ws.Endpoint;
	import java.util.concurrent.Executors;
	
	public class TeansPublisherMuti {
	
	    public static void main(String[] args) {
	
	        int port = 9876;
	        String url = "http://localhost:" + port + "/teams";
	
	        Endpoint endpoint = Endpoint.create(new Teams());
	
	        endpoint.setExecutor(Executors.newFixedThreadPool(10));
	        endpoint.publish(url);
	
	        System.out.println("Publishing Teams on port " + port);
	    }
	
	}
