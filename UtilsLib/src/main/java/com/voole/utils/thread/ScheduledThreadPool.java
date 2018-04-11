package com.voole.utils.thread;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Created by lichao
 * @desc 本类是定时任务Timer的线程池实现类
 * @time 2017/11/10 14:28
 * 邮箱：lichao@voole.com
 */

public class ScheduledThreadPool {
    private ScheduledExecutorService pool = null;
    public ScheduledThreadPool(String name) {
        pool = new ScheduledThreadPoolExecutor(1,new BasicThreadFactory(name));
    }
    /**
     * 设置
     * @param command 添加的线程对象
     * @param initialDelay 几秒后执行
     * @param period 每过多少秒执行一次
     * @param unit 执行单位
     */
    public void scheduleAtFixedRate(Runnable command,
                                    long initialDelay,
                                    long period,
                                    TimeUnit unit){
        if (pool!=null){
            pool.scheduleAtFixedRate(command, initialDelay,period, unit);
        }
    }
    /**
     * 设置
     * @param command 添加的线程对象
     * @param initialDelay 几秒后执行
     * @param unit 执行单位
     */
    public void scheduleDelay(Runnable command,
                                    long initialDelay,
                                    TimeUnit unit){
        if (pool!=null){
            pool.schedule(command,initialDelay,unit);
        }
    }

    public void shutdown(){
        if (pool!=null){
            pool.shutdown();
        }
    }

    public void shutdownNow(){
        if (pool!=null){
            pool.shutdownNow();
        }
    }
}
