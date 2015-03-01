package com.qieji.mobile.still.Widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.qieji.mobile.still.View.ClipImageBorderView;
import com.qieji.mobile.still.View.ZoomImageView;

/**
 * 图片剪裁控件
 * Created by August on 2015/2/28.
 */
public class ClipImage extends RelativeLayout {

    private ZoomImageView mZoomImageView;
    private ClipImageBorderView mClipImageBorderView;

    private int mHorizontalPadding = 20;

    public ClipImage(Context context, AttributeSet attrs) {
        super(context, attrs);

        mZoomImageView = new ZoomImageView(context);
        mClipImageBorderView = new ClipImageBorderView(context);

        android.view.ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        this.addView(mZoomImageView, lp);
        this.addView(mClipImageBorderView, lp);

        // dp转px
        mHorizontalPadding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
                        .getDisplayMetrics());

    }

    public void setImageBitmap(Bitmap bm) {
        mZoomImageView.setImageBitmap(bm);
    }

}
