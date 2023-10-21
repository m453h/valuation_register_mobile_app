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


public class FormStepTwo extends Fragment {

    private boolean isComplete;

    private ParentFormWizard parentFormWizard;

    private EditText roadInfrastructureQualityEditText;

    private List<String> roadInfrastructureQualityIds;
    private List<String> roadInfrastructureQualityText;

    private AutoCompleteTextView hasTitleTextView;

    private TextInputLayout whenTitleAcquiredInputLayout;

    private EditText whenTitleAcquiredEditText;
    private EditText plotSizeEditText;

    private EditText landUseEditText;
    private List<String> landUseIds;
    private List<String> landUseText;
    private EditText accommodationDetailsEditText;
    private List<String> accommodationDetailsIds;
    private List<String> accommodationDetailsText;

    private AutoCompleteTextView propertySizeTextView;

    private Intent intent;



    private Bundle formData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.form_step_two, container, false);

        // Get ui form widgets
        roadInfrastructureQualityEditText = v.findViewById(R.id.road_infrastructure_quality);
        hasTitleTextView = v.findViewById(R.id.has_title_text_view);
        whenTitleAcquiredEditText = v.findViewById(R.id.when_was_title_acquired_edit_text);
        whenTitleAcquiredInputLayout = v.findViewById(R.id.when_was_title_acquired_input_layout);
        plotSizeEditText = v.findViewById(R.id.plot_size_edit_text);
        landUseEditText = v.findViewById(R.id.land_use_edit_text);
        propertySizeTextView = v.findViewById(R.id.property_size_text_view);
        accommodationDetailsEditText = v.findViewById(R.id.accommodation_details_edit_text);

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

        parentFormWizard.initializeDropdownLists(propertySizeTextView, "property-size");
        parentFormWizard.initializeSimpleDropdownLists(hasTitleTextView,
                new String[]{"Yes", "No"});

        roadInfrastructureQualityIds = new ArrayList<>();
        roadInfrastructureQualityText = new ArrayList<>();
        roadInfrastructureQualityEditText.setOnClickListener(v1 ->
                parentFormWizard.
                        getDialogSelector(roadInfrastructureQualityEditText,
                                "infrastructure-quality",
                                R.string.road_infrastructure_quality, (configuration, type) -> {
                                    if (type.equals("checked")) {
                                        roadInfrastructureQualityIds.
                                                add(configuration.configurationId);
                                        roadInfrastructureQualityText.
                                                add(configuration.description);
                                    } else if (type.equals("unchecked")) {
                                        roadInfrastructureQualityIds.
                                                remove(configuration.configurationId);
                                        roadInfrastructureQualityText.
                                                remove(configuration.description);
                                    }
                                    formData.putString("roadInfrastructureQualityIds",
                                            android.text.TextUtils.join(",",
                                                    roadInfrastructureQualityIds));

                                    formData.putString("roadInfrastructureQualityText",
                                            android.text.TextUtils.join(", ",
                                                    roadInfrastructureQualityText));
                                })
        );

        landUseIds = new ArrayList<>();
        landUseText = new ArrayList<>();
        landUseEditText.setOnClickListener(v1 ->
                parentFormWizard.
                        getDialogSelector(landUseEditText,
                                "land-use",
                                R.string.land_use, (configuration, type) -> {
                                    if (type.equals("checked")) {
                                        landUseIds.
                                                add(configuration.configurationId);
                                        landUseText.
                                                add(configuration.description);
                                    } else if (type.equals("unchecked")) {
                                        landUseIds.
                                                remove(configuration.configurationId);
                                        landUseText.
                                                remove(configuration.description);
                                    }
                                    formData.putString("landUseIds",
                                            android.text.TextUtils.join(",",
                                                    landUseIds));

                                    formData.putString("landUseText",
                                            android.text.TextUtils.join(", ",
                                                    landUseText));
                                })
        );


        // Set a listener for is neighbour of valuable project dropdown item to get selected item
        hasTitleTextView.
                setOnItemClickListener((parent, view, position, id) -> {

                    if (parent.getItemAtPosition(position).toString().equals("Yes"))
                        whenTitleAcquiredInputLayout.setVisibility(View.VISIBLE);
                    else
                        whenTitleAcquiredInputLayout.setVisibility(View.GONE);

                    formData.putString("hasTitle",
                            parent.getItemAtPosition(position).toString());
                });


        accommodationDetailsIds = new ArrayList<>();
        accommodationDetailsText = new ArrayList<>();
        accommodationDetailsEditText.setOnClickListener(v1 ->
                parentFormWizard.
                        getDialogSelector(accommodationDetailsEditText,
                                "accommodation-details",
                                R.string.accommodation_details, (configuration, type) -> {
                                    if (type.equals("checked")) {
                                        accommodationDetailsIds.
                                                add(configuration.configurationId);
                                        accommodationDetailsText.
                                                add(configuration.description);
                                    } else if (type.equals("unchecked")) {
                                        accommodationDetailsIds.
                                                remove(configuration.configurationId);
                                        accommodationDetailsText.
                                                remove(configuration.description);
                                    }
                                    formData.putString("accommodationDetailsIds",
                                            android.text.TextUtils.join(",",
                                                    accommodationDetailsIds));

                                    formData.putString("accommodationDetailsText",
                                            android.text.TextUtils.join(", ",
                                                    accommodationDetailsText));
                                })
        );

        propertySizeTextView.setOnItemClickListener((parent, view, position, id) -> {
            parentFormWizard.getConfigurationItemId("property-size",
                    parent.getItemAtPosition(position).toString(), (configuration, type) -> {
                        formData.putString("propertySizeId", configuration.configurationId);
                        formData.putString("propertySizeText", configuration.description);
                    });
        });

        return v;
    }

    /**
     * Called whenever the wizard proceeds to the next step or goes back to the previous step
     */
    private void bindDataFields() {
        formData.putString("plotSize", plotSizeEditText.getText().toString());
        formData.putString("whenTitleAcquired", whenTitleAcquiredEditText.getText().toString());

        isComplete = formData.get("roadInfrastructureQualityIds") != null &&
                formData.get("hasTitle") != null &&
                formData.get("plotSize") != null &&
                formData.get("landUseIds") != null &&
                formData.get("propertySizeId") != null &&
                formData.get("accommodationDetailsIds") != null;
        intent.putExtras(formData);
    }

    private void displayBoundDataFields() {
        if (formData != null) {
            if (formData.get("roadInfrastructureQualityText") != null) {
                roadInfrastructureQualityEditText.
                        setText(Objects.requireNonNull(
                                formData.get("roadInfrastructureQualityText")).toString());
            }

            if (formData.get("hasTitle") != null) {
                hasTitleTextView.
                        setText(Objects.requireNonNull(
                                formData.get("hasTitle")).toString(), false);

                if (Objects.equals(formData.getString("hasTitle"), "Yes")) {
                    whenTitleAcquiredInputLayout.setVisibility(View.VISIBLE);
                }
            }

            if (formData.get("plotSize") != null) {
                plotSizeEditText.
                        setText(Objects.requireNonNull(
                                formData.get("plotSize")).toString());
            }

            if (formData.get("whenTitleAcquired") != null) {
                whenTitleAcquiredEditText.
                        setText(Objects.requireNonNull(
                                formData.get("whenTitleAcquired")).toString());
            }

            if (formData.get("landUseText") != null) {
                landUseEditText.
                        setText(Objects.requireNonNull(
                                formData.get("landUseText")).toString());
            }

            if (formData.get("propertySizeText") != null) {
                propertySizeTextView.
                        setText(Objects.requireNonNull(
                                formData.get("propertySizeText")).toString());
            }

            if (formData.get("accommodationDetailsText") != null) {
                accommodationDetailsEditText.
                        setText(Objects.requireNonNull(
                                formData.get("accommodationDetailsText")).toString());
            }
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
