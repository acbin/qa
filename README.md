# 问答社区
Q & A Website Demo

## 1. 技术选型
SpringBoot + MyBatis + FreeMarker

## 2. 功能规划

### 用户注册登录模块

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

## 3. 数据库字段设计
### 用户(User)
| id | name | password | salt | head_url |
|-----|-----|------|-----|-----|
| 1 | bingo   | 123 | xxx | xxx |

### 站内信(Message)
| id | fromid | toid | content | conversation_id | created_date |
|-----|-----|------|-----|-----|-----|
| 1 | 1   | 2 | 内容 | 1-2 | 2018/5/20|

### 问题(Question)
| id | title | content | user_id | created_date | comment_count |
|-----|-----|------|-----|-----|-----|
| 1 | 标题   | 内容 | 1 | 2018/5/20| 3 |

### 评论(Comment)
| id | content | user_id | created_date | entity_id | entity_type |
|-----|-----|------|-----|-----|-----|
| 1 | 内容   | 1 | 2018/5/20| 1 | ENTITY_TYPE.COMMENT |

