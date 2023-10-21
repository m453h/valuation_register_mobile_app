package com.aru.valuationregister.Database.Daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.aru.valuationregister.Database.Models.Configuration;

import java.util.List;

@Dao
public interface ConfigurationDao {
    @Query("SELECT * FROM configuration")
    List<Configuration> getAll();

    @Query("SELECT * FROM configuration WHERE type = :type")
    List<Configuration> loadAllByType(String type);

    @Query("SELECT * FROM configuration WHERE type = :type and description = :description")
    Configuration getConfigurationItemId(String type, String description);

    @Insert
    void insertAll(Configuration... configurations);

    @Delete
    void delete(Configuration configuration);

    @Query("DELETE FROM configuration")
    void deleteAll();
}