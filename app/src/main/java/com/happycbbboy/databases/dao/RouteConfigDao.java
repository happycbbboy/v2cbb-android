package com.happycbbboy.databases.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.happycbbboy.domain.RouteConfig;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface RouteConfigDao {
    @Query("SELECT * FROM RouteConfig")
    Flowable<List<RouteConfig>> getAll();

    @Query("SELECT * FROM RouteConfig WHERE id IN (:ids)")
    Flowable<List<RouteConfig>> loadAllByIds(int[] ids);

    @Query("SELECT * FROM RouteConfig WHERE name LIKE :name LIMIT 1")
    Flowable<RouteConfig> findByName(String name);
    @Query("SELECT * FROM RouteConfig WHERE id LIKE :id LIMIT 1")
    Flowable<RouteConfig> findById(int id);
//    @Query("SELECT * FROM RouteConfig WHERE id LIKE :p LIMIT 1")
//    Flowable<RouteConfig> findByRouteConfig(RouteConfig p);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Completable insertAll(RouteConfig... pcs);

    @Delete
    public Completable delete(RouteConfig RouteConfig);
}