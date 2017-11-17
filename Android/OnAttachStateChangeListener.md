## 概述 ##
	
	/**
     * Interface definition for a callback to be invoked when this view is attached
     * or detached from its window.
     */
    public interface OnAttachStateChangeListener {
        /**
         * Called when the view is attached to a window.
         * @param v The view that was attached
         */
        public void onViewAttachedToWindow(View v);
        /**
         * Called when the view is detached from a window.
         * @param v The view that was detached
         */
        public void onViewDetachedFromWindow(View v);
    }
当你的view attach 或者 detach到window的时候都会回调这个接口，我们可以测试一下在什么时候view会attach 和detach 到window上  
### 1. ###

	package com.example.jinxiong.onattachstatechange.view;

	import android.content.Context;
	import android.graphics.Canvas;
	import android.support.annotation.Nullable;
	import android.util.AttributeSet;
	import android.util.Log;
	import android.view.View;
	
	/**
	 * Created by jinxiong on 2017/3/20.
	 */
	
	public class MyView extends android.support.v7.widget.AppCompatTextView implements View.OnAttachStateChangeListener {

    private static final String TAG = "MainActivity";

    public MyView(Context context) {
        super(context);
        init();
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        this.addOnAttachStateChangeListener(this);
    }


    @Override
    public void onViewAttachedToWindow(View v) {
        Log.d(TAG, "onViewAttachedToWindow: ");
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        Log.d(TAG, "onViewDetachedFromWindow: ");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure: ");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d(TAG, "onLayout: ");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw: ");

    }
	}
重写TextView，为它增加onAttachStateChageListener,
	
	package com.example.jinxiong.onattachstatechange;

	import android.os.Bundle;
	import android.support.v7.app.AppCompatActivity;
	import android.util.Log;
	import android.view.View;
	
	public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: 之后");


        this.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.test);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
	}
在activity的onCreate 和 onDestroy方法中log，运行程序可以看到log如下：   

	03-19 21:37:07.851 15910-15910 D/MainActivity: onCreate: 之后
	03-19 21:37:07.879 15910-15910 D/MainActivity: onViewAttachedToWindow: 
	03-19 21:37:07.893 15910-15910 D/MainActivity: onMeasure: 
	03-19 21:37:08.040 15910-15910 D/MainActivity: onMeasure: 
	03-19 21:37:08.040 15910-15910 D/MainActivity: onLayout: 
	03-19 21:37:08.109 15910-15910 D/MainActivity: onMeasure: 
	03-19 21:37:08.109 15910-15910 D/MainActivity: onLayout: 
	03-19 21:37:08.109 15910-15910 D/MainActivity: onDraw: 
按下返回键  
	
	03-19 21:42:12.001 15910-15910 D/MainActivity: onDestroy: 
	03-19 21:42:12.002 15910-15910 D/MainActivity: onViewDetachedFromWindow:
**结论1**：在activity中，当执行完setContentView方法之后才会将view attach 到window上的，当执行完onDestroy后才会detach   
在上面增加了一个button ,也设置了一个点击事件，点击之后会再次调用setContentView方法，将另外的布局文件替换到原先的布局文件，log如下：  

	03-19 21:47:16.498 15910-15910 D/MainActivity: onViewDetachedFromWindow: 
**增加结论1：**当执行完setContentView方法之后才会将view attach 到window上的，当执行完onDestroy后或者setContentView被再一次调用，view才会detach

----------

