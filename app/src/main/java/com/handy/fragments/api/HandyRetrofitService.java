package com.handy.fragments.api;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface HandyRetrofitService
{
    String JOBS_PATH = "/jobs/";

    @GET(JOBS_PATH + "{id}")
    void getBookingDetails(@Path("id") String bookingId,
                           @Query("type") String type,
                           HandyRetrofitCallback cb);


    @GET("/radio1/playlist.json")
    void getRadioPlaylist(HandyRetrofitCallback callback);
}
