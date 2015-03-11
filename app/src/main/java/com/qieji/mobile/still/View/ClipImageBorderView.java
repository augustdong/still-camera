package com.qieji.mobile.still.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.qieji.mobile.still.Widget.ClipImage;

/**
 * Created by August on 2015/2/27.
 * 图像截取遮罩框
 */
public class ClipImageBorderView extends View implements  ViewTreeObserver.OnGlobalLayoutListener {

    Context mContext;
    public Paint mPaint;
    boolean bOnGlobalLayoutOnce = true;

    RectF mMaskRect; // 遮罩的Rect
    public int mBorderWidth = 1;
    public int borderColor = Color.parseColor("#FFFFFF");
    public int maskColor = Color.parseColor("#AA000000");

    public ClipImageBorderView(Context context) {
        super(context);
        mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mMaskRect == null || mPaint == null) {
            Log.w("asd", "dd");
            return;
        }
        mPaint.setColor(maskColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, mMaskRect.left, getHeight(), mPaint);
        canvas.drawRect(mMaskRect.right, 0, getWidth(), getHeight(), mPaint);
        canvas.drawRect(mMaskRect.left, 0, mMaskRect.right, mMaskRect.top, mPaint);
        canvas.drawRect(mMaskRect.left, mMaskRect.bottom, mMaskRect.right, getHeight(), mPaint);
        mPaint.setColor(borderColor);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(mMaskRect, mPaint);
    }

    @Override
    public void onGlobalLayout() {
        if (bOnGlobalLayoutOnce) {
            mMaskRect = ClipImage.getMaskRect(getHeight(), getWidth(), mContext);
            bOnGlobalLayoutOnce = false;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

}
