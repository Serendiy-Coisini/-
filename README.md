# 校园零售销售系统

## 一、项目说明（半成品，首页图片功能未完善）

- **技术栈**：Spring Boot 3.2 + MyBatis-Plus + MySQL 8.0 + Thymeleaf（网页前端）
- **分层架构**：Controller（控制层）→ Service（业务层）→ Mapper（数据访问层）→ Entity（实体层）
- **功能特性**：
  - ✅ 用户登录使用**学号**（不是用户名）
  - ✅ 注册模块：只需填写**学号、密码、宿舍号**即可完成注册
  - ✅ 商品选购界面：**两列平铺**显示，每商品有图片展示
  - ✅ **加入购物车**功能
  - ✅ 商品图片：管理员可以**添加或更换**商品图片
  - ✅ 用户下单：在**确认订单页面**填写收货信息（姓名、宿舍号、联系电话）
  - ✅ 图片上传：使用**项目根目录的绝对路径**存储
  - ✅ 首页：显示**部分热门商品**
  - ✅ 管理员后台：只有**商品管理**和**订单管理**，无"我的订单"功能
  - ✅ 登录页面：**轮播学校风景图片**（3张图片自动切换）
  - ✅ 首页：**横幅图片**
  - ✅ 动态分类功能：添加商品时输入新分类，会自动在商品选购界面显示该分类

## 二、环境准备


## 三、图片资源（已添加可更换）

**首页图片**：
将1张图片文件重命名为：
- `home1.jpg`

然后复制到项目目录：
```
项目根目录/src/main/resources/static/img/
```

**操作步骤**：
在项目根目录下找到 `src/main/resources/static/` 目录
将登录页面的3张图片复制到 `img` 目录，重命名为：`login1.jpg`、`login2.jpg`、`login3.jpg`
将首页的1张图片复制到 `img` 目录，重命名为：`home1.jpg`

> **注意**：如果使用 PNG 格式，文件名应为 `login1.png`、`home1.png` 等，并确保 HTML 中的路径也使用 `.png` 扩展名。


## 四、创建数据库并导入数据

### 步骤 1：打开 MySQL 客户端

- **命令行方式**：打开 CMD 或 PowerShell，输入 `mysql -u root -p`，然后输入密码
- **图形化工具**：使用 Navicat、MySQL Workbench、DBeaver 等工具连接 MySQL

### 步骤 2：创建数据库


### 步骤 3：执行初始化脚本

> **重要**：若之前已建过库，表结构有变更，请重新执行本脚本（会先 DROP 再 CREATE 表，数据会重置）。

**在图形化工具中执行**

1. 打开 Navicat 或 MySQL Workbench
2. 连接到 MySQL 服务器
3. 选择 `campus_retail` 数据库（如果没有，先创建）
4. 打开 `sql/init.sql` 文件
5. 执行整个脚本

### 步骤 4：验证数据库是否创建成功

在 MySQL 客户端中执行：

```sql
USE campus_retail;
SHOW TABLES;
-- 应看到：user, product, orders, order_item 这4张表

SELECT * FROM user;
-- 应看到：
-- 学号 admin（管理员，密码123456）
-- 学号 2021001（普通用户，密码123456，宿舍号：1栋301）
-- 学号 2021002（普通用户，密码123456，宿舍号：2栋205）


## 五、修改项目配置（连接你的 MySQL）

### 步骤 1：打开配置文件

用 IntelliJ IDEA 或记事本打开：

```
项目根目录/src/main/resources/application.properties
```

### 步骤 2：修改数据库连接信息

找到以下配置项，按你的实际情况修改：

```properties
# 数据库连接URL（数据库名必须是 campus_retail，与上面创建的一致）
spring.datasource.url=jdbc:mysql://localhost:3306/campus_retail?useSSL=false&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true

# MySQL 用户名（一般是 root）
spring.datasource.username=root

# MySQL 密码（改成你的实际密码）
spring.datasource.password=123456
```

**重要说明**：
- 如果 MySQL 端口不是 3306，把 `3306` 改成你的实际端口
- `spring.datasource.password` 必须改成你的 MySQL root 密码
- 如果 MySQL 安装在远程服务器，把 `localhost` 改成服务器IP地址

### 步骤 3：确认图片上传路径配置

项目已配置为使用**项目根目录的绝对路径**存储上传的图片，无需修改。上传的商品图片会保存在：

```
项目根目录/uploads/product/
```

该目录会在首次上传图片时自动创建。

---

## 五、用 IntelliJ IDEA 打开并运行（推荐）

### 步骤 1：打开项目

1. 打开 IntelliJ IDEA
2. `File` → `Open`，选择项目根目录
3. 选择 “Open as Project”，等待 Maven 自动下载依赖（右下角有进度）

### 步骤 2：配置 JDK

- `File` → `Project Structure` → `Project`：SDK 选 **17**
- `File` → `Settings` → `Build, Execution, Deployment` → `Build Tools` → `Maven`：如有需要可指定 JDK 17

### 步骤 3：运行项目

1. 在左侧找到：`src/main/java/com/campus/retail/CampusRetailApplication.java`
2. 右键该文件 → **Run 'CampusRetailApplication'**
3. 控制台出现 “校园零售销售系统启动成功！” 且无报错，即表示启动成功

### 步骤 4：用浏览器访问

在浏览器地址栏输入：

```
http://localhost:8080
```

会跳转到登录页；若 8080 被占用，可在 `application.properties` 中修改 `server.port=8081` 等再重启。


---

## 七、功能使用说明

### 1. 登录与注册

- **登录**：http://localhost:8080/login，使用 **学号** 和密码
- **注册**：http://localhost:8080/register，学号 + 设置密码 + 宿舍号 即可
- 测试账号：
  - 管理员学号：`admin` / `123456`
  - 普通用户学号：`2021001` / `123456` 或 `2021002` / `123456`

### 2. 普通用户

- **首页**：登录后自动跳转
- **商品选购**：两列平铺，每商品有图片（无图显示“暂无图片”）、单价、数量、**加入购物车**
- **购物车**：查看、改数量、删除；填写 **姓名、宿舍号、联系电话** 后 **提交订单**
- **我的订单**：查看、支付、取消（仅待支付）

### 3. 管理员（admin）

- **无商品选购**：管理员界面不显示商品选购入口，直接访问 /product/list 会跳回首页
- **商品管理**：
  - 增、删、改商品（名称、单价、库存、**分类**、描述、**商品图片**：可添加或更换）
  - **分类功能**：添加商品时可输入分类名称，如果输入新分类，该分类会自动出现在商品选购界面的分类列表中
- **订单管理**：查看全部订单及收货信息（姓名、宿舍号、联系电话）


## 十、常见问题及解决方案

### 1. 启动报错：Access denied for user 'root'@'localhost'

- 检查 `application.properties` 里的 `spring.datasource.username`、`spring.datasource.password` 是否与 MySQL 一致
- 在 MySQL 中确认：`SELECT user,host FROM mysql.user;` 是否有 `root@localhost`，且密码正确

### 2. 启动报错：Unknown database 'campus_retail'

- 说明数据库未创建或名字写错，回到 **第三节** 执行 `CREATE DATABASE campus_retail;` 和 `init.sql`

### 3. 启动报错：Table 'campus_retail.xxx' doesn't exist

- 说明 `init.sql` 未在该库中执行成功，重新执行 `SOURCE` 或 `mysql < sql/init.sql`，并 `SHOW TABLES;` 确认

### 4. 8080 端口被占用

- 在 `application.properties` 中改为：`server.port=8081`（或其它未占用端口），然后访问 `http://localhost:8081`

### 5. 页面能打开，但点“提交订单”后报错或没反应

- 看 IDEA 控制台或终端的报错信息
- 常见原因：购物车为空（未选数量）、库存不足、商品不存在，按提示检查商品与数量

### 6. Maven 依赖下载很慢

- 可配置国内镜像，例如在 Maven 的 `settings.xml` 的 `<mirrors>` 中加入阿里云镜像（可自行搜索 “Maven 阿里云镜像” 配置）



## 十一、快速开始检查清单

在开始使用系统前，请确认以下步骤已完成：

- [ ] ✅ JDK 17 已安装并配置环境变量
- [ ] ✅ MySQL 8.0 已安装并启动
- [ ] ✅ 已创建数据库 `campus_retail`
- [ ] ✅ 已执行 `sql/init.sql` 脚本
- [ ] ✅ 已修改 `application.properties` 中的数据库连接信息
- [ ] ✅ 已准备登录页面图片（3张：login1.jpg, login2.jpg, login3.jpg）和首页图片（1张：home1.jpg）并放置到 `src/main/resources/static/img/` 目录
- [ ] ✅ 已用 IntelliJ IDEA 打开项目
- [ ] ✅ Maven 依赖已下载完成
- [ ] ✅ 项目已成功启动（控制台无错误）
- [ ] ✅ 浏览器可以访问 `http://localhost:8080`

---


### 需求分析
- **用户角色**：
  - 普通用户（学生）：浏览商品、加入购物车、下单、查看订单
  - 管理员：商品管理、订单管理
- **功能需求**：
  - 用户注册（学号、密码、宿舍号）
  - 用户登录（使用学号）
  - 商品浏览（两列平铺、图片展示）
  - 购物车管理
  - 订单下单（填写收货信息：姓名、宿舍号、联系电话）
  - 订单查询与管理
  - 商品管理（管理员：增删改、图片上传）
  - 订单管理（管理员：查看所有订单）

### 技术选型
- **后端框架**：Spring Boot 3.2（简化配置、快速开发）
- **持久层框架**：MyBatis-Plus（简化 CRUD 操作）
- **数据库**：MySQL 8.0（关系型数据库，稳定可靠）
- **前端模板**：Thymeleaf（服务端渲染，简单易用）
- **构建工具**：Maven（依赖管理、项目构建）
- **开发工具**：IntelliJ IDEA

###  系统设计

#### 4.1 分层架构设计
- **Controller 层**：接收请求，调用 Service，返回视图
- **Service 层**：业务逻辑处理
- **Mapper 层**：数据访问，使用 MyBatis-Plus
- **Entity 层**：实体类，对应数据库表

#### 4.2 数据库设计
- **user 表**：用户信息（学号、密码、宿舍号、角色等）
- **product 表**：商品信息（名称、价格、库存、图片等）
- **orders 表**：订单信息（订单号、用户ID、收货信息、总金额、状态等）
- **order_item 表**：订单明细（订单ID、商品ID、数量、金额等）

#### 4.3 功能模块设计
- 用户管理模块（注册、登录）
- 商品管理模块（浏览、管理、图片上传）
- 购物车模块（添加、更新、删除）
- 订单模块（下单、查询、管理）

### 5. 详细设计与实现

#### 5.1 关键功能实现
- **学号登录**：UserController 中实现学号验证
- **注册功能**：只需填写学号、密码、宿舍号
- **商品两列平铺**：使用 CSS Grid 布局实现
- **图片上传**：使用项目根目录绝对路径存储
- **购物车功能**：使用 Session 存储购物车数据
- **订单下单**：在确认订单页面填写收货信息后提交


### 6. 测试与运行
- 环境搭建步骤
- 数据库创建与初始化
- 项目配置与启动
- 功能测试（登录、注册、商品选购、下单等）

### 7. 项目特色
- 登录页轮播校园风景图片（3张自动切换），界面美观
- 首页横幅图片完整显示，界面美观
- 商品选购两列平铺，用户体验好
- 购物车确认订单后再填写地址，流程合理
- 管理员可上传/更换商品图片
- 首页显示热门商品
- 动态分类功能：添加商品时输入新分类，会自动在商品选购界面显示

按上述步骤完成环境准备、建库、改配置、启动后，即可在浏览器中使用本系统。
