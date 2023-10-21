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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FormStepOne extends Fragment {

    private boolean isComplete;

    private AutoCompleteTextView streetTextView;

    private AutoCompleteTextView landPlotsTextView;
    private EditText propertyAccessibilityEditText;
    private ParentFormWizard parentFormWizard;

    private Intent intent;

    private List<String> propertyAccessibilityIds;
    private List<String> getPropertyAccessibilityText;

    private Bundle formData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.form_step_1, container, false);

        // Get ui form widgets
        streetTextView = v.findViewById(R.id.streetTextView);
        landPlotsTextView = v.findViewById(R.id.landPlotsTextView);
        propertyAccessibilityEditText = v.findViewById(R.id.propertyAccessibility);

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
        parentFormWizard.addToCurrentViews("villages-and-streets", streetTextView);
        parentFormWizard.addToCurrentViews("land-plots", landPlotsTextView);
        isComplete = false;

        // Set persisted form data to current UI elements
        displayBoundDataFields();


        //An array of widget keys with dropdown lists
        String[] configurations = {"villages-and-streets",
                "land-plots",
        };

        // Initialize dropdown lists with
        for (String config : configurations) {
            parentFormWizard.initializeDropdownLists(config);
        }

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
                        getDialogSelector(propertyAccessibilityEditText,
                                "property-accessibility", (configuration, type) -> {
                                    if (type.equals("checked")) {
                                        propertyAccessibilityIds.
                                                add(configuration.configurationId);
                                        getPropertyAccessibilityText.
                                                add(configuration.description);
                                    } else if (type.equals("unchecked")) {
                                        propertyAccessibilityIds.
                                                remove(configuration.configurationId);
                                        getPropertyAccessibilityText.
                                                remove(configuration.description);
                                    }
                                    formData.putString("propertyAccessibilityIds",
                                            android.text.TextUtils.join(",",
                                                    propertyAccessibilityIds));

                                    formData.putString("propertyAccessibilityText",
                                            android.text.TextUtils.join(", ",
                                                    getPropertyAccessibilityText));
                                })
        );


        return v;
    }

    /**
     * Called whenever the wizard proceeds to the next step or goes back to the previous step
     */
    private void bindDataFields() {

        isComplete = formData.get("villageStreetId") != null &&
                formData.get("landPlotId") != null &&
                formData.get("propertyAccessibilityIds") != null;

        intent.putExtras(formData);
    }

    private void displayBoundDataFields() {
        if (formData != null) {
            if (formData.get("propertyAccessibilityText") != null) {
                propertyAccessibilityEditText.
                        setText(Objects.requireNonNull(
                                formData.get("propertyAccessibilityText")).toString());
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
