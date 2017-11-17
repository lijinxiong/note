![](http://i.imgur.com/KP8fptq.png)    
## 开篇语 ##
使用as3.0开发kotlin应用    
## 开始 ##

[官方下载地址](https://developer.android.com/studio/preview/index.html?utm_source=android-studio)   

![](http://i.imgur.com/OfC9t33.png)     

可以与稳定版共存，不会影响    
### 创建project ###
![](http://i.imgur.com/5TxfCXh.png)   
来看看生成的代码    
	
	
	class MainActivity : AppCompatActivity() {

	    override fun onCreate(savedInstanceState: Bundle?) {
	        super.onCreate(savedInstanceState)
	        setContentView(R.layout.activity_main)
	    }
	}
然后我们可以在app 模块下的build.gradle中加上这么一句话    

	
	apply plugin: 'kotlin-android-extensions'
然后我们可以直接在onCreate方法中加上这行代码

	
	text_view.text = "hello kotlin"//text_view是xml布局文件中的控件id
就是这么简单，我们就省去了findViewById然后转类型的麻烦   



