package com.bingo.qa.controller;

import com.bingo.qa.model.EntityType;
import com.bingo.qa.model.HostHolder;
import com.bingo.qa.service.LikeService;
import com.bingo.qa.util.QaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/like",method = RequestMethod.POST)
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId) {

        // 用户未登录，直接返回
        if (hostHolder.getUser() == null) {
            return QaUtil.getJSONString(999);
        }
        // System.out.println("commentId: " + commentId);

        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return QaUtil.getJSONString(0, String.valueOf(likeCount));
    }


    @RequestMapping(path = "/dislike",method = RequestMethod.POST)
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId) {

        // 用户未登录，直接返回
        if (hostHolder.getUser() == null) {
            return QaUtil.getJSONString(999);
        }

        long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return QaUtil.getJSONString(0, String.valueOf(likeCount));
    }


}
