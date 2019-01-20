package com.example.administrator.animationdemo;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
//    private ProgressCircleView progressCircleview;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        progressCircleview = (ProgressCircleView) findViewById(R.id.progress_circleview);
//        progressCircleview.setProgress(100);
        new MyTask().execute("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1547987073732&di=780cada4f115a5c431c759154e7c4feb&imgtype=0&src=http%3A%2F%2Fbrup.shengri.cn%2Fgoods%2F2017%2F02%2F13182518_b36033172439a36a4aa534d0336d86a1.jpg");

    }
    class MyTask extends AsyncTask<String, Integer, Bitmap> {
        // 下载前的操作(主)
        @Override
        protected void onPreExecute() {
            // 下载前需要显示进度条
//            progressCircleview.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        // 下载图片(子)
        @Override
        protected Bitmap doInBackground(String... params) {
            URL url = null;
            try {
                url = new URL(params[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5 * 1000);
                //设置从主机读取数据超时
                connection.setReadTimeout(5 * 1000);
                // 设置是否使用缓存  默认是true
                connection.setUseCaches(true);
                // 设置为Post请求
                connection.setRequestMethod("GET");
                //connection设置请求头信息
                //设置请求中的媒体类型信息。
                connection.setRequestProperty("Content-Type", "application/json");
                //设置客户端与服务连接类型
                connection.addRequestProperty("Connection", "Keep-Alive");
                // 开始连接
                connection.connect();
                if(connection.getResponseCode() == 200){
                    InputStream is = connection.getInputStream();
                    long length = connection.getContentLength();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();// 字节数组输出流(专用于保存网络上下载的数据)
                    byte[] buffer = new byte[1024]; // 输入缓冲器
                    int ret, received = 0; // 接收到多少
                    while (true) {
                        ret = is.read(buffer); // 真正读到多少字节(最后一次读到的值不一定是1024)
                        if (ret < 0) {
                            break;
                        }
                        Thread.sleep(100);
                        // 把buffer的数据先保存到一个靠谱的地方
                        baos.write(buffer, 0, ret);
                        received += ret; // 更新当前接收到的长度
                        int progress = (int) (100 * received / length); // 计算进度值
                        publishProgress(progress); // 发布最新的进度值
                    }

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        // 发布更新的操作(主)
        @Override
        protected void onProgressUpdate(Integer... values) {
//            progressCircleview.setProgress(values[0]);
            super.onProgressUpdate(values);
        }

        // 下载后的操作(主)
        @Override
        protected void onPostExecute(Bitmap result) {

            // 下载后取消进度条
//            progressCircleview.setProgress(0);
//            progressCircleview.setVisibility(View.GONE);
            super.onPostExecute(result);
            Toast.makeText(MainActivity.this,"下载成功",Toast.LENGTH_SHORT).show();
        }

    }
}
