package com.ljy.ljyutils.activity;

import android.app.KeyguardManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjyDatabaseUtil;
import com.ljy.util.LjyLogUtil;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 设计原则
 * 1. 单一职责原则
 * 一个类只有一个引起它变化的原因
 * 2. 开放封闭原则
 * 对扩展是开放的,对修改是封闭的
 * 3. 里氏替换原则
 * 子类替换父类后,程序的行为是一样的
 * 4. 依赖倒置原则
 * 细节依赖于抽象,面向接口编程,降低耦合度
 * 5. 接口隔离原则
 * 建立单一接口,尽量细化接口,提高内聚,减少对外交互
 * 6.迪米特法则 (最少知识原则)
 * 高内聚低耦合,尽量减少与其他实体间发生相互作用
 * <p>
 * 几乎所有设计模式的通病:类的膨胀,大量衍生类的创建
 * 好处:更弱的耦合性,更灵活的控制性,更好的扩展性
 */
public class DesignPatternActivity extends BaseActivity {

    @BindView(R.id.textViewShow)
    TextView mTextViewShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_pattern);
        ButterKnife.bind(mActivity);
    }

    public void onDesignBtnClick(View view) {
        mTextViewShow.append("----------" + ((Button) view).getText() + "----------\n");
        LjyLogUtil.setAppendLogMsg(true);
        switch (view.getId()) {
            case R.id.btnSingleton:
                //单例模式
                methodSingleton();
                break;
            case R.id.btnSimpleFactoryPattern:
                //简单工厂模式
                methodSimpleFactoryPattern();
                break;
            case R.id.btnFactoryMethod:
                //工厂方法模式
                methodFactoryMethod();
                break;
            case R.id.btnAbstractFactoryPattern:
                //抽象工厂模式
                methodAbstractFactoryPattern();
                break;
            case R.id.btnStrategyPattern:
                //策略模式
                methodStrategyPattern();
                break;
            case R.id.btnAdapterPattern:
                //适配器模式
                methodAdapterPattern();
                break;
            case R.id.btnProxyPattern:
                //代理模式
                methodProxyPattern();
                break;
            case R.id.btnTemplateMethodPattern:
                //模板方法模式
                methodTemplateMethodPattern();
                break;
            case R.id.btnBuilderPattern:
                //建造者模式
                methodBuilderPattern();
                break;
            case R.id.btnFacadePattern:
                //外观模式
                methodFacadePattern();
                break;
            case R.id.btnProtoPattern:
                //原型模式
                methodProtoPattern();
                break;
            case R.id.btnStatePattern:
                //状态模式
                methodStatePattern();
                break;
            case R.id.btnDutyChainPattern:
                //责任链模式
                methodDutyChainPattern();
                break;
            case R.id.btnInterpreterPattern:
                //解释器模式
                methodInterpreterPattern();
                break;
            case R.id.btnICommandPattern:
                //命令模式
                methodICommandPattern();
                break;
            case R.id.btnObserverPattern:
                //观察者模式
                methodObserverPattern();
                break;
            case R.id.btnMemoPattern:
                //备忘录模式
                methodMemoPattern();
                break;
            case R.id.btnIteratorPattern:
                //迭代器模式
                methodIteratorPattern();
                break;
            case R.id.btnVisitorPattern:
                //访问者模式
                methodVisitorPattern();
                break;
            case R.id.btnMediatorPattern:
                //中介者模式
                methodMediatorPattern();
                break;
        }
        mTextViewShow.append(LjyLogUtil.getAllLogMsg());
        LjyLogUtil.setAppendLogMsg(false);
    }

    /**
     * 中介者模式(调解者/调停者模式)
     * 定义: 包装了一系列对象相互作用的方式,使得这些对象不必相互明显作用,从而实现松耦合.
     * 当某些对象之间的作用发生改变时,不会影响其他对象,保证独立变化.
     * 将多对多的相互作用转化为一对多的相互作用,由复杂的网状结构变为以中介者为中心的星型结构
     *
     *
     */
    private void methodMediatorPattern() {
        //以电脑播放CD为例
        //1.构造主板对象
        MainBoard mainBoard=new MainBoard();
        //2.构造其他设备
        CDDevice cdDevice=new CDDevice(mainBoard);
        CPU cpu=new CPU(mainBoard);
        GraphicsCard graphicsCard=new GraphicsCard(mainBoard);
        SoundCard soundCard=new SoundCard(mainBoard);
        //3.将各个零部件安装到主板
        mainBoard.setCDDevice(cdDevice);
        mainBoard.setCPU(cpu);
        mainBoard.setGraphicsCard(graphicsCard);
        mainBoard.setSoundCard(soundCard);
        //4.光驱读取cd-->cpu解析数据-->声卡显卡播放音视频(主板做中介)
        cdDevice.load();

    }

    //抽象同事类
    abstract class Colleague{
        protected Mediator mMediator;//每个同事都该知道其中介者

        public Colleague(Mediator mediator) {
            mMediator = mediator;
        }
    }

    //负责从主板传递来的音视频数据的解码
    class CPU extends  Colleague{
        private String dataVideo,dataSound;//视频音频数据

        public CPU(Mediator mediator) {
            super(mediator);
        }

        public String getDataVideo() {
            return dataVideo;
        }

        public String getDataSound() {
            return dataSound;
        }

        public void decodeData(String data){
            //分割音视频数据
            String[] tmp=data.split(",");
            //解析以视频数据
            dataVideo=tmp[0];
            dataSound=tmp[1];
            //告诉中介者自身状态改变
            mMediator.changed(this);
        }
    }

    //光驱负责读取光盘的数据,并提供给主板
    class CDDevice extends Colleague{
        public String data;//视频数据

        public CDDevice(Mediator mediator) {
            super(mediator);
        }

        //读取视频数据
        public String read(){
            return data;
        }

        //加载视频数据
        public void load(){
            data="视频数据,音频数据";
            //通知中介者
            mMediator.changed(this);
        }
    }

    //显卡声卡负责播放视频和音频
    class GraphicsCard extends Colleague{

        public GraphicsCard(Mediator mediator) {
            super(mediator);
        }

        //播放视频
        public void videoPlay(String data){
            LjyLogUtil.i("videoPlay:"+data);
        }
    }

    class SoundCard extends Colleague{

        public SoundCard(Mediator mediator) {
            super(mediator);
        }

        public void soundPlay(String data){
            LjyLogUtil.i("soundPlay:"+data);
        }
    }

    //抽象的中介者
    abstract class Mediator{
        //同事对象改变时通知中介者的方法,由中介者通知其他对象
        public abstract void changed(Colleague c);
    }


    //中介者实现类:主板
    class MainBoard extends Mediator{
        private CDDevice mCDDevice;//光驱
        private CPU mCPU;//cpu
        private SoundCard mSoundCard;//声卡
        private GraphicsCard mGraphicsCard;//显卡

        @Override
        public void changed(Colleague c) {
            if (c==mCDDevice){
                //光驱读取完数据
                handleCD((CDDevice)c);
            }else if (c==mCPU){
                //cpu解析完数据
                handleCPU((CPU)c);
            }

        }

        //处理cpu读取数据后与其他设备的交互
        private void handleCPU(CPU cpu) {
            mSoundCard.soundPlay(cpu.getDataSound());
            mGraphicsCard.videoPlay(cpu.getDataVideo());
        }

        //处理光驱读取数据后与其他设备的交互
        private void handleCD(CDDevice cdDevice) {
            mCPU.decodeData(cdDevice.read());
        }

        public void setCDDevice(CDDevice CDDevice) {
            mCDDevice = CDDevice;
        }

        public void setCPU(CPU CPU) {
            mCPU = CPU;
        }

        public void setSoundCard(SoundCard soundCard) {
            mSoundCard = soundCard;
        }

        public void setGraphicsCard(GraphicsCard graphicsCard) {
            mGraphicsCard = graphicsCard;
        }
    }

    /**
     * 访问者模式:
     * 将数据操作与数据结构分离
     * 定义: 封装一些作用于某种数据结构中的各元素的操作,它可以在不改变
     * 这个数据结构的前提下定义作用于这些元素的新的操作
     * 使用场景:
     * 1. 对象结构比较稳定,但经常需要在此对象结构上定义新的操作
     * 2. 需要对一个对象结构中的对象进行很多不同的并且不相关的操作,
     * 而需要避免这些操作污染这些对象的类,也不希望在增加新操作时修改这些类
     * Android源码中的使用:
     * 编译期注解(依赖APT(Annotation Processing Tools)实现),
     * ButterKnife,Dagger,Retrofit等开源库都是基于APT
     */
    private void methodVisitorPattern() {
        //以员工绩效评定为例
        //1.构建报表
        BusinessReport report = new BusinessReport();
        LjyLogUtil.i("给CEO看的报表:");
        report.showReport(new CEOVisitor());
        LjyLogUtil.i("给CTO看的报表");
        report.showReport(new CTOVisitor());
    }


    public interface Visitor {
        //访问工程师类型
        void visit(Engineer engineer);

        //访问经理类型
        void visit(Manage leader);
    }

    class CEOVisitor implements Visitor {

        @Override
        public void visit(Engineer engineer) {
            LjyLogUtil.i("工程师: " + engineer.name + ", KPI: " + engineer.kpi);
        }

        @Override
        public void visit(Manage leader) {
            LjyLogUtil.i("产品: " + leader.name + ", KPI: " + leader.kpi);
        }
    }

    class CTOVisitor implements Visitor {

        @Override
        public void visit(Engineer engineer) {
            LjyLogUtil.i("工程师: " + engineer.name + ", 代码数量: " + engineer.getCodeLines());
        }

        @Override
        public void visit(Manage leader) {
            LjyLogUtil.i("产品: " + leader.name + ", 产品数量: " + leader.getProducts());
        }
    }

    //员工基类
    abstract class Staff {
        String name;
        int kpi;

        public Staff(String name) {
            this.name = name;
            this.kpi = new Random().nextInt(10);
        }

        public abstract void accept(Visitor visitor);
    }

    //工程师
    class Engineer extends Staff {

        public Engineer(String name) {
            super(name);
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }

        //工程师这一年写的代码数量
        public int getCodeLines() {
            return new Random().nextInt(10 * 100 * 100);
        }
    }

    //经理
    class Manage extends Staff {
        private int products;//产品数量

        public Manage(String name) {
            super(name);
            this.products = new Random().nextInt(10);
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }

        public int getProducts() {
            return products;
        }
    }

    //员工业务报表
    class BusinessReport {
        List<Staff> mStaffs = new LinkedList<>();

        public BusinessReport() {
            mStaffs.add(new Manage("经理-王小亮"));
            mStaffs.add(new Engineer("码农-孔小齐"));
            mStaffs.add(new Manage("经理-崔小磊"));
            mStaffs.add(new Engineer("码农-马小哲"));
            mStaffs.add(new Engineer("码农-刘小洋"));
        }

        public void showReport(Visitor visitor) {
            for (Staff staff : mStaffs) {
                staff.accept(visitor);
            }
        }
    }


    /**
     * 迭代器模式:又称游标(Cursor)模式
     * 行为型设计模式
     * 定义:提供一种方法顺序访问一个容器对象中的各个元素,而又不需要暴露该对象的内部表示
     * android源码中的使用:
     * List,Map所包含的迭代器,数据库查询使用的Cursor
     * 优点:
     * 弱化了容器类与遍历算法之间的关系
     * 缺点:
     * 类文件的增加
     */
    private void methodIteratorPattern() {
        //以统计员工数据为例
        Company1 company1 = new Company1();
        Company2 company2 = new Company2();
        Boss1 boss1 = new Boss1();
        LjyLogUtil.i("----普通方式----");
        boss1.read1(company1.getEmployees());
        boss1.read2(company2.getEmployees());
        LjyLogUtil.i("----iterator方式----");
        boss1.read3(company1);
        boss1.read3(company2);
        LjyDatabaseUtil.getHelper().saveAll(company1.getEmployees());
        LjyLogUtil.i("----LjyDatabaseUtil----");
        List<Employee> list = LjyDatabaseUtil.getHelper().queryAll(Employee.class);
        boss1.read1(list);

    }

    class Employee {
        private String name;
        private int age;
        private String sex;
        private String job;

        public Employee(String name, int age, String sex, String job) {
            this.name = name;
            this.age = age;
            this.sex = sex;
            this.job = job;
        }

        @Override
        public String toString() {
            return "员工:{" +
                    "姓名:'" + name + '\'' +
                    ", 年龄:" + age +
                    ", 性别:'" + sex + '\'' +
                    ", 职位:'" + job + '\'' +
                    '}';
        }
    }

    interface Iterator {
        //是否还有下一个元素
        boolean hasNext();

        //返回当前位置的元素,并将位置移至下一位
        Employee next();
    }

    class Company1 implements Iterator {
        private List<Employee> mList = new ArrayList<>();
        private int index;

        public Company1() {
            mList.add(new Employee("小金", 23, "男", "程序猿"));
            mList.add(new Employee("小七", 20, "男", "测试"));
            mList.add(new Employee("小丽", 21, "女", "UI设计师"));
            mList.add(new Employee("小亮", 26, "男", "产品"));
        }

        public List<Employee> getEmployees() {
            return mList;
        }

        @Override
        public boolean hasNext() {
            return !(index > mList.size() - 1 || mList.get(index) == null);
        }

        @Override
        public Employee next() {
            Employee employee = mList.get(index);
            index++;
            return employee;
        }
    }

    class Company2 implements Iterator {
        private Employee[] mArray = new Employee[4];
        private int index;

        public Company2() {
            mArray[0] = new Employee("毛毛", 19, "男", "前端");
            mArray[1] = new Employee("华仔", 21, "男", "测试");
            mArray[2] = new Employee("莉莉", 23, "女", "UI设计师");
            mArray[3] = new Employee("童歌", 32, "男", "产品");
        }

        public Employee[] getEmployees() {
            return mArray;
        }

        @Override
        public boolean hasNext() {
            return !(index > mArray.length - 1 || mArray[index] == null);
        }

        @Override
        public Employee next() {
            Employee employee = mArray[index];
            index++;
            return employee;
        }
    }

    class Boss1 {
        public void read1(List<Employee> list) {
            for (int i = 0; i < list.size(); i++) {
                LjyLogUtil.i(list.get(i).toString());
            }
        }

        public void read2(Employee[] array) {
            for (int i = 0; i < array.length; i++) {
                LjyLogUtil.i(array[i].toString());
            }
        }

        public void read3(Iterator iterator) {
            while (iterator.hasNext()) {
                LjyLogUtil.i(iterator.next().toString());
            }
        }

    }

    /**
     * 备忘录模式: 编程中的后悔药
     * 定义:在不破坏封闭的前提下,捕获一个对象的内部状态,并在该对象之外保存这个状态,
     * 这样以后就可以将该对象恢复到原先保存的状态
     * 优点:
     * 提供可以恢复状态的机制
     * 实现了信息的封装,不用关心保存状态的细节
     * 缺点:
     * 消耗资源(如果类的成员变量过多,每次保存都会消耗一定的内存)
     * <p>
     * android源码中的使用:
     * Activity的onSaveInstanceState,onRestoreInstanceState
     * (非主动退出或跳转到其他activity时触发onSaveInstanceState)
     */
    private void methodMemoPattern() {
        //以游戏存档为例,屏蔽了外界对CallOfDuty对象的直接访问
        //1.创建
        CallOfDuty game = new CallOfDuty();
        //2.玩
        game.play();
        //3.存档
        Caretaker caretaker = new Caretaker();
        caretaker.saveMemoto(game.createMemoto());
        //4.退出
        game.quit();
        //5.恢复游戏
        CallOfDuty newGame = new CallOfDuty();
        LjyLogUtil.i("newGame,恢复存档前属性:\n" + newGame.toString());
        newGame.restore(caretaker.getMemoto());
        LjyLogUtil.i("newGame,恢复存档后游戏属性:\n" + newGame.toString());
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//    }

    //使命召唤游戏
    class CallOfDuty {
        private int mCurrentLevel = 1;//当前等级/关卡
        private int mLifeBar = 100;//血量
        private String mGun = "汉阳造";//武器

        //玩
        public void play() {
            LjyLogUtil.i("使命召唤:");
            mLifeBar -= 10;
            mCurrentLevel++;
            mGun = "沙漠之鹰+Ak47";
            LjyLogUtil.i("晋级到第" + mCurrentLevel + "关,当前血量:" + mLifeBar + ",当前武器:" + mGun);
        }

        //退出
        public void quit() {
            LjyLogUtil.i("-----quit-------");
            LjyLogUtil.i("退出前的游戏属性:\n" + this.toString());
            LjyLogUtil.i("-----已退出------");
        }

        //创建备忘录
        public Memoto createMemoto() {
            Memoto memoto = new Memoto();
            memoto.mCurrentLevel = mCurrentLevel;
            memoto.mLifeBar = mLifeBar;
            memoto.mGun = mGun;
            return memoto;
        }

        //恢复游戏
        public void restore(Memoto memoto) {
            this.mCurrentLevel = memoto.mCurrentLevel;
            this.mLifeBar = memoto.mLifeBar;
            this.mGun = memoto.mGun;
        }

        @Override
        public String toString() {
            return "CallOfDuty{" +
                    "mCurrentLevel=" + mCurrentLevel +
                    ", mLifeBar=" + mLifeBar +
                    ", mGun='" + mGun + '\'' +
                    '}';
        }
    }

    //备忘录类
    class Memoto {
        private int mCurrentLevel;
        private int mLifeBar;
        private String mGun;

        @Override
        public String toString() {
            return "Memoto{" +
                    "mCurrentLevel=" + mCurrentLevel +
                    ", mLifeBar=" + mLifeBar +
                    ", mGun='" + mGun + '\'' +
                    '}';
        }
    }

    //Caretaker,负责管理Memoto
    class Caretaker {
        Memoto mMemoto;

        //存档
        public void saveMemoto(Memoto memoto) {
            this.mMemoto = memoto;
        }

        //读档
        public Memoto getMemoto() {
            return mMemoto;
        }
    }


    /**
     * 观察者模式:
     * 定义对象间一种一对多的依赖关系,使得每当一个对象改变状态,
     * 则所有依赖于它的对象都会得到通知并被自动更新
     * 常用:GUI系统,订阅发布系统
     * 使用场景:关联行为,事件多级触发,跨系统的消息交换(消息队列,事件总线)
     * 优点:解耦,减小依赖性,灵活,可扩展
     * 缺点:开发和运行效率,一对多调试比较复杂,而且java中的消息通知是默认顺序执行的,
     * 一个观察者卡顿,会影响整体的执行效率,这种情况下,一般要考虑异步方式
     * <p>
     * Observer,Observable是JDK中内置类型,可见观察者模式的重要性
     * <p>
     * android源码中的使用:
     * 1.ListView Adapter的notifyDataSetChanged,
     * setAdapter时中会创建一个数据集的观察者AdapterDataSetObserver
     * 而BaseAdapter中有一个被观察者DataSetObservable,Adapter的notifyDataSetChanged中就是调用了
     * DataSetObservable的notifyChanged方法,notifyChanged中则是遍历了所有观察者(mObservers),
     * 并调用他们的onChanged方法
     * 2.BroadcastReceiver广播的注册订阅发送,也是典型的观察者模式
     * 3.EventBus,RxJava等
     */
    private void methodObserverPattern() {
        //以极客头条订阅为例
        //1.创建被观察者
        GeekNews geekNews = new GeekNews();
        //2.创建观察者
        Coder coder1 = new Coder("马画藤");
        Coder coder2 = new Coder("云马");
        Coder coder3 = new Coder("王剑灵");
        //订阅/注册
        geekNews.addObserver(coder1);
        geekNews.addObserver(coder2);
        geekNews.addObserver(coder3);
        //发布消息
        geekNews.postNews("最新Android文章-RxJava详解");
        geekNews.postNews("最新iOS文章-UIView详解");
    }

    //观察者,程序员
    class Coder implements Observer {
        public String name;

        public Coder(String name) {
            this.name = name;
        }

        @Override
        public void update(Observable o, Object arg) {
            LjyLogUtil.i("hi, " + name + ", 极客头条有文章更新了,更新内容:" + arg);
        }

        @Override
        public String toString() {
            return "码农:" + name;
        }
    }

    //极客头条,被观察者
    class GeekNews extends Observable {
        public void postNews(String content) {
            setChanged();
            notifyObservers(content);
        }
    }


    /**
     * 命令模式:
     * 让程序通畅执行,行为型设计模式
     * 定义:
     * 将一个请求封装成一个对象,从而让用户使用不同的请求把客户端参数化;
     * 对请求排队或者记录请求日志,以及支持可撤销的操作
     * 是回调机制的一个面向对象的替代品
     * 将行为调用者与实现者解耦
     */
    private void methodICommandPattern() {
        //以CS游戏,贪吃蛇游戏为例
        //开闭原则
        GameMachine btn = new GameMachine();
        CS cs = new CS();
        btn.initWithGame(cs);
        //执行命令
        btn.toLeft();
        btn.toRight();
        btn.toTop();
        btn.toLeft();
        btn.toBottom();
        btn.toRight();
        Snake snake = new Snake();
        btn.initWithGame(snake);
        //执行命令
        btn.toLeft();
        btn.toRight();
        btn.toTop();
        btn.toLeft();
        btn.toBottom();
        btn.toRight();


    }

    abstract class Game {
        abstract void toLeft();

        abstract void toRight();

        abstract void toTop();

        abstract void toBottom();
    }

    //接收者角色,CS游戏
    class CS extends Game {
        //游戏方法的具体实现
        @Override
        void toLeft() {
            LjyLogUtil.i("cs 向左走");
        }

        @Override
        void toRight() {
            LjyLogUtil.i("cs 向右走");
        }

        @Override
        void toTop() {
            LjyLogUtil.i("cs 跳起");
        }

        @Override
        void toBottom() {
            LjyLogUtil.i("cs 蹲下");
        }
    }

    //接收者角色,CS游戏
    class Snake extends Game {
        //游戏方法的具体实现
        @Override
        void toLeft() {
            LjyLogUtil.i("Snake 向左");
        }

        @Override
        void toRight() {
            LjyLogUtil.i("Snake 向右");
        }

        @Override
        void toTop() {
            LjyLogUtil.i("Snake 向上");
        }

        @Override
        void toBottom() {
            LjyLogUtil.i("Snake 向下");
        }
    }

    //命令者抽象,定义执行方法
    public interface Command {
        void execute();
    }

    public static class LeftCommand implements Command {
        //游戏对象的引用
        private Game machine;

        public LeftCommand(Game machine) {
            this.machine = machine;
        }

        @Override
        public void execute() {
            machine.toLeft();
        }
    }

    public static class RightCommand implements Command {
        //游戏对象的引用
        private Game machine;

        public RightCommand(Game machine) {
            this.machine = machine;
        }

        @Override
        public void execute() {
            machine.toRight();
        }
    }

    public static class TopCommand implements Command {
        //游戏对象的引用
        private Game machine;

        public TopCommand(Game machine) {
            this.machine = machine;
        }

        @Override
        public void execute() {
            machine.toTop();
        }
    }

    public static class BottomCommand implements Command {
        //游戏对象的引用
        private Game machine;

        public BottomCommand(Game machine) {
            this.machine = machine;
        }

        @Override
        public void execute() {
            machine.toBottom();
        }
    }


    //请求者类,命令由游戏机发起
    static class GameMachine {
        private LeftCommand mLeftCommand;
        private RightCommand mRightCommand;
        private TopCommand mTopCommand;
        private BottomCommand mBottomCommand;

        public void initWithGame(Game game) {
            setLeftCommand(new LeftCommand(game));
            setRightCommand(new RightCommand(game));
            setTopCommand(new TopCommand(game));
            setBottomCommand(new BottomCommand(game));
        }

        public void setLeftCommand(LeftCommand leftCommand) {
            mLeftCommand = leftCommand;
        }

        public void setRightCommand(RightCommand rightCommand) {
            mRightCommand = rightCommand;
        }

        public void setTopCommand(TopCommand topCommand) {
            mTopCommand = topCommand;
        }

        public void setBottomCommand(BottomCommand bottomCommand) {
            mBottomCommand = bottomCommand;
        }

        public void toLeft() {
            mLeftCommand.execute();
        }

        public void toRight() {
            mRightCommand.execute();
        }

        public void toTop() {
            mTopCommand.execute();
        }

        public void toBottom() {
            mBottomCommand.execute();
        }
    }

    /**
     * 解释器模式 (化繁为简的翻译机)
     * 本质:将复杂的问题简单化,模块化,分离实现,解释执行
     * 使用场景:
     * 1.如果某个简单的语言需要解释执行而且可以将该语言中的语句表示为一个抽象的语法树时
     * 2.某些特性的领域出现不断重复的问题时,可以将该领域的问题转化为语法规则下的语句,
     * 然后构建解释器来解释该语句,例如英文的大小写转换
     * 优点:
     * 灵活的扩展性,扩展延伸时只需增加新的解释器
     * 缺点:
     * 生成的大量类可能造成后期维护困难;对于复杂的文法构建其抽象语法树会异常繁琐
     * Android源码中的应用:
     * AndroidManifest.xml这个配置文件的解释(PackageParser),xml解析
     */
    private void methodInterpreterPattern() {
        //以对算术表达式的解释为例
        String expression = "123 + 10 + 100 + 100 + 3000 - 1000 - 111";
        Calculator calculator = new Calculator(expression);
        String info = expression + " 的计算结果是: " + calculator.calculate();
        LjyLogUtil.i(info);
    }

    //解释器抽象基类
    abstract class ArithmeticExpression {
        public abstract int interpret();
    }

    //数字解释器
    class NumExpression extends ArithmeticExpression {
        private int num;

        public NumExpression(int num) {
            this.num = num;
        }

        @Override
        public int interpret() {
            return num;
        }
    }

    //运算符号抽象解释器
    abstract class OperatorExpression extends ArithmeticExpression {
        //声明两个变量存储运算符号两边的数字解释器
        protected ArithmeticExpression exp1, exp2;

        public OperatorExpression(ArithmeticExpression exp1, ArithmeticExpression exp2) {
            this.exp1 = exp1;
            this.exp2 = exp2;
        }
    }

    //加法运算解释器
    class AdditionExpression extends OperatorExpression {

        public AdditionExpression(ArithmeticExpression exp1, ArithmeticExpression exp2) {
            super(exp1, exp2);
        }

        @Override
        public int interpret() {
            return exp1.interpret() + exp2.interpret();
        }
    }

    //减法解释器
    class SubtractionExpression extends OperatorExpression {
        public SubtractionExpression(ArithmeticExpression exp1, ArithmeticExpression exp2) {
            super(exp1, exp2);
        }

        @Override
        public int interpret() {
            return exp1.interpret() - exp2.interpret();
        }
    }


    //处理与解释相关的业务
    class Calculator {
        //声明一个stack栈存储并操作相关的解释器
        private Stack<ArithmeticExpression> mExpStack = new Stack<>();

        public Calculator(String expression) {
            //存储运算符两边的数字解释器
            ArithmeticExpression exp1, exp2;
            //空格分隔表达式字符串
            String[] elements = expression.split(" ");
            for (int i = 0; i < elements.length; i++) {
                switch (elements[i].charAt(0)) {
                    case '+'://如果是加号
                        exp1 = mExpStack.pop();//将栈中的解释器弹出作为运算符号左边的解释器
                        exp2 = new NumExpression(Integer.valueOf(elements[++i]));
                        mExpStack.push(new AdditionExpression(exp1, exp2));
                        break;
                    case '-':
                        exp1 = mExpStack.pop();
                        exp2 = new NumExpression(Integer.valueOf(elements[++i]));
                        mExpStack.push(new SubtractionExpression(exp1, exp2));
                        break;
                    default:
                        mExpStack.push(new NumExpression(Integer.valueOf(elements[i])));
                        break;
                }
            }
        }

        //计算结果
        public int calculate() {
            return mExpStack.pop().interpret();
        }
    }

    /**
     * 责任链模式:
     * 行为型设计模式之一,使编程更有灵活性
     * 将一个请求从链式的首端发出,沿着链的路径依次传递给每个节点对象,直到有对象处理这个请求为止
     * 使多个对象都有机会处理请求,从而避免了请求发送者与接收者之间的耦合关系
     * 优点:
     * 请求者与处理者关系解耦,提高代码灵活性
     * 缺点:
     * 对链中请求处理者的遍历,如果处理者太多,那么必定会影响性能,尤其是在一些递归调用中
     * android源码中的使用:
     * 触摸事件从ViewTree分发传递
     */
    private void methodDutyChainPattern() {
        /*
        //构造处理对象
        AbsHandler handler1 = new Handler1();
        AbsHandler handler2 = new Handler2();
        AbsHandler handler3 = new Handler3();
        //设置节点
        handler1.nextHandler = handler2;
        handler2.nextHandler = handler3;
        //构造请求对象
        AbsRequest request1 = new Request1("001");
        AbsRequest request2 = new Request2("002");
        AbsRequest request3 = new Request3("003");
        //总是从链式的首段发起请求
        handler1.handleRequest(request1);
        handler1.handleRequest(request2);
        handler1.handleRequest(request3);
         */

        //申请报销费用为例
        //1.构建领导对象
        GroupLeader groupLeader = new GroupLeader();
        Director director = new Director();
        Manager manager = new Manager();
        Boss boss = new Boss();
        //2.设置上一级领导
        groupLeader.nextLeader = director;
        director.nextLeader = manager;
        manager.nextLeader = boss;
        //3.发起报账申请,从最低级开始
        LjyLogUtil.i("---------审批范围-----------\n组长<=1000,主管<=5000,经理<=10000,老板>10000\n----------------");
        int num1 = 800;
        LjyLogUtil.i("-----------小智申请报销" + num1 + "元-----------");
        groupLeader.handleRequest(num1);
        int num2 = 6800;
        LjyLogUtil.i("-----------小米申请报销" + num2 + "元-----------");
        groupLeader.handleRequest(num2);
        int num3 = 18000;
        LjyLogUtil.i("-----------小明申请报销" + num3 + "元-----------");
        groupLeader.handleRequest(num3);


    }

    //test---1:以申请报销费用为例
    abstract class Leader {
        protected Leader nextLeader;//上一级领导处理者

        /**
         * 处理报账请求
         *
         * @param money 能批复的报账额度
         */
        public final void handleRequest(int money) {
            if (money <= limit()) {
                handle(money);
            } else {
                if (nextLeader != null) {
                    nextLeader.handleRequest(money);
                }
            }
        }

        /**
         * 自身能批复的额度权限
         *
         * @return 额度
         */
        public abstract int limit();

        /**
         * 处理报账行为
         *
         * @param money 具体金额
         */
        public abstract void handle(int money);
    }

    //组长
    class GroupLeader extends Leader {

        @Override
        public int limit() {
            return 1000;
        }

        @Override
        public void handle(int money) {
            LjyLogUtil.i("组长批准报销" + money + "元");
        }
    }

    //主管
    class Director extends Leader {

        @Override
        public int limit() {
            return 5000;
        }

        @Override
        public void handle(int money) {
            LjyLogUtil.i("主管批准报销" + money + "元");
        }
    }

    //经理
    class Manager extends Leader {

        @Override
        public int limit() {
            return 10000;
        }

        @Override
        public void handle(int money) {
            LjyLogUtil.i("经理批准报销" + money + "元");
        }
    }

    //主管
    class Boss extends Leader {

        @Override
        public int limit() {
            return Integer.MAX_VALUE;
        }

        @Override
        public void handle(int money) {
            LjyLogUtil.i("老板批准报销" + money + "元");
        }
    }

    //test----2
    abstract class AbsHandler {
        protected AbsHandler nextHandler;//下一节点上的处理者对象

        /**
         * 处理请求
         *
         * @param request 请求对象
         */
        public final void handleRequest(AbsRequest request) {
            if (getHandleLevel() == request.getRequestLevel()) {
                //处理级别一致则由该处理对象处理
                handle(request);
            } else {
                //否则转发给下一级节点
                if (nextHandler != null) {
                    nextHandler.handleRequest(request);
                } else {
                    LjyLogUtil.i("所有处理者都不能处理该请求");
                }
            }
        }

        /**
         * 获取处理者对象的处理级别
         *
         * @return 处理级别
         */
        protected abstract int getHandleLevel();

        protected abstract void handle(AbsRequest request);
    }

    abstract class AbsRequest {
        private Object obj;//处理对象

        public AbsRequest(Object obj) {
            this.obj = obj;
        }

        /**
         * 获取处理的内容对象
         */
        public Object getContent() {
            return obj;
        }

        /**
         * 获取请求级别
         */
        abstract int getRequestLevel();
    }

    class Request1 extends AbsRequest {

        public Request1(Object obj) {
            super(obj);
        }

        @Override
        int getRequestLevel() {
            return 1;
        }
    }

    class Request2 extends AbsRequest {

        public Request2(Object obj) {
            super(obj);
        }

        @Override
        int getRequestLevel() {
            return 2;
        }
    }

    class Request3 extends AbsRequest {

        public Request3(Object obj) {
            super(obj);
        }

        @Override
        int getRequestLevel() {
            return 3;
        }
    }

    class Handler1 extends AbsHandler {

        @Override
        protected int getHandleLevel() {
            return 1;
        }

        @Override
        protected void handle(AbsRequest request) {
            LjyLogUtil.i(String.format("处理者:%s__请求对象:%s__request.getContent:%s",
                    this.getClass().getSimpleName(), request.getClass().getSimpleName(), request.getContent().toString()));
        }
    }

    class Handler2 extends AbsHandler {

        @Override
        protected int getHandleLevel() {
            return 2;
        }

        @Override
        protected void handle(AbsRequest request) {
            LjyLogUtil.i(String.format("处理者:%s__请求对象:%s__request.getContent:%s",
                    this.getClass().getSimpleName(), request.getClass().getSimpleName(), request.getContent().toString()));
        }
    }

    class Handler3 extends AbsHandler {

        @Override
        protected int getHandleLevel() {
            return 3;
        }

        @Override
        protected void handle(AbsRequest request) {
            LjyLogUtil.i(String.format("处理者:%s__请求对象:%s__request.getContent:%s",
                    this.getClass().getSimpleName(), request.getClass().getSimpleName(), request.getContent().toString()));
        }
    }


    /**
     * 状态模式:
     * 不同状态决定不同行为,与策略模式结构几乎一样,但目的和本质不同
     * 当一个对象的内在状态改变时,其行为也随之改变
     * 优点:
     * 将繁琐的状态判断转换成结构清晰的状态类族,
     * 在避免代码膨胀的同时也保证了可扩展性和可维护性
     * 缺点:
     * 会增加系统类和对象的个数
     * android源码中的使用:
     * WifiSettings中wifi的不同状态
     */
    private void methodStatePattern() {
        //普通写法
        LjyLogUtil.i("普通写法:");
        TvControler tvControler = new TvControler();
        tvControler.powerOnOFF();
        tvControler.nextChannel();
        tvControler.prevChannel();
        tvControler.turnUp();
        tvControler.turnDown();
        tvControler.powerOnOFF();
        tvControler.nextChannel();
        tvControler.prevChannel();
        tvControler.turnUp();
        tvControler.turnDown();
        //状态模式写法
        LjyLogUtil.i("状态模式:");
        TvControler2 tvControler2 = new TvControler2();
        tvControler2.powerOn();
        tvControler2.nextChannel();
        tvControler2.prevChannel();
        tvControler2.turnUp();
        tvControler2.turnDown();
        tvControler2.powerOff();
        tvControler2.nextChannel();
        tvControler2.prevChannel();
        tvControler2.turnUp();
        tvControler2.turnDown();
    }

    //以电视遥控器为例
    //1.普通写法
    class TvControler {
        //开机状态
        private final static int POWER_ON = 1;
        //关机状态
        private final static int POWER_OFF = 2;
        //记录状态,默认为关
        private int mState = POWER_OFF;

        public void powerOnOFF() {
            if (mState == POWER_OFF) {
                mState = POWER_ON;
                LjyLogUtil.i("开机啦~~");
            } else {
                mState = POWER_OFF;
                LjyLogUtil.i("关机啦~~");
            }
        }

        public void nextChannel() {
            if (mState == POWER_ON) {
                LjyLogUtil.i("下一频道");
            } else {
                LjyLogUtil.i("还没有开机哦");
            }
        }

        public void prevChannel() {
            if (mState == POWER_ON) {
                LjyLogUtil.i("上一频道");
            } else {
                LjyLogUtil.i("还没有开机哦");
            }
        }

        public void turnUp() {
            if (mState == POWER_ON) {
                LjyLogUtil.i("调高音量");
            } else {
                LjyLogUtil.i("还没有开机哦");
            }
        }

        public void turnDown() {
            if (mState == POWER_ON) {
                LjyLogUtil.i("降低音量");
            } else {
                LjyLogUtil.i("还没有开机哦");
            }
        }
    }

    //2.状态模式写法
    //虽然看起来更复杂了,但是如果再增加一种状态时,确是比上面的要方便

    interface TvState {
        void nextChannel();

        void prevChannel();

        void turnUp();

        void turnDown();
    }

    /**
     * 开机机状态
     */
    class PowerOnState implements TvState {

        @Override
        public void nextChannel() {
            LjyLogUtil.i("下一频道");
        }

        @Override
        public void prevChannel() {
            LjyLogUtil.i("上一频道");
        }

        @Override
        public void turnUp() {
            LjyLogUtil.i("调高音量");
        }

        @Override
        public void turnDown() {
            LjyLogUtil.i("降低音量");
        }
    }

    /**
     * 关机机状态
     */
    class PowerOffState implements TvState {

        @Override
        public void nextChannel() {
            LjyLogUtil.i("还没有开机哦");
        }

        @Override
        public void prevChannel() {
            LjyLogUtil.i("还没有开机哦");
        }

        @Override
        public void turnUp() {
            LjyLogUtil.i("还没有开机哦");
        }

        @Override
        public void turnDown() {
            LjyLogUtil.i("还没有开机哦");
        }
    }

    class TvControler2 {
        //记录状态,默认为关
        private TvState mTvState;

        public void setTvState(TvState tvState) {
            mTvState = tvState;
        }

        public void powerOn() {
            setTvState(new PowerOnState());
            LjyLogUtil.i("开机啦~~");
        }

        public void powerOff() {
            setTvState(new PowerOffState());
            LjyLogUtil.i("关机啦~~");
        }

        public void nextChannel() {
            mTvState.nextChannel();
        }

        public void prevChannel() {
            mTvState.prevChannel();
        }

        public void turnUp() {
            mTvState.turnUp();
        }

        public void turnDown() {
            mTvState.turnDown();
        }
    }


    /**
     * 原型模式(使程序运行更高效)
     * (样板,克隆,可定制)
     * 用原型实例指定创建对象的种类,并通过拷贝这些原型创建新的对象
     * 适用于:
     * 1.类初始化需要消耗非常多的资源
     * 2.创建复杂或构造耗时
     * 3.保护性拷贝:一个对象提供给其他对象访问,且各个调用者可能都会修改其值,
     * 可以使用原型模式拷贝多个对象供其使用,可以使用Cloneable接口
     * 优点:
     * 对内存中的二进制流的拷贝,比new性能好,尤其是循环体内产生大量对象时
     * 缺点:
     * 其优点也是缺点,直接在内存中拷贝,不执行构造函数,少了约束
     * Android源码中的使用:
     * clone方法,如下面例子中的clone,以及arrayList的clone方法,Intent.clone()等
     */
    private void methodProtoPattern() {
        //构建文档对象
        WordDocument document = new WordDocument();
        //编辑文档
        document.setText("这是一篇文章啊");
        document.addImages("img001");
        document.addImages("img002");
        document.addImages("img003");
        //show
        LjyLogUtil.i("document:");
        document.showDocument();
        //拷贝
        WordDocument document2 = document.clone();
        //show2
        LjyLogUtil.i("clone document_2:");
        document2.showDocument();
        //编辑2
        document2.setText("修改了文字啊");
        //浅拷贝与深拷贝的区别,详勘clone方法
        document2.addImages("image_004");
//        document2.setImages(new ArrayList<String>());
        //show
        LjyLogUtil.i("document:");
        document.showDocument();
        //show2
        LjyLogUtil.i("clone document_2:");
        document2.showDocument();

    }

    //以文档的编辑还原为例
    class WordDocument implements Cloneable {
        //文本
        private String text;
        //图片列表
        private ArrayList<String> images = new ArrayList<>();

        public WordDocument() {
            LjyLogUtil.i("WordDocument的构造函数");
        }

        @Override
        protected WordDocument clone() {
            WordDocument doc = null;
            try {
                doc = (WordDocument) super.clone();
                doc.text = this.text;
                //浅拷贝:
//                doc.images = this.images;
                //深拷贝
                doc.images = (ArrayList<String>) this.images.clone();
            } catch (CloneNotSupportedException e) {
                LjyLogUtil.e(e.getLocalizedMessage());
            }
            return doc;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public ArrayList<String> getImages() {
            return images;
        }

        public void setImages(ArrayList<String> images) {
            this.images = images;
        }

        public void addImages(String img) {
            this.images.add(img);
        }

        public void showDocument() {
            LjyLogUtil.i("---------word content start-----------");
            LjyLogUtil.i("text:" + text);
            LjyLogUtil.i("images:");
            for (String imgName : images) {
                LjyLogUtil.i(imgName);
            }
            LjyLogUtil.i("---------word content end-----------");
        }
    }

    /**
     * 外观模式:
     * (由外观类去保存各个子系统的引用，实现由一个统一的外观类去包装多个子系统类，
     * 然而客户端只需要引用这个外观类，然后由外观类来调用各个子系统中的方法。)
     * 通过创建一个统一的类，用来包装子系统中一个或多个复杂的类，
     * 客户端可以通过调用外观类的方法来调用内部子系统中所有方法
     * 优点:
     * 降低了客户类与子系统类的耦合度，实现了子系统与客户之间的松耦合关系
     * 对客户屏蔽了子系统组件，从而简化了接口，减少了客户处理的对象数目并使子系统的使用更加简单。
     * 降低原有系统的复杂度和系统中的编译依赖性，并简化了系统在不同平台之间的移植过程
     * 缺点:
     * 在不引入抽象外观类的情况下，增加新的子系统可能需要修改外观类或客户端的源代码，违背了“开闭原则”
     * 不能很好地限制客户使用子系统类，如果对客户访问子系统类做太多的限制则减少了可变性和灵活性。
     * android源码中的使用:
     * context,他封装了很多操作,如startActivity,sendBroadcast,bindServiced等,
     * context的子类中实现这些方法:
     * Context-->ContextWrapper--->ContextThemeWrapper--->Activity
     */
    private void methodFacadePattern() {
        SmartControl smartControl = new SmartControl();
        smartControl.allOn();
        smartControl.allOff();

        startActivity(null);
        mContext.sendBroadcast(null);
        mContext.bindService(null, null, 0);
    }

    //以智能家居,统一管理家电为例
    public class Light {
        public void on() {
            LjyLogUtil.i("开灯...");
        }

        public void off() {
            LjyLogUtil.i("关灯...");
        }
    }

    public class Televison {
        public void on() {
            LjyLogUtil.i("开电视...");
        }

        public void off() {
            LjyLogUtil.i("关电视...");
        }
    }

    public class Aircondition {
        public void on() {
            LjyLogUtil.i("开空调...");
        }

        public void off() {
            LjyLogUtil.i("关空调...");
        }
    }

    /**
     * 智能遥控器
     */
    public class SmartControl {
        private Light mLight = new Light();
        private Televison mTelevison = new Televison();
        private Aircondition mAircondition = new Aircondition();

        /**
         * 开所有电器
         */
        public void allOn() {
            lightOn();
            tvOn();
            airOn();
        }

        /**
         * 关所有电器
         */
        public void allOff() {
            lightOff();
            tvOff();
            airOff();
        }

        public void lightOn() {
            mLight.on();
        }

        public void lightOff() {
            mLight.off();
        }

        public void tvOn() {
            mTelevison.on();
        }

        public void tvOff() {
            mTelevison.off();
        }

        public void airOn() {
            mAircondition.on();
        }

        public void airOff() {
            mAircondition.off();
        }
    }

    /**
     * 建造者模式:(自由扩展你的项目)
     * 将一个复杂对象的构建与它的表示分离,使得同样的构建过程可以创建不同的表示
     * 在用户不知道对象的建造过程和细节的情况下就可以直接创建复杂的对象。
     * <p>
     * 在Android源码中的使用: AlertDialog.Builder
     */
    private void methodBuilderPattern() {
        HuaWeiBuilder builder = new HuaWeiBuilder();
        builder.buildOS("Android 7.0");
        builder.buildRAM(4);
        builder.buildAICPU(true);
        MobilePhone huaWeiPhone = builder.build();
        LjyLogUtil.i(huaWeiPhone.toString());

    }

    //以组装手机为例:
    abstract class MobilePhone {
        protected String cpu;
        protected String ram;
        protected String os;

        protected MobilePhone() {

        }

        public abstract void setCPU();

        public void setRAM(String ram) {
            this.ram = ram;
        }

        public void setOS(String os) {
            this.os = os;
        }

        @Override
        public String toString() {
            return "MobilePhone{" +
                    "cpu='" + cpu + '\'' +
                    ", ram=" + ram +
                    ", os='" + os + '\'' +
                    '}';
        }
    }

    class HuaWeiPhone extends MobilePhone {
        protected String aiCpu;

        protected HuaWeiPhone() {

        }

        @Override
        public void setCPU() {
            this.cpu = "麒麟970";
        }

        public void setAiCpu(String aiCpu) {
            this.aiCpu = aiCpu;
        }

        @Override
        public String toString() {
            return "HuaWeiPhone{" +
                    "cpu='" + cpu + '\'' +
                    ", ram=" + ram +
                    ", os='" + os + '\'' +
                    ", aiCpu='" + aiCpu + '\'' +
                    '}';
        }
    }

    abstract class PhoneBuilder {

        public abstract void buildRAM(int ram);

        public abstract void buildOS(String os);

        public abstract MobilePhone build();
    }

    class HuaWeiBuilder extends PhoneBuilder {
        HuaWeiParam mHuaWeiParam;
        private HuaWeiPhone huaWeiPhone;

        public HuaWeiBuilder() {
            mHuaWeiParam = new HuaWeiParam();
        }

        public void buildAICPU(boolean isSupportAi) {
            if (isSupportAi)
                mHuaWeiParam.aiCpu = "麒麟AI芯片";
        }

        @Override
        public void buildRAM(int ram) {
            mHuaWeiParam.ram = String.format("AMD-007 %dG", ram);
        }

        @Override
        public void buildOS(String os) {
            mHuaWeiParam.os = os;
        }

        @Override
        public MobilePhone build() {
            huaWeiPhone = new HuaWeiPhone();
            huaWeiPhone.setCPU();
            huaWeiPhone.setOS(mHuaWeiParam.os);
            huaWeiPhone.setRAM(mHuaWeiParam.ram);
            huaWeiPhone.setAiCpu(mHuaWeiParam.aiCpu);
            return huaWeiPhone;
        }

        class HuaWeiParam {
            String aiCpu;
            String os;
            String ram;
        }
    }


    /**
     * 模板方法模式:
     * 基于”继承“
     * 定义一个模板结构(框架,关键步骤,固定流程)，将具体内容延迟到子类去实现
     * 解决的问题
     * 1.提高代码复用性
     * 将相同部分的代码放在抽象的父类中，而将不同的代码放入不同的子类中
     * 2.实现了反向控制
     * 通过一个父类调用其子类的操作，通过对子类的具体实现扩展不同的行为，实现了反向控制 & 符合“开闭原则”
     * <p>
     * 在Android源码中的体现:
     * 1.AsyncTask:doInBackground方法是必须要重写的,其他的方法如果需要也可以重写,但是若不重写也可以
     * 2.Activity的生命周期函数,固定的几个生命
     */
    private void methodTemplateMethodPattern() {
        MacComputer macComputer = new MacComputer();
        macComputer.startUp();
        LjyLogUtil.i("\n");
        DellComputer dellComputer = new DellComputer();
        dellComputer.startUp();
    }

    //在Android源码中的体现:
    class MyAcync extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            return null;
        }
    }

    //以电脑开机过程为例
    abstract class Computer {
        //1.开机
        void powerOn() {
            LjyLogUtil.i("1.接入30v电源");
        }

        //2.硬件检查
        void checkHardware() {
            LjyLogUtil.i("2 检查硬件");
        }

        //3.载入操作系统
        abstract void loadOS();

        //4.登录
        void login() {
            LjyLogUtil.i("4.输入用户名密码登录");
        }

        //开机,固定步骤1,2,3,4;设为final防止被复写篡改
        public final void startUp() {
            LjyLogUtil.i(String.format("%s:-------开机start------", this.getClass().getSimpleName()));
            powerOn();
            checkHardware();
            loadOS();
            login();
            LjyLogUtil.i(String.format("%s:-------开机end------", this.getClass().getSimpleName()));
        }

    }

    class MacComputer extends Computer {

        @Override
        void powerOn() {
//            super.powerOn();
            LjyLogUtil.i("1.接入20.2v电源");
        }

        @Override
        void checkHardware() {
            super.checkHardware();
            LjyLogUtil.i("---> 2.2 检查硬件防火墙");
        }

        @Override
        void loadOS() {
            LjyLogUtil.i("3.载入mac系统");
        }

        @Override
        void login() {
//            super.login();
            LjyLogUtil.i("4.输入指纹登录");
        }
    }

    class DellComputer extends Computer {

        @Override
        void loadOS() {
            LjyLogUtil.i("3.载入windows系统");
        }
    }


    /**
     * 代理模式:(间接访问目标对象)
     * (静态代理,动态代理)
     * 给目标对象提供一个代理对象，并由代理对象控制对目标对象的引用
     * 解决问题:无法访问或者不想直接访问目标对象时
     * 优点
     * 协调调用者和被调用者，降低了系统的耦合度
     * 代理对象作为客户端和目标对象之间的中介，起到了保护目标对象的作用
     * 缺点
     * 由于在客户端和真实主题之间增加了代理对象，因此会造成请求的处理速度变慢；
     * 实现代理模式需要额外的工作（有些代理模式的实现非常复杂），从而增加了系统实现的复杂度。
     * <p>
     * 1.远程代理:为一个对象在不同的地址空间提供局部代表
     * 隐藏一个对象存在于不同地址空间的事实；
     * 远程机器可能具有更好的计算性能与处理速度，可以快速响应并处理客户端请求。
     * 2.虚拟代理：通过使用过一个小的对象代理一个大对象
     * 目的：减少系统的开销。
     * 3.保护代理：控制目标对象的访问，给不同用户提供不同的访问权限
     * 目的：用来控制对真实对象的访问权限
     * 4.智能引用代理，当需要控制对原始对象的访问时,额外操作包括耗时操作、计算访问次数等等
     * 目的：在不影响对象类的情况下，在访问对象时进行更多的操作
     * 5.防火墙代理：保护目标不让恶意用户靠近
     * 6.Cache代理：为结果提供临时的存储空间，以便其他客户端调用
     */
    private void methodProxyPattern() {
        LjyLogUtil.i("------普通模式-----------");
        PersonA personA = new PersonA();
        personA.submit();
        personA.burden();
        personA.defend();
        personA.finish();
        LjyLogUtil.i("------静态代理模式-----------");
        Lawyer lawyer = new Lawyer(personA);
        lawyer.submit();
        lawyer.burden();
        lawyer.defend();
        lawyer.finish();
        LjyLogUtil.i("------动态代理模式-----------");
        //用反射机制实现
        DynamicProxy proxy = new DynamicProxy(personA);
        ClassLoader loader = personA.getClass().getClassLoader();
        //动态构造一个代理者律师
        ILawsuit lvShi = (ILawsuit) Proxy.newProxyInstance(loader, new Class[]{ILawsuit.class}, proxy);
        lvShi.submit();
        lvShi.burden();
        lvShi.defend();
        lvShi.finish();

    }

    //静态代理:
    //以打官司为例
    public interface ILawsuit {
        void submit();//提交申请

        void burden();//举证

        void defend();//辩护

        void finish();//诉讼完成
    }

    class PersonA implements ILawsuit {

        @Override
        public void submit() {
            LjyLogUtil.i(String.format("%s:老板拖欠工作,申请仲裁呀", this.getClass().getSimpleName()));
        }

        @Override
        public void burden() {
            LjyLogUtil.i(String.format("%s:这是合同和银行流水", this.getClass().getSimpleName()));
        }

        @Override
        public void defend() {
            LjyLogUtil.i(String.format("%s:证据确凿,还我公道", this.getClass().getSimpleName()));
        }

        @Override
        public void finish() {
            LjyLogUtil.i(String.format("%s:诉讼成功,七日内结算", this.getClass().getSimpleName()));
        }
    }

    public class Lawyer implements ILawsuit {
        private ILawsuit mILawsuit;//持有的被代理者的引用

        public Lawyer(ILawsuit lawsuit) {
            mILawsuit = lawsuit;
        }

        @Override
        public void submit() {
            LjyLogUtil.i(String.format("%s:我是律师,我为%s代言", this.getClass().getSimpleName(), mILawsuit.getClass().getSimpleName()));
            mILawsuit.submit();
        }

        @Override
        public void burden() {
            LjyLogUtil.i(String.format("%s:我是律师,我为%s代言", this.getClass().getSimpleName(), mILawsuit.getClass().getSimpleName()));
            mILawsuit.burden();
        }

        @Override
        public void defend() {
            LjyLogUtil.i(String.format("%s:我是律师,我为%s代言", this.getClass().getSimpleName(), mILawsuit.getClass().getSimpleName()));
            mILawsuit.defend();
        }

        @Override
        public void finish() {
            LjyLogUtil.i(String.format("%s:我是律师,我为%s代言", this.getClass().getSimpleName(), mILawsuit.getClass().getSimpleName()));
            mILawsuit.finish();
        }
    }

    //动态代理
    class DynamicProxy implements InvocationHandler {
        private Object obj;//被代理的类引用

        public DynamicProxy(Object obj) {
            this.obj = obj;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //调用被代理类对象的方法
            Object result = method.invoke(obj, args);
            return result;
        }
    }


    /**
     * 适配器模式
     * 定义一个包装类，用于包装不兼容接口的对象
     * 适配器模式的形式分为：类的适配器模式(继承) & 对象的适配器模式(代理)
     * 解决的问题
     * 原本由于接口不兼容而不能一起工作的那些类可以在一起工作
     * <p>
     * android源码中的使用:如 listView的Adapter
     */
    private void methodAdapterPattern() {
        //1.类的适配器模式
        Phone phone = new Phone();
        phone.setVoltAdapter(new Volt220To5Adapter());
        //2.对象的适配器模式
        phone.setVoltAdapter(new Volt5Adapter(new Volt220()) {
            @Override
            public int getVolt() {
                return getVoltage() / 44 + 1;
            }
        });
    }

    //场景:手机充电需要5V输入,而家用电输出22V,那么充电器就是一个适配器

    class Phone {
        public void setVoltAdapter(VoltAdapter adapter) {
            LjyLogUtil.i(String.format("手机充电:%sV", adapter.getVolt()));
        }
    }

    abstract class Volt {
        public abstract int getVoltage();
    }

    class Volt220 extends Volt {
        public int getVoltage() {
            return 220;
        }
    }

    interface VoltAdapter {
        int getVolt();
    }

    //1.类的适配器模式:
    class Volt220To5Adapter extends Volt220 implements VoltAdapter {

        @Override
        public int getVolt() {
            int temp = getVoltage() / 44;
            return temp;
        }
    }

    //2.对象的适配器模式
    abstract class Volt5Adapter implements VoltAdapter {

        private final Volt volt;

        public Volt5Adapter(Volt volt) {
            this.volt = volt;
        }

        public int getVoltage() {
            return volt.getVoltage();
        }

        @Override
        abstract public int getVolt();
    }

    /**
     * 策略模式
     * 3.1 优点
     * a.策略类之间可以自由切换
     * 由于策略类都实现同一个接口，所以使它们之间可以自由切换。
     * b.易于扩展
     * 增加一个新的策略只需要添加一个具体的策略类即可，基本不需要改变原有的代码，符合“开闭原则“
     * c.避免使用多重条件选择语句（if else），充分体现面向对象设计思想。
     * 3.2 缺点
     * a.客户端必须知道所有的策略类，并自行决定使用哪一个策略类。
     * b.策略模式将造成产生很多策略类，可以通过使用享元模式在一定程度上减少对象的数量。
     * <p>
     * android源码中的使用:如 动画中的插值器
     */
    private void methodStrategyPattern() {
        //普通方法:
        PriceCalculator priceCalculator = new PriceCalculator();
        LjyLogUtil.i("公交16公里:" + priceCalculator.calculatePrice(16, PriceCalculator.TYPE_BUS));
        LjyLogUtil.i("地铁32公里:" + priceCalculator.calculatePrice(32, PriceCalculator.TYPE_SUBWAY));
        //那么上面的如果要增加一种出租车计价方法呢?
        //策略模式
        setStrategy(new BusStrategy());
        LjyLogUtil.i("公交16公里:" + calculatePrice(16));
        setStrategy(new SubwayStrategy());
        LjyLogUtil.i("地铁32公里:" + calculatePrice(32));
        setStrategy(new TaxiStrategy());
        LjyLogUtil.i("出租10公里:" + calculatePrice(10));

    }


    CalculateStrategy mStrategy;

    public void setStrategy(CalculateStrategy strategy) {
        mStrategy = strategy;
    }

    public int calculatePrice(int km) {
        return mStrategy.calculatePrice(km);
    }

    //以公交地铁分段计价为例:
    //方式1:
    class PriceCalculator {
        //公交类型
        private static final int TYPE_BUS = 1;
        //地铁类型
        private static final int TYPE_SUBWAY = 2;

        //计算公交价钱
        private int busPrice(int km) {
            int extraTotal = km - 10;
            int extraFactor = extraTotal / 5;
            int fraction = extraTotal % 5;
            int price = 2 + extraFactor * 2;
            return km <= 0 ? 0 : fraction > 0 ? price + 2 : price;
        }

        //计算地铁价钱
        private int subwayPrice(int km) {
            if (km <= 0) {
                return 0;
            } else if (km <= 6) {
                return 3;
            } else if (km < 12) {
                return 4;
            } else if (km < 22) {
                return 5;
            } else if (km < 32) {
                return 6;
            }
            return 7;
        }

        int calculatePrice(int km, int type) {
            if (type == TYPE_BUS) {
                return busPrice(km);
            } else if (type == TYPE_SUBWAY) {
                return subwayPrice(km);
            }
            return 0;
        }
    }

    //方式2:
    public interface CalculateStrategy {
        //按距离计算价格
        int calculatePrice(int km);
    }

    public class BusStrategy implements CalculateStrategy {

        @Override
        public int calculatePrice(int km) {
            int extraTotal = km - 10;
            int extraFactor = extraTotal / 5;
            int fraction = extraTotal % 5;
            int price = 2 + extraFactor * 2;
            return km <= 0 ? 0 : fraction > 0 ? price + 2 : price;
        }
    }

    public class SubwayStrategy implements CalculateStrategy {

        @Override
        public int calculatePrice(int km) {
            if (km <= 0) {
                return 0;
            } else if (km <= 6) {
                return 3;
            } else if (km < 12) {
                return 4;
            } else if (km < 22) {
                return 5;
            } else if (km < 32) {
                return 6;
            }
            return 7;
        }
    }

    public class TaxiStrategy implements CalculateStrategy {

        @Override
        public int calculatePrice(int km) {
            return 8 + (km - 5) * 2;
        }
    }


    /**
     * 抽象工厂模式
     * 提供一个创建一系列相关或相互依赖对象的接口，
     * 而无须指定它们具体的类；具体的工厂负责实现具体的产品实例
     * 与工厂方法模式最大的区别：
     * 抽象工厂中每个工厂可以创建多种类的产品；而工厂方法每个工厂只能创建一类
     * 优点:
     * 降低耦合,抽象工厂模式将具体产品的创建延迟到具体工厂的子类中
     * 新增一种产品类时，只需要增加相应的具体产品类和相应的工厂子类即可
     * 每个具体工厂类只负责创建对应的产品
     * 不使用静态工厂方法，可以形成基于继承的等级结构。
     * <p>
     * 对于新的产品种类符合开-闭原则，
     * 对于新的产品族不符合开-闭原则；
     * 这一特性称为开-闭原则的倾斜性。
     * (如下例子,就是可以方便的创建ADBCDEFG等不同的monky和dunky,但不能创建其他种类的动物)
     */
    private void methodAbstractFactoryPattern() {
        AnimalFactoryA factoryA = new AnimalFactoryA();
        factoryA.newMonky().show();
        factoryA.newDunky().show();
        AnimalFactoryB factoryB = new AnimalFactoryB();
        factoryB.newMonky().show();
        factoryB.newDunky().show();

        //下面的方法更抽象,但是应该属于工厂方法模式或者简单工厂模式,
        // 因为它需要知道要创建的具体实现类,耦合度高
//        AnimFactory animFactory = new AnimFactory();
//        animFactory.getAnimal(MonkyA.class).show();
//        animFactory.getAnimal(MonkyB.class).show();
//        animFactory.getAnimal(MonkyC.class).show();
//        animFactory.getAnimal(DunkyA.class).show();
//        animFactory.getAnimal(DunkyB.class).show();
//        animFactory.getAnimal(DunkyC.class).show();
    }

    public class AnimFactory {
        public <T extends Animal> T getAnimal(Class<T> c) {
            Animal animal = null;
            try {
                animal = (Animal) Class.forName(c.getName()).newInstance();
                //java.lang.InstantiationException: java.lang.Class<com.ljy.ljyutils.activity.DesignPatternActivity$MonkyA> has no zero argument constructor
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            LjyLogUtil.i("animal:" + animal);
            return (T) animal;
        }
    }


    abstract class Animal {
        public abstract void show();
    }

    abstract class Monky extends Animal {
        @Override
        public abstract void show();
    }

    abstract class Dunky extends Animal {
        @Override
        public abstract void show();
    }

    abstract class AnimalFactory {
        public abstract Monky newMonky();

        public abstract Dunky newDunky();
    }

    class MonkyA extends Monky {

        @Override
        public void show() {
            LjyLogUtil.i("MonkyA:齐天大圣孙悟空");
        }
    }

    class DunkyA extends Dunky {

        @Override
        public void show() {
            LjyLogUtil.i("DunkyA:阿凡驴的提");
        }
    }

    class AnimalFactoryA extends AnimalFactory {

        @Override
        public Monky newMonky() {
            return new MonkyA();
        }

        @Override
        public Dunky newDunky() {
            return new DunkyA();
        }
    }

    class MonkyB extends Monky {

        @Override
        public void show() {
            LjyLogUtil.i("MonkyB:六耳弥猴桃");
        }
    }

    class DunkyB extends Dunky {

        @Override
        public void show() {
            LjyLogUtil.i("DunkyB:赶集的小毛驴");
        }
    }

    class AnimalFactoryB extends AnimalFactory {

        @Override
        public Monky newMonky() {
            return new MonkyB();
        }

        @Override
        public Dunky newDunky() {
            return new DunkyB();
        }
    }

    //增加产品类c时:
    class MonkyC extends Monky {

        @Override
        public void show() {
            LjyLogUtil.i("MonkyC:第三只猴子");
        }
    }

    class DunkyC extends Dunky {

        @Override
        public void show() {
            LjyLogUtil.i("DunkyC:第三只驴");
        }
    }

    class AnimalFactoryC extends AnimalFactory {

        @Override
        public Monky newMonky() {
            return new MonkyC();
        }

        @Override
        public Dunky newDunky() {
            return new DunkyC();
        }
    }


    /**
     * 工厂方法模式，又称工厂模式、多态工厂模式和虚拟构造器模式
     * <p>
     * 简单工厂的弊端:
     * 1.工厂类集中了所有实例（产品）的创建逻辑，一旦这个工厂不能正常工作，整个系统都会受到影响
     * 2.违背“开放 - 关闭原则”，一旦添加新产品就不得不修改工厂类的逻辑，这样就会造成工厂逻辑过于复杂
     * 3.简单工厂模式由于使用了静态工厂方法，静态方法不能被继承和重写，会造成工厂角色无法形成基于继承的等级结构
     * <p>
     * 工厂方法模式如何解决了上述问题:
     * 工厂方法模式把具体产品的创建推迟到工厂类的子类（具体工厂）中，
     * 此时工厂类不再负责所有产品的创建，而只是给出具体工厂必须实现的接口
     */
    private void methodFactoryMethod() {

        DuckAFactory duckAFactory = new DuckAFactory();
        duckAFactory.newDuck().show();

        DuckBFactory duckBFactory = new DuckBFactory();
        duckBFactory.newDuck().show();

    }

    abstract class Duck {
        public abstract void show();
    }

    static abstract class DuckFactory {
        public abstract Duck newDuck();
    }

    class DuckA extends Duck {

        @Override
        public void show() {
            LjyLogUtil.i("DuckA:呀呀呀~");
        }
    }

    class DuckB extends Duck {

        @Override
        public void show() {
            LjyLogUtil.i("DuckB:呀呀呀~");
        }
    }

    class DuckAFactory extends DuckFactory {

        @Override
        public Duck newDuck() {
            return new DuckA();
        }
    }

    class DuckBFactory extends DuckFactory {

        @Override
        public Duck newDuck() {
            return new DuckB();
        }
    }


    /**
     * 简单工厂模式
     * 又叫静态方法模式（因为工厂类定义了一个静态方法）
     * 将“类实例化的操作”与“使用对象的操作”分开，
     * 让使用者不用知道具体参数就可以实例化出所需要的“产品”类，
     * 从而避免了在客户端代码中显式指定，实现了解耦。
     */
    private void methodSimpleFactoryPattern() {
        CatFactory.newCat(CatFactory.CatType.TYPE_A).show();
        CatFactory.newCat(CatFactory.CatType.TYPE_B).show();
//        try {
//            CatFactory.newCat(2).show();
//        } catch (NullPointerException e) {
//            LjyLogUtil.i("没有这一类Cat");
//        }
    }

    static abstract class Cat {
        public abstract void show();
    }

    static class CatA extends Cat {

        @Override
        public void show() {
            LjyLogUtil.i("catA:喵喵喵~");
        }
    }

    static class CatB extends Cat {

        @Override
        public void show() {
            LjyLogUtil.i("catB:喵喵喵~");
        }
    }

    static class CatFactory {
        public enum CatType {
            TYPE_A,
            TYPE_B
        }

        public static Cat newCat(CatType catType) {
            switch (catType) {
                case TYPE_A:
                    return new CatA();
                case TYPE_B:
                    return new CatB();
                default:
                    return null;
            }
        }
    }


    /**
     * 单例模式
     * 实现1个类只有1个实例化对象 & 提供一个全局访问点
     * <p>
     * android源码中的体现:
     * LayoutInflater.from(context)
     * context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
     * Calendar.getInstance()
     */
    private void methodSingleton() {
        Dog dog1 = Dog.getInstance();
        Dog dog2 = Dog.getInstance();
        String info = "dog1.hashCode:" + dog1.hashCode() + "\ndog2.hashCode:" + dog2.hashCode();
        LjyLogUtil.i(info);
        dog1.doSomething();
        A a = A.getInstance();
        a.doSomething();
        B b = B.getInstance();
        b.doSomething();
        C c = C.getInstance();
        c.doSomething();
        D d = D.INSTANCE;
        d.doSomething();
    }

    //通过静态内部类实现
    public static class Dog {

        private Dog() {

        }

        public static Dog getInstance() {
            return DogHolder.dogInstance;
        }

        private static class DogHolder {
            private static Dog dogInstance = new Dog();
        }

        public void doSomething() {
            LjyLogUtil.i(String.format("%s: do sth....", this.getClass().getName()));
        }
    }

    //饿汉式
    public static class A {
        private static A a = new A();

        private A() {
        }

        public static A getInstance() {
            return a;
        }

        public void doSomething() {
            LjyLogUtil.i(String.format("%s: do sth....", this.getClass().getName()));
        }
    }

    //懒汉式
    public static class B {
        private static B b;

        private B() {
        }

        public static synchronized B getInstance() {
            if (b == null) {
                b = new B();
            }
            return b;
        }

        public void doSomething() {
            LjyLogUtil.i(String.format("%s: do sth....", this.getClass().getName()));
        }
    }

    //DCL ( Double Check Lock)
    public static class C implements Serializable {
        private static C c;

        private C() {
        }

        public static C getInstance() {
            if (c == null) {
                synchronized ((C.class)) {
                    if (c == null) {
                        c = new C();
                    }
                }
            }
            return c;
        }

        public void doSomething() {
            LjyLogUtil.i(String.format("%s: do sth....", this.getClass().getName()));
        }

        private Object readResolve() throws ObjectStreamException {
            return c;
        }

    }

    //枚举单例,写法简单,线程安全,并且保证任何情况都是单例,
    // 上面的其他实现方法单例在反序列化(提供了一个特别的钩子函数)时会创建新的单例,
    // 解决方法是如C中实现readResolve方法返回单例对象
    public enum D {
        INSTANCE;

        public void doSomething() {
            LjyLogUtil.i(String.format("%s: do sth....", this.getClass().getName()));
        }
    }

    //使用容器实现单例(可以管理多种类型的单例)
    public static class SingletonManager {

        private static Map<String, Object> instanceMap = new HashMap<>();

        private SingletonManager() {
        }

        public static void registerInstance(String key, Object instance) {
            if (!instanceMap.containsKey(key)) {
                instanceMap.put(key, instance);
            }
        }

        public static Object getInstance(String key) {
            return instanceMap.get(key);
        }
    }


}
