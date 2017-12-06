package com.qinqi.debugtoolbox;

import com.qinqi.debugtoolbox.log.Logger;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;

/**
 * Created by qinqi on 15/6/10.
 */
public class NetHelper {
    private static long time;
    private static int TIME_OUT = 5;

    /**
     * 获取数据，用于也是用Observable的方式去处理
     *
     * @param url
     * @return
     */
    public static Observable<String> getData(final String url) {
        try {
            return  Observable.just(getContent(url));
        } catch (IOException e) {
            return Observable.error(e);
        }
    }

    public static Observable<String> postData(final String url, final String data) {
        try {
            return  Observable.just(postContent(url,data));
        } catch (IOException e) {
            return Observable.error(e);
        }
    }

    public static Observable<String> postDataByRequestBody(final String url, final RequestBody body){
        try {
            return  Observable.just(postContentRequestBody(url,body));
        } catch (IOException e) {
            return Observable.error(e);
        }
    }

    /**
     * 只有这里没有使用异步，但是OkHttpClient也是非常的简单明了
     *
     * @param urlString
     * @return
     * @throws IOException
     */
    private static String getContent(String urlString) throws IOException {
        OkHttpClient client = MyApplication.getOkHttpClientInstance();

        Request request = new Request.Builder()
                .url(urlString)
                .build();
        Response response = client.newCall(request).execute();
        String result = response.body().string();
//        Ln.d("NetHelper:getContent:Response:" + result);
        return result;
    }

    private static String postContentRequestBody(String urlString, final RequestBody body) throws IOException {
        OkHttpClient client = MyApplication.getOkHttpClientInstance();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Request request = new Request.Builder()
                .url(urlString)
                .addHeader("Content-Type","application/json")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        String result = response.body().string();
        Logger.d("NetHelper","postContentRequestBody:Response:" + result);
        return result;
    }

    private static String postContent(String urlString, String data) throws IOException {
        OkHttpClient client = MyApplication.getOkHttpClientInstance();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);
        Request request = new Request.Builder()
                .url(urlString)
                .addHeader("Content-Type","application/json")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        String result = response.body().string();
//        Ln.d("NetHelper:getContent:Response:" + result);
        return result;
    }


}
