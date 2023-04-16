package com.airy.mypids.data

class Station(
    val names: List<String>,
    val latitude: Double,
    val longitude: Double,
    val interchanges: List<String>? = null,
    val farInterchanges: List<String>? = null,
    val underConstruction: Boolean = false,
    val openDoorSide: OpenDoorSide = OpenDoorSide.LEFT
){
    val name get() = names[0]

    override fun toString(): String =
        "Station(names=listOf(\"$name\"), latitude=$latitude, longitude=$longitude, interchanges=$interchanges)"
}

enum class OpenDoorSide{
    LEFT, RIGHT, BOTH, NONE
}