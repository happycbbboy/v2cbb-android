package com.happycbbboy.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.happycbbboy.databases.dao.ProxyConfigDao;
import com.happycbbboy.domain.ProxyConfig;

@Database(entities = {ProxyConfig.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract ProxyConfigDao proxyConfigDao();
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "AppDatabase.db")
//                            .createFromAsset("databases/AppDatabase.db")
//                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}