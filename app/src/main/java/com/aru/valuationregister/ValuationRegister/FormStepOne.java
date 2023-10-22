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


public class FormStepOne extends Fragment {

    private boolean isComplete;
    private AutoCompleteTextView streetTextView;
    private AutoCompleteTextView landPlotsTextView;
    private AutoCompleteTextView isNeighbourOfValuableProjectTextView;
    private TextInputLayout roadNameInputLayout;
    private TextInputLayout projectNameInputLayout;
    private EditText notableLandmarksEditText;
    private EditText accessibilityRoadNameEditText;
    private EditText propertyAccessibilityEditText;
    private EditText valuableProjectNameEditText;
    private ParentFormWizard parentActivity;
    private Intent intent;
    private List<String> propertyAccessibilityIds;
    private List<String> getPropertyAccessibilityText;
    private Bundle formData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.form_step_one, container, false);

        //Set the intent used to persist data across fragments
        intent = requireActivity().getIntent();
        formData = intent.getExtras();

        if (formData == null) {
            formData = new Bundle();
        }

        parentActivity = (ParentFormWizard) getActivity();
        assert parentActivity != null;

        isComplete = false;

        // Initialize form widgets
        initializeStreetWidget(v);
        initializeLandPlotsWidget(v);
        initializePropertyAccessibilityWidget(v);
        initializeIsNeighbourOfValuableProjectWidget(v);
        initializeEditTexts(v);

        // Set persisted form data to current UI elements
        displayBoundDataFields();
        return v;
    }

    private void initializeEditTexts(View v) {
        notableLandmarksEditText = v.findViewById(R.id.notable_landmarks);
        accessibilityRoadNameEditText = v.findViewById(R.id.accessibility_road_name);
        valuableProjectNameEditText = v.findViewById(R.id.valuable_project_name_edit_text);
    }
    private void initializeStreetWidget(View v) {
        streetTextView = v.findViewById(R.id.street_text_view);
        parentActivity.initializeDropdownLists(streetTextView, "villages-and-streets");
        streetTextView.setOnItemClickListener((parent, view, position, id) -> {
            parentActivity.getConfigurationItemId("villages-and-streets",
                    parent.getItemAtPosition(position).toString(), (configuration, type) -> {
                        formData.putString("villageStreetId", configuration.configurationId);
                        formData.putString("villageStreetText", configuration.description);
                    });
        });
    }

    private void initializeLandPlotsWidget(View v) {
        landPlotsTextView = v.findViewById(R.id.land_plots_text_view);
        parentActivity.initializeDropdownLists(landPlotsTextView, "land-plots");
        landPlotsTextView.setOnItemClickListener((parent, view, position, id) -> {
            parentActivity.getConfigurationItemId("land-plots",
                    parent.getItemAtPosition(position).toString(), (configuration, type) -> {
                        formData.putString("landPlotId", configuration.configurationId);
                        formData.putString("landPlotText", configuration.description);
                    });
        });
    }
    private void initializePropertyAccessibilityWidget(View v) {
        propertyAccessibilityIds = new ArrayList<>();
        getPropertyAccessibilityText = new ArrayList<>();
        propertyAccessibilityEditText = v.findViewById(R.id.property_accessibility_edit_text);
        roadNameInputLayout = v.findViewById(R.id.accessibility_road_name_input_layout);
        propertyAccessibilityEditText.setOnClickListener(v1 ->
                parentActivity.
                        getDialogSelector(propertyAccessibilityEditText,
                                "property-accessibility",
                                R.string.property_accessibility, (configuration, type) -> {
                                    if (type.equals("checked")) {
                                        propertyAccessibilityIds.
                                                add(configuration.configurationId);
                                        getPropertyAccessibilityText.
                                                add(configuration.description);
                                        if (configuration.description.equals("Through Roads"))
                                            roadNameInputLayout.setVisibility(View.VISIBLE);
                                    } else if (type.equals("unchecked")) {
                                        propertyAccessibilityIds.
                                                remove(configuration.configurationId);
                                        getPropertyAccessibilityText.
                                                remove(configuration.description);
                                        if (configuration.description.equals("Through Roads"))
                                            roadNameInputLayout.setVisibility(View.GONE);
                                    }
                                    formData.putString("propertyAccessibilityIds",
                                            android.text.TextUtils.join(",",
                                                    propertyAccessibilityIds));

                                    formData.putString("propertyAccessibilityText",
                                            android.text.TextUtils.join(", ",
                                                    getPropertyAccessibilityText));
                                })
        );
    }

    private void initializeIsNeighbourOfValuableProjectWidget(View v) {
        isNeighbourOfValuableProjectTextView = v.
                findViewById(R.id.is_neighbour_of_valuable_project_text_view);
        projectNameInputLayout = v.findViewById(R.id.valuable_project_name_input_layout);
        parentActivity.initializeSimpleDropdownLists(isNeighbourOfValuableProjectTextView,
                new String[]{"Yes", "No"});
        isNeighbourOfValuableProjectTextView.
                setOnItemClickListener((parent, view, position, id) -> {

                    if (parent.getItemAtPosition(position).toString().equals("Yes"))
                        projectNameInputLayout.setVisibility(View.VISIBLE);
                    else
                        projectNameInputLayout.setVisibility(View.GONE);

                    formData.putString("isNeighbourOfValuableProject",
                            parent.getItemAtPosition(position).toString());
                });
    }

    /**
     * Called whenever the wizard proceeds to the next step or goes back to the previous step
     */
    private void bindDataFields() {
        formData.putString("notableLandmarks", notableLandmarksEditText.getText().toString());
        formData.putString("accessibilityRoadName", accessibilityRoadNameEditText.
                getText().toString());
        formData.putString("valuableProjectName", valuableProjectNameEditText.
                getText().toString());

        isComplete = formData.get("villageStreetId") != null &&
                formData.get("landPlotId") != null &&
                formData.get("notableLandmarks") != null &&
                formData.get("propertyAccessibilityIds") != null;
        intent.putExtras(formData);
    }

    private void displayBoundDataFields() {
        if (formData != null) {

            parentActivity.setBoundWidgetData(propertyAccessibilityEditText ,
                    formData.getString("propertyAccessibilityText", null));

            parentActivity.setBoundWidgetData(streetTextView ,
                    formData.getString("villageStreetText", null));

            parentActivity.setBoundWidgetData(landPlotsTextView ,
                    formData.getString("landPlotText", null));

            parentActivity.setBoundWidgetData(notableLandmarksEditText ,
                    formData.getString("notableLandmarks", null));

            parentActivity.setBoundWidgetData(accessibilityRoadNameEditText ,
                    formData.getString("accessibilityRoadName", null));

            parentActivity.setBoundWidgetData(isNeighbourOfValuableProjectTextView ,
                    formData.getString("isNeighbourOfValuableProject", null));

            parentActivity.setBoundWidgetData(valuableProjectNameEditText ,
                    formData.getString("valuableProjectName", null));

            if (formData.getString("isNeighbourOfValuableProject") !=null) {
                if (Objects.equals(formData.get("isNeighbourOfValuableProject"), "Yes")) {
                    projectNameInputLayout.setVisibility(View.VISIBLE);
                }
            }

            if (formData.getString("propertyAccessibilityText") !=null) {
                if (Objects.requireNonNull(formData.get("propertyAccessibilityText"))
                        .toString().contains("Through Roads")) {
                    roadNameInputLayout.setVisibility(View.VISIBLE);
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
