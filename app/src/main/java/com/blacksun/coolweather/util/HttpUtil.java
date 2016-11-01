package com.blacksun.coolweather.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/10/31 0031.
 */

public class HttpUtil {

    public interface HttpCallbackListener {

        void onfinish(String response);

        void onError(Exception e);
    }

    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {

                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    if (listener != null) {
                        // 回调onFinish()方法
                        listener.onfinish(response.toString());
                    }
                } catch (IOException e) {

                    e.printStackTrace();
                    if (listener != null) {
                        // 回调onError()方法
                        listener.onError(e);
                    }
                } finally {

                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
