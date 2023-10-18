package com.aru.valuationregister.MenuActivities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.aru.valuationregister.Database.MyDataSource;
import com.aru.valuationregister.LoginActivity;
import com.aru.valuationregister.R;
import com.aru.valuationregister.Rest.Action;
import com.aru.valuationregister.SettingsActivity;
import com.aru.valuationregister.ValuationRegister.ParentFormWizard;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by michael.nkotagu on 6/18/2015.
 */
public class MainMenuActivity extends AppCompatActivity {

    //Android Variables
    private Toolbar toolbar;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<MenuData> items;
    static View.OnClickListener myOnClickListener;
    private SharedPreferences prefs;
    private String version;
    private ProgressDialog mProgressDialog;


    private MyDataSource db;

    private int counter;
    private String localRecordId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mProgressDialog = new ProgressDialog(this);

        toolbar = findViewById(R.id.app_bar);

        prefs = getApplicationContext().getSharedPreferences("VALUATION_REGISTER", MODE_PRIVATE);

        db = new MyDataSource(getApplicationContext());

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Valuation Register Menu");
        getSupportActionBar().setElevation(5);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        items = new ArrayList<>();
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


        adapter = new MenuMyAdapter(items);
        recyclerView.setAdapter(adapter);


        recyclerView.addOnItemTouchListener(
                new MenuRecyclerItemClickListener(this.getApplicationContext(), new MenuRecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        int selectedItemPosition = recyclerView.getChildPosition(view);
                        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForPosition(selectedItemPosition);

                        TextView textViewName = viewHolder.itemView.findViewById(R.id.textViewName);
                        String selectedName = (String) textViewName.getText();
                        switch (selectedName) {
                            case "Valuation Register":
                                Intent intent = new Intent(getApplicationContext(), ParentFormWizard.class);
                                startActivity(intent);
                                break;

                            case "Settings":
                                intent = new Intent(getApplicationContext(), SettingsActivity.class);
                                intent.putExtra("callingActivity", "Internal");
                                startActivity(intent);
                                break;

                            case "Upload Data":
                                counter = db.getRecordIdentifier(new String[]{"COUNT (_id)"}, "tbl_court_data", null, null);

                                new AlertDialog.Builder(MainMenuActivity.this)
                                        .setTitle("Data Upload Status")
                                        .setMessage(counter + " Record(s) found press OK to begin data upload")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                sendDataToSync();
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {

                                            }
                                        })
                                        .show();
                                break;

                            case "Fetch Updates":
                                new AlertDialog.Builder(MainMenuActivity.this)
                                        .setTitle("Update Status")
                                        .setMessage("Application will exit to check for updates")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                getUpdates();
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {

                                            }
                                        })
                                        .show();
                                break;

                            default:
                                new AlertDialog.Builder(MainMenuActivity.this)
                                        .setTitle("Feature Not Allowed")
                                        .setMessage("Mobile phone is not allowed to perform this action ")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {

                                            }
                                        }).show();
                                break;
                        }
                    }
                })
        );


        version = Action.getApplicationVersion(getApplicationContext());

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

    public boolean sendDataToSync() {
        String[] data;

        data = db.getRecentCourtData();

        localRecordId = data[0];
        if (data[1] != null) {
            try {
                JSONObject message = new JSONObject(data[1]);
                message.put("courtBmpOne", getBase64ImageFromUri("courtBmpOne", message));
                message.put("courtBmpTwo", getBase64ImageFromUri("courtBmpTwo", message));
                message.put("courtBmpThree", getBase64ImageFromUri("courtBmpThree", message));
                message.put("courtBmpFour", getBase64ImageFromUri("courtBmpFour", message));

                String URL = Action.getRequestURL("/api/submitCourtForm");

                JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, URL, message, createMyReqSuccessListener(), createMyReqErrorListener());

                RequestQueue queue = Action.getInstance(getApplicationContext()).getRequestQueue();

                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage(getResources().getString(R.string.loader_text));
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();

                myReq.setRetryPolicy(new DefaultRetryPolicy(30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(myReq);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return true;
    }


    private Response.Listener<JSONObject> createMyReqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {

                counter--;

                Log.e("VALUATION_REGISTER", "Remainder is: " + counter + "");

                //Check status of last upload and delete it
                final String status = Action.getFieldValue(response, "status");

                if (status.equals("PASS") || status.equals("DUPLICATE")) {
                    db.deleteCourtData(localRecordId);
                    Log.e("VALUATION_REGISTER", "Deleted data");
                }

                if (counter == 0) {
                    mProgressDialog.dismiss();

                    new AlertDialog.Builder(MainMenuActivity.this)
                            .setTitle(R.string.h_results)
                            .setMessage(R.string.data_uploaded)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            }).show();
                } else {
                    sendDataToSync();
                }


            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError ex) {
                String message = Action.parseErrorResponse(ex);
                mProgressDialog.dismiss();

                new AlertDialog.Builder(MainMenuActivity.this)
                        .setTitle(R.string.h_error_details)
                        .setMessage(message + "\n\n" + getResources().getString(R.string.offline_upload))
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                            }
                        }).show();


            }
        };
    }


    public String getBase64ImageFromUri(String myStr, JSONObject object) {
        try {
            Uri myUri = Uri.parse(Action.getValue(object, myStr));
            return Action.encodeBmp(Action.getProperImage(myUri));
        } catch (NullPointerException ex) {
            return null;
        }

    }


    public void getUpdates() {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
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
                .setPositiveButton(R.string.action_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.action_no, null)
                .show();
    }

    @Override
    public void onBackPressed() {
        confirmExit();
        super.onBackPressed();
    }

}
