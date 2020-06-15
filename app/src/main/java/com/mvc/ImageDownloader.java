package com.mvc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.mvc.bean.ImageBean;
import com.mvc.callback.Callback;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDownloader {
    // 成功
    public static final int SUCCESS = 200;

    // 失败
    public static final int ERROR = 404;

    public void down(Callback callback, ImageBean imageBean) {
        new Thread(new DownLoader(callback,imageBean)).start();
    }

    static final class DownLoader implements Runnable{
        private final Callback callback;
        private final ImageBean imageBean;

        public DownLoader(Callback callback, ImageBean imageBean) {
            this.callback = callback;
            this.imageBean = imageBean;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(imageBean.getRequestPath());
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("GET");

                if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    Log.e("ImageDownloader","HttpURLConnection.HTTP_OK");
                    InputStream inputStream = httpURLConnection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    showUi(SUCCESS,bitmap);
                } else {
                    Log.e("ImageDownloader","HttpURLConnection.ERROR");
                    showUi(ERROR, null);
                }
            } catch (Exception e) {
                Log.e("ImageDownloader","HttpURLConnection.ERROR");
                e.printStackTrace();
                showUi(ERROR, null);
            }
        }
        private void showUi(int resultCode, Bitmap bitmap) {
            if (callback != null) {
                imageBean.setBitmap(bitmap);
                callback.callback(resultCode, imageBean);
            }
        }
    }
}
