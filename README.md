# 学生信息管理系统

基于 **Vue 2 + Laravel 9 + MySQL 8 + Redis 7** 的学生信息管理 Web 应用起步项目，提供学生信息的新增、查询、编辑、删除和分页功能。

## 项目结构

```text
.
├── backend/                  # Laravel 9 REST API
├── frontend/                 # Vue 2 单页应用
├── docker-compose.yml         # MySQL 8、Redis 7 开发服务
└── README.md
```

## 环境要求

- PHP 8.0.2+、Composer
- Node.js 14+、npm
- Docker 和 Docker Compose（或本机安装 MySQL 8、Redis 7）

## 启动数据库

在项目根目录运行：

```bash
docker compose up -d
```

Compose 默认创建数据库 `student_management`，MySQL 端口 `3306`，Redis 端口 `6379`。

## 启动 Laravel API

```bash
cd backend
cp .env.example .env
composer install
php artisan key:generate
php artisan migrate
php artisan serve
```

API 默认地址：`http://127.0.0.1:8000/api`。学生接口为 `GET/POST /api/students`、`GET/PUT/DELETE /api/students/{id}`。`.env.example` 已配置本项目 Compose 服务的数据库及 Redis 连接参数。

## 启动 Vue 前端

另开终端：

```bash
cd frontend
npm install
npm run serve
```

打开终端输出的本地地址（通常是 `http://localhost:8080`）。前端默认访问 `http://localhost:8000/api`；如需修改，在 `frontend/.env.local` 中设置：

```dotenv
VUE_APP_API_BASE_URL=http://localhost:8000/api
```

修改 API 地址后需要重启前端开发服务。

## 说明

这是可继续扩展的基础项目，当前未包含用户登录、角色权限及生产环境部署配置。请勿将开发用数据库密码直接用于生产环境。
