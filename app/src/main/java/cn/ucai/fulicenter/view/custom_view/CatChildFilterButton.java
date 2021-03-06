package cn.ucai.fulicenter.view.custom_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.ArrayList;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.utils.Utils;
import cn.ucai.fulicenter.view.adapter.CatFilterAdapter;

/**
 * Created by Administrator on 2017/5/9 0009.
 */

public class CatChildFilterButton extends Button {
    Context mContext;
    CatChildFilterButton mbtnTop;
    PopupWindow mPopupWindow;
    GridView mgvCategory;
    CatFilterAdapter mAdapter;

    /**
     * true:arrow down
     * false:arrow up
     */
    boolean mExpandOff;

    public CatChildFilterButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mbtnTop = this;
        mExpandOff = true;
        initGridView();
    }

    private void initPopupWindow() {
        mPopupWindow = new PopupWindow();
        mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xbb000000));
        mPopupWindow.setContentView(mgvCategory);
        mPopupWindow.showAsDropDown(mbtnTop);
    }

    private void initGridView() {
        mgvCategory = new GridView(mContext);
        mgvCategory.setColumnWidth(Utils.px2dp(mContext, 1500));
        mgvCategory.setHorizontalSpacing(Utils.px2dp(mContext, 10));
        mgvCategory.setVerticalSpacing(Utils.px2dp(mContext, 10));
        mgvCategory.setNumColumns(2);
        mgvCategory.setBackgroundColor(Color.TRANSPARENT);
        mgvCategory.setPadding(3, 3, 3, 3);
        mgvCategory.setCacheColorHint(0);
    }

    private void setBtnTopArrow() {
        Drawable right = null;
        int resId;
        if (mExpandOff) {
            right = mContext.getResources().getDrawable(R.drawable.arrow2_down);
            resId = R.drawable.arrow2_down;
        } else {
            right = mContext.getResources().getDrawable(R.drawable.arrow2_up);
            resId = R.drawable.arrow2_up;
        }
        right.setBounds(0, 0, getDrawableWidth(mContext, resId), getDrawableHeight(mContext, resId));
        mbtnTop.setCompoundDrawables(null, null, right, null);
        mExpandOff = !mExpandOff;
    }


    /**
     * 设置分类列表的下拉按钮单击事件监听
     *
     * @param groupName
     * @param childList
     */
    public void setOnCatFilterClickListener(final String groupName,
                                            final ArrayList<CategoryChildBean> childList) {
        mbtnTop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mbtnTop.setTextColor(Color.WHITE);
                mbtnTop.setText(groupName);
                if (mExpandOff) {//若分类列表的窗口未打开，则弹出窗口
                    mAdapter = new CatFilterAdapter(mContext, childList);
                    mgvCategory.setAdapter(mAdapter);
                    initPopupWindow();
                } else {//否则，关闭窗口
                    if (mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                }
                setBtnTopArrow();
            }
        });
    }


    public static int getDrawableWidth(Context context, int resId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        return bitmap.getWidth();
    }

    public static int getDrawableHeight(Context context, int resId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        return bitmap.getHeight();
    }

    public  void release() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }
}
