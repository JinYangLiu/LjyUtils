package com.ljy.ljyutils.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjyLogUtil;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

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
        }
        mTextViewShow.setText(LjyLogUtil.getAllLogMsg());
        LjyLogUtil.setAppendLogMsg(false);
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
        SmartControl smartControl=new SmartControl();
        smartControl.allOn();
        smartControl.allOff();

        startActivity(null);
        mContext.sendBroadcast(null);
        mContext.bindService(null,null,0);
    }

    //以智能家居,统一管理家电为例
    public class Light{
        public void on(){
            LjyLogUtil.i("开灯...");
        }

        public void off(){
            LjyLogUtil.i("关灯...");
        }
    }

    public class Televison{
        public void on(){
            LjyLogUtil.i("开电视...");
        }

        public void off(){
            LjyLogUtil.i("关电视...");
        }
    }

    public class Aircondition{
        public void on(){
            LjyLogUtil.i("开空调...");
        }

        public void off(){
            LjyLogUtil.i("关空调...");
        }
    }

    public class SmartControl{
        private Light mLight=new Light();
        private Televison mTelevison=new Televison();
        private Aircondition mAircondition=new Aircondition();
        public void allOn(){
            lightOn();
            tvOn();
            airOn();
        }

        public void allOff(){
            lightOff();
            tvOff();
            airOff();
        }

        public void lightOn(){
            mLight.on();
        }

        public void lightOff(){
            mLight.off();
        }

        public void tvOn(){
            mTelevison.on();
        }

        public void tvOff(){
            mTelevison.off();
        }

        public void airOn(){
            mAircondition.on();
        }

        public void airOff(){
            mAircondition.off();
        }
    }

    /**
     * 建造者模式:
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
        AnimFactory animFactory = new AnimFactory();
        animFactory.getAnimal(MonkyA.class).show();
        animFactory.getAnimal(MonkyB.class).show();
        animFactory.getAnimal(MonkyC.class).show();
        animFactory.getAnimal(DunkyA.class).show();
        animFactory.getAnimal(DunkyB.class).show();
        animFactory.getAnimal(DunkyC.class).show();
    }

    class AnimFactory {
        public <T extends Animal> T getAnimal(Class<T> c) {
            T animal = null;
            try {
                animal = (T) Class.forName(c.getName()).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return animal;
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
