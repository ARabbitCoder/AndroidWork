package com.ayearn.db.generated;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ayearn.db.bean.DownLoadInfo;
import com.ayearn.db.bean.User;
import org.greenrobot.greendao.query.QueryBuilder;
import java.util.List;

/**
 * @author Created by liujingwei
 * @desc GreenDao 操作类,单例方式实现数据库的增删改查,可以自定义数据库的存储路径
 * @time 2018/1/3 19:54
 * 邮箱：liujingwei@voole.com
 */

public class GreenDaoManager {
    private static final String DB_NAME = "voole.db";
    public SQLiteDatabase mDb ;
    public DaoMaster mDaoMaster ;
    public DaoSession mDaoSession ;
    public static GreenDaoManager mGreenDaoManager = null;
    private GreenDaoManager(){

    }

    /**
     * 这里需要注意,在application中初始化时候需要调用init方法
     * @param context
     */
    public void  init(Context context){
        MyOpenHelper myOpenHelper = new MyOpenHelper(context,DB_NAME,null);
        mDb = myOpenHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(mDb);
        mDaoSession = mDaoMaster.newSession();
    }
    public  static  GreenDaoManager getInstance(){
        if (mGreenDaoManager==null){
            synchronized (GreenDaoManager.class){
                if (mGreenDaoManager==null){
                    mGreenDaoManager = new GreenDaoManager();
                }
            }
        }
        return mGreenDaoManager;
    }

    /**
     * 插入数据 返回成功后的id
     * @param user
     */
     public long insertUser(User user){
        if(user!=null){
            return mDaoSession.getUserDao().insertOrReplace(user);
        }
        return -1;
     }

    /**
     * 删除数据 根据user的id值为删除标准
     * @param user
     */
     public void deleteUser(User user){
         if (user!=null){
             mDaoSession.getUserDao().delete(user);
         }
     }

    /**
     * 根据 ID  key删除
     * @param key
     */
     public void delectUserByKey(long key){
         mDaoSession.getUserDao().deleteByKey(key);
     }

    /**
     * 更新数据
     * @param user
     */
    public void update(User user){
         if(user!=null){
             mDaoSession.getUserDao().update(user);
         }
    }

    /**
     * 查询所有
     * @return
     */
    public  List<User> queryAllUser(){
        return mDaoSession.getUserDao().loadAll();
    }

    private void testQuery(){
       QueryBuilder builder =  mDaoSession.getUserDao().queryBuilder();
       builder.where(UserDao.Properties.Id.eq(880909),UserDao.Properties.Username.eq("jack"));
    }

    public List<DownLoadInfo> queryAllDownload(){
        return mDaoSession.getDownLoadInfoDao().loadAll();
    }

    public void updateDownloadInfo(DownLoadInfo downLoadInfo){
        mDaoSession.getDownLoadInfoDao().update(downLoadInfo);
    }
}
