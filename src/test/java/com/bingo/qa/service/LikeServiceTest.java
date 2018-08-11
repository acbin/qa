package com.bingo.qa.service;

import com.bingo.qa.service.impl.LikeServiceImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LikeServiceTest {

    @Autowired
    LikeServiceImpl likeService;

    @Test
    public void like() {
        likeService.like(1, 1, 1);
    }

    @Test
    public void disLike() {
        likeService.disLike(1, 1, 1);
    }

    @Test
    public void getLikeStatus() {
        likeService.getLikeStatus(1, 1, 1);
    }

    @Test
    public void getLikeCount() {
        long cnt = likeService.getLikeCount(1, 1);
        System.out.println(cnt);
    }
}