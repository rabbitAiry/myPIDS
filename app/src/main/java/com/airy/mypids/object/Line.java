package com.airy.mypids.object;

import java.util.LinkedList;
import java.util.List;

public class Line {
    public String lineName;
    public List<Station> stationItemList;
    public List<LineLanguage> languageList;
    public int selectedLanguageIndex = 0;

    public Line(String lineName, String[] stationNames, LineLanguage defaultLanguage) {
        this.lineName = lineName;
        stationItemList = new LinkedList<>();
        for (String s:stationNames) {
            stationItemList.add(new Station(s));
        }
        languageList = new LinkedList<>();
        languageList.add(defaultLanguage);
    }

    public String switchLanguage(){
        selectedLanguageIndex = (selectedLanguageIndex+1)%languageList.size();
        return languageList.get(selectedLanguageIndex).languageName;
    }

    public String getStationName(int index){
        Station station = stationItemList.get(index);
        return station.stationNameList.get(selectedLanguageIndex);
    }

    public String getTerminalDesc(){
        return languageList
                .get(selectedLanguageIndex)
                .getTerminalDesc(getStationName(stationItemList.size()-1));
    }

    public String getTextOfNextStation(){
        return languageList.get(selectedLanguageIndex).textOfNextStation;
    }
}
