<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [java多线程sleep与wait区别](#java%E5%A4%9A%E7%BA%BF%E7%A8%8Bsleep%E4%B8%8Ewait%E5%8C%BA%E5%88%AB)
- [Collection和Collections的区别](#collection%E5%92%8Ccollections%E7%9A%84%E5%8C%BA%E5%88%AB)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->


#### java多线程sleep与wait区别

- sleep
    - 属于Thread类中
    - 是线程用来控制自身流程的
    - 线程不会释放对象锁
    - sleep()方法导致了程序暂停执行指定的时间，让出cpu给其他线程，但是他的监控状态依然保持着，
    当指定的时间到了又会自动恢复运行状态。在调用sleep()方法的过程中。
    - sleep必须捕获异常，而wait，notify和notifyAll不需要捕获异常。
- wait
    - 属于Object类
    - 用于不同线程之间的调度的
    - 线程会放弃对象锁
    - 与wait配套的方法还有notify和notifyAll
    - 而当调用wait()方法的时候, 线程会放弃对象锁，进入等待此对象的等待锁定池，只有针对此对象调用
    notify()方法后本线程才进入对象锁定池，准备获取对象锁进入运行状态
    - 由于wait函数的特殊意义，所以他是应该放在同步语句块中的，这样才有意义
    
#### Collection和Collections的区别

- java.util.Collection是一个集合接口
    - 它提供了对集合对象进行基本操作的通用接口方法
    - Collection接口在Java 类库中有很多具体的实现
    - Collection接口的意义是为各种具体的集合提供了最大化的统一操作方式
    ```` 
    Collection  
    ├List  
    │├LinkedList  
    │├ArrayList  
    │└Vector  
    │　└Stack  
    └Set 
- java.util.Collections是一个包装类
    - 它包含有各种有关集合操作的静态多态方法
    - 此类不能实例化，就像一个工具类，服务于Java的Collection框架。
  ````
    public class TestCollections {  
      public static void main(String args[]) {  
          //List是Collection接口的实现类 
          List list = new ArrayList();  
          double array[] = { 112, 111, 23, 456, 231 };  
          for (int i = 0; i < array.length; i++) {  
              list.add(new Double(array[i]));  
          }
          //调用Collections的方法进行排序
          Collections.sort(list);  
          for (int i = 0; i < array.length; i++) {  
              System.out.println(list.get(i));  
          }  
      }  
    }  
    
### java的二进制兼容性：
- 定义：一个类改变时，新版的类是否可以直接替换原来的类，却不至于损坏其他由不同厂商，作者开发的依赖于该类的组件
- 优势：
    1. java将二进制兼容性的粒度从整个库（如unix的.so库文件，windows的.dll库文件），细化到了单个的类（.class）
    2. java的二进制兼容性不需要有意识的去规划，而是一种与生具来的天性（.java-->.class）
    3. 传统的共享对象只针对函数名称，而java二进制兼容性考虑到类重载，函数签名（方法名+形参类型列表），返回值类型；
    4. java提供了更晚上的错误控制机制，版本不兼容会触发异常，但可以方便但捕获和处理
- 关键：延迟绑定（Late Binding），指java直到运行时才检查类，域，方法的名称，
    这意味着只要域，方法的名称（及类型）一样，类的主题可以任意替换（其实还与public，private，static，abstract等修饰符有关）
- 方法的兼容性：要注意重写对父类方法的覆盖；
（java用一种称为"虚拟方法调度"的技术判断要调用的方法体，它依据被调用的方法所在的实际实例来决定要使用的方法体，可以看作一种扩展的延迟绑定策略）        
- 域的兼容性：域不能覆盖    
````
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

        French french=new French();
        Language language=french;
        french.perform();
        language.perform();//调用实际实例的方法体
        System.out.println(french.greeting);
        System.out.println(language.greeting);//依赖于实例的类型

    }
    
    //输出结果如下：
    请输入要执行的方法名：testBinaryCompatibility
    To be or not to be.
    To be or not to be.
    Bon jour
    你好

````
    
       
    
