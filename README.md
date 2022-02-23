# 简介
**restful-client-spring-boot-starter** 是一个基于spring-boot的快速集成http客户端的启动器

# 使用方式

## 1. 依赖配置

1.1 [进入页面](https://github.com/whvixd/restful-client-spring-boot-starter/releases)，下载jar包到本地（未发布到maven仓库）

1.2 将下载的jar包添加到本地依赖中

```bash
mvn install:install-file -DgroupId=com.github.whvixd -DartifactId=restful-client-spring-boot-starter -Dversion=1.0.0-Dpackaging=jar -Dfile=~/Downloads/restful-client-spring-boot-starter.jar
```

1.3 本地spring-boot项目添加mvn依赖

     <dependency>
            <groupId>com.github.whvixd</groupId>
            <artifactId>restful-client-spring-boot-starter</artifactId>
            <version>1.0.0</version>
     </dependency>
     
     

## 2. 示例代码

2.1 spring-boot启动类添加`@RestfulClientScan`

```java
@SpringBootApplication
@RestfulClientScan(basePackages = "com.github.whvixd.restful.client.spring.boot.client")
public class RestfulClientApplication {
    public static void main(String[] args) {
            SpringApplication.run(RestfulClientApplication.class, args);
        }
    
}
```

2.2 添加`client`模块

```java
@RequestMapping(path = "127.0.0.1:8080",message = "hello")
public interface HelloRestfulClient {
    @RequestGet(path = "/hello/get")
    String helloGet(@RequestHeader Map<String, String> headers);

    @RequestGet(path = "/hello/get/{type}/{id}")
    String helloGetPath(@RequestHeader Map<String, String> headers, @RequestPathParam Map<String, String> pathParam);

    @RequestGet(path = "/hello/get/{type}?id={id}&name={name}")
    String helloGetQuery(@RequestHeader Map<String, String> headers, @RequestPathParam Map<String, String> pathParam, @RequestQueryParam Map<String, String> queryParam);

    @RequestPost(path = "/hello/post")
    String helloPost(@RequestHeader Map<String, String> headers, @RequestBody Map<String, Object> body);

    @RequestPost(path = "/hello/post/serialize")
    RestfulClientAutoConfigurationTest.HelloPostRes helloPostSerialize(@RequestHeader Map<String, String> headers, @RequestBody RestfulClientAutoConfigurationTest.HelloPostBody body);
}
```


2.3 `client`的使用

```java
@Service
public class HelloService{
    @Autowired
    private HelloRestfulClient helloRestfulClient;
    
    public String helloGet(){
        Map<String,String> mockHeaders=new HashMap<>();
        return helloRestfulClient.helloGet(mockHeaders);
    }
} 
```

> 代码可参考 `RestfulClientAutoConfigurationTest`