package cn.ucai.fulicenter.model.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cn.ucai.fulicenter.model.bean.User;

/**
 * Created by Administrator on 2017/5/10 0010.
 */

public class DBManager {
    private static DBOpenHelper sHelper;
    private static DBManager manager = new DBManager();

    public static DBManager getInstance() {
        return manager;
    }

    public  void iniDB(Context context) {
        sHelper = DBOpenHelper.getInstance(context);
    }

    public boolean saveUser(User user) {
        SQLiteDatabase database = sHelper.getWritableDatabase();
        if (database.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(DBOpenHelper.USER_COLUMN_NAME,user.getMuserName());
            values.put(DBOpenHelper.USER_COLUMN_NICK,user.getMuserNick());
            values.put(DBOpenHelper.USER_COLUMN_AVATAR_PATH,user.getMavatarPath());
            values.put(DBOpenHelper.USER_COLUMN_AVATAR_TYPE,user.getMavatarType());
            values.put(DBOpenHelper.USER_COLUMN_AVATAR,user.getMavatarId());
            values.put(DBOpenHelper.USER_COLUMN_AVATAR_SUFFIX,user.getMavatarSuffix());
            values.put(DBOpenHelper.USER_COLUMN_AVATAR_UPDATE_TIME,user.getMavatarLastUpdateTime());
            long insert = database.replace(DBOpenHelper.USER_TABLE_NAME, null, values);
            return insert > 0 ? true : false;
        }
        return false;
    }

    public synchronized User getUser(String username) {
        SQLiteDatabase database = sHelper.getWritableDatabase();
        User user = new User();
        if (database.isOpen()) {
            String sql = "select * from " + sHelper.USER_TABLE_NAME + " where " + sHelper.USER_COLUMN_NAME + "=?";
            Cursor cursor = database.rawQuery(sql, new String[]{username});
            if (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(sHelper.USER_COLUMN_NAME));
                String nick = cursor.getString(cursor.getColumnIndex(sHelper.USER_COLUMN_NICK));
                String path = cursor.getString(cursor.getColumnIndex((sHelper.USER_COLUMN_AVATAR_PATH)));
                String suffix = cursor.getString(cursor.getColumnIndex(sHelper.USER_COLUMN_AVATAR_SUFFIX));
                String time = cursor.getString(cursor.getColumnIndex(sHelper.USER_COLUMN_AVATAR_UPDATE_TIME));
                int type = cursor.getInt(cursor.getColumnIndex(sHelper.USER_COLUMN_AVATAR_TYPE));
                user = new User(username, nick, id, path, suffix, type, time);
            }
        }
        return user;
    }
}
