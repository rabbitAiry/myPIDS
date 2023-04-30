package com.airy.pids_lib.bluetooth.data

/**
 * 表示该司机
 * 序列化后形式： "@001@"
 */
class DriverLineMessage(
    val driverId: String,
    val cityName: String,
    val lineName: String,
    val uid: String,
    val range: IntRange
){
    override fun toString(): String = "$driverId@$cityName@$lineName@$uid@${range.first}@${range.last}"

    fun toByteArray(): ByteArray = toString().encodeToByteArray()
}

fun String.toBluetoothMessage(): DriverLineMessage {
    val list = split("@")
    return DriverLineMessage(
        driverId = list[0],
        cityName = list[1],
        lineName = list[2],
        uid = list[3],
        range = IntRange(list[4].toInt(), list[5].toInt())
    )
}