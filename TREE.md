```
.
├── QaApplication.java                  // 主应用入口
├── async                               // 异步处理模块
│   ├── EventConsumer.java
│   ├── EventHandler.java
│   ├── EventModel.java
│   ├── EventProducer.java
│   ├── EventType.java
│   └── handler                         // 处理器
│       ├── AddCommentHandler.java
│       ├── AddQuestionHandler.java
│       ├── FeedHandler.java
│       ├── FollowHandler.java
│       └── LikeHandler.java
├── common                              // 公共响应模块
│   ├── CommonResp.java
│   └── CommonRespConst.java
├── configuration                       // 配置类
│   └── QaWebConfiguration.java
├── controller                          // 后台所有Controller
│   ├── CommentController.java
│   ├── CrawlController.java
│   ├── FeedController.java
│   ├── FollowController.java
│   ├── IndexController.java
│   ├── LikeController.java
│   ├── LoginController.java
│   ├── MessageController.java
│   ├── QuestionController.java
│   └── SearchController.java
├── dao                                // 数据访问层对象
│   ├── AuthUserDAO.java
│   ├── CommentDAO.java
│   ├── FeedDAO.java
│   ├── LoginTicketDAO.java
│   ├── MessageDAO.java
│   ├── QuestionDAO.java
│   └── UserDAO.java
├── interceptor                        // 系统拦截器
│   ├── LoginRequiredInterceptor.java
│   └── PassportInterceptor.java
├── model                              // 业务模型对象
│   ├── AuthUser.java
│   ├── Comment.java
│   ├── EntityType.java
│   ├── Feed.java
│   ├── HostHolder.java
│   ├── LoginTicket.java
│   ├── Message.java
│   ├── Question.java
│   ├── User.java
│   └── ViewObject.java
├── service                            // 业务处理层                         
│   ├── AuthUserService.java
│   ├── CommentService.java
│   ├── CrawlService.java
│   ├── FeedService.java
│   ├── FollowService.java
│   ├── LikeService.java
│   ├── MessageService.java
│   ├── QuestionService.java
│   ├── SearchService.java
│   ├── SensitiveService.java
│   ├── UserService.java
│   └── impl                           // 业务处理层实现类
│       ├── AuthUserServiceImpl.java
│       ├── CommentServiceImpl.java
│       ├── CrawlServiceImpl.java
│       ├── FeedServiceImpl.java
│       ├── FollowServiceImpl.java
│       ├── LikeServiceImpl.java
│       ├── MessageServiceImpl.java
│       ├── QuestionServiceImpl.java
│       ├── SearchServiceImpl.java
│       ├── SensitiveServiceImpl.java
│       └── UserServiceImpl.java
├── task                              // 定时爬虫任务
│   └── CrawlTask.java
└── util                              // 系统工具类封装
    ├── AvatarGenerator.java
    ├── JedisAdapter.java
    ├── JedisTest.java
    ├── QaUtil.java
    ├── RedisKeyUtil.java
    ├── RequestUtil.java
    └── TimeUtil.java
```