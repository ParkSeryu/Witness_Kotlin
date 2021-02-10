package com.parkseryu.witness.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_anniversary")
data class UserAnniversaryEntity(
    @PrimaryKey(autoGenerate = true) val No: Int = 0,
    @ColumnInfo(name = "whatDay") val whatDay: String,
    @ColumnInfo(name = "whenDay") val whenDay: String,
    @ColumnInfo(name = "leftDay") val leftDay: String
)
