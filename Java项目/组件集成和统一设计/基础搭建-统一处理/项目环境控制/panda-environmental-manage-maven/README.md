# maven profile 进行版本控制

## 版本管理

application.properties中使用${}进行留空，打包时扫描pro文件，将${}内容替换。

base.pro为基础文件，不管是在那个环境中，都会存在。（在pom.xml中进行配置的。）

其他文件会当作环境，进行替换。

## 默认环境控制

在pom.xml中，activeByDefault为true，则设置为默认环境。

## 打包

```shell
# -P 环境
mvn clean package -P test
```