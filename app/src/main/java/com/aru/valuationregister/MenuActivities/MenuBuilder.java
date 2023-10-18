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
            this.nameArray = new String[4];
            this.descriptionArray = new String[4];
            this.drawableArray = new int[4];

            this.nameArray[0] = "Create a new Record";
            this.nameArray[1] = "Fetch Updates";
            this.nameArray[2] = "Upload Data";
            this.nameArray[3] = "Settings";

            this.descriptionArray[0] = "Add a plot or property valuation record";
            this.descriptionArray[1] = "Download application data";
            this.descriptionArray[2] = "Upload offline data";
            this.descriptionArray[3] = "Change your Device Configurations";

            this.drawableArray[0] = R.drawable.menu_create;
            this.drawableArray[1] = R.drawable.menu_download;
            this.drawableArray[2] = R.drawable.menu_upload;
            this.drawableArray[3] = R.drawable.menu_settings;
        }
    }
}
