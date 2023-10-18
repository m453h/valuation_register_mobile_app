package com.aru.valuationregister.Database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

    public class MySQLiteHelper extends SQLiteOpenHelper
    {

        private static final String DATABASE_NAME = "valuation_register.db";
        private static final int DATABASE_VERSION = 9;

        private static final String CREATE_TABLE_REGIONS ="CREATE TABLE cfg_regions (_id INTEGER PRIMARY KEY AUTOINCREMENT,region_id INTEGER, region_name VARCHAR(200));";
        private static final String CREATE_TABLE_DISTRICTS ="CREATE TABLE cfg_districts (_id INTEGER PRIMARY KEY AUTOINCREMENT,district_id INTEGER,region_id INTEGER, district_name VARCHAR(200));";
        private static final String CREATE_TABLE_WARDS ="CREATE TABLE cfg_wards (_id INTEGER PRIMARY KEY AUTOINCREMENT,ward_id INTEGER, ward_name VARCHAR(200),district_id INTEGER);";
        private static final String CREATE_TABLE_AREAS ="CREATE TABLE cfg_village_street (_id INTEGER PRIMARY KEY AUTOINCREMENT,area_id INTEGER, area_name VARCHAR(200),ward_id INTEGER);";

        private static final String CREATE_TABLE_COURT_LEVELS ="CREATE TABLE cfg_court_levels (_id INTEGER PRIMARY KEY AUTOINCREMENT,level_id INTEGER, description VARCHAR(200));";
        private static final String CREATE_TABLE_BUILDING_STATUS ="CREATE TABLE cfg_court_building_status (_id INTEGER PRIMARY KEY AUTOINCREMENT,status_id INTEGER, description VARCHAR(200));";
        private static final String CREATE_TABLE_BUILDING_OWNERSHIP_STATUS ="CREATE TABLE cfg_court_building_ownership_status (_id INTEGER PRIMARY KEY AUTOINCREMENT,status_id INTEGER, description VARCHAR(200));";
        private static final String CREATE_TABLE_LAND_OWNERSHIP_STATUS ="CREATE TABLE cfg_land_ownership_status (_id INTEGER PRIMARY KEY AUTOINCREMENT,status_id INTEGER, description VARCHAR(200));";
        private static final String CREATE_TABLE_ZONES ="CREATE TABLE cfg_zones (_id INTEGER PRIMARY KEY AUTOINCREMENT,zone_id INTEGER, zone_name VARCHAR(200));";
        private static final String CREATE_TABLE_ECONOMIC_ACTIVITIES ="CREATE TABLE cfg_economic_activities (_id INTEGER PRIMARY KEY AUTOINCREMENT,activity_id INTEGER, description VARCHAR(200));";
        private static final String CREATE_TABLE_LAND_USE ="CREATE TABLE cfg_land_uses (_id INTEGER PRIMARY KEY AUTOINCREMENT,activity_id INTEGER, description VARCHAR(200));";
        private static final String CREATE_TABLE_TRANSPORT_MODES ="CREATE TABLE cfg_transport_modes (_id INTEGER PRIMARY KEY AUTOINCREMENT,mode_id INTEGER, description VARCHAR(200));";
        private static final String CREATE_TABLE_ENVIRONMENTAL_STATUS ="CREATE TABLE cfg_court_environmental_status (_id INTEGER PRIMARY KEY AUTOINCREMENT,status_id INTEGER, description VARCHAR(200));";
        private static final String CREATE_TABLE_COURT_DATA ="CREATE TABLE tbl_court_data (_id INTEGER PRIMARY KEY AUTOINCREMENT, message TEXT);";


        private Context context;

        private static MySQLiteHelper sInstance;

        private MySQLiteHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        public static synchronized MySQLiteHelper getInstance(Context context) {

            // Use the application context, which will ensure that you
            if (sInstance == null)
            {
                if(context!=null)
                {
                    sInstance = new MySQLiteHelper(context.getApplicationContext());
                }
            }
            return sInstance;
        }

        public void onCreate(SQLiteDatabase db) {
            //Create Database
            try
            {
                db.execSQL(CREATE_TABLE_REGIONS);
                db.execSQL(CREATE_TABLE_DISTRICTS);
                db.execSQL(CREATE_TABLE_WARDS);
                db.execSQL(CREATE_TABLE_AREAS);
                db.execSQL(CREATE_TABLE_COURT_LEVELS);
                db.execSQL(CREATE_TABLE_BUILDING_OWNERSHIP_STATUS);
                db.execSQL(CREATE_TABLE_BUILDING_STATUS);
                db.execSQL(CREATE_TABLE_LAND_OWNERSHIP_STATUS);
                db.execSQL(CREATE_TABLE_ZONES);
                db.execSQL(CREATE_TABLE_ECONOMIC_ACTIVITIES);
                db.execSQL(CREATE_TABLE_TRANSPORT_MODES);
                db.execSQL(CREATE_TABLE_COURT_DATA);
                db.execSQL(CREATE_TABLE_LAND_USE);
                db.execSQL(CREATE_TABLE_ENVIRONMENTAL_STATUS);
               // Toast.makeText(context, "DB Created", Toast.LENGTH_LONG).show();
            }
            catch (SQLException e)
            {
                //Toast.makeText(context, "DB Create Failed"+e.getMessage(), Toast.LENGTH_LONG).show();
                //Log.v("DBActivity",e.getMessage());
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //Create Database
            try
            {

                String ALTER_TABLE_COURT_DATA_1="ALTER TABLE tbl_court_data ADD COLUMN imageViewOne TEXT;";
                String ALTER_TABLE_COURT_DATA_2="ALTER TABLE tbl_court_data ADD COLUMN imageViewTwo TEXT;";
                String ALTER_TABLE_COURT_DATA_3="ALTER TABLE tbl_court_data ADD COLUMN imageViewThree TEXT;";
                String ALTER_TABLE_COURT_DATA_4="ALTER TABLE tbl_court_data ADD COLUMN imageViewFour TEXT;";

                db.execSQL(ALTER_TABLE_COURT_DATA_1);
                db.execSQL(ALTER_TABLE_COURT_DATA_2);
                db.execSQL(ALTER_TABLE_COURT_DATA_3);
                db.execSQL(ALTER_TABLE_COURT_DATA_4);

                Toast.makeText(context, "DB Upgraded", Toast.LENGTH_LONG).show();
                //Log.e("DBActivity","Done");

            }
            catch (SQLException e)
            {
                Toast.makeText(context, "DB Upgrade Failed"+e.getMessage(), Toast.LENGTH_LONG).show();
                //Log.e("DBActivity",e.getMessage());
            }
        }



    }


