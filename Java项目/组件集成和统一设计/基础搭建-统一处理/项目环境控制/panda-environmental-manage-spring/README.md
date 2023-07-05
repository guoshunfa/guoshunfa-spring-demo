# spring profile 进行环境控制

## 环境配置

通过添加文件，application-\*.properties（\* 替换成环境，如dev。），进行环境管理。

## 调整环境

application.properties中填入spring.profiles.active=dev，值dev替换成其他环境。

## 打包

执行maven打包命令即可。

```shell
mvn package
```