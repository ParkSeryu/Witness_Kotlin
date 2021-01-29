package com.parkseryu.witness.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.micromos.knpmobile.Event
import com.parkseryu.witness.MainActivity.Companion.mDate
import com.parkseryu.witness.MainActivity.Companion.today
import com.parkseryu.witness.`object`.AnniversaryObject
import com.parkseryu.witness.dto.MeetDayEntity
import com.parkseryu.witness.repository.DayRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = DayRepositoryImpl(application)
    private val _dbInitializerEvent = MutableLiveData<Event<Unit>>()
    val dbInitializerEvent: LiveData<Event<Unit>> = _dbInitializerEvent

    val startDay = MutableLiveData<String>()

    lateinit var selectDay: List<MeetDayEntity>
    var diffDays = String()

    private val _makeUiEvent = MutableLiveData<Event<Unit>>()
    val makeUiEvent: LiveData<Event<Unit>> = _makeUiEvent
    private val firstDayFlag = 1
    private val formatter = SimpleDateFormat("yyyyMMdd", Locale.KOREA)
    private val anniversaryFormatter = SimpleDateFormat("yyyy. MM. dd", Locale.KOREA)
    private val gregorianCalendar = GregorianCalendar()

    init {
        init()
    }

    fun insertDay() {
        CoroutineScope(Dispatchers.IO).launch {
            repository.insert(
                MeetDayEntity(
                    startDay = startDay.value!!,
                    topPhrase = "우리는",
                    bottomPhrase = "입니다"
                )
            )
            var year = 1
            gregorianCalendar.time = formatter.parse(startDay.value!!)!!
            for (i in 0..AnniversaryObject.remainDay.lastIndex) {
                if (AnniversaryObject.remainDayInt[i] == 0) {
                    gregorianCalendar.add(Calendar.YEAR, year)
                    year++
                } else {
                    gregorianCalendar.add(
                        Calendar.DAY_OF_MONTH,
                        AnniversaryObject.remainDayInt[i] - 1
                    )
                }
                var whenDay = anniversaryFormatter.format(gregorianCalendar.time)
                when (gregorianCalendar.get(Calendar.DAY_OF_WEEK)) {
                    1 -> whenDay += AnniversaryObject.addWhenDay[0]
                    2 -> whenDay += AnniversaryObject.addWhenDay[1]
                    3 -> whenDay += AnniversaryObject.addWhenDay[2]
                    4 -> whenDay += AnniversaryObject.addWhenDay[3]
                    5 -> whenDay += AnniversaryObject.addWhenDay[4]
                    6 -> whenDay += AnniversaryObject.addWhenDay[5]
                    7 -> whenDay += AnniversaryObject.addWhenDay[6]
                }
                var d_Day = String()
                d_Day = if(mDate.time > gregorianCalendar.timeInMillis) "D+"
                else "D-"
                val calculation =  mDate.time - gregorianCalendar.timeInMillis
                var calculationDay = calculation / (24 * 60 * 60 * 1000)
                calculationDay = abs(calculationDay)
                d_Day += calculationDay

            /*    repository.insert(
                    AnniversaryEntity(
                        whatDay = AnniversaryObject.remainDay[i],
                        whenDay = whenDay,
                        d_Day = d_Day,
                    )
                )*/
            }
            init()
        }
    }

    private fun init() {
        CoroutineScope(Dispatchers.IO).launch {
            selectDay = repository.select()
            Log.d("selectTest", "$selectDay")
            if (selectDay.isEmpty()) {
                _dbInitializerEvent.postValue(Event(Unit))
            } else {
                val beginDate = formatter.parse(selectDay[0].startDay)!!.time
                val endDate = formatter.parse(today)!!.time
                diffDays = ((endDate - beginDate) / (24 * 60 * 60 * 1000) + firstDayFlag).toString()
                Log.d("test", diffDays)
              /*  repository.insert(
                    AnniversaryEntity(
                        whatDay =,
                        whenDay =,
                        d_Day =
                    )
                )*/
                _makeUiEvent.postValue(Event(Unit))

            }
        }
    }


}