package cn.ucai.fulicenter.application;

import android.app.Application;
import android.content.Context;

import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.utils.UserDao;

/**
 * Created by Administrator on 2017/5/3 0003.
 */

public class FuLiCenterApplication extends Application{
    private static FuLiCenterApplication instance;
    /** 存储的文件名 */
    public static final String DATABASE = "Database";
    /** 存储后的文件路径：/data/data/<package name>/shares_prefs + 文件名.xml */
    public static final String PATH = "/data/data/code.sharedpreferences/shared_prefs/Database.xml";

    private User currentUser;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static FuLiCenterApplication getInstance() {
        return instance;
    }

    public User getUser() {
        return currentUser;
    }

    public void setUser(User user) {
        this.currentUser = user;
    }


}
