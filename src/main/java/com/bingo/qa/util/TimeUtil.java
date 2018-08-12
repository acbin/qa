package com.bingo.qa.util;

/**
 * @author bingo
 * @since 2018/8/12
 */

public class TimeUtil {

    public static void wait1s() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
