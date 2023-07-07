# redis 控制session

> redis统一管理session信息，可支持多服务之间session通信。

## 类描述

- IgnoreAuthorize：忽略权限注解，放在类或方法上，可进行忽略权限（登陆）。（在LoginInterceptor做的控制）
- LoginController：测试接口。
- LoginConfig：拦截器控制，主要控制拦截那些路径。
- LoginInterceptor：登陆拦截器，验证接口是否携带正确的token，或是否忽略权限。
- RedisSessionConfig：redis服务管理。
- application.properties：环境变量管理。
- panda.http：接口测试。

## Maven

```xml
<!-- 满足需求，主要引入部分 -->
<dependencies>
    <!--        缓存数据库相关-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <dependency>
        <groupId>redis.clients</groupId>
        <artifactId>jedis</artifactId>
    </dependency>
    <!--spring-session-redis sessiong共享       -->
    <dependency>
        <groupId>org.springframework.session</groupId>
        <artifactId>spring-session-data-redis</artifactId>
        <version>2.0.6.RELEASE</version>
    </dependency>
</dependencies>
```