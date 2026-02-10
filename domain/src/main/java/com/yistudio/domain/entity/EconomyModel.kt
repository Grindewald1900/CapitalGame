package com.yistudio.domain.entity

data class EconomyModel(
    val cash: Double,
    val offlineEarnings: Double,
    val influence: Double = 0.0,
    val equity: Double = 0.0
)
