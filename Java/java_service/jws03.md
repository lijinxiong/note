## wsdl在web服务中的作用 ##
在基于soap协议的web 服务中，wsdl的主要作用就是对服务契约定义的描述     

### wsimport ###

命令行中输入 wsimport  
	
	用法: wsimport [options] <WSDL_URI>

	其中 [options] 包括:
	  -b <path>                 指定 jaxws/jaxb 绑定文件或附加模式
	                            (每个 <path> 都必须具有自己的 -b)
	  -B<jaxbOption>            将此选项传递给 JAXB 模式编译器
	  -catalog <file>           指定用于解析外部实体引用的目录文件
	                            支持 TR9401, XCatalog 和 OASIS XML 目录格式。
	  -d <directory>            指定放置生成的输出文件的位置
	  -encoding <encoding>      指定源文件所使用的字符编码
	  -extension                允许供应商扩展 - 不按规范
	                            指定功能。使用扩展可能会
	                            导致应用程序不可移植或
	                            无法与其他实现进行互操作
	  -help                     显示帮助
	  -httpproxy:<host>:<port>  指定 HTTP 代理服务器 (端口默认为 8080)
	  -keep                     保留生成的文件
	  -p <pkg>                  指定目标程序包
	  -quiet                    隐藏 wsimport 输出
	  -s <directory>            指定放置生成的源文件的位置
	  -target <version>         按给定的 JAXWS 规范版本生成代码
	                            默认为 2.2, 接受的值为 2.0, 2.1 和 2.2
	                            例如, 2.0 将为 JAXWS 2.0 规范生成兼容的代码
	  -verbose                  有关编译器在执行什么操作的输出消息
	  -version                  输出版本信息
	  -wsdllocation <location>  @WebServiceClient.wsdlLocation 值
	  -clientjar <jarfile>      创建生成的 Artifact 的 jar 文件以及
	                            调用 Web 服务所需的 WSDL 元数据。
	  -generateJWS              生成存根 JWS 实现文件
	  -implDestDir <directory>  指定生成 JWS 实现文件的位置
	  -implServiceName <name>   生成的 JWS 实现的服务名的本地部分
	  -implPortName <name>      生成的 JWS 实现的端口名的本地部分
	
	扩展:
	  -XadditionalHeaders              映射标头不绑定到请求或响应消息不绑定到
	                                   Java 方法参数
	  -Xauthfile                       用于传送以下格式的授权信息的文件:
	                                   http://username:password@example.org/stock?wsdl
	  -Xdebug                          输出调试信息
	  -Xno-addressing-databinding      允许 W3C EndpointReferenceType 到 Java 的绑定
	  -Xnocompile                      不编译生成的 Java 文件
	  -XdisableAuthenticator           禁用由 JAX-WS RI 使用的验证程序,
	                                   将忽略 -Xauthfile 选项 (如果设置)
	  -XdisableSSLHostnameVerification 在提取 wsdl 时禁用 SSL 主机名
	                                   验证
	
	示例:
	  wsimport stock.wsdl -b stock.xml -b stock.xjb
	  wsimport -d generated http://example.org/stock?wsdl

### 使用@WebResult ###
修改TimeService   
	
	package ch01.ts;

	import javax.jws.WebMethod;
	import javax.jws.WebResult;
	import javax.jws.WebService;
	import javax.jws.soap.SOAPBinding;
	
	@WebService
	@SOAPBinding(style = SOAPBinding.Style.RPC)//默认是SOAPBinding.Style.DOCUMENT
	public interface TimeService {
		
	    @WebMethod
	    @WebResult(partName = "result")
	    String getTimeAsString();
	
	    @WebMethod
	    long getTimeAsElapsed();
	
	}
打开wsdl    
	
	<message name="getTimeAsElapsed"/>
	<message name="getTimeAsElapsedResponse">
	<part name="return" type="xsd:long"/>
	</message>
	<message name="getTimeAsString"/>
	<message name="getTimeAsStringResponse">
	<part name="result" type="xsd:string"/>
	</message>
很少用到这个注解    

## wsdl 文档结构 ##
从高级别上来说，一个wsdl文档就是一个服务和消费者之间沟通的契约。这个契约提供了诸如服务端点，操作及对这些操作所需要的数据类型等关键信息     
整个wsdl文档结构最外层的元素名称被命名为 definitions      
组成结构     

- 类型（type）这部分不是必需的，通常基于像XML模式（xml schema）这样的数据类型系统来提供数据类型的定义。这种用来定义数据类型的文档就是xsd（xml schema definition）。在数据类型部分可以持有指向，引用一个xsd。如果类型部分为空，就像前面的TimeService，那么通常服务只是使用简单数据类型，比如xsd:String 和 xsd：long     
	下面是type不为空   
	
		<types>
		<xsd:schema>
		<xsd:import namespace="http://team.ch01/" schemaLocation="http://localhost:9876/teams?xsd=1"/>
		</xsd:schema>
		</types>
	http://localhost:9876/teams?xsd=1 内容   
		
		<xs:schema xmlns:tns="http://team.ch01/" xmlns:xs="http://www.w3.org/2001/XMLSchema" version="1.0" targetNamespace="http://team.ch01/">
		<xs:element name="getTeam" type="tns:getTeam"/>
		<xs:element name="getTeamResponse" type="tns:getTeamResponse"/>
		<xs:element name="getTeams" type="tns:getTeams"/>
		<xs:element name="getTeamsResponse" type="tns:getTeamsResponse"/>
		<xs:complexType name="getTeam">
		<xs:sequence>
		<xs:element name="arg0" type="xs:string" minOccurs="0"/>
		</xs:sequence>
		</xs:complexType>
		<xs:complexType name="getTeamResponse">
		<xs:sequence>
		<xs:element name="return" type="tns:team" minOccurs="0"/>
		</xs:sequence>
		</xs:complexType>
		<xs:complexType name="team">
		<xs:sequence>
		<xs:element name="name" type="xs:string" minOccurs="0"/>
		<xs:element name="players" type="tns:player" nillable="true" minOccurs="0" maxOccurs="unbounded"/>
		<xs:element name="rosterCount" type="xs:int"/>
		</xs:sequence>
		</xs:complexType>
		<xs:complexType name="player">
		<xs:sequence>
		<xs:element name="name" type="xs:string" minOccurs="0"/>
		<xs:element name="nickname" type="xs:string" minOccurs="0"/>
		</xs:sequence>
		</xs:complexType>
		<xs:complexType name="getTeams">
		<xs:sequence/>
		</xs:complexType>
		<xs:complexType name="getTeamsResponse">
		<xs:sequence>
		<xs:element name="return" type="tns:team" minOccurs="0" maxOccurs="unbounded"/>
		</xs:sequence>
		</xs:complexType>
		</xs:schema>
- 消息（message）该部分定义了实现服务的相关消息，消息使用的数据类型通常来自前面的类型定义部分，如果type为空，则使用的是简单的默认数据类型。而消息的顺序表明了服务的请求模式，消息，in/out表明是请求/响应模式，而out/in表明是要求/响应模式      

		<message name="getTimeAsElapsed"/>
		<message name="getTimeAsElapsedResponse">
		<part name="return" type="xsd:long"/>
		</message>
		<message name="getTimeAsString"/>
		<message name="getTimeAsStringResponse">
		<part name="result" type="xsd:string"/>
		</message>
		-----另一个-------
		<message name="getTeams">
		<part name="parameters" element="tns:getTeams"/>
		</message>
		<message name="getTeamsResponse">
		<part name="parameters" element="tns:getTeamsResponse"/>
		</message>
		<message name="getTeam">
		<part name="parameters" element="tns:getTeam"/>
		</message>
		<message name="getTeamResponse">
		<part name="parameters" element="tns:getTeamResponse"/>
		</message>
- porttype部分该部分以命名的操作描述了服务，每一个操作都是一个或多个消息。服务操作的名称是在web服务@WebMethod注解中指定。这个有点类似Java接口，用来抽象地描述服务，而不包括服务的实现细节。    
	
		<portType name="TimeService">
		<operation name="getTimeAsElapsed">
		<input wsam:Action="http://ts.ch01/TimeService/getTimeAsElapsedRequest" message="tns:getTimeAsElapsed"/>
		<output wsam:Action="http://ts.ch01/TimeService/getTimeAsElapsedResponse" message="tns:getTimeAsElapsedResponse"/>
		</operation>
		<operation name="getTimeAsString">
		<input wsam:Action="http://ts.ch01/TimeService/getTimeAsStringRequest" message="tns:getTimeAsString"/>
		<output wsam:Action="http://ts.ch01/TimeService/getTimeAsStringResponse" message="tns:getTimeAsStringResponse"/>
		</operation>
		</portType>
		----------另一个-----
		<portType name="Teams">
		<operation name="getTeams">
		<input wsam:Action="http://team.ch01/Teams/getTeamsRequest" message="tns:getTeams"/>
		<output wsam:Action="http://team.ch01/Teams/getTeamsResponse" message="tns:getTeamsResponse"/>
		</operation>
		<operation name="getTeam">
		<input wsam:Action="http://team.ch01/Teams/getTeamRequest" message="tns:getTeam"/>
		<output wsam:Action="http://team.ch01/Teams/getTeamResponse" message="tns:getTeamResponse"/>
		</operation>
		</portType>
- 绑定binding部分，是wsdl定义从抽象到具体的描述，可以看作是对porttype接口的一个实现类。描述了一些服务比较重要的实现细节     
	- 指明了发送和接受soap消息所使用的传输协议，http是主流   
	
			soap:binding transport="http://schemas.xmlsoap.org/soap/http" style="rpc"/>
	- 服务的绑定样式，style属性可以取rpc 和 document    
	- soap消息中使用的数据编码格式有两种，分别是literal 和encode 

- 服务service 指定一个或多个端点，端点中描述了服务的功能，以及服务所包括的所有操作。一个service可以包含一到多个port，一个port包括了与之对应的porttype 和 binding   

### wsdl的绑定 ###

	
	<binding name="TeamsPortBinding" type="tns:Teams">
	<soap:binding transport="http://schemas.xmlsoap.org/soap/http" style="document"/>
	<operation name="getTeams">
	<soap:operation soapAction=""/>
	<input>
	<soap:body use="literal"/>
	</input>
	<output>
	<soap:body use="literal"/>
	</output>
	</operation>
	<operation name="getTeam">
	<soap:operation soapAction=""/>
	<input>
	<soap:body use="literal"/>
	</input>
	<output>
	<soap:body use="literal"/>
	</output>
	</operation>
	</binding>
绑定的样式属性style 可以是rpc 或 document（默认）  而use 属性的值可以是literal（默认） 或者是encode      
不常使用encode ，因为违背了wsdl 最初的愿望：希望能帮助软件架构师和开发人员所开发的web服务在不同平台，计算机之间无缝互通     

将TimeService 变为style 为document ，重新生成客户端代码    
会多出GetTimeAsElapsed GetTimeAsElapsedResponse 等多个java文件，请求的时候使用的是GetTimeAsElapsed类型，而返回的时候是GetTimeAsElapsedResponse类型     
对应的wsdl 文件也会发生变化      

### document 绑定样式服务特点 ###
