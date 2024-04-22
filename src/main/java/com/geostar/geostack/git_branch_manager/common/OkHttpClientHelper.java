package com.geostar.geostack.git_branch_manager.common;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class OkHttpClientHelper {

    private final OkHttpClient okHttpClient;

    public OkHttpClientHelper(@Autowired OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public String sendGetRequest(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return Objects.requireNonNull(response.body()).string();
        }
    }


    public String sendPostRequest(String url, String body, String token) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json"), body.getBytes()))
                .header("Authorization", token)
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("请求失败，错误码：" + response);
            }
            return Objects.requireNonNull(response.body()).string();
        }
    }

}
