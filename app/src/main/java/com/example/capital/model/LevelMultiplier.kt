package com.example.capital.model

sealed class LevelMultiplier(open var str: String) {
    data class One(override var str: String = "x1", val speed: Int = 1) : LevelMultiplier(str)
    data class Ten(override var str: String = "x10")  : LevelMultiplier(str)
    data class TwentyFive(override var  str: String = "x25")  : LevelMultiplier(str)
    data class Hundred(override var  str: String = "x100")  : LevelMultiplier(str)
    data class Max(override var  str: String = "Max")  : LevelMultiplier(str)
}