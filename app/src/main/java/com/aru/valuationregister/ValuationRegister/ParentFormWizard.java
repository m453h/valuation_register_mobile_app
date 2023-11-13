package com.aru.valuationregister.ValuationRegister;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.aru.valuationregister.Database.AppDatabase;
import com.aru.valuationregister.Database.AppExecutors;
import com.aru.valuationregister.Database.Models.Configuration;
import com.aru.valuationregister.R;
import com.aru.valuationregister.Rest.Action;
import com.aru.valuationregister.ValuationRegister.Interfaces.ConfigurationQueryResultsCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ParentFormWizard extends AppCompatActivity implements View.OnClickListener {


    private int currentForm = 1;
    private int maximumFormCount = 5;
    private ProgressDialog mProgressDialog;
    private JSONObject myMessage;
    private Bundle formData;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_form_wizard);

        mProgressDialog = new ProgressDialog(this);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Valuation Register Form");
        db = AppDatabase.getInstance(getApplicationContext());

        initializeFragment();
        initializeNavigationButtons();
    }

    private void initializeNavigationButtons() {
        Button back = findViewById(R.id.back);
        Button next = findViewById(R.id.next);

        back.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    private void initializeFragment() {
        Fragment fragment;
        Class<? extends Fragment> fragmentClass = FormStepOne.class;
        try {
            fragment = fragmentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.flContent, fragment)
                .commit();
    }


    private void switchFragment() {
        Class<? extends Fragment> fragmentClass = null;
        switch (currentForm) {
            case 1:
                fragmentClass = FormStepOne.class;
                break;
            case 2:
                fragmentClass = FormStepTwo.class;
                break;
            case 3:
                fragmentClass = FormStepThree.class;
                break;
            case 4:
                fragmentClass = FormStepFour.class;
                break;
            case 5:
                fragmentClass = FormStepFive.class;
                break;
        }

        if (fragmentClass != null) {
            try {
                Fragment fragment = fragmentClass.newInstance();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, fragment)
                        .commit();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.flContent);
    }

    public void displayIncompleteFormDialog() {
        new AlertDialog.Builder(ParentFormWizard.this)
                .setTitle(R.string.h_incomplete_form)
                .setMessage(getResources().getString(R.string.p_incomplete_form))
                .setPositiveButton(android.R.string.ok, (dialog, id) -> {
                }).show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.next) {
            if (currentForm < maximumFormCount) {
                currentForm = currentForm + 1;
                switchFragment();
            } else {
                saveFormData();
            }
        } else if (v.getId() == R.id.back) {
            if (currentForm > 1) {
                currentForm = currentForm - 1;
                switchFragment();
            }
        }
    }

    private void confirmExit() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.h_confirm_exit)
                .setMessage(R.string.p_confirm_close)
                .setPositiveButton(R.string.action_yes, (dialogInterface, i) -> finish())
                .setNegativeButton(R.string.action_no, null)
                .show();
    }

    @Override
    public void onBackPressed() {
        confirmExit();
    }

    public void saveFormData() {
        formData = this.getIntent().getExtras();
        final SharedPreferences prefs = getApplicationContext().
                getSharedPreferences(getResources().getString(R.string.app_prefs), MODE_PRIVATE);

        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_heading)
                .setMessage(R.string.confirm_paragraph)
                .setCancelable(false)
                .setNegativeButton(android.R.string.no, (dialog, id) -> {
                })
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                    myMessage = new JSONObject();
                    try {
                        myMessage.put("authToken", prefs.
                                getString("AuthToken", null));
                        myMessage.put("villageStreetId",
                               formData.get("villageStreetId"));
                        myMessage.put("landPlotId",
                               formData.get("landPlotId"));
                        myMessage.put("notableLandmarks",
                               formData.get("notableLandmarks"));
                        myMessage.put("propertyAccessibilityIds",
                               formData.get("propertyAccessibilityIds"));
                        myMessage.put("accessibilityRoadName",
                               formData.get("accessibilityRoadName"));
                        myMessage.put("isNeighbourOfValuableProject",
                               formData.get("isNeighbourOfValuableProject"));
                        myMessage.put("valuableProjectName",
                               formData.get("valuableProjectName"));

                        myMessage.put("roadInfrastructureQualityIds",
                               formData.get("roadInfrastructureQualityIds"));
                        myMessage.put("hasTitle",
                               formData.get("hasTitle"));
                        myMessage.put("propertyAccessibilityIds",
                               formData.get("propertyAccessibilityIds"));
                        myMessage.put("whenTitleAcquired",
                               formData.get("whenTitleAcquired"));
                        myMessage.put("plotSize",
                               formData.get("plotSize"));
                        myMessage.put("landUseId",
                               formData.get("landUseId"));
                        myMessage.put("propertySizeId",
                                formData.get("propertySizeId"));
                        myMessage.put("accommodationDetailsIds",
                               formData.get("accommodationDetailsIds"));

                        myMessage.put("landDevelopmentStatus",
                               formData.get("landDevelopmentStatus"));
                        myMessage.put("numberOfBuildings",
                               formData.get("numberOfBuildings"));
                        myMessage.put("buildingTypeIds",
                               formData.get("buildingTypeIds"));
                        myMessage.put("servantQuarterSize",
                                formData.get("servantQuarterSize"));
                        myMessage.put("propertyConditionId",
                               formData.get("propertyConditionId"));
                        myMessage.put("hasMortgage", formData.
                                getString("hasMortgage"));
                        myMessage.put("mortgageCreditFacilityId",
                               formData.get("mortgageCreditFacilityId"));

                        myMessage.put("servicesAmenitiesIds",
                               formData.get("servicesAmenitiesIds"));
                        myMessage.put("otherServicesAmenities",
                               formData.get("otherServicesAmenities"));
                        myMessage.put("whenPlotWasPurchased",
                               formData.get("whenPlotWasPurchased"));
                        myMessage.put("landAcquisitionId",
                               formData.get("landAcquisitionId"));
                        myMessage.put("otherWayLandWasAcquired",
                               formData.get("otherWayLandWasAcquired"));
                        myMessage.put("howMuchWasPaidToAcquireLand",
                               formData.get("howMuchWasPaidToAcquireLand"));

                        myMessage.put("landAcquisitionFundSourceId",
                               formData.get("landAcquisitionFundSourceId"));
                        myMessage.put("otherSourceOfFunds",
                               formData.get("otherSourceOfFunds"));
                        myMessage.put("landAcquisitionSourceOfInfoId",
                               formData.get("landAcquisitionSourceOfInfoId"));
                        myMessage.put("otherSourceOfInfo",
                               formData.get("otherSourceOfInfo"));
                        myMessage.put("awareOfSale",
                               formData.get("awareOfSale"));
                        myMessage.put("propertySaleTypeId",
                               formData.get("propertySaleTypeId"));
                        myMessage.put("sellingPrice",
                               formData.get("sellingPrice"));
                        myMessage.put("identifier", Action.getUUID());

                        Log.e(getResources().getString(R.string.app_prefs),myMessage.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestQueue queue = Action.getInstance(getApplicationContext()).
                            getRequestQueue();
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setMessage(getResources().getString(R.string.loader_text));
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();

                    String URL = Action.getRequestURL("/valuation-register");

                    JsonObjectRequest myReq = new JsonObjectRequest(
                            Request.Method.POST,
                            URL,
                            myMessage,
                            response -> {
                                mProgressDialog.dismiss();
                                final String status = Action.
                                        getFieldValue(response, "status");
                                final String description = Action.
                                        getFieldValue(response, "status_description");

                                new AlertDialog.Builder(ParentFormWizard.this)
                                        .setTitle(R.string.h_results)
                                        .setMessage(description)
                                        .setPositiveButton(android.R.string.yes, (dialog1, id) -> {
                                            if (status.equals("PASS"))
                                                finish();
                                        }).show();
                            },
                            ex -> {
                                String message = Action.parseErrorResponse(ex);
                                mProgressDialog.dismiss();
                                new AlertDialog.Builder(ParentFormWizard.this)
                                        .setTitle(R.string.h_error_details)
                                        .setMessage(message + "\n\n" +
                                                getResources().getString(R.string.offline_stored))
                                        .setNegativeButton(android.R.string.no, (dialogNo, id) -> {
                                        })
                                        .setPositiveButton(android.R.string.yes,
                                                (dialogOk, id) -> finish()
                                        ).show();
                            }
                    );

                    myReq.setRetryPolicy(new DefaultRetryPolicy(30000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue.add(myReq);
                }).show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                confirmExit();
                break;
        }
        return true;
    }

    public void initializeDropdownLists(AutoCompleteTextView widget, String type) {

        AppExecutors.getInstance().diskIO().execute(() -> {
            List<Configuration> configurations = db.configurationDao().
                    loadAllByType(type);

            String[] data = new String[configurations.size()];

            for (int i = 0; i < configurations.size(); i++) {
                Configuration config = configurations.get(i);
                data[i] = config.description;
            }

            runOnUiThread(() -> {
                ArrayAdapter<String> arrayAdapter =
                        new ArrayAdapter<>(getCurrentFragment().requireActivity(),
                        R.layout.dropdown_item, data);
                widget.setAdapter(arrayAdapter);
            });
        });
    }

    public void initializeSimpleDropdownLists(AutoCompleteTextView widget, String[] data) {

        AppExecutors.getInstance().diskIO().execute(() -> {

            runOnUiThread(() -> {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getCurrentFragment()
                        .requireActivity(),
                        R.layout.dropdown_item, data);
                widget.setAdapter(arrayAdapter);
            });
        });
    }

    public void getConfigurationItemId(String type, String description,
                                       ConfigurationQueryResultsCallback callback) {

        AppExecutors.getInstance().diskIO().execute(() -> {
            Configuration configuration = db.configurationDao().
                    getConfigurationItemId(type, description);
            callback.onDataReceived(configuration, "selected");
        });
    }

    public void getDialogSelector(EditText editText, String type, int heading,
                                  ConfigurationQueryResultsCallback callback) {
        String selectedValuesText = editText.getText().toString();

        AppExecutors.getInstance().diskIO().execute(() -> {
            List<Configuration> configurations = db.configurationDao().
                    loadAllByType(type);

            String[] data = new String[configurations.size()];
            boolean[] checkedValues = new boolean[configurations.size()];
            ArrayList<String> selectedItems = new ArrayList<>();

            if (!selectedValuesText.isEmpty()) {
                String[] parsedCheckedValues = selectedValuesText.split(",");
                for (String s : parsedCheckedValues) {
                    s = s.strip();
                    selectedItems.add(s);
                }
            }

            for (int i = 0; i < configurations.size(); i++) {
                Configuration config = configurations.get(i);
                data[i] = config.description;
                checkedValues[i] = selectedItems.contains(config.description);
            }

            runOnUiThread(() -> {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle(heading)
                        .setMultiChoiceItems(data, checkedValues,
                                (dialog1, indexSelected, isChecked) -> {
                            if (isChecked) {
                                selectedItems.add(data[indexSelected]);
                                callback.onDataReceived(configurations.get(indexSelected),
                                        "checked");
                            } else {
                                selectedItems.remove(data[indexSelected]);
                                callback.onDataReceived(configurations.get(indexSelected),
                                        "unchecked");
                            }
                        }).setPositiveButton("OK", (dialogOk, id) -> {
                            String selectedItemsText = selectedItems.toString();
                            selectedItemsText = selectedItemsText.
                                    replace('[', ' ');
                            selectedItemsText = selectedItemsText.
                                    replace(']', ' ');
                            editText.setText(selectedItemsText);
                        }).setNegativeButton("Cancel",
                                (dialogCancel, id) -> {
                        }).create();
                dialog.show();
            });

        });

    }

    public void setBoundWidgetData(AutoCompleteTextView widget, String value) {
        if (value != null && widget != null) {
            widget.setText(value);
        }
    }

    public void setBoundWidgetData(EditText widget, String value) {
        if (value != null && widget != null) {
            widget.setText(value);
        }
    }
}
