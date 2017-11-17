## 介绍 ##
xmlns全称：xml namespace，xml 命名空间  
w3school:[http://www.w3school.com.cn/xml/xml_namespaces.asp](http://www.w3school.com.cn/xml/xml_namespaces.asp)   
xmlns 提供避免元素命名冲突的方法，主要是使用前缀来避免命名冲突，给个可能不是很准确的demo  
自定义属性：

		<?xml version="1.0" encoding="utf-8"?>
		<resources>
	    
		    <declare-styleable name="for_textView">
		        <attr name="textColor" format="color"></attr>
		    </declare-styleable>
	
		</resources>
自定义TextView  

	package company.com.progressdialog;

	import android.content.Context;
	import android.content.res.TypedArray;
	import android.os.Build;
	import android.support.annotation.RequiresApi;
	import android.util.AttributeSet;
	import android.util.Log;
	import android.widget.TextView;
	
	/**
	 * Created by jinxiong on 2017/3/6.
	 */
	
	public class MyTextView extends TextView {

    private static final String TAG = "MyTextView";
    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);

    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.for_textView);

        Log.d(TAG, "for_textView_textColor: " + typedArray.getColor(R.styleable.for_textView_textColor, -1));

        typedArray.recycle();
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }
	}
xml布局：   

	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:for_textView="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/activity_main2"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context="company.com.progressdialog.MainActivity">


    <company.com.progressdialog.MyTextView
        android:text="test"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textColor="@color/colorAccent"
        for_textView:textColor="#123456"
        />

	</LinearLayout>
那么在xml中布局中，MyTextView的textColor这个属性有两处定义，一个是"[http://schemas.android.com/apk/res/android](http://schemas.android.com/apk/res/android)",一个是我们本地自定义的（注：  
上面的xmlns:for_textView="http://schemas.android.com/apk/res-auto"  
可以替换为     
xmlns:for_textView="http://schemas.android.com/apk/company.com.progressdialog"  
）。假使是没有xmlns这个的前缀，那么当我们设置textColor的属性是设置哪一个的尼？  
### 结构 ###
XML 命名空间属性被放置于元素的开始标签之中，并使用以下的语法：   

	xmlns:namespace-prefix="namespaceURI"  
在我们android的每一个布局文件中的根元素中都会这么定义，其中的uri就是资源定位符，可以通过它找到在相应的属性定义。其中我们的namespace的可以随意定义   
	
	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout
    xmlns:hello="http://schemas.android.com/apk/res/android"
    xmlns:for_textView="http://schemas.android.com/apk/company.com.progressdialog"
    xmlns:tools="http://schemas.android.com/tools"
    hello:id="@+id/activity_main2"
    hello:layout_width="match_parent"
    hello:layout_height="match_parent"
    hello:orientation="vertical"
    tools:context="company.com.progressdialog.MainActivity">


    <company.com.progressdialog.MyTextView
        hello:text="test"
        hello:layout_width="wrap_content"
        hello:layout_height="wrap_content"
        hello:textColor="@color/colorAccent"
        for_textView:textColor="#123456"
        />

	</LinearLayout>
我们把原本的android这个ns（namespace）改为hello，这样也是可以的，但是什么必要   
## Android中常见的命名空间 ##
1. android

	    xmlns:android="http://schemas.android.com/apk/res/android"
在这个ns中我们经常使用得最多  
	
		<?xml version="1.0" encoding="utf-8"?>
		<LinearLayout
	    xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:tools="http://schemas.android.com/tools"
	    android:id="@+id/activity_main"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:orientation="vertical"
	    tools:context="company.com.progressdialog.MainActivity">
	    
		</LinearLayout>
2. tools  

	    xmlns:tools="http://schemas.android.com/tools"
这个命名空间可以把它理解为工具一样的空间，它的作用只是影响到开发阶段，在当这个项目编译成app安装在手机上的时候，以tools为前缀的ns的所有属性都被移除了  
    
	    <?xml version="1.0" encoding="utf-8"?>
		<LinearLayout
	    xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:tools="http://schemas.android.com/tools"
	    android:id="@+id/activity_main"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:orientation="vertical"
	    tools:context="company.com.progressdialog.MainActivity">
	
	
	    <TextView
	        android:text="test"
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        tools:layout_gravity="center"
	        />
		</LinearLayout>

![](https://github.com/lijinxiong/note/blob/master/Android/img/xmlns_01.png)   
但是运行在手机上的时候    
![](https://github.com/lijinxiong/note/blob/master/Android/img/xmlns_02.png)  
而且tool本身也有很多自己的特性,详细看这篇文章[https://github.com/lijinxiong/note/tree/master/Android/toolsNamespace.md](https://github.com/lijinxiong/note/tree/master/Android/toolsNamespace.md)  
3. 自定义命名空间  
就如一开始看到的for_textView那个命名空间，其中也关联着view的自定义和attr定义。可以看这个文章[https://github.com/lijinxiong/note/tree/master/Android/attr和style和theme.md](https://github.com/lijinxiong/note/tree/master/Android/attr和style和theme.md)




