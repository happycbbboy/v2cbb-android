package com.happycbbboy.databases.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.happycbbboy.domain.ProxyConfig;

import java.util.List;
import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface ProxyConfigDao {
    @Query("SELECT * FROM ProxyConfig")
    Flowable<List<ProxyConfig>> getAll();

    @Query("SELECT * FROM ProxyConfig WHERE id IN (:userIds)")
    Flowable<List<ProxyConfig>> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM ProxyConfig WHERE name LIKE :name LIMIT 1")
    Flowable<ProxyConfig> findByName(String name);
    @Query("SELECT * FROM ProxyConfig WHERE id LIKE :id LIMIT 1")
    Flowable<ProxyConfig> findById(int id);
//    @Query("SELECT * FROM ProxyConfig WHERE id LIKE :p LIMIT 1")
//    Flowable<ProxyConfig> findByProxyConfig(ProxyConfig p);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Completable insertAll(ProxyConfig... pcs);

    @Delete
    public Completable delete(ProxyConfig proxyConfig);
}