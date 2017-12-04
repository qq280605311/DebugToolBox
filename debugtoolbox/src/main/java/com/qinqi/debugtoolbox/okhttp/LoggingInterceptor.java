package com.qinqi.debugtoolbox.okhttp;

import android.os.Handler;

import com.qinqi.debugtoolbox.task.InsertDBToNetAsyncTask;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.RealResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;

/**
 * Created by qinqi on 2016/11/11.
 */

public class LoggingInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private Handler handler = new Handler();

    @Override
    public Response intercept(Chain chain) throws IOException {
        String request_url = "";
        int elapsed_time = 0;
        String method = "";
        String request_header = "";
        String request_body = "";
        String request_media_type = "";
        int code = 0;
        String message = "";
        String protocol_str = "";
        String response_body = "";
        String response_header = "";
        String response_media_type = "";
        long response_content_length = 0L;

        Request request = chain.request();

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;
        request_body = hasRequestBody ? requestBodyByRequest(request) : "";

        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;

        request_url = request.url().toString();
        method = request.method();
        protocol_str = protocol.toString().toUpperCase();

        if (hasRequestBody) {
            if (requestBody.contentType() != null) {
                request_media_type = requestBody.contentType().toString();
            }
        }

        Headers requestHeaders = request.headers();
        request_header = requestHeaders.toString();

        long startNs = System.nanoTime();
        Response response = chain.proceed(request);
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        elapsed_time = (int) tookMs;

        ResponseBody responseBody = response.body();
        Headers responseHeaders = response.headers();

        String contentEncoding = response.headers().get("Content-Encoding");
        if (contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip")) {
            response = unzip(response);
            responseBody = response.body();
            responseHeaders = response.headers();
        }

        response_header = responseHeaders.toString();
        long contentLength = responseBody.contentLength();
        response_content_length = contentLength;

        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();

        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            try {
                charset = contentType.charset(UTF8);
                response_media_type = contentType.toString();
            } catch (UnsupportedCharsetException e) {
                //Couldn't decode the response body; charset is likely malformed.
                return response;
            }
        }

        if (contentLength != 0) {
            response_body = buffer.clone().readString(charset);
        }

        if(contentLength == -1){
            response_content_length = response_body.length();
        }

        code = response.code();
        message = response.message();

        final NetTraffics traffics = new NetTraffics();
        traffics.setTime(System.currentTimeMillis());
        traffics.setUrl(request_url);
        traffics.setElapsedTime(elapsed_time);
        traffics.setMethod(method);
        traffics.setRequestHeader(request_header);
        traffics.setRequestBody(request_body);
        traffics.setRequestMediaType(request_media_type);

        traffics.setCode(code);
        traffics.setMessage(message);
        traffics.setProtocol(protocol_str);
        traffics.setResponseBody(response_body);
        traffics.setResponseHeader(response_header);
        traffics.setResponseMediaType(response_media_type);
        traffics.setResponseContentLength(response_content_length);

        InsertDBToNetAsyncTask task = new InsertDBToNetAsyncTask();
        task.execute(traffics);

        return response;
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    private String requestBodyByRequest(Request request) {
        try {
            Request copy = request.newBuilder().build();
            Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (IOException e) {
            e.printStackTrace();
            return "did not work";
        }
    }

    private static Response unzip(final Response response) throws IOException {
        if (response.body() == null) {
            return response;
        }

        //response.body().string；
        //这句日志加上，底下的解压缩就会报错，应该是因为string()方法会将流关闭。

        GzipSource responseBody = new GzipSource(response.body().source());
        Headers strippedHeaders = response.headers().newBuilder()
                .removeAll("Content-Encoding")
                .removeAll("Content-Length")
                .build();
        return response.newBuilder()
                .headers(strippedHeaders)
                .body(new RealResponseBody(strippedHeaders, Okio.buffer(responseBody)))
                .build();
    }
}
