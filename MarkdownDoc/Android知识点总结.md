<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Retrofit框架的源码以及原理](#retrofit%E6%A1%86%E6%9E%B6%E7%9A%84%E6%BA%90%E7%A0%81%E4%BB%A5%E5%8F%8A%E5%8E%9F%E7%90%86)
- [MVC,MVP和MVVM三种架构的区别和优点](#mvcmvp%E5%92%8Cmvvm%E4%B8%89%E7%A7%8D%E6%9E%B6%E6%9E%84%E7%9A%84%E5%8C%BA%E5%88%AB%E5%92%8C%E4%BC%98%E7%82%B9)
- [什么情况下会导致内存泄露](#%E4%BB%80%E4%B9%88%E6%83%85%E5%86%B5%E4%B8%8B%E4%BC%9A%E5%AF%BC%E8%87%B4%E5%86%85%E5%AD%98%E6%B3%84%E9%9C%B2)
- [AsyncTask的工作原理](#asynctask%E7%9A%84%E5%B7%A5%E4%BD%9C%E5%8E%9F%E7%90%86)
- [Android多进程通信](#android%E5%A4%9A%E8%BF%9B%E7%A8%8B%E9%80%9A%E4%BF%A1)
- [Handler 消息机制](#handler-%E6%B6%88%E6%81%AF%E6%9C%BA%E5%88%B6)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->


### Retrofit框架的源码以及原理

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

### MVC,MVP和MVVM三种架构的区别和优点

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
        - 对于简单的应用会稍显麻烦,各式各样的接口与概念,使得整个应用充斥着零散的接口,
        但对于复杂的应用,能够很好的组织应用结构,使得应用变得灵活.
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
    
### 什么情况下会导致内存泄露

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
    
### AsyncTask的工作原理

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
  
- 线程池
    - 好处：
        1. 重用线程池中的线程，避免线程的创建和销毁所带来的性能开销
        2. 能有效控制线程池中的最大并发数，避免大量线程之间相互抢占系统资源而导致的阻塞现象
        3. 能够对线程进行简单的管理，提供如定时执行，制定间隔循环等功能
    - 起源：Android中的线程池概念源于java中的 Executor，具体实现是 ThreadPoolExecutor，
    主要分为4类，可通过Executors(注意是s)的工厂方法获得；
        - Executors.newFixedThreadPool():只有核心线程且没有超时机制，线程不会回收，可以快速相应外界请求，且任务队列大小没有限制
        - Executors.newCachedThreadPool():只有非核心线程，最大线程数为Integer.MAX_VALUE，超时时长60s,且任务队列无法存储元素，适合执行大量耗时较少的任务
        - Executors.newScheduledThreadPool():核心线程数固定，非核心数不限，且非核心线程闲置时会立即回收，主要用于执行定时任务和具有固定周期的重复任务
        - Executors.newSingleThreadExecutor():只有一个核心线程，统一所有的外界任务到一个线程中，顺序执行，这使得这些任务之间不需要处理线程同步的问题
    - 重要参数
        - corePoolSize：线程池的核心线程数，默认情况下，核心线程会在线程池中一直存活，即使他们处于闲置状态；
        但是若将allowsCoreThreadTimeOut=true，那么闲置的核心线程在等待新任务时会有超时策略，时长由keepAliveTime
        控制，超时后核心线程会被终止
        - maximumPoolSize：线程池所能容纳的最大线程数，当活动线程数达到这个数值后，后续的新任务将被阻塞
        ````
        如AsyncTask的线程池中
        CORE_POOL_SIZE = **Math.max(2, Math.min(CPU_COUNT - 1, 4));**
        MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
        KEEP_ALIVE_SECONDS = 30;
        BlockingQueue<Runnable> sPoolWorkQueue =
                    new LinkedBlockingQueue<Runnable>(128);//任务队列size
        ````
    — 执行任务的大致规则：
        1. 若线程池中的线程数量未达到核心线程数，则直接启动一个核心线程执行任务
        2. 若已达到，则任务被插入到任务队列中排队等待执行
        3. 若无法插入到任务队列，往往是由于任务队列已满，若此时线程数量未达到最大线程数，则立即启动一个非核心线程来执行任务
        4. 若已达到，则拒绝执行此任务
        

### Android多进程通信

- IPC: Inter Process Communication, 进程间通信(进程之间进行数据交换)

- 多进程定义: 正常情况下, 在Android中多进程是指一个应用中存在多个进程的情况

- 多进程实现: 除了NDK的fork外,只有一种方法, 在AndroidManifest.xml中声明四大组件时,用android:process属性指定.
    (也就是无法给一个线程或一个实体类指定其运行时所在的进程)
    - 不指定 : 默认运行在主进程,进程名为包名(com.ljy.ljyutils)
    - android:process=":abc" : 运行在"包名:abc"的进程中(com.ljy.ljyutils:abc)
    - android:process="ljy.123.abc" : 运行在名为"ljy.123.abc"的进程中
    - 区别: 以':'开头的进程属于当前应用的私有进程; 而不以:开头的属于全局进程,其他应用通过ShareUID方式可以和它
    跑在同一个进程;
        - UID: android系统会为每个应用分配唯一的UID,具有相同UID的应用才能共享数据
        - ShareUID: AndroidManifest.xml根标签中android:sharedUserId="com.ljy.share" 
        - 两个应用有相同的shareUID并且签名相同才能跑在同一个进程,他们可以相互访问私有数据如data目录,组件信息等,
        如果在同一个进程还可以共享内存数据;
        
- 多进程存在的问题: Android会为每个进程都分配独立的虚拟机,不同虚拟机在内存上有不同的地址空间,
    这就导致不同虚拟机中访问同一个类的对象会产生多份副本,那么多进程就可能造成如下问题:
    - 静态成员,单例模式完全失效
    - 线程同步机制完全失效
    - SharedPreferences的可靠性下降(由于系统对其读写有一定的缓存策略,即在内存中有一份SharedPreferences文件的缓存,因此高并发的读写访问有很大几率会丢失数据)
    - Application会多次创建

- 序列化:
    - Serializable接口: java提供,空接口,开销大,大量的IO操作
    - Parcelable接口: android提供,通过intent,binder传递,效率要更高,我们常用的Bundle就是其实现类
    - 用处:
        - 数据持久化
        - 通过网络传输给其他客户端
        - 通过Intent和Binder传输数据时
        
- 跨进程通信的方式:
    - Intent+Bundle
        - 四大组件中的Activity,Service,Receiver都支持使用Bundle传递数据,所以当我们启动不同进程的组件时,就可以用Intent+bundle进行数据传递        
        
    - 共享文件和SharedPreferences
        - 两个进程通过读写同一个文件来交换数据
        - 对象可以通过序列化和反序列化进行读写
        - SharedPreferences本质就是读写文件
        
    - Binder: implements IBinder   
        - 基于Binder的: AIDL,Messenger,ContentProvider
        - 从Android Framework角度讲,Binder是ServiceManager链接各种Manager(ActivityManager,WindowManager等)和ManagerService的桥梁
        - 从Android 应用层来说,Binder是客户端和服务端进行通信的媒介(bindService)
        - Binder主要用在Service中,包括普通的Service,AIDL和Messenger
        - 普通的Service中的Binder不涉及进程间通信,没有触及到Binder的核心
        
    - AIDL: 
        - 服务端: 
            - 创建要操作的实体类，实现 Parcelable 接口
            - 新建aidl文件夹，在其中创建接口aidl文件以及实体类的映射aidl文件
            - Make project
            - 服务service中实现AidlInterface.Stub对象, 并在onBind方法中返回
        - 客户端:
            - copy服务端提供的aidl文件夹和实体类
            - Make project
            - ServiceConnection.onServiceConnected方法中调用AidlInterface.Stub.asInterface(IBinder)创建AidlInterface实例,并调用其方法
            
    - Messenger(信使,轻量级的IPC方案)
            - 通过它可以在不同进程间传递Message对象
            - 底层其实就是AIDL, 只是进一步进行了封装, 以方便使用,从下面这个构造方法就可以看出
            ````
            public Messenger(IBinder target) {
                mTarget = IMessenger.Stub.asInterface(target);
            }
            - 具体使用请看本项目中的MessengerService.java及其注册调用
        
    - ContentProvider
        - Android中提供的专门用于不同应用间进行数据共享的方式,天生就适合进程间通信
        - 底层实现同样也是Binder,而且比AIDL要简单
        - 需要注意的: CRUD操作,防止SQL注入,权限控制
        - 系统中预制了许多ContentProvider,如通讯录,日程表信息等
        - query,insert,delete,update是运行在Binder线程中的, 存在多线程并发访问,方法内部要做好线程同步
            - 例子BookProvider中采用了SQLite,并且只有一个SQLiteDatabase的链接, 所以可以正确的应对多线程情况,
            因为SQLiteDatabase内部对数据库的操作是有同步处理的
        - onCreate运行在main线程(UI线程),不能进行耗时操作
        
    - Socket 
        - 也称套接字,是网络通信中的概念
        - 分为 流式套接字 和 用户数据报套接字 两种, 分别对应于网络的传输控制层中的TCP和UDP协议
            - TCP: 面向连接的协议,提供稳定的双向通信功能,连接的建立需要经过三次握手才能完成,
                为了提供稳定的数据传输功能,其本身提供了超时重传机制
            - UDP: 无连接,提供不稳定的单向通信功能(也可以实现双向通信),具有更好的效率,但不能保证数据一定能
                正确传输,尤其是在网络拥塞的情况下
        - 具体使用参考TcpServerService.java && ProcessActivity.java
        
### Handler 消息机制

- Handler: android消息机制的上层接口,其运行需要底层的MessageQueue和Looper支撑

- MessageQueue: 消息队列,内部存储了一组消息,一队列的形式对外提供插入删除工作,
(其实主要是包含插入(enqueueMessage)和读取(next(无限循环的，如果没有消息，next会一直阻塞在这里))两个操作,读取操作本身会伴随着删除操作)     
但其内部实现是采用单链表(插入和删除上比较有优势)的数据结构而非真正的队列;

- Looper: MessageQueue只是存储消息,Looper以无限循环的形式去查找是否有新消息,有则处理,无则等待;
线程是默认没有Looper的,如果要在线程中船舰handler就要为线程创建Looper（通过Looper.prepare()创建）,而UI线程(ActivityThread)是个例外,其创建时就会初始化Looper
    - 注:Looper是运行在创建Handler所在的线程中的,这样一来handler中的业务逻辑就被切换到创建handler的线程中去执行了
    
- ThreadLocal: looper中使用到的数据存储类,可以在不同线程中互不干扰的存储并提供数据,通过它在指定的线程中存储数据,数据存储后只有
指定线程中可以获取到存储的数据,如某些数据以线程为作用域,且不同线程有不同的数据副本时,可以考虑采用threadLocal,
如Looper就是以线程为作用域,若不用ThreadLocal就要提供一个全局的哈希表供查找并封装一个LooperManager类
(在不同线程中访问同一个ThreadLocal的get方法所得到的值是不同的,set方法是不相影响的)

- Q: 系统为何不允许在子线程中访问UI?
    - 因为Android中的UI控件不是线程安全的,如果在多线程中并发访问可能导致ui控件处于不可预期的状态
    - 为什么不加锁机制?
        1. 锁机制会让UI访问的逻辑变得复杂
        2. 锁机制会降低UI的访问效率,因为锁机制会阻塞某些线程的执行
        - 介于以上2点,最简单且高效额方法就是采用单线程模型处理UI操作
        
### 插件化

- 动态加载技术/插件化技术，减轻应用的内存和cpu占用，实现热拔插，即在不发布新版本的情况下更新某些模块
- 要解决三个基础的问题：（具体实现可参考 PluginCoreLib）
    - 资源访问
        - 插件apk中的资源（即R.xxx），宿主apk是无法直接访问的
        - 解决方案：参照ContextImpl中的getAccess和getResources方法，自行实现资源加载方法；
        详见PluginActivity.java的loadResources();
    - Activity生命周期管理
        - 反射方式：利用反射，在代理Activity的生命周期方法中，调用插件Activity的生命周期方法（代码复杂+性能开销，不推荐使用）
        - 接口方式：
        - 使用Fragment代替Activity
    - ClassLoader的管理
- 插件一般是dex或经过特殊处理的apk；一般都要用到代理Activity，用以启动插件Activity   

### 性能优化

- 布局优化：
    - 单层布局：尽量选择LinearLayout或FrameLayout，而少用 RelativeLayout，应为RelativeLayout功能较复杂，更耗性能；
        但从程序扩展性的角度看，更倾向于RelativeLayout
    - 多层布局：布局较复杂时，RelativeLayout能够有效的减少布局层级
    - <include/>标签：实现布局文件的复用，如app自定义的TitleBar
        只支持 layout_xx 和id属性，当include和被包含布局的根标签都指定了id时，以include为准；指定layout_xx属性时，
        必须也要指定layout_width和layout_height，否则无法生效
    - <merge/>标签：在UI的结构优化中起着非常重要的作用，它可以删减多余的层级，优化UI。
        <merge/>多用于替换FrameLayout或者当一个布局包含另一个时，<merge/>标签消除视图层次结构中多余的视图组。
        例如你的主布局文件是垂直布局，引入了一个垂直布局的include，这是如果include布局使用的LinearLayout就没意义了，
        使用的话反而减慢你的UI表现。这时可以使用<merge/>标签优化。
    - <ViewStub/>标签：懒加载，不会影响UI初始化时的性能；
        各种不常用的布局，如进度条、显示错误消息等可以使用ViewStub标签，以减少内存使用量，加快渲染速度
    - 使用 style 来定义通用的属性，从而重复利用代码，减少代码量
    - 封装组合view实现view复用
    - 使用 LinearLayoutCompat 组件来实现线性布局元素之间的分割线，从而减少了使用View来实现分割线效果
    
- 绘制优化：
    - 在自定义View的onDraw方法中要避免进行大量操作
        - 不要创建新的布局对象，因为onDraw可能会被频繁调用
        - 不要做耗时任务，也尽量不要执行循环操作
        
- 内存泄漏：
    - 静态变量导致：
        - 如： private static Context mContext;
        - 又如：
            ````
                private static View view;
                
                private void initView(){
                    view=new View(this);
                }
            ````
            
    - 单例模式导致：单例对象持有当前activity的引用
        - 如xxx.getInstance().init(this) ,init方法需要传入当前上下文对象，最好这样写：
            xxx.getInstance().init(this.getApplicationContext())
            
    - 属性动画导致：当通过animator.setRepeatCount(ValueAnimator.INFINITE);设置为无限循环时
        应在onDestroy()方法中停止动画 animator.cancel()
        
    - 集合类导致：集合类如果仅仅有添加元素的方法，而没有相应的删除机制，导致内存被占用。 
        特别是如果这个集合类是全局性的变量
        
    - 匿名内部类或非静态内部类导致（非静态内部类持有外部类引用）
        - 当在activity中创建其非静态内部类的静态实例时，该实例的生命周期和应用一样长，而且持有外部类即activity的引用，
         导致activity的内存资源不能正常回收；（应当设为静态内部内或抽成独立类并用单例模式访问）
        - 当匿名内部类的实例被异步线程持有时，如常见的Runable r=new Runable(){...}
        - handler:若handler发送的message未被处理，则该message和发送它的handler将被线程messageQueue一直持有；
            那么如果时直接new Handler(){...},会默认持有activity的引用，导致activity无法回收；
            所以应该使用静态的private static MyHandler extends Handler ；如果需要引入当前activity，
            那么使用弱引用的方式:
            ````
            private static class MyHandler extends Handler {
                private final WeakReference<Activity> mInstance;
            
                public MyHandler(Activity activity) {
                    mInstance = new WeakReference<>(activity);
                }
            
                @Override
                public void handleMessage(Message msg) {
                    Activity activity = mInstance==null?null:mInstance.get();
                    if (activity == null||activity.isFinishing()) {
                        return;
                    }
                    ...
                }
            }
            ````
            如上就可以成功的避免activity泄漏，但looper线程的消息队列还是持有未处理的消息，
            so,退出activity时还是应该移除未处理的消息
            ````
            onDestroy(){
                handler.removeCallbacks/removeMessages/removeCallbacksAndMessages
            }
            ````
            
    - 资源未关闭造成的内存泄漏
        - 对于使用了BraodcastReceiver，ContentObserver，File，游标 Cursor，Stream，Bitmap等资源的使用，
            应该在Activity销毁时及时关闭或者注销，否则这些资源将不会被回收，造成内存泄漏。
            
    - 使用Android系统服务不当容易导致
    ````
        //如：为了Activity与服务交互，我们把Activity作为监听器
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ALL);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        //应在onDestroy中：
        sensorManager.unregisterListener(this, sensor);
        
        
    ````
  
    - 尽量避免使用static成员变量
        - 其生命周期将与整个app进程生命周期一样。 这会导致一系列问题，如果你的app进程设计上是长驻内存的，
        那即使app切到后台，这部分内存也不会被释放；按照现在手机app内存管理机制，占内存较大的后台进程将优先回收    
        - 修复的方法是：
            不要在类初始时初始化静态成员。可以考虑lazy初始化（懒加载）。
            架构设计上要思考是否真的有必要这样做，尽量避免。
            如果架构需要这么设计，那么此对象的生命周期你有责任管理起来。

  ### Fragment
  - 目的：解决不同屏幕分辨率的动态灵活的UI设计
  - 优点：
    - 将Activity分离成多个可重用的组件，每个都有自己的生命周期和UI
    - 创建动态灵活的UI设计，适应于不同的屏幕尺寸
    - 与Activity绑定，可以动态的移除，加入，交换
    - 轻量切换，解决Activity间的切换不流畅
    - 替代TabActivity做导航，性能更好
    - 可以嵌套使用，生成更好的界面效果
    - 布局内容更新更方便，可以懒加载，提高性能
  - 生命周期(结合Activity的生命周期)：
    - [Act.onCreate()]-->onAttach()-->onCreate()-->onCreateView()-->onActivityCreated()-->
    - [Act.onStart()]-->onStart()
    - [Act.onResume()]-->onResume()  
    - onPause()-->[Act.onPause()]
    - onStop()-->[Act.onStop()]
    - onDestroyView()-->onDestroy()--onDetach()-->[Act.onDestroy()]
  - Fragment间信息交互：
    - 方式1：取得对象
    ````
    //点击该Fragment的button按钮，将该button的text设置为另一个fragment中Edittext的文本值
    button.setOnClickListener(new View.OnClickListener() { 
        @Override 
        public void onClick(View v) { 
            //通过FragmentManager找到另一个fragment中的edittext对象，并取得text内容 
            EditText editText = (EditText)(getFragmentManager().findFragmentByTag("left").getView().findViewById(R.id.name)); 
            button.setText(editText.getText().toString()); 
        } 
    }); 
    ```` 
    - 方式2：通过回调函数  
    ````
    button.setOnClickListener(new View.OnClickListener() { 
        @Override 
        public void onClick(View v) { 
            RightFragment rightFrag = (RightFragment) (getFragmentManager().findFragmentByTag("right")); 
            //通过set方法，向其传递一个实例化对象，由于rightFrag.set()方法内部执行RightFragment.CallBack.get()方法，完成了参数的传递
            rightFrag.set(new RightFragment.CallBack() { 
                @Override 
                public void get(String str) { 
                    button.setText(str); 
                } 
            }); 
        } 
    }); 
    
    //RightFragment
    public class RightFragment extends ListFragment { 
        public void set(CallBack callBack) { 
            EditText editText = (EditText) getView().findViewById(R.id.name); 
            callBack.get(editText.getText().toString()); 
        } 
    
        interface CallBack { 
            public void get(String str); 
        } 
    }
    ```` 
  - 与Activity信息交互
    - 方式1：通过getActivity() 方法来获得Activity的实例，如：View listView = getActivity().findViewById(R.id.list);
    但是注意调用getActivity()时，fragment必须和activity关联（attached to an activity），否则将会返回一个null。
    - 方式2：通过回调函数
    
### Activity

- Activity的startActivity和Context的startActivity区别
    1. 从Activity中启动新的Activity时可以直接mContext.startActivity(intent)就好;
    2. 如果从其他Context中启动Activity则必须给intent设置Flag: intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    
- Android应用程序/Activity启动过程  
    Activity启动有两种情况，第一种是从桌面launcher上点击相应的应用图标，第二种是在activity中通过调用startActivity来启动一个新的activity。
    我们创建一个新的项目，默认的根activity都是MainActivity，而所有的activity都是保存在堆栈中的，我们启动一个新的activity就会放在上一个activity上面，
    而我们从桌面点击应用图标的时候，由于launcher本身也是一个应用，当我们点击图标的时候，系统就会调用startActivitySately(),
    一般情况下，我们所启动的activity的相关信息都会保存在intent中，比如action，category等等。我们在安装这个应用的时候，
    系统也会启动一个PackaManagerService的管理服务，这个管理服务会对AndroidManifest.xml文件进行解析，从而得到应用程序中的相关信息，
    比如service，activity，Broadcast等等，然后获得相关组件的信息。当我们点击应用图标的时候，就会调用startActivitySately()方法，
    而这个方法内部则是调用startActivty(),而startActivity()方法最终还是会调用startActivityForResult()这个方法。
    而在startActivityForResult()方法是有返回结果的，所以系统就直接给一个-1，就表示不需要结果返回了。
    而startActivityForResult()这个方法实际是通过Instrumentation类中的execStartActivity()方法来启动activity，
    Instrumentation这个类主要作用就是监控程序和系统之间的交互。而在这个execStartActivity()方法中会获取ActivityManagerService的代理对象，
    通过这个代理对象进行启动activity。启动会就会调用一个checkStartActivityResult()方法，如果说没有在配置清单中配置有这个组件，
    就会在这个方法中抛出异常了。当然最后是调用的是Application.scheduleLaunchActivity()进行启动activity，
    而这个方法中通过获取得到一个ActivityClientRecord对象，而这个ActivityClientRecord通过handler来进行消息的发送，
    系统内部会将每一个activity组件使用ActivityClientRecord对象来进行描述，而ActivityClientRecord对象中保存有一个LoaderApk对象，
    通过这个对象调用handleLaunchActivity来启动activity组件，而页面的生命周期方法也就是在这个方法中进行调用。
     
    
- Activity任务栈/启动模式
    - 使用方式：
        - AndroidManifest.xml中给Activity标签设置android:launchMode="standard|singleInstance|singleTask|singleTop"
        - 给Intent设置Flag：intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|FLAG_ACTIVITY_NEW_TASK);
    - 分类：
        1. standard：默认模式：每次启动都会创建一个新的activity对象，放到目标任务栈中
        2. singleTop：判断当前的任务栈顶是否存在相同的activity对象，如果存在，则直接使用，如果不存在，那么创建新的activity对象放入栈中
        3. singleTask：在任务栈中会判断是否存在相同的activity，如果存在，那么会清除该activity之上的其他activity对象显示，如果不存在，则会创建一个新的activity放入栈顶
        4. singleInstance：会在一个新的任务栈中创建activity，并且该任务栈种只允许存在一个activity实例，其他调用该activity的组件会直接使用该任务栈种的activity对象
    
### Service
- Service生命周期？
  - service有两种启动方式，startService()和bindService()，不同的启动方式他们的生命周期是不一样；
  - startService() --> onCreate()--> onStartCommand()--> onDestroy()。
    这种方式启动的话，需要注意几个问题:
        1. 当我们通过startService被调用以后，多次在调用startService(),onCreate()方法也只会被调用一次，
           而onStartCommand()会被多次调用；当我们调用stopService()的时候，onDestroy()就会被调用，从而销毁服务。
        2. 当我们通过startService启动时候，通过intent传值，在onStartCommand()方法中获取值的时候，一定要先判断intent是否为null。
  - bindService()-->onCreate()-->onBind()-->unBind()-->onDestroy()
    bindService 这种方式进行启动service好处是更加便利activity中操作service，
    比如加入service中有几个方法，a,b ，如果要在activity中调用，在需要在activity获取ServiceConnection对象，
    通过ServiceConnection来获取service中内部类的类对象，然后通过这个类对象就可以调用类中的方法，当然这个类需要继承Binder对象.
        
### Broadcast
- 注册方式
    - 第一种是静态注册，也可成为常驻型广播，这种广播需要在AndroidManifest.xml中进行注册，这中方式注册的广播，
    不受页面生命周期的影响，即使退出了页面，也可以收到广播; 
    这种广播一般用于想开机自启动啊等等，由于这种注册的方式的广播是常驻型广播，所以会占用CPU的资源。
     
    - 第二种是动态注册，是在代码中注册的，这种注册方式也叫非常驻型广播，收到生命周期的影响，退出页面后，就不会收到广播，
    我们通常运用在更新UI方面。这种注册方式优先级较高。最后需要解绑，否会会内存泄露
- 广播是分为有序广播和无序广播。

### JVM、Dalvik以及ART

- JVM: 基于栈,基于栈的机器必须使用指令来载入和操作栈上数据，所需指令更多更多。
       运行的是java字节码。（java类会被编译成一个或多个字节码.class文件）
- Dalvik: Google公司自己设计用于Android平台的Java虚拟机。dex格式是专为Dalvik应用设计的一种压缩格式，
    适合于内存和处理器速度有限的系统。Dalvik允许同时运行多个虚拟机的实例，并且每一个应用作为独立的Linux进程执行。
    独立的进程可以防止在虚拟机崩溃的时候所有程序都被关闭;
    基于寄存器的Dalvik实现虽然牺牲了一些平台无关性，但是它在代码的执行效率上要更胜一筹。    
    
- ART: 
    - 在Dalvik下，应用每次运行都需要通过即时编译器（JIT）将字节码转换为机器码，即每次都要编译加运行，
      这一机制并不高效(拖慢应用以后每次启动的效率)，但让应用安装比较快，而且更容易在不同硬件和架构上运行。
      
    - ART完全改变了这种做法，在应用安装时就预编译字节码到机器语言，在移除解释代码这一过程后，应用程序执行将更有效率，启动更快。    
      ART占用空间比Dalvik大（字节码变为机器码之后，可能会增加10%-20%），这也是著名的“空间换时间大法"。
      预编译也可以明显改善电池续航，因为应用程序每次运行时不用重复编译了，从而减少了 CPU 的使用频率，降低了能耗。 
     
### 进程保活（不死进程）
- 黑色保活：不同的app进程，用广播相互唤醒（包括利用系统提供的广播进行唤醒）,举个3个比较常见的场景：
    场景1：开机，网络切换、拍照、拍视频时候，利用系统产生的广播唤醒app
    场景2：接入第三方SDK也会唤醒相应的app进程，如微信sdk会唤醒微信，支付宝sdk会唤醒支付宝。由此发散开去，就会直接触发了下面的 场景3
    场景3：假如你手机里装了支付宝、淘宝、天猫、UC等阿里系的app，那么你打开任意一个阿里系的app后，有可能就顺便把其他阿里系的app给唤醒了。

- 白色保活：启动前台Service
    调用系统api启动一个前台的Service进程，这样会在系统的通知栏生成一个Notification，用来让用户知道有这样一个app在运行着，
    哪怕当前的app退到了后台。如QQ音乐等音乐播放器就是这样;
    
- 灰色保活：利用系统的漏洞启动前台Service，区别在于它不会在系统通知栏处出现一个Notification
    思路一：API < 18，启动前台Service时直接传入new Notification()；
    思路二：API >= 18，同时启动两个id相同的前台Service，然后再将后启动的Service做stop处理     
    
### Universal-ImageLoader，Picasso，Fresco，Glide对比    
- ImageLoader：
    - 可能由于HttpClient被Google放弃，作者就放弃维护这个框架
    - 优点：
        1. 支持下载进度监听
        2. 可以在 View 滚动中暂停图片加载，通过 PauseOnScrollListener 接口可以在 View 滚动中暂停图片加载。
        3. 默认实现多种内存缓存算法 这几个图片框架都可以配置缓存算法，不过 ImageLoader 默认实现了较多缓存算法，
           如 Size 最大先删除、使用最少先删除、最近最少使用、先进先删除、时间最长先删除等。
        4. 支持本地缓存文件名规则定义
- Picasso 
    - 优点
        1. 自带统计监控功能。支持图片缓存使用的监控，包括缓存命中率、已使用内存大小、节省的流量等。
        2. 支持优先级处理。每次任务调度前会选择优先级高的任务，比如 App 页面中 Banner 的优先级高于 Icon 时就很适用。
        3. 支持延迟到图片尺寸计算完成加载
        4. 支持飞行模式、并发线程数根据网络类型而变。 手机切换到飞行模式或网络类型变换时会自动调整线程池最大并发数，
           比如 wifi 最大并发为 4，4g 为 3，3g 为 2。这里是根据网络类型来决定最大并发数，而不是 CPU 核数。
        5. “无”本地缓存。无”本地缓存，不是说没有本地缓存，而是 Picasso 自己没有实现，交给了 Square 的另外一个网络库
           okHttp 去实现，这样的好处是可以通过请求 Response Header 中的 Cache-Control 及 Expired 控制图片的过期时间。        
- Glide
    - 优点
        1. 不仅仅可以进行图片缓存还可以缓存媒体文件，它支持 Gif、WebP、缩略图。甚至是 Video，所以更该当做一个媒体缓存。
        2. 支持优先级处理。
        3. 与 Activity/Fragment 生命周期一致，支持 trimMemory。Glide对每个context都保持一个RequestManager，
           通过 FragmentTransaction 保持与 Activity/Fragment 生命周期一致，并且有对应的 trimMemory 接口实现可供调用。
        4. 支持 okHttp、Volley。Glide 默认通过 UrlConnection 获取数据，可以配合 okHttp 或是 Volley 使用。
           实际 ImageLoader、Picasso 也都支持 okHttp、Volley。
        5. 内存友好。Glide 的内存缓存有个 active 的设计，从内存缓存中取数据时，不像一般的实现用 get，而是用 remove，
           再将这个缓存数据放到一个 value 为软引用的 activeResources map 中，并计数引用数，在图片加载完成后进行判断，
           如果引用计数为空则回收掉。内存缓存更小图片，Glide 以 url、view_width、view_height、屏幕的分辨率等
           做为联合 key，将处理后的图片缓存在内存缓存中，而不是原始图片以节省大小。图片默认使用默认 RGB_565 而不是 
           ARGB_888，虽然清晰度差些，但图片更小，也可配置到 ARGB_888。
        6. Glide 可以通过 signature 或不使用本地缓存支持 url 过期           
- Fresco 
    Facebook 推出的开源图片缓存工具，主要特点包括：两个内存缓存加上 Native 缓存构成了三级缓存，
    - 优点：
        1. 图片存储在安卓系统的匿名共享内存, 而不是虚拟机的堆内存中, 图片的中间缓冲数据也存放在本地堆内存, 所以, 
           应用程序有更多的内存使用, 不会因为图片加载而导致oom, 同时也减少垃圾回收器频繁调用回收 Bitmap 导致的界面卡顿, 性能更高。
        2. 渐进式加载 JPEG 图片, 支持图片从模糊到清晰加载。
        3. 图片可以以任意的中心点显示在 ImageView, 而不仅仅是图片的中心。
        4. JPEG 图片改变大小也是在 native 进行的, 不是在虚拟机的堆内存, 同样减少 OOM。
        5. 很好的支持 GIF 图片的显示。
    - 缺点:
        1. 框架较大, 影响 Apk 体积
        2. 使用较繁琐        
        
### Xutils, OKhttp, Volley, Retrofit对比
- xUtils: 
    这个框架非常全面，可以进行网络请求，可以进行图片加载处理，可以数据储存，还可以对view进行注解，使用这个框架非常方便，
    但是缺点也是非常明显的，使用这个项目，会导致项目对这个框架依赖非常的严重，一旦这个框架出现问题，那么对项目来说影响非常大的。
- okHttp：
    okHttp针对Java和Android程序，封装的一个高性能的http请求库，支持同步，异步，而且封装了线程池，封装了数据转换，
    封装了参数的使用，错误处理等。API使用起来更加的方便。但是我们在项目中使用的时候仍然需要自己在做一层封装，这样才能使用的更加的顺手。
    - OkHttp的优势在于性能更高
- Volley：
    Volley是Google官方出的一套小而巧的异步请求库，该框架封装的扩展性很强，支持HttpClient、HttpUrlConnection， 
    甚至支持OkHttp，而且Volley里面也封装了ImageLoader，所以如果你愿意你甚至不需要使用图片加载框架，不过这块功能
    没有一些专门的图片加载框架强大，对于简单的需求可以使用，稍复杂点的需求还是需要用到专门的图片加载框架。
    Volley也有缺陷，比如不支持post大数据，所以不适合上传文件。不过Volley设计的初衷本身也就是为频繁的、数据量小的网络请求而生。
    - 优势在于封装的更好
- Retrofit：
    Retrofit是Square公司出品的默认基于OkHttp封装的一套RESTful网络请求框架。Retrofit的封装可以说是很强大，
    里面涉及到一堆的设计模式,可以通过注解直接配置请求，可以使用不同的http客户端，虽然默认是用http ，可以使用
    不同Json Converter 来序列化数据，同时提供对RxJava的支持，使用Retrofit + OkHttp + RxJava + Dagger2 
    可以说是目前比较潮的一套框架，但是需要有比较高的门槛。
    - Retrofit解耦的更彻底
    - 默认使用OkHttp,性能上也要比Volley占优势
        
### Http https区别
1、https协议需要到ca申请证书，一般免费证书较少，因而需要一定费用。
2、http是超文本传输协议，信息是明文传输，https则是具有安全性的ssl加密传输协议。
3、http和https使用的是完全不同的连接方式，用的端口也不一样，前者是80，后者是443。
4、http的连接很简单，是无状态的；HTTPS协议是由SSL+HTTP协议构建的可进行加密传输、身份认证的网络协议，比http协议安全。
 
- https实现原理：
（1）客户使用https的URL访问Web服务器，要求与Web服务器建立SSL连接。
（2）Web服务器收到客户端请求后，会将网站的证书信息（证书中包含公钥）传送一份给客户端。
（3）客户端的浏览器与Web服务器开始协商SSL连接的安全等级，也就是信息加密的等级。
（4）客户端的浏览器根据双方同意的安全等级，建立会话密钥，然后利用网站的公钥将会话密钥加密，并传送给网站。
（5）Web服务器利用自己的私钥解密出会话密钥。
（6）Web服务器利用会话密钥加密与客户端之间的通信。
 
### Android5.0，6.0，7.0，8.0 新特性
- 5.0:
    - 开启扁平化时代，使用新的MaterialDesign设计风格
    - 全新的通知中心
    - 放弃了之前一直使用的Dalvik虚拟机，改用ART模式
    - 增加BatterySaver模式来进行省电处理
    - RecyclerView，CardView，Toolbar，FloatingActionButton(悬浮按钮),SnackBar,AppBarLayout,TabLayout
    - 三种Notification：普通，折叠，悬挂

- 6.0:
    - 为每位用户的每一个应用提供了两套数据存储方案:工作资料和个人信息    
    - 指纹识别
    - 运行时权限
    - App Links(应用唤起)
    - Android Pay
    - App Standby（应用待机）、Doze（瞌睡）Exemptions（豁免）等模式来加强电源管理
    
- 7.0:
    - 对文件数据加密，更加安全     
    - 分屏多任务(多窗口模式)
    - 加入了JIT编译器，安装程序快了75%，所占空间减少了50%。
    - 全新下拉快捷开关页
    - 通知消息快捷回复，通知消息归拢
    
- 8.0:
    - 通知渠道 — Notification Channels
      引入通知渠道，提高用户体验，方便用户管理通知信息。    
    - 画中画模式
    - 自动填充框架
    - 指纹手势
- 9.0:
    - 屏幕缺口支持
    - 多摄像头支持和摄像头更新      

         