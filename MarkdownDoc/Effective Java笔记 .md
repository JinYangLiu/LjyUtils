
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
    - int枚举：
        - 引入枚举前，一般是声明一组具名的int常量，每个常量代表一个类型成员，这种方法叫做int枚举模式  
        - int枚举模式是类型不安全的，例如下面两组常量：性别和动物种类，二者不存在任何关系，
            然而却可以将ANIMAL_DOG传入一个需要性别参数的方法中，编译器不会出现警告，而且方法内部逻辑还会继续执行比较等操作，
            ````
            public static final int SEX_WOMAN=0;//女
            public static final int SEX_MAN=1;//男
            
            public static final int ANIMAL_CAT=10;//猫
            public static final int ANIMAL_DOG=11;//狗
            ````
        - 采用int枚举模式的程序是十分脆弱的，因为int枚举是编译时常量，被编译到使用它们的客户端中，如果与枚举常量关联的
            int值发生变化客户端就必须重新编译，然而不重新编译却不会报错，但是会导致程序的结果不准确，
            例如上面的常量SEX_MAN被客户端使用，于是将其值1编译到客户端的.class中，然后如果API类中将SEX_MAN的值改为2，
            却不重新编译客户端，那么客户端得到的结果就是不准确的了
        - int枚举常量很难被翻译成可打印的字符串，不利于开发调试
        - 开发过程中还可能遇到这种模式的变体，如String枚举模式，同样是存在上述问题
        
    - 枚举类型：
    
        - 由一组固定的常量组成的合法值的类型，例如
            ````
             public enum Sex {MAN, WOMAN}
            
             public enum Animal {CAT, DOG}
            ````
        
        - 实现思路：通过公有静态的final域为每个枚举常量导出实例的类
        
        - 枚举类型是类型安全的枚举模式，而且完善类int枚举模式的不足
        
        - 枚举类型还允许添加任意的方法和域，并实现任意的接口，它提供类所以Object方法的高级实现，
        实现了Comparable和Serializable接口，并针对枚举类型的可任意改变性设计了序列化方式
            ````
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
            
            //使用枚举的方法
            private static void testEnum(Sex sex, Animal animal) {
                System.out.println(sex.toString());
                System.out.println(animal.toString());
                int foots = animal.getFoots();
                String info = animal.getInfo();
                int result = animal.compareTo(Animal.CAT);
            }
            //调用方法
            testEnum(Sex.MAN, Animal.DOG);
            ````
            
        - 特定于常量的方法实现：在枚举类型中声明一个抽象方法，并在特定于常量的类主体中，
        用具体的方法覆盖每个常量的抽象方法。例如下面这样定义一个代表加减乘除等运算符的枚举    
            ````
            //普通写法，这样写逻辑上没有问题，但不太好看，而且若新增枚举常量，而忘记在apply方法的switch中添加相应的条件，
            //编译不会有问题，但运行时如果用到新增的枚举常量就会失败
            public enum Operation {
                PLUS, MINUS, TIMES, DIVIDE;
        
                double apply(double x, double y){
                    switch (this){
                        case PLUS: return x+y;
                        case MINUS:return x-y;
                        case TIMES:return x*y;
                        case DIVIDE:return x/y;
                    }
                    throw new AssertionError("Unknown operation: "+this);
                }
            }
            
            //特定于常量的方法实现
            public enum Operation {
            
                PLUS ("+") { double apply(double x, double y) { return x + y; }},
                MINUS ("-") { double apply(double x, double y) { return x - y; }},
                TIMES ("*") { double apply(double x, double y) { return x * y; }},
                DIVIDE ("/") { double apply(double x, double y) { return x / y; }};
                
                private final String symbol;//特定于常量的数据
        
                Operation(String symbol) {
                    this.symbol = symbol;
                }
        
                abstract double apply(double x, double y);
                
                //重写toString
                @Override public String toString(){ return symbol;}
            }
            ````        
        
        - 策略枚举：多个枚举常量同时共享相同的行为时，考虑使用策略枚举，例如下面这样：
            ````
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
            ````
    
31. 用实例域代替序数

    - 序数：枚举天生就与一个单独的int值相关联，所有枚举都有一个ordinal()方法，返回每个枚举常量在类型中的数字位置（类似于数组索引）    
    - 永远不要根据枚举的序数导出与他相关联的值，而是将它保存在一个实例域中
    （Enum规范中关于ordinal()写到："大多数程序员都不需要这个方法，它是设计成用于像EnumSet，EnumMap这种基于枚举的通用数据结构的）
        ````
        public enum Ensemble{
            SOLO(1),DUET(2),TRIQ(3),QUARTET(4),QUINTET(5);

            private final int numberOfMusicians;
            Ensemble(int size){
                this.numberOfMusicians = size;
            }
            public int numberOfMusicians(){
                // return ordinal()+1;
                return numberOfMusicians;
            }
        }
        ````
32. 用EnumSet代替位域
    - 位域：可以用or（|）位运算将几个常量合并到一个集合中,例如下面代码这样：
        ````
        public class Text_1 {
            //int枚举常量
            public static final int STYLE_BOLD = 1 << 0;//1,粗体
            public static final int STYLE_ITALIC = 1 << 1;//2,斜体
            public static final int STYLE_UNDERLINE = 1 << 2;//4,下划线
            public static final int STYLE_STRIKETHROUGH = 1 << 3;//8,删除线
    
            // parameter is bitwise OR of zero or more STYLE_ constants
            public void applyStyles(int styles) {
                //...do something
            }
        }
        
        //使用
        Text_1 text_1=new Text_1();
        //通过位域可以同时传递多个style
        text_1.applyStyles(Text_1.STYLE_BOLD | Text_1.STYLE_ITALIC);
        ````
    - 位域的不足：具有int枚举的所有缺点
    - 替代方案--EnumSet：
        从单个枚举类型中提取多个值，每个EnumSet内容都表示为位矢量，
        如果底层的枚举类型有64或更少的元素（大多如此），整个EnumSet就是用单个long来表示，
        因此，它的性能比得上位域的性能
        ````
        public class Text_2 {
            //枚举类型
            public enum Style {BOLD, ITALIC, UNDERLINE, STRIKETHROUGH}
    
            //类型安全的
            public void applyStyles(Set<Style> styles){
                //do something
            } 
        }
        
        //使用
        Text_2 text_2 = new Text_2();
        text_2.applyStyles(EnumSet.of(Style.BOLD, Style.ITALIC));
        ````        

33. 用EnumMap代替序数索引
    - 使用序数索引ordinal的场景：
        ````
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
        
        //对花园中的花进行分类
        //花园
        Flower[] garden = {
                new Flower("兰", Flower.Type.SPRING),
                new Flower("竹", Flower.Type.SUMMER),
                new Flower("菊", Flower.Type.AUTUMN),
                new Flower("梅", Flower.Type.WINTER),
                new Flower("桃", Flower.Type.SPRING),
                new Flower("杏", Flower.Type.SUMMER),
                new Flower("梨", Flower.Type.AUTUMN),
        };
    
        //init set数组
        Set<Flower>[] flowersByType = (Set<Flower>[]) new Set[Flower.Type.values().length];
    
        for (int i = 0; i < flowersByType.length; i++)
            flowersByType[i] = new HashSet<>();
    
        //分类
        for (Flower f : garden) 
            flowersByType[f.type.ordinal()].add(f);
    
        //打印
        for (int i = 0; i < flowersByType.length; i++) 
            System.out.println(flowersByType[i]);
    
        //结果
        [桃, 兰]
        [杏, 竹]
        [梨, 菊]
        [梅]
        ````
    上面代码实现了对花园中对植物进行分类，然而存在许多问题，1.数组不能与泛型兼容，需要进行未受检对转换;
    2.set数组并不知道每个索引set的set代表什么；3. 之前有提到不推荐使用ordinal
    - 解决方案：EnumMap
        ````
         //EnumMap:
        //init map
        Map<Flower.Type,Set<Flower>> flowerByType=new EnumMap<>(Flower.Type.class);
    
        for (Flower.Type t :Flower.Type.values())
            flowerByType.put(t,new HashSet<Flower>());
        //分类
        for (Flower f :garden)
            flowerByType.get(f.type).add(f);
    
        //打印
        System.out.println(flowerByType);
        //结果
        {SPRING=[桃, 兰], SUMMER=[杏, 竹], AUTUMN=[梨, 菊], WINTER=[梅]}
        ````    
        
34. 用接口模拟可伸缩的枚举：
    - 虽然无法编写可扩展的枚举类型，却可以通过编写接口以及实现该接口的基础枚举类型，对它进行模拟，这样允许客户端
    编写自己的枚举来实现接口；如果API是根据接口编写的，那么在使用基础枚举类型的任何地方，也都可以使用这些枚举。
    例如下面代码，还是用之前的算数运算符举例：
    （但是这样还是有些不足，就是无法实现从一个枚举类型继承到另一个枚举类型，代码少的当然可以直接复制粘贴，
    如果功能比较多则可以将他们封装在一个辅助类或静态辅助方法中，避免代码的复制工作）
    ````
    
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
    ````
    
35. 注解优先于命名模式
    - 命名模式：有些程序元素需要通过某种工具或框架进行特殊处理
        - 例1：JUnit测试框架原本要求用户一定要用test作为测试方法名的开头
        - 例2：iOS中的init方法要求必须是initXXX()
    - 命名模式缺陷：
        1. 文字拼写错误会导致失败，且没有任何提示，造成错误的安全感，如JUnit的测试方法testXX写成textXX或tsetXX等
        2. 无法确保他们只用于相应的程序元素，如JUnit的命名只对方法生效，将某个类命名testXX是无效的，不会报错，但不会执行测试
        3. 没有提供将参数值与程序元素关联起来的好方法，如JUnit想增加一种测试类别，只在抛出某种特定异常时才会成功, 
        而这个异常类型需要用户通过参数进行自定义，这种实现通过命名模式实现（将异常类型编写到方法名中）并不理想
    - 注解对上面问题的解决，请看下面代码
        ````
        //1.创建注解
        //Retention，Target这种用在注解类型声明中的注解被称为元注解
        @Retention(RetentionPolicy.RUNTIME)//表示test注解应在运行时保留
        //解决问题2：只能用于方法中
        @Target(ElementType.METHOD)//表示test注解只在方法声明中才合法，不能用在类声明，域声明或其他元素
        public @interface Test {
            Class<? extends Exception>[] value();
        }
        //2.使用注解
        //解决问题1：使用注解不会出现拼错而不自知的情况
        //解决问题2：将错误类型以参数传递
        @Test(NullPointerException.class)
        private static void m1() {
            System.out.println("m1");
        }
    
        @Test(NullPointerException.class)
        private static void m2() {
            throw new NullPointerException("空指针了");
        }
    
        @Test({NullPointerException.class,IndexOutOfBoundsException.class})
        private static void m3() {
            throw new RuntimeException("报错了哦");
        }
        //3.解析注解
        private static void testAnno() {
            Class testClass=MainTest.class;
            for (Method m:testClass.getDeclaredMethods()) {
                if (m.isAnnotationPresent(Test.class)){
                    //System.out.println("methodName:"+m.getName()+"_Anno:"+m.getAnnotation(Test.class).toString());
                    try {
                        m.invoke(null);
                    } catch (Throwable throwable) {
                        Throwable exc=throwable.getCause();
                        Class<? extends Exception>[] excTypes= m.getAnnotation(Test.class).value();
                        for (Class<? extends Exception> excType:excTypes){
                            if (excType.isInstance(exc)){
                                System.out.println("触发了"+excType.getSimpleName()+",msg:"+exc.getLocalizedMessage());
                            }
                        }
    
                    }
                }
            }
        }
        ```` 
36. 坚持使用Override注解 
    - 应该在想要覆盖超类声明的每个方法声明中使用Override注解
        - 例如我们经常会重写自定义模型类的equals方法，下面用代码说明使用Override注解的优势
        ````
            //1. 如果不使用注解，我们像下面这些写，程序不会报错，我们也没看出什么问题
            //然而要注意到这里参数类型Person，而Object类中equals方法的参数类型是Object，
            那么这就是对equals方法的重载，而不是重写，那么当使用者调用该方法，并传入一个非Person类型的参数时，
            仍然会调用父类的equals方法
            public boolean equals(Person obj) {
                return true;
            }
        
            //2. 使用Override注解，编译器会对重写对方法进行检查，确保重写语法正确，并对错误对写法有明确对提示信息
            @Override
            public boolean equals(Object obj) {
                if (obj instanceof Person) {
                    Person p = (Person) obj;
                    System.out.println(String.format("hash:_%d, p.hash_%d", this.hashCode(), p.hashCode()));
                    return this.hashCode() == p.hashCode()
                            && this.name.equals(p.name)
                            && this.age == p.age
                            && this.height == p.height;
                } else {
                    retrn false;
                }
            }
        ````              
        - 使用Override还有一点好处，就是可以区分哪些方法是超类对，哪些方法子类扩展对

37. 用标记接口定义类型
    - 标记接口：没有方法声明，只是表示具有某种属性，如Serializable接口
    - 标记接口的优点
        1. 标记接口定义的类型是由被标记类的实例实现的，标记注解则没有这样的类型
        2. 标记接口可以更加精确的被锁定，可以是对其他接口的扩展，也可以被其他标记接口扩展，如Collection和Set
    - 标记注解的优点：
        1. 可以通过默认方式添加一个或多个注解类型的元素，给已被使用的注解类型添加更多的信息，方便扩展
        2. 另一个优点在于它们是更大的注解机制的一部分，因此，标记注解，在那些支持注解作为编程元素之一的框架中同样具有一致性
    - 如何选择？
        - 如果标记是应用到任何程序元素而不只是类或接口，那就必须使用注解
        - 如果只是用于类或接口，需要考虑要编写只接受有这种标记的方法，使用接口作为相关方法的参数类型，
        可以提供编译时就进行类型检查的好处        
        - 是否要永远限制这个标记只用于特殊接口的元素，如果是，最好将标记定义成该接口的一个子接口
     
### 方法

38. 检查参数的有效性
    - 绝大多数方法和构造器对于传递给它们的参数值都会有某些限制，例如，索引值必须非负数，对象引用不能为null，
    我们应该在文档中清楚的指明这些限制，并在方法体的开头处检查这些参数，以强制施加这些限制，清楚的抛出适当的异常，
    以便在发生错误后尽快的检测出错误，例如下面这样：
        ````
        //1. 对于公有方法应该在方法注释中用@throws说明违反参数值限制时会抛出的异常
        /**
         * 对数组的两个元素换位
         *
         * @param array 目标数组
         * @param i     需要交换的索引i
         * @param j     需要交换的另一个索引j
         * @throws NullPointerException           if array is null
         * @throws ArrayIndexOutOfBoundsException if i|j is more than array.length-1
         */
        public void swap(int[] array, int i, int j) {
            if (array == null)
                throw new NullPointerException("数组不能是空");
                
            if (i >= array.length - 1 || j >= array.length - 1)
                throw new ArrayIndexOutOfBoundsException("需要交换的索引应在数组长度范围内");
                
            if (i == j)
                return;
                
            array[i] = array[i] + array[j];
            array[j] = array[i] - array[j];
            array[i] = array[i] - array[j];
        }
        
        //2. 对于私有方法，应该使用断言assert来检查他们的有有效性，并确保只将有效的参数传递进来
        private void swap(int[] array, int i, int j) {
            //断言失败将抛出AssertionError
            assert array != null;
            assert i < array.length && j < array.length;
            if (i == j)
                return;
            array[i] = array[i] + array[j];
            array[j] = array[i] - array[j];
            array[i] = array[i] - array[j];
        }
        ````
        
    - 如果不检查：
        - 可能在处理过程中失败，并产生令人费解的异常
        - 更糟糕的情况是正常返回，但计算出错误的结果
        - 最糟糕的是正常返回，但使得某个对象处于被破坏但状态，在将来某个不确定的时候，某个不想干的点上引发错误
    - 对于某些参数，方法本身没有用到，却被保存起来供以后使用，检验这类参数的有效性尤为重要，构造器就是属于这种情景之一。
    - 例外情况：有些情况下有效性检查工作是非常昂贵的，或是根本不切实际的，或是有效性检查已经隐含在计算过程中完成来
        
39. 必要时进行保护性拷贝
    - 假设类的客户端会尽其所能来破坏这个类的约束条件，因此你必须保护性的设计程序
    - 对于构造器的每个可变参数进行保护性拷贝是必要的
    请看下面例子:
    ````
    //这个类想表示一段不可变的时间周期
    public static final class Period{
        private final Date start;
        private final Date end;

        public Period(Date start, Date end) {
            if (start.compareTo(end)>0)
                throw new IllegalArgumentException("start 不应该在 end 之后");
            this.start = start;
            this.end = end;
        }

        public Date getStart() {
            return start;
        }

        public Date getEnd() {
            return end;
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
    //然而使用时：
    Date start =new Date(1991-1900,2-1,14);
    Date end =new Date(2018-1900,6-1,1);
    Period period=new Period(start,end);
    end.setYear(1970-1900);
    System.out.println(period.toString());
    // 打印结果：
    Period{start=1991-02-14, end=1970-06-01}
    ````
    下面使用保护性拷贝进行改进
    ````
    //1.构造器改进
    public Period(Date start, Date end) {
        this.start = new Date(start.getTime());
        this.end = new Date(end.getTime());
        //注意应先进行保护性拷贝，再对拷贝后的对象进行有效性检查，可以避免"危险阶段"期间从另一个线程改变类的参数
        //危险阶段：指从检查参数开始，到拷贝参数之间的时间段
        if (this.start.compareTo(this.end)>0)
            throw new IllegalArgumentException("start 不应该在 end 之后");
    }
    //2.get方法改进
    public Date getStart() {
        return new Date(start.getTime());
    }

    public Date getEnd() {
        return new Date(end.getTime());
    }
    //这样period对象就是真正不可变的了
    ````
    - 保护性拷贝会使性能收到一定损失，如果类信任它的客户端不会进行不恰当的修改组件，可以在文档中指明客户端的职责是：不得修改
    受到影响的组件，以此来代替保护性拷贝。
    
40. 谨慎设计方法签名
    - 谨慎的选择方法的名称：
        方法名称应始终遵循标准的命名习惯，1. 应易于理解并与同包中其他方法名称风格一致，2. 应选择与大众认可的名称相一致的名称，如java类库的API
    - 不要过于追求提供便利的方法：
        每个方法都应该尽其所能，功能齐全，方法太多会使类难以学习，使用，文档化，测试和维护，会使接口的实现者和用户的工作变得复杂        
    - 避免过长的参数列表
        - 目标是4个或更少，相同类型的长参数序列格外有害，如margin(int l,int t,int r,int b...)，很可能不小心弄错顺序，
        而程序仍可编译运行，然而的到错误的结果，缩短参数列表的方法：
            - 把方法分分解成多个方法
            - 创建辅助类      
            - 使用建造者模式（builder模式）
        - 对于参数类型，要优先使用接口而不是类，例如，我们经常使用的集合类，应优先使用Map做参数而不是HashMap，优先使用List而不是ArrayList
        - 对于boolean参数，要优先使用两个元素的枚举类型，更具有易读性，方便编写
        
41. 慎用重载
    - 对于重载方法的选择是静态的，对于重写/覆盖方法的选择是动态的，选择被覆盖的方法的版本是在运行时进行的，选择的依据是被调用方法所在对象的运行时类型        
    例如下面代码中对问题：想用重载区分传入参数对实际类型，然而结果只是选择了引用声明的类型对应的重载方法
        ````
        //定义三个类，对name方法分别进行重写，anim中对name重载
        public static class Anim {
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
        //调用
        Anim[] anims = {new Anim(), new Cat(), new TomCat()};
        for (int i = 0; i < anims.length; i++) {
            System.out.println("重写.name:" + anims[i].name());
            System.out.println("重载.name:" + Anim.name(anims[i]));
        }
        System.out.println("重载.name:" + Anim.name(new Anim()));
        System.out.println("重载.name:" + Anim.name(new Cat()));
        System.out.println("重载.name:" + Anim.name(new TomCat()));
        //结果：
        重写.name:animal
        重载.name:Anim
        重写.name:cat
        重载.name:Anim
        重写.name:tom cat
        重载.name:Anim
        
        重载.name:Anim
        重载.name:Cat
        重载.name:TomCat
        ````
    - 一个安全而保守的策略是：
        - 永远不要导出两个具有相同参数数目的重载方法（可以给方法起不同的名称而不是使用重载机制），对于可变参数（varargs）不要重载它。
        例如ObjectOutputStream类的write方法：writeBoolean(boolean),writeInt(int),writeLong(long)，而不是使用重载。
        - 对于构造器则是应该进了使用静态工厂方法代替重载，或使用builder模式
        - 如果几个重载方法的参数直接不存在任何关系，如read(int),read(List),read(String[]),当然是可以使用重载的
        - 自动装箱引发的一个问题，下面程序中的remove，希望去除0，1，2；然而由于自动装箱对Integer对象和int值进行了转换，
        导致调用了remove不同对重载方法，所以得到了意料外对结果。
        ````
        Set<Integer> set =new TreeSet<>();
        List<Integer> list=new ArrayList<>();
        for (int i = -3; i < 3 ; i++) {
            set.add(i);
            list.add(i);
        }
        System.out.println("111-->"+set+"_"+list);
        for (int i = 0; i < 3; i++) {//期望去掉0，1，2
            set.remove(i);//set只有一个remove(obj)
            list.remove(i);//list有两个重载的remove(obj)和remove(int)
            //这里调用了list.remove(int index)
            //改进方法是：
            //list.remove(Integer.valueOf(i));
        }
        System.out.println("222-->"+set+"_"+list);
        //结果：
        111-->[-3, -2, -1, 0, 1, 2]_[-3, -2, -1, 0, 1, 2]
        222-->[-3, -2, -1]_[-2, 0, 2]
        ````
        
42. 慎用可变参数
    - 可变参数方法：可匹配不同长度的变量的方法，接收0或多个指定类型的参数，其机制是通过先创建一个数组，
    数组的大小为调用位置所传递的参数数量，然后将参数值传到数组中，最后将数组传递给方法
    - 可变参数为了printf和反射机制而设计,如printf(String format, Object ... args)
    - 不必改造具有final数组参数的每个方法，只当确实是在数量不定的值上执行调用时才使用可变参数
    - 缺点：
        - 客户端调用这个方法时可能没有传递参数进去，编译时并不会失败，但可能会造成运行时错误，应进行显示的有效性检查，
        如args.length==0的情况。
        - 可变参数方法的每次调用都会导致进行一次数组分配和初始化
    - 如果真的需要可变参数的灵活性，可以考虑下面重载的实现方案：
    ````
    public void foo(){}
    public void foo(int a1){}
    public void foo(int a1,int a2){}
    public void foo(int a1,int a2,int a3){}
    public void foo(int a1,int a2,int a3,int... ans){}
    ````
    
43. 返回零长度的数组或集合，而不是null
    - 若直接返回null，几乎每次用到该方法时都需要额外的代码来处理null的情况，一旦忘记处理就会出错；
    有人可能觉得返回null避免来分配数组或创建对象的开销，然而
    1. 相对于一个可能导致运行时崩溃的定时炸弹，此时担心性能问题是不明智的，
    2. 可以用一个私有静态的空数组进行共享，避免每次调用都创建新的对象
    3. 可以使用java内置的空集合避免自己创建的开销：Collections.emptyList(),Collections.emptyMap(),Collections.emptySet();
    
44. 为所有导出的API元素编写文档注释
    - 为了正确的编写API文档，必须在每个被导出的类，接口，构造器，方法和域声明之前增加一个文档注释，方便用javadoc导出api文档
    - 方法的文档注释应该简洁的描述出它和客户端之间的约定。
    
### 通用程序设计

45. 将局部变量的作用域最小化
    - 优点：增强代码的可读性和可维护性，并降低出错的可能性
    - 最有力的方法就是在第一次使用它的地方声明，几乎每个局部变量的声明都应该包含一个初始化的表达式，
    如果还没有足够的信息进行初始化，就应该推迟这个声明；
    - for（i，each）循环都支持声明循环变量，所以当循环终止后不再需要循环变量的内容时，for循环就优先于while循环；
    
46. for-each循环优先于for-i循环
    - 使用(集合)迭代器iterator或(数组)for-i的循环变量，由于循环变量会使用多次，且容易和外部的变量搞混，导致出错；
      for-each则隐藏了循环变量，避免了混乱和出错的可能。
    - 性能优势，对于数组索引的边界值只计算一次。
    - 对比下面代码
    ````
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
    ````  
    - for-each不仅可以遍历集合和数组，还可以遍历任何实现Iterator接口的对象
    - 简洁性 ，预防bug，且没有性能损失
    - 无法使用for-each情况：
        1. 过滤：如果需要遍历集合，并删除选定的元素，就要使用显示的迭代器，以便调用其remove方法
        2. 转换：如果需要修改集合或数组的值，就需要集合的迭代器或数组索引了
        3. 平行迭代：如果需要并行的遍历多个集合或数组，就需要用到迭代器或数组索引
        
47. 了解和使用类库
    - 看下面求随机数方法：
    ````
    public static final Random rnd=new Random();
    /**
     * 求n以内的随机数
     */
    public static int random(int n){
        //生成一个随机数，取绝对值，再%n
        return Math.abs(rnd.nextInt())%n;
    }
    ````
    这个方法表面看起来是没有什么问题的，然而，实际上却有三个缺点：
        1. 如果n是2的乘方，一段周期后，它生成的随机数将重复
        2. 如果n不是2的乘方，则平均起来，有些数出现的更加频繁，尤其n比较大时
        3. 极少数情况下会返回一个指定范围外的数，如生成的随机数为Integer.MIN_VALUE，Math.abs会返回负数MIN_VALUE，如下：
        ````
        System.out.println("模拟随机出Integer.MIN_VALUE情况："+Math.abs(Integer.MIN_VALUE)%11);
        ````
    修正方案是使用Random的nextInt(int n)方法；
    而如果要自己修正就需要了解伪随机数生成器，数论和2的求补算法的相关知识；        
    - 使用类库的优点：
        1. 充分利用前人的知识和经验
        2. 不必浪费时间处理与工作不太相关的底层细节
        3. 有专人或组织维护，性能随着版本的迭代会不断提高，bug也会被逐渐发现并修复，功能也会不断丰富；
    - 所以说不管是java还是Android，及时关注官方的api更新说明还是很有必要的，因为如果你不知道这些类库或者新增加的功能，
    可能会花费百倍千倍的时间去写一些没有必要，甚至可能有bug的的代码，总之不要重复造轮子；
    
48. 如果需要精确的答案，请避免使用float和double
    - float,double为科学计算和工程计算而设计，执行二进制浮点运算，然而它们并没有提供精确的结果，尤其不适合用于货币计算；
    例如下面代码：
    ````
    double num = 1.00;
    int count = 0;
    for (double i = 0.10; num >= i; i += 0.10) {
        num-=i;
        count++;
        System.out.println("num:"+num);
    }
    System.out.println("count:"+count);
    
    //输出结果
    num:0.9
    num:0.7
    num:0.3999999999999999
    count:3
    ````
    修正方案：用BigDecimal代替double
    ````
    final BigDecimal temp=new BigDecimal(".10");
    int count=0;
    BigDecimal num=new BigDecimal("1.00");
    for (BigDecimal i=temp;num.compareTo(i)>=0;i=i.add(temp)){
        num=num.subtract(i);
        System.out.println("num:"+num);
        count++;
    }
    System.out.println("count:"+count);
    
    //输出结果
    num:0.90
    num:0.70
    num:0.40
    num:0.00
    count:4
    ````
    - 然而BigDecimal用起来比较麻烦，而且效率和性能要低(要创建BigDecimal对象)
    另一种方法是使用int或long代替（根据具体数值大小决定），例如上面例子中的数值都*100。
    - BigDecimal还有一个优点就是可以完全控制舍入，并提供了8种舍入模式
    - 还有一点就是9位数以内用int，18位以内用long，超过18位就必须用BigDecimal了
    
49. 基本类型优先于装箱基本类型
    - 基本类型只有值，而装箱基本类型不同的对象可以有相同值，相同值的可以是不同的对象
    - 基本类型只有功能值，装箱基本类型还可以为null
    - 基本类型更节省时间和空间
    - 任何情况下，当一项操作中混合使用基本类型和装箱基本类型时，装箱基本类型就会自动拆箱
    如Integer i； if(i==1){...}，会NullPointerException，正是由于拆箱时发现i==null；
    而且反复的装箱拆箱是会影响性能的，而且还可能导致一些你想不到的问题；
    
50. 如果其他类型更合适，则尽量避免使用字符串
    - 字符串不适合代替其他的值类型（基本数据类型 & 对象引用）
    - 字符串不适合代替枚举类型（如前面的第30条）
    - 字符串不适合代替聚集类型（如果一个实体有多个组件，用一个字符串表示这个实体通常是很不恰当的）
    - 字符串不适合代替能力表
        - 例如想设计一个线程局部变量ThreadLocal：
        ````
        public class ThreadLocal{
            private ThreadLocal(){}
            private static Map<String,Object> map=new HashMap<>();
            public static void set(String key,Object value){
                map.put(key,value);
            }
            
            public static Object get(String key){
                return map.get(key);
            }
        }
        ````
        像上面这样写可以，但是又一个前提是key不能重复，如果多个线程（无意或故意的）用了相同的key，就会导致这个变量被
        多个线程共享，安全性很差；
        可以想下面这样,各线程通过getKey获取key，再以这个key进行变量的存取：
        ````
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
        ````
        当然如果有兴趣也可以看一些java.util.ThreadLocal的实现
    
51. 当心字符串连接的性能
    - 我们使用字符串经常这样写"String name = "Mr."+"刘"；",很方便，如果只是少量的使用确实可以；
    - 但是：为连接n个字符串而重复的使用字符串连接操作符(+),需要n的平方级的时间。
    因为字符串是不可变的，+操作要创建新的对象，并拷贝要相加的两个对象；
    - 为了获得可以接受的性能，请用StringBuilder替代String；相加次数越多，性能的差距是成平方级增长的；
    若有兴趣可以写个for循环打印时间戳试一下；
    - 这一条其实我们刚入行时就都已经知道了，主要是平时开发过程中要注意应用；

52. 通过接口引用对象
    - 如果有合适的接口类型存在，那么对于参数，返回值，变量和域来说，就都应该使用接口类型进行声明；
      只有当用构造器创建某个对象时，才需要真正的引用这个对象的类；
      如: List list = new ArrayList();或 public void methodA(List list){...}
    - 养成用接口作为类型的习惯，你的程序将更加灵活；（例如有新的实现可以提供更好的性能，可以方便的进行更换）
    - 如果依赖域现实的任何特殊属性，就要在声明变量的地方给这些需求建立相应的文档说明；
    - 不适合的情况：
        - 值类String，BigInteger等，一般是final的
        - 对象属于给于类的框架，就应该使用基类（一般是抽象类），如java.util.TimerTask抽象类
        - 类提供类接口不存在的额外方法，切程序依赖于这些额外方法，如LinkedHashMap
        
53. 接口优先于反射机制
    - 核心反射机制java.lang.reflect,提供了"通过程序来访问关于已装载的类的信息"的能力；
    如获得Constructor，Method，Field；
    - 然而也要付出相应的代价：
        - (将编译期错误推迟到了运行时)丧失了编译时类型检查的好处，包括异常检查，且调用不存在或不可访问的方法时，将在运行时失败；
        - 执行反射所需要的代码非常笨拙和冗长，写着乏味，读着困难；
        - 性能损失，调用反射方法比普通方法慢了许多；
    - 反射功能通常只是在设计时被用到，普通的应用程序在运行时不应该以反射的方式访问对象；
    - 有些复杂的应用程序需要使用到反射机制，如浏览器，对象监视器，代码分析工具，解释型的内嵌式系统，
      他们必须用到编译时无法获取的类，但是存在适当的接口或者超类，这种情况，可以通过反射创建实例，
      通过接口或超类以正常的方式访问这些实例，如果所用构造器不带参数，可以考试使用Class.newInstance,
      而非java.lang.reflect;
    
54. 谨慎的使用本地方法
    - JNI(Java Native Interface)允许java程序调用本地方法(native method, 指本地程序语言c/c++编写的方法)；
    - native方法的三种用途
        - 提供"访问特定于平台的机制"的能力，如访问注册表，文件锁
        （随着ava的发展已经在逐渐完善这些功能，如java,util.prefs提供了注册表的功能，SystemTray提供访问桌面系统托盘区的能力）
        - 提供访问遗留代码库的能力，从而可以访问遗留数据
        - 通过本地语言编写程序中注重性能的部分，提高程序性能（不值得提倡，随着ava的发展性能已经被不断的优化了）
    - native方法的缺点：
        - 本地语言是不安全的，所以，使用本地方法的应用程序也不再能避免受内存毁坏错误的影响；
        - 本地语言是平台相关的，所以，使用本地方法的程序也不再是可自由移植的；
        - 比较难调试，
        - 进入和退出本地代码时，需要一定的开销，所以如果只是使用本地代码做少量工作，反而可能降低性能
        - 需要"胶合代码"的本地方法编写起来单调乏味；
      
55. 谨慎的进行优化
    - 3条优化相关的格言
        - 很多计算上的过失都被归咎于效率，而不是任何其他原因，甚至包括盲目的做傻事；
        - 不要去计较效率上的一些小小的得失，在97%的情况下，不成熟的优化才是一切问题的根源      
        - 优化方面应该遵守两条规则：1. 不要优化；2. (仅针对专家)还是不要进行优化
        （可能你认为程序慢的地方并没有问题；可以使用性能剖析工具检测代码）
    - 总结：优化弊大于利，特别是不成熟的优化；
    - 要努力编写好的程序而不是快的程序，不要因为性能而牺牲合理的结构；
    （再多底层的优化也无法弥补算法选择的不当）
    - 但必须在设计过程中考虑到性能问题：
        - 避免那些限制性能的设计决策，尤其是API，线路层wire-level协议，永久数据格式） 
        - api设计对性能的影响：
            - 公有类型可变，会导致大量不必要的保护性拷贝；
            - 适合用复合模式时却使用继承，会导致与超类永远束缚在一起，限制子类性能；
            - 使用实现类型代替接口，会把程序束缚在具体的实现上，即使将来有更优的实现也无法使用；
            
56. 遵守普遍接受的命名惯例
    - 包名：以组织的Internet域名开头，且顶级域名放前面，如com.ljy; 且不应以java,javax开头；
    - 类和接口名：一个或多个单词组成（通常是名词或形容词），每个单词首字母大写，尽量避免使用缩写，尤其是你自己创造的缩写；
    - 方法和域的名称：一个或多个单词（方法通常用动词，动词短语或形容词），除第一个单词外首字母大写；
    - 常量域：唯一推荐使用下划线的情形；
    - 类型参数名称：通常由单个字母组成，常用的一般由五种：T表示任意的类型，E表示集合的元素类型，K和V表示映射的键和值，X表示异常；
      （过个时可以这样：T1,T2,T3...）

### 异常

57. 只针对异常的情况才使用异常
    ```
    private static void testException() {
        int[] range={1,2,3,4,5,6};
        try {
            int i=0;
            while (true)
                System.out.println("i="+range[i++]);
        }catch (ArrayIndexOutOfBoundsException e){
        }
    }
    ```
    - 上面代码有什么问题呢？
        - 试图通过抛出异常并忽略的方式终止无限循环；意图避免for循环的越界检查
        - 然而：
            1. 异常机制的设计初衷适用于不正常的情形，so，很少有JVM对其进行优化；
            2. 把代码放在try-cache中阻止了现代JVM实现本来可能要执行的某些特定优化；
            3. for遍历并不会导致冗余的检查，有些现代的JVM会将它们优化掉；
        - 上面的代码模糊了代码意图，降低了性能，还不能保证正常工作，
            例如循环体中的计算过程调用了一个方法，这个方法执行了对某个不想关的数组的越界访问，
            出发异常，而该try语句块会忽略掉这个bug，增加了调试过程的复杂性；
    - 对api设计的启发:设计良好的api不应该强迫它的客户端为了正常的控制流而使用异常;
        例如Iterator有个hasNext方法可以判断是否终止循环,如果没有提供,可能用户就会不得不使用上面的代码实现了;

    - 总之,异常就是为了异常情况下使用而设计,不要用于普通的控制流,也不要编写迫使别人这样做的API;

58. 对可恢复的情况使用受检异常,对编程错误使用运行时异常
    - java的三种可抛出结构(throwable):
        - 受检异常(checked exception)
        - 运行时异常(run-time exception)
        - 错误(error)
    - 如果期望调用者能够适当的恢复，则通过抛出受检异常,强迫调用者在一个catch子句中处理改异常或者将其传播出去;
    - api的设计者让api用户面对受检的异常,以此强制用户从这个异常条件中回复;
        用户可以忽略这样的强制要求,只需要捕获并忽略即可,但这往往不是一个好办法;
        如File,io流操作是常见的IOException,FileNotFoundException。

     - 另外两种则是不需要，也不应该被捕获的
     - 用运行时异常表明编程错误；大多数表示前提违例( precondition violation)；
        前提违例是指api用户没有遵守api规范建立的约定，如ArrayIndexOutOfBoundsException,NullPointerException；
     - 错误被jvm保留用于表示资源不足，约束失败，或其他使程序无法继续执行的条件；
        因此最好不要实现任何新的Error子类

    - api设计者往往会忘记，异常也是个完全意义上的对象，可以在它上面定义任意方法，
        这些方法主要用于为捕获异常的代码提供额外的信息，受检的异常往往指明了可恢复的条件，
        所以对于这样的异常，提供一些辅助方法尤为重要，可以帮助调用者获得一些有助于恢复的信息

59. 避免不必要的使用受检异常

    - 受检异常强迫程序员处理，大大增强了可靠性
    - 过分的使用受检的异常会使api使用起来非常不方便（要做try-catch处理或抛出）
    - 所以设计api时要谨慎使用受检异常，如果使用api的程序员无法做的比下面的更好，那么使用未受检的异常更为合适；
    ```
    1.
    try{
        ...
    } catch(TheCheckedException e){
        throw new AssertionError();//Can't happen!
    ｝
    2.
    try{
        ...
    } catch(TheCheckedException e){
        e.printStackTrace();//on well,we lose!
        System.exit(1);
    ｝
    ```
    其实仔细回想一下，之前没少写上面这种代码，呵呵~~
    - 应该问问自己，是否有别的途径来避免使用受检异常
    - 一种解决方法是：把抛出异常的方法分成两个方法，其中一个返回boolean，
    即使用if判断来处理是否抛出异常的两种情况，就如之前提过的Iterator有个hasNext方法；
    (但是这样可能会失去强制约束，api使用者不一定会调用如Iterator.hasNext()这种方法,
    这就需要完善的api说明文档来规范使用者的调用了)

60. 优先使用标准的异常
    - 专家与菜鸟的一个主要区别: 高度的代码重用,这是一条通用规则,异常也不例外,
    本条目将讨论常见的可重用异常(未受检);
    - 重用现有异常的好处:
        1. 使api更加易于学习和使用(通用,习惯用法);
        2. 对用到这些api的程序而言,可读性会更好(不会出现很多不熟悉的异常);
        3. 异常类越少,内存印记(footprint)就越小,装载这些类的时间开销就越少;
    - 如果希望增加更多的失败-捕获信息,可以把现有的异常进行子类化
    - 常用异常:
        - IllegalArgumentException: 非法参数（调用者传参不合适时）
        ```
         public void setGVPAdapter(GVPAdapter adapter) {
            if (null == adapter) {
                throw new IllegalArgumentException("适配器不能为空");
            }
            ...
        }
        ```
        - IllegalStateException：非法状态（被调用的程序中某个对象的状态不满足程序运行需求）
        ```
        public View getFooterView(int position) {
            if (mFooterViewInfos.isEmpty()) {
                throw new IllegalStateException("you must add a FooterView before!");
            }
            return mFooterViewInfos.get(position).view;
        }
        ```
        - NullPointerException: 这个就很常用了, 某个不允许为空的对象或参数为空时
        - IndexOutOfBoundsException: 这个也很熟悉了,操作数组时经常会遇到他的子类
        ```
         /**
         * 对数组的两个元素换位
         *
         * @param array 目标数组
         * @param i     需要交换的索引i
         * @param j     需要交换的另一个索引j
         * @throws NullPointerException           if array is null
         * @throws ArrayIndexOutOfBoundsException if i|j is more than array.length-1
         */
        public static void swapPublic(int[] array, int i, int j) {
            if (array == null)
                throw new NullPointerException("数组不能是空");
            if (i >= array.length - 1 || j >= array.length - 1)
                throw new ArrayIndexOutOfBoundsException("需要交换的索引应在数组长度范围内");
            if (i == j)
                return;
            array[i] = array[i] + array[j];
            array[j] = array[i] - array[j];
            array[i] = array[i] - array[j];
        }
        ```
        - ConcurrentModificationException: 如果一个对象被设计为专用于单线程或与外部同步机制配合使用,
            一旦发现它正在或已经被并发的修改,就应该抛出这个异常;
        - UnsupportedOperationException: 对象不支持用户请求的方法；

61. 抛出与抽象相对应的异常
    - 如果方法抛出的异常与它所执行的任务没有明显的联系，将会使人不知所措；
    当放过传递由低层抽象抛出的异常时，往往会发生这种情况，这也让实现细节污染了更高层的api；
    - 异常转译：更高层的实现应该捕获低层的异常，同时抛出可以按照高层抽象进行解释的异常；
    ```
        //取自AbstractSequentialList：
        public E get(int index) {
            try {
                return listIterator(index).next();
            } catch (NoSuchElementException exc) {
                throw new IndexOutOfBoundsException("Index: "+index);
            }
        }
    ```
    - 异常链：如果低层异常对于调试导致高层异常的问题非常有帮助，
    可以将低层异常传到高层异常，高层异常提供访问方法（Throwable.getCause()）来获取低层异常：
    ```
    try{
        ...//todo
    }catch(LowerLevelException cause){
        throw new HigherLevelException(cause);
    }

    //高层异常的构造器将原因传到支持链的超级构造器
    class HigherLevelException extends Exception{
        HigherLevelException(Throwable cause){
            super(cause);
        }
    }
    ```
    - 处理来自低层的异常最好的做法是:在调用低层方法之前确保他们会执行成功,从而避免他们抛出异常,如检查参数的有效性;
    - 如果无法避免,次选方案是,让高层来悄悄绕开这些异常,从而将高层方法的调用者与低层问题隔离,可以使用适当的记录机制将异常记录下来,有助于管理员调查问题;

62. 每个方法抛出的异常都要有文档
    - 始终要单独的声明受检的异常,并利用javadoc的@throws标记,准确的记录下抛出每个异常的条件;
    - 一个方法需要抛出多个异常类时,不要用这些异常的超类或Exception,Throwable,代替,这样不仅没有提供"这个方法能够抛出哪些异常"的指导信息,
    而且大大妨碍了该方法的使用,因为它实际上掩盖了该方法在同样的执行环境下可能抛出的任何其他异常;
    - 未受检异常最好也使用javadoc的@throws标签记录,但不要使用throws关键字将未受检的异常包含在方法的声明中
    - 如果一个类中的许多方法处于同样的原因而抛出同一个异常,该类的文档注释中对这个异常建立文档也是可以的;
    如"All methods in this class throw a NullPointerException if a null object reference is passed in any parameter";

63. 在细节消息中包含能捕获失败的信息
    - 为了捕获失败,异常的细节信息应该包含所有"对该异常有贡献"的参数和域的值; 
     
      
    
    
    
    
    
    