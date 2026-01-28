# 校园零售销售系统 - 详细操作步骤

## 一、项目说明

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
  - ✅ 首页：**横幅图片**（单张图片完整显示），界面美观
  - ✅ 动态分类功能：添加商品时输入新分类，会自动在商品选购界面显示该分类

---

## 二、环境准备

### 1. 安装 JDK 17

- 下载：https://adoptium.net/ 或 Oracle JDK 17
- 配置 `JAVA_HOME`，并把 `%JAVA_HOME%\bin` 加入 Path
- 命令行执行 `java -version` 能显示 17 即为成功

### 2. 安装 Maven（如用命令行构建）

- 下载：https://maven.apache.org/
- 配置 `MAVEN_HOME` 和 Path
- 执行 `mvn -v` 能显示版本即可

> 若使用 **IntelliJ IDEA**，可只用其自带的 Maven，不必单独安装。

### 3. 安装并启动 MySQL 8.0

- 下载：https://dev.mysql.com/downloads/mysql/
- 安装后启动 MySQL 服务
- 记住你设置的 **root 密码**（安装时或之后修改的）

---

## 三、准备图片资源（重要）

在启动项目前，需要准备图片用于登录页和首页的显示。

### 步骤 1：准备图片文件

**登录页面轮播图**（需要3张）：
- 建议尺寸：1920x1080 或类似比例，格式：JPG 或 PNG
- 用于登录页面左侧轮播显示

**首页横幅图**（需要1张）：
- 建议尺寸：宽度 1920px，高度 150px（或按比例）

### 步骤 2：放置图片到指定目录

**登录页面图片**：
将3张图片文件重命名为：
- `login1.jpg`
- `login2.jpg`
- `login3.jpg`

**首页图片**：
将1张图片文件重命名为：
- `home1.jpg`（

然后复制到项目目录：
```
项目根目录/src/main/resources/static/img/
```

**操作步骤**：
1. 在项目根目录下找到 `src/main/resources/static/` 目录
2. 如果 `img` 目录不存在，请先创建该目录
3. 将登录页面的3张图片复制到 `img` 目录，重命名为：`login1.jpg`、`login2.jpg`、`login3.jpg`
4. 将首页的1张图片复制到 `img` 目录，重命名为：`home1.jpg`

> **注意**：如果使用 PNG 格式，文件名应为 `login1.png`、`home1.png` 等，并确保 HTML 中的路径也使用 `.png` 扩展名。

---

## 四、创建数据库并导入数据

### 步骤 1：打开 MySQL 客户端

- **命令行方式**：打开 CMD 或 PowerShell，输入 `mysql -u root -p`，然后输入密码
- **图形化工具**：使用 Navicat、MySQL Workbench、DBeaver 等工具连接 MySQL

### 步骤 2：创建数据库

在 MySQL 客户端中执行：

```sql
CREATE DATABASE campus_retail DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 步骤 3：执行初始化脚本

> **重要**：若之前已建过库，表结构有变更，请重新执行本脚本（会先 DROP 再 CREATE 表，数据会重置）。

**方式 A：在 MySQL 命令行中执行**

```sql
USE campus_retail;
SOURCE 项目根目录/sql/init.sql;
```

> **注意**：
> - `SOURCE` 后的路径要把反斜杠 `\` 改为正斜杠 `/`
> - 路径必须是你的实际项目路径，例如：`SOURCE D:/projects/campus-retail-system/sql/init.sql;`

**方式 B：在 CMD 或 PowerShell 中执行**

```powershell
cd 项目根目录
mysql -u root -p campus_retail < sql/init.sql
```

> **注意**：将 `项目根目录` 替换为你的实际项目路径，例如：`cd D:\projects\campus-retail-system`

输入 MySQL 的 root 密码后，脚本会自动建表并插入测试数据。

**方式 C：在图形化工具中执行**

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

SELECT * FROM product;
-- 应看到5条示例商品数据
```

---

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

## 六、用命令行运行（可选）

在项目根目录下执行：

```bash
# 若已配置 Maven 环境变量：
mvn spring-boot:run
```

或用 IDEA 自带 Maven 的 `mvn` 所在目录执行上述命令。看到启动成功提示后，同样访问：`http://localhost:8080`。

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

---

## 九、项目结构说明（分层架构）

### 项目目录结构

```
campus-retail-system/
├── pom.xml                          # Maven 依赖与构建配置
├── sql/
│   └── init.sql                     # 数据库初始化脚本（建表+测试数据）
├── uploads/                          # 上传的商品图片存储目录（自动创建）
│   └── product/                      # 商品图片文件夹
├── src/main/
│   ├── java/com/campus/retail/
│   │   ├── CampusRetailApplication.java   # Spring Boot 启动类
│   │   ├── config/
│   │   │   └── WebConfig.java             # 静态资源配置（图片访问路径）
│   │   ├── controller/                    # 控制层（接收请求，返回视图）
│   │   │   ├── PageController.java       # 首页、登录页、登出
│   │   │   ├── UserController.java       # 用户登录、注册
│   │   │   ├── ProductController.java    # 商品列表（顾客）、商品管理+图片上传（管理员）
│   │   │   ├── CartController.java       # 购物车：加入、查看、更新、删除
│   │   │   └── OrderController.java      # 下单（从购物车+收货信息）、订单查询、订单管理
│   │   ├── service/                      # 业务逻辑层（处理业务逻辑）
│   │   │   ├── UserService.java          # 用户相关业务
│   │   │   ├── ProductService.java       # 商品相关业务（包括热门商品查询）
│   │   │   └── OrderService.java         # 订单相关业务
│   │   ├── mapper/                       # 数据访问层（Mapper接口，MyBatis-Plus）
│   │   │   ├── UserMapper.java           # 用户表操作
│   │   │   ├── ProductMapper.java        # 商品表操作
│   │   │   ├── OrdersMapper.java         # 订单表操作
│   │   │   └── OrderItemMapper.java      # 订单明细表操作
│   │   └── entity/                       # 实体层（对应数据库表）
│   │       ├── User.java                 # 用户实体（学号、密码、宿舍号等）
│   │       ├── Product.java              # 商品实体（名称、价格、库存、图片等）
│   │       ├── Orders.java               # 订单实体（订单号、收货信息等）
│   │       └── OrderItem.java            # 订单明细实体
│   └── resources/
│       ├── application.properties        # 配置文件（数据库、端口、MyBatis等）
│       ├── static/                       # 静态资源（CSS、JS、图片）
│       │   └── img/                       # 图片资源目录
│       │       ├── login1.jpg            # 登录页轮播图片1（需自行添加）
│       │       ├── login2.jpg            # 登录页轮播图片2（需自行添加）
│       │       ├── login3.jpg            # 登录页轮播图片3（需自行添加）
│       │       ├── home1.jpg             # 首页横幅图片（需自行添加）
│       │       └── placeholder.svg       # 商品占位图
│       └── templates/                    # 前端网页模板（Thymeleaf）
│           ├── login.html                # 登录页面（带轮播图）
│           ├── register.html             # 注册页面（学号、密码、宿舍号）
│           ├── index.html                # 首页（热门商品、横幅图）
│           ├── product/
│           │   ├── list.html             # 商品选购页面（两列平铺、加入购物车）
│           │   ├── manage.html           # 商品管理页面（管理员）
│           │   └── edit.html             # 新增/编辑商品页面（含图片上传）
│           ├── cart/
│           │   └── view.html             # 购物车页面
│           └── order/
│               ├── confirm.html           # 确认订单页面（填写收货信息）
│               ├── my.html               # 我的订单页面（普通用户）
│               ├── detail.html           # 订单详情页面
│               └── manage.html           # 订单管理页面（管理员）
└── 操作步骤.md                           # 本操作文档
```

### 分层架构说明

1. **Controller 层（控制层）**
   - 职责：接收 HTTP 请求，调用 Service 层处理业务，返回视图或数据
   - 位置：`controller/` 包

2. **Service 层（业务逻辑层）**
   - 职责：处理业务逻辑，调用 Mapper 层操作数据库
   - 位置：`service/` 包

3. **Mapper 层（数据访问层）**
   - 职责：使用 MyBatis-Plus 进行数据库 CRUD 操作
   - 位置：`mapper/` 包

4. **Entity 层（实体层）**
   - 职责：对应数据库表结构，使用 Lombok 简化代码
   - 位置：`entity/` 包

---

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

---

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

## 十二、实训报告可写内容建议

### 1. 项目概述
- 项目名称：校园零食销售系统
- 项目背景：校园零售场景、用户需求分析
- 项目目标：实现商品选购、购物车、订单管理等核心功能

### 2. 需求分析
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

### 3. 技术选型
- **后端框架**：Spring Boot 3.2（简化配置、快速开发）
- **持久层框架**：MyBatis-Plus（简化 CRUD 操作）
- **数据库**：MySQL 8.0（关系型数据库，稳定可靠）
- **前端模板**：Thymeleaf（服务端渲染，简单易用）
- **构建工具**：Maven（依赖管理、项目构建）
- **开发工具**：IntelliJ IDEA

### 4. 系统设计

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

---

按上述步骤完成环境准备、建库、改配置、启动后，即可在浏览器中使用本系统。
