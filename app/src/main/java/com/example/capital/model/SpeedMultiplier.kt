package com.example.capital.model

sealed class SpeedMultiplier(val ) {
    data class One(val str: String = "x1") : SpeedMultiplier()
    data class Ten(val str: String = "x10")  : SpeedMultiplier()
    data class TwentyFive(val str: String = "x25")  : SpeedMultiplier()
    data class Hundred(val str: String = "x100")  : SpeedMultiplier()
    data class Max(val str: String = "Max")  : SpeedMultiplier()
}