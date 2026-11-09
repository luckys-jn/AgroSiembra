package com.angelramirez.agresiembre.data

data class Parcel(
    val id: String,
    var name: String,
    var area: String = "",
    var altitude: String = "",
    var ph: String = "6.5",
    var soilType: String = "Franco",
    var latitude: String = "",
    var longitude: String = "",
    var isGpsActive: Boolean = false
)