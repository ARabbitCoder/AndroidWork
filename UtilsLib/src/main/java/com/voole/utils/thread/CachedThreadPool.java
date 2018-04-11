package com.voole.utils.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Created by lichao
 * @desc 缓存线程池
 * @time 2017/11/10 14:04
 * 邮箱：lichao@voole.com
 */

public class CachedThreadPool {
    /**
     * 当前线程池控制对象
     */
    private  volatile  static CachedThreadPool cachedThreadPool;
    /**
     * 当前线程池对象
     */
    private static ExecutorService executorService ;
    /**
     * 线程池名称
     */
    private static  final  String NAME = "CachedThreadPool";
    /**
     * 线程数量
     */
    private static  final  int MAXIMUMPOOLSIZE = 10;
    /**
     * 缓存线程池
     */
    private CachedThreadPool() {
        executorService = new ThreadPoolExecutor(MAXIMUMPOOLSIZE, MAXIMUMPOOLSIZE,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),new BasicThreadFactory(NAME));
    }

    /**
     * 单例模式
     * @return
     */
    public static CachedThreadPool getInstance(){
        if (cachedThreadPool==null){
            synchronized (CachedThreadPool.class){
                if (cachedThreadPool==null){
                    cachedThreadPool = new CachedThreadPool();
                }
            }
        }
        return  cachedThreadPool;
    }

    /**
     * 获取当前线程池对象
     * @return
     */
    public ExecutorService getCachedThreadPool (){
        return executorService;
    }

    /**
     *  将线程加入到线程池中
     * @param command 线程
     * @return true 加入成功,false 加入失败
     */
    public boolean execute(Runnable command){
        boolean isSuccess = false;
        if (command!=null){
            executorService.execute(command);
            isSuccess = true;
        }
        return isSuccess;
    }
}
