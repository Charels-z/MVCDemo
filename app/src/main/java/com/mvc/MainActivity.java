package com.mvc;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mvc.bean.ImageBean;
import com.mvc.callback.Callback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Callback {

    private ImageView imageView;
    private final static String PATH = "https://s1.ax1x.com/2020/06/09/t58kLR.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.iv_image);
        findViewById(R.id.bt_get_image).setOnClickListener(this);

        // 内存泄漏
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(50000);
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        ImageBean imageBean = new ImageBean();
        imageBean.setRequestPath(PATH);
        new ImageDownloader().down(this, imageBean);
    }

    /**
     * @param resultCode 请求结果返回标识码
     * @param imageBean  Model层数据中bitmap对象（用于C层刷新V）
     */
    @Override
    public void callback(final int resultCode, final ImageBean imageBean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (resultCode) {
                    case ImageDownloader.SUCCESS: // 成功
                        imageView.setImageBitmap(imageBean.getBitmap());
                        break;

                    case ImageDownloader.ERROR: // 失败
                        Toast.makeText(MainActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}