# 状态列表 #
## state_enble ##
当前的view是否能够消费点击事件，
	
	<?xml version="1.0" encoding="utf-8"?>
	<selector xmlns:android="http://schemas.android.com/apk/res/android">

    <!--this.findViewById(R.id.my_btn).setEnabled(boolean);-->
    <!--android:enabled="false"-->
    <!--代表这个view是否能够消费掉点击事件，false就是不能，true反之-->
    <item android:drawable="@android:color/holo_blue_dark"
          android:state_enabled="false"/>
    <item android:drawable="@android:color/holo_orange_dark"
          android:state_enabled="true"
        />
	</selector>
## state_pressed ##
view是否被压着，
		
	<?xml version="1.0" encoding="utf-8"?>
	<selector xmlns:android="http://schemas.android.com/apk/res/android">

    <!--压-->
    <item
        android:drawable="@color/colorAccent"
        android:state_pressed="true"/>

    <!--没压-->
    <item
        android:drawable="@color/colorPrimary"
        android:state_pressed="false"/>

	</selector>
![](https://github.com/lijinxiong/note/blob/master/Android/img/selector_01.gif)   
## state_hovered ##
	
	<?xml version="1.0" encoding="utf-8"?>
	<selector xmlns:android="http://schemas.android.com/apk/res/android">

    <!--这个貌似不好用的赶脚-->
    <item android:drawable="@color/colorPrimary"
          android:state_hovered="true"/>
    <item android:drawable="@color/colorAccent"
          android:state_hovered="false"/>
	</selector>
## state_focused ##
	
	<?xml version="1.0" encoding="utf-8"?>
	<selector xmlns:android="http://schemas.android.com/apk/res/android">

    <item android:drawable="@color/colorAccent"
          android:state_focused="true"/>
    <item android:drawable="@color/colorPrimaryDark"
          android:state_focused="false"/>
	</selector>
![](https://github.com/lijinxiong/note/blob/master/Android/img/selector_02.gif)  
那两个控件是editview   
## state_checked ##
	
	<?xml version="1.0" encoding="utf-8"?>
	<selector xmlns:android="http://schemas.android.com/apk/res/android">


    <item android:drawable="@color/colorPrimary"
          android:state_checked="true"
        />
    <item android:drawable="@color/colorAccent"
          android:state_checked="false"/>
	</selector>      
![](https://github.com/lijinxiong/note/blob/master/Android/img/selector_03.gif)  
## state_window_focused ##

	<?xml version="1.0" encoding="utf-8"?>
	<selector xmlns:android="http://schemas.android.com/apk/res/android">
    
    <!--如果当应用窗口有焦点（应用在前台）时应使用此项目，则值为“true”；
    如果当应用窗口没有焦点（例如，通知栏下拉或对话框出现）时应使用此项目，则值为“false”。-->
    <item android:drawable="@color/colorAccent"
          android:state_window_focused="true"/>
    <item android:drawable="@color/colorPrimaryDark"
          android:state_window_focused="false"/>

	</selector>
![](https://github.com/lijinxiong/note/blob/master/Android/img/selector_04.gif)  

## enterFadeDuration 和 exitFadeDuration ##
	
	<?xml version="1.0" encoding="utf-8"?>
	<selector
    android:enterFadeDuration="5000"
    android:exitFadeDuration="5000"
    xmlns:android="http://schemas.android.com/apk/res/android">


    <item android:drawable="@color/colorPrimary"
          android:state_checked="true"
        />
    <item android:drawable="@color/colorAccent"
          android:state_checked="false"/>
	</selector>
![](https://github.com/lijinxiong/note/blob/master/Android/img/selector_05.gif)
----------
**注：**请记住，Android 将应用状态列表中第一个与对象当前状态匹配的项目。因此，如果列表中的第一个项目不含上述任何状态属性，则每次都会应用它，这就是默认值应始终放在最后的原因（如以下示例所示）。
		
	<?xml version="1.0" encoding="utf-8"?>
	<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_pressed="true"
          android:drawable="@drawable/button_pressed" /> <!-- pressed -->
    <item android:state_focused="true"
          android:drawable="@drawable/button_focused" /> <!-- focused -->
    <item android:state_hovered="true"
          android:drawable="@drawable/button_focused" /> <!-- hovered -->
    <item android:drawable="@drawable/button_normal" /> <!-- default -->
	</selector>
demo：[https://github.com/lijinxiong/Select](https://github.com/lijinxiong/Select)