package com.aru.valuationregister.ValuationRegister;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.aru.valuationregister.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FormStepThree extends Fragment {

    private boolean isComplete;

    private ParentFormWizard parentActivity;

    private Intent intent;

    private AutoCompleteTextView landDevelopmentStatusTextView;

    private EditText numberOfBuildingsEditText;
    private EditText servantQuarterSizeEditText;

    private EditText buildingTypeEditText;
    private List<String> buildingTypeIds;
    private List<String> buildingTypeText;
    private AutoCompleteTextView propertyConditionEditText;
    private AutoCompleteTextView hasMortgageTextView;
    private AutoCompleteTextView mortgageAmountTextView;

    private TextInputLayout mortgageAmountInputLayout;

    private TextInputLayout servantQuarterSizeInputLayout;

    private Bundle formData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.form_step_three, container, false);

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


        // Initialize widgets
        initializeLandDevelopmentStatusWidget(v);
        initializeBuildingTypesWidget(v);
        initializePropertyConditionWidget(v);
        initializeHasMortgageWidget(v);
        initializeMortgageAmountWidget(v);
        initializeEditTexts(v);

        // Set persisted form data to current UI elements
        displayBoundDataFields();

        return v;
    }

    private void initializeLandDevelopmentStatusWidget(View v) {
        landDevelopmentStatusTextView = v.findViewById(R.id.is_land_developed_text_view);
        parentActivity.initializeSimpleDropdownLists(landDevelopmentStatusTextView,
                new String[]{"Developed", "Undeveloped"});
        landDevelopmentStatusTextView.
                setOnItemClickListener((parent, view, position, id) -> {
                    formData.putString("landDevelopmentStatus",
                            parent.getItemAtPosition(position).toString());
                });
    }

    private void initializeBuildingTypesWidget(View v) {
        buildingTypeIds = new ArrayList<>();
        buildingTypeText = new ArrayList<>();
        buildingTypeEditText = v.findViewById(R.id.building_types_edit_text);
        servantQuarterSizeInputLayout = v.findViewById(R.id.servant_quarter_size_input_layout);

        buildingTypeEditText.setOnClickListener(v1 ->
                parentActivity.
                        getDialogSelector(buildingTypeEditText,
                                "building-types",
                                R.string.building_types, (configuration, type) -> {
                                    if (type.equals("checked")) {
                                        buildingTypeIds.
                                                add(configuration.configurationId);
                                        buildingTypeText.
                                                add(configuration.description);

                                        if (configuration.description.
                                                equals("Servant Quarter (outbuilding)")) {
                                            servantQuarterSizeInputLayout.
                                                    setVisibility(View.VISIBLE);
                                        }
                                    } else if (type.equals("unchecked")) {
                                        buildingTypeIds.
                                                remove(configuration.configurationId);
                                        buildingTypeText.
                                                remove(configuration.description);

                                        if (configuration.description.
                                                equals("Servant Quarter (outbuilding)")) {
                                            servantQuarterSizeInputLayout.
                                                    setVisibility(View.GONE);
                                        }
                                    }
                                    formData.putString("buildingTypeIds",
                                            android.text.TextUtils.join(",",
                                                    buildingTypeIds));
                                    formData.putString("buildingTypeText",
                                            android.text.TextUtils.join(", ",
                                                    buildingTypeText));
                                })
        );
    }

    private void initializePropertyConditionWidget(View v) {
        propertyConditionEditText = v.findViewById(R.id.property_condition_text_view);
        parentActivity.initializeDropdownLists(propertyConditionEditText,
                "property-condition");

        propertyConditionEditText.setOnItemClickListener((parent, view, position, id) -> {
            parentActivity.getConfigurationItemId("property-condition",
                    parent.getItemAtPosition(position).toString(), (configuration, type) -> {
                        formData.putString("propertyConditionId", configuration.configurationId);
                        formData.putString("propertyConditionText", configuration.description);
                    });
        });
    }

    private void initializeHasMortgageWidget(View v) {
        hasMortgageTextView = v.findViewById(R.id.has_mortgage_text_view);
        mortgageAmountInputLayout = v.findViewById(R.id.mortgage_amount_input_layout);
        parentActivity.initializeSimpleDropdownLists(hasMortgageTextView,
                new String[]{"Yes", "No"});
        hasMortgageTextView.
                setOnItemClickListener((parent, view, position, id) -> {
                    formData.putString("hasMortgage",
                            parent.getItemAtPosition(position).toString());

                    if (parent.getItemAtPosition(position).toString().equals("Yes")){
                        mortgageAmountInputLayout.setVisibility(View.VISIBLE);
                    } else {
                        mortgageAmountInputLayout.setVisibility(View.GONE);
                    }
                });
    }

    private void initializeMortgageAmountWidget(View v) {
        mortgageAmountTextView = v.findViewById(R.id.mortgage_amount_text_view);
        parentActivity.initializeDropdownLists(mortgageAmountTextView,
                "mortgage-credit-facility");
        mortgageAmountTextView.setOnItemClickListener((parent, view, position, id) -> {
            parentActivity.getConfigurationItemId("mortgage-credit-facility",
                    parent.getItemAtPosition(position).toString(), (configuration, type) -> {
                        formData.putString("mortgageCreditFacilityId", configuration.configurationId);
                        formData.putString("mortgageCreditFacilityText", configuration.description);
                    });
        });
    }

    private void initializeEditTexts(View v) {
        numberOfBuildingsEditText = v.findViewById(R.id.number_of_buildings_edit_text);
        servantQuarterSizeEditText = v.findViewById(R.id.servant_quarter_size_edit_text);
    }

    /**
     * Called whenever the wizard proceeds to the next step or goes back to the previous step
     */
    private void bindDataFields() {

        formData.putString("numberOfBuildings", numberOfBuildingsEditText.getText().toString());
        formData.putString("servantQuarterSize", servantQuarterSizeEditText.getText().toString());

        isComplete = formData.get("landDevelopmentStatus") != null &&
                formData.get("numberOfBuildings") != null &&
                formData.get("buildingTypeIds") != null &&
                formData.get("propertyConditionId") != null &&
                formData.get("hasMortgage") != null;
        intent.putExtras(formData);
        isComplete = true;
    }

    private void displayBoundDataFields() {
        if (formData != null) {
            parentActivity.setBoundWidgetData(landDevelopmentStatusTextView ,
                    formData.getString("landDevelopmentStatus", null));

            parentActivity.setBoundWidgetData(numberOfBuildingsEditText ,
                    formData.getString("numberOfBuildings", null));

            parentActivity.setBoundWidgetData(buildingTypeEditText ,
                    formData.getString("buildingTypeText", null));

            parentActivity.setBoundWidgetData(servantQuarterSizeEditText ,
                    formData.getString("servantQuarterSize", null));

            parentActivity.setBoundWidgetData(propertyConditionEditText ,
                    formData.getString("propertyConditionText", null));

            parentActivity.setBoundWidgetData(hasMortgageTextView ,
                    formData.getString("hasMortgage", null));

            parentActivity.setBoundWidgetData(mortgageAmountTextView ,
                    formData.getString("mortgageCreditFacilityText", null));

            if (formData.getString("hasMortgage") !=null) {
                if (Objects.equals(formData.
                        getString("hasMortgage"), "Yes")) {
                    mortgageAmountInputLayout.setVisibility(View.VISIBLE);
                }
            }

            if (formData.getString("buildingTypeText") !=null) {
                if (Objects.requireNonNull(formData.getString("buildingTypeText"))
                        .contains("Servant Quarter (outbuilding)")) {
                    servantQuarterSizeInputLayout.setVisibility(View.VISIBLE);
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
