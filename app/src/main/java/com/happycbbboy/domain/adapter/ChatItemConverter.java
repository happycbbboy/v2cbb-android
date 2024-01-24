package com.happycbbboy.domain.adapter;

import androidx.room.TypeConverter;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class ChatItemConverter {
    @TypeConverter
    public String objectToString(List<String> list) {
        return JSONObject.toJSONString(list);
    }

    @TypeConverter
    public List<String> stringToObject(String json) {
        return  JSONObject.parseArray(json,String.class);
    }
}
