package com.xin.lv.yang.utils.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.xin.lv.yang.utils.info.HttpInfo;

import org.jsoup.helper.HttpConnection;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * 网络请求相关操作
 */

public class HttpUtils {

    private static HttpUtils httpUtils;
    private OkHttpClient http;

    public static synchronized HttpUtils getInstance() {
        if (httpUtils == null) {
            httpUtils = new HttpUtils();
        }
        return httpUtils;
    }

    private static List<HttpInfo> headList;

    public static synchronized HttpUtils getInstance(List<HttpInfo> headList) {
        HttpUtils.headList = headList;
        if (httpUtils == null) {
            httpUtils = new HttpUtils();
        }
        return httpUtils;
    }

    private final static int CONNECT_TIMEOUT = 60;
    private final static int READ_TIMEOUT = 60;
    private final static int WRITE_TIMEOUT = 60;
    public final static int ERROR = 404;

    public final static int ERROR_BUG = 405;

    private HttpUtils() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        http = builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();

    }


    /**
     * 构建请求地址
     *
     * @return 构建好的url
     */
    private String buildUrl(String url, List<HttpInfo> list) {
        if (list == null) {
            return url;
        }
        StringBuilder buffer = new StringBuilder();
        buffer.append(url);
        int len = list.size();
        for (int i = 0; i < len; i++) {
            if (i == 0) {
                buffer.append("?");
            } else {
                buffer.append("&");
            }
            HttpInfo httpInfo = list.get(i);
            buffer.append(httpInfo.getKey());
            buffer.append("=");
            buffer.append(httpInfo.getValues());
        }
        return buffer.toString();
    }


    /**
     * get请求
     *
     * @param url     url
     * @param list    数据
     * @param what    线程标识
     * @param handler handler
     */
    public void get(String url, List<HttpInfo> list, int what, Handler handler) {
        url = buildUrl(url, list);
        Request get = new Request.Builder().url(url).cacheControl(CacheControl.FORCE_NETWORK).build();
        call(get, what, handler);

    }


    public String getTong(String url, List<HttpInfo> list) {
        url = buildUrl(url, list);
        Request get = new Request.Builder().url(url).cacheControl(CacheControl.FORCE_NETWORK).build();
        Call call = http.newCall(get);
        try {
            Response rr = call.execute();
            if (rr.isSuccessful()) {
                return rr.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";

    }


    public void getWithHead(String url, List<HttpInfo> list, int what, Handler handler) {
        url = buildUrl(url, list);

        Log.i("result", "======" + url);

        Request.Builder builder = new Request.Builder();
        StringBuilder stringBuilder = new StringBuilder();

        for (HttpInfo tt : headList) {
            builder.addHeader(tt.getKey(), tt.getValues());

            stringBuilder.append(tt.getKey() + "=" + tt.getValues() + "----");
        }

        Log.i("result", "head----" + stringBuilder.toString());

        Request get = builder.url(url).cacheControl(CacheControl.FORCE_NETWORK).build();
        call(get, what, handler);

    }


    /**
     * post 请求
     *
     * @param url     请求地址
     * @param list    请求数据
     * @param what    线程标识
     * @param handler handler
     */
    public void post(String url, List<HttpInfo> list, int what, Handler handler) {
        StringBuilder sb = new StringBuilder();
        FormBody.Builder build = new FormBody.Builder();
        for (HttpInfo t : list) {
            build.add(t.getKey(), t.getValues());

            sb.append(t.getKey() + "=" + t.getValues() + "---");

        }

        /// 请求参数
        RequestBody body = build.build();

        Request.Builder builder = new Request.Builder();

        if (headList != null && headList.size() != 0) {
            for (HttpInfo ttt : headList) {
                builder.addHeader(ttt.getKey(), ttt.getValues());

                sb.append("header");
                sb.append(ttt.getKey() + "=" + ttt.getValues() + "---");

            }
        }

        Log.i("result", url + "-----" + sb.toString());

        Request post = builder.post(body).url(url).build();
        call(post, what, handler);

    }


    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    /**
     * post json数据
     *
     * @param url     url
     * @param json    json格式数据
     * @param what    线程标识
     * @param handler handler
     */
    public void postJson(String url, String json, int what, Handler handler) {

        Log.i("result", "url----" + url + "---json----" + json);

        RequestBody postJson = RequestBody.create(JSON, json);
        Request.Builder builder = new Request.Builder();

        Request post = builder.url(url).post(postJson).build();
        call(post, what, handler);

    }


    private static final MediaType XML = MediaType.parse("application/text; charset=utf-8");


    /**
     * 同步post请求
     *
     * @param url   请求地址
     * @param order post 数据
     * @return 返回获取的数据
     */
    public String postXML(String url, String order) {
        RequestBody postJson = RequestBody.create(XML, order);
        Request post = new Request.Builder().url(url).post(postJson).build();
        try {
            Response response = http.newCall(post).execute();
            if (response.isSuccessful()) {
                String str = response.body().string();
                return str;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }


    /**
     * 上传图片，不带进度提示
     *
     * @param url     上传地址
     * @param list    带图片地址的集合
     * @param what    线程标识
     * @param handler handler
     */
    public void postImageNoProgress(String url, List<String> list, int what, Handler handler) {
        MultipartBody.Builder build = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (list != null) {
            for (String t : list) {
                build.addFormDataPart("filename", "upload.png", RequestBody.create(MediaType.parse("image/png"), new File(t)));

            }

            RequestBody body = build.build();
            Request post = new Request.Builder().url(url).post(body).build();
            call(post, what, handler);

        }
    }


    public void postImageOneNoProgress(String url, String path, int what, Handler handler) {
        MultipartBody.Builder build = new MultipartBody.Builder().setType(MultipartBody.FORM);

        File file = new File(path);

        Log.i("result", "文件是否存在=====" + file.exists() + "-------" + file.getName());

        if (file.exists()) {

            build.addFormDataPart("icon", file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
            RequestBody body = build.build();
            Request post = new Request.Builder().url(url).post(body).build();
            call(post, what, handler);
        }

    }


    /**
     * 带进度提示的图片上传 单张上传
     *
     * @param url     上传地址
     * @param path    图片地址
     * @param what    线程标识
     * @param handler handler
     */
    public void postImageWithProgress(String url, String path, int what, Handler handler, ProgressListener progress) {
        MultipartBody.Builder build = new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody requestBody = createCustomRequestBody(MediaType.parse("image/png"), new File(path), progress);
        build.addFormDataPart("filename", "upload.png", requestBody);
        RequestBody body = build.build();
        Request post = new Request.Builder().url(url).post(body).build();
        call(post, what, handler);

    }


    /**
     * 同步上传图片，外部使用AsyncTask
     */
    public void postPic(String url, String path, ProgressListener progress, OnSuccess onSuccess) {
        MultipartBody.Builder build = new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody requestBody = createCustomRequestBody(MediaType.parse("image/png"), new File(path), progress);
        build.addFormDataPart("filename", "upload.png", requestBody);
        RequestBody body = build.build();
        Request post = new Request.Builder().url(url).post(body).build();
        try {
            Response response = http.newCall(post).execute();
            if (response.isSuccessful()) {
                String str = response.body().string();
                onSuccess.onSuccess(str);
            } else if (response.isRedirect()) {
                onSuccess.onFailure();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void postVideoWithProgress(String url, String path, int what, Handler handler, ProgressListener progress) {
        MultipartBody.Builder build = new MultipartBody.Builder().setType(MultipartBody.FORM);
        File file = new File(path);

        Log.i("result", "-------" + file.exists());

        RequestBody requestBody = createCustomRequestBody(MediaType.parse("video/mp4"), file, progress);
        build.addFormDataPart("icon", "upload.mp4", requestBody);
        RequestBody body = build.build();
        Request post = new Request.Builder().url(url).post(body).build();
        call(post, what, handler);
    }


    /**
     * 请求响应
     *
     * @param request 请求方式
     * @param what    线程标识
     * @param handler handler
     */
    private void call(final Request request, final int what, final Handler handler) {

        http.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.i("result", "执行到此，网络连接异常");

                Message message = handler.obtainMessage();
                message.what = ERROR;
                message.obj = "";
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();

                String url = request.url().encodedPath();

                Log.i("result", "执行数据返回--------" + url + "----" + str);

                if (str.startsWith("{")) {
                    Message msg = handler.obtainMessage();
                    msg.what = what;
                    msg.obj = str;
                    handler.sendMessage(msg);

                } else {
                    Message message = handler.obtainMessage();
                    message.what = ERROR_BUG;
                    message.obj = "";
                    handler.sendMessage(message);
                }
            }
        });
    }


    /**
     * 构造进度提示的body
     *
     * @param contentType mediatype 对象
     * @param file        文件对象
     * @param listener    进度监听
     * @return 构造体body
     */
    private static RequestBody createCustomRequestBody(final MediaType contentType, final File file, final ProgressListener listener) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source;
                try {
                    source = Okio.source(file);
                    sink.writeAll(source);
                    Buffer buf = new Buffer();
                    Long remaining = contentLength();
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        listener.onProgress(contentLength(), remaining -= readCount, remaining == 0);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }


    private static final String headerKey = "Authorization";
    private static final String add = "Gouxiang_";


    public void postJsonWithHeader(String url, String json, int what, String header, Handler handler) {

        Log.i("result", "url=====" + url + "  json===" + json + "--token----" + header);

        RequestBody postJson = RequestBody.create(JSON, json);
        Request post = new Request.Builder().url(url).post(postJson).header(headerKey, add + header).build();
        call(post, what, handler);

    }


    /**
     * 同步请求
     *
     * @param url
     * @param json
     * @param header
     * @return
     */
    public String postJsonWithHeaderTong(String url, String json, String header) {

        Log.i("result", "url=====" + url + "  json===" + json);

        RequestBody postJson = RequestBody.create(JSON, json);
        Request post = new Request.Builder().url(url).post(postJson).header(headerKey, add + header).build();
        try {
            Response response = http.newCall(post).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * post 带图片文件
     *
     * @param url      请求地址
     * @param list     参数集合 键值对
     * @param httpInfo 图片
     * @param what     标识符
     * @param handler  handler
     */
    public void postWithFile(String url, List<HttpInfo> list, HttpInfo httpInfo, int what, Handler handler) {

        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        StringBuilder sb = new StringBuilder();
        for (HttpInfo temp : list) {
            /// 添加请求体 键值对
            requestBody.addFormDataPart(temp.getKey(), temp.getValues());

            sb.append(temp.getKey() + "=" + temp.getValues()).append("-----");

        }

        if (httpInfo != null) {

            File file = httpInfo.getFile();

            if (file != null && file.isFile() && file.exists()) {

                requestBody.addFormDataPart(httpInfo.getKey(), file.getName(), RequestBody.create(MediaType.parse("image/png"), file));

            }
        }

        Request.Builder builder = new Request.Builder();

        if (headList != null && headList.size() != 0) {
            for (HttpInfo tt : headList) {
                builder.addHeader(tt.getKey(), tt.getValues());
            }
        }

        Log.i("result", "sb-----sb----" + sb.toString());

        Request request = builder.url(url).post(requestBody.build()).build();

        call(request, what, handler);

    }


    public interface ProgressListener {
        void onProgress(long totalBytes, long remainingBytes, boolean done);
    }


    /**
     * 下载文件
     */
    public void downFile(String url, final String fileName, final OnDownBack onDownBack) {
        Request request = new Request.Builder().url(url).build();
        http.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onDownBack.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;     // 输入流
                FileOutputStream fos = null;  // 输出流

                ResponseBody body = response.body();
                try {
                    if (body != null) {
                        is = body.byteStream();              //获取输入流
                        long total = body.contentLength();    //获取文件大小
                        onDownBack.onStart((int) total);

                        if (is != null) {
                            File file = new File(fileName);   // 设置路径
                            fos = new FileOutputStream(file);
                            byte[] buf = new byte[1024];
                            int ch = -1;
                            int process = 0;
                            while ((ch = is.read(buf)) != -1) {
                                fos.write(buf, 0, ch);
                                process += ch;
                                onDownBack.onProgress(process);

                            }
                        }

                        // 下载完成
                        if (fos != null) {
                            fos.flush();
                            fos.close();
                        }

                        onDownBack.onFinish();
                    } else {
                        onDownBack.onFailure();
                    }
                } catch (Exception e) {
                    onDownBack.onFailure();

                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });

    }


    public interface OnDownBack {
        /**
         * 设置大小
         *
         * @param allProgress
         */
        void onStart(int allProgress);

        /**
         * 下载进度提示
         *
         * @param p
         */
        void onProgress(int p);

        /**
         * 下载完成提示
         */
        void onFinish();

        /**
         * 请求失败
         */
        void onFailure();
    }


    public interface OnSuccess {
        void onSuccess(String str);

        void onFailure();
    }

}
