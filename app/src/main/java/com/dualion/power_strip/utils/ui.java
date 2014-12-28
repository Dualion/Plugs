package com.dualion.power_strip.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class ui {

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void toggleView(final View mainView, final View progressView, int shortAnimTime, final boolean show){
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            mainView.setVisibility(show ? View.GONE : View.VISIBLE);
            mainView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mainView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mainView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public static void showView( final View mainView, int shortAnimTime, final boolean show){
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {

            if (show){
                mainView.setVisibility(View.VISIBLE);
                mainView.animate()
                        .setDuration(shortAnimTime)
                        .scaleY(1)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mainView.setVisibility(View.VISIBLE);
                            }
                        });
            } else {
                mainView.animate()
                        .setDuration(shortAnimTime)
                        .scaleY(0)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mainView.setVisibility(View.GONE);
                            }
                        });
            }

            /*mainView.setVisibility(show ? View.VISIBLE : View.GONE);
            mainView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mainView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });*/
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mainView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

}
