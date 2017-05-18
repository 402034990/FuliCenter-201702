package cn.ucai.fulicenter.model.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/15 0015.
 */

public class AntiShake {
    private List<OneClickUtil> utils = new ArrayList<>();

    public boolean check(Object o) {
        String flag;
        if (o == null) {
            flag = Thread.currentThread().getStackTrace()[2].getMethodName();
        } else {
            flag = o.toString();
        }
        for (OneClickUtil util : utils) {
            if (util.getMethodName().equals(flag)) {
                return util.check();
            }
        }
        OneClickUtil clickUtil = new OneClickUtil(flag);
        utils.add(clickUtil);
        return clickUtil.check();
    }

    public boolean check() {
        return check(null);
    }
}
