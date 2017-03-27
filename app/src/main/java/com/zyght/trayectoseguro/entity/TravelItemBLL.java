package com.zyght.trayectoseguro.entity;

import java.util.ArrayList;

/**
 * Created by Arley Mauricio Duarte on 3/27/17.
 */

public class TravelItemBLL {

    private ArrayList<TravelItem> travelItems = new ArrayList<>();

    private static final TravelItemBLL ourInstance = new TravelItemBLL();

    public static TravelItemBLL getInstance() {
        return ourInstance;
    }

    private TravelItemBLL() {
    }

    public void clear() {
        travelItems.clear();
    }

    public void add(TravelItem travelItem){
        travelItems.add(travelItem);
    }

    public ArrayList<TravelItem> getTravelItems() {
        return travelItems;
    }
}
