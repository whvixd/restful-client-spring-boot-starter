# 简介
restful-client-spring-boot-starter 是一个基于springboot的快速集成http客户端的启动器

# 使用方式

## 1. 添加依赖

```
<groupId>com.github.whvixd</groupId>
<artifactId>restful-client-spring-boot-starter</artifactId>
<version>1.0.0</version>
```
## 2. 添加客户端代码

```java
@RequestMapping(path = "192.168.11.111:8080", coder = HelloCoderHandler.class)
interface HelloClient {
    @RequestGet(path = "/hello/get")
    String helloGet(@RequestHeader Map<String, String> headers);

    @RequestPost(path = "/hello/post")
    String helloPost(@RequestHeader Map<String, String> headers, @RequestBody Map<String, Object> body);
}
```