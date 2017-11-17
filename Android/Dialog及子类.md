## Dialog ##
正常的使用    

	public class Main6Activity extends AppCompatActivity {

    Button mButton;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        init();
    }

    private void init() {

        mButton = (Button) this.findViewById(R.id.button);
        dialog = new Dialog(this);
        dialog.setTitle("title");

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

    }
	}

![](https://github.com/lijinxiong/note/blob/master/Android/img/Dialog及子类_01.gif)  
非常的丑陋！！！   
	
	 /**
     * Creates a dialog window that uses the default dialog theme.
     * <p>
     * The supplied {@code context} is used to obtain the window manager and
     * base theme used to present the dialog.
     *
     * @param context the context in which the dialog should run
     * @see android.R.styleable#Theme_dialogTheme
     */
其实我们可以自定义dialog的主题，包括将自定义的布局传给这个window让他显示  
		
	 private static final String TAG = "Main5Activity";

    private Dialog mDialog;
    private Button testButton;
    private Button confirmButton;
    private Button cancelButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        init();
    }

    private void init() {
        mDialog = new Dialog(this, R.style.my_dialog);
        testButton = (Button) this.findViewById(R.id.button);

        mDialog.setCanceledOnTouchOutside(false);//设置点击dialog以外的区域不能使dialog消失

        //加载自定义布局
        mDialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_content, null)
                , new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        confirmButton = (Button) mDialog.findViewById(R.id.confirm);
        cancelButton = (Button) mDialog.findViewById(R.id.cancel);

        Window window = mDialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        params.gravity = Gravity.CENTER;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Main5Activity.this, "confirm click", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Main5Activity.this, "cancel click", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        });

    }
dialog xml

	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="@android:color/white"
    android:orientation="vertical">

    <TextView
        android:id="@+id/title"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:gravity="center"
        android:paddingBottom="5dp"
        android:paddingTop="5dp"

        android:text="提示"
        android:textColor="@android:color/holo_blue_dark"
        android:textSize="18sp"
        />

    <TextView
        android:id="@+id/content"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:paddingBottom="5dp"
        android:paddingLeft="10dp"
        android:paddingTop="5dp"

        android:text="确定退出应用?"
        android:textColor="@android:color/darker_gray"
        />


    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="5dp"

        android:orientation="horizontal">

        <Button
            android:id="@+id/confirm"
            android:layout_width="0dp"

            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:background="@android:color/white"
            android:text="confirm"
            android:textColor="@android:color/holo_blue_light"
            />

        <android.support.v4.widget.Space
            android:layout_width="2dp"
            android:layout_height="match_parent"
            />

        <Button
            android:id="@+id/cancel"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:background="@android:color/white"
            android:text="cancel"
            android:textColor="@android:color/holo_blue_light"
            />

    </LinearLayout>

	</LinearLayout>  
style  
	
	 <style name="my_dialog" parent="Base.Theme.AppCompat.Dialog">
        <item name="android:windowIsFloating">true</item>
        <item name="android:windowFrame">@null</item>
    </style>
![](https://github.com/lijinxiong/note/blob/master/Android/img/Dialog及子类_02.gif)
  
	
	