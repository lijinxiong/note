## 使用 ##



	 //1.8 lambda
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName());
        }).start();

        new Thread() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        }.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        }).start();


        new Thread() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "   run");
            }
        }.run();
## run 和start ##

- run 执行run方法，并没有创建一个新的线程   
- start 创建一个新的线程，并在新的线程执行run方法     
