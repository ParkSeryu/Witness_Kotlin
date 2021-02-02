package com.parkseryu.witness.repository

import android.content.Context
import com.parkseryu.witness.db.AppDatabase
import com.parkseryu.witness.dto.AnniversaryEntity
import com.parkseryu.witness.dto.MeetDayEntity

interface DayRepository{
    fun selectMeetDay() : List<MeetDayEntity>
    fun selectAnniversaryDay() : List<AnniversaryEntity>
    fun insert(meetDayEntity: MeetDayEntity)
    fun insert(anniversaryEntity: AnniversaryEntity)
    fun update(updateLeftDay: String, originalLeftDay : String)
}

class DayRepositoryImpl(context: Context) : DayRepository{
    private val meetDayDao = AppDatabase.getInstance(context).meetDayDao()
    private val anniversaryDao = AppDatabase.getInstance(context).anniversaryDao()

    override fun selectMeetDay() : List<MeetDayEntity>{
        return meetDayDao.getAll()
    }

    override fun selectAnniversaryDay(): List<AnniversaryEntity> {
        return anniversaryDao.getAll()
    }

    override fun insert(meetDayEntity: MeetDayEntity) {
        meetDayDao.insert(meetDayEntity)
    }

    override fun insert(anniversaryEntity: AnniversaryEntity) {
        anniversaryDao.insert(anniversaryEntity)
    }

    override fun update(updateLeftDay: String, originalLeftDay : String) {
        anniversaryDao.update(updateLeftDay, originalLeftDay)
    }
}