package com.bingo.qa;

import com.bingo.qa.model.EntityType;
import com.bingo.qa.service.LikeService;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/** 测试示例
 * Created by bingo on 2018/6/5.
 */

/**
 * 测试四部曲：
 * 1.初始化数据
 * 2.执行要测试的业务
 * 3.验证测试的数据
 * 4.清理数据
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LikeServiceTests {
    @Autowired
    LikeService likeService;

    @Test
    public void testStatus() {
        // Assert.assertEquals(1, likeService.getLikeStatus(1, EntityType.ENTITY_USER, 1));
    }

    // 异常测试
    // 给一些非法参数，看是否按预期抛出异常
    @Test(expected = IllegalArgumentException.class)
    public void testException() {
        System.out.println("testException");
        throw new IllegalArgumentException("非法参数异常");
    }

    // 如果所有测试之前都需要先初始化一些参数，可放在beforeClass
    @BeforeClass
    public static void beforeClass() {
        System.out.println("beforeClass");
    }

    // 所有都测试完，进行数据清理，可以放在afterClass
    @AfterClass
    public static void afterClass() {
        System.out.println("afterClass");
    }

}

/**
 * 附SpringBoot项目打包：
 * https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-create-a-deployable-war-file
 *
 * 1.applicationi继承SpringBootServletInitializer
 * 2.pom.xml打包成war
 * 3.mvn package -Dmaven.test.skip=true
 * 4.去除多余的main函数
 */
