### 加密算法 ###
#### 对称加密算法 ####
在加密和解密时使用相同的密钥，或是使用两个可以简单地相互推算的密钥。实务上，这组密钥成为在两个或多个成员间的共同秘密，以便维持专属的通讯联系。与公开密钥加密相比，要求双方取得相同的密钥是对称密钥加密的主要缺点之一。常见的对称加密算法有DES、3DES、AES、Blowfish、IDEA、RC5、RC6。对称加密的速度比公钥加密快很多，在很多场合都需要对称加密。  
维基百科  
[https://zh.wikipedia.org/wiki/%E5%B0%8D%E7%A8%B1%E5%AF%86%E9%91%B0%E5%8A%A0%E5%AF%86](https://zh.wikipedia.org/wiki/%E5%B0%8D%E7%A8%B1%E5%AF%86%E9%91%B0%E5%8A%A0%E5%AF%86)  
client和server是怎么通过对称加密算法通信   
①client生成一个钥匙key1  
②client用key1加密需要传送给server的数据  
③client将加密后的数据和key1传送给server  
④server收到key1和加密的数据，用key1将加密的数据解密  
**问题**
在client传输数据和密钥key1的时候，中间可能给黑客截获，有了密钥就可以解密加密的数据
#### 非对称加密 ####
维基百科  
[https://zh.wikipedia.org/wiki/%E5%85%AC%E5%BC%80%E5%AF%86%E9%92%A5%E5%8A%A0%E5%AF%86](https://zh.wikipedia.org/wiki/%E5%85%AC%E5%BC%80%E5%AF%86%E9%92%A5%E5%8A%A0%E5%AF%86)  
这个算法中有两个钥匙，一个是公开的，一个是私有的，公开密钥一般用于给传输数据方，私钥一般在接收方  
client和server通信   
①server生成一对密钥，将其中的加密密钥（公开密钥）发送给client
②client用接受到的公开密钥加密需要传输的数据，并将加密的数据传输给server  
③server接收到数据，并用拥有的私钥解密加密了的数据  
**问题**
非对称加密算法只能加密比较少的数据，大的数据量会非常耗时  
#### 混合密码（对称密码和非对称密码结合） ####
通信过程  
①server生成一对密钥，将公开密钥发送给client    
②client生成以个密钥key1，将数据用key1加密，然后用sever发过来的公钥对key1进行加密，将用key1加密的数据和用公钥加密的key1发送给server  
③server收到加密的key1，用拥有的私钥加密key1，然后用解密了的key1解密加密的数据  
④通信完成  
