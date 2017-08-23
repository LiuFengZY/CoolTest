package com.lenovo.cooltest.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
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
        public void onSuccess(T response);
        public void onError(T error);
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

    public static void doHttpRequest(HTTP_REQUEST_METHOD method, String url, final HttpCallback<String> callback, final boolean isWithData) {
        switch(method) {
            case HTTP_GET:
                break;
            case HTTP_POST:
                HttpThreadPoolUtils.execute(new HttpRequestPostRunnable(null, callback, url, false));
                break;
            default:
                break;
        }

    }

    /**
     * Http Get Runnable.
     */
    static class HttpRequestGetRunnable implements Runnable {

        @Override
        public void run() {

        }
    }
    /**
     * Http Post Runnable.
     */
    static class HttpRequestPostRunnable implements Runnable {

        private Context mContext = null;
        private HttpCallback mCallback = null;
        private String mUrl = "";
        private boolean isShowDialog = false;

        public HttpRequestPostRunnable(final Context context,
                                   final HttpCallback callBack, final String url,
                                   final boolean isShowDialog) {
            this.mContext = context;
            this.mCallback = callBack;
            this.mUrl = url;
            this.isShowDialog = isShowDialog;
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

            HttpURLConnection urlConnection = null;
            InputStream is = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setReadTimeout(READ_TIME_OUT);
                urlConnection.setConnectTimeout(CONNECTE_TIME_OUT);
                urlConnection.setRequestMethod("POST");
                urlConnection.setUseCaches(false);
                urlConnection.setRequestProperty("connection", "close");
                urlConnection.connect();
                int code = urlConnection.getResponseCode();
                if (code >= 200 && code < 400) {
                    is = urlConnection.getInputStream();
                    byte sRead[] = CommonUtils.read(is);
                    String strRead = new String(sRead, ENCODE);
                    System.out.println("liufeng http post, read111 : " + "len..." + strRead.length() + strRead);
                    mCallback.onSuccess(strRead);
                } else {
                    is = urlConnection.getErrorStream();
                    byte sRead[] = CommonUtils.read(is);
                    String responseError = new String(sRead, ENCODE);
                    mCallback.onError(responseError);
                }
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                mCallback.onError(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                mCallback.onError(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}
