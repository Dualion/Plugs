package com.dualion.power_strip.power_strip.restapi;

import com.dualion.power_strip.power_strip.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class RestPlug {

    private PlugService service;
    Gson gson;

    public RestPlug(String url, String username, String password)
    {

        User user = new User(username, password);

        ApiRequestInterceptor requestInterceptor = new ApiRequestInterceptor();
        requestInterceptor.setUser(user);

        gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setRequestInterceptor(requestInterceptor)
                .setEndpoint(url)
                //.setClient(new OkClient()) // The default client didn't handle well responses like 401
                .setConverter(new GsonConverter(gson))
                .build();

        service = restAdapter.create(PlugService.class);
    }

    public PlugService getService()
    {
        return service;
    }


}
