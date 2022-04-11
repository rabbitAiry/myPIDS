package com.airy.mypids.object;

public enum LineLanguage {
    Chinese("下一站：",
            "$ \n到了",
            "$ 方向",
            "本次旅程已到达终点"),
    English("Next：",
            "arrive:\n$",
            "Towards: $",
            "the end of the journey");

    // 在pattern中，"$"会被站名替代，如：在pattern"$ 方向"下，广州东站最终得到的结果是："广州东站 方向"
    // "$"应该在pattern中只出现一次
    public String textOfNextStation;
    public String patternOfArrivedStation;
    public String patternOfTerminalStation;
    public String alreadyInTerminal;

    LineLanguage(String textOfNextStation, String patternOfArrivedStation, String patternOfTerminalStation, String alreadyInTerminal) {
        this.textOfNextStation = textOfNextStation;
        this.patternOfArrivedStation = patternOfArrivedStation;
        this.patternOfTerminalStation = patternOfTerminalStation;
        this.alreadyInTerminal = alreadyInTerminal;
    }

    public String getNextStationDesc(String stationName){
        return textOfNextStation+stationName;
    }

    public String getArrivedStationDesc(String stationName){
        return patternOfArrivedStation.replace("$", stationName);
    }

    public String getTerminalDesc(String stationName){
        return patternOfTerminalStation.replace("$", stationName);
    }

    public String getAlreadyInTerminalDesc(){
        return alreadyInTerminal;
    }
}
