package com.ljy.ljyutils.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjyLogUtil;

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
        }
        mTextViewShow.setText(LjyLogUtil.getAllLogMsg());
        LjyLogUtil.setAppendLogMsg(false);
    }

    /**
     * 适配器模式
     * 定义一个包装类，用于包装不兼容接口的对象
     * 适配器模式的形式分为：类的适配器模式(继承) & 对象的适配器模式(代理)
     * 解决的问题
     * 原本由于接口不兼容而不能一起工作的那些类可以在一起工作
     *
     */
    private void methodAdapterPattern() {
        //1.类的适配器模式
        Phone phone=new Phone();
        phone.setVoltAdapter(new Volt220To5Adapter());
        //2.对象的适配器模式
        phone.setVoltAdapter(new Volt5Adapter(new Volt220()) {
            @Override
            public int getVolt() {
                return getVoltage()/44+1;
            }
        });
    }

    //场景:手机充电需要5V输入,而家用电输出22V,那么充电器就是一个适配器

    class Phone {
        public void setVoltAdapter(VoltAdapter adapter){
            LjyLogUtil.i(String.format("手机充电:%sV",adapter.getVolt()));
        }
    }

    abstract class Volt{
        public abstract int getVoltage();
    }
    class Volt220 extends Volt{
        public int getVoltage() {
            return 220;
        }
    }

    interface VoltAdapter {
        int getVolt();
    }

    //1.类的适配器模式:
    class Volt220To5Adapter extends Volt220 implements VoltAdapter{

        @Override
        public int getVolt() {
            int temp=getVoltage()/44;
            return temp;
        }
    }

    //2.对象的适配器模式
    abstract class Volt5Adapter implements VoltAdapter{

        private final Volt volt;

        public Volt5Adapter(Volt volt){
            this.volt=volt;
        }

        public int getVoltage(){
            return volt.getVoltage();
        }

        @Override
        abstract public int getVolt() ;
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
        public abstract Animal newMonky();

        public abstract Animal newDunky();
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
        public Animal newMonky() {
            return new MonkyA();
        }

        @Override
        public Animal newDunky() {
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
        public Animal newMonky() {
            return new MonkyB();
        }

        @Override
        public Animal newDunky() {
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
        public Animal newMonky() {
            return new MonkyC();
        }

        @Override
        public Animal newDunky() {
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
     */
    private void methodSingleton() {
        Dog dog1 = Dog.getInstance();
        Dog dog2 = Dog.getInstance();
        String info = "dog1.hashCode:" + dog1.hashCode() + "\ndog2.hashCode:" + dog2.hashCode();
        mTextViewShow.setText(info);
        LjyLogUtil.i(info);
    }

    public static class Dog {

        private Dog() {

        }

        public static Dog getInstance() {
            return DogHolder.dogInstance;
        }

        private static class DogHolder {
            private static Dog dogInstance = new Dog();
        }
    }


}
