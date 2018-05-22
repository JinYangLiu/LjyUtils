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
- 插件一般是dex或警告特殊处理的apk，一般都要用到代理Activity，用以启动插件Activity   
    