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

### String、StringBuffer、StringBuilder区别 
- String:字符串常量 不适用于经常要改变值得情况，每次改变相当于生成一个新的对象
- StringBuffer:字符串变量 （线程安全）
- StringBuilder:字符串变量（线程不安全） 确保单线程下可用，效率略高于StringBuffer

### 进程和线程的区别 
进程是cpu资源分配的最小单位，线程是cpu调度的最小单位。
进程之间不能共享资源，而线程共享所在进程的地址空间和其它资源。
一个进程内可拥有多个线程，进程可开启进程，也可开启线程。
一个线程只能属于一个进程，线程可直接使用同进程的资源,线程依赖于进程而存在。

### final，finally，finalize的区别 
final:修饰类、成员变量和成员方法，类不可被继承，成员变量不可变，成员方法不可重写
finally:与try...catch...共同使用，确保无论是否出现异常都能被调用到
finalize:类的方法,垃圾回收之前会调用此方法,子类可以重写finalize()方法实现对资源的回收

### Serializable 和Parcelable 的区别 
Serializable Java 序列化接口 在硬盘上读写 读写过程中有大量临时变量的生成，内部执行大量的i/o操作，效率很低。
Parcelable Android 序列化接口 效率高 使用麻烦 在内存中读写（AS有相关插件 一键生成所需方法） ，对象不能保存到磁盘中

### 静态属性和静态方法是否可以被继承？是否可以被重写？以及原因？ 
可继承 不可重写 而是被隐藏
如果子类里面定义了静态方法和属性，那么这时候父类的静态方法或属性称之为"隐藏"。如果你想要调用父类的静态方法和属性，直接通过父类名.方法或变量名完成。

### String为什么要设计成不可变的？ 
1. 字符串池的需求
字符串池是方法区（Method Area）中的一块特殊的存储区域。当一个字符串已经被创建并且该字符串在池中，该字符串的引用会立即
返回给变量，而不是重新创建一个字符串再将引用返回给变量。如果字符串不是不可变的，那么改变一个引用（如: string2）的字符
串将会导致另一个引用（如: string1）出现脏数据。

2. 允许字符串缓存哈希码
在java中常常会用到字符串的哈希码，例如： HashMap 。String的不变性保证哈希码始终不变。
这种方法意味着不必每次使用时都重新计算一次哈希码——这样，效率会高很多。

3. 安全
String广泛的用于java 类中的参数，如：网络连接（Network connetion），打开文件（opening files ）等等。
如果String不是不可变的，网络连接、文件将会被改变——这将会导致一系列的安全威胁。操作的方法本以为连接上了一台机器，
但实际上却不是。由于反射中的参数都是字符串，同样，也会引起一系列的安全问题。

### 集合类List,Set,Map的区别

- Set是最简单的一种集合。集合中的对象不按特定的方式排序，并且没有重复对象;主要有下面两个实现类：
    - HashSet：按照哈希算法来存取集合中的对象，存取速度比较快 
    - TreeSet：实现了SortedSet接口，使用二叉树存储集合中对象，能够对集合中的对象进行排序。

- List的特征是其元素以线性方式存储，集合中可以存放重复对象。 
    - ArrayList: 用可变数组实现，可以对元素进行随机的访问，向ArrayList()中插入与删除元素的速度慢。 
    - LinkedList: 在实现中采用双向链表数据结构。插入和删除速度快，访问速度慢。

- Map 是一种把键对象和值对象映射的集合，它的每一个元素都包含一对键对象和值对象。 Map没有继承于Collection接口， 
    从Map集合中检索元素时，只要给出键对象，就会返回对应的值对象。
    - HashMap：基于散列表(哈希表)实现。插入和查询“键值对”的开销是固定的。可以通过构造器设置容量capacity和
        负载因子load factor，以调整容器的性能。
    - LinkedHashMap： 使用链表实现，迭代遍历它时，取得“键值对”的顺序是其插入次序，或者是最近最少使用(LRU)的次序。
        只比HashMap慢一点。而在迭代访问时更快，因为它使用链表维护内部次序。
    - TreeMap： 基于红黑树数据结构的实现。查看“键”或“键值对”时，它们会被排序(次序由Comparable或Comparator决定)。
        TreeMap的特点在于，你得到的结果是经过排序的。TreeMap是唯一的带有subMap()方法的Map，它可以返回一个子树。
    - WeakHashMao ：弱键(weak key)Map，Map中使用的对象也被允许释放: 这是为解决特殊问题设计的。
        如果没有map之外的引用指向某个“键”，则此“键”可以被垃圾收集器回收。 
    - ArrayMap:采用二分法查找，提供了数组收缩的功能，在clear或remove后，会重新收缩数组，释放空间；
        与HashMap添加数据时扩容时的处理不一样，HashMap当使用put方法添加键值对时，就会new一个HashMapEntry对象，
        重新创建对象，开销很大。ArrayMap用的是copy数据，所以效率相对要高。
- HashTable 
    - HashMap不是线程安全的，效率高一点、方法不是Synchronize的要提供外同步，有containsValue和containsKey方法  
    - HashTable是线程安全，不允许有null的键和值，效率稍低，方法是是Synchronize的。有contains方法方法。
        HashTable 继承于Dictionary 类     

### synchronized 和volatile 关键字的区别 
    
1. volatile本质是在告诉jvm当前变量在寄存器（工作内存）中的值是不确定的，需要从主存中读取；
   synchronized则是锁定当前变量，只有当前线程可以访问该变量，其他线程被阻塞住。
   
2. volatile仅能使用在变量级别；synchronized则可以使用在变量、方法、和类级别的

3. volatile仅能实现变量的修改可见性，不能保证原子性；synchronized则可以保证变量的修改可见性和原子性

4. volatile不会造成线程的阻塞；synchronized可能会造成线程的阻塞。

5. volatile标记的变量不会被编译器优化；synchronized标记的变量可以被编译器优化