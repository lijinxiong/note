## attr ##
attribute，属性,风格样式style的最小单元。   
### 步骤 ###
1. 创建新的.xml文件，自定义属性  

		<?xml version="1.0" encoding="utf-8"?>
		<resources>

    	<declare-styleable name="test">

        <attr name="custom_attr1" format="string"></attr>
        <attr name="custom_attr2" format="integer"></attr>

    	</declare-styleable>
		</resources>
2. 自定义View   
	
		package com.example.jinxiong.test.attr;
	
		import android.content.Context;
		import android.content.res.TypedArray;
		import android.os.Build;
		import android.support.annotation.Nullable;
		import android.support.annotation.RequiresApi;
		import android.util.AttributeSet;
		import android.util.Log;
		import android.widget.TextView;
		
		import com.example.jinxiong.test.R;
		
		/**
		 * Created by jinxiong on 2017/3/7.
		 */
		
		public class MyTextView extends TextView {
	
	    private static final String TAG = "MyTextView";
	
	    public MyTextView(Context context) {
	        super(context);
	    }
	
	    public MyTextView(Context context, @Nullable AttributeSet attrs) {
	        this(context, attrs, 0);
	        init(context, attrs);
	    }
	
	    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
	        super(context, attrs, defStyleAttr);
	        init(context, attrs);
	
	    }
	
	    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
	        super(context, attrs, defStyleAttr, defStyleRes);
	        init(context, attrs);
	    }
	
	    private void init(Context context, @Nullable AttributeSet attrs) {
	        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.test);
	
	        Log.d(TAG, "test_custom_attr1: " + typedArray.getString(R.styleable.test_custom_attr1));
	        Log.d(TAG, "test_custom_attr2: " + typedArray.getInteger(R.styleable.test_custom_attr2, -1));
	
	    }
		}
xml布局：  

		<?xml version="1.0" encoding="utf-8"?>
		<android.support.constraint.ConstraintLayout
	    xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:app="http://schemas.android.com/apk/res-auto"
	    xmlns:tools="http://schemas.android.com/tools"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    tools:context="com.example.jinxiong.test.attr.Main2Activity">
	
	
	    <com.example.jinxiong.test.attr.MyTextView
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:text="@string/app_name"
	        app:custom_attr1="i am custom_attr1"
	        app:custom_attr2="12"
	        />
	
		</android.support.constraint.ConstraintLayout>
log:  
![](https://github.com/lijinxiong/note/blob/master/Android/img/attr和style和theme_01.png)  
### attr注意项 ###
####attr 和 styleable的关系 ####
attr不依赖styleable，放在styleable 中只是为了方便使用attr，
	
	<?xml version="1.0" encoding="utf-8"?>
	<resources>

    <declare-styleable name="adfsfsdff">

        <attr name="custom_attr1" format="string"></attr>
        <attr name="custom_attr2" format="integer"></attr>

    </declare-styleable>
    <attr name="custom_attr3" format="string"/>

	</resources>
获取

	private void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.adfsfsdff);

        Log.d(TAG, "attr1  " + typedArray.getString(R.styleable.adfsfsdff_custom_attr1));
        Log.d(TAG, "attr2  " + typedArray.getInteger(R.styleable.adfsfsdff_custom_attr2, -1));
        typedArray.recycle();

        TypedArray attr3 = context.obtainStyledAttributes(attrs, new int[]{R.attr.custom_attr3});
        Log.d(TAG, "attr3 " + attr3.getString(0));
		attr3.recycle();

    }
为什么是这样子，我们可以查看R文件   
![](https://github.com/lijinxiong/note/blob/master/Android/img/attr和style和theme_02.png)   
![](https://github.com/lijinxiong/note/blob/master/Android/img/attr和style和theme_03.png)  
可以看到所有的attr在R文件中都生成一个id，所以说，attr的name在整个的project中是唯一的（自定义的，可以跟系统中的相同，比如定义一个textColor的attr，是可以跟系统定义的一样的name，但是不能再定义一个textColor的attr了），而放在styleable的attr1和attr2只是被放在了一个叫做adfsfsdff（styleable name）数组中，R.styleable.adfsfsdff_custom_attr2和attr1 就是对应的下标值，对应着0和1.所以我么使用attr3的时候，也是先定义一个数组，然后再通过下标0来获取它   
#### styleable的name规范 ####
官方的推荐做法是一般为View的名字，不会像我上面那样为了测试随意打几个英文。
#### 其他 ####
	
		<attr name="numeric">
            <!-- Input is numeric. -->
            <flag name="integer" value="0x01" />
            <!-- Input is numeric, with sign allowed. -->
            <flag name="signed" value="0x03" />
            <!-- Input is numeric, with decimals allowed. -->
            <flag name="decimal" value="0x05" />
        </attr>
		   
        <attr name="bufferType">
            <!-- Can return any CharSequence, possibly a
             Spanned one if the source text was Spanned. -->
            <enum name="normal" value="0" />
            <!-- Can only return Spannable. -->
            <enum name="spannable" value="1" />
            <!-- Can only return Spannable and Editable. -->
            <enum name="editable" value="2" />
        </attr>
这是系统的TextView的attr属性定义的一部分，可以看到，一个在attr之间一个用了enum，一个是flag，之间有什么不一样。   
![](https://github.com/lijinxiong/note/blob/master/Android/img/attr和style和theme_04.png)   
![](https://github.com/lijinxiong/note/blob/master/Android/img/attr和style和theme_05.png)     
enum只能选其中的一个，而flag是可以多选的，falg的值可以做或运算
## style ##
样式是指为 **View 或窗口**指定外观和格式的属性集合。样式可以指定高度、填充、字体颜色、字号、背景色等许多属性。 样式是在与指定布局的 XML 不同的 XML 资源中进行定义。

Android 中的样式与网页设计中层叠样式表的原理类似 — 您可以通过它将***设计与内容分离***。
	
	<?xml version="1.0" encoding="utf-8"?>
	<android.support.constraint.ConstraintLayout
	    xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:app="http://schemas.android.com/apk/res-auto"
	    xmlns:tools="http://schemas.android.com/tools"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    tools:context="com.example.jinxiong.test.style.Main3Activity">
	
	    <TextView
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:textColor="@android:color/holo_red_dark"
	        android:textSize="16sp"
	
	        />
	
	</android.support.constraint.ConstraintLayout>	
和这样是一样的效果的   
	
	    <TextView
        style="@style/textView"
        />
	<style name="textView">
        <item name="android:text">test</item>
        <item name="android:layout_width">wrap_content</item>
        <item name="android:layout_height">wrap_content</item>
        <item name="android:textSize">16sp</item>
        <item name="android:textColor">@android:color/holo_red_dark</item>
    </style>
#### 定义样式 ####
要创建一组样式，在res/values/目录想保存一个xml文件，该XML文件的根节点必须是< resources>.因为resource元素的每一额子元素在编译时都会被转换成一个资源对象，该对象可由< style >元素的name属性中的name值引用。可以在布局的xml中以@style/textView形式引用该样式。  

对于你想创建的每一个样式，向该文件中添加一个< style>元素，该元素带有对样式进行唯一标识的name属性（没了这个属性你定义的style就没法进行引用，name属性为必须属性），然后为该样式的每一个属性添加一个< item>元素，该元素带有声明样式属性和属性值的，属性名（name）为必须属性，属性值可以是string，其他资源的引用或者其他，根据对应的attr设定的format（attr上面有介绍）  
而style中的parent属性是可选的，他可以指定当前定义的style继承另外style  
#### 继承 ####
	
	<style name="textView2" parent="textView">

    </style>

    <style name="textView.textView3">

    </style>
两种继承的方式，但是你继承android内建的style的话，只能通过parent的方式。   
   
#### 其他 ####
如果你当前View应用样式中，有某些attr是不属于这个view的话，也就是说，当前view不能应用style中的某些属性，那么当前view就会忽略掉他，只应用他能应用的属性
## 主题Theme ##
主题是指对整个 Activity 或应用而不是对单个 View（如上例所示）应用的样式。 以主题形式应用样式时，Activity 或应用中的每个视图都将应用其支持的每个样式属性。比如application应用了上面的textView的style，那么整个app中的文本都会红色字体，16sp大小（属性没有再一次被覆盖）  
	
在 XML 中定义您想用作 Activity 或应用主题的样式与定义视图样式的方法完全相同。 诸如上文所定义的样式可作为单个视图的样式加以应用，也可作为整个 Activity 或应用的主题加以应用。 

#### 对 Activity 或应用应用主题 ####
要为您的应用的所有 Activity 设置主题，请打开 AndroidManifest.xml 文件并编辑 < application> 标记，在其中加入带样式名称的 android:theme 属性。 例如：

	< application android:theme="@style/CustomTheme">
如果您只想对应用中的一个 Activity 应用主题，则改为给 <activity> 标记添加 android:theme 属性。

正如 Android 提供了其他内建资源一样，有许多预定义主题可供您使用，可免于自行编写。 例如，您可以使用 Dialog 主题，为您的 Activity 赋予类似对话框的外观：

	< activity android:theme="@android:style/Theme.Dialog">
或者，如果您希望背景是透明的，则可使用 Translucent 主题：

	< activity android:theme="@android:style/Theme.Translucent">


**多看attrs.xml styles.xml themes.xml**

> [https://developer.android.com/guide/topics/ui/themes.html?hl=zh-cn#DefiningStyles](https://developer.android.com/guide/topics/ui/themes.html?hl=zh-cn#DefiningStyles "Android API")
