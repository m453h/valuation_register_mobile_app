package com.aru.valuationregister.ValuationRegister;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.aru.valuationregister.R;

import org.json.JSONObject;



public class ParentFormWizard extends AppCompatActivity implements View.OnClickListener {


    private int currentForm = 1;

    private int maximumFormCount = 9;

    private String TAG = "COURTLOX";

    private ProgressDialog mProgressDialog;


    private JSONObject myMessage;


    private Bundle formData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_form_wizard);

        mProgressDialog = new ProgressDialog(this);



        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Valuation Register Form");

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

        /*Fragment fragment = null;
        Class fragmentClass;
        fragmentClass = evolvetz.jmapper.CourtLogsheet.CourtFormStep1.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();*/

    }

    private void switchFragment() {

        // Handle navigation view item clicks here.
        Fragment fragment = null;
        Fragment activeFragment = getCurrentFragment();
        Class fragmentClass = null;
        String fragmentTag = "HOME_FRAGMENT";

        /*
        switch (currentForm) {
            case 1:
                fragmentClass = CourtFormStep1.class;
                fragmentTag = "FORM_STEP_1";
                break;
            case 2:
                fragmentClass = CourtFormStep2.class;
                fragmentTag = "FORM_STEP_2";
                break;
            case 3:
                fragmentClass = CourtFormStep3.class;
                fragmentTag = "FORM_STEP_3";
                break;
            case 4:
                fragmentClass = CourtFormStep4.class;
                fragmentTag = "FORM_STEP_4";
                break;
            case 5:
                fragmentClass = CourtFormStep5.class;
                fragmentTag = "FORM_STEP_5";
                break;
            case 6:
                fragmentClass = CourtFormStep6.class;
                fragmentTag = "FORM_STEP_6";
                break;
            case 7:
                fragmentClass = CourtFormStep7.class;
                fragmentTag = "FORM_STEP_7";
                break;

            case 8:
                fragmentClass = CourtFormStep8.class;
                fragmentTag = "FORM_STEP_8";
                break;

            case 9:
                saveFormData();
                break;
        }*/

        if (fragmentClass != null) {
            try {
                assert fragmentClass != null;
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (activeFragment.getClass() != fragmentClass) {
                if (fragmentTag.equals("FORM_STEP_1")) {
                    FragmentManager fragmentManager = getSupportFragmentManager();

                    int count = fragmentManager.getBackStackEntryCount();

                    for (int i = 0; i < count; ++i) {
                        fragmentManager.popBackStack();
                    }

                    fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                } else {
                    fragmentReplace(fragment, fragmentTag);
                }
            }
        }
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


    public void displayIncompleteFormDialog()
    {
        new AlertDialog.Builder(ParentFormWizard.this)
                .setTitle(R.string.h_incomplete_form)
                .setMessage(getResources().getString(R.string.p_incomplete_form))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).show();
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.next)
        {
            if (currentForm < maximumFormCount)
                currentForm = currentForm + 1;
            switchFragment();
        }
        else if(v.getId()==R.id.back)
        {
            if (currentForm>1)
                currentForm = currentForm - 1;
            switchFragment();
        }

    }

    private void confirmExit()
    {
        new AlertDialog.Builder(this)
                .setTitle(R.string.h_confirm_exit)
                .setMessage(R.string.p_confirm_close)
                .setPositiveButton(R.string.action_yes, (dialogInterface, i) -> finish())
                .setNegativeButton(R.string.action_no,null)
                .show();
    }

    @Override
    public void onBackPressed() {
       confirmExit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:confirmExit();break;
        }
        return true;
    }

}
