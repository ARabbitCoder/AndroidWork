package com.ayearn.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author liujingwei
 * @DESC
 *1 @Entity 定义实体
 *2 @nameInDb 在数据库中的名字，如不写则为实体中类名
 *3 @indexes 索引
 *4 @createInDb 是否创建表，默认为true,false时不创建
 *5 @schema 指定架构名称为实体
 *6 @active 无论是更新生成都刷新
 *7 @Id
 *8 @NotNull 不为null
 *9 @Unique 唯一约束
 *10 @ToMany 一对多
 *11 @OrderBy 排序
 *12 @ToOne 一对一
 *13 @Transient 不存储在数据库中
 *14 @generated 由greendao产生的构造函数或方法
 * @time 2018-3-30 14:14
 */
@Entity(createInDb = true,nameInDb = "user")
public class User {
    @Id
    private long id;
    @NotNull
    private String username;
    @NotNull
    private String userpass;
    public String getUserpass() {
        return this.userpass;
    }
    public void setUserpass(String userpass) {
        this.userpass = userpass;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    @Generated(hash = 1868800420)
    public User(long id, @NotNull String username, @NotNull String userpass) {
        this.id = id;
        this.username = username;
        this.userpass = userpass;
    }
    @Generated(hash = 586692638)
    public User() {
    }
}
