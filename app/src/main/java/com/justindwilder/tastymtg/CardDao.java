package com.justindwilder.tastymtg;

import java.util.List;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

/**
    This interface establishes the methods that will interat with the database. The functionality is 
    handled via the annotations preceding the methods and class. 
*/
@Dao
public interface CardDao {

    @Insert
    void insert(CardData card);

    @Update
    void update(CardData card);

    @Delete
    void delete(CardData card);

    //can use SQL operations
    @Query("SELECT * FROM card_table ORDER BY favorite DESC")
    LiveData<List<CardData>> getAllCards();
    
    //This method returns a list instead of LiveData to allow swapping operations. 
    @Query("SELECT * FROM card_table ORDER BY favorite DESC")
    List<CardData> getList();
}
