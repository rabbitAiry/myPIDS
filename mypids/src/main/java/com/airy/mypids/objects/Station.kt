package com.airy.mypids.objects

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
}


//): Parcelable {
//    constructor(parcel: Parcel) : this(
//        parcel.readString()!!,
//        parcel.readDouble(),
//        parcel.readDouble()
//    ) {
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(name)
//        parcel.writeDouble(latitude)
//        parcel.writeDouble(longitude)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<Station> {
//        override fun createFromParcel(parcel: Parcel): Station {
//            return Station(parcel)
//        }
//
//        override fun newArray(size: Int): Array<Station?> {
//            return arrayOfNulls(size)
//        }
//    }
//}

enum class OpenDoorSide{
    LEFT, RIGHT, BOTH, NONE
}