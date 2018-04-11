## 目录
+ [java多线程sleep与wait区别](#1)
+ [Collection 和Collections的区别](#2)

#### <span id = "1">java多线程sleep与wait区别</span>

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
    
#### <span id = "2">Collection和Collections的区别</span>

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
       
    
