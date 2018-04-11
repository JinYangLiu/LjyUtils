
## 目录

+ [Retrofit框架的源码以及原理](#1)
+ [MVC,MVP和MVVM三种架构的区别和优点](#2)
+ [什么情况下会导致内存泄露](#3)
+ [AsyncTask原理](#4)

#### <span id = "1">Retrofit框架的源码以及原理</span>

- 使用注解+java接口来定义后台服务API接口
- 使用步骤
    1. 定义请求接口
    2. 创建retrofit实例
    3. retrofit.create创建接口动态代理实例(Proxy.newProxyInstance),为接口的每个method创建一个对应的ServiceMethod,
    这个ServiceMethod会调用接口method的getAnnotations进行foreach遍历并用if else判断处理所有注解;
    然后使用这个ServiceMethod对象创建OkHttpCall对象,最后用serviceMethod的callAdapter调用OkHttpCall并返回结果.
    4. 调用对应的接口方法进行网络请求,并对返回结果进行处理
        1. Retrofit原始: call.enqueue(new Callback())
        2. Retrofit+RxJava: observable.subscribe(new Observer)
- 其中几个关键类的作用
    - Retorift：
        - 负责配置请求过程中的基本参数，如：请求地址，结果转换器，自定义OKHttpClient等，同时还会生成请求接口对象
    - Call： 
        - 网络请求执行者(Retrofit.Call)，如：调用login方法得到的Call对象
    - OkHttpCall：
        - 此类是Retrofit.Call接口的实现，调用login方法得到的Call对象就是此类的实例。
        但是其底层网络请求执行都是通过OkHttp.Call接口间接执行的，也就是说OkHttpCall是对OkHttp.Call网络请求功能的封装。
    - Converter & Converter.Factory： 
        - 分别负责网络请求结果转换以及生成Converter转换器(工厂模式)
    - CallAdapter & CallAdapter.Factory： 
        - 分别负责对Retrofit.Call实例(OkHttpCall)进行适配及生成CallAdapter适配器
    - Platform：
        - 确定Retrofit当前运行平台，以及确定当前平台默认的的CallAdapter.Factory和Executor
    - ExecutorCallAdapterFactory：
        - Android平台下的默认CallAdapter.Factory实现
    - ServiceMethod： 
        - 解析接口服务所有注解、生成请求对象Request、解析请求结果Response
    - ParameterHandler： 
        - 服务接口方法(login())参数解析处理器，配合ServiceMethod进行服务接口参数注解解析
    - RequestBuilder： 
        - 根据参数和URL构造请求需要的OkHttp.Request对象
- 设计模式:
    - 建造者模式 : 
        - Retrofit.Builder
        - 优点： 
            - 不需要知道产品内部的组成细节，产品与创建过程解耦
            - 分步组装产品，使得产品的创建过程更精细更清晰
            - 容易扩展，新产品只需要新建一个建造者即可
    - 工厂方法模式 : 
        - Converter.Factory , CallAdapter.Factory
        - 优点 
            - 只关注产品工厂即可，不需要关注产品如何创建，由工厂确定如何创建
            - 扩展性好，新增产品时，只需要新增一个具体工厂和具体产品
    - 适配器模式 : 
        - public CallAdapter<?, ?> ExecutorCallAdapterFactory.get(...){...}
        - 优点
            - 复用性好，引入适配器类来重用适配者类，无需修改原有代码
            - 增加类的透明性，将适配过程封装在适配器类中，对使用者来说相对透明
            - 灵活性扩展性好，通过配置可以随时更换适配器
    - 代理模式 :
        - 静态:
            - ExecutorCallbackCall代理OkHttpCall具体请求操作
        - 动态:
            - Retrofit.create(final Class<T> service) , OkHttpCall.enqueue() ,  OkHttpCall.execute()
        - 优点 
            - 协调调用者与被调用者，降低系统耦合度
            - 减小外部接口与内部接口实现的关联，降低耦合

#### <span id = "2">MVC,MVP和MVVM三种架构的区别和优点</span>

- MVC
    - View：对应于xml布局文件
    - Model：实体模型
    - Controllor：对应于Activity业务逻辑，数据处理和UI处理
    - 缺点: 由于xml文件功能太弱，Activity基本上是View和Controller的合体，
    既要负责视图的显示又要加入控制逻辑，承担的功能过多;
    而相对来说ios中的xib/storyboard与ViewController就要好的多了,因为xib的功能要强上一下
- MVP
    - View: 对应于Activity和xml，负责View的绘制以及与用户交互
    - Model: 实体模型
    - Presenter: 负责完成View于Model间的交互和业务逻辑
    - 缺点: 
        - Activity需要实现各种跟UI相关的接口，同时要在Activity中编写大量的事件，然后在事件处理中调用
        presenter的业务处理方法，View和Presenter只是互相持有引用并互相做回调,代码不美观。
        - 这种模式中，程序的主角是UI，通过UI事件的触发对数据进行处理，更新UI就有考虑线程的问题。
        而且UI改变后牵扯的逻辑耦合度太高，一旦控件更改（如TextView 替换 EditText等）牵扯的更新UI的接口就必须得换。
        - 复杂的业务同时会导致presenter层太大，代码臃肿的问题。
- MVVM
    - View: 对应于Activity和xml，负责View的绘制以及与用户交互
    - Model: 实体模型
    - ViewModel: 负责完成View于Model间的交互,负责业务逻辑     
    - 优点 : MVVM的目标和思想MVP类似，利用数据绑定(Data Binding)、依赖属性(Dependency Property)、
    命令(Command)、路由事件(Routed Event)等新特性，打造了一个更加灵活高效的架构。
- 三者差异
    - 在于如何粘合View和Model
    - Controller接收View的操作事件，根据事件不同，或者调用Model的接口进行数据操作，或者进行View的跳转，
    从而也意味着一个Controller可以对应多个View。Controller对View的实现不太关心，只会被动地接收，
    Model的数据变更不通过Controller直接通知View，通常View采用观察者模式监听Model的变化。
    - Presenter与Controller一样，接收View的命令，对Model进行操作；与Controller不同的是Presenter会
    反作用于View，Model的变更通知首先被Presenter获得，然后Presenter再去更新View。一个Presenter只对应于一个View。
    - ViewModel,注意这里的“Model”指的是View的Model，跟MVVM中的一个Model不是一回事。
    所谓View的Model就是包含View的一些数据属性和操作的这么一个东东，这种模式的关键技术就是
    数据绑定（data binding），View的变化会直接影响ViewModel，ViewModel的变化或者内容也会直接体现在View上。
    这种模式实际上是框架替应用开发者做了一些工作，开发者只需要较少的代码就能实现比较复杂的交互。
    
#### <span id = "3">什么情况下会导致内存泄露</span>

- 内存泄露的根本原因：
    - 长生命周期的对象持有短生命周期的对象的引用。短周期对象就无法及时释放。
1. 静态集合类引起内存泄露
    - 主要是HashMap，Vector等，如果是静态集合 这些集合没有及时set null的话，就会一直持有这些对象。
2. remove方法无法删除set集  
    - 经过测试，hashcode修改后，就没有办法remove了。
3. observer 
    - 我们在使用监听器的时候，往往是addXxxListener，但是当我们不需要的时候，忘记removeXxxListener，就容易内存leak。
    - 广播没有unRegisterReceiver
4. 各种数据链接没有关闭
    - 数据库,cursor,contentProvider,io,socket等
5. 内部类：
    - java中的内部类（匿名内部类），会持有宿主类的强引用this。
    - 所以如果是new Thread这种，后台线程的操作，当线程没有执行结束时，activity不会被回收。
    - Context的引用，当TextView 等等都会持有上下文的引用。如果有static drawable，就会导致该内存无法释放。
6. 单例
    - 单例是一个全局的静态对象，当持有某个复制的类A，A无法被释放，内存leak。
    
#### <span id = "4">AsyncTask的工作原理</span>

- 构造方法
    - public abstract class AsyncTask<Params, Progress, Result> 
        - Params：doInBackground方法的参数类型；
        - Progress：AsyncTask所执行的后台任务的进度类型；
        - Result：后台任务的返回结果类型

- 主要方法
    - onPreExecute() //此方法会在后台任务执行前被调用，用于进行一些准备工作
    - doInBackground(Params... params) //此方法中定义要执行的后台任务，在这个方法中可以调用
    publishProgress来更新任务进度（publishProgress内部会调用onProgressUpdate方法）
    - onProgressUpdate(Progress... values) //由publishProgress内部调用，表示任务进度更新
    - onPostExecute(Result result) //后台任务执行完毕后，此方法会被调用，参数即为后台任务的返回结果
    - onCancelled() //此方法会在后台任务被取消时被调用
    - 除了doInBackground方法由AsyncTask内部线程池执行外，其余方法均在主线程中执行。
    
- AsyncTask是对Handler与线程池的封装
    - AsyncTask有两个线程池：SerialExecutor 和 ThreadPoolExecutor;
        - 前者用于任务的排队，默认是串行的线程池, 后者用于真正的执行任务。
        - 排队执行过程:
            1. 系统先把参数Params封装为 FutureTask 对象，它相当于Thread 的 Runnable
            2. 接着FutureTask交给SerialExecutor的execute方法，它先把FutureTask插入到任务队列tasks中，
            如果这个时候没有正在活动的AsyncTask任务，那么就会执行下一个AsyncTask任务，同时当一个AsyncTask
            任务执行完毕之后，AsyncTask会继续执行其他任务直到所有任务都被执行为止。
        - 任务线程池
            - AsyncTask对应的线程池ThreadPoolExecutor都是进程范围内共享的，都是static的，
            所以是AsyncTask控制着进程范围内所有的子类实例。由于这个限制的存在，当使用默认线程池时，
            如果线程数超过线程池的最大容量，线程池就会爆掉(3.0默认串行执行，不会出现这个问题)。
            - 针对这种情况, 可以尝试自定义线程池，配合AsyncTask使用。
    - AsyncTask有一个 InternalHandler，用于将执行环境从线程池切换到主线程。
    AsyncTask内部就是通过InternalHandler来发送任务执行的进度以及执行结束等消息
    ````
    new InternalHandler(Looper.getMainLooper())
    //内部实现
    public void handleMessage(Message msg) {
        AsyncTaskResult<?> result = (AsyncTaskResult<?>) msg.obj;
        switch (msg.what) {
            case MESSAGE_POST_RESULT:
                // There is only one result
                result.mTask.finish(result.mData[0]);
                break;
            case MESSAGE_POST_PROGRESS:
                result.mTask.onProgressUpdate(result.mData);
                break;
        }
    }
    
- 局限性：
  - 在Android 4.1版本之前，AsyncTask类必须在主线程中加载，这意味着对AsyncTask类的第一次访问必须发生在主线程中；
  在Android 4.1以及以上版本则不存在这一限制，因为ActivityThread（代表了主线程）的main方法中会自动加载AsyncTask
  - AsyncTask对象必须在主线程中创建
  - AsyncTask对象的execute方法必须在主线程中调用
  - 一个AsyncTask对象只能调用一次execute方法   
