package com.aru.valuationregister;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.aru.valuationregister.MenuActivities.MainMenuActivity;
import com.aru.valuationregister.Rest.Action;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    private EditText usernameEditText;
    private EditText passwordEditText;

    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = getApplicationContext().
                getSharedPreferences("VALUATION_REGISTER", MODE_PRIVATE);

        Action.initialize(prefs); // Initialize action with global shared preferences

        mProgressDialog = new ProgressDialog(this);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> {
            handleLoginEvent();
        });

        Button skipLogin = findViewById(R.id.skipLogin);
        skipLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
            startActivity(intent);
        });

    }

    private void handleLoginEvent() {
        TextInputLayout usernameTextInputLayout = findViewById(R.id.username_input_layout);
        TextInputLayout passwordTextInputLayout = findViewById(R.id.password_input_layout);

        if (usernameEditText.getText().toString().isEmpty()) {
            usernameTextInputLayout.setError(getString(R.string.error_field_required));
            return;
        } else {
            usernameTextInputLayout.setError(null);
        }

        if (passwordEditText.getText().toString().isEmpty()) {
            passwordTextInputLayout.setError(getString(R.string.error_field_required));
            return;
        } else {
            passwordTextInputLayout.setError(null);
        }

        if (usernameEditText.getText().toString().length() < 4 ||
                passwordEditText.getText().toString().length() < 6) {
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle(R.string.h_login_failure)
                    .setMessage(R.string.error_invalid_login)
                    .setPositiveButton(android.R.string.yes, (dialog, id) -> {
                    }).show();
        } else {
            postLoginRequest(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());
        }
    }

    private void postLoginRequest(String username, String password) {
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getResources().getString(R.string.loader_text));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        String URL = Action.getRequestURL("/login");
        JSONObject message = new JSONObject();

        try {
            message.put("username", username);
            message.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getResources().getString(R.string.loader_text));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, URL, message,
                createMyReqSuccessListener(),
                createMyReqErrorListener()
        );

        Action.getInstance(getApplicationContext()).addToRequestQueue(myReq);
    }

    private Response.Listener<JSONObject> createMyReqSuccessListener() {
        return response -> {
            mProgressDialog.dismiss();
            int header = R.string.h_login_failure;
            String status = Action.getValue(response, "status");
            String statusDescription = Action.getValue(response, "status_description");

            if (status != null) {
                if (!status.equals("PASS")) {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle(header)
                            .setMessage(statusDescription)
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.yes, (dialog, id) -> {

                            }).show();
                } else {
                    /*try {
                        db.cleanLocationData();
                        JSONArray regions = new JSONArray(Action.getValue(response, "regions"));
                        JSONArray districts = new JSONArray(Action.getValue(response, "districts"));
                        JSONArray wards = new JSONArray(Action.getValue(response, "wards"));
                        if (
                                db.recordData(regions, "regions") &&
                                        db.recordData(districts, "districts") &&
                                        db.recordData(wards, "wards")
                        ) {
                            setAuthToken(Action.getValue(response, "token"));
                            getNextActivity();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                }
            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
        return ex -> {
            String message = Action.parseErrorResponse(ex);
            mProgressDialog.dismiss();
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle(R.string.h_login_failure)
                    .setMessage(message)
                    .setNeutralButton(R.string.view_settings, (dialog, id) -> {
                        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton(android.R.string.no, (dialog, id) -> finish())
                    .setPositiveButton(R.string.action_continue, (dialog, id) -> getNextActivity()).show();

        };
    }

    public boolean setAuthToken(String authToken) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("AuthToken", authToken);
        editor.apply(); //commit changes
        return true;
    }

    public void getNextActivity() {
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}