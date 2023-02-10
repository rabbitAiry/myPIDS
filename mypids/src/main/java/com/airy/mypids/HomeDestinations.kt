package com.airy.mypids

interface HomeDestination{
    val route: String
    val title: String
    val isStart: Boolean
        get() = false
}

val destinationsList = listOf(Start, Line, LineConfig, /* StationConfig, DetailConfig */)

object Start: HomeDestination{
    override val route = "Start"
    override val title = "开始"
    override val isStart = true
}

object Line: HomeDestination{
    override val route = "line"
    override val title = "1/4 选择线路"
}

object LineConfig: HomeDestination{
    override val route = "lineConfig"
    override val title = "2/4 线路属性"
}

object StationConfig: HomeDestination{
    override val route = "stationConfig"
    override val title = "3/4 站点属性"
}

object DetailConfig: HomeDestination{
    override val route = "detailConfig"
    override val title = "4/4 站点详情"
}