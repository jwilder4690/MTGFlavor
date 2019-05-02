package com.example.mtgflavorsampler;

import java.util.List;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CardDao {

    @Insert
    void insert(CardData card);

    @Update
    void update(CardData card);

    @Delete
    void delete(CardData card);

    @Query("SELECT * FROM card_table ORDER BY favorite DESC")
    LiveData<List<CardData>> getAllCards();
    //can use SQL operations
}
