package com.airy.mypids.object;

public class LineLanguage {
    public String languageName;
    public String textOfNextStation;
    // "$"会被站名替代，如：在pattern"$ 方向"下，广州东站最终得到的结果是："广州东站 方向"
    // "$"应该在pattern中只出现一次
    public String patternOfTerminalStation;

    public LineLanguage(String languageName, String textOfNextStation, String patternOfTerminalStation) {
        this.languageName = languageName;
        this.textOfNextStation = textOfNextStation;
        this.patternOfTerminalStation = patternOfTerminalStation;
    }

    public String getTerminalDesc(String stationName){
        return patternOfTerminalStation.replace("$", stationName);
    }

    public static LineLanguage getDefaultChineseLineLanguage(){
        return new LineLanguage("CN", "下一站：","$ 方向");
    }

    public static LineLanguage getDefaultEnglishLineLanguage(){
        return new LineLanguage("EN", "Next：","Towards: $");
    }
}
