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

    private ParentFormWizard parentActivity;
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

        //Set the intent used to persist data across fragments
        intent = requireActivity().getIntent();
        formData = intent.getExtras();

        if (formData == null) {
            formData = new Bundle();
        }

        // Get the instance of the Parent form wizard with form building helpers
        parentActivity = (ParentFormWizard) getActivity();
        assert parentActivity != null;

        isComplete = false;

        initializeRoadInfrastructureQualityWidget(v);
        initializeAccommodationDetailsWidget(v);
        initializeLandUseWidget(v);
        initializePropertySize(v);
        initializeHasTitleWidget(v);
        initializeEditTexts(v);

        // Set persisted form data to current UI elements
        displayBoundDataFields();

        return v;
    }

    private void initializeRoadInfrastructureQualityWidget(View v) {
        roadInfrastructureQualityIds = new ArrayList<>();
        roadInfrastructureQualityText = new ArrayList<>();
        roadInfrastructureQualityEditText = v.findViewById(R.id.road_infrastructure_quality);
        roadInfrastructureQualityEditText.setOnClickListener(v1 ->
                parentActivity.
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
    }

    private void initializeAccommodationDetailsWidget(View v) {
        accommodationDetailsIds = new ArrayList<>();
        accommodationDetailsText = new ArrayList<>();
        accommodationDetailsEditText = v.findViewById(R.id.accommodation_details_edit_text);
        accommodationDetailsEditText.setOnClickListener(v1 ->
                parentActivity.
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
    }

    private void initializeLandUseWidget(View v) {
        landUseIds = new ArrayList<>();
        landUseText = new ArrayList<>();
        landUseEditText = v.findViewById(R.id.land_use_edit_text);
        landUseEditText.setOnClickListener(v1 ->
                parentActivity.
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

    }

    private void initializePropertySize(View v) {
        propertySizeTextView = v.findViewById(R.id.property_size_text_view);
        parentActivity.initializeDropdownLists(propertySizeTextView, "property-size");
        propertySizeTextView.setOnItemClickListener((parent, view, position, id) -> {
            parentActivity.getConfigurationItemId("property-size",
                    parent.getItemAtPosition(position).toString(), (configuration, type) -> {
                        formData.putString("propertySizeId", configuration.configurationId);
                        formData.putString("propertySizeText", configuration.description);
                    });
        });
    }

    private void initializeHasTitleWidget(View v) {
        hasTitleTextView = v.findViewById(R.id.has_title_text_view);
        whenTitleAcquiredInputLayout = v.findViewById(R.id.when_was_title_acquired_input_layout);
        parentActivity.initializeSimpleDropdownLists(hasTitleTextView,
                new String[]{"Yes", "No"});
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
    }

    private void initializeEditTexts(View v) {
        whenTitleAcquiredEditText = v.findViewById(R.id.when_was_title_acquired_edit_text);
        plotSizeEditText = v.findViewById(R.id.plot_size_edit_text);
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

            parentActivity.setBoundWidgetData(roadInfrastructureQualityEditText,
                    formData.getString("roadInfrastructureQualityText", null));

            parentActivity.setBoundWidgetData(hasTitleTextView,
                    formData.getString("hasTitle", null));

            parentActivity.setBoundWidgetData(plotSizeEditText,
                    formData.getString("plotSize", null));

            parentActivity.setBoundWidgetData(whenTitleAcquiredEditText,
                    formData.getString("whenTitleAcquired", null));

            parentActivity.setBoundWidgetData(landUseEditText,
                    formData.getString("landUseText", null));

            parentActivity.setBoundWidgetData(propertySizeTextView,
                    formData.getString("propertySizeText", null));

            parentActivity.setBoundWidgetData(accommodationDetailsEditText,
                    formData.getString("accommodationDetailsText", null));

            if (formData.get("hasTitle") != null) {
                if (Objects.equals(formData.getString("hasTitle"), "Yes")) {
                    whenTitleAcquiredInputLayout.setVisibility(View.VISIBLE);
                }
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
