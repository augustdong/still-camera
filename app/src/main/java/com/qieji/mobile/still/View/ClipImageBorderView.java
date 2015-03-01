package com.qieji.mobile.still.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by August on 2015/2/27.
 * 图像截取遮罩框
 */
public class ClipImageBorderView extends View {

    // 遮罩框的水平padding（dp）
    public int hPadding = 30;
    // 遮罩框的垂直padding
    public int vPadding;
    public int borderWidth = 1;
    // 遮罩框的宽高
    public int width;
    public int height;
    public int borderColor = Color.parseColor("#FFFFFF");
    public int maskColor = Color.parseColor("#AA000000");

    public Paint mPaint;

    public ClipImageBorderView(Context context) {
        super(context);
        // dp转px
        hPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, hPadding, getResources().getDisplayMetrics());
        vPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, vPadding, getResources().getDisplayMetrics());
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    public ClipImageBorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // dp转px
        hPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, hPadding, getResources().getDisplayMetrics());
        vPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, vPadding, getResources().getDisplayMetrics());
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth() - 2 * hPadding;
        height = (width / 16) * 9;
        vPadding = (getHeight() - height) / 2;
        mPaint.setColor(maskColor);
        mPaint.setStyle(Paint.Style.FILL);
        // start to paint
        canvas.drawRect(0, 0, hPadding, getHeight(), mPaint);
        canvas.drawRect(getWidth() - hPadding, 0, getWidth(), getHeight(), mPaint);
        canvas.drawRect(hPadding, 0, getWidth() - hPadding, vPadding, mPaint);
        canvas.drawRect(hPadding, getHeight() - vPadding, getWidth() - hPadding, getHeight(), mPaint);
        mPaint.setColor(borderColor);
        mPaint.setStrokeWidth(borderWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(hPadding, vPadding, getWidth() - hPadding, getHeight() - vPadding, mPaint);
    }
}
