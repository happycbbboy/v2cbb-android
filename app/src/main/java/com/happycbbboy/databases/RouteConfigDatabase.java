package com.happycbbboy.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.happycbbboy.databases.dao.RouteConfigDao;
import com.happycbbboy.domain.RouteConfig;

@Database(entities = {RouteConfig.class}, version = 1,exportSchema = false)
public abstract class RouteConfigDatabase extends RoomDatabase {
    private static volatile RouteConfigDatabase INSTANCE;

    public abstract RouteConfigDao proxyConfigDao();
    public static RouteConfigDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RouteConfigDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    RouteConfigDatabase.class, "RouteConfig.db")
//                            .createFromAsset("databases/AppDatabase.db")
//                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}