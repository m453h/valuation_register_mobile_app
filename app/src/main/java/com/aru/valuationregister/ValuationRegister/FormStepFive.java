package com.aru.valuationregister.ValuationRegister;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.aru.valuationregister.R;


public class FormStepFive extends Fragment {

    private boolean isComplete;

    private ParentFormWizard parentFormWizard;

    private Intent intent;



    private Bundle formData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.form_step_two, container, false);

        // Get ui form widgets


        //Set the intent used to persist data across fragments
        intent = requireActivity().getIntent();
        formData = intent.getExtras();

        if (formData == null) {
            formData = new Bundle();
        }

        // Get the instance of the Parent form wizard with form building helpers
        parentFormWizard = (ParentFormWizard) getActivity();
        assert parentFormWizard != null;

        // Add dropdown lists to the parent activity, these will be used to initialize the widget
        isComplete = false;

        // Set persisted form data to current UI elements
        displayBoundDataFields();



        return v;
    }

    /**
     * Called whenever the wizard proceeds to the next step or goes back to the previous step
     */
    private void bindDataFields() {

        isComplete = formData.get("villageStreetId") != null &&
                formData.get("landPlotId") != null &&
                formData.get("notableLandmarks") != null &&
                formData.get("propertyAccessibilityIds") != null;
        intent.putExtras(formData);
    }

    private void displayBoundDataFields() {
        if (formData != null) {

        }

    }

    @Override
    public void onPause() {
        super.onPause();
        bindDataFields();
    }

    @Override
    public void onResume() {
        super.onResume();
        displayBoundDataFields();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button btnNext = requireActivity().findViewById(R.id.next);
        btnNext.setOnClickListener(v -> {
            bindDataFields();

            if (isComplete) {
                ((ParentFormWizard) requireActivity()).onClick(v);
            } else {
                ((ParentFormWizard) requireActivity()).displayIncompleteFormDialog();
            }
        });

        Button btnBack = requireActivity().findViewById(R.id.back);
        btnBack.setOnClickListener(v -> ((ParentFormWizard) requireActivity()).onClick(v));
    }

}
