package com.bingo.qa.controller;


import com.bingo.qa.model.HostHolder;
import com.bingo.qa.model.Message;
import com.bingo.qa.model.User;
import com.bingo.qa.model.ViewObject;
import com.bingo.qa.service.MessageService;
import com.bingo.qa.service.UserService;
import com.bingo.qa.util.QaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    private static Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @PostMapping(value = {"/msg/addMessage"})
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content) {
        try {
            if (hostHolder.getUser() == null) {
                return QaUtil.getJSONString(999, "未登录");
            }

            User user = userService.selectByName(toName);
            if (user == null) {
                return QaUtil.getJSONString(1, "用户不存在");
            }

            Message message = new Message();
            message.setCreatedDate(new Date());
            message.setFromId(hostHolder.getUser().getId());
            message.setToId(user.getId());
            message.setContent(content);

            messageService.addMessage(message);

            return QaUtil.getJSONString(0);

        } catch (Exception e) {
            logger.error("发送消息失败" + e.getMessage());
            return QaUtil.getJSONString(1, "发送消息失败");
        }
    }


    @GetMapping(value = {"/msg/list"})
    public String getConversationList(Model model) {
        if (hostHolder.getUser() == null) {
            return "redirect:/reglogin";
        }

        int localUserId = hostHolder.getUser().getId();
        List<Message> conversationList = messageService.getConversationList(localUserId, 0, 10);
        List<ViewObject> vos = new ArrayList<>();
        for (Message message : conversationList) {
            ViewObject vo = new ViewObject();
            vo.set("conversation", message);
            int targetId = message.getFromId() == localUserId ? message.getToId() : message.getFromId();
            vo.set("user", userService.selectById(targetId));
            vo.set("unread", messageService.getConversationUnreadCount(localUserId, message.getConversationId()));
            vos.add(vo);

        }

        model.addAttribute("vos", vos);


        return "/letter";
    }

    // 与某人的所有会话(详情)
    @GetMapping(value = {"/msg/detail"})
    public String getConversationDetail(Model model,
                                        @RequestParam("conversationId") String conversationId) {
        try {
            List<Message> messageList = messageService.getConversationDetail(conversationId, 0, 10);
            List<ViewObject> vos = new ArrayList<>();
            for (Message message : messageList) {
                ViewObject vo = new ViewObject();


                if (hostHolder.getUser() == null) {
                    return "redirect:/reglogin";
                }

                int localUserId = hostHolder.getUser().getId();
                if (localUserId == message.getToId()) {
                    // 用户点击详情时，将消息设为已读
                    messageService.updateStatus(message.getId());
                    message = messageService.getById(message.getId());
                }


                vo.set("message", message);

                // 找到发送消息的用户的id
                vo.set("user", userService.selectById(message.getFromId()));


                vos.add(vo);

            }
            model.addAttribute("vos", vos);


        } catch (Exception e) {
            logger.error("获取详情失败" + e.getMessage());
        }
        return "/letterDetail";

    }

}
