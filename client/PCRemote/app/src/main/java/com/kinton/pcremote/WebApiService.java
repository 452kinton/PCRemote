package com.kinton.pcremote;

import com.kinton.pcremote.enitity.CallBack;


import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface WebApiService {

    @FormUrlEncoded
    @POST("mouse_move")
    Observable<CallBack> sendMoveEvent(@Field("angle")double angle,@Field("radio")double radio);

    @FormUrlEncoded
    @POST("mouse_click")
    Observable<CallBack> sendClickEvent(@Field("type")int type);

    @POST("mouse_down")
    Observable<CallBack> sendDownEvent();

    @POST("mouse_up")
    Observable<CallBack> sendUpEvent();

    @GET("check_server")
    Observable<CallBack> checkServer();

    @POST("heart_beats")
    Observable<CallBack> sendHeartBeats();
}
