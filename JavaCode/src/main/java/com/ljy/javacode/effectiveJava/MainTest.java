package com.ljy.javacode.effectiveJava;

import com.ljy.javacode.effectiveJava.bean.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

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
                    testFanXing();
                    break;
                case "enum":
                    testEnum(Sex.MAN, Animal.DOG);
                    break;
                default:
                    break;
            }
        }

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

    private static <E> void testFanXing() {
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
