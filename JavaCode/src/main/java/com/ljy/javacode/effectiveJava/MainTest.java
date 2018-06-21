package com.ljy.javacode.effectiveJava;

import com.ljy.javacode.effectiveJava.bean.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
                default:
                    break;
            }
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
        swap(dst,0,1);
        System.out.println("dst-->" + dst.toString());

        Favorites f=new Favorites();
        f.put(String.class,"java");
        f.put(Integer.class,0x16);//按16进制存
        f.put(Class.class,Favorites.class);
        String fStr=f.get(String.class);
        int fInt=f.get(Integer.class);//按10进制取
        Class<?> fFavor=f.get(Class.class);
        System.out.printf("%s %x %s%n",fStr,fInt,fFavor.getName());

    }

    public static class Favorites{
        private Map<Class<?> ,Object> favorites=new HashMap<>();

        public <T> void put(Class<T> type,T instance){
            if (type==null)
                throw new NullPointerException("type is null");
            favorites.put(type,instance);
        }

        public <T> T get(Class<T> type){
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
        list.set(i,list.set(j,list.get(i)));
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
