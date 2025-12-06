package com.example.capital.ui.state

data class BusinessUiState(
    val id: String,
    val name: String,
    val level: Int,
    val incomePerSec: Double,    // raw number, e.g. 3200.0
    val upgradeCost: Double,     // e.g. 15700.0
    val automated: Boolean,
)