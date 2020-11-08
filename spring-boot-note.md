# ayspringboot学习笔记

## 一、配置文件

### 1. 配置文件

#### 1.1. properties文件配置

```yaml
server.port=8081
server.address=127.0.0.1

student.name=Mountain-dev
student.age=14
student.achievement.english=98
student.achievement.math=88
student.lesson=english, math, chinese
student.dog.dogName=xiaoTianQuan
student.dog.dogColor=yellow
```

#### 1.2. yaml文件配置

```yaml
server:
  address: 127.0.0.2
  port: 8088
student:
  name: mountain-yaml-prod
  age: 20
#  ""双引号内的内容表示，不转义，根据内容自动处理
#  lesson: ["a", "b \n c"]
# ''单引号内容表示，转义，全部按照正常字符串来处理
  lesson: ["a", 'b \n c']
  achievement: {"geogrophy": 88, "history": 97}
  dog:
#    dogName: Jane
    dogColor: gray
#    属性本来命名是驼峰法dogName，yaml文件中支持松散语法绑定
    dog-name: July
#    dog_name: July
```

#### 1.3. @Value与@ConfigurationProperties

|                    | @ConfigurationProperties | @Value     |
| ------------------ | ------------------------ | ---------- |
| 功能               | 批量注入配置文件中的属性 | 一个个指定 |
| 松散绑定(松散语法) | 支持                     | 不支持     |
| SpEL               | 不支持                   | 支持       |
| JSR303数据校验     | 支持                     | 不支持     |

**SpEL**:  #{5*2} 结果为 10

**JSR303**: @Email  || @AssertTrue || @Null 等注解验证

#### 1.4. @PropertySource&@ImportResource

**@PropertySource**:加载指定的配置文件

```java
/**
 * 将配置文件中配置的每一个属性值，映射到这个组件中
 * @ConfigurationProperties: 告诉springBoot将本类的所有属性和配置文件中相关的配置进行绑定，默认从全局文件中获取
 *  prefix 将类映射成对应属性名，若不添加则默认类名
 *
 *  @Component： 声明该配置是springboot的一个组件
 *  @PropertySource: 加载指定的配置文件
 */
@Data
@Component
@ConfigurationProperties(prefix = "student")
@PropertySource(value = {"classpath:student.properties"})
public class Student {

    private String name;

    private int age;

    private Map<String, String> achievement;

    private List<String> lesson;

    private Dog dog;

    private String parent;
}
```

**@ImportResource**：导入spring配置文件，让配置文件内容生效

```java
@SpringBootApplication
@ImportResource(locations = {"classpath:beans.xml"})
public class SpringBootYamlApplication {

   public static void main(String[] args) {
      SpringApplication.run(SpringBootYamlApplication.class, args);
   }

}
```

**beans.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean class="com.mountain.spring.boot.yaml.service.HelloService" id="helloService">

    </bean>
</beans>
```

==》优化：springboot推荐给容器添加组件方式一般使用 @Configuration标注指明当前类是个配置类

```
/**
 * 标志当前类为配置类
 */
@Configuration
public class MyAppConfig {
    
    @Bean
    public DataSource initDataSource() {
        return null;
    }
}
```

#### 1.5. 配置文件占位符

a. **随机数**

${random.value}、${random.int}、${random.long}、${random.int(10)}

b.占位符获取之前配置的值，如果没有可以指定默认值 : ${perison.name:李四}

### 2. 自动配置原理

​	springboot启动时候的时候加载主配置类，@EnableAutoConfiguration自动加载配置

@EnableAutoConfiguration

```java
@Import(AutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration {
```

```java
protected AutoConfigurationEntry getAutoConfigurationEntry(AnnotationMetadata annotationMetadata) {
   if (!isEnabled(annotationMetadata)) {
      return EMPTY_ENTRY;
   }
   AnnotationAttributes attributes = getAttributes(annotationMetadata);
   List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes);
```

```java
protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes) {
   List<String> configurations = SpringFactoriesLoader.loadFactoryNames(getSpringFactoriesLoaderFactoryClass(),
         getBeanClassLoader());
   Assert.notEmpty(configurations, "No auto configuration classes found in META-INF/spring.factories. If you "
         + "are using a custom packaging, make sure that file is correct.");
   return configurations;
}
```

```java
public static List<String> loadFactoryNames(Class<?> factoryType, @Nullable ClassLoader classLoader) {
   String factoryTypeName = factoryType.getName();
   return loadSpringFactories(classLoader).getOrDefault(factoryTypeName, Collections.emptyList());
}
```

```java
public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/spring.factories";

private static Map<String, List<String>> loadSpringFactories(@Nullable ClassLoader classLoader) {
   MultiValueMap<String, String> result = cache.get(classLoader);
   if (result != null) {
      return result;
   }

   try {
       // 加载srping.facotries文件加入到容器中
      Enumeration<URL> urls = (classLoader != null ?
            classLoader.getResources(FACTORIES_RESOURCE_LOCATION) :
```

**案例**：HttpEncodingAutoConfiguration为例

```java
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ServerProperties.class) // 启动指定类
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET) // @Conditional 根据不同条件，如果满足指定的条件，整个配置类里面的配置才会生效；ConditionalOnWebApplication配置当前应用是否是web应用，如果是，当前配置类生效
@ConditionalOnClass(CharacterEncodingFilter.class) // 判断当前项目有没有这个类；乱码解决过滤器
@ConditionalOnProperty(prefix = "server.servlet.encoding", value = "enabled", matchIfMissing = true)
public class HttpEncodingAutoConfiguration {
```