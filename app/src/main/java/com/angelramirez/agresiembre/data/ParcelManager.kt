package com.angelramirez.agresiembre.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

object ParcelManager {
    // Inicia sólo con el Lote 1
    val parcels = mutableStateListOf(
        Parcel("1", "Lote 1 - Finca El Paraíso")
    )

    // Slot/Parcela activa
    var selectedParcel by mutableStateOf(parcels[0])

    fun addParcel(name: String) {
        val newParcel = Parcel(
            id = (parcels.size + 1).toString(),
            name = name
        )
        parcels.add(newParcel)
    }

    fun selectParcel(parcel: Parcel) {
        selectedParcel = parcel
    }

    // Actualiza los datos de la parcela activa en tiempo real
    fun updateActiveParcelData(
        area: String = selectedParcel.area,
        altitude: String = selectedParcel.altitude,
        ph: String = selectedParcel.ph,
        soilType: String = selectedParcel.soilType,
        lat: String = selectedParcel.latitude,
        lng: String = selectedParcel.longitude,
        gpsActive: Boolean = selectedParcel.isGpsActive
    ) {
        selectedParcel.area = area
        selectedParcel.altitude = altitude
        selectedParcel.ph = ph
        selectedParcel.soilType = soilType
        selectedParcel.latitude = lat
        selectedParcel.longitude = lng
        selectedParcel.isGpsActive = gpsActive
    }
}