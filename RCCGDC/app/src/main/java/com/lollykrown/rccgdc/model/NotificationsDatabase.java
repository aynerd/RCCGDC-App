package com.lollykrown.rccgdc.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Notifications.class}, exportSchema = false, version = 1)
public abstract class NotificationsDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "notificationsdb";
    public abstract NotificationsDao notificationsDao();

    private static volatile NotificationsDatabase INSTANCE;

    public static NotificationsDatabase getInMemoryDatabase(Context context){
        if (INSTANCE == null) {
            synchronized (NotificationsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NotificationsDatabase.class, NotificationsDatabase.DATABASE_NAME)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static NotificationsDatabase getFileDatabase(Context context){
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NotificationsDatabase.class, "database-filename")
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance(){
        INSTANCE = null;
    }

}
