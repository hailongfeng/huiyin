package com.ewizarpos.okhttp;

import android.os.Build;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/5/31.
 */
public class OKHttpUtil {

    private static OKHttpUtil mInstance;
    private OkHttpClient mOkHttpClient;
//    private Handler mDelivery;
    public final static int CONNECT_TIMEOUT =30;
    public final static int READ_TIMEOUT=120;
    public final static int WRITE_TIMEOUT=30;

    private static final String TAG = "OkHttpClientManager";

    private OKHttpUtil() {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .cookieJar(CookieJar.NO_COOKIES)
                .build();

//        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
//        mOkHttpClient.setConnectTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
//        mOkHttpClient.setReadTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
//        mOkHttpClient.setWriteTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
//        mOkHttpClient.setRetryOnConnectionFailure(false);
//        mDelivery = new Handler(Looper.getMainLooper());
    }

    public static OKHttpUtil getInstance() {
        if (mInstance == null) {
            synchronized (OKHttpUtil.class) {
                if (mInstance == null) {
                    mInstance = new OKHttpUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 异步post请求
     *
     * @param heads       请求头
     * @param bodyContent 请求内容 body中的
     * @param callback
     */
    public void post(String url, Map<String, String> heads, String bodyContent, Callback callback) {
        Request request = buildPostRequest(url, heads, bodyContent);
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    /**
     *
     * @param url 请求地址
     * @param heads 请求头
     * @param params 请求参数
     * @param callback 回调函数
     */
    public void post(String url, Map<String, String> heads, Map<String, String> params, Callback callback) {
        Request request = buildPostRequest(url, heads, params);
        mOkHttpClient.newCall(request).enqueue(callback);
    }


    //同步
    public Response postSync(String url, Map<String, String> heads, String bodyContent) throws Throwable {
        Request request = buildPostRequest(url, heads, bodyContent);
        return mOkHttpClient.newCall(request).execute();
    }
    //同步
    public Response postSync(String url, Map<String, String> heads,  Map<String, String> params) throws Throwable {
        Request request = buildPostRequest(url, heads, params);
        return mOkHttpClient.newCall(request).execute();
    }

    //异步
    public void get(String url, Map<String, String> heads, Callback callback) {
        Headers.Builder builder = mapToHeaderBuilder(heads);
        final Request request = new Request.Builder()
                .url(url)
                .headers(builder.build())
                .build();
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    //同步
    public Response getSync(String url, Map<String, String> heads) throws Throwable {
        Headers.Builder builder = mapToHeaderBuilder(heads);
        final Request request = new Request.Builder()
                .url(url)
                .headers(builder.build())
                .build();
        Call call = mOkHttpClient.newCall(request);
        Response execute = call.execute();
        return execute;
    }

    private Headers.Builder mapToHeaderBuilder(Map<String, String> headers) {
        Headers.Builder builder = new Headers.Builder();
        if (headers != null) {
            Iterator<String> iterator = headers.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = headers.get(key);
                builder.add(key, value);
            }
        }
        if (Build.VERSION.SDK_INT > 13) {
            builder.add("Connection", "close");
//            MyLog.d("head Connection set close");
        }
        return builder;
    }

    private Request buildPostRequest(String url, Map<String, String> headers, String content) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        Headers.Builder builder = mapToHeaderBuilder(headers);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, content);
        Headers headers1 = builder.build();
        return new Request.Builder()
                .url(url)
                .headers(headers1)
                .post(body)
                .build();
    }
    private Request buildPostRequest(String url, Map<String, String> headers, Map<String, String> params) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        if (params == null) {
            params = new HashMap<>();
        }
        Headers.Builder headBuilder = mapToHeaderBuilder(headers);
        RequestBody formBody=null;
        FormBody.Builder builder=  new FormBody.Builder();
        Iterator<String> iterator = params.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = params.get(key);
            builder.add(key, value);
        }
        formBody=  builder.build();

        return new Request.Builder()
                .url(url)
                .headers(headBuilder.build())
                .post(formBody)
                .build();
    }

}
