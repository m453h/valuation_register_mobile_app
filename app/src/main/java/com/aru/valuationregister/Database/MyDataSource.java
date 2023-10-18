package com.aru.valuationregister.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.aru.valuationregister.Rest.Action;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@SuppressWarnings("WeakerAccess")
public class MyDataSource {

    private SQLiteDatabase db;
    private MySQLiteHelper dbHelper;
    private Cursor cursor;
    private String TAG = "VALUATION_DB";

    public MyDataSource(Context context) {
        dbHelper = MySQLiteHelper.getInstance(context);
    }

    public void open() throws SQLException {

        db = dbHelper.getWritableDatabase();

    }

    public void close() {

        dbHelper.close();

    }

    public boolean recordData(JSONArray values, String subField) {

        this.open();

        boolean status = true;

        for (int i = 0; i < values.length(); i++) {
            try {
                if (subField.equals("regions")) {
                    status = recordRegions(values.getJSONObject(i));
                } else if (subField.equals("districts")) {
                    status = recordDistricts(values.getJSONObject(i));
                } else if (subField.equals("wards")) {
                    status = recordWards(values.getJSONObject(i));
                } else if (subField.equals("courtCategories")) {
                    status = recordCourtCategories(values.getJSONObject(i));
                } else if (subField.equals("courtLevels")) {
                    status = recordCourtLevels(values.getJSONObject(i));
                } else if (subField.equals("courtBuildingStatus")) {
                    status = recordCourtBuildingStatus(values.getJSONObject(i));
                } else if (subField.equals("courtBuildingOwnershipStatus")) {
                    status = recordCourtBuildingOwnershipStatus(values.getJSONObject(i));
                } else if (subField.equals("landOwnershipStatus")) {
                    status = recordCourtLandOwnershipStatus(values.getJSONObject(i));
                } else if (subField.equals("zones")) {
                    status = recordZones(values.getJSONObject(i));
                } else if (subField.equals("economicActivities")) {
                    status = recordEconomicActivities(values.getJSONObject(i));
                } else if (subField.equals("transportModes")) {
                    status = recordTransportModes(values.getJSONObject(i));
                } else if (subField.equals("landUses")) {
                    status = recordLandUses(values.getJSONObject(i));
                } else if (subField.equals("environmentalStatus")) {
                    status = recordEnvironmentalStatus(values.getJSONObject(i));
                }


                if (!status) {
                    break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        this.close();

        return status;
    }

    private boolean recordRegions(JSONObject value) {

        boolean status = true;

        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("region_id", Action.getFieldValue(value, "region_id"));

            contentValues.put("region_name", Action.getFieldValue(value, "region_name"));

            db.insert("cfg_regions", null, contentValues);

        } catch (IllegalStateException e) {
            Log.v(TAG, "Error" + e.getMessage());

            status = false;
        }

        return status;
    }

    private boolean recordWards(JSONObject value) {

        boolean status = true;

        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("ward_id", Action.getFieldValue(value, "ward_id"));

            contentValues.put("ward_name", Action.getFieldValue(value, "ward_name"));

            contentValues.put("district_id", Action.getFieldValue(value, "district_id"));

            db.insert("cfg_wards", null, contentValues);

        } catch (IllegalStateException e) {
            Log.v(TAG, "Error" + e.getMessage());

            status = false;
        }

        return status;
    }

    private boolean recordDistricts(JSONObject value) {

        boolean status = true;

        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("district_id", Action.getFieldValue(value, "district_id"));

            contentValues.put("district_name", Action.getFieldValue(value, "district_name"));

            contentValues.put("region_id", Action.getFieldValue(value, "region_id"));

            //Log.e(TAG,Action.getFieldValue(value,"district_name")+'|'+Action.getFieldValue(value,"region_id"));

            db.insert("cfg_districts", null, contentValues);

        } catch (IllegalStateException e) {
            Log.v(TAG, "Error" + e.getMessage());

            status = false;
        }

        return status;
    }

    private boolean recordCourtLevels(JSONObject value) {

        boolean status = true;

        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("level_id", Action.getFieldValue(value, "level_id"));

            contentValues.put("description", Action.getFieldValue(value, "description"));

            db.insert("cfg_court_levels", null, contentValues);

        } catch (IllegalStateException e) {
            Log.v(TAG, "Error" + e.getMessage());

            status = false;
        }

        return status;
    }

    private boolean recordCourtCategories(JSONObject value) {

        boolean status = true;

        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("category_id", Action.getFieldValue(value, "category_id"));

            contentValues.put("category_name", Action.getFieldValue(value, "description"));

            db.insert("cfg_court_categories", null, contentValues);

        } catch (IllegalStateException e) {
            Log.v(TAG, "Error" + e.getMessage());

            status = false;
        }

        return status;
    }

    private boolean recordCourtBuildingStatus(JSONObject value) {

        boolean status = true;

        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("status_id", Action.getFieldValue(value, "status_id"));

            contentValues.put("description", Action.getFieldValue(value, "description"));

            db.insert("cfg_court_building_status", null, contentValues);

        } catch (IllegalStateException e) {
            Log.v(TAG, "Error" + e.getMessage());

            status = false;
        }

        return status;
    }

    private boolean recordCourtBuildingOwnershipStatus(JSONObject value) {

        boolean status = true;

        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("status_id", Action.getFieldValue(value, "status_id"));

            contentValues.put("description", Action.getFieldValue(value, "description"));

            db.insert("cfg_court_building_ownership_status", null, contentValues);

        } catch (IllegalStateException e) {
            Log.v(TAG, "Error" + e.getMessage());

            status = false;
        }

        return status;
    }

    private boolean recordCourtLandOwnershipStatus(JSONObject value) {

        boolean status = true;

        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("status_id", Action.getFieldValue(value, "status_id"));

            contentValues.put("description", Action.getFieldValue(value, "description"));

            db.insert("cfg_land_ownership_status", null, contentValues);

        } catch (IllegalStateException e) {
            Log.v(TAG, "Error" + e.getMessage());

            status = false;
        }

        return status;
    }

    private boolean recordZones(JSONObject value) {

        boolean status = true;

        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("zone_id", Action.getFieldValue(value, "zone_id"));

            contentValues.put("zone_name", Action.getFieldValue(value, "zone_name"));

            db.insert("cfg_zones", null, contentValues);

        } catch (IllegalStateException e) {
            Log.v(TAG, "Error" + e.getMessage());

            status = false;
        }

        return status;
    }

    private boolean recordEconomicActivities(JSONObject value) {

        boolean status = true;

        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("activity_id", Action.getFieldValue(value, "activity_id"));

            contentValues.put("description", Action.getFieldValue(value, "description"));

            db.insert("cfg_economic_activities", null, contentValues);

        } catch (IllegalStateException e) {
            Log.v(TAG, "Error" + e.getMessage());

            status = false;
        }

        return status;
    }


    private boolean recordLandUses(JSONObject value) {

        boolean status = true;

        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("activity_id", Action.getFieldValue(value, "activity_id"));

            contentValues.put("description", Action.getFieldValue(value, "description"));

            db.insert("cfg_land_uses", null, contentValues);

        } catch (IllegalStateException e) {
            Log.v(TAG, "Error" + e.getMessage());

            status = false;
        }

        return status;
    }


    private boolean recordEnvironmentalStatus(JSONObject value) {

        boolean status = true;

        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("status_id", Action.getFieldValue(value, "status_id"));

            contentValues.put("description", Action.getFieldValue(value, "description"));

            db.insert("cfg_court_environmental_status", null, contentValues);

        } catch (IllegalStateException e) {
            Log.v(TAG, "Error" + e.getMessage());

            status = false;
        }

        return status;
    }

    private boolean recordTransportModes(JSONObject value) {

        boolean status = true;

        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("mode_id", Action.getFieldValue(value, "mode_id"));

            contentValues.put("description", Action.getFieldValue(value, "description"));

            db.insert("cfg_transport_modes", null, contentValues);

        } catch (IllegalStateException e) {
            Log.v(TAG, "Error" + e.getMessage());

            status = false;
        }

        return status;
    }


    public void cleanData() {
        this.open();

        db.delete("cfg_court_levels", "1", null);
        db.delete("cfg_court_building_status", "1", null);
        db.delete("cfg_court_building_ownership_status", "1", null);
        db.delete("cfg_land_ownership_status", "1", null);
        db.delete("cfg_zones", "1", null);
        db.delete("cfg_economic_activities", "1", null);
        db.delete("cfg_transport_modes", "1", null);
        db.delete("cfg_court_environmental_status", "1", null);
        db.delete("cfg_land_uses", "1", null);

        this.close();

    }

    public void cleanLocationData() {
        this.open();

        db.delete("cfg_regions", "1", null);
        db.delete("cfg_districts", "1", null);
        db.delete("cfg_wards", "1", null);
        db.delete("cfg_village_street", "1", null);
        this.close();
    }


    public List<String> getDBLabels(String[] columns, String tableName, String filterCondition, String orderBy) {

        List<String> labels = new ArrayList<String>();

        this.open();

        cursor = db.query(tableName, columns, filterCondition, null, null, null, orderBy);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            }

            while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();

        db.close();

        return labels;
    }

    public int getRecordIdentifier(String[] columns, String tableName, String filterCondition, String[] params) {

        int value = 0;

        this.open();

        cursor = db.query(tableName, columns, filterCondition, params, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                value = cursor.getInt(0);
            }

            while (cursor.moveToNext());
        }

        cursor.close();

        db.close();

        return value;
    }


    public String[] getRecentCourtData() {

        this.open();

        String data[] = new String[2];

        cursor = db.rawQuery("SELECT _id,message FROM tbl_court_data ORDER BY _id LIMIT 1", null);

        if (cursor.moveToFirst()) {
            do {
                data[0] = cursor.getString(0);

                data[1] = cursor.getString(1);
            }

            while (cursor.moveToNext());
        }

        cursor.close();

        db.close();

        return data;
    }


    public HashMap<String, String> getAssociativeResults(String[] columns, String tableName, String filterCondition, String orderBy) {

        HashMap<String, String> data = new HashMap<>();

        this.open();

        cursor = db.query(tableName, columns, filterCondition, null, null, null, orderBy);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                data.put(cursor.getString(0), cursor.getString(1));
            }

            while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();

        db.close();

        return data;
    }


    public boolean recordUnsyncedCourtData(String message) {

        this.open();

        boolean status = true;

        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("message", message);

            db.insert("tbl_court_data", null, contentValues);

        } catch (IllegalStateException e) {
            Log.v(TAG, "Error" + e.getMessage());

            status = false;
        }

        this.close();

        return status;
    }


    public void deleteCourtData(String Id) {
        this.open();
        String TBL_NAME = "tbl_court_data";
        db.execSQL("delete from " + TBL_NAME + " where _id=" + Id);
        this.close();
    }

}







