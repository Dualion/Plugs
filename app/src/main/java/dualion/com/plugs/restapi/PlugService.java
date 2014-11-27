package dualion.com.plugs.restapi;

import dualion.com.plugs.model.PlugsList;
import retrofit.Callback;
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
    @PUT("/api/v1.0/pins/{id}")
    public void setPlug(@Path("id") Integer id, Callback<PlugsList> callback);


}
