package com.parkseryu.witness.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.parkseryu.witness.dao.MeetDayDao
import com.parkseryu.witness.dto.MeetDay

@Database(entities = [MeetDay::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun meetDayDao(): MeetDayDao

    companion object{
        private val DB_NAME = "roob-db"
        private var instance : AppDatabase? = null
    }

    fun getInstance(context: Context): AppDatabase {
        return instance ?: synchronized(this){
            instance ?: buildDatabase(context)
        }
    }

    private fun buildDatabase(context: Context): AppDatabase{
        return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME)
            .addCallback(object : RoomDatabase.Callback(){
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                }
            }).build()
    }
}