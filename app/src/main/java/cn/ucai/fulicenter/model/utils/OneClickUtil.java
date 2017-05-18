package cn.ucai.fulicenter.model.utils;

import java.util.Calendar;

/**
 * Created by Administrator on 2017/5/15 0015.
 */

public class OneClickUtil {
    private String methodName;
    //设置第二次点击的时间
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    public OneClickUtil(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }

    public boolean check() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            return false;
        } else {
            return true;
        }
    }
}
