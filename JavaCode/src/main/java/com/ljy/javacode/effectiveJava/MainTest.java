package com.ljy.javacode.effectiveJava;

import com.ljy.javacode.effectiveJava.anno.Test;
import com.ljy.javacode.effectiveJava.bean.Person;
import com.ljy.javacode.structure.hash.HashTable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by ljy on 2018/6/15.
 */

public class MainTest {
    public static void main(String[] args) {

        System.out.print("请输入要执行的方法名：");
        Scanner sc = new Scanner(System.in);
        if (sc.hasNext()) {
            String param = sc.next();
            switch (param) {
                case "person":
                    Boolean.valueOf("true");
                    testPerson();
                    break;
                case "BinaryCompatibility":
                    //测试二进制兼容性
                    testBinaryCompatibility();
                    break;
                case "fanxing":
                    //泛型测试
                    testFanXing();
                    break;
                case "enum":
                    //枚举测试
                    testEnum(Sex.MAN, Animal.DOG);
                    break;
                case "anno":
                    //注解测试
                    testAnno();
                    break;
                case "method":
                    testMethod();
                    break;
                case "general":
                    testGeneral();
                    break;
                default:
                    break;
            }
        }

    }

    private static void testGeneral() {
        //需求：把listKey，listValue放到map中：
        List<String> listKey = new ArrayList<>();
        List<String> listValue = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        //iterator遍历集合,像下面这样写会有什么问题呢？
        for (Iterator<String> i = listKey.iterator(); i.hasNext(); )
            for (Iterator<String> j = listValue.iterator(); j.hasNext(); )//这一层我们一般是复制上面一行，容易忘记修改i.hasNext()
                map.put(i.next(), j.next());//这一行执行了太多此的j.next()
        //优化：用for-each代替
        for (String key : listKey)
            for (String value : listValue)
                map.put(key, value);
        System.out.println("模拟随机出Integer.MIN_VALUE情况：" + Math.abs(Integer.MIN_VALUE) % 4);

//        double num = 1.00;
//        int count = 0;
//        for (double i = .10; num >= i; i += .10) {
//            num-=i;
//            count++;
//            System.out.println("num:"+num);
//        }
//        System.out.println("count:"+count);
        final BigDecimal temp=new BigDecimal(".10");
        int count=0;
        BigDecimal num=new BigDecimal("1.00");
        for (BigDecimal i=temp;num.compareTo(i)>=0;i=i.add(temp)){
            num=num.subtract(i);
            System.out.println("num:"+num);
            count++;
        }
        System.out.println("count:"+count);

    }

    public static class ThreadLocal{
        private ThreadLocal(){}
        private static Map<String,Object> map=new HashMap<>();
        public static void set(String key,Object value){
            map.put(key,value);
        }

        public static Object get(String key){
            return map.get(key);
        }
    }

    public static class ThreadLocal2{
        private ThreadLocal2(){}
        private static Map<Key,Object> map=new HashMap<>();

        public static class Key{}

        public static Key getKey(){
            return new Key();
        }

        public static void set(Key key,Object value){
            map.put(key,value);
        }

        public static Object get(Key key){
            return map.get(key);
        }
    }

    public static final Random rnd = new Random();

    /**
     * 求n以内的随机数
     */
    public static int random(int n) {
        return Math.abs(rnd.nextInt()) % n;
    }

    private static void testMethod() {

        Date start = new Date(1991 - 1900, 2 - 1, 14);
        Date end = new Date(2018 - 1900, 6 - 1, 1);
        Period period = new Period(start, end);
        end.setYear(1970 - 1900);
        System.out.println(period.toString());

        Anim[] anims = {new Anim(), new Cat(), new TomCat()};
        for (int i = 0; i < anims.length; i++) {
            System.out.println("重写.name:" + anims[i].name());
            System.out.println("重载.name:" + Anim.name(anims[i]));
        }

        System.out.println("重载.name:" + Anim.name(new Anim()));
        System.out.println("重载.name:" + Anim.name(new Cat()));
        System.out.println("重载.name:" + Anim.name(new TomCat()));

        System.out.println("Anim.name:" + Anim.name);
        System.out.println("Cat.name:" + Cat.name);


        Set<Integer> set = new TreeSet<>();
        List<Integer> list = new ArrayList<>();
        for (int i = -3; i < 3; i++) {
            set.add(i);
            list.add(i);
        }
        System.out.println("111-->" + set + "_" + list);
        for (int i = 0; i < 3; i++) {//期望去掉0，1，2
            set.remove(i);
            list.remove(i);
            list.remove(Integer.valueOf(i));
        }
        System.out.println("222-->" + set + "_" + list);
    }

    public void foo() {
    }

    public void foo(int a1) {
    }

    public void foo(int a1, int a2) {
    }

    public void foo(int a1, int a2, int a3) {
    }

    public void foo(int a1, int a2, int a3, int... ans) {
    }

    public static class Anim {
        public static String name = "ANIM";

        public String name() {
            return "animal";
        }

        public static String name(Anim anim) {
            return "Anim";
        }

        public static String name(Cat cat) {
            return "Cat";
        }

        public static String name(TomCat tomCat) {
            return "TomCat";
        }
    }

    public static class Cat extends Anim {
        public static String name = "CAT";

        @Override
        public String name() {
            return "cat";
        }
    }

    public static class TomCat extends Cat {
        @Override
        public String name() {
            return "tom cat";
        }


    }

    public static final class Period {
        private final Date start;
        private final Date end;

        public Period(Date start, Date end) {
            this.start = new Date(start.getTime());
            this.end = new Date(end.getTime());
            //注意应先进行保护性拷贝，再对拷贝后的对象进行有效性检查，可以避免"危险阶段"期间从另一个线程改变类的参数
            //危险阶段：指从检查参数开始，到拷贝参数之间的时间段
            if (this.start.compareTo(this.end) > 0)
                throw new IllegalArgumentException("start 不应该在 end 之后");
        }

        public Date getStart() {
            return new Date(start.getTime());
        }

        public Date getEnd() {
            return new Date(end.getTime());
        }

        private static final SimpleDateFormat dateFormat;

        static {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }

        @Override
        public String toString() {
            return "Period{" +
                    "start=" + dateFormat.format(start) +
                    ", end=" + dateFormat.format(end) +
                    '}';
        }
    }

    private static void testAnno() {
        Class testClass = MainTest.class;
        for (Method m : testClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Test.class)) {
                //System.out.println("methodName:"+m.getName()+"_Anno:"+m.getAnnotation(Test.class).toString());
                try {
                    m.invoke(null);
                } catch (Throwable throwable) {
                    Throwable exc = throwable.getCause();
                    Class<? extends Exception>[] excTypes = m.getAnnotation(Test.class).value();
                    for (Class<? extends Exception> excType : excTypes) {
                        if (excType.isInstance(exc)) {
                            System.out.println("触发了" + excType.getSimpleName() + ",msg:" + exc.getLocalizedMessage());
                        }
                    }

                }
            }
        }
    }

    @Test(NullPointerException.class)
    private static void m1() {
        System.out.println("m1");
    }

    @Test(NullPointerException.class)
    private static void m2() {
        throw new NullPointerException("空指针了");
    }

    @Test({NullPointerException.class, IndexOutOfBoundsException.class})
    private static void m3() {
        throw new RuntimeException("报错了哦");
    }

    @Test(NullPointerException.class)
    private void m4() {
        System.out.println("m4");
    }

    private static void testEnum(Sex sex, Animal animal) {
        System.out.println(sex.toString());// 打印 MAN
        System.out.println(animal.toString());// 打印 DOG
        int foots = animal.getFoots();
        String info = animal.getInfo();
        int result = animal.compareTo(Animal.CAT);

        System.out.println("MONDAY.numberOfDays:" + PayrollDay.MONDAY.numberOfDays());
        System.out.println("SUNDAY.numberOfDays:" + PayrollDay.SUNDAY.numberOfDays());

        Text_1 text_1 = new Text_1();
        text_1.applyStyles(Text_1.STYLE_BOLD | Text_1.STYLE_ITALIC);

        Text_2 text_2 = new Text_2();
        text_2.applyStyles(EnumSet.of(Text_2.Style.BOLD, Text_2.Style.ITALIC));

        //对花园中的花进行分类
        Flower[] garden = {
                new Flower("兰", Flower.Type.SPRING),
                new Flower("竹", Flower.Type.SUMMER),
                new Flower("菊", Flower.Type.AUTUMN),
                new Flower("梅", Flower.Type.WINTER),
                new Flower("桃", Flower.Type.SPRING),
                new Flower("杏", Flower.Type.SUMMER),
                new Flower("梨", Flower.Type.AUTUMN),
        };

        Set<Flower>[] flowersByType = (Set<Flower>[]) new Set[Flower.Type.values().length];

        for (int i = 0; i < flowersByType.length; i++)
            flowersByType[i] = new HashSet<>();

        for (Flower f : garden)
            flowersByType[f.type.ordinal()].add(f);

        //print
        for (int i = 0; i < flowersByType.length; i++)
            System.out.println(flowersByType[i]);


        //EnumMap:
        //init map
        Map<Flower.Type, Set<Flower>> flowerByType = new EnumMap<>(Flower.Type.class);

        for (Flower.Type t : Flower.Type.values())
            flowerByType.put(t, new HashSet<Flower>());
        //分类
        for (Flower f : garden)
            flowerByType.get(f.type).add(f);

        System.out.println(flowerByType);


    }

    //花
    public static class Flower {
        //按花季分类：春夏秋冬
        public enum Type {
            SPRING, SUMMER, AUTUMN, WINTER
        }

        private final String name;//花名
        private final Type type;//花季

        public Flower(String name, Type type) {
            this.name = name;
            this.type = type;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static class Text_1 {
        public static final int STYLE_BOLD = 1 << 0;//1,粗体
        public static final int STYLE_ITALIC = 1 << 1;//2,斜体
        public static final int STYLE_UNDERLINE = 1 << 2;//4,下划线
        public static final int STYLE_STRIKETHROUGH = 1 << 3;//8,删除线

        // parameter is bitwise OR of zero or more STYLE_ constants
        public void applyStyles(int styles) {
        }
    }

    public static class Text_2 {
        public enum Style {BOLD, ITALIC, UNDERLINE, STRIKETHROUGH}

        public void applyStyles(Set<Style> styles) {
            //do something
        }
    }

    public enum PayrollDay {
        MONDAY(PayType.WEEKDAY),
        TUESDAY(PayType.WEEKDAY),
        WEDNESDAY(PayType.WEEKDAY),
        THURSDAY(PayType.WEEKDAY),
        FRIDAY(PayType.WEEKDAY),
        SATURDAY(PayType.WEEKEND),
        SUNDAY(PayType.WEEKEND);
        private final PayType payType;

        PayrollDay(PayType payType) {
            this.payType = payType;
        }

        public int numberOfDays() {
            return ordinal() + 1;
        }

        private enum PayType {
            WEEKDAY {
                @Override
                double overtimeDay(double hrs, double payRate) {
                    return hrs <= HOURS_PER_SHIFT ? 0 : (hrs - HOURS_PER_SHIFT) * payRate / 2;
                }
            },
            WEEKEND {
                @Override
                double overtimeDay(double hrs, double payRate) {
                    return hrs * payRate / 2;
                }
            };

            private static final int HOURS_PER_SHIFT = 8;

            abstract double overtimeDay(double hrs, double payRate);

            double pay(double hoursWorked, double payRate) {
                double basePay = hoursWorked * payRate;
                return basePay + overtimeDay(hoursWorked, payRate);
            }
        }
    }

    //一个代表加减乘除等运算符的枚举
    public enum Operation1 {
        PLUS, MINUS, TIMES, DIVIDE;

        double apply(double x, double y) {
            switch (this) {
                case PLUS:
                    return x + y;
                case MINUS:
                    return x - y;
                case TIMES:
                    return x * y;
                case DIVIDE:
                    return x / y;
            }
            throw new AssertionError("Unknown operation: " + this);
        }
    }


    //运算符接口
    public interface Operation {
        double apply(double x, double y);
    }

    //基础运算符：加减乘除
    public enum BaseOperation implements Operation {
        PLUS("+") {
            public double apply(double x, double y) {
                return x + y;
            }
        },
        MINUS("-") {
            public double apply(double x, double y) {
                return x - y;
            }
        },
        TIMES("*") {
            public double apply(double x, double y) {
                return x * y;
            }
        },
        DIVIDE("/") {
            public double apply(double x, double y) {
                return x / y;
            }
        };

        private final String symbol;//特定于常量的数据

        BaseOperation(String symbol) {
            this.symbol = symbol;
        }


        //重写toString
        @Override
        public String toString() {
            return symbol;
        }
    }

    //扩展的运算符：求幂求余
    public enum ExtendOperation implements Operation {

        EXP("^") {
            @Override
            public double apply(double x, double y) {
                return Math.pow(x, y);
            }
        },
        REMAINDER("%") {
            @Override
            public double apply(double x, double y) {
                return x % y;
            }
        };

        private final String symbol;//特定于常量的数据

        ExtendOperation(String symbol) {
            this.symbol = symbol;
        }


        //重写toString
        @Override
        public String toString() {
            return symbol;
        }
    }

    public enum Sex {MAN, WOMAN}

    public enum Animal {
        CAT("喵喵喵", 4),
        DOG("汪汪汪", 4);

        private final String info;
        private final int foots;//几条腿

        Animal(String info, int foots) {
            this.info = info;
            this.foots = foots;
        }

        public String getInfo() {
            return info;
        }

        public int getFoots() {
            return foots;
        }
    }

    public static void testFanXing() {
        printList(Arrays.asList(new int[]{1, 2, 3}));
        printList(mList);
//        printObjectList(mList);
        printStringList(mList);
        printAnyList(mList);

        //fails at runtime(运行时报错)
//        Object[] objArr=new Long[10];
//        objArr[0]="Hello";

        //won't compile（不能编译通过:Incompatible types,required...,find...）
//        List<Object> objList=new ArrayList<Long>();
//        objList.add("World");
        List<String> l1 = new ArrayList<String>();
        List<Integer> l2 = new ArrayList<Integer>();

        System.out.println(l1.getClass() == l2.getClass());
//        new List<E>[12];
//        new List<String>[12];
//        new E[12];
//        new List<?>[12];
//
        class Stack<T> {
            private T[] arr;
            private int top = -1;

            @SuppressWarnings("unchecked")
            public Stack() {
                arr = (T[]) new Object[10];
            }

            public T pop() {
                T result = arr[top--];
                return result;
            }

            //泛型不可变
            public void pushAll_1(Collection<T> list) {
                for (T item : list) {
                    arr[++top] = item;
                }
            }

            //有限制的通配符类型(上限)
            public void pushAll_2(Collection<? extends T> list) {
                for (T item : list) {
                    arr[++top] = item;
                }
            }

            public void popAll_1(Collection<T> dst) {
                while (top >= 0) {
                    dst.add(pop());
                }
            }

            //下限
            public void popAll_2(Collection<? super T> dst) {
                while (top >= 0) {
                    dst.add(pop());
                }
            }

            public void display() {
                System.out.println("Stack-->" + Arrays.asList(arr).toString());
            }


        }

        //调用
        Stack<Number> numberStack = new Stack<>();
        List<Integer> stringList = Arrays.asList(new Integer[]{11, 12, 13});
        // objectStack.pushAll_1(stringList);//报错
        numberStack.pushAll_2(stringList);
        numberStack.display();
        List<Object> dst = new ArrayList<>();
        // numberStack.popAll_1(dst);//报错
        numberStack.popAll_2(dst);
        System.out.println("dst-->" + dst.toString());
        swap(dst, 0, 1);
        System.out.println("dst-->" + dst.toString());

        Favorites f = new Favorites();
        f.put(String.class, "java");
        f.put(Integer.class, 0x16);//按16进制存
        f.put(Class.class, Favorites.class);
        String fStr = f.get(String.class);
        int fInt = f.get(Integer.class);//按10进制取
        Class<?> fFavor = f.get(Class.class);
        System.out.printf("%s %x %s%n", fStr, fInt, fFavor.getName());

    }

    public static class Favorites {
        private Map<Class<?>, Object> favorites = new HashMap<>();

        public <T> void put(Class<T> type, T instance) {
            if (type == null)
                throw new NullPointerException("type is null");
            favorites.put(type, instance);
        }

        public <T> T get(Class<T> type) {
            return type.cast(favorites.get(type));
        }
    }

    //使用无限制的通配符，在公共API中这种写法更好，因为简单
    //一般来说如果类型参数只在方法声明中出现一次，就可以用通配符取代它
    public static void swap(List<?> list, int i, int j) {
        // list.set(i,list.set(j,list.get(i)));//编译时报错，不能将任何null之外的值放到List<?>中
        swapHelper(list, i, j);
    }

    //使用无限制的类型参数
    private static <E> void swapHelper(List<E> list, int i, int j) {
        list.set(i, list.set(j, list.get(i)));
    }

    public static Set union1(Set s1, Set s2) {
        Set result = new HashSet(s1);//警告：unchecked call...
        result.addAll(s2);//警告：unchecked call...
        return result;
    }

    public static <E> Set<E> union2(Set<E> s1, Set<E> s2) {
        Set<E> result = new HashSet<>(s1);
        result.addAll(s2);
        return result;
    }

    private static List<String> mList = Arrays.asList(new String[]{"aa", "bb", "cc"});

    public static void printList(List list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).toString());
        }
    }

    public static void printObjectList(List<Object> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).toString());
        }
    }

    public static void printStringList(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).toString());
        }
    }

    public static void printAnyList(List<?> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).toString());
        }
    }

    private static void testBinaryCompatibility() {
        class Language {
            String greeting = "你好";

            void perform() {
                System.out.println("白日依山尽");
            }
        }

        class French extends Language {
            String greeting = "Bon jour";

            void perform() {
                System.out.println("To be or not to be.");
            }
        }

        French french = new French();
        Language language = french;
        french.perform();
        language.perform();
        System.out.println(french.greeting);
        System.out.println(language.greeting);

    }

    private static void testPerson() {
        //Class.newInstance()
        try {
            Person p = Person.class.newInstance();
            //非必要时不推荐使用这种写法
            //1。要处理各种异常，既不方便也不雅观
            //2。破坏了编译时对异常检查
            //3。无法知道Person.class是否存在无参的构造方法
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //通过构造器
        Person person1 = new Person() {
            @Override
            public void printHello() {
                super.printHello();
                System.out.println("world");
            }
        };
        person1.printHello();
        Person person2 = new Person("ljy");
        Person person3 = new Person("ljy", 18, 170);
        //通过工厂方法
        Person person4 = Person.person();
        Person person5 = Person.personWithName("ljy");
        Person person6 = Person.personWithNameAndAge("LLL", 55);
        //通过建造者
        Person person = new Person.Builder()
                .setName("ljy")
                .setNickname("Mr.L")
                .setAge(18)
                .setHeight(170)
                .setSex("Man")
                .setEmail("123321@qq.com")
                .setFather(person6)
                .build();
        System.out.println(person.toString());
        System.out.println("person.equals(person3): " + person.equals(person3));
        try {
            Person p = person.clone();
            p.hashCode();
            System.out.println("clone: " + p.toString());
            System.out.println("p==person: " + (p == person));
            System.out.println("person==person: " + (person == person));
            System.out.println("getClass: " + (p.getClass() == person.getClass()));
            System.out.println("p.equals(person): " + p.equals(person));
            person6.setAge(66);
            System.out.println("clone: " + p.toString());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        System.out.println(person.toString());
    }
}
