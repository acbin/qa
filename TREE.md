```
.
├── QaApplication.java
├── async
│   ├── EventConsumer.java
│   ├── EventHandler.java
│   ├── EventModel.java
│   ├── EventProducer.java
│   ├── EventType.java
│   └── handler
│       ├── AddCommentHandler.java
│       ├── AddQuestionHandler.java
│       ├── FeedHandler.java
│       ├── FollowHandler.java
│       └── LikeHandler.java
├── common
│   ├── CommonResp.java
│   └── CommonRespConst.java
├── configuration
│   └── QaWebConfiguration.java
├── controller
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
├── dao
│   ├── AuthUserDAO.java
│   ├── CommentDAO.java
│   ├── FeedDAO.java
│   ├── LoginTicketDAO.java
│   ├── MessageDAO.java
│   ├── QuestionDAO.java
│   └── UserDAO.java
├── interceptor
│   ├── LoginRequiredInterceptor.java
│   └── PassportInterceptor.java
├── model
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
├── service
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
│   └── impl
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
├── task
│   └── CrawlTask.java
└── util
    ├── AvatarGenerator.java
    ├── JedisAdapter.java
    ├── JedisTest.java
    ├── QaUtil.java
    ├── RedisKeyUtil.java
    ├── RequestUtil.java
    └── TimeUtil.java
```