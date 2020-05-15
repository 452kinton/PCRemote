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
    @POST("key_click")
    Observable<CallBack> sendKeyClick(@Field("id")int id);

    @FormUrlEncoded
    @POST("mouse_btn_touch")
    Observable<CallBack> sendMouseBtnTouchEvent(@Field("type")int type,@Field("state") int state);

    @POST("mouse_down")
    Observable<CallBack> sendDownEvent();

    @POST("mouse_up")
    Observable<CallBack> sendUpEvent();

    @GET("check_server")
    Observable<CallBack> checkServer();

    @POST("heart_beats")
    Observable<CallBack> sendHeartBeats();
}
