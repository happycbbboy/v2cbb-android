package com.happycbbboy.ui.route.placeholder;

import com.happycbbboy.domain.RouteConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class PlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    public static List<RouteConfig> ITEMS = new ArrayList<RouteConfig>();

    /**
     * A map of sample (placeholder) items, by ID.
     */
    public static Map<Integer, RouteConfig> ITEM_MAP = new HashMap<Integer, RouteConfig>();


    public static void addItem(RouteConfig item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getId(), item);
    }
    public static void deleteItem(int i) {
        RouteConfig RouteConfig = ITEMS.get(i);
        ITEMS.remove(i);
        ITEM_MAP.remove(RouteConfig.getId());
//        ITEMS.add(item);
//        ITEM_MAP.put(item.id, item);
    }
   
}