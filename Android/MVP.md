## 概述 ##
![](http://i.imgur.com/4eLyEG6.png)  

在Android中  

1. view 指代的是activity和xml  
2. model指代的是一切的业务逻辑层的东西，下载，文件读写的一些耗时与业务相关的操作  
3. presenter 就是连接view和model的桥梁  

相对于mvc，mvp就是切断了model 和 view之间的直接联系  
![](http://i.imgur.com/QfXwPq1.png)    
相对与mvc 在Android中activity的任务量非常大，所以继而优化出mvp
## 任务 ##
实现一个小的mvp 的 demo ，功能是从网上下载一段json数据，然后再一步步优化一些小细节  
### 流程 ###
![](http://i.imgur.com/Zjy1Leq.png)  

大概就是这么个样子  
### 步骤 ###
#### 分析 ####
看第一张图片可知道，presenter作为view 和 model的桥梁，必须是拥有view层和model层的实例（先不管是怎么样的是实例），而view能向presenter发起数据请求，那么也拥有presenter的实例。而model层接收完信息是怎么通知presenter的尼？那么也就是回调函数（其实也算是拥有一个presenter的实例了）  
#### 定义包和接口 ####
model的接口   

	public interface IModel {
	
	    /**
	     *
	     * @param loadDataListener presenter 实现这个接口，用于加载数据完成后
	     *                         presenter能够知道
	     * @return 因为可能需要耗时操作，不能卡死main线程，所以返回值为void
	     */
	    void loadData(LoadDataListener loadDataListener);
	
	    interface LoadDataListener {

        /**
         *当数据加载完成之后调用此函数，将加载的数据发给presenter
         */
        void complete(String data);

    }
	
	
	}
presenter接口：  
  
	public interface IPresenter {
	
	    void getData();
	}

那么对于view，在这里就是activity，是否也需要抽象一些功能出来变成一个接口，比如加载前，加载中甚至加载完成的时候显示加载的数据？   
view 的 接口
	
	public interface IView {
	
	    /**
	     * 加载数据的时候一些用户提示
	     */
	    void showDowning();
	
	    /**
	     * 加载完成的时候显示数据
	     */
	
	    void showData(String data);
	}

#### 实现接口 ####
view
	
	public class MainActivity extends AppCompatActivity implements IView {
	
	    private TextView mTextView;
	    private Button mButton;

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	        init();
	
	    }
	
	    private void init() {
	        mTextView = (TextView) this.findViewById(R.id.content_view);
	        mButton = (Button) this.findViewById(R.id.download_data_btn);
	
	        mButton.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                //拥有一个presenter的实例，将一个Iview的实例传递过去
	                new PresenterImp(MainActivity.this).getData();
	            }
	        });
	    }
	
	    /**
	     * 提示用户加载开始了
	     */
	    @Override
	    public void showDowning() {
	        Toast.makeText(this, "下载中....", Toast.LENGTH_SHORT).show();
	    }
	
	    /**
	     * 展示加载的数据
	     *
	     * @param data
	     */
	    @Override
	    public void showData(String data) {
	        mTextView.setText(data);
	    }
	}
model:  

	
	public class ModelImp implements IModel {
	
	    private static final String TAG = "ModelImp";
	
	    /**
	     *
	     * @param loadDataListener presenter 实现这个接口，用于加载数据完成后
	     *                         presenter能够知道
	     */
	    @Override
	    public void loadData(final LoadDataListener loadDataListener) {
	
	        new Thread(new Runnable() {
	            @Override
	            public void run() {
	
	                try {
	                    URL url = new URL("http://tj.nineton.cn/Heart/index/future24h/" +
	                            "?city=CHSH000000&language=zh-chs&" +
	                            "key=36bdd59658111bc23ff2bf9aaf6e345c");

	                    URLConnection urlConnection = url.openConnection();
	                    InputStream inputStream = urlConnection.getInputStream();
	
	                    byte[] bytes = new byte[1024];
	                    inputStream.read(bytes);
	
	                    String s = new String(bytes);
	
	                    Log.d(TAG, "run: " + s);
	                    loadDataListener.complete(s);
	                } catch (MalformedURLException e) {
	                    e.printStackTrace();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }).start();
	    }
	}
这里下载的数据是没有解释的，懒  
presenter：  
	
	public class PresenterImp implements IPresenter {
	
	    //拥有一个model 的实例，我们可以直接new 一个
	    IModel mIModel = new ModelImp();
	
	    //那么拥有一个view的实例怎么写？直接把activity传过来？
	    //显然是不合适的，我们拿到这个view的实例只是为了提示下载时候的一些信息，是不是，下载的时候告诉用户开始下载了
	    //所以我们就只需要一个Iview的接口的实例就可以了，并且view（activity）已经实现了这个接口的
	
	    IView mIView;
	
	    public PresenterImp(IView IView) {
	        mIView = IView;
	    }
	
	    @Override
	    public void getData() {
	
	        if (null != mIView) {
	            //提示用户下载开始了
	            mIView.showDowning();
	        }
	
	        //开始下载
	        mIModel.loadData(new IModel.LoadDataListener() {
	
	            /**
	             * model层下载完成回调此函数，将数据传过来,这里注意的是这个方法是在model层被调用了，
	             * 而我们是在子线程中下载数据，这个方法也是在子线程中被回调，所以更新ui需要注意
	             * @param data
	             */
	            @Override
	            public void complete(final String data) {
	
	                ((Activity) mIView).runOnUiThread(new Runnable() {
	                    @Override
	                    public void run() {
	                        mIView.showData(data);
	                    }
	                });
	            }
	        });
	    }
	}

运行结果：  
![](http://i.imgur.com/RGilbJV.png)  

至此我们算是完成了mvp式的demo，那么这样子就是最完美的吗？
### 问题分析 ###
#### 1 ####
我们在上面的分析中说到在presenter中是持有View ，Model的实例的，而Model层通过回调函数通知presenter层下载数据的情况，而这个回调函数所在的接口就是由presenter其内部实现的，也就是说这个接口实例中持有着presenter的实例   

![](http://i.imgur.com/a0BcYTy.png)  
那么相当于在model层中拥有着view层的实例（间接），也就是在拥有activity的实例，并且在这里还在子线程中持有，那么就会导致一个常见的内存泄漏问题   

**解决**   
把在presenter层中的Iview实例换成若引用  
	
	public class PresenterImp implements IPresenter {
	
	
	    //拥有一个model 的实例，我们可以直接new 一个
	    IModel mIModel = new ModelImp();
	
	    //那么拥有一个view的实例怎么写？直接把activity传过来？
	    //显然是不合适的，我们拿到这个view的实例只是为了提示下载时候的一些信息，是不是，下载的时候告诉用户开始下载了
	    //所以我们就只需要一个Iview的接口的实例就可以了，并且view（activity）已经实现了这个接口的
	
	    WeakReference<IView> mIViewWeakReference;
	
	    public PresenterImp(IView iView) {
	        mIViewWeakReference = new WeakReference<>(iView);
	    }
	
	    @Override
	    public void getData() {
	
	        IView iView = mIViewWeakReference.get();
	        if (null != iView) {
	            //提示用户下载开始了
	            iView.showDowning();
	        }
	
	        //开始下载
	        mIModel.loadData(new IModel.LoadDataListener() {
	
	            /**
	             * model层下载完成回调此函数，将数据传过来,这里注意的是这个方法是在model层被调用了，
	             * 而我们是在子线程中下载数据，这个方法也是在子线程中被回调，所以更新ui需要注意
	             * @param data
	             */
	            @Override
	            public void complete(final String data) {
	
	                IView iView = mIViewWeakReference.get();
	
	                if (null != iView) {
	
	                    ((Activity) iView).runOnUiThread(new Runnable() {
	                        @Override
	                        public void run() {
	
	                            IView iView = mIViewWeakReference.get();
	                            if (null != iView) {
	                                iView.showData(data);
	                            }
	                        }
	                    });
	                }
	            }
	        });
	    }
	}
但是这样就真的没事了嘛？那么还有什么问题？
#### 2 ####
比如说，加载的时间是10s，而用户在5s的时候退出当前的activity，虽然在presenter层中是弱引用，但是对于系统而言，即使是这样子，系统也不一定在model层调用complete函数的之前把这个activity回收掉，也就是说，我依然会执行iView.showData(),单数对于activity来说，它已经是destroy了，这样子也是会出现一些问题     
那么现在只需在activity的onDestroy方法中处理下在presenter中的弱引用就可以了   

	
	public class MainActivity extends AppCompatActivity implements IView {
	
	    private TextView mTextView;
	    private Button mButton;
	    private IPresenter mIPresenter;
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	        init();
	
	    }
	
	    private void init() {
	        mTextView = (TextView) this.findViewById(R.id.content_view);
	        mButton = (Button) this.findViewById(R.id.download_data_btn);
	
	        mButton.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                //拥有一个presenter的实例，将一个Iview的实例传递过去
	                //
	                mIPresenter = new PresenterImp().attach(MainActivity.this);
	                mIPresenter.getData();
	            }
	        });
	    }
	
	    @Override
	    public void showDowning() {
	        Toast.makeText(this, "下载中....", Toast.LENGTH_SHORT).show();
	    }

	    @Override
	    public void showData(String data) {
	        mTextView.setText(data);
	    }
	
	    @Override
	    protected void onDestroy() {
	        super.onDestroy();
	        ((PresenterImp) mIPresenter).detach();
	    }
	}
在presenter中也稍微加了一些方法  
	
	public PresenterImp attach(IView iView) {
        mIViewWeakReference = new WeakReference<>(iView);
        return this;
    }

    public void detach(){
        if (null != mIViewWeakReference) {
            mIViewWeakReference.clear();
        }
    }
那么现在就是完美的吗？  
#### 3 ####
每一次写个activity的时候都要写这么多重复的代码，而且对于不一样的model和presenter每次也是包含着重复的代码，是否可以抽象出一个类来做一些重复的工作？    
新建了一个新的project，复制了文件过来，先看总的目录   

![](http://i.imgur.com/Bs33JLo.png)   
抽象出一个BaseActivity和一个BasePresenter  
BaseActivity：
	
	
	public abstract class BaseActivity extends AppCompatActivity implements IView {
	
	    protected IPresenter mIPresenter;
	
	    protected abstract IPresenter createPresenter();
	
	    @Override
	    protected void onCreate(@Nullable Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        mIPresenter = createPresenter();
	        mIPresenter.attach(this);
	    }
	
	    @Override
	    protected void onDestroy() {
	        super.onDestroy();
	        mIPresenter.detach();
	    }
	}
在这个抽象类中，定义了IPresenter成员变量，而至于创建一个怎么样的IPresenter，就交给了继承此抽象类的activity，由它决定  
并且在在这个抽象类中的onCreate方法和onDestroy方法中调用了IPresenter 的attach 和detach方法    

再看看现在的MainActivity  

	
	public class MainActivity extends BaseActivity {
	
	    private TextView mTextView;
	    private Button mButton;
	
	
	    @Override
	    protected IPresenter createPresenter() {
	//        return new WeatherPresenter();
	        return new JokePresenter();
	    }
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	        init();
	
	    }
	
	    private void init() {
	        mTextView = (TextView) this.findViewById(R.id.content_view);
	        mButton = (Button) this.findViewById(R.id.download_data_btn);
	
	        mButton.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                mIPresenter.getData();
	            }
	        });
	    }
	
	    @Override
	    public void showDowning() {
	        Toast.makeText(this, "下载中....", Toast.LENGTH_SHORT).show();
	    }
	
	    @Override
	    public void showData(String data) {
	        mTextView.setText(data);
	    }
	
	}
BasePresenter:  
	
	
	public abstract class BasePresenter<T> implements IPresenter {
	
	
	    protected WeakReference<IView> mIViewWeakReference;
	    protected IModel<T> mIModel;
	
	    public BasePresenter() {
	        mIModel = createModel();
	    }
	
	    protected abstract IModel createModel();
	
	    @Override
	    public void attach(IView iView) {
	        mIViewWeakReference = new WeakReference<IView>(iView);
	    }
	
	    @Override
	    public void detach() {
	        if (null != mIViewWeakReference) {
	            mIViewWeakReference.clear();
	        }
	    }
	}
在这个抽象类中依旧拥有IView的弱引用和Imodel的实例，而对于IModel具体是哪个也交给了抽象方法createModel，由子类自己具体实现。   

至于泛型T，则是需要根据下载数据的java bean ，像目录结构图中的WeatherPresenter和JokePresenter，对应着不一样的Presenter，那么由model层返回的也就是对应的bean  

attach和detach就是用于关联IView和解绑IView  

现在的WeatherPresenter：  

	
	public class WeatherPresenter extends BasePresenter<WeatherData> {
	
	    @Override
	    protected IModel createModel() {
	        return new WeatherModel();
	    }
	
	    @Override
	    public void getData() {
	
	        IView iView = mIViewWeakReference.get();
	        if (null != iView) {
	            //提示用户下载开始了
	            iView.showDowning();
	        }
	
	        //开始下载
	        mIModel.loadData(new LoadDataListener<WeatherData>() {
	
	            @Override
	            public void complete(final WeatherData data) {
	                IView iView = mIViewWeakReference.get();
	
	                if (null != iView) {
	
	                    ((Activity) iView).runOnUiThread(new Runnable() {
	                        @Override
	                        public void run() {
	
	                            IView iView = mIViewWeakReference.get();
	                            if (null != iView) {
	
	                                iView.showData(data.getStatus());
	                            }
	                        }
	                    });
	                }
	            }
	
	
	        });
	
	    }
	}
Imodel则是加上了泛型   
	

	public interface IModel<T> {
	
	    void loadData(LoadDataListener<T> loadDataListener);
	
	
	    interface LoadDataListener<T> {
	
	        void complete(T data);
	
	    }
	
	
	}
对应的WeatherModel   
	
	
	public class WeatherModel<T> implements IModel<T> {
	
	    private static final String TAG = "WeatherModel";
	    private final String URL = "http://tj.nineton.cn/Heart/index/future24h/?city=CHSH000000&language=zh-chs&key=36bdd59658111bc23ff2bf9aaf6e345c";
	    
	    @Override
	    public void loadData(final IModel.LoadDataListener loadDataListener) {
	
	        new Thread(new Runnable() {
	            @Override
	            public void run() {
	
	                try {
	                    URL url = new URL(URL);
	
	                    URLConnection urlConnection = url.openConnection();
	                    InputStream inputStream = urlConnection.getInputStream();
	
	                    StringBuilder builder = new StringBuilder();
	
	                    byte[] bytes = new byte[1024];
	                    int volumn = -1;
	                    while ((volumn = inputStream.read(bytes)) != -1) {
	
	                        builder.append(new String(bytes, 0, volumn));
	                    }
	
	                    String s = builder.toString();
	
	                    Log.d(TAG, "run: " + s);
	
	                    Gson gson = new Gson();
	
	                    WeatherData weatherData = gson.fromJson(s, WeatherData.class);
	
	                    inputStream.close();
	
	                    loadDataListener.complete(weatherData);
	                } catch (MalformedURLException e) {
	                    e.printStackTrace();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }).start();
	    }
	}

------

![](http://i.imgur.com/61vkSBg.png)   


---
MVP1地址：[https://github.com/lijinxiong/MVP1](https://github.com/lijinxiong/MVP1)    
MVP2地址：[https://github.com/lijinxiong/MVP2](https://github.com/lijinxiong/MVP2)   



-----