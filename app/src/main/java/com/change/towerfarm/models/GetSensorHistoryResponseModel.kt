package com.change.towerfarm.models

import com.google.gson.annotations.SerializedName

/**
 * Get Sensor ListのResponseModel
 */
data class GetSensorHistoryResponseModel(
    val device: String,
    val data: List<HistorySensorModel>
)

data class HistorySensorModel(
    val datetime: String,
    @SerializedName("LTMP")
    val ltmp: String,
    @SerializedName("HUM")
    val hum: String,
    @SerializedName("CO2")
    val co2: String,
    @SerializedName("LUX")
    val lux: String
)

