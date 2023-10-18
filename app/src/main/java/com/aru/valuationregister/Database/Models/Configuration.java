package com.aru.valuationregister.Database.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Configuration {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "configId")
    public String configurationId;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "type")
    public String type;

    public Configuration(String id, String description, String type) {
        this.configurationId = id;
        this.description = description;
        this.type = type;
    }

    public Configuration(){

    }

}
