> [!NOTE]
> 此项目已停止更新维护，功能已与新项目[TeamSpace](https://github.com/itrfcn/TeamSpace) 合并。
# 用户管理系统

> 此项目没有什么亮点，仅用于学习github

演示站点 user.z7z.me

## **需求分析**

1. 登录 / 注册
2. 用户管理（仅限管理员），对用户进行信息修改，封禁删除
3. 用户信息，允许用户自己修改信息

## **技术选型**

### 前端

三件套 + Vue + 组件库 Ant Design + Ant Design Vue

### 后端

- java

- spring（依赖注入框架，帮助你管理 Java 对象，集成一些其他的内容）
- springmvc（web 框架，提供接口访问、restful接口等能力）
- mybatis（Java 操作数据库的框架，持久层框架，对 jdbc 的封装）
- mybatis-plus（对 mybatis 的增强，不用写 sql 也能实现增删改查）
- springboot（快速启动 / 快速集成项目。不用自己管理 spring 配置，不用自己整合各种框架）
- junit 单元测试库
- mysql

## 部署流程

### 数据库

建库语句

```mysql
create table user
(
    id           bigint auto_increment comment 'id'
        primary key,
    username     varchar(256) default '默认用户'        not null comment '用户名',
    userAccount  varchar(256)                           null comment '账号',
    avatarUrl    varchar(1024)                          null comment '用户头像',
    gender       tinyint                                null comment '性别',
    userPassword varchar(512)                           not null comment '密码',
    phone        varchar(128)                           null comment '手机号',
    email        varchar(512)                           null comment '邮箱',
    userStatus   int          default 0                 not null comment '账号状态',
    createTime   timestamp    default CURRENT_TIMESTAMP null comment '账号创建时间',
    updateTime   timestamp    default CURRENT_TIMESTAMP null comment '更新时间',
    isDelete     int          default 0                 not null comment '是否删除',
    userRole     int          default 0                 not null comment '用户权限'
)
    comment '用户' engine = InnoDB;

```

id为0的用户为超级管理员，自己手动插入

默认注册用户id是从1开始的

### 后端

打开后端文件，使用Maven下载相关依赖

到application-prod.yml 文件中把数据库信息改成自己的

使用Maven打包项目，打包前跳过测试

打包完成的jar包在 target 目录里面

运行命令

```java
 java -jar .\user-center-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### 前端

打开前端文件，使用yarn或者npm下载相关依赖

到 public 目录下需改网站标题和图标

到 request.ts 文件，将 http://user.z7z.me 修改为自己的域名

使用 yarn build 打包文件

打包好的文件位于 dist 目录

### 部署

前端Nginx配置

```nginx
   server {
    	listen 80;
        server_name user.z7z.me;  #此处改为前端地址

        #此处用于解决代理问题
        location /api {
            proxy_pass http://user-backend.z7z.me;  #此处改为后端地址
            proxy_set_header Host $proxy_host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_buffering off;
            proxy_set_header Connection "";
        }	 	

        #此处用于解决路径404问题
        location / {
            root www/wwwroot/user.z7z.me; #此处改为服务器上前端文件路径
            index index.html;
            try_files $uri $uri/ /index.html;
        }

    }

```

后端Nginx配置

```nginx
   server {
        listen 80;
        server_name user-backend.z7z.me; #此处改为后端地址
        
        #CORS 配置
        location ^~ /api/ {
        # 禁止非 GET|POST|HEAD|OPTIONS|PUT|PATCH|DELET 的请求
        if ( $request_method !~ ^(GET|POST|HEAD|OPTIONS|PUT|PATCH|DELETE)$ ) {
            return 444;
        }

        set $origin $http_origin;
        # 重点！比如：
        # $origin !~ '^http?://user\.z7z\.me$
        # 下面配置前端域名，比如 user.z7z.me
        if ($origin !~ '^http?://user\.z7z\.me$') {
            set $origin 'http://user.z7z.me';
        }

        if ($request_method = 'OPTIONS') {

            add_header 'Access-Control-Allow-Origin' "$origin" always;
            add_header 'Access-Control-Allow-Methods' 'GET, POST, PATCH, PUT, DELETE, OPTIONS' always;
            add_header 'Access-Control-Allow-Headers' 'Content-Type, Accept, Authorization' always;
            add_header 'Access-Control-Allow-Credentials' 'true' always;

            add_header Access-Control-Max-Age 1728000;
            add_header Content-Type 'text/plain charset=UTF-8';
            add_header Content-Length 0;
            return 204;
        }
        
        if ($request_method ~ '(GET|POST|PATCH|PUT|DELETE)') {
            add_header Access-Control-Allow-Origin "$origin" always;
            add_header Access-Control-Allow-Methods 'GET, POST, PATCH, PUT, DELETE, OPTIONS' always;
            add_header Access-Control-Allow-Headers 'Content-Type, Accept, Authorization' always;
            add_header Access-Control-Allow-Credentials true always;
        }
        #代理到后端服务
        proxy_pass http://localhost:8080/; #此处改为后端要反向代理的真实地址
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr; 
        }
    }
```
