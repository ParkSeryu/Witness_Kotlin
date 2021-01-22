package com.parkseryu.witness.repository

import android.content.Context
import com.parkseryu.witness.db.AppDatabase

class RepositoryImpl(context: Context) {
    private val mRoom = AppDatabase.getInstance(context).meetDayDao()

    fun select(){
        mRoom.getAll()
    }
}