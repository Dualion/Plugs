package com.dualion.power_strip.restapi;

import android.util.Base64;

import com.dualion.power_strip.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class RestPlug {

    private final PlugService service;

    public RestPlug(String url, String username, String password)
    {

        User user = new User(username, password);

        ApiRequestInterceptor requestInterceptor = new ApiRequestInterceptor();
        requestInterceptor.setUser(user);

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setRequestInterceptor(requestInterceptor)
                .setEndpoint(url)
                .setConverter(new GsonConverter(gson))
                .build();

        service = restAdapter.create(PlugService.class);
    }

    public PlugService getService()
    {
        return service;
    }

    private class ApiRequestInterceptor implements RequestInterceptor {

        private User user;

        @Override
        public void intercept(RequestFacade requestFacade) {

            if (user != null) {
                final String authorizationValue = encodeCredentialsForBasicAuthorization();
                requestFacade.addHeader("Authorization", authorizationValue);
            }
        }

        private String encodeCredentialsForBasicAuthorization() {
            final String userAndPassword = user.getUsername() + ":" + user.getPassword();
            return "Basic " + Base64.encodeToString(userAndPassword.getBytes(), Base64.NO_WRAP);
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

    }

}
