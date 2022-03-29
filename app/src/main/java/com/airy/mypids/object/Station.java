package com.airy.mypids.object;

import java.util.LinkedList;
import java.util.List;

public class Station {
    public List<String> stationNameList;
    public boolean metroStationNearby;

    public Station(String stationChineseName) {
        stationNameList = new LinkedList<>();
        stationNameList.add(stationChineseName);
    }
}