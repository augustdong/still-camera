package com.qieji.mobile.still;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.qieji.mobile.still.stage.StageActivity;

import java.io.File;

public class MainActivity extends Activity implements OnClickListener {

    public static final String ACTIVITY_TAG = "MainActivity";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    Button btnTest;
    File captureImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTest =  (Button) findViewById(R.id.btn_test);
        btnTest.setOnClickListener(this);
        captureImg = new File(Environment.getExternalStorageDirectory(), "capture_img.jpg");
    }

    protected void onBtnTestClick() {
        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(captureImg));
        startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    protected void onStartStageActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        // 如果照相成功，则打开新的stageActivity处理
        Intent i = new Intent(MainActivity.this, StageActivity.class);
        i.setData(uri);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_test:
                onBtnTestClick();
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            Log.w(ACTIVITY_TAG, "CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE");
            if (resultCode == RESULT_OK) {
                Uri uri = null;
                if (data !=  null && data.getData() != null) {
                    uri = data.getData();
                }
                if (uri == null) {
                    uri = Uri.fromFile(captureImg);
                }
                if (uri == null) {
                    return;
                }
                onStartStageActivity(uri);
                return;
            }
            if (resultCode == RESULT_CANCELED) {
                return;
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        // todo
        // destroy时，删除临时资源
        super.onDestroy();
    }

}
