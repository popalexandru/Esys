package com.example.leagueapp.models

import android.graphics.Bitmap

class Spell(
    val key : String = "0",
    val name : String = "none",
    val x : Int = 0,
    val y : Int = 0,
    val w : Int = 48,
    val h : Int = 48,
    val image : Bitmap? = null
)