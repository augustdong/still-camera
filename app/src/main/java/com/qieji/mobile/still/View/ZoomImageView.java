package com.qieji.mobile.still.View;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

// todo
// 无论移动、缩放：边界不能越过蒙板框

/**
 * Created by August on 2015/2/27.
 * 可以自由移动、缩放的ImageView
 * todo 添加可以旋转
 */
public class ZoomImageView extends ImageView implements ScaleGestureDetector.OnScaleGestureListener,
        View.OnTouchListener, ViewTreeObserver.OnGlobalLayoutListener {

    public static final float SCALE_MAX = 4.0f;
    // 初始化时的缩放比例，如果图片宽或高大于屏幕，此值将小于0
    public float initScale = 1.0f;

    public final Matrix mScaleMatrix = new Matrix();
    public final float[] mMatrixValues = new float[9];

    // 手势侦测
    public ScaleGestureDetector mScaleGestureDetector = null;

    public boolean bOnGlobalLayoutOnce = true;


    public int lastPointerCount;
    public float mLastX;
    public float mLastY;

    public ZoomImageView(Context context) {
        super(context);
        super.setScaleType(ScaleType.MATRIX);
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        this.setOnTouchListener(this);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setScaleType(ScaleType.MATRIX);
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        this.setOnTouchListener(this);
    }

    // 返回当前图像缩放比例
    public final float getScale() {
        mScaleMatrix.getValues(mMatrixValues);
        return mMatrixValues[Matrix.MSCALE_X];
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scale = getScale();
        float scaleFactor = detector.getScaleFactor();

        if (getDrawable() == null) {
            return  true;
        }

        mScaleMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
        setImageMatrix(mScaleMatrix);

        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);

        // 图片随着手移动
        float x = 0, y = 0;
        // 拿到触摸点的个数
        final int pointerCount = event.getPointerCount();
        // 得到多个触摸点的x与y均值
        for (int i = 0; i < pointerCount; i++)
        {
            x += event.getX(i);
            y += event.getY(i);
        }
        x = x / pointerCount;
        y = y / pointerCount;

        /**
         * 每当触摸点发生变化时，重置mLasX , mLastY
         */
        if (pointerCount != lastPointerCount)
        {
            mLastX = x;
            mLastY = y;
        }

        lastPointerCount = pointerCount;

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - mLastX;
                float dy = y - mLastY;
                if (getDrawable() != null) {
                    // if ()
                    mScaleMatrix.postTranslate(dx, dy);
                    setImageMatrix(mScaleMatrix);
                }
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                lastPointerCount = 0;
                break;
        }

        return true;
    }

    @Override
    public void onGlobalLayout() {
        if (bOnGlobalLayoutOnce) {
            Drawable d = getDrawable();
            if (d == null) {
                return;
            }

            int width = getWidth();
            int height = getHeight();
            int drawableWidth = d.getIntrinsicWidth();
            int drawableHeight = d.getIntrinsicHeight();
            float scale = 1.0f;
            // todo 最好可以设置初始化大小
            // 如果图片的款或者高大于屏幕，则所防止屏幕的宽或高
            if (drawableWidth > width && drawableHeight <= height)
            {
                scale = width * 1.0f / drawableWidth;
            }
            if (drawableHeight > height && drawableWidth <= width)
            {
                scale = height * 1.0f / drawableHeight;
            }
            // 如果宽和高都大于屏幕，则让其按按比例适应屏幕大小
            if (drawableWidth > width && drawableHeight > height)
            {
                scale = Math.min(drawableWidth * 1.0f / width, drawableHeight * 1.0f / height);
            }
            initScale = scale;
            // 图片移动至屏幕中心
            mScaleMatrix.postTranslate((width - drawableWidth) / 2, (height - drawableHeight) / 2);
            mScaleMatrix.postScale(scale, scale, getWidth() / 2, getHeight() / 2);
            setImageMatrix(mScaleMatrix);
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
