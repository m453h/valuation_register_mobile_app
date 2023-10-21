package com.aru.valuationregister.ValuationRegister;

import android.app.ProgressDialog;
import android.os.Bundle;
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
import androidx.fragment.app.FragmentTransaction;

import com.aru.valuationregister.Database.AppDatabase;
import com.aru.valuationregister.Database.AppExecutors;
import com.aru.valuationregister.Database.Models.Configuration;
import com.aru.valuationregister.R;
import com.aru.valuationregister.ValuationRegister.Callbacks.ConfigurationQueryResultsCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class ParentFormWizard extends AppCompatActivity implements View.OnClickListener {


    private int currentForm = 1;

    private int maximumFormCount = 9;

    private ProgressDialog mProgressDialog;


    private JSONObject myMessage;


    private Bundle formData;

    private AppDatabase db;

    private HashMap<String, AutoCompleteTextView> dropdownLists;

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
        dropdownLists = new HashMap<>();
    }

    public void addToCurrentViews(String key, AutoCompleteTextView view) {
        this.dropdownLists.put(key, view);
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
        Fragment fragment;
        Class<? extends Fragment> fragmentClass;

        switch (currentForm) {
            case 1:
                fragmentClass = FormStepOne.class;
            default:
                fragmentClass = FormStepOne.class;
        }


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

    public void fragmentReplace(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContent, fragment, tag);
        fragmentTransaction.addToBackStack(null); //this will add it to back stack
        fragmentTransaction.commit();
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
            if (currentForm < maximumFormCount)
                currentForm = currentForm + 1;
            switchFragment();
        } else if (v.getId() == R.id.back) {
            if (currentForm > 1)
                currentForm = currentForm - 1;
            switchFragment();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                confirmExit();
                break;
        }
        return true;
    }

    public void initializeDropdownLists(String type) {

        AppExecutors.getInstance().diskIO().execute(() -> {
            List<Configuration> configurations = db.configurationDao().
                    loadAllByType(type);

            String[] data = new String[configurations.size()];

            for (int i = 0; i < configurations.size(); i++) {
                Configuration config = configurations.get(i);
                data[i] = config.description;
            }

            runOnUiThread(() -> {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getCurrentFragment().requireActivity(),
                        R.layout.dropdown_item, data);
                Objects.requireNonNull(dropdownLists.get(type)).setAdapter(arrayAdapter);
            });
        });
    }

    public void getConfigurationItemId(String type, String description, ConfigurationQueryResultsCallback callback) {

        AppExecutors.getInstance().diskIO().execute(() -> {
            Configuration configuration = db.configurationDao().
                    getConfigurationItemId(type, description);
            callback.onDataReceived(configuration, "selected");
        });
    }

    public void getDialogSelector(EditText editText, String type, ConfigurationQueryResultsCallback callback) {
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
                        .setTitle(R.string.property_accessibility)
                        .setMultiChoiceItems(data, checkedValues, (dialog1, indexSelected, isChecked) -> {
                            if (isChecked) {
                                selectedItems.add(data[indexSelected]);
                                callback.onDataReceived(configurations.get(indexSelected), "checked");
                            } else {
                                selectedItems.remove(data[indexSelected]);
                                callback.onDataReceived(configurations.get(indexSelected), "unchecked");
                            }
                        }).setPositiveButton("OK", (dialogOk, id) -> {
                            String selectedItemsText = selectedItems.toString();
                            selectedItemsText = selectedItemsText.replace('[', ' ');
                            selectedItemsText = selectedItemsText.replace(']', ' ');
                            editText.setText(selectedItemsText);
                        }).setNegativeButton("Cancel", (dialogCancel, id) -> {
                        }).create();
                dialog.show();
            });

        });

    }

}
