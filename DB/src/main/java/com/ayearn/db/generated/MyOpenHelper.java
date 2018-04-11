package com.ayearn.db.generated;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.voole.utils.log.LogUtil;

/**
 * @author Created by liujingwei
 * @desc
 * @time 2018/1/4 11:36
 * 邮箱：liujingwei@voole.com
 */

public class MyOpenHelper extends DaoMaster.OpenHelper {

    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //操作数据库的更新 有几个表升级都可以传入到下面
        LogUtil.d("onUpgrade db newVersion is " + newVersion + " oldVersion is " + oldVersion);
        if (newVersion>oldVersion){
//            FavoriteFilmDao.createTable(db, true);
//            // 加入新字段
//            db.execSQL("ALTER TABLE 'favorite_film' ADD 'NUM' TEXT;");
            MigrationHelper.migrate(db,UserDao.class);
        }
    }
}
