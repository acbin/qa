package com.bingo.qa.model;

import org.springframework.stereotype.Component;

/**
 * @author bingo
 */
@Component
public class HostHolder {
    /**
     * 线程本地变量
     * 每条线程都有自己的一个变量
     *
     * 就是将私有线程和该线程存放的副本对象做一个映射，各个线程之间的变量互不干扰。
     * 别的线程并不能获取到当前线程的副本值，形成了副本的隔离，互不干扰。
     */
    private static ThreadLocal<User> users = new ThreadLocal<>();

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();
    }
}
