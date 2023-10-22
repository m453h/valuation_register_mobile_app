package com.aru.valuationregister.ValuationRegister;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.aru.valuationregister.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FormStepFour extends Fragment {

    private boolean isComplete;
    private ParentFormWizard parentActivity;
    private EditText servicesAmenitiesEditText;
    private List<String> servicesAmenitiesIds;
    private List<String> servicesAmenitiesText;
    private TextInputLayout otherServicesAmenitiesInputLayout;
    private AutoCompleteTextView howLandWasAcquiredTextView;
    private TextInputLayout otherWayLandWasAcquiredInputLayout;
    private EditText whenPlotWasPurchasedEditText;
    private EditText otherWayLandWasAcquiredEditText;
    private EditText howMuchWasPaidToAcquireLandEditText;
    private EditText otherServicesAmenitiesEditText;
    private Intent intent;



    private Bundle formData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.form_step_four, container, false);

        //Set the intent used to persist data across fragments
        intent = requireActivity().getIntent();
        formData = intent.getExtras();

        if (formData == null) {
            formData = new Bundle();
        }

        // Get the instance of the Parent form wizard with form building helpers
        parentActivity = (ParentFormWizard) getActivity();
        assert parentActivity != null;

        // Add dropdown lists to the parent activity, these will be used to initialize the widget
        isComplete = false;

        // Initialize UI widgets
        initializeServicesAmenitiesWidget(v);
        initializePropertyConditionWidget(v);
        initializeEditTexts(v);

        // Set persisted form data to current UI elements
        displayBoundDataFields();



        return v;
    }

    private void initializeServicesAmenitiesWidget(View v) {
        servicesAmenitiesIds = new ArrayList<>();
        servicesAmenitiesText = new ArrayList<>();
        servicesAmenitiesEditText = v.findViewById(R.id.services_amenities_text_view);
        otherServicesAmenitiesInputLayout = v.
                findViewById(R.id.other_services_amenities_input_layout);

        servicesAmenitiesEditText.setOnClickListener(v1 ->
                parentActivity.
                        getDialogSelector(servicesAmenitiesEditText,
                                "services-amenities",
                                R.string.services_amenities_in_plot, (configuration, type) -> {
                                    if (type.equals("checked")) {
                                        servicesAmenitiesIds.
                                                add(configuration.configurationId);
                                        servicesAmenitiesText.
                                                add(configuration.description);

                                        if (configuration.description.
                                                equals("Any Other")) {
                                            otherServicesAmenitiesInputLayout.
                                                    setVisibility(View.VISIBLE);
                                        }
                                    } else if (type.equals("unchecked")) {
                                        servicesAmenitiesIds.
                                                remove(configuration.configurationId);
                                        servicesAmenitiesText.
                                                remove(configuration.description);

                                        if (configuration.description.
                                                equals("Any Other")) {
                                            otherServicesAmenitiesInputLayout.
                                                    setVisibility(View.GONE);
                                        }
                                    }
                                    formData.putString("servicesAmenitiesIds",
                                            android.text.TextUtils.join(",",
                                                    servicesAmenitiesIds));
                                    formData.putString("servicesAmenitiesText",
                                            android.text.TextUtils.join(", ",
                                                    servicesAmenitiesText));
                                })
        );
    }

    private void initializePropertyConditionWidget(View v) {
        howLandWasAcquiredTextView = v.findViewById(R.id.how_land_was_acquired_text_view);
        otherWayLandWasAcquiredInputLayout = v.
                findViewById(R.id.other_way_land_was_acquired_input_layout);
        parentActivity.initializeDropdownLists(howLandWasAcquiredTextView,
                "land-acquisition");
        howLandWasAcquiredTextView.setOnItemClickListener((parent, view, position, id) -> {
            parentActivity.getConfigurationItemId("land-acquisition",
                    parent.getItemAtPosition(position).toString(), (configuration, type) -> {
                        formData.putString("landAcquisitionId", configuration.configurationId);
                        formData.putString("landAcquisitionText", configuration.description);
            });

            if (parent.getItemAtPosition(position).toString().equals("Any Other")) {
                otherWayLandWasAcquiredInputLayout.setVisibility(View.VISIBLE);
            } else {
                otherWayLandWasAcquiredInputLayout.setVisibility(View.GONE);
            }
        });
    }

    private void initializeEditTexts(View v) {
        whenPlotWasPurchasedEditText = v.
                findViewById(R.id.when_plot_was_purchased_edit_text);
        otherWayLandWasAcquiredEditText = v.
                findViewById(R.id.other_way_land_was_acquired_edit_text);
        howMuchWasPaidToAcquireLandEditText = v.
                findViewById(R.id.how_much_was_paid_to_acquire_land_edit_text);
        otherServicesAmenitiesEditText = v.
                findViewById(R.id.other_services_amenities_edit_text);
    }

    /**
     * Called whenever the wizard proceeds to the next step or goes back to the previous step
     */
    private void bindDataFields() {

        formData.putString("whenPlotWasPurchased", whenPlotWasPurchasedEditText.
                getText().toString());
        formData.putString("otherWayLandWasAcquired", otherWayLandWasAcquiredEditText
                .getText().toString());
        formData.putString("howMuchWasPaidToAcquireLand", howMuchWasPaidToAcquireLandEditText.
                getText().toString());
        formData.putString("otherServicesAmenities", otherServicesAmenitiesEditText.
                getText().toString());

        isComplete = formData.get("whenPlotWasPurchased") != null &&
                formData.get("servicesAmenitiesIds") != null &&
                formData.get("whenPlotWasPurchased") != null &&
                formData.get("landAcquisitionText") != null;
        intent.putExtras(formData);
        isComplete = true;
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
