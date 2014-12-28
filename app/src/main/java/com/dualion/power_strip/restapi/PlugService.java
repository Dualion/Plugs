package com.dualion.power_strip.restapi;

import com.dualion.power_strip.model.PlugsList;
import com.dualion.power_strip.model.Scheduler;
import com.dualion.power_strip.model.SchedulerDiario;
import com.dualion.power_strip.model.SchedulerSemanal;
import com.google.common.net.MediaType;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface PlugService {

    // "/api/v1.0/pins"
    @Headers("Content-Type: form-urlencoded; charset=utf-8")
    @GET("/api/v1.0/pins")
    public void getAllPlugs(Callback<PlugsList> callback);

    // "/api/v1.0/pins/{id}"
    @Headers("Content-Type: form-urlencoded; charset=utf-8")
    @GET("/api/v1.0/pins/{id}")
    public void getPlugFromId(@Path("id") Integer id, Callback<PlugsList> callback);

    // "/api/v1.0/pins/{id}"
    @PUT("/api/v1.0/pins/{id}")
    public void toggleStatePlugFromId(@Path("id") Integer id, Callback<PlugsList> callback);

    // "/api/v1.0/pins/{id}/{pin_name}"
    @PUT("/api/v1.0/pins/{id}/{pin_name}")
    public void SetComponentPlugFromId(@Path("id") Integer id, @Path("pin_name") String component, Callback<PlugsList> callback);

    @PUT("/api/v1.0/pins/scheduler/{id}")
    public void SetSchedulerDiarioFromId(@Body SchedulerDiario body, @Path("id") Integer id, Callback<PlugsList> callback);

    @PUT("/api/v1.0/pins/scheduler/{id}")
    public void SetSchedulerSemanalFromId(@Body SchedulerSemanal body, @Path("id") Integer id, Callback<PlugsList> callback);

    @PUT("/api/v1.0/pins/scheduler/{id}")
    public void SetSchedulerFromId(@Body Scheduler body, @Path("id") Integer id, Callback<PlugsList> callback);

}
