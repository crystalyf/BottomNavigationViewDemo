package com.change.towerfarm.models

import java.io.Serializable

/**
 * Get Sensor ListのResponseModel
 */
data class GetSensorListResponseModel(
    var detail: MutableList<SensorInformation>? = null
)

/**
 * only information , not have value
 */
data class SensorInformation(
    var device: String? = null,
    var param: MutableList<Param>? = null
) : Serializable

data class Param(
    var id: String? = null,
    var name: String? = null,
    var min: String? = null,
    var max: String? = null,
    var unit: String? = null
) : Serializable

