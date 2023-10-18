package com.aru.valuationregister;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.aru.valuationregister.Database.AppDatabase;
import com.aru.valuationregister.Database.AppExecutors;
import com.aru.valuationregister.Database.Models.Configuration;
import com.aru.valuationregister.MenuActivities.MainMenuActivity;
import com.aru.valuationregister.Rest.Action;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;


public class LoginActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    private EditText usernameEditText;
    private EditText passwordEditText;

    private SharedPreferences prefs;
    private AppDatabase db;

    private String dataChecksum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = getApplicationContext().
                getSharedPreferences("VALUATION_REGISTER", MODE_PRIVATE);

        // Initialize action with global shared preferences
        Action.initialize(prefs);

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

        db = AppDatabase.getInstance(getApplicationContext());
        dataChecksum = prefs.getString("dataChecksum", null);

        if (prefs.getString("AuthToken", null) != null) {
            getNextActivity();
        }
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

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, URL, message,
                response -> {
                    String status = Action.getValue(response, "status");
                    String statusDescription = Action.getValue(response, "status_description");

                    if (status != null) {
                        if (!status.equals("PASS")) {
                            mProgressDialog.dismiss();
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle(R.string.h_login_failure)
                                    .setMessage(statusDescription)
                                    .setCancelable(false)
                                    .setPositiveButton(android.R.string.yes, (dialog, id) -> {
                                    }).show();
                        } else {
                            String remoteDataChecksum = Action.
                                    getFieldValue(response, "checksum");
                            String authToken = Action.
                                    getFieldValue(response, "token");

                            setAuthToken(authToken);

                            if (!Objects.equals(dataChecksum, remoteDataChecksum)) {
                                sendConfigurationDataRequest(authToken);
                            } else {
                                mProgressDialog.dismiss();
                                getNextActivity();
                            }
                        }
                    }
                },
                ex -> {
                    String msg = Action.parseErrorResponse(ex);
                    mProgressDialog.dismiss();
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle(R.string.h_login_failure)
                            .setMessage(msg)
                            .setNeutralButton(R.string.view_settings, (dialog, id) -> {
                                Intent intent = new Intent(getApplicationContext(),
                                        SettingsActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            })
                            .setNegativeButton(android.R.string.no, (dialog, id) -> finish())
                            .setPositiveButton(R.string.action_continue, (dialog, id) ->
                                    getNextActivity()).show();

                }
        );

        Action.getInstance(getApplicationContext()).addToRequestQueue(myReq);
    }

    private void sendConfigurationDataRequest(String authToken) {

        String URL = Action.getRequestURL("/configurations");
        JSONObject message = new JSONObject();

        try {
            message.put("token", authToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, message,
                response -> {
                    mProgressDialog.dismiss();

                    String status = Action.getValue(response, "status");
                    String statusDescription = Action.getValue(response, "status_description");

                    if (Objects.equals(status, "PASS")) {
                        try {
                            JSONArray configurations = new JSONArray(Action.getValue(response,
                                    "configuration"));

                            addConfigurationDataToDatabase(configurations);
                            getNextActivity();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle(R.string.h_login_failure)
                                .setMessage(statusDescription)
                                .setCancelable(false)
                                .setPositiveButton(android.R.string.yes, (dialog, id) -> {
                                }).show();
                    }
                },
                error -> {
                    mProgressDialog.dismiss();
                }
        );

        Action.getInstance(getApplicationContext()).addToRequestQueue(request);
    }


    public boolean setAuthToken(String authToken) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("AuthToken", authToken);
        editor.apply();
        return true;
    }

    public void getNextActivity() {
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void addConfigurationDataToDatabase(final JSONArray configurations) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            db.configurationDao().deleteAll();
            if (configurations != null) {
                for (int i = 0; i < configurations.length(); i++) {
                    try {
                        JSONObject config = configurations.getJSONObject(i);
                        Configuration configuration = new Configuration(
                                config.getString("id"),
                                config.getString("description"),
                                config.getString("type")
                        );
                        db.configurationDao().insertAll(configuration);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void getAllFromDb() {

        AppExecutors.getInstance().diskIO().execute(() -> {
            List<Configuration> configurations = db.configurationDao().getAll();
            for (Configuration config : configurations) {
                Log.wtf("VALUATION_REGISTER", config.description);
                runOnUiThread(() -> {
                });
            }
        });
    }


}