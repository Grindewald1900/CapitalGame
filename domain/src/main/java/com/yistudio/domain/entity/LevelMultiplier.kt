package com.yistudio.domain.entity

sealed class LevelMultiplier(val str: String) {
    object One : LevelMultiplier("x1")
    object Ten : LevelMultiplier("x10")
    object TwentyFive : LevelMultiplier("x25")
    object Hundred : LevelMultiplier("x100")
    object Max : LevelMultiplier("Max")
}
