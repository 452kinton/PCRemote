package com.kinton.pcremote.utils;

import com.kinton.pcremote.WebApiService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {

    private Retrofit retrofit;

    private NetworkUtils() {

    }

    private static class InstatnceHodler {
        private static NetworkUtils networkUtils = new NetworkUtils();
    }

    public static NetworkUtils getInstance() {
        return InstatnceHodler.networkUtils;
    }

    public void init(String baseUrl) {
        retrofit = new Retrofit.Builder()
                //添加转换器Converter(将 json 转为 JavaBean)：
                .addConverterFactory(GsonConverterFactory.create())
                //创建 Retrofit 的时候添加如下代码：
                .baseUrl("http://"+baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public WebApiService getWebApiService(){
        if(retrofit!=null){
            return retrofit.create(WebApiService.class);
        }
        return null;
    }
}
