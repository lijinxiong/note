![](http://i.imgur.com/gqwWu5q.png)    
## 开车了,坐稳吧 ##
**MainActivity**
	
	 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
根据继承不一样的XxxActivity，会跳转到不一样的XxxActivity中，但是都是会跳回到**Activity**中   
	
	public void setContentView(@LayoutRes int layoutResID) {
        getWindow().setContentView(layoutResID);
        initWindowDecorActionBar();
    }
getWindow()返回的永远都是PhoneWindow   
	
	mWindow = new PhoneWindow(this, window);
直接去PhoneWindow看setContentView方法   
	
	@Override
    public void setContentView(int layoutResID) {
       
        if (mContentParent == null) {
            installDecor();
        } else if (!hasFeature(FEATURE_CONTENT_TRANSITIONS)) {
            mContentParent.removeAllViews();
        }

		//将布局文件放置到mContentParent中
        if (hasFeature(FEATURE_CONTENT_TRANSITIONS)) {
            final Scene newScene = Scene.getSceneForLayout(mContentParent, layoutResID,getContext());
            transitionTo(newScene);
        } else {
            mLayoutInflater.inflate(layoutResID, mContentParent);
        }
       
    }
*FEATURE_CONTENT_TRANSITIONS* 这个属性5.0才有的，就是由activity_main更换到activity_main1的时候展示一种过渡([链接](https://developer.android.com/reference/android/R.styleable.html))    
所以现在可以确定**mContentParent**是我们盛放着我们自定义的xml布局文件    
然后点击**installDecor()**方法   
	
	private void installDecor() {
        mForceDecorInstall = false;
        if (mDecor == null) {
            mDecor = generateDecor(-1);
            mDecor.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
            mDecor.setIsRootNamespace(true);
            if (!mInvalidatePanelMenuPosted && mInvalidatePanelMenuFeatures != 0) {
                mDecor.postOnAnimation(mInvalidatePanelMenuRunnable);
            }
        } else {
            mDecor.setWindow(this);
        }
        if (mContentParent == null) {
            mContentParent = generateLayout(mDecor);
			......一坨无关代码
        }
    }
**generateDecor(-1);**会new出一个DecorView并且关联上PhoneWindow，在看看**mContentParent**的赋值    
	
	protected ViewGroup generateLayout(DecorView decor) {
		
		//第一坨代码....
		mDecor.onResourcesLoaded(mLayoutInflater, layoutResource);

        ViewGroup contentParent = (ViewGroup)findViewById(ID_ANDROID_CONTENT);
        //一坨代码...

        return contentParent;
    }
**layoutResource**是一个xml布局文件的id，像我们自定义的activity_main，那么它是怎么来的？是根据略去的第一坨代码根据我们设置的主题，window属性啥的从系统提供的布局文件中选出来的。例如这个:  
		
	
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true"
    android:orientation="vertical">
    <ViewStub android:id="@+id/action_mode_bar_stub"
              android:inflatedId="@+id/action_mode_bar"
              android:layout="@layout/action_mode_bar"
              android:layout_width="match_parent"
              android:layout_height="wrap_content"
              android:theme="?attr/actionBarTheme" />
    <FrameLayout
         android:id="@android:id/content"
         android:layout_width="match_parent"
         android:layout_height="match_parent"
         android:foregroundInsidePadding="false"
         android:foregroundGravity="fill_horizontal|top"
         android:foreground="?android:attr/windowContentOverlay" />
	</LinearLayout>
我们现在先看第一个方法**onResourcesLoaded**   
 	
	//DecorView.java中
	void onResourcesLoaded(LayoutInflater inflater, int layoutResource) {
        
		//一坨代码...
		//一声不吭把系统选出来的布局文件加载进去返回View
        final View root = inflater.inflate(layoutResource, null);
        if (mDecorCaptionView != null) {
            if (mDecorCaptionView.getParent() == null) {
                addView(mDecorCaptionView,
                        new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
            }
            mDecorCaptionView.addView(root,
                    new ViewGroup.MarginLayoutParams(MATCH_PARENT, MATCH_PARENT));
        } else {

            // Put it below the color views.
            addView(root, 0, new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));//加到decorView中
        }
        mContentRoot = (ViewGroup) root;//mContentRoot终于出现了
        initializeElevation();
    }
mDecorCaptionView[源码飞机票](https://android.googlesource.com/platform/frameworks/base/+/android-7.1.1_r6/core/java/com/android/internal/widget/DecorCaptionView.java)是7.0出现的。   
不是7.0的时候就会执行把这个布局文件(view root)放到decorView中，而最后就把这个root赋值给mContentRoot，就相当于decorView中包着mContentRoot   
*mContentRoot 本来是放在PhoneWindow中的，像mContentParent一样，但是不知啥时候放到DecorView中了*
那么现在回到这   
	
	protected ViewGroup generateLayout(DecorView decor) {
		
		//第一坨代码....
		mDecor.onResourcesLoaded(mLayoutInflater, layoutResource);//刚刚看完我是不是

        ViewGroup contentParent = (ViewGroup)findViewById(ID_ANDROID_CONTENT);
        //一坨代码...

        return contentParent;
    }
这里返回的**contentParent**就是给**mContentParent**赋值的，  
	
	if (mContentParent == null) {
            mContentParent = generateLayout(mDecor);
			......一坨无关代码
        }
那么为什么会执行**findViewById**，这个id为啥是这个？   
	
	/**
     * The ID that the main layout in the XML layout file should have.
     */
    public static final int ID_ANDROID_CONTENT = com.android.internal.R.id.content;
再看看我们上面放出的xml文件是不是有个id为content的ViewGroup     
so，可以知道，我们的布局文件是包在**mContentParent**，而**mContentParent**是被包在**mContentRoot**中的，而**mContentRoot**是包在**decorView**中的。   
   
好了下车吧   
那么你知道**requestWindowFeature()**为啥在**setContentView**之前才有效了吗??   
[以前csdn的这篇文章，略有不同](http://blog.csdn.net/lijinxiong520/article/details/51842640)   
2017/5/27 16:15:46 