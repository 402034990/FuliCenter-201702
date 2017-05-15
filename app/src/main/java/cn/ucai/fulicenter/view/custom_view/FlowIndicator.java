package cn.ucai.fulicenter.view.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import cn.ucai.fulicenter.R;

/**
 * Created by Administrator on 2017/5/5 0005.
 */

public class FlowIndicator extends View {
    private int mCount;
    private int mFocus;
    private int mNormal_Color;
    private int mFocus_Color;
    private int mSpace;
    private int mRadius;
    Paint mPaint;
    public FlowIndicator(Context context) {
        super(context);
    }

    public int getmCount() {
        return mCount;
    }

    public void setmCount(int mCount) {
        this.mCount = mCount;
        invalidate();
    }

    public int getmFocus() {
        return mFocus;
    }

    public void setmFocus(int mFocus) {
        this.mFocus = mFocus;
        invalidate();
    }

    public FlowIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FlowIndicator);
        mCount = array.getInt(R.styleable.FlowIndicator_count, 1);
        mFocus = array.getInt(R.styleable.FlowIndicator_focus,0);
        mSpace = array.getDimensionPixelOffset(R.styleable.FlowIndicator_space, 10);
        mRadius = array.getDimensionPixelOffset(R.styleable.FlowIndicator_r, 10);
        mNormal_Color = array.getColor(R.styleable.FlowIndicator_normal_color, Color.WHITE);
        mFocus_Color = array.getColor(R.styleable.FlowIndicator_focus_color, Color.RED);

        mPaint = new Paint();
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getMeasuredWidth(widthMeasureSpec),getMeasuredHeight(heightMeasureSpec));
    }

    private int getMeasuredHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int result = size;
        if (mode != MeasureSpec.EXACTLY) {
            size = getPaddingBottom() + getPaddingTop() + mRadius * 2;
            result = Math.min(result, size);
        }
        return result;
    }

    private int getMeasuredWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int result = size;
        if (mode != MeasureSpec.EXACTLY) {
            size = getPaddingLeft() + getPaddingRight() + mCount * 2 * mRadius + mSpace * (mCount - 1);
            result = Math.min(result, size);
        }
        return result;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAntiAlias(true);
        if (mCount == 0) {
            return;
        }

        int left = (getWidth() - (mCount * 2 * mRadius + mSpace * (mCount - 1)) / 2);

        for (int i=0;i<mCount;i++) {
            int x = getLeftPaddingOffset() + i * (2 * mRadius + mSpace) + mRadius;
            int color = i == mFocus ? mFocus_Color : mNormal_Color;
            mPaint.setColor(color);
            canvas.drawCircle(x,getHeight()/2,mRadius,mPaint);
        }
    }
}
