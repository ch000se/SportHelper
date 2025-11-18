package com.example.sporthelper.data.mapper

import com.example.sporthelper.domain.model.BodyPart

data class BodyPartDto(
    val name: String = "",
    val active: Boolean = false,
    val measuringUnit: String = "",
    val latestValue: Float? = null,
    val bodyPart: String? = null,
)

fun BodyPart.toBodyPartDto(): BodyPartDto {
    return BodyPartDto(
        name = name,
        active = isActive,
        measuringUnit = measuringUnit,
        latestValue = latestValue,
        bodyPart = bodyPart
    )
}

fun BodyPartDto.toBodyPart(): BodyPart {
    return BodyPart(
        name = name,
        isActive = active,
        measuringUnit = measuringUnit,
        latestValue = latestValue,
        bodyPart = bodyPart
    )
}