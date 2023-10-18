package com.aru.valuationregister.MenuActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aru.valuationregister.R;
import com.aru.valuationregister.Rest.Action;
import com.aru.valuationregister.SettingsActivity;
import com.aru.valuationregister.ValuationRegister.ParentFormWizard;

import java.util.ArrayList;

/**
 * Created by michael.nkotagu on 6/18/2015.
 */
public class MainMenuActivity extends AppCompatActivity {

    private static RecyclerView recyclerView;
    static View.OnClickListener myOnClickListener;
    private SharedPreferences prefs;
    private String version;
    private ProgressDialog mProgressDialog;

    private int counter;
    private String localRecordId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mProgressDialog = new ProgressDialog(this);
        Toolbar toolbar = findViewById(R.id.app_bar);
        prefs = getApplicationContext().getSharedPreferences("VALUATION_REGISTER", MODE_PRIVATE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Valuation Register Menu");
        getSupportActionBar().setElevation(5);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        RecyclerView.Adapter<MenuMyAdapter.MyViewHolder> adapter =
                new MenuMyAdapter(getApplicationMenuItems());
        recyclerView.setAdapter(adapter);
        handleRecyclerViewClickEvent();
        version = Action.getApplicationVersion(getApplicationContext());

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                confirmExit();
            }
        };
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback);
    }

    private ArrayList<MenuData> getApplicationMenuItems(){
        ArrayList<MenuData> items = new ArrayList<>();
        MenuBuilder myMenu = new MenuBuilder("MainMenuActivity");

        for (int i = 0; i < myMenu.nameArray.length; i++) {
            items.add(
                    new MenuData(
                            myMenu.nameArray[i],
                            myMenu.descriptionArray[i],
                            myMenu.drawableArray[i]
                    )
            );
        }
        return items;
    }

    private void handleRecyclerViewClickEvent() {
        recyclerView.addOnItemTouchListener(
                new MenuRecyclerItemClickListener(this.getApplicationContext(),
                        (view, position) -> {
                            int selectedItemPosition = recyclerView.getChildLayoutPosition(view);
                            RecyclerView.ViewHolder viewHolder = recyclerView.
                                    findViewHolderForAdapterPosition(selectedItemPosition);

                            assert viewHolder != null;
                            TextView textViewName = viewHolder.itemView.
                                    findViewById(R.id.textViewName);
                            String selectedName = (String) textViewName.getText();
                            switch (selectedName) {
                                case "Create a new Record":
                                    Intent intent = new Intent(getApplicationContext(),
                                            ParentFormWizard.class);
                                    startActivity(intent);
                                    break;

                                case "Settings":
                                    intent = new Intent(getApplicationContext(),
                                            SettingsActivity.class);
                                    intent.putExtra("callingActivity", "Internal");
                                    startActivity(intent);
                                    break;

                                case "Upload Data":

                                    new AlertDialog.Builder(MainMenuActivity.this)
                                            .setTitle("Data Upload Status")
                                            .setMessage(counter + " Record(s) found press OK to begin data upload")
                                            .setPositiveButton("Ok", (dialog, whichButton) -> {
                                            })
                                            .setNegativeButton("Cancel", (dialog, whichButton) -> {

                                            })
                                            .show();
                                    break;

                                case "Fetch Updates":
                                    new AlertDialog.Builder(MainMenuActivity.this)
                                            .setTitle("Update Status")
                                            .setMessage("Application will exit to check for updates")
                                            .setPositiveButton("Ok", (dialog, whichButton) -> getUpdates())
                                            .setNegativeButton("Cancel", (dialog, whichButton) -> {

                                            })
                                            .show();
                                    break;

                                default:
                                    new AlertDialog.Builder(MainMenuActivity.this)
                                            .setTitle("Feature Not Allowed")
                                            .setMessage("Mobile phone is not allowed to perform this action ")
                                            .setPositiveButton("Ok", (dialog, whichButton) -> {

                                            }).show();
                                    break;
                            }
                        })
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    public void removeAuthToken() {
        prefs.edit().remove("AuthToken").apply();
    }

    public void getUpdates() {
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
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
