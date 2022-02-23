package com.parkseryu.witness.ui.home

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.micromos.knpmobile.Event
import com.parkseryu.witness.MainActivity
import com.parkseryu.witness.R
import com.parkseryu.witness.`object`.AnniversaryObject
import com.parkseryu.witness.`object`.AnniversaryObject.today
import com.parkseryu.witness.dto.AnniversaryEntity
import com.parkseryu.witness.dto.MeetDayEntity
import com.parkseryu.witness.dto.UserAnniversaryEntity
import com.parkseryu.witness.repository.DayRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.properties.Delegates

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

    private val repository = DayRepositoryImpl(application)
    private val _dbInitializerEvent = MutableLiveData<Event<Unit>>()
    val dbInitializerEvent: LiveData<Event<Unit>> = _dbInitializerEvent

    lateinit var startDay: String
    lateinit var selectDay: List<MeetDayEntity>
    lateinit var diffDays: String

    var tenYearsFlag by Delegates.notNull<Boolean>()
    lateinit var userAddAnniversaryName: String
    lateinit var userAddAnniversaryDay: String

    lateinit var anniversaryDay: List<AnniversaryEntity>
    lateinit var userAnniversaryDay: List<UserAnniversaryEntity>
    private val _makeUiEvent = MutableLiveData<Event<Unit>>()
    val makeUiEvent: LiveData<Event<Unit>> = _makeUiEvent

    private val _fabClickEvent = MutableLiveData<Event<Unit>>()
    val fabClickEvent: LiveData<Event<Unit>> = _fabClickEvent

    private val _fabAddAniClickEvent = MutableLiveData<Event<Unit>>()
    val fabAddAniClickEvent: LiveData<Event<Unit>> = _fabAddAniClickEvent

    private val firstDayFlag = 1
    private val formatter = SimpleDateFormat("yyyyMMdd", Locale.KOREA)
    private val anniversaryFormatter = SimpleDateFormat("yyyy. MM. dd", Locale.KOREA)
    private val gregorianCalendar = GregorianCalendar()

    init {
        init()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val intent = Intent(context, MainActivity::class.java).apply{
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent : PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val builder = Notification.Builder(context, "testChannel")
            .setSmallIcon(R.drawable.witness_icon)
            .setContentTitle("test")
            .setContentText("test")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "테스트 채널"
            val descriptionText = "testChannel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("testChannel", name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            notificationManager.notify(1, builder.build())
//
        }
    }

    private fun init() {
        CoroutineScope(Dispatchers.IO).launch {
            selectDay = repository.selectMeetDay()
            Log.d("selectTest", "$selectDay")
            if (selectDay.isEmpty()) {
                _dbInitializerEvent.postValue(Event(Unit))
            } else {
                val beginDate = formatter.parse(selectDay[0].startDay)!!.time
                val endDate = formatter.parse(today)!!.time
                diffDays = ((endDate - beginDate) / (24 * 60 * 60 * 1000) + firstDayFlag).toString()
                Log.d("test", "$beginDate / $endDate / $diffDays")
                setAnniversaryDay(diffDays.toInt())
                setUserAnniversaryDay(diffDays.toInt())
                anniversaryDay = repository.selectUnionAnniversaryDay()
                _makeUiEvent.postValue(Event(Unit))
            }
        }
    }

    private fun setAnniversaryDay(diffDays: Int) {
        var compareToStartDay = diffDays
        anniversaryDay = repository.selectAnniversaryDay()
        if (anniversaryDay[0].leftDay.substring(2) != "DAY") {
            compareToStartDay = anniversaryDay[0].leftDay.substring(2).toInt()
        }
        if (diffDays > compareToStartDay) {
            Log.d("test11", "$diffDays / $compareToStartDay")
            val flag = diffDays - compareToStartDay
            Log.d("test111", "$flag")
            for (i in 0..anniversaryDay.lastIndex) {
                var leftDay: String
                leftDay = when {
                    anniversaryDay[i].leftDay == "D-DAY" -> {
                        "D+$flag"
                    }
                    anniversaryDay[i].leftDay.substring(1, 2) == "+" -> {
                        "D+${(anniversaryDay[i].leftDay.substring(2).toInt() + flag)}"
                    }
                    else -> "D-${(anniversaryDay[i].leftDay.substring(2).toInt() - flag)}"
                }
                if (leftDay == "D-0") leftDay = "D-DAY"

                repository.updateAnniversary(leftDay, anniversaryDay[i].leftDay)
                Log.d("test1", anniversaryDay[i].leftDay)
            }
        }
    }

    private fun setUserAnniversaryDay(diffDays: Int) {
        var compareToStartDay = diffDays
        Log.d("test22", "$diffDays / $compareToStartDay")
        userAnniversaryDay = repository.selectUserAnniversaryDay()
        if (userAnniversaryDay.isNotEmpty())
            if (anniversaryDay[0].leftDay.substring(2) != "DAY") {
                compareToStartDay = anniversaryDay[0].leftDay.substring(2).toInt()
            }
        if (diffDays > compareToStartDay) {
            val flag = diffDays - compareToStartDay
            for (i in 0..userAnniversaryDay.lastIndex) {
                var leftDay: String
                leftDay = when {
                    userAnniversaryDay[i].leftDay == "D-DAY" -> {
                        "D+$flag"
                    }
                    userAnniversaryDay[i].leftDay.substring(1, 2) == "+" -> {
                        "D+${(userAnniversaryDay[i].leftDay.substring(2).toInt() + flag)}"
                    }
                    else -> "D-${(userAnniversaryDay[i].leftDay.substring(2).toInt() - flag)}"
                }
                if (leftDay == "D-0") leftDay = "D-DAY"

                repository.updateUserAnniversary(leftDay, userAnniversaryDay[i].leftDay)
                Log.d("test2", userAnniversaryDay[i].leftDay)
            }
        }
    }

    fun insertDay() {
        CoroutineScope(Dispatchers.IO).launch {
            repository.insert(
                MeetDayEntity(
                    startDay = startDay,
                    topPhrase = "우리는",
                    bottomPhrase = "입니다"
                )
            )
            var year = 1
            for (i in 0..AnniversaryObject.remainDay.lastIndex) {
                gregorianCalendar.time = formatter.parse(startDay)!!
                if (AnniversaryObject.remainDayInt[i] == 0) {
                    gregorianCalendar.add(Calendar.YEAR, year)
                    year++
                } else {
                    gregorianCalendar.add(
                        Calendar.DAY_OF_MONTH,
                        AnniversaryObject.remainDayInt[i] - firstDayFlag
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

                val todayTime = formatter.parse(today)!!.time
                var leftDay = when {
                    todayTime == gregorianCalendar.timeInMillis -> "D-DAY"
                    todayTime > gregorianCalendar.timeInMillis -> "D+"
                    else -> "D-"
                }

                val calculation = todayTime - gregorianCalendar.timeInMillis
                var calculationDay = calculation / (24 * 60 * 60 * 1000)
                calculationDay = abs(calculationDay)
                if (leftDay != "D-DAY")
                    leftDay +=
                        if (i == 0)
                            calculationDay + firstDayFlag
                        else calculationDay

                repository.insert(
                    AnniversaryEntity(
                        whatDay = AnniversaryObject.remainDay[i],
                        whenDay = whenDay,
                        leftDay = leftDay
                    )
                )
            }
            init()
        }
    }

    fun insertAnniversaryDay() {
        CoroutineScope(Dispatchers.IO).launch {
            gregorianCalendar.time = formatter.parse(userAddAnniversaryDay)!!
            for (i in 0..10) {
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

                val todayTime = formatter.parse(today)!!.time
                var leftDay = when {
                    todayTime == gregorianCalendar.timeInMillis -> "D-DAY"
                    todayTime > gregorianCalendar.timeInMillis -> "D+"
                    else -> "D-"
                }

                val calculation = todayTime - gregorianCalendar.timeInMillis
                var calculationDay = calculation / (24 * 60 * 60 * 1000)
                calculationDay = abs(calculationDay)
                if (leftDay != "D-DAY")
                    leftDay +=
                        if (i == 0)
                            calculationDay + firstDayFlag
                        else calculationDay

                repository.insert(
                    UserAnniversaryEntity(
                        whatDay = userAddAnniversaryName,
                        whenDay = whenDay,
                        leftDay = leftDay
                    )
                )
                if (!tenYearsFlag) break
                gregorianCalendar.add(Calendar.YEAR, 1)
            }
            init()
        }
    }

    fun onClickFab() {
        _fabClickEvent.value = Event(Unit)
    }

    fun onClickFabAddAni() {
        _fabAddAniClickEvent.value = Event(Unit)
    }

    fun setRecyclerViewItemColor(leftDay: String): Int {
        return if (leftDay.substring(1, 2) == "-")
            Color.rgb(81, 81, 81)
        else
            Color.GRAY
    }
}