package com.qieji.mobile.still.stage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.qieji.mobile.still.R;

import java.io.File;

/**
 * Created by August on 2015/2/26.
 */
public class StageActivity extends Activity {

    public static final String ACTIVITY_TAG = "StageActivity";

    protected ImageView sourceImgV;
    protected Bitmap sourceImgBM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage);

        // init
        sourceImgV = (ImageView) findViewById(R.id.source_img_v);

        Intent i = getIntent();
        Uri uri = i.getData();
        // Toast.makeText(this, uri + "", Toast.LENGTH_LONG).show();
        initSourceImgView(uri);
    }

    protected void initSourceImgView(Uri uri) {
        if (uri == null) {
            return;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 5;

        String imgPath = uri.getPath();
        File file = new File(imgPath);
        if (file.exists()) {
            sourceImgBM =  BitmapFactory.decodeFile(imgPath, options);
            if (sourceImgBM != null) {
                int width = sourceImgBM.getWidth();
                int height = sourceImgBM.getHeight();
                // 强制横向
                if (width < height) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    sourceImgBM = Bitmap.createBitmap(sourceImgBM, 0, 0,
                            width, height, matrix, true);
                }
                sourceImgV.setImageBitmap(sourceImgBM);
            }
        }
    }

}
