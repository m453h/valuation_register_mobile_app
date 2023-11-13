package com.aru.valuationregister.MenuActivities;


import com.aru.valuationregister.R;

/**
 * Created by michael.nkotagu on 6/18/2015.
 */
public class MenuBuilder {
    public String[] nameArray;
    public String[] descriptionArray;
    public int[] drawableArray;


    public MenuBuilder(String selection) {
        if (selection.equals("MainMenuActivity"))
        {
            this.nameArray = new String[5];
            this.descriptionArray = new String[5];
            this.drawableArray = new int[5];

            this.nameArray[0] = "Browser existing data";
            this.nameArray[1] = "Create a new Record";
            this.nameArray[2] = "Fetch Updates";
            this.nameArray[3] = "Upload Data";
            this.nameArray[4] = "Settings";

            this.descriptionArray[0] = "Add a plot or property valuation record";
            this.descriptionArray[1] = "Add a plot or property valuation record";
            this.descriptionArray[2] = "Download application data";
            this.descriptionArray[3] = "Upload offline data";
            this.descriptionArray[4] = "Change your Device Configurations";

            this.drawableArray[0] = R.drawable.menu_data;
            this.drawableArray[1] = R.drawable.menu_create;
            this.drawableArray[2] = R.drawable.menu_download;
            this.drawableArray[3] = R.drawable.menu_upload;
            this.drawableArray[4] = R.drawable.menu_settings;
        }
    }
}
