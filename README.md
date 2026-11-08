# 学生信息管理系统（Java 版）

已提供一个可本地运行的 **Java + Spring Boot + Thymeleaf + H2** 学生信息管理网页。无需安装 Python、PHP、Node.js、MySQL 或 Redis；网页由 Java 服务提供，数据保存在本机 H2 文件数据库中。

> 当前 Java 版是仓库根目录的主项目。原先的 `backend/`（Laravel）和 `frontend/`（Vue）目录暂时保留，运行本 Java 版不需要使用它们。

## 需要安装

- JDK 17 或更高版本
- Apache Maven 3.6.3 或更高版本

检查安装：

```bash
java -version
mvn -version
```

## 启动

在仓库根目录执行：

```bash
mvn spring-boot:run
```

启动完成后，在浏览器打开 <http://localhost:8080>。

也可以先打包，再运行：

```bash
mvn clean package
java -jar target/student-management-1.0.0.jar
```

## 功能

- 学生信息列表、姓名/学号/班级关键词搜索
- 新增、编辑、删除学生信息
- 学号唯一校验及表单校验
- H2 文件数据库持久化；数据文件保存在项目根目录的 `./data/` 下

无需另外启动数据库服务。默认开发配置启用了 H2 控制台，仅供本机开发使用。部署到公开网络前，请配置正式数据库、访问控制和安全设置。
