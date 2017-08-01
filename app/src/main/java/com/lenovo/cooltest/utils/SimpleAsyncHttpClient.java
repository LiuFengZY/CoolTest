package com.lenovo.cooltest.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Map;

/**
 * Created by liufeng23 on 2017/7/31.
 */

public class SimpleAsyncHttpClient {
    private static final String TAG = SimpleAsyncHttpClient.class.getSimpleName();

    private static int READ_TIME_OUT = 10 * 1000;

    private static int CONNECTE_TIME_OUT = 10 * 1000;

    private static final String ENCODE = "UTF-8";

    public interface HttpCallback <T> {
        public  void onSuccess(T response);
        public  void onError(T error);
    }

    public enum HTTP_REQUEST_METHOD {
        HTTP_GET,
        HTTP_POST,
        HTTP_PUT
    };

    public String makeHttpGetData(String url, final Map<String, String> params) {
        StringBuffer buffer = new StringBuffer(url);
        buffer.append("?");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            buffer.append(entry.getKey())
                    .append("=")
                    .append(Encoder.encode(entry.getValue()))
                    .append("&");

        }
        return buffer.toString();
    }

    public static void doRequestGet(String url, final HttpCallback<String> callback, final boolean isWithData) {
        HttpThreadPoolUtils.execute(new HttpRequestRunnable(0,null,null, callback,
                url, false));
    }

    static class HttpRequestRunnable implements Runnable {

        final int mSendType;
        private Map<String, String> mMap = null;
        private Context mContext = null;
        private HttpCallback mCallback = null;
        private String mUrl = "";
        private boolean isShowDialog = false;

        public HttpRequestRunnable(final int sendType, final Map<String, String> map, final Context context,
                                   final HttpCallback callBack, final String url,
                                   final boolean isShowDialog) {
            this.mSendType = sendType;
            this.mMap = map;
            this.mContext = context;
            this.mCallback = callBack;
            this.mUrl = url;
            this.isShowDialog = isShowDialog;
        }

        private void doHttpGet(String url, final HttpCallback callBack) {
            System.out.println("liufeng, run....do get");
        }
        @Override
        public void run() {
            String data = null;
            URL url = null;
            try {
                url = new URL(this.mUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                mCallback.onError(e.getMessage());
                return;
            }
            BufferedReader bufferedReader  = null;
            StringBuffer response = new StringBuffer();
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setReadTimeout(READ_TIME_OUT);
                urlConnection.setConnectTimeout(CONNECTE_TIME_OUT);
                urlConnection.setRequestMethod("GET");
                urlConnection.setUseCaches(false);
                urlConnection.setRequestProperty("connection", "close");
                urlConnection.connect();
                int code = urlConnection.getResponseCode();
                if (code >= 200 && code < 400) {
                    bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),ENCODE));
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }
                    System.out.println("on, liufeng, success");
                    mCallback.onSuccess(response.toString());
                } else {
                    bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream(),ENCODE));
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }
                    mCallback.onError(response.toString());
                }
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                mCallback.onError(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                mCallback.onError(e.getMessage());
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}
