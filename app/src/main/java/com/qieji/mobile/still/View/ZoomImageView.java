package com.qieji.mobile.still.View;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.qieji.mobile.still.Widget.ClipImage;

// todo
// 无论移动、缩放：边界不能越过蒙板框

/**
 * Created by August on 2015/2/27.
 * 可以自由移动、缩放的ImageView
 * todo 添加可以旋转
 */
public class ZoomImageView extends ImageView implements ScaleGestureDetector.OnScaleGestureListener,
        View.OnTouchListener, ViewTreeObserver.OnGlobalLayoutListener {

    Context mContext;

    RectF mMaskRect; // 遮罩的Rect

    public static final float SCALE_MAX = 4.0f;
    // 初始化时的缩放比例，如果图片宽或高大于屏幕，此值将小于0
    public float initScale = 1.0f;

    public final float[] mMatrixValues = new float[9];

    public final Matrix mMatrix = new Matrix(); // 图片每次变动的matrix
    public final Matrix mSavedMatrix = new Matrix();
    public final Matrix mStartMatrix = new Matrix(); // 记录每次操作图片开始时的matrix

    // 手势侦测
    public ScaleGestureDetector mScaleGestureDetector = null;

    public boolean bOnGlobalLayoutOnce = true;

    public int mLastPointerCount;
    public float mLastX;
    public float mLastY;
    PointF mStart = new PointF();

    public ZoomImageView(Context context) {
        super(context);
        mContext = context;
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
        mMatrix.getValues(mMatrixValues);
        return mMatrixValues[Matrix.MSCALE_X];
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scale = getScale();
        float scaleFactor = detector.getScaleFactor();

        if (getDrawable() == null) {
            return  true;
        }

        mMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
        setImageMatrix(mMatrix);

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
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mSavedMatrix.set(mMatrix);
                mStartMatrix.set(mMatrix);
                mStart.set(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                if (getDrawable() != null) {
                    mMatrix.set(mSavedMatrix);
                    mMatrix.postTranslate(event.getX() - mStart.x, event.getY()
                            - mStart.y);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        if (event.getAction() == MotionEvent.ACTION_UP
                || event.getAction() == MotionEvent.ACTION_POINTER_UP) {
            if (isOutOfFrame(mMatrix)) {
                mMatrix.set(mStartMatrix);
                setImageMatrix(mMatrix);
            }
        } else {
            setImageMatrix(mMatrix);
        }

        mScaleGestureDetector.onTouchEvent(event);

        return true;
    }

    /**
     * 判断图片可见范围是否超出遮罩边框
     *
     * @param matrix
     * @return
     */
    public boolean isOutOfFrame(final Matrix matrix) {
        RectF visibleRect = getVisibleRect(matrix);
        return !visibleRect.contains(mMaskRect);
    }

    /**
     * 获取图片可见范围
     *
     * @param matrix
     * @return
     */
    public RectF getVisibleRect(final Matrix matrix) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return new RectF(0, 0, 0, 0);
        }
        Rect rect = drawable.getBounds();
        int width = rect.width();
        int height = rect.height();
        float[] values = new float[9];
        matrix.getValues(values);
        Rect visibleRect = new Rect();
        width = (int) (width * values[8]);
        height = (int) (height * values[8]);
        visibleRect.left = (int) values[2];
        visibleRect.top = (int) values[5];
        visibleRect.right = (int) (visibleRect.left + width * values[0] + height
                * values[1]);
        visibleRect.bottom = (int) (visibleRect.top + height * values[0] - width
                * values[1]);
        RectF newRect = new RectF();
        newRect.left = Math.min(visibleRect.left, visibleRect.right);
        newRect.top = Math.min(visibleRect.top, visibleRect.bottom);
        newRect.right = Math.max(visibleRect.left, visibleRect.right);
        newRect.bottom = Math.max(visibleRect.top, visibleRect.bottom);
        return newRect;
     }

    @Override
    public void onGlobalLayout() {
        if (bOnGlobalLayoutOnce) {
            mMaskRect = ClipImage.getMaskRect(getHeight(), getWidth(), mContext);
            Drawable drawable = getDrawable();
            if (drawable == null) {
                return;
            }
            float scale = 1.0f;
            int width = getWidth();
            int height = getHeight();
            int drawableWidth = drawable.getIntrinsicWidth();
            int drawableHeight = drawable.getIntrinsicHeight();
            if (drawableWidth >= width && drawableHeight <= height) {
                scale = width * 1.0f / drawableWidth;
            } else if (drawableHeight >= height && drawableWidth <= width) {
                scale = height * 1.0f / drawableHeight;
            } else if (drawableWidth >= width && drawableHeight >= height) {
                scale = Math.min(drawableWidth * 1.0f / width, drawableHeight * 1.0f / height);
            } else if (drawableWidth <= width && drawableHeight <= height) {
                scale = Math.min(width * 1.0f / drawableWidth,height * 1.0f / drawableHeight);
            }
            initScale = scale;
            mMatrix.postTranslate((width - drawableWidth) / 2, (height - drawableHeight) / 2);
            mMatrix.postScale(scale, scale, getWidth() / 2, getHeight() / 2);
            setImageMatrix(mMatrix);
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
