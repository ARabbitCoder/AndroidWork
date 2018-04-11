package com.voole.utils.thread;

import android.support.annotation.NonNull;

import java.util.concurrent.ThreadFactory;

/**
 * @author Created by lichao
 * @desc 线程创建类
 * @time 2017/11/9 10:54
 * 邮箱：lichao@voole.com
 */

public class BasicThreadFactory implements ThreadFactory {
    /**
     * 名字
     */
    private String name;
    public BasicThreadFactory(String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(@NonNull Runnable r) {
        Thread thread = new Thread(r,name+ "-Thread");
        return thread;
    }
}
