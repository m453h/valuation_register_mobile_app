package com.aru.valuationregister.ValuationRegister;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.aru.valuationregister.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;


public class FormStepFive extends Fragment {

    private boolean isComplete;

    private ParentFormWizard parentActivity;

    private AutoCompleteTextView landAcquisitionSourceOfFundsTextView;
    private TextInputLayout otherSourceOfFundsInputLayout;
    private EditText otherSourceOfFundsEditText;
    private AutoCompleteTextView landAcquisitionSourceOfInfoTextView;
    private TextInputLayout otherSourceOfInfoInputLayout;
    private EditText otherSourceOfInfoEditText;
    private AutoCompleteTextView awareOfSaleTextView;
    private AutoCompleteTextView typeOfSaleTextView;
    private EditText sellingPriceEditText;
    private LinearLayout saleInNeighborhoodLayout;
    private Intent intent;

    private Bundle formData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.form_step_five, container, false);

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

        // Initialize form widgets
        initializeSourceOfFundsWidget(v);
        initializeSourceOfInfoWidget(v);
        initializeAwareOfPropertySaleWidget(v);
        initializePropertySaleTypeWidget(v);
        initializeEditTexts(v);

        // Set persisted form data to current UI elements
        displayBoundDataFields();

        return v;
    }

    private void initializeSourceOfFundsWidget(View v) {
        landAcquisitionSourceOfFundsTextView = v.
                findViewById(R.id.land_acquisition_source_of_funds_text_view);
        otherSourceOfFundsInputLayout = v.
                findViewById(R.id.other_source_of_funds_for_land_acquisition_input_layout);
        parentActivity.initializeDropdownLists(landAcquisitionSourceOfFundsTextView,
                "land-acquisition-funds-source");
        landAcquisitionSourceOfFundsTextView
                .setOnItemClickListener((parent, view, position, id) -> {
            parentActivity.getConfigurationItemId("land-acquisition-funds-source",
                    parent.getItemAtPosition(position).toString(), (configuration, type) -> {
                        formData.putString("landAcquisitionFundSourceId",
                                configuration.configurationId);
                        formData.putString("landAcquisitionFundSourceText",
                                configuration.description);
                    });
            if (parent.getItemAtPosition(position).toString().equals("Any Other")) {
                otherSourceOfFundsInputLayout.setVisibility(View.VISIBLE);
            } else {
                otherSourceOfFundsInputLayout.setVisibility(View.GONE);
            }
        });
    }

    private void initializeSourceOfInfoWidget(View v) {
        landAcquisitionSourceOfInfoTextView = v.
                findViewById(R.id.land_acquisition_source_of_info_text_view);
        otherSourceOfInfoInputLayout = v.
                findViewById(R.id.other_source_of_info_for_land_acquisition_input_layout);
        parentActivity.initializeDropdownLists(landAcquisitionSourceOfInfoTextView,
                "land-acquisition-information-source");
        landAcquisitionSourceOfInfoTextView
                .setOnItemClickListener((parent, view, position, id) -> {
                    parentActivity.getConfigurationItemId("land-acquisition-information-source",
                            parent.getItemAtPosition(position).toString(), (configuration, type) -> {
                                formData.putString("landAcquisitionSourceOfInfoId",
                                        configuration.configurationId);
                                formData.putString("landAcquisitionSourceOfInfoText",
                                        configuration.description);
                            });
                    if (parent.getItemAtPosition(position).toString().equals("Any Other")) {
                        otherSourceOfInfoInputLayout.setVisibility(View.VISIBLE);
                    } else {
                        otherSourceOfInfoInputLayout.setVisibility(View.GONE);
                    }
                });
    }

    private void initializeAwareOfPropertySaleWidget(View v) {
        awareOfSaleTextView = v.findViewById(R.id.aware_of_sale_text_view);
        saleInNeighborhoodLayout = v.findViewById(R.id.sale_in_neighborhood);
        parentActivity.initializeSimpleDropdownLists(awareOfSaleTextView,
                new String[]{"Yes", "No"});
        awareOfSaleTextView.
                setOnItemClickListener((parent, view, position, id) -> {
                    formData.putString("awareOfSale",
                            parent.getItemAtPosition(position).toString());
                    if (parent.getItemAtPosition(position).toString().equals("Yes")){
                        saleInNeighborhoodLayout.setVisibility(View.VISIBLE);
                    } else {
                        saleInNeighborhoodLayout.setVisibility(View.GONE);
                    }
                });
    }

    private void initializePropertySaleTypeWidget(View v) {
        typeOfSaleTextView = v.
                findViewById(R.id.type_of_sale_text_view);
        parentActivity.initializeDropdownLists(typeOfSaleTextView,
                "property-sale-type");
        typeOfSaleTextView
                .setOnItemClickListener((parent, view, position, id) -> {
                    parentActivity.getConfigurationItemId("property-sale-type",
                            parent.getItemAtPosition(position).toString(), (configuration, type) -> {
                                formData.putString("propertySaleTypeId",
                                        configuration.configurationId);
                                formData.putString("propertySaleTypeText",
                                        configuration.description);
                            });
                });
    }

    private void initializeEditTexts(View v) {
        otherSourceOfFundsEditText = v.
                findViewById(R.id.other_source_of_funds_for_land_acquisition_edit_text);
        otherSourceOfInfoEditText = v.
                findViewById(R.id.other_land_acquisition_source_of_info_edit_text);
        sellingPriceEditText = v.
                findViewById(R.id.selling_price_edit_text);
    }

    /**
     * Called whenever the wizard proceeds to the next step or goes back to the previous step
     */
    private void bindDataFields() {
        formData.putString("otherSourceOfFunds", otherSourceOfFundsEditText.
                getText().toString());
        formData.putString("otherSourceOfInfo", otherSourceOfInfoEditText.
                getText().toString());
        formData.putString("sellingPrice", sellingPriceEditText.
                getText().toString());
        isComplete = formData.get("landAcquisitionFundSourceId") != null &&
                formData.get("landAcquisitionSourceOfInfoId") != null &&
                formData.get("awareOfSale") != null;
        intent.putExtras(formData);
    }

    private void displayBoundDataFields() {
        if (formData != null) {
            parentActivity.setBoundWidgetData(landAcquisitionSourceOfFundsTextView ,
                    formData.getString("landAcquisitionFundSourceText", null));

            parentActivity.setBoundWidgetData(otherSourceOfFundsEditText ,
                    formData.getString("otherSourceOfFunds", null));

            parentActivity.setBoundWidgetData(landAcquisitionSourceOfInfoTextView ,
                    formData.getString("landAcquisitionSourceOfInfoText", null));

            parentActivity.setBoundWidgetData(otherSourceOfInfoEditText ,
                    formData.getString("otherSourceOfInfo", null));

            parentActivity.setBoundWidgetData(awareOfSaleTextView ,
                    formData.getString("awareOfSale", null));

            parentActivity.setBoundWidgetData(typeOfSaleTextView ,
                    formData.getString("propertySaleTypeText", null));

            parentActivity.setBoundWidgetData(sellingPriceEditText ,
                    formData.getString("sellingPrice", null));

            if (formData.getString("landAcquisitionFundSourceText") !=null) {
                if (Objects.equals(formData.
                        getString("landAcquisitionFundSourceText"), "Any Other")) {
                    otherSourceOfFundsInputLayout.setVisibility(View.VISIBLE);
                }
            }

            if (formData.getString("landAcquisitionSourceOfInfoText") !=null) {
                if (Objects.equals(formData.
                        getString("landAcquisitionSourceOfInfoText"), "Any Other")) {
                    otherSourceOfInfoInputLayout.setVisibility(View.VISIBLE);
                }
            }

            if (formData.getString("awareOfSale") !=null) {
                if (Objects.equals(formData.
                        getString("awareOfSale"), "Yes")) {
                    saleInNeighborhoodLayout.setVisibility(View.VISIBLE);
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
