package com.example.leagueapp.database

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "searches_table")
data class SearchItem(
        @PrimaryKey
        @ColumnInfo(name = "name") var name : String
) : Serializable{
    //@PrimaryKey(autoGenerate = true) var id: Int? = null
}
