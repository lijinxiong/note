![](http://i.imgur.com/NyI6fuy.jpg)     

--------
不知道大家的手机上有没有这个功能     

![](http://i.imgur.com/geU2WoL.jpg)    
第一个功能双击点亮屏幕，对于android的屏幕而言，它能感应到我们手指的触摸。那么对于我们来说，Android的这个触摸事件是怎么样的，从Activity到我们点击的那个View,究竟发什么哪些不为人知的故事?    
## 分析 ##
![](http://i.imgur.com/gqwWu5q.png)     

对于这图有不理解的可以看看[我的这篇文章](http://www.jianshu.com/p/ab497bd4da20)。  
传闻所有的View都有**dispatchTouchEvent**和**onTouchEvent**这两个方法，而且ViewGroup还有个**onInterceptTouchEvent**这个方法。那么我们先尝试在**activity**中找找这些方法，看看有木有    
### Activity ###
在Activity中搜索到dispatchtTouchEvent方法   
	

	public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            onUserInteraction();
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }
这个函数是由系统调用的，屏幕感应到手指的触摸，告诉系统，系统把这个触摸事件传递个当前运行的activity   
- onUserInteraction是一个空方法，作用就是帮助activity管理下状态栏的通知，以及帮助Activity决定在什么合适的时间取消那个显示的通知   
- 在最后一个语句中我们发现了**onTouchEvent**了  
- 关键就在第二句的语句，**getWindow().superDispatchTouchEvent(ev)**，我们知道在activity中window的实例就是PhoneWindow([飞机票](http://www.jianshu.com/p/ab497bd4da20))，所以我们就去PhoneWindow中看看**superDispatchTouchEvent**方法   
### PhoneWindow ###
	

	@Override
    public boolean superDispatchTouchEvent(MotionEvent event) {
        return mDecor.superDispatchTouchEvent(event);
    }
又飞去了DeocorView了   
### DeocorView ###


	public boolean superDispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }
我们知道DecorView是继承FrameLayout的，而FrameLayout是继承ViewGroup的，又到了ViewGroup了  
### ViewGroup ###
来到了传闻的ViewGroup了，本来不打算分析它的源码的，但是发现刚刚走进看源码的时候，居然看错了，很可恶，然后花了两个钟现在才看大概知道流程，所以先放事件处理的流程的图，免得看源码的时候直接晕死    

![](http://i.imgur.com/fsetm0o.png)   

ViewGroup直接不拦截事件，上层ViewGroup的dispatchTouchEvent调用View的dispatchTouchEvent，View调用onTouchEvent，返回的结果true的话，ViewGroup的onTouchEvent不再调用，ViewGroup直接将结果返回个上层。而View执行onTouchEvent结果是false的时候，ViewGroup的onTouchEvent会执行。就是这个流程   
#### 分析 ####
	
	
	//省略代码...（包含有每次down事件都会清除TouchTarget和一些TouchState，mFirstTouchTarget=null）

	TouchTarget newTouchTarget = null;//存放事件消费者(onTouchEvent返回true的View)
    boolean alreadyDispatchedToNewTouchTarget = false;//事件是否已经分配给view了
    if (!canceled && !intercepted) {

       //省略一部分不相干代码

            final int childrenCount = mChildrenCount;
			//进入if
            if (newTouchTarget == null && childrenCount != 0) {
                
                final View[] children = mChildren;//viewGroup拥有的子view
                for (int i = childrenCount - 1; i >= 0; i--) {
                    //确定子view是否能接受触摸事件的代码，省去

                    newTouchTarget = getTouchTarget(child);//down事件返回的是空
                    if (newTouchTarget != null) {//当事件不是down的时候，比如move，进入这里
                        
						//设置为非down事件，退出for循环
                        newTouchTarget.pointerIdBits |= idBitsToAssign;
                        break;
                    }
					//这里调用子view的dispatchTouchEvent，然后是onTouchEvent的返回结果在这
					//true代表子类已经消费了此次的触摸事件
                    if (dispatchTransformedTouchEvent(ev, false, child, idBitsToAssign)) {
                       
               			//存放事件消费者，而且mFirstTouchTarget和newTouchTarget指向同一个对象
                        newTouchTarget = addTouchTarget(child, idBitsToAssign);
                        alreadyDispatchedToNewTouchTarget = true;//设置事件已经分配
                        break;//退出for循环
                    }

                  
                }
                if (preorderedList != null) preorderedList.clear();
            }
			//down事件和move事件都不会进入这里
			//down事件是newTouchTarget 和 mFirstTouchTarget 都为null
			//而move事件是都不为null
            if (newTouchTarget == null && mFirstTouchTarget != null) {
                // Did not find a child to receive the event.
                // Assign the pointer to the least recently added target.
                newTouchTarget = mFirstTouchTarget;
                while (newTouchTarget.next != null) {
                    newTouchTarget = newTouchTarget.next;
                }
                newTouchTarget.pointerIdBits |= idBitsToAssign;
            }
        }
    }
	