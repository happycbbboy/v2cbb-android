package com.happycbbboy.domain;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.happycbbboy.domain.adapter.ChatItemConverter;

import java.util.List;

@Entity
@TypeConverters(ChatItemConverter.class)
public class RouteConfig {

    @PrimaryKey(autoGenerate = true)
    Integer id;
    @ColumnInfo(name = "name")
    String name;

    public final static int CURRENT_ROUTE_POLICY_NORMAL = 0;
    public final static int CURRENT_ROUTE_POLICY_FREEE = 1;
    public final static int CURRENT_ROUTE_POLICY_TUNNEL = 2;

    @ColumnInfo(name = "current_route_policy")
    Integer currentRoutePolicy;

    @ColumnInfo(name = "route")
    List<String> route;

    @ColumnInfo(name = "include_package")
    List<String> checkPackages;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRoute() {
        return route;
    }

    public void setRoute(List<String> route) {
        this.route = route;
    }

    public Integer getCurrentRoutePolicy() {
        return currentRoutePolicy;
    }

    public void setCurrentRoutePolicy(Integer currentRoutePolicy) {
        this.currentRoutePolicy = currentRoutePolicy;
    }

    public List<String> getCheckPackages() {
        return checkPackages;
    }

    public void setCheckPackages(List<String> checkPackages) {
        this.checkPackages = checkPackages;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
