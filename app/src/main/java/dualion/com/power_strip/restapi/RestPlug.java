package dualion.com.power_strip.restapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class RestPlug {

    private PlugService service;
    Gson gson;

    public RestPlug(String url)
    {
        gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(url)
                .setConverter(new GsonConverter(gson))
                .build();

        service = restAdapter.create(PlugService.class);
    }

    public PlugService getService()
    {
        return service;
    }


}
