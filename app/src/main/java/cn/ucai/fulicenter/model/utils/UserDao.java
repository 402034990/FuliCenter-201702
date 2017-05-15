package cn.ucai.fulicenter.model.utils;

import android.content.Context;

import cn.ucai.fulicenter.model.bean.User;

/**
 * Created by Administrator on 2017/5/10 0010.
 */

public class UserDao {
    public UserDao(Context context) {
        DBManager.getInstance().iniDB(context);
    }

    public User getUser(String username) {
        return DBManager.getInstance().getUser(username);
    }

    public boolean saveUser(User user) {
        return DBManager.getInstance().saveUser(user);
    }
}

