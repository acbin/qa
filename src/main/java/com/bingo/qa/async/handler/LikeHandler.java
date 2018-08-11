package com.bingo.qa.async.handler;


import com.bingo.qa.async.EventHandler;
import com.bingo.qa.async.EventModel;
import com.bingo.qa.async.EventType;
import com.bingo.qa.model.Message;
import com.bingo.qa.model.User;
import com.bingo.qa.service.impl.MessageService;
import com.bingo.qa.service.impl.UserService;
import com.bingo.qa.util.QaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandler{

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandler(EventModel model) {
        Message message = new Message();
        // 给某个实体点了个赞
        // 实体对应的owner应该收到来自System的一条消息
        message.setFromId(QaUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.selectById(model.getActorId());
        message.setContent("用户" + user.getName() + "赞了你的评论,http://localhost:8080/question/" + model.getExt("questionId"));
        messageService.addMessage(message);

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
