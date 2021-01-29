package com.parkseryu.witness.repository

import android.content.Context
import com.parkseryu.witness.db.AppDatabase
import com.parkseryu.witness.dto.MeetDayEntity

interface DayRepository{
    fun select() : List<MeetDayEntity>
    fun insert(meetDayEntity: MeetDayEntity)
//    fun insert(anniversaryEntity: AnniversaryEntity)
}

class DayRepositoryImpl(context: Context) : DayRepository{
    private val meetDayDao = AppDatabase.getInstance(context).meetDayDao()
    //private val anniversaryDao = AppDatabase.getInstance(context).anniversaryDao()

    override fun select() : List<MeetDayEntity>{
        return meetDayDao.getAll()
    }

    override fun insert(meetDayEntity: MeetDayEntity) {
        meetDayDao.insert(meetDayEntity)
    }

/*    override fun insert(anniversaryEntity: AnniversaryEntity) {
        anniversaryDao.insert(anniversaryEntity)
    }*/
}