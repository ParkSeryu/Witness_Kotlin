package com.parkseryu.witness.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meetDay")
data class MeetDay(
    @PrimaryKey(autoGenerate = false)var startDay : Int = 0,
    @ColumnInfo(name = "TopPhrases") var topPharse : String = "",
    @ColumnInfo(name = "bottomPhrases") var bottomPharse : String = ""
)