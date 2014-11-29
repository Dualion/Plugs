package com.dualion.power_strip.power_strip.restapi;

import android.util.Base64;

import com.dualion.power_strip.power_strip.model.User;

import retrofit.RequestInterceptor;

public class ApiRequestInterceptor implements RequestInterceptor {

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
