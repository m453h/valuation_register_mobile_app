package com.aru.valuationregister;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aru.valuationregister.Rest.Action;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;


public class SettingsActivity extends AppCompatActivity implements OnClickListener {


    // 1. UI references.
    private TextView mAPIURLView;
    private SharedPreferences prefs;
    private ProgressDialog mProgressDialog;
    private Toolbar toolbar;

    private EditText inputText;
    private EditText inputTextConfirmation;
    private TextInputLayout inputLayout;
    private AlertDialog alertDialog;


    private String apiURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mProgressDialog=new ProgressDialog(this);

        toolbar= findViewById(R.id.app_bar);

        LinearLayout mAPILayout = findViewById(R.id.mURLAPILayout);
        mAPILayout.setOnClickListener(this);

        prefs = getApplicationContext().
                getSharedPreferences(getResources().getString(R.string.app_prefs), MODE_PRIVATE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setElevation(5);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        mAPIURLView = findViewById(R.id.mAPIURL);

        apiURL = prefs.getString("apiURL", null);

        if(apiURL!=null)
            mAPIURLView.setText(apiURL);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.mURLAPILayout) {
            getSettingsEditorDialog(R.string.api_url, R.string.api_url, "",
                    InputType.TYPE_CLASS_TEXT);
        }
    }

    public void updateSettingsValue(String type)
    {

        String value = inputText.getText().toString();
        String valueConfirmation = inputTextConfirmation.getText().toString();

        if(value.matches(""))
        {
            inputLayout.setError(getResources().getString(R.string.error_field_required));
        }
        else
        {

            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage(getResources().getString(R.string.loader_text));
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            mProgressDialog.hide();

            SharedPreferences.Editor editor = prefs.edit();

            editor.putString("apiURL", value);
            editor.apply();
            mAPIURLView.setText(value);
            alertDialog.dismiss();
            Action.setParentUrl();
            mAPIURLView.setText(apiURL);
        }
    }


    public void getSettingsEditorDialog(int header, int hint, String value, final int inputType)
    {
        Context context = this;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(header);

        View inflate = LayoutInflater.from(context).
                inflate(R.layout.custom_dialog_profile, null, false);

        inputText = inflate.findViewById(R.id.mProfileInput);
        inputText.setText(value);
        inputText.setInputType(inputType);

        inputLayout = inflate.findViewById(R.id.mProfileInputLayout);
        inputLayout.setHint(getResources().getString(hint));

        inputTextConfirmation = inflate.findViewById(R.id.mProfileInputConfirmation);

        if(header==R.string.password)
        {
            inputTextConfirmation.setVisibility(View.VISIBLE);
            inputTextConfirmation.setInputType(inputType);

            TextInputLayout inputConfirmationLayout = inflate.
                    findViewById(R.id.mProfileInputConfirmationLayout);
            inputConfirmationLayout.setHint(getResources().getString(R.string.prompt_confirm_password));
        }

        builder.setView(inflate);
        builder.setCancelable(false);
        final String inputTypeValue = getResources().getString(header);


        if(inputTypeValue.equals("System URL"))
            inputText.setText(apiURL);


        builder.setNegativeButton(android.R.string.cancel, (dialogInterface, i)
                -> dialogInterface.cancel());


        builder.setPositiveButton(android.R.string.ok, null);

        alertDialog = builder.create();

        final AlertDialog copyAlertDialog = alertDialog;

        alertDialog.setOnShowListener(dialogInterface -> {

            Button button =  copyAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view ->
                    updateSettingsValue(inputTypeValue)
            );
        });

        alertDialog.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:finish();break;
        }
        return true;
    }

}

