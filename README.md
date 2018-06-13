# Q & A 问答社区

**QA** 是一个基于 [B/S](https://zh.wikipedia.org/wiki/%E6%B5%8F%E8%A7%88%E5%99%A8-%E6%9C%8D%E5%8A%A1%E5%99%A8) 架构而设计开发的社区网站。

[![Build Status](https://travis-ci.org/yanglbme/qa.svg?branch=master)](https://travis-ci.org/yanglbme/qa)
[![Java Version](https://img.shields.io/badge/Java-%3E%3D8-blue.svg)](https://github.com/yanglbme/qa)
[![Framework](https://img.shields.io/badge/Powered%20by-Spring%20Framework-green.svg)](https://spring.io/projects/spring-boot)

主要为用户提供以下服务：

- 问题发布
- 评论
- 用户私信
- 关注
- 站内全文搜索

## 技术选型
[Spring Boot](https://spring.io/projects/spring-boot) + [MyBatis](http://www.mybatis.org/mybatis-3/zh/index.html) + MySQL + [Redis](https://redis.io/) + [FreeMarker](http://freemarker.foofun.cn/index.html)

## 功能描述

### 注册登录模块
为了保证用户信息安全，系统对用户密码采用`salt + md5` 方式进行加密。用户注册/登录成功后，系统会生成一个`ticket`，将`ticket`与用户`id`相关联，并将此信息插入到数据库表`login_ticket`中，同时将`ticket`响应给客户端。

用户每次请求页面的时候，都需要先经过`PassportInterceptor`拦截器，拦截器判断此`ticket`是否真实有效，若是，根据`ticket`对应的用户`id`，查出相应用户信息，并添加至页面上下文中。

### 问题发布模块
敏感词、 `JS` 标签过滤(前缀树实现)

### 评论中心
统一的评论服务：评论针对的实体可以是评论，也可以是问题

###  站内信
未读消息实现

### Redis 实现赞踩功能
以`entityType`和`entityId`作为键，用户的每一次点赞/点踩，将用户的ID存入该`Like`/`Dislike`键位置。

### 异步设计与站内邮件通知系统

###  SNS 关注功能，关注和粉丝列表页实现

### Timeline 实现

### Python 爬虫实现数据抓取和导入

###  站内全文搜索

## 数据库表字段设计
### user
- id
- name
- password
- salt
- head_url

### login_ticket
- id
- user_id
- ticket
- expired
- status

### message
- id
- from_id
- to_id
- content
- conversation_id
- created_date

### question
- id
- title
- content
- user_id
- created_date
- comment_count

### comment
- id
- content
- user_id
- created_date
- entity_id
- entity_type

### feed
- id
- created_date
- user_id
- data
- type

