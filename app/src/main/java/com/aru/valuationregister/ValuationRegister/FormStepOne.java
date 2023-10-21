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
    private ParentFormWizard parentFormWizard;

    private Intent intent;

    private List<String> propertyAccessibilityIds;
    private List<String> getPropertyAccessibilityText;

    private Bundle formData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.form_step_one, container, false);

        // Get ui form widgets
        streetTextView = v.findViewById(R.id.street_text_view);
        landPlotsTextView = v.findViewById(R.id.land_plots_text_view);
        propertyAccessibilityEditText = v.findViewById(R.id.property_accessibility_edit_text);
        isNeighbourOfValuableProjectTextView = v.findViewById(R.id.is_neighbour_of_valuable_project_text_view);
        roadNameInputLayout = v.findViewById(R.id.accessibility_road_name_input_layout);
        projectNameInputLayout = v.findViewById(R.id.valuable_project_name_input_layout);
        notableLandmarksEditText = v.findViewById(R.id.notable_landmarks);
        accessibilityRoadNameEditText = v.findViewById(R.id.accessibility_road_name);
        valuableProjectNameEditText = v.findViewById(R.id.valuable_project_name_edit_text);

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

        parentFormWizard.initializeDropdownLists(streetTextView, "villages-and-streets");
        parentFormWizard.initializeDropdownLists(landPlotsTextView, "land-plots");


        parentFormWizard.initializeSimpleDropdownLists(isNeighbourOfValuableProjectTextView,
                new String[]{"Yes", "No"});

        // Set a listener for village-streets dropdown item to get selected id
        streetTextView.setOnItemClickListener((parent, view, position, id) -> {
            parentFormWizard.getConfigurationItemId("villages-and-streets",
                    parent.getItemAtPosition(position).toString(), (configuration, type) -> {
                        formData.putString("villageStreetId", configuration.configurationId);
                        formData.putString("villageStreetText", configuration.description);
                    });
        });

        // Set a listener for land-plots dropdown item to get selected id
        landPlotsTextView.setOnItemClickListener((parent, view, position, id) -> {
            parentFormWizard.getConfigurationItemId("land-plots",
                    parent.getItemAtPosition(position).toString(), (configuration, type) -> {
                        formData.putString("landPlotId", configuration.configurationId);
                        formData.putString("landPlotText", configuration.description);
                    });
        });

        // Set a listener for multiple property accessibility selected from dialog interface
        propertyAccessibilityIds = new ArrayList<>();
        getPropertyAccessibilityText = new ArrayList<>();

        propertyAccessibilityEditText.setOnClickListener(v1 ->
                parentFormWizard.
                        getDialogSelector(propertyAccessibilityEditText, "property-accessibility",
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

        // Set a listener for is neighbour of valuable project dropdown item to get selected item
        isNeighbourOfValuableProjectTextView.
                setOnItemClickListener((parent, view, position, id) -> {

                    if (parent.getItemAtPosition(position).toString().equals("Yes"))
                        projectNameInputLayout.setVisibility(View.VISIBLE);
                    else
                        projectNameInputLayout.setVisibility(View.GONE);

                    formData.putString("isNeighbourOfValuableProject",
                            parent.getItemAtPosition(position).toString());
                });

        return v;
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
            if (formData.get("propertyAccessibilityText") != null) {
                propertyAccessibilityEditText.
                        setText(Objects.requireNonNull(
                                formData.get("propertyAccessibilityText")).toString());
                if (Objects.requireNonNull(formData.get("propertyAccessibilityText"))
                        .toString().contains("Through Roads")) {
                    roadNameInputLayout.setVisibility(View.VISIBLE);
                }
            }

            if (formData.get("villageStreetText") != null) {
                streetTextView.
                        setText(Objects.requireNonNull(
                                formData.get("villageStreetText")).toString(), false);
            }

            if (formData.get("landPlotText") != null) {
                landPlotsTextView.
                        setText(Objects.requireNonNull(
                                formData.get("landPlotText")).toString(), false);
            }

            if (formData.get("notableLandmarks") != null) {
                notableLandmarksEditText.
                        setText(Objects.requireNonNull(
                                formData.get("notableLandmarks")).toString());
            }

            if (formData.get("accessibilityRoadName") != null) {
                accessibilityRoadNameEditText.
                        setText(Objects.requireNonNull(
                                formData.get("accessibilityRoadName")).toString());
            }

            if (formData.get("isNeighbourOfValuableProject") != null) {
                isNeighbourOfValuableProjectTextView.
                        setText(Objects.requireNonNull(
                                formData.get("isNeighbourOfValuableProject")).toString(), false);

                if (Objects.equals(formData.get("isNeighbourOfValuableProject"), "Yes")) {
                    projectNameInputLayout.setVisibility(View.VISIBLE);
                }
            }

            if (formData.get("valuableProjectName") != null) {
                valuableProjectNameEditText.
                        setText(Objects.requireNonNull(
                                formData.get("valuableProjectName")).toString());
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
