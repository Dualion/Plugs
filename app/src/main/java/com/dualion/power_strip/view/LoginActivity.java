package com.dualion.power_strip.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.dualion.power_strip.R;
import com.dualion.power_strip.data.SharedData;
import com.dualion.power_strip.model.ui.BaseActivity;
import com.dualion.power_strip.model.PlugsList;
import com.dualion.power_strip.model.User;
import com.dualion.power_strip.restapi.PlugService;
import com.dualion.power_strip.restapi.RestPlug;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.dualion.power_strip.utils.ui.showProgress;


public class LoginActivity extends BaseActivity {

    private AutoCompleteTextView userView;
    private EditText passwordView;
    private EditText urlApiView;
    private CheckBox savePass;
    private View progressView;
    private View loginFormView;

    @Inject
    SharedData settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // Set up the login form.
        urlApiView = (EditText) findViewById(R.id.urlApi);
        urlApiView.setText(settings.getURI());
        urlApiView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    View focusView;
                    focusView = userView;
                    focusView.requestFocus();
                    return true;
                }
                return false;
            }
        });

        userView = (AutoCompleteTextView) findViewById(R.id.user);
        userView.setText(settings.getUser());
        userView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    View focusView;
                    focusView = passwordView;
                    focusView.requestFocus();
                    return true;
                }
                return false;
            }
        });

        passwordView = (EditText) findViewById(R.id.password);
        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        savePass = (CheckBox) findViewById(R.id.savePass);

        String pass = settings.getPass();
        if (!pass.isEmpty()) {
            passwordView.setText(pass);
            savePass.setChecked(true);
        }

        Button userSignInButton = (Button) findViewById(R.id.user_sign_in_button);
        userSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        loginFormView = findViewById(R.id.login_form);
        progressView = findViewById(R.id.login_progress);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_quit:
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void attemptLogin() {
        // Reset errors.
        userView.setError(null);
        passwordView.setError(null);

        // Store values at the time of the login attempt.
        String urlApi = urlApiView.getText().toString();
        String userName = userView.getText().toString();
        String password = passwordView.getText().toString();
        User user = new User(userName, password);

        boolean cancel = false;
        View focusView = null;

        //Check for valid url
        if (!Patterns.WEB_URL.matcher(urlApi).matches()){
            urlApiView.setError(getString(R.string.error_invalid_url));
            focusView = urlApiView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!user.isPasswordValid()) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        // Check for a valid username.
        if (!user.isUsernameValid()) {
            userView.setError(getString(R.string.error_invalid_username));
            focusView = userView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(loginFormView,
                    progressView,
                    getResources().getInteger(android.R.integer.config_shortAnimTime),
                    true);
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(userView.getWindowToken(), 0);
            login(urlApi, user);
        }
    }

    private void login(final String urlApi, final User user) {

        final RestPlug restProduct = new RestPlug(urlApi, user.getUsername(), user.getPassword());
        PlugService plugService = restProduct.getService();
        plugService.getAllPlugs(new Callback<PlugsList>() {
            @Override
            public void success(PlugsList plugsList, Response response) {

                settings.setURI(urlApi);
                settings.setUser(user.getUsername());

                if (savePass.isChecked()) {
                    settings.setPass(user.getPassword());
                    settings.setCurrentPass(user.getPassword());
                } else {
                    settings.setPass("");
                    settings.setCurrentPass(user.getPassword());
                }

                Intent myIntent = new Intent(loginFormView.getContext(), MainActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                showProgress(loginFormView,
                        progressView,
                        getResources().getInteger(android.R.integer.config_shortAnimTime),
                        false);
                try {
                    if (retrofitError.getResponse().getStatus() == 403) {
                        passwordView.setError(getString(R.string.error_incorrect_password));
                        passwordView.requestFocus();
                    } else {
                        urlApiView.setError(getString(R.string.error_incorrect_data));
                    }
                } catch(Exception ex)
                {
                    urlApiView.setError(getString(R.string.error_invalid_url));
                }
            }
        });
    }
}
