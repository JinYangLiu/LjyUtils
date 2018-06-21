
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
    
### 对于所有对象都通用的方法

- 主要是Object类的非final方法 & Comparable.compareTo()方法
    
8. 覆盖equals时请遵守通用约定 
    - equals通用约定
        - equals方法实现了等价关系
        - 自反性：对于任何非null的引用值x，x.equals(x)必须返回true
        - 对称性：对于任何非null的引用值x和y，当且仅当y.equals(x)返回true时，x.equals(y)必须返回true
        - 传递性：对于任何非null的引用值x，y和z,如果x.equals(y)返回true，并且y.equals(z)返回true,则
        x.equals(z)也必须返回true
        - 一致性：对于任何非null的引用值x和y，只要equals的比较操作在对象中所用的信息没有被修改，
        则多次调用x.equals(y)的返回结果是一致的
    - 不需要覆盖equals对情况：
        1. 类对每个实例本质上都是唯一的，例如Thread，每个实例都对应一个线程
        2. 不关心类是否提供了"逻辑相等"的测试功能：如Random可以覆盖equals检查两个实例是否产生相同的随机数序列，但
        设计者并不认为用户需要或期望这样的功能，所以并没有重写equals方法
        3. 超类已经覆盖类equals，且对于子类也适用时
        4. 类是私有的或是包级私有的，可以确定他的equals方法永远不会被调用，此时最好覆盖equals方法,防止被意外调用
        ````
        @Override
        public boolean equals(Object obj) {
            throw new AssertionError(" Method is never called")
        }
        ````
    - 需要覆盖equals的情况：
        - "值等"情形：类具有自己特有的"逻辑相等"概念，而且超类还没有覆盖equals以实现期望的行为
    
    - 注意equals方法的写法: 
        不要将equals声明中的Object对象替换成其他的类型，这可能带来不可预知的麻烦
        ````
        public boolean equals(Person obj) {
             return true;
         }
     
         @Override
         public boolean equals(Object obj) {
             return super.equals(obj);
         }
        ````

9. 覆盖equals时总要覆盖hashCode
    - hashCode通用约定：
        1. 在程序执行期间，只要对象的equals方法的比较操作所用到的信息没有被修改，那么同一个对象调用多次，
        hashCode方法都必须始终如一的返回同一个整数，同一个应用程序的多次执行过程中，返回的整数可以不一致
        2. 如果两个对象使用equals比较是相等的，那么hashCode必须产生同样的整数结果
        3. 如果两个对象使用equals比较是不相等的，那么hashCode方法不一定要产生不同的整数结果，但是，程序员应该知道，
        给不想等的对象产生不同的hashCode可能提高散列表的性能
    - 为不相等的对象产生不相等的散列码（hashCode方法的返回值）：
        1. 将某个非零常数值赋值给result，如result=17
        2.为每个关键域f（指equals涉及的域）计算散列码,并合并到result中
            1. 计算
                1. 若该域是boolean，则c=(f？1：0)
                2. 若该域是byte，char，short，int，则c=(int)f
                3. 若该域是long，则c=(int)(f^(f>>>32))
                4. 若该域是float，则c=Float.floatToIntBits(f)
                5. 若该域是double，则d=Double.doubleToLongBits(f),然后按iii中处理c=(int)(d^(d>>>32))
                6. 若该域是一个对象引用，并且该类的equals方法通过递归调用equals方法比较这个域，
                则同样的为这个域递归的调用hashCode，若需要更复杂的，则为这个域计算一个范式，然后针对这个范式调用hashCode，
                如果这个域是null，则c=0或其他某个常熟
                7. 若该域是一个数组，则把每个元素都当作单独的域处理，若数组域中每个元素都很重要，可以利用Arrays.hashCode方法
            2. 合并，公式：result=31*result+c;    
        3. 返回result
        - 可以把冗余域（可以根据其他关键域计算出来）排除
        - 必须把equals中没有用到的域排除
        - 详见com.ljy.javacode.bean.Person的equals方法
        
10. 始终覆盖toString
    - 通用约定：建议所有子类都覆盖这个方法
      
11. 谨慎的覆盖clone
    - implements Cloneable, 重写clone方法
    - 注意不能伤害到原始对象，并确保正确到创建被克隆对象中到约束条件
    - 可能有必要从某些域中去掉final修饰符
    - 详见com.ljy.javacode.bean.Person的clone方法      
    - 一些专家级到程序员干脆从来不去覆盖clone方法，也从来不去调用它，除非拷贝数组
    - 对于一个为了继承而设计的类，如果你未能提供行为良好的受保护的clone方法，它的子类就不可能实现Cloneable接口
    
12. 考虑实现Comparable接口
    - 遵守自反性，对称性，传递性
    - 详见com.ljy.javacode.bean.Person的compareTo方法 
    - 注意
    - 如果想给一个实现了Comparable接口的类增加值组件，请不要扩展这个类，
    要编写一个不相关的类，其中包含第一个类的一个实例,并提供一个视图（view）方法返回这个实例，
    这样既可以在第二个类上自由的实现compareTo方法，也允许客户端在必要的时候，将第二个类的实例视同第一个类的实例
    
### 类和接口

13. 使类和成员的可访问性最小化      
    - 封装（信息隐藏）：隐藏实现细节，将它的API与内部实现隔离，降低模块间的耦合性，
    易于独立开发，测试，优化等，提高软件的可重用性，
    - 访问控制的方式：使用访问修饰符（private，public，protected）
    - 尽可能的使每个类或者成员不能被外界访问
    - 不能为了方便测试，而将类，接口或者成员变成包的导出API的一部分（即不要将权限大于package-private（default））
    - 包含公有可变域的类不是线程安全的，即 public &&（非final || 引用可变对象），同样也适用于静态域
        例如：
        ````
        private static final Person[] familys={new Person("bob"),new Person("lily")};
        //1. 公有+可变对象的数组 （不安全的）
        public static final Person[] FAMILYS_1=familys;
        //2. 私有数组+公有不可变列表 （修正1）
        public static final List<Person> FAMILYS_2= Collections.unmodifiableList(Arrays.asList(familys));
        //3. 私有数组+公有方法返回私有数组备份（修正2）
        public static Person[] getFamilys(){
            return familys.clone();
        }
        ````

14. 在公有类中使用访问方法, 而非公有域   
     - 如果类可以在它所在的包外进行访问，就提供访问方法(getter)和设值方法(setter)，
     以保留将来改变该类内部表示法的灵活性，而且方便对其添加限制条件
     
15. 使可变性最小化
    - 不可变类： 其实例不能被修改，每个实例所有信息在创建实例时提供，并且在对象的整个生命周期内固定不变，
    如String，基本类型的包装类，BigInteger，BigDecimal，不可变类比可变类更易于设计，实现和使用，
    且更加不易出错，是线程安全，可以被自由的共享，充分利用现有实例；
    - 使类不可变的五条规则：
        1. 不提供任何修改对象状态的方法
        2. 保证类不会被扩展：用final修饰或将所有构造方法私有化并提供公有的静态工厂方法
        3. 使所有域都是final的
        (实际上只要没有方法能够对域产生外部可见的改变即可，如延迟初始化，懒汉单例等，都不能让该域是final的)
        4. 使所有域都是私有的
        5. 确保对于任何可变组件的互斥访问：对于指向可变对象的域，必须确保客户端无法获得指向这些对象的引用，
        并且永远不使用客户端提供的对象引用来初始化这样的域；在构造器，访问方法，readObject方法中使用保护性拷贝技术
    - 如果一个类的安全性依赖于参数（来源于客户端）的不可变性，就必须进行检查，确保是真正的不可变类，而不是不可信任的子类实例，
    如果是后者，就必须进行保护性拷贝，例如：
        ````
        public static BigInteger safeInstance(BigInteger val){
            
            //if (val instanceof BigInteger)
            //这里不能用instanceOf，因为val可能是其子类
            if (val.getClass()!=BigInteger.class)
                return new BigInteger(val.toByteArray());
            return val;
        }
        ````
    - 除非有需要，不要为getter方法编写对应的setter方法，以保证类的不可变，getter方法可以返回保护性拷贝对象
    - 只有当确认有必要实现令人满意的性能时，才应该为不可变类提供公有的可变配套类，如String的可变配套类StringBuilder和StringBuffer
    - 构造器/静态工厂方法应该创建完全初始化的对象，并建立起所有的约束关系，不要再提供额外的公有初始化方法，或重新初始化方法
    
16. 复合优先于继承
    - 继承是实现代码重用的有力手段，但并非永远是最佳方案（跨包边界的继承，可能处于不同程序员的控制下，是非常危险的，
    父类随着发行版本的不同可能发生变化，子类就有可能遭到破坏），例如下面代码中所提到的问题：
    ````
            class MyList {
        
                private ArrayList<String> mList = new ArrayList<>();
        
                //public boolean add(String value){
                //   return mList.add(value);
                //}
        
                //问题2：如果有一天add方法进行了这样的修改，对空值进行判断,那么子类中的size就可能不准确了
                public boolean add(String value) {
                    if (!TextUtils.isEmpty(value))
                        return mList.add(value);
                    return false;
                }
        
                public int addAll(String[] values) {
                    int count=0;
                    for (String val : values) {
                        if (add(val))
                            count++;
                    }
                    return count;
                }
            }
        
            //演示继承可能引发的问题
            class MySizeList extends MyList {
                private int size;
        
                @Override
                public boolean add(String value) {
                    size++;
                    return super.add(value);
                }
        
                @Override
                public int addAll(String[] values) {
                    //问题1：这里看着逻辑上没有问题，然而super.addAll底层调用的是add方法，就会导致size被加类两遍
                    size += values.length;
                    return super.addAll(values);
                }
        
                public int getSize() {
                    return size;
                }
            }
        
            //使用复合/转发代替继承（装饰器模式）
            class MyForwardList{
        
                private MyList myList;
        
                private int size;
        
                public void add(String value){
                    if (myList.add(value))
                        size++;
                }
        
                public void addAll(String[] values){
                    size+=myList.addAll(values);
                }
                
                public int getSize() {
                    return size;
                }
            }

    ````
    - 只有当A，B两个类之间存在"B is A"关系，且A类本身不存在缺陷时，才应该使用继承
  
17. 要么为继承而设计，提供文档说明，要么就禁止继承    
    - 该类必须有文档说明它可覆盖方法的自用性，方法或构造器中调用了哪些可覆盖方法，每个调用如何影响后续处理过程
    - 好的API文档应该描述一个给定方法做了什么工作，而不是描述它是如何做到的
    - 可以使用受保护（protected）的方法或域，提供钩子（hook），以便子类能够获取更多的权限/功能
    - 对于为了继承而设计的类，唯一的测试方法就是编写子类，以此查找遗漏，和判断是否应该私有，一般编写3个子类测试即可
    - 构造器决不能调用可被覆盖的方法，无论直接或是间接
    （因为超类的构造方法先于子类构造方法执行，而可被覆盖的方法可能在子类重写时依赖子类构造方法初始化的数据，这时会导致执行该方法出现问题）
    （可将可覆盖方法代码体移到一个私有辅助方法中，可覆盖方法和构造器都调用这个私有辅助方法）
    - 为继承而设计的类，最好不要实现Cloneable或Serializable接口，如果实现，请不要在clone或readObject方法中调用可覆盖的方法，原因同上
    - 对于普通的具体类，若非为了安全的进行子类化而设计，要禁止其子类化
    （或者完全消除这个类中可覆盖方法的自用性，以确保安全的进行子类化）
    
18. 接口优于抽象类
    - 二者区别：
        1. 抽象类允许包含某些方法的实现，接口则不允许
        2. (重要)为了实现由抽象类定义的类型，类必须成为抽象类的一个子类，而java只允许单继承，使得抽象类作为类型定义收到类极大的限制
        3. 抽象类的演变比接口容易的多，在后续发行中可以在抽象类中增加新的具体方法，并包含合理的默认实现，
        则该抽象类的所有实现都将具有这个新的方法，而接口则不行
    - 现有类可以很容易被更新，以实现新的接口；而抽象类则需要放到类型层次的最高处，无论后续的其他子孙类是否需要
    - 接口是定义mixin（混合类型）的理想选择，方便实现某些可供选择的行为
    - 接口允许我们构造非层次结构的类型框架，如一个coder接口和一个gamer接口，然而一个人可能即是程序员又是游戏玩家
    - 可以通过对重要接口提供一个抽象的骨架实现类，把接口和抽象类的优点结合起来，如常见的集合类中List接口和AbstractList抽象类等等
    （骨架实现类为例继承的目的而设计，有助于接口的实现，而又不是必须的）
    - 接口一旦公开发行，并被广泛实现，再想改变这个接口几乎是不可能的，因此必须在设计之初就保证接口的正确性

19. 接口只用于定义类型
    - 当类实现接口时，接口就充当可以引用这个类的实例的类型，任何为了其他目的而定义的接口是不恰当的
    （有一种常量接口，只包含静态final域，没有任何方法，需要这些常量的类实现这个接口，以避免用类名来修饰常量，
    这是对接口的不良使用，1.常量接口暴露了类的实现细节；2.后续子类的命名空间会被接口中的常量污染）
    (常量接口的替代方案：1.添加到类或接口中；2.使用枚举类型导出常量；3.使用不可实例化的工具类)
    
20. 类层次优先于标签类 
    - 标签类：可以有多种风格的实例的类，并包含表示实例风格的标签域
    （这种标签类中充斥着样板代码，包含枚举或静态域标签，以及条件语句，破坏可读性和单一职责原则，过于冗长，容易出错，效率底下，占用内存增加）
    （如一个形状类通过一个type字段来判断要创建出的实例是矩形，圆，还是三角形；最好是创建一个形状抽象类，再分别实现代表矩形，圆，三角形的不同子类）
    
21. 用函数对象表示策略 （策略模式）  
    - 函数对象：如果一个类仅仅执行这样一个方法，此方法执行其他对象（这些对象被显示对传递给这些方法）上的操作，它的实例实际上等同于一个指向该方法的指针，
    这样的实例成为函数对象，如下：
    ````
        //比较器接口
        public interface Comparator<T>{
            public int compare(T t1,T t2);
        }
    
        //宿主类
        class Host{
        
            //通过长度比较字符串的比较器, 这里使用静态内部类，而不是匿名内部类，以便具体的策略类实现第二个Serializable接口
            //实际使用中，如果需要频繁使用策略类的实例，最好使用单例模式（因为它们一般是无状态的（没有域），
            该类的所有实例在功能上是相互等价的，使用单例可以节省不必要的对象创建开销）
            private static class StrLenCmp implements Comparator<String>,Serializable {
                
                @Override
                public int compare(String t1, String t2) {
                    return t1.length()-t2.length();
                }
            }
    
            //函数对象表示策略
            public static final StrLenCmp STR_LEN_CMP=new StrLenCmp();
    
            //待比较的域
            public String name;
    
            public int compare(Host other){
                return STR_LEN_CMP.compare(this.name,other.name);
            }
        }
    ````
    
22. 优先考虑静态成员类
    - 嵌套类：指被定义在另一个类的内部的类，嵌套类存在的目的应该只是为它的外围类提供服务；
    如果嵌套类将来可能用于其他某个环境，它就应该是顶层类；
    - 嵌套类分为：静态成员类，非静态成员类，匿名类，和局部类，后三种都称为内部类
    - 静态成员类是最简单的嵌套类，把它看作普通类即可，
        - 常见的用法：
            - 公有静态成员类：作为公有的辅助类，仅当与它的外部类一起使用才有意义
            - 私有静态成员类：用来代表外围类所代表对象的部分组件，如汽车的轮子，Map中的Entry代表一组键值对
    - 静态成员类（独立）与非静态成员类（依赖）的唯一区别是：内部类实例是否依赖外围类的实例
    - 匿名类：只需要在一个地方创建实例时使用，使用时被声明和实例化，可以出现在任何允许存在表达式的地方，应尽量简短（10行左右）以维持可读性
        - 常见用法：
            1. 动态的创建函数对象，如Comparator比较器
            2. 创建过程对象，如进行接口回调，Runnable，Thread等
            3. 用在静态工厂方法内部返回匿名内部类创建的实例
    - 局部类：用的最少，在任何可以声明局部变量的地方            

### 泛型

23. 请不要在新代码中使用原生态类型
    - 如不要使用List，而是使用List<E>（泛型）,形式类型参数E表示列表的元素类型
    - 由于java 20岁时才有泛型，为了保证移植兼容性，java仍旧支持原生态类型
    - 如果使用原生态类型，就失掉了泛型在安全性和表述性方面的所有优势
    - 例如下面代码：
    ````
        private List<String> mList=Arrays.asList(new String[]{"aa","bb","cc"})
        
        //原生态类型List，可以包含任何类型对象，不安全（逃避了类型检查）
        public void printList(List list){
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i).toString());
            }
        }
    
        //参数化的类型 List<Object>，可以包含任何类型对象，安全
        public void printObjectList(List<Object> list){
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i).toString());
            }
        }
    
        //参数化的类型 List<String>，只能是String类型，安全
        public void printStringList(List<String> list){
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i).toString());
            }
        }
        
        //List<?> 无限制通配符类型，只能是某种未知类型，安全
        //类型通配符上限通过形如Box<? extends Number>形式定义，相对应的，类型通配符下限为Box<? super Number>形式
        public static void printAnyList(List<?> list) {
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i).toString());
            }
        }
        
        public void main(){
            printList(mList);//可以，不安全
            printObjectList(mList);//报错，安全
            printStringList(mList);//可以，安全
            printAnyList(mList);//可以，安全
            //List<String>是List的一个子类型，而不是List<Object>的子类型
        }
    ````
    - 在类文字中必须使用原生态类型，不允许使用参数化类型，如String[].class,List.class,不能使用List<String>.class,List<?>.class等

24. 消除非受检警告    
    - 用泛型编程时，会遇到许多编译器警告
        - 非受检强制转化警告
        - 非受检方法调用警告
        - 非受检普通数组创建警告
        - 非受检转换警告
    - 要尽可能的消除每一个非受检警告，以确保代码是类型安全的（否则可能会在运行时出行ClassCastException）：
    - 如果无法消除警告，同时可以证明引起警告的代码是类型安全的，可以用@SuppressWarnings("unchecked")注解来禁止这条警告，最好添加注释说明为什么是安全的
    （应在尽可能小的范围中使用该注解,如果在长度不止一行的方法或构造器中使用该注解，可将其移到一个局部变量的声明中,例如下面这样：）
        ````
        public static <T> T getObjectByFieldName(Object object, String fieldName, Class<T> clas) {
            Field field = getField(object.getClass(), fieldName, true, true);
            if (field != null) {
                field.setAccessible(true);
                @SuppressWarnings("unchecked")
                T result = (T) field.get(object);
                return result;
            } else {
                return null;
            }
        }
        ````
        
25. 列表优先于数组
    - 两个不同点
        1. 数组是协变的：如果Sub是Super的子类，那么数组Sub[]也是Super[]的子类；
        而泛型则是不可变的List<Sub>和List<Super>不存在父子关系；
        正因如此，数组在这一点上是存在缺陷的,请看下面例子：
        ````
        //编译期合法的：
        //fails at runtime(运行时报错：ArrayStoreException)
        Object[] objArr=new Long[10];
        objArr[0]="Hello";

        //编译期不合法：
        //won't compile（不能编译通过:Incompatible types,required...,find...）
        List<Object> objList=new ArrayList<Long>();
        objList.add("World");
        ````
        2. 数组是具体化的，在运行时才知道并检查它们的元素类型约束；
        而java中的泛型只存在于编译期，以强化类型信息，在运行时丢弃（擦除）其元素类型信息，.class中是没有泛型的；
        （擦除就是使泛型可以与没有使用泛型的代码随意进行互用，也是为了兼容泛型出现前的代码）
        ````
        List<String> l1 = new ArrayList<String>();
        List<Integer> l2 = new ArrayList<Integer>();
        
        System.out.println(l1.getClass() == l2.getClass());
        
        //输出结果为true
        ````
    - 综上原因，创建泛型数组 new List<E>[],参数化类型数组 new List<String>[],类型参数数组new E[]，这些都是非法的

26. 优先考虑泛型
    - 将类泛型化，尽量使用泛型代替Object，如用E[] 代替Object[]，例如下面代码：
        ````
        class Stack<T>{
            private T[] arr;
            private int size=10;
        
            @SuppressWarnings("unchecked")
            public Stack() {
                arr=(T[])new Object[size];
            }
            
            public T pop(){
                T result=arr[--size];
                return result;
            }
        }
        ````

27. 优先考虑泛型方法
    - 如同类泛型化一样，泛型方法也具有类型检查的优势，无需明确指定类型参数的值，
    更加安全，也便于使用，尤其是静态工具方法，非常适合泛型化
        ````
        public static Set union1(Set s1,Set s2){
            Set result=new HashSet(s1);//警告：unchecked call...
            result.addAll(s2);//警告：unchecked call...
            return result;
        }
    
        //泛型化，但是这样写有个局限性，两个参数和返回值的类型参数必须相同
        //可以利用有限制的通配符类型，使其更加灵活
        public static <E> Set<E> union2(Set<E> s1,Set<E> s2){
            Set<E> result=new HashSet<>(s1);
            result.addAll(s2);
            return result;
        }
        ````
    - 递归类型限制：通过某个包含该类型参数本身的表达式来限制类型参数，如 <T extends Comparable<T>>    
    
28. 利用有限制通配符来提升API的灵活性
    - 我们知道参数化类型是不可变的，如List<String>与List<Object>不存在父子关系（A is B 的关系），
    其所带来的优势就是类型安全，可以解决25.1中的问题，然而有些时候却不够灵活，
    例如下面代码中的pushAll_1()和popAll_1()使用时的问题
    ````
     //栈的实现类
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

        //泛型不可变
        public void popAll_1(Collection<T> dst){
            while (top >=0){
                dst.add(pop());
            }
        }
        
        //下限
        public void popAll_2(Collection<? super T> dst){
            while (top >=0){
                dst.add(pop());
            }
        }

        public void display() {
            System.out.println("Stack-->" + Arrays.asList(arr).toString());
        }
     }

    //调用
    Stack<Number> numberStack = new Stack<>();
    List<Integer> stringList = Arrays.asList(new Integer[]{11,12,13});
    // objectStack.pushAll_1(stringList);//报错
    numberStack.pushAll_2(stringList);
    numberStack.display();
    List<Object> dst=new ArrayList<>();
    // numberStack.popAll_1(dst);//报错
    numberStack.popAll_2(dst);
    System.out.println("dst-->" +dst.toString());

    ````
    - 类型参数和通配符选择哪个？请看下面代码：
    ````
    //无限制的通配符，在公共API中这种写法更好，因为简单
    //一般来说如果类型参数只在方法声明中出现一次，就可以用通配符取代它
    public static void swap(List<?> list, int i, int j) {
        // list.set(i,list.set(j,list.get(i)));//编译时报错，不能将任何null之外的值放到List<?>中
        swapHelper(list, i, j);
    }
    
    //无限制的类型参数,使用泛型方法作为私有辅助方法
    private static <E> void swapHelper(List<E> list, int i, int j) {
        list.set(i,list.set(j,list.get(i)));
    }    
    ````
    
29. 优先考虑类型安全的异构容器    
    - 泛型的常见用法是这样List<T>,Map<K,V>,ThreadLocal<T>,充当被参数化了的容器，
    但这限制了每个容器只能有固定数目的类型参数，如：List只有一个类型参数T代表其元素类型；
    - 一种解决方案：将键(key)进行参数化而不是将容器参数化，然后将参数化的键提交给容器来插入或者获取值，
    用泛型系统来确保值的类型与它的键相符，就行下面代码这样：
    ````
    //Favorites是类型安全的，同时也是异构的（所有键都是不同类型的）
    //将其称作：类型安全的异构容器
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
    
    //使用
    Favorites f=new Favorites();
    
    f.put(String.class,"java");
    f.put(Integer.class,0x16);//按16进制存
    f.put(Class.class,Favorites.class);
    
    String fStr=f.get(String.class);
    int fInt=f.get(Integer.class);//按10进制取
    Class<?> fFavor=f.get(Class.class);
    System.out.printf("%s %x %s%n",fStr,fInt,fFavor.getName());

    ````
    
### 枚举和注解

30. 用enum代替int常量    
    
    