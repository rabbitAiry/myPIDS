package com.airy.mypids.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces

val lineInfoB8 = LineInfo(
    lineName = "B8路(宝岗大道总站 - 棠德花苑总站)",
    rawLineName = "B8路",
    lineDirection = "棠德花苑总站",
    lineId = "B8路(宝岗大道总站 - 棠德花苑总站)",
    lineColor = Color(1.0F, 1.0F, 1.0F, 1.0F, ColorSpaces.Srgb),
    lineMapLeftToRight = true,
    otherLineColors = mapOf(
        "3 号线" to Color(0.9098039F, 0.61960787F, 0.2784314F, 1.0F, ColorSpaces.Srgb),
        "2号线" to Color(0.0F, 0.4F, 0.61960787F, 1.0F, ColorSpaces.Srgb),
        "1号线" to Color(0.92941177F, 0.8117647F, 0.23921569F, 1.0F, ColorSpaces.Srgb),
        "6号线" to Color(0.47843137F, 0.078431375F, 0.32156864F, 1.0F, ColorSpaces.Srgb),
        "3号线北延线" to Color(0.9098039F, 0.61960787F, 0.2784314F, 1.0F, ColorSpaces.Srgb),
        "21号线" to Color(0.23529412F, 0.10980392F, 0.45882353F, 1.0F, ColorSpaces.Srgb),
        "5号线" to Color(0.78039217F, 0.019607844F, 0.2509804F, 1.0F, ColorSpaces.Srgb),
        "APM线" to Color(0.0F, 0.72156864F, 0.8784314F, 1.0F, ColorSpaces.Srgb)
    ),
    stations = listOf(
        Station(
            names = listOf("宝岗大道总站"),
            latitude = 23.1043929928701,
            longitude = 113.27056663557613,
            interchanges = null
        ), Station(
            names = listOf("宝岗大道北"),
            latitude = 23.10891365052501,
            longitude = 113.2681052784796,
            interchanges = null
        ), Station(
            names = listOf("市红会医院"),
            latitude = 23.11245361687522,
            longitude = 113.26956053340528,
            interchanges = null
        ), Station(
            names = listOf("解放南路"),
            latitude = 23.12349670794944,
            longitude = 113.26915629592592,
            interchanges = listOf("2 号线", "6 号线")
        ), Station(
            names = listOf("大南路"),
            latitude = 23.12640481830732,
            longitude = 113.27413290844956,
            interchanges = listOf("6 号线")
        ), Station(
            names = listOf("文明路"),
            latitude = 23.1288475817344,
            longitude = 113.2791095209732,
            interchanges = null
        ), Station(
            names = listOf("中山图书馆"),
            latitude = 23.128963902680116,
            longitude = 113.28305308216069,
            interchanges = listOf("1 号线")
        ), Station(
            names = listOf("三角市"),
            latitude = 23.128490309620357,
            longitude = 113.28706850778897,
            interchanges = null
        ), Station(
            names = listOf("较场西路"),
            latitude = 23.13134015109684,
            longitude = 113.28973647515272,
            interchanges = null
        ), Station(
            names = listOf("烈士陵园"),
            latitude = 23.132993529594003,
            longitude = 113.29194630670654,
            interchanges = listOf("1 号线")
        ), Station(
            names = listOf("中山医"),
            latitude = 23.131248757714314,
            longitude = 113.2971385125525,
            interchanges = null
        ), Station(
            names = listOf("东山口"),
            latitude = 23.13042621443709,
            longitude = 113.30029156489148,
            interchanges = listOf("1 号线", "6 号线")
        ), Station(
            names = listOf("农林东"),
            latitude = 23.130933034635486,
            longitude = 113.30547478768234,
            interchanges = null
        ), Station(
            names = listOf("梅花村"),
            latitude = 23.13282736183269,
            longitude = 113.3126881809251,
            interchanges = listOf("5 号线")
        ), Station(
            names = listOf("杨箕村"),
            latitude = 23.13495429345086,
            longitude = 113.31788936982613,
            interchanges = listOf("1 号线")
        ), Station(
            names = listOf("天河"),
            latitude = 23.133317556129626,
            longitude = 113.32682750964746,
            interchanges = listOf("1 号线", "3 号线", "3 号线北延线", "APM线")
        ), Station(
            names = listOf("冼村"),
            latitude = 23.132428558355898,
            longitude = 113.33934988845242,
            interchanges = null
        ), Station(
            names = listOf("石牌村"),
            latitude = 23.132029753679664,
            longitude = 113.34621294254639,
            interchanges = null
        ), Station(
            names = listOf("国防大厦"),
            latitude = 23.131439852896847,
            longitude = 113.3546031160068,
            interchanges = null
        ), Station(
            names = listOf("华侨医院(潭村)"),
            latitude = 23.130567459605107,
            longitude = 113.35872633829624,
            interchanges = null
        ), Station(
            names = listOf("员村山顶"),
            latitude = 23.129645209027622,
            longitude = 113.36525701935163,
            interchanges = null
        ), Station(
            names = listOf("天府路(地铁天河公园站)"),
            latitude = 23.13129860865805,
            longitude = 113.36888617361073,
            interchanges = listOf("21 号线")
        ), Station(
            names = listOf("天河公园"),
            latitude = 23.13581834967454,
            longitude = 113.36921854664932,
            interchanges = null
        ), Station(
            names = listOf("上社(BRT)"),
            latitude = 23.13826924774372,
            longitude = 113.37228176843733,
            interchanges = null
        ), Station(
            names = listOf("学院(BRT)"),
            latitude = 23.13527831519466,
            longitude = 113.37809380508497,
            interchanges = null
        ), Station(
            names = listOf("棠下村(BRT)"),
            latitude = 23.132029753679664,
            longitude = 113.38550482553984,
            interchanges = null
        ), Station(
            names = listOf("棠东(BRT)"),
            latitude = 23.131390002006214,
            longitude = 113.39173906577703,
            interchanges = null
        ), Station(
            names = listOf("家家乐医院"),
            latitude = 23.134771511605013,
            longitude = 113.40019212062313,
            interchanges = null
        ), Station(
            names = listOf("泰安花园"),
            latitude = 23.13664085950185,
            longitude = 113.39897042512996,
            interchanges = null
        ), Station(
            names = listOf("地铁棠东站"),
            latitude = 23.13732212640404,
            longitude = 113.3957365252951,
            interchanges = listOf("21 号线")
        ), Station(
            names = listOf("棠德花苑总站"),
            latitude = 23.140437631644794,
            longitude = 113.39277211711317,
            interchanges = null
        )
    ),
    currIdx = 0,
    languages = listOf("zh"),
    stationIdStart = 0,
    isStationIdIncrease = true,
    startStationIdx = 0,
    endStationIdx = 31
)