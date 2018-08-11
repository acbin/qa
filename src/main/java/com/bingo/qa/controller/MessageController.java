package com.bingo.qa.controller;


import com.bingo.qa.model.HostHolder;
import com.bingo.qa.model.Message;
import com.bingo.qa.model.User;
import com.bingo.qa.model.ViewObject;
import com.bingo.qa.service.impl.MessageService;
import com.bingo.qa.service.impl.SensitiveService;
import com.bingo.qa.service.impl.UserService;
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
import org.springframework.web.util.HtmlUtils;

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
    private SensitiveService sensitiveService;

    @Autowired
    private UserService userService;

    @PostMapping(value = {"/msg/addMessage"})
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content) {
        try {
            User localUser = hostHolder.getUser();

            if (localUser == null) {
                return QaUtil.getJSONString(999, "未登录");
            }

            // 查找私信接收方
            User user = userService.selectByName(toName);
            if (user == null) {
                return QaUtil.getJSONString(1, "用户不存在");
            }

            Message message = new Message();
            message.setCreatedDate(new Date());

            // 私信的fromId为当前登录用户的Id
            message.setFromId(localUser.getId());
            message.setToId(user.getId());

            // 对私信内容进行敏感词过滤
            content = sensitiveService.filter(HtmlUtils.htmlEscape(content));
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
        User localUser = hostHolder.getUser();
        if (localUser == null) {
            return "redirect:/reglogin";
        }

        int localUserId = localUser.getId();

        // 查找与当前登录用户相关联的私信列表
        List<Message> conversationList = messageService.getConversationList(localUserId, 0, 10);

        List<ViewObject> vos = new ArrayList<>();
        for (Message message : conversationList) {
            ViewObject vo = new ViewObject();
            vo.set("conversation", message);

            // 与当前登录用户私信的另一方的id
            int targetId = message.getFromId() == localUserId ? message.getToId() : message.getFromId();

            // 根据id查找出另一方的信息
            vo.set("user", userService.selectById(targetId));

            // 当前登录用户与另一方私信未读数量
            // 另一方发私信给当前用户，当前用户为to方
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
            // 获取当前登录用户
            User localUser = hostHolder.getUser();
            if (localUser == null) {
                // 未登录，直接返回登录页面
                return "redirect:/reglogin";
            }

            int localUserId = localUser.getId();

            // 根据conversationId获取双方私信列表
            List<Message> messageList = messageService.getConversationDetail(conversationId, 0, 10);
            List<ViewObject> vos = new ArrayList<>();

            for (Message message : messageList) {
                ViewObject vo = new ViewObject();

                // 当前登录用户属于消息接收方
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