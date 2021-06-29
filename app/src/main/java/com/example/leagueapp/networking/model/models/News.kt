package com.example.leagueapp.networking.model.models

import java.util.*

data class News(val URL : String, val title : String, val date : String, val imageUrl : String, val termIds : List<String>)