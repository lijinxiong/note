## 概述 ##
官网文档：[https://developer.android.com/guide/components/fragments.html#Creating](https://developer.android.com/guide/components/fragments.html#Creating)   
Fragment中文称为碎片或者片段，片段必须始终嵌入在 Activity 中，其生命周期直接受宿主 Activity 生命周期的影响。 例如，当 Activity 暂停时，其中的所有片段也会暂停；当 Activity 被销毁时，所有片段也会被销毁。 不过，当 Activity 正在运行（处于已恢复生命周期状态）时，您可以独立操纵每个片段，如添加或移除它们。 
## 设计原理 ##
Android 在 Android 3.0（API 级别 11）中引入了片段，主要是为了给大屏幕（如平板电脑）上更加动态和灵活的 UI 设计提供支持。  
![](https://developer.android.com/images/fundamentals/fragments.png)   
## Fragment的生命周期 ##
![](https://developer.android.com/images/fragment_lifecycle.png)    
测试：
	
#### 正常加载fragment ####

	
	public class MyFragment1 extends Fragment {

    private static final String TAG = "MyFragment1";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "Fragment onAttach: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Fragment onCreate: ");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "Fragment onCreateView: ");
        return inflater.inflate(R.layout.my_fragment1, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "Fragment  onActivityCreated: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "Fragment  onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Fragment  onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "Fragment  onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "Fragment  onStop: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "Fragment onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Fragment  onDestroy: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "Fragment onDetach: ");
    }
	}
MainActivity   

	public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyFragment1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
        Log.d(TAG, "onCreate: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

	}
log
	
	03-20 09:12:30.213 13992-13992 D/MyFragment1: Fragment onAttach: 
	03-20 09:12:30.213 13992-13992 D/MyFragment1: Fragment onCreate: 
	03-20 09:12:30.213 13992-13992 D/MyFragment1: Fragment onCreateView: 
	03-20 09:12:30.214 13992-13992 D/MyFragment1: onCreate: 
	03-20 09:12:30.226 13992-13992 D/MyFragment1: Fragment  onActivityCreated: 
	03-20 09:12:30.226 13992-13992 D/MyFragment1: Fragment  onStart: 
	03-20 09:12:30.226 13992-13992 D/MyFragment1: onStart: 
	03-20 09:12:30.229 13992-13992 D/MyFragment1: onResume: 
	03-20 09:12:30.229 13992-13992 D/MyFragment1: Fragment  onResume: 
可以看到看到Fragment执行完onAttach，onCreate，onCreateView之后，Activity的onCreate才会被执行，然后才到onActivityCreated方法，然后才是其他。   
这些方法的具体说明：   
1. onAttach

	 /**
     * Called when a fragment is first attached to its activity.
     * {@link #onCreate(Bundle)} will be called after this.
     *
     * @deprecated See {@link #onAttach(Context)}.
     */
    @Deprecated
    @CallSuper
    public void onAttach(Activity activity)
当这个fragment第一次关联到它所依赖的activity的时候这个方法会被调用，这个方法调用之后onCreate就会被调用   
2. onCreate  
	
	 /**
     * Called to do initial creation of a fragment.  This is called after
     * {@link #onAttach(Activity)} and before
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * 
     * <p>Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, see {@link #onActivityCreated(Bundle)}.
     *
     * <p>Any restored child fragments will be created before the base
     * <code>Fragment.onCreate</code> method returns.</p>
     * 
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @CallSuper
    public void onCreate(@Nullable Bundle savedInstanceState)
这个方法用来初始化fragment，在onAttach方法之后，onCreateView之前。需要注意的是这个方法可以在当前activity还在创建的时候被调用（就是说activity还没执行完onCreate方法），所以你不要再这个点上依赖activity的某些事情，比如说activity的view层次树，如果你想当这个activity完成create的时候做一些事情，可以在onActivityCreated方法中做，这个时候的activity已经执行完onCreate了   

3.onActivityCreated  

		/**
	     * Called when the fragment's activity has been created and this
	     * fragment's view hierarchy instantiated.  It can be used to do final
	     * initialization once these pieces are in place, such as retrieving
	     * views or restoring state.  It is also useful for fragments that use
	     * {@link #setRetainInstance(boolean)} to retain their instance,
	     * as this callback tells the fragment when it is fully associated with
	     * the new activity instance.  This is called after {@link #onCreateView}
	     * and before {@link #onViewStateRestored(Bundle)}.  
	     * @param savedInstanceState If the fragment is being re-created from
	     * a previous saved state, this is the state.  
	     */  
	    @CallSuper  
	    public void onActivityCreated(@Nullable Bundle savedInstanceState)   

当关联这个fragment的activity 被创建之后 和 这个fragment 的 视图层次树已经实例化了这个方法就会被调用，可以在此时做一些最后的初始化，例如查找view和保存状态。通过使用setRetainInstance方法来保存fragment的实例，当fragment重新关联到另一个activity实例的时候，fragment 不会再次被创建，这个方法后面再试试。onActivityCreated这个方法在onCreateView之后调用，onViewStateRestored之前调用   

4.onStart  
	

		/**
	     * Called when the Fragment is visible to the user.  This is generally
	     * tied to {@link Activity#onStart() Activity.onStart} of the containing
	     * Activity's lifecycle.
	     */
	    @CallSuper
	    public void onStart() {  
当这个fragment 对用户可见的时候被调用，通常这个方法和activity的onStart束缚着一起   
 
5.onResume  

		/**
	     * Called when the fragment is visible to the user and actively running.
	     * This is generally
	     * tied to {@link Activity#onResume() Activity.onResume} of the containing
	     * Activity's lifecycle.
	     */
	    @CallSuper
	    public void onResume()
这个方法是在activity已经执行完onResume方法之后才调用的
#### 从当前的activity跳转到另一个activity ####
	
	03-23 10:24:20.153 2562-2562 D/MyFragment1: Fragment  onPause: 
	03-23 10:24:20.153 2562-2562 D/MyFragment1: onPause: 
	03-23 10:24:20.834 2562-2562 D/MyFragment1: Fragment  onStop: 
	03-23 10:24:20.834 2562-2562 D/MyFragment1: onStop: 
额....这好像没什么说的，log很清楚，按返回键回到有fragment的activity中
	
	03-23 10:30:16.949 2562-2562 D/MyFragment1: Fragment  onStart: 
	03-23 10:30:16.949 2562-2562 D/MyFragment1: onStart: 
	03-23 10:30:16.949 2562-2562 D/MyFragment1: onResume: 
	03-23 10:30:16.949 2562-2562 D/MyFragment1: Fragment  onResume: 
额，然后Fragment的onStart永远在activity的之前调用，fragment 的onResume在activity running了之后调用  
![](https://developer.android.com/images/activity_fragment_lifecycle.png)  
上图是activity和fragment 的生命周期图，
#### 旋转屏幕 ####
	
	03-23 10:58:26.689 2562-2562 D/MyFragment1: Fragment  onPause: 
	03-23 10:58:26.689 2562-2562 D/MyFragment1: onPause: 
	03-23 10:58:26.690 2562-2562 D/MyFragment1: Fragment  onStop: 
	03-23 10:58:26.690 2562-2562 D/MyFragment1: onStop: 
	03-23 10:58:26.690 2562-2562 D/MyFragment1: Fragment onDestroyView: 
	03-23 10:58:26.690 2562-2562 D/MyFragment1: Fragment  onDestroy: 
	03-23 10:58:26.690 2562-2562 D/MyFragment1: Fragment onDetach: 
	03-23 10:58:26.690 2562-2562 D/MyFragment1: onDestroy: 
	03-23 10:58:26.753 2562-2562 D/MyFragment1: Fragment onAttach: 
	03-23 10:58:26.753 2562-2562 D/MyFragment1: Fragment onCreate: 
	03-23 10:58:26.753 2562-2562 D/MyFragment1: Fragment onCreateView: 
	03-23 10:58:26.754 2562-2562 D/MyFragment1: onCreate: 
	03-23 10:58:26.759 2562-2562 D/MyFragment1: Fragment  onActivityCreated: 
	03-23 10:58:26.759 2562-2562 D/MyFragment1: Fragment  onStart: 
	03-23 10:58:26.759 2562-2562 D/MyFragment1: onStart: 
	03-23 10:58:26.760 2562-2562 D/MyFragment1: onResume: 
	03-23 10:58:26.760 2562-2562 D/MyFragment1: Fragment  onResume: 
毫无疑问，先是fragment onDestroy之后到activity的onDestroy，之后两个重新创建。  
然后我们在fragment 的 onActivityCreated方法中加入下面的语句，
	
	setRetainInstance(true);
再次运行，然后旋转屏幕，   
![](https://github.com/lijinxiong/note/blob/master/Android/img/Fragment_01.gif)  
然后log  
	
	03-23 11:07:05.983 7004-7004 D/MyFragment1: Fragment  onPause: 
	03-23 11:07:05.983 7004-7004 D/MyFragment1: onPause: 
	03-23 11:07:05.989 7004-7004 D/MyFragment1: Fragment  onStop: 
	03-23 11:07:05.989 7004-7004 D/MyFragment1: onStop: 
	03-23 11:07:05.989 7004-7004 D/MyFragment1: Fragment onDestroyView: 
	03-23 11:07:05.989 7004-7004 D/MyFragment1: Fragment onDetach: 
	03-23 11:07:05.989 7004-7004 D/MyFragment1: onDestroy: 
	03-23 11:07:06.028 7004-7004 D/MyFragment1: Fragment onAttach: 
	03-23 11:07:06.028 7004-7004 D/MyFragment1: Fragment onCreateView: 
	03-23 11:07:06.036 7004-7004 D/MyFragment1: onCreate: 
	03-23 11:07:06.039 7004-7004 D/MyFragment1: Fragment  onActivityCreated: 
	03-23 11:07:06.040 7004-7004 D/MyFragment1: Fragment  onStart: 
	03-23 11:07:06.040 7004-7004 D/MyFragment1: onStart: 
	03-23 11:07:06.040 7004-7004 D/MyFragment1: onResume: 
	03-23 11:07:06.040 7004-7004 D/MyFragment1: Fragment  onResume: 
可以看到fragment并没有destroy和create，那么现在可以看看setRetainInstance方法   
	
	/**
     * Control whether a fragment instance is retained across Activity
     * re-creation (such as from a configuration change).  This can only
     * be used with fragments not in the back stack.  If set, the fragment
     * lifecycle will be slightly different when an activity is recreated:
     * <ul>
     * <li> {@link #onDestroy()} will not be called (but {@link #onDetach()} still
     * will be, because the fragment is being detached from its current activity).
     * <li> {@link #onCreate(Bundle)} will not be called since the fragment
     * is not being re-created.
     * <li> {@link #onAttach(Activity)} and {@link #onActivityCreated(Bundle)} <b>will</b>
     * still be called.
     * </ul>
     */
    public void setRetainInstance(boolean retain)   
可以看到，当我们调用这个方法传true给它的时候，fragment生命周期会发生一些细微的改变，当与它关联的activity重新create的时候     

1. onDestroy方法不会被调用，但是onDetach方法会调用，因为需要从旧activity中解离出来  
2. onCreate不会再被调用，因为这个fragment不需要再次被创建，它的实例还存在，  
3. onAttact 和 onCreateView还是被调用了，也可以看到，旋转之前，我在edittext中输入了一些内容，但是旋转之后就没了，是因为旋转之后的edittext不在是以前的那个了，内容当然不在了。重新创建了一个edttext在onCreateView中。   
## 动态加载Fragment ##
	
	this.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();

                MyFragment1 myFragment1 = new MyFragment1();
                transaction.add(R.id.root_view, myFragment1);
                transaction.commit();
            }
        });
当我们按下按钮的时候，就会   
![](https://github.com/lijinxiong/note/blob/master/Android/img/Fragment_02.gif)  
但是当我们按下返回键的时候并没有回到最初的界面，而是退出了整个activity，那么在当中加上下面一句代码   
	
	transaction.addToBackStack(null);
当我们add fragment之后再返回就能回到最初没有fragment的页面，不至于退出activity   
	
	/**
     * Add this transaction to the back stack.  This means that the transaction
     * will be remembered after it is committed, and will reverse its operation
     * when later popped off the stack.
     *
     * @param name An optional name for this back stack state, or null.
     */
    public abstract FragmentTransaction addToBackStack(@Nullable String name);
这个方法的作用就是将事务保存到返回栈中，当出栈的时候就会倒退（撤销）回去。  
如果您向事务添加了多个更改（如又一个 add() 或 remove()），并且调用了 addToBackStack()，则在调用 commit() 前应用的所有更改都将作为单一事务添加到返回栈，并且返回按钮会将它们一并撤消。

向 FragmentTransaction 添加更改的顺序无关紧要，不过：

您必须最后调用 commit()  
如果您要向同一容器添加多个片段，则您添加片段的顺序将决定它们在视图层次结构中的出现顺序  
   
如果您没有在执行移除片段的事务时调用 addToBackStack()，则事务提交时该片段会被销毁，用户将无法回退到该片段。 不过，如果您在删除片段时调用了 addToBackStack()，则系统会停止该片段，并在用户回退时将其恢复。 
#### 创建对 Activity 的事件回调 ####
例如，如果一个新闻应用的 Activity 有两个片段 — 一个用于显示文章列表（片段 A），另一个用于显示文章（片段 B）— 那么片段 A 必须在列表项被选定后告知 Activity，以便它告知片段 B 显示该文章。就是两个fragment通过activity相互交互

	public class FragmentB extends Fragment {

    ActionB mActionB;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ActionB) {
            mActionB = (ActionB) context;
        }else {
            try {
                throw new Exception("must implement ActionB");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_b, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.getActivity().findViewById(R.id.action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionB.actionB();
            }
        });
    }

    public interface ActionB{
        void actionB();
    }

	}
MainActivity   
	
	public class FragmentInteractionActivity extends AppCompatActivity implements FragmentA.ActionA, FragmentB.ActionB {

    Fragment fragmentA;
    Fragment fragmentB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_interaction);

        if (fragmentA == null) {
            fragmentA = new FragmentA();
        }
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.root_view, fragmentA);
        transaction.commit();
    }

    @Override
    public void actionB() {
        if (fragmentA == null) {
            fragmentA = new FragmentA();
        }
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.root_view, fragmentA);
        transaction.commit();
    }

    @Override
    public void actionA() {
        if (fragmentB == null) {
            fragmentB = new FragmentB();
        }
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.root_view, fragmentB);
        transaction.commit();
    }
	}	
剩下的Fragment也是类似的，通过activity就不用fragment操纵fragment这种方法

demo代码下载  
[https://github.com/lijinxiong/Fragment](https://github.com/lijinxiong/Fragment "demo下载")
