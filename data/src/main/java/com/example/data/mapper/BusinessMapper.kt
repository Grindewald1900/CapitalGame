package com.example.data.mapper

import com.example.data.entity.BusinessEntity
import com.example.domain.entity.BusinessModel

fun BusinessEntity.toDomainModel(): BusinessModel {
    return BusinessModel(
        id = this.id,
        name = this.name,
        level = this.level,
        baseIncomePerSec = this.baseIncomePerSec,
        automated = this.automated
    )
}

fun BusinessModel.toEntity(): BusinessEntity {
    return BusinessEntity(
        id = this.id,
        name = this.name,
        level = this.level,
        baseIncomePerSec = this.baseIncomePerSec,
        automated = this.automated
    )
}