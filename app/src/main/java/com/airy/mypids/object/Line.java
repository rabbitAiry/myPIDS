package com.airy.mypids.object;

import java.util.LinkedList;
import java.util.List;

public class Line {
    public String lineName;
    public List<Station> stationItemList;
    public List<LineLanguage> languageList;
    public int selectedLanguageIndex = 0;
    public int currStationIndex = 0; // currStationIndex总是指最近到达的车站

    public Line(String lineName, String[] stationNames, LineLanguage defaultLanguage) {
        this.lineName = lineName;
        stationItemList = new LinkedList<>();
        for (String s:stationNames) {
            stationItemList.add(new Station(s));
        }
        languageList = new LinkedList<>();
        languageList.add(defaultLanguage);
    }

    public void switchLanguage(){
        selectedLanguageIndex = (selectedLanguageIndex+1)%languageList.size();
    }

    public String getStationName(int index){
        Station station = stationItemList.get(index);
        return station.stationNameList.get(selectedLanguageIndex);
    }

    public String getNextStationDesc(){
        if(currStationIndex>=stationItemList.size()-1)
            return languageList.get(selectedLanguageIndex).getAlreadyInTerminalDesc();
        else
            return languageList
                .get(selectedLanguageIndex)
                .getNextStationDesc(getStationName(currStationIndex+1));
    }

    public String getArrivedStationDesc(){
        return languageList.get(selectedLanguageIndex).getArrivedStationDesc(getStationName(currStationIndex));
    }

    public String getTerminalDesc(){
        return languageList
                .get(selectedLanguageIndex)
                .getTerminalDesc(getStationName(stationItemList.size()-1));
    }

    public String getTextOfNextStation(){
        return languageList.get(selectedLanguageIndex).textOfNextStation;
    }

    public void setCurrStationIndex(int newIdx){
        if(newIdx<0 || newIdx>= stationItemList.size())return;
        currStationIndex = newIdx;
    }

    public void switchToNextStation(){
        if(currStationIndex< stationItemList.size())
            currStationIndex++;
    }
}
