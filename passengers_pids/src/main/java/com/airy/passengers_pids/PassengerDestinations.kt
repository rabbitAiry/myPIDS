package com.airy.passengers_pids

interface PassengerDestinations {
    val route: String
    val title: String
}

val destinationsList = listOf(LineSelection, TerminalSelection, BusWaiting, OnJourney)

object LineSelection : PassengerDestinations {
    override val route = "LineSelection"
    override val title = "准备旅程！\n选择出行线路"
}

object TerminalSelection : PassengerDestinations {
    override val route = "TerminalSelecting"
    override val title = "将要前往..."
}

object BusWaiting : PassengerDestinations {
    override val route = "BusWaiting"
    override val title = "最快一趟车"
}

object OnJourney : PassengerDestinations {
    override val route = "OnJourney"
    override val title = "下一站"
}
