package com.qieji.mobile.still.Widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.qieji.mobile.still.View.ClipImageBorderView;
import com.qieji.mobile.still.View.ZoomImageView;

import static com.qieji.mobile.common.util.dp2px;

/**
 * 图片剪裁控件
 * Created by August on 2015/2/28.
 */
public class ClipImage extends RelativeLayout {

    private static final int MASK_HPADDING = 20; // 遮罩框的水平padding（dp）
    private static final double MASK_RATIO = 16 / 9.0d;

    private ZoomImageView mZoomImageView; // 可移动的图片
    private ClipImageBorderView mClipImageBorderView; // 显示给用户的边框

    public ClipImage(Context context, AttributeSet attrs) {
        super(context, attrs);

        mZoomImageView = new ZoomImageView(context);
        mClipImageBorderView = new ClipImageBorderView(context);

        android.view.ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        this.addView(mZoomImageView, lp);
        this.addView(mClipImageBorderView, lp);
    }

    public void setImageBitmap(Bitmap bm) {
        mZoomImageView.setImageBitmap(bm);
    }

    public static RectF getMaskRect(int viewHeight, int viewWidth, Context context) {
        int hPadding = dp2px(MASK_HPADDING, context);
        int width = viewWidth - hPadding * 2;
        int height = (int)(width / MASK_RATIO);
        int vPadding = (viewHeight - height) / 2;
        RectF rect = new RectF(hPadding, vPadding, hPadding + width, vPadding + height);
        return rect;
    }

}
