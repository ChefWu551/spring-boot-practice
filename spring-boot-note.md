# springboot学习笔记

资源文档： https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/reference

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

## 二、日志

springboot日志资源地址：https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/reference/htmlsingle/#boot-features-logging

### 1. 日志框架

| 日志门面(日志的抽象层)                | 日志实现                    |
| ------------------------------------- | --------------------------- |
| SLF4j(simple logging Facade for java) | Log4j, JUL, log4j2, Logback |
|                                       |                             |
|                                       |                             |

日志门面（SLF4j）+日志实现(Logback):

springboot: 底层是spring框架，spring框架默认使用JCL  ->  修改为slf4j+logback

### 2. SLF4j使用

资源地址：http://www.slf4j.org/manual.html

#### 2.1. 如何在系统中使用SLF4j

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorld {
  public static void main(String[] args) {
    Logger logger = LoggerFactory.getLogger(HelloWorld.class);
    logger.info("Hello World");
  }
}
```

​	日志记录方法的调用，不应该直接调用日志的实现类，调用日志抽象层里面的方法



图示

![concrete-bindings](image\concrete-bindings.png)

每一个日志的实现框架都有自己的配置文件，使用slf4j以后，**配置文件还是做成日志实现框架自己本身的配置文件**

#### 2.2. 统一日志框架-slf4j

资源地址：http://www.slf4j.org/legacy.html

集成多个框架，每个框架都有自己的日志框架，需要统一使用统一使用一种框架来处理=》slf4j

![legacy](image\legacy.png)

**操作步骤：**

==a. 排除原有的其他日志框架；==

==b. 用中间包（jcl-over-slf4j.jar / log4j-over-slf4j / jul-to-slf4j.jar/ ...）来替换原有的日志框架==

==c. 导入slf4j其他的实现==

### 3. springboot-logging

```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter</artifactId>
</dependency>
```

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-logging</artifactId>
  <version>2.3.5.RELEASE</version>
  <scope>compile</scope>
</dependency>
```

logback-classic：使用logback记录日志

jul-to-slf4j / log4j-to-slf4j : 把其他日志转为slf4j 的相关依赖，导入了日志抽象层

![springboot-logging](image\springboot-logging.png)

总结：

​	a. springboot底层也是使用slf4j+logback的方式进行日志记录

​	b. springboot也把其他的日志替换成了slf4j；

​	c. 中间替换包

![jul-to-slf4j](D:\codeProject\spring-boot-practice\image\jul-to-slf4j.png)

​	d. 如果我们要引入其他框架，一定要把框架默认日志依赖移除掉，否则会出现依赖冲突(springboot 改写的和框架本来的)

### 4. springboot-日志使用

**配置日志及使用**：

```properties
logging.level.com.mountain=trace

#logging.file.path=

logging.file.name=logs/logging_${random.uuid}.txt
# 控制台输出日志格式
logging.pattern.console=
# 指定文件中日志输出的格式
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n
```

```java
@SpringBootTest
class LoggingApplicationTests {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    void contextLoads() {
        logger.trace("trace log;");
        logger.debug("debug log");
        logger.info("info");
        logger.warn("warn log");
        logger.error("error log");
    }

}
```

**springboot logback配置文件示例**

![springboot-logback](image\springboot-logback.png)

###   5. 自定义配置

![springboot-logging-map](image\springboot-logging-map.png)

logback-spring.xml: 日志框架就不直接加载日志的配置项，有springboot高级使用

```xml
<!-- 可指定对应的运行环境 -->

<springProfile name="staging">
    <!-- configuration to be enabled when the "staging" profile is active -->
</springProfile>

<springProfile name="dev | staging">
    <!-- configuration to be enabled when the "dev" or "staging" profiles are active -->
</springProfile>

<springProfile name="!production">
    <!-- configuration to be enabled when the "production" profile is not active -->
</springProfile>
```

**logback.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration  scan="true" scanPeriod="60 seconds" debug="false">
    <contextName>spring-boot-logging</contextName>
    <!--输出到控制台-->
    <!--<appender name="console" class="ch.qos.logback.core.ConsoleAppender">-->
        <!--<encoder>-->
            <!--<pattern>%d{HH:mm:ss.SSS} %contextName [%thread] %-5level %logger{36} - %msg%n</pattern>-->
        <!--</encoder>-->
    <!--</appender>-->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} %contextName [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    <!--输出到文件-->
    <appender name="FILE"
              class="ch.qos.logback.core.rolling.RollingFileAppender">
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/mylog.%d{yyyy-MM-dd}.log</fileNamePattern>
        </rollingPolicy>
        <encoder>
            <pattern>%relative [%thread] %level %logger - %msg%n</pattern>
        </encoder>
    </appender>

    <root level="TRACE">
        <appender-ref ref="CONSOLE" />
        <appender-ref ref="FILE" />
    </root>

    <logger name="com.mountain" level="TRACE"/>

</configuration>
```

### 6. 日志切换（slf4j替换成log4j 或 切换成log4j2）

![slf4j-to-log4j](image\slf4j-to-log4j.png)

#### 6.1. 排除logback

```xml
<dependencies>
   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter</artifactId>
      <exclusions>
         <exclusion>
            <artifactId>logback-classic</artifactId>
            <groupId>ch.qos.logback</groupId>
         </exclusion>
         <exclusion>
            <artifactId>log4j-to-slf4j</artifactId>
            <groupId>org.apache.logging.log4j</groupId>
         </exclusion>
      </exclusions>
   </dependency>
</dependencies>
```

#### 6.2. 导入slf4j-log4j12依赖

```xml
<dependency>
   <groupId>org.slf4j</groupId>
   <artifactId>slf4j-log4j12</artifactId>
</dependency>
```

![slf4j-log4j12](image\slf4j-log4j12.png)

#### 6.3. 增加log4j配置文件 log4j.properties

此时 配置文件将自动被框架识别！

## 三、web开发

### 1. 自动配置

xxxAutoConfiguration: 自动配置组件

xxxProperties: 配置类来封装配置文件内容

### 2. springboot对静态资源的映射规则

#### 2.1. 以webjars引入资源

资源连接：https://www.webjars.org/ 筛选对应前端组件的依赖

```
<dependency>
   <groupId>org.webjars</groupId>
   <artifactId>jquery</artifactId>
   <version>3.5.1</version>
</dependency>
```



**WebMvcAutoConfiguration**自动解析webjars的内容

```java
org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
```

```java
@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
   if (!this.resourceProperties.isAddMappings()) {
      logger.debug("Default resource handling disabled");
      return;
   }
   Duration cachePeriod = this.resourceProperties.getCache().getPeriod();
   CacheControl cacheControl = this.resourceProperties.getCache().getCachecontrol().toHttpCacheControl();
   if (!registry.hasMappingForPattern("/webjars/**")) {
      customizeResourceHandlerRegistration(registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/")
            .setCachePeriod(getSeconds(cachePeriod)).setCacheControl(cacheControl));
   }
   String staticPathPattern = this.mvcProperties.getStaticPathPattern();
   if (!registry.hasMappingForPattern(staticPathPattern)) {
      customizeResourceHandlerRegistration(registry.addResourceHandler(staticPathPattern)
            .addResourceLocations(getResourceLocations(this.resourceProperties.getStaticLocations()))
            .setCachePeriod(getSeconds(cachePeriod)).setCacheControl(cacheControl));
   }
}
```

a. 所有的 /webjars.xxx,都去classpath:/META-INF/resources/webjars/找对应资源；

webjars：以jar包的方式引入静态资源

![webjars-jquery](image\webjars-jquery.png)

=》http://127.0.0.1:8080/webjars/jquery/3.5.1/jquery.js 可访问对饮的资源文件

#### 2.2. springboot默认静态资源映射

```java
@ConfigurationProperties(prefix = "spring.resources", ignoreUnknownFields = false)
public class ResourceProperties {

   private static final String[] CLASSPATH_RESOURCE_LOCATIONS = { "classpath:/META-INF/resources/",
         "classpath:/resources/", "classpath:/static/", "classpath:/public/" };
```

### 3. 模板引擎

#### 3.1. 引入thymeleaf

资源地址：https://www.thymeleaf.org/documentation.html

```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

**配置项：**

```java
@ConfigurationProperties(prefix = "spring.thymeleaf")
public class ThymeleafProperties {

   private static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;

   public static final String DEFAULT_PREFIX = "classpath:/templates/";

   public static final String DEFAULT_SUFFIX = ".html";
```



### 4. SpringMvc自动配置

**视频资源：**https://www.bilibili.com/video/BV1Et411Y7tQ?p=32

资源地址：https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/reference/htmlsingle/#boot-features-spring-mvc （Spring MVC Auto-configuration）

##### Spring MVC Auto-configuration

Spring Boot provides auto-configuration for Spring MVC that works well with most applications.

The auto-configuration adds the following features on top of Spring’s defaults:

- Inclusion of `ContentNegotiatingViewResolver` and `BeanNameViewResolver` beans.

  ```java
  // WebMvcAutoConfiguration.java
  @Bean
  @ConditionalOnBean(ViewResolver.class)
  @ConditionalOnMissingBean(name = "viewResolver", value = ContentNegotiatingViewResolver.class)
  public ContentNegotiatingViewResolver viewResolver(BeanFactory beanFactory) {
      ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
      resolver.setContentNegotiationManager(beanFactory.getBean(ContentNegotiationManager.class));
      // ContentNegotiatingViewResolver uses all the other view resolvers to locate
      // a view so it should have a high precedence
      resolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
      return resolver;
  }
  
  // RequestAttributes.java
  @Override
  @Nullable
  public View resolveViewName(String viewName, Locale locale) throws Exception {
      RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
      Assert.state(attrs instanceof ServletRequestAttributes, "No current ServletRequestAttributes");
      List<MediaType> requestedMediaTypes = getMediaTypes(((ServletRequestAttributes) attrs).getRequest());
      if (requestedMediaTypes != null) {
          List<View> candidateViews = getCandidateViews(viewName, locale, requestedMediaTypes);
          View bestView = getBestView(candidateViews, requestedMediaTypes, attrs);
          if (bestView != null) {
              return bestView;
          }
      }
  ```

  

  - 自动配置ViewResolver试图解析器，根据方法的返回值得到视图对象，视图对象决定如何渲染

  - ContentNegotiatingViewResolver：组合所有试图的解析器；

  - ==**如何定制：**==可以给自己的容器中添加一个视图解析器，加载bean会自动生成！

    自定义一个视图解析器：

    ```java
    @Configuration
    public class MyViewResolver implements ViewResolver {
    
        @Override
        public View resolveViewName(String viewName, Locale locale) throws Exception {
            return null;
        }
    
        @Bean(value = "myViewResolverPlus")
        public ViewResolver myViewResolver(){
            return new MyViewResolver();
        }
    }
    ```

    验证：

    ![viewResolver-component](D:\codeProject\spring-boot-practice\image\viewResolver-component.png)

- Support for serving static resources, including support for WebJars (covered [later in this document](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/reference/htmlsingle/#boot-features-spring-mvc-static-content))).

- Automatic registration of `Converter`, `GenericConverter`, and `Formatter` beans.

- Support for `HttpMessageConverters` (covered [later in this document](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/reference/htmlsingle/#boot-features-spring-mvc-message-converters)).

- Automatic registration of `MessageCodesResolver` (covered [later in this document](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/reference/htmlsingle/#boot-features-spring-message-codes)).

- Static `index.html` support.

- Custom `Favicon` support (covered [later in this document](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/reference/htmlsingle/#boot-features-spring-mvc-favicon)).

- Automatic use of a `ConfigurableWebBindingInitializer` bean (covered [later in this document](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/reference/htmlsingle/#boot-features-spring-mvc-web-binding-initializer)).

If you want to keep those Spring Boot MVC customizations and make more [MVC customizations](https://docs.spring.io/spring/docs/5.2.8.RELEASE/spring-framework-reference/web.html#mvc) (interceptors, formatters, view controllers, and other features), you can add your own `@Configuration` class of type `WebMvcConfigurer` but **without** `@EnableWebMvc`.

扩展springmvc配置

```java
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    // 重定向请求操作
    @Override
    public void addViewControllers(ViewControllerRegistry registration) {
        registration.addViewController("/hello").setViewName("redirect");
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("prefix", c->c.isAnnotationPresent(HelloController.class));
    }
}
```

​	1）、WebMvcAutoConfiguration是SpringMVC的自动配置类

​	2）、在做其他自动配置时会导入：@Import(EnableWebMvcConfiguration.class)

```
@Configuration(proxyBeanMethods = false)
@Import(EnableWebMvcConfiguration.class)
@EnableConfigurationProperties({ WebMvcProperties.class, ResourceProperties.class })
@Order(0)
public static class WebMvcAutoConfigurationAdapter implements WebMvcConfigurer {
```

​	3）、容器中所有的webmvcConfigure都会一起起作用

​	4）、我们配置的类也会被调用

If you want to provide custom instances of `RequestMappingHandlerMapping`, `RequestMappingHandlerAdapter`, or `ExceptionHandlerExceptionResolver`, and still keep the Spring Boot MVC customizations, you can declare a bean of type `WebMvcRegistrations` and use it to provide custom instances of those components.

If you want to take complete control of Spring MVC, you can add your own `@Configuration` annotated with `@EnableWebMvc`, or alternatively add your own `@Configuration`-annotated `DelegatingWebMvcConfiguration` as described in the Javadoc of `@EnableWebMvc`.



32讲

1. 补充@EnableMvc注解导致全局mvc自动配置失效的原理

   33讲

2. 添加一个拦截器，让相关请求重定向一个界面，通过重写WebMvcConfigurer（)方法，默认访问首页

3. 通过引用bootstrap的css文件和js文件达到样式控制的效果

   34讲

4. bundle resource 通过 重新设置基础资源包 引入中英两种动态修改页面，根据浏览器语言切换对应语言显示。

5. 鼠标点击动态切换语言

6. 登录成功跳转登录成功界面，失败则登录页提示登录失败

7. 拦截器进行登录检查

8. 



### 5. 错误处理机制

##### 	1）.如何定制错误响应

​	ErrorMvcAutoConfiguration:错误处理的自动配置

​	给容器中添加以下组件：

	1. DefaultErrorAtrributes
 	2. BasicErrorAttributes
 	3. 





























































































































# 终、todo-list:

1. 阅读springboot使用日志框架源码及理解配置原理