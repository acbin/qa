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

### 注册登录
为了保证用户信息安全，系统对用户密码采用「salt + md5」方式进行加密。用户注册/登录成功后，系统会生成一个 ticket ，将 ticket 与用户 id 相关联，并将此信息插入到数据库表 login_ticket 中，同时将 ticket 响应给客户端。

用户每次请求页面的时候，都需要先经过 PassportInterceptor 拦截器，拦截器判断此 ticket 是否真实有效，若是，根据 ticket 对应的用户 id ，查出相应用户信息，并添加至页面上下文中。

### 用户内容发布
- 问题发布
- 评论发布
- 私信发布

在以上 **UGC** (User Generated Content, 用户产生的内容)中，系统都会进行 HTML 标签及敏感词过滤，这在一定程度上防止网站被注入<script>脚本或者充斥着不良信息。

对于**敏感词过滤**，采用「前缀树」方式实现，前缀树结点结构如下：

```java
class TrieNode {
    // 标记是否为敏感词结尾
    boolean end;
    
    // 该结点的所有直接子结点
    Map<Character, TrieNode> subNodes = new HashMap<>();
    
    // 添加一个子结点
    void addSubNode(Character key, TrieNode node) {
        subNodes.put(key, node);
    }
    
    // 根据key获取子结点
    TrieNode getSubNode(Character key) {
        return subNodes.get(key);
    }
}
```

从敏感词文件 SensitiveWords.txt 顺序读取每一行，建立前缀树。进行过滤时，遍历需要过滤的文本，用星号替换发现的敏感词。

对于**评论功能**，系统依靠 EntityType 与 EntityId 识别所评论的实体，从而建立起一个统一的评论服务中心。用户对于问题/评论的回复，都可以应用此服务。查询某实体下的评论时，同样根据 EntityType 和 EntityId 查询即可。 


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

