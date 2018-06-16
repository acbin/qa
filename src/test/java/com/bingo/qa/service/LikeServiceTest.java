package com.bingo.qa.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class LikeServiceTest {

    @Autowired
    LikeService likeService;

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