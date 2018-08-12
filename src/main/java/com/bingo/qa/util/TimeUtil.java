package com.bingo.qa.util;

/**
 * @author bingo
 * @since 2018/8/12
 */

public class TimeUtil {

    /**
     * 休眠1s
     */
    public static void wait1s() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 休眠2s
     */
    public static void wait2s() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
