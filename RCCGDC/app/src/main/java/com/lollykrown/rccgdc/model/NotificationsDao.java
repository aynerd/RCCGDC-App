package com.lollykrown.rccgdc.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface NotificationsDao {

    @Insert
    void addNotifications(Notifications notifications);

    @Update
    void updateNotifications(Notifications notifications);

    @Query("SELECT * FROM notifications")
    List<Notifications> getAll();

    @Delete
    void deleteNotifications(Notifications notifications);
}
