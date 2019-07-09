package com.justindwilder.tastymtg;

import android.content.Context;
import android.os.AsyncTask;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
    This class establishes the database on users device that holds our CardData entities. 
*/
@Database(entities = {CardData.class}, version = 5)
public abstract class CardDatabase extends RoomDatabase {

    private static CardDatabase instance;

    public abstract CardDao cardDao();

    public static synchronized CardDatabase getInstance(Context context){
        
        //Only called if there is no database instance. 
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CardDatabase.class, "card_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    /**
        Asynchronous callback that calls the PopulateDbAsyncTask.
    */
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(SupportSQLiteDatabase db){
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    /**
        Asynchronous task which initializes the database with a default CardData object. 
        Because this task and callback are asynchronous, the database is able to build 
        without needing to wait for the data to update. 
    */
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private CardDao cardDao;

        private PopulateDbAsyncTask(CardDatabase db){
            cardDao = db.cardDao();
        }
        @Override
        protected Void doInBackground(Void... voids){
            //Default card (Omniscience) inserted
            cardDao.insert(new CardData());
            return null;
        }
    }
}
