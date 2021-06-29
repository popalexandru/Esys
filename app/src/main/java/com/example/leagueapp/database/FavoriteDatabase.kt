package com.example.leagueapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
        entities = [FavoriteItem::class],
        version = 1
)
abstract class FavoriteDatabase : RoomDatabase(){
    abstract fun getSearchesDao() : FavoriteDAO
}