package com.parkseryu.witness.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meetDay")
data class MeetDayEntity(
    @PrimaryKey(autoGenerate = false)val startDay : String ,
    @ColumnInfo(name = "TopPhrases") val topPhrase : String,
    @ColumnInfo(name = "bottomPhrases") val bottomPhrase : String
)

