package dualion.com.plugs.restapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dualion.com.plugs.model.Plug;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class RestPlug {

    private static final String BASE_URL = "http://lluiscanals.noip.me:8082";
    private PlugService service;
    Gson gson;

    public RestPlug()
    {
        gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setConverter(new GsonConverter(gson))
                .build();

        service = restAdapter.create(PlugService.class);
    }

    public PlugService getService()
    {
        return service;
    }


}
