package com.surge.common.core.utils.http;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 可以单例模式调用 HttpClient.getInstance().xxx 方式
 * 也可以新创建对象
 * @lichunqing
 */
public class HttpClient {

    private static final Logger log = LoggerFactory.getLogger(HttpClient.class);
    private OkHttpClient client;
    private static volatile HttpClient instance;
    private HttpClient(){}
    public HttpClient(OkHttpClient client) {
        this.client = client;
    }

    public static HttpClient getInstance() {
        if (instance == null) {
            synchronized (HttpClient.class) {
                if (instance == null) {
                    instance = new HttpClient();
                    OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();

//                    httpBuilder.addInterceptor(new HttpLogInterceptor());   // 拦截器打印日志

                    instance.client = httpBuilder
                            .connectTimeout(8, TimeUnit.SECONDS)   //三次握手 + SSL建立耗时
                            .readTimeout(20, TimeUnit.SECONDS)      //source读取耗时，读取数据
                            .writeTimeout(15, TimeUnit.SECONDS)     //sink写入耗时，发送数据
                            .build();
                }
            }
        }
        return instance;
    }

    public String get(String url, Map<String,String> params) {
        return this.get(url, null, params);
    }

    public String get(String url, Map<String, String> headers, Map<String,String> params) {
        Headers.Builder hb = new Headers.Builder();
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                hb.add(entry.getKey(), entry.getValue());
            }
        }
        String newUrl;
        if(params != null && !params.isEmpty()){
            newUrl = buildUrlWithParams(url, params);
        }else {
            newUrl = url;
        }
        Request request = new Request.Builder()
                .headers(hb.build())
                .url(newUrl)
                .build();
        return getResponse(request);
    }

    public String postJson(String url, Map<String, String> headers, String json){
        return this.postRawData(url, headers, "application/json", json);
    }

    public String postXml(String url, Map<String, String> headers, String json){
        return this.postRawData(url, headers, "application/xml", json);
    }

    public String postPlainText(String url, Map<String, String> headers, String json){
        return this.postRawData(url, headers, "text/plain", json);
    }

    public String postRawData(String url, Map<String, String> headers, String contentType, String text){
        Headers.Builder hb = new Headers.Builder();
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                hb.add(entry.getKey(), entry.getValue());
            }
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse(contentType), text == null ? "" : text);
        Request request = new Request.Builder()
                .url(url)
                .headers(hb.build())
                .method("POST", requestBody)
                .build();
        return getResponse(request);
    }

    public String postFormUrlencoded(String url, HashMap<String, String> headers, HashMap<String, String> params){
        Headers.Builder hb = new Headers.Builder();
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                hb.add(entry.getKey(), entry.getValue());
            }
        }

        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }

        Request request = new Request.Builder()
                .url(url)
                .headers(hb.build())
                .post(builder.build())
                .build();

        return getResponse(request);
    }

    public String postFormData(String url,
                               HashMap<String, String> headers,
                               HashMap<String, String> fields,
                               HttpFormDataFile formDataFile) {
        List<HttpFormDataFile> formDataFiles = new ArrayList<>(1);
        formDataFiles.add(formDataFile);
        return this.postFormData(url, headers, fields, formDataFiles);
    }

    public String postFormData(String url,
                               HashMap<String, String> headers,
                               HashMap<String, String> fields,
                               List<HttpFormDataFile> formDataFiles) {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        for(HttpFormDataFile item : formDataFiles) {
            if(item.getFileData() != null && item.getFileData().length > 0) {
                builder.addFormDataPart(item.getFieldName(), item.getFileName(),
                        RequestBody.create(MediaType.parse(item.getContentType()), item.getFileData()));
            }
            if(item.getFile() != null) {
                builder.addFormDataPart(item.getFieldName(), item.getFileName(),
                        RequestBody.create(MediaType.parse(item.getContentType()), item.getFile()));
            }
        }

        if (fields != null && !fields.isEmpty()) {
            for (Map.Entry<String, String> entry : fields.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }

        Headers.Builder hb = new Headers.Builder();
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                hb.add(entry.getKey(), entry.getValue());
            }
        }

        Request request = new Request.Builder()
                .url(url)
                .headers(hb.build())
                .post(builder.build())
                .build();

        return getResponse(request);
    }

    public String postBinary(String url, HashMap<String, String> headers, byte[] data) {
        RequestBody body;
        if (data.length == 0) {
            System.err.println("The send data is empty.");
            return null;
        } else {
            body = RequestBody.create(MediaType.parse("application/octet-stream"), data);
        }

        Headers.Builder hb = new Headers.Builder();
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                hb.add(entry.getKey(), entry.getValue());
            }
        }

        Request request = new Request.Builder()
                .url(url)
                .headers(hb.build())
                .post(body)
                .build();

        return getResponse(request);
    }

    public String postBinary(String url, HashMap<String, String> headers, String filepath) {
        RequestBody body;
        File file = new File(filepath);
        if (!file.isFile()) {
            log.error("The filePath is not a file: " + filepath);
            return null;
        } else {
            body = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        }
        Headers.Builder hb = new Headers.Builder();
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                hb.add(entry.getKey(), entry.getValue());
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .headers(hb.build())
                .post(body)
                .build();

        return getResponse(request);
    }


    public String buildUrlWithParams(String baseUrl, Map<String, String> params) {
        StringBuilder sb = new StringBuilder(baseUrl);
        if (!params.isEmpty()) {
            sb.append("?");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append(key).append("=").append(value).append("&");
            }
            sb.deleteCharAt(sb.length() - 1); // 删除最后一个多余的 "&"
        }
        return sb.toString();
    }

    private String getResponse(Request q) {
        Response response = null;
        String result = null;
        try {
            response = client.newCall(q).execute();
            result = response.body().string();
            response.close();
        } catch (Exception e) {
            log.error("http client call exception: {}", e);
        }
        return result;
    }

    private byte[] getResponseBytes(Request q) {
        Response response = null;
        byte[] result = null;
        try {
            response = client.newCall(q).execute();
            response.body().byteStream();
            result = response.body().bytes();
        } catch (Exception e) {
            log.error("ok http client call exception: {}", e);
        } finally {
            response.close();
        }
        return result;
    }



}
