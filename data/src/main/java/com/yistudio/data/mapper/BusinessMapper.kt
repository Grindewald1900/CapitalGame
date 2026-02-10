package com.yistudio.data.mapper

import com.yistudio.data.entity.BusinessEntity
import com.yistudio.domain.entity.BusinessModel

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