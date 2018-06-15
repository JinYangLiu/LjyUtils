
### 创建和销毁对象

1. 使用静态工厂方法代替构造器（详见com.ljy.javacode.bean.Person中的实现）
    - 一个类对外提供获取自身实例对象的方法
        1. 提供公有构造器
        2. 公有的静态工厂方法（一个返回当前类实例的静态方法，包括当不限于单例），其实这种方式在Object-C里面深有体现
    - 静态工厂方法的优势    
        1. 可以随意组合要初始化的属性，通过不同的命名，不仅可以避免构造器的限制（一个类只能有一个带有指定签名的构造器），
        而且可以做到顾名思义
        2. 可以不用在每次调用时都创建一个新的对象，可以使用预先构建好的对象，或将构建好的对象缓存起来，进行重复利用，
        适用于经常请求创建相同对象，并且创建对象的代价很高，如常见的单例模式写法就是对这一点的应用
            - 能为重复的调用返回相同的类，有助于类控制某个时刻哪些实例应该存在，即：实例受控的类，功能如下：
                1. 确保它是一个Singleton或者是不可实例化的
                2. 使得不可变的类不会存在两个相等的实例，即当切仅当a==b时，才有a.equals(b)=true，如果保证了这一点，
                就可以使用==代替equals，这样可以提高性能，枚举类型保证了这一点
                (==为hash地址相同，即同一个对象；equals为实例内容相同)
        3. 可以返回原返回值类型的任何子类型的对象，有更好的灵活性，而且可以隐藏实现类(子类)，使API更简洁，适用于基于接口的框架；
        （如可以用工厂方法的不同命名标识不同的子类对象，也可以同一个工厂方法，通过参数（如Type）进行判断，返回不同类型的子类对象）
        4. 创建参数化类型实例(即泛型)时，使代码更简洁，如
        ````
        HashMap<String,List<String>> map = new HashMap<String,List<String>>();
        //（作者写这本书时java还没有加入自动类型推导，不像我们可以直接HashMap<String,List<String>> map = new HashMap<>();）
        //上面这样写就很麻烦，假设HashMap提供了下面的工厂方法：
        public static <K,V> HashMap<K,V> newInstance(){
            return new HashMap<K,V>();
        }
        //那么创建实例时可以这样写：
        HashMap<String,List<String>> map =HashMap.newInstance();
        ````
    - 静态工厂方法的缺点
        1. 类如果不含公有的(public)或受保护的(protected)构造器,就不能被子类化，不方便扩展和形成关系体系
        （不过某些情况也可以用组合（复和/composition，即该类的实例做为新类的一个属性）代替继承，同样可以实现代码的复用）
        2. 与其他静态方法没有任何区别，不方便使用者查找使用
        （可以通过添加注释以及命名规范，弥补这个问题）
        
2. 遇到多个构造器参数时要考虑使用构建器（建造者模式）（详见com.ljy.javacode.bean.Person中的实现）        
    - 如果有大量的可选参数需要任意组合
        - 使用构造器：不能任意的组合
        - 使用公有静态工厂方法：需要提前写出足够多组合的工厂方法，而且参数过多时通过命名区分也将很不方便
        - 使用JavaBeans：即无参构造+setter方法，但是这使得构造过程分到了多个调用中，而且不能有效但保证一致性；
            还有就是阻止了把类做成不可变的可能，需要额外保证它的线程安全性
        - 使用Builder模式：
            - 优点：既保证了安全性，又有很好的可读性,而且方便对参数添加约束条件，也可以自动填充某些参数而不提供给调用者去修改
            - 缺点：代码量增多，可能增加内存开销，
            - 适用于参数比较多的情况
            
3. 用私有构造器或者枚举类型强化Singleton属性
    - 单例模式的饿汉式，懒汉式，都是需要将构造器私有化
    - 单元素的枚举类型已经成为实现 Singleton 的最佳方法  

4. 通过私有构造器强化不可实例化的能力  
    - 有些类只有静态方法和静态属性，如一些工具类LogUtil等，我们不希望它可以创建实例，然而一个类在缺少构造器时，
    系统会默认提供一个公有无参的构造方法，这时我们可以显示的提供一个私有的构造方法，以保证该类不可实例化
    ````
    public class LogUtil{
        //suppress default constructor for noninstantiability
        private LogUtil(){
            throw new AssertionError("LogUtil 不可被实例化");
            //加上面这一句时防止通过反射或者在该类内部实例化其实例对象
        }
    }
    ````
5. 避免创建不必要的对象
    - 例1： Boolean.valueOf("true")要优于new Boolean("true")
    ````
    //其内部实现如下
    public static Boolean valueOf(String s) {
            return parseBoolean(s) ? TRUE : FALSE;
    }
    public static final Boolean TRUE = new Boolean(true);
    public static final Boolean FALSE = new Boolean(false);
    ````
    - 例2：com.ljy.javacode.effectiveJava.bean.Person中的isBabyBoomer_good()方法
    - 注意这里是说避免创建不必要的对象，而不是尽量少的创建对象
    
6. 消除过期的对象引用  
   - 一般而言，只要类是自己管理内存，就应当注意内存泄漏问题 
   例如：栈实现类com.ljy.javacode.structure.stack.ArrayStack中的pop方法，将已经出栈的元素设为空
   - 内存泄漏另一个常见来源是缓存：WeakHashMap，LinkedHashMap.removeEldestEntry(),java.lang.ref
   - 监听器和其他回调：要取消注册或使用weak reference，如只将他们保存为WeakHashMap中的键
   
7. 避免使用终结方法
    - finalizer通常是不可预测的，也是很危险的，一般是不必要的，除非作为安全网，或者为了终止非关键的本地资源，
    若使用了终结方法，就要记住调用super.finalize,如果需要把终结方法与公有的非final类关联起来，请考虑使用终结方法守卫者，
    以确保即使子类的终结方法未能调用super.finalize,该终结方法也会被执行