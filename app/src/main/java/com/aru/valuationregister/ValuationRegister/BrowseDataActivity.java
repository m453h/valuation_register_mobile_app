package com.aru.valuationregister.ValuationRegister;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aru.valuationregister.R;
import com.aru.valuationregister.Rest.Action;

/**
 * Created by michael.nkotagu on 6/18/2015.
 */
public class BrowseDataActivity extends AppCompatActivity {

    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        Toolbar toolbar = findViewById(R.id.app_bar);
        prefs = getApplicationContext().getSharedPreferences("VALUATION_REGISTER", MODE_PRIVATE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.action_browse);
        getSupportActionBar().setElevation(5);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        WebView myWebView = findViewById(R.id.web_view);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.clearCache(true);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl(Action.getParentUrl()+"/search");

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                confirmExit();
            }
        };
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();


        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.browse_menu, menu);
        return true;
    }

    public void removeAuthToken() {
        prefs.edit().remove("AuthToken").apply();
    }

    private void confirmExit() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.h_confirm_exit)
                .setMessage(R.string.p_confirm_close_app)
                .setPositiveButton(R.string.action_yes, (dialogInterface, i) -> finish())
                .setNegativeButton(R.string.action_no, null)
                .show();
    }


}
