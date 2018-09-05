package com.bingo.qa.async.handler;

import com.bingo.qa.async.EventHandler;
import com.bingo.qa.async.EventModel;
import com.bingo.qa.async.EventType;
import com.bingo.qa.model.Message;
import com.bingo.qa.model.User;
import com.bingo.qa.service.MessageService;
import com.bingo.qa.service.UserService;
import com.bingo.qa.util.QaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author bingo
 */
@Component
public class AddCommentHandler implements EventHandler {

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;


    @Override
    public void doHandler(EventModel model) {
        Message message = new Message();
        // 评论了某个实体
        // 实体对应的owner应该收到来自System的一条消息
        message.setFromId(QaUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.selectById(model.getActorId());
        message.setContent("用户" + user.getName() + "评论了你的问题,快来看看吧~ http://localhost:8080/question/" + model.getEntityId());
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.COMMENT);
    }
}