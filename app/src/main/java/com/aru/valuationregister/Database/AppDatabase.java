package com.aru.valuationregister.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.aru.valuationregister.Database.Daos.ConfigurationDao;
import com.aru.valuationregister.Database.Models.Configuration;

@Database(entities = Configuration.class, exportSchema = false, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "valuation_register_db";
    private static AppDatabase instance;
    public abstract ConfigurationDao configurationDao();

    public static  synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
