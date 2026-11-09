package com.angelramirez.agresiembre.data

data class Crop(
    val id: String,
    val name: String,
    val scientificName: String,
    val compatibility: Int = 0,
    val harvestTime: String,
    val waterNeed: String,
    val soilSuitability: String,
    val description: String,
    val minAltitud: Int = 0,
    val maxAltitud: Int = 4000,
    val minPh: Float = 4.0f,
    val maxPh: Float = 9.0f
)