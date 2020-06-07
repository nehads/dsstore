package com.nehads.friends.main.profile

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.os.SystemClock
import androidx.core.app.AlarmManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nehads.friends.di.repository.DataRepository
import com.nehads.friends.receiver.AlarmReceiver
import com.nehads.friends.util.Constants
import com.nehads.friends.util.cancelNotifications
import com.nehads.friends.util.plusAssign
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileViewModel @Inject
constructor(
    private val appDataRepository: DataRepository,
    private val mContext: Application,
    private val mCompositeDisposable: CompositeDisposable
) : ViewModel() {

    private val REQUEST_CODE = 0
    private val TRIGGER_TIME = "TRIGGER_AT"

    private val minute: Long = 60_000L
    private val second: Long = 1_000L

    var isLoading = MutableLiveData<Boolean>()
    var errorData = MutableLiveData<String>()
    var pageNav = MutableLiveData<Any>()

    private var _email = MutableLiveData<String>()
    val email: LiveData<String>
        get() = _email

    private var _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name

    private val timerLengthOptions: Int
    private val notifyPendingIntent: PendingIntent

    private val alarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private var prefs =
        mContext.getSharedPreferences("com.example.android.friends", Context.MODE_PRIVATE)
    private val notifyIntent = Intent(mContext, AlarmReceiver::class.java)

    private val _elapsedTime = MutableLiveData<Long>()
    val elapsedTime: LiveData<Long>
        get() = _elapsedTime

    private var _alarmOn = MutableLiveData<Boolean>()
    val isAlarmOn: LiveData<Boolean>
        get() = _alarmOn

    private lateinit var timer: CountDownTimer

    init {
        _alarmOn.value = PendingIntent.getBroadcast(
            mContext,
            REQUEST_CODE,
            notifyIntent,
            PendingIntent.FLAG_NO_CREATE
        ) != null

        notifyPendingIntent = PendingIntent.getBroadcast(
            mContext,
            REQUEST_CODE,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        timerLengthOptions = Constants.REMINDER_TIME

        //If alarm is not null, resume the timer back for this alarm
        if (_alarmOn.value!!) {
            createTimer()
        }

    }

    fun logoutUser() {
        isLoading.value = true
        mCompositeDisposable +=
            appDataRepository.nukeTheUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        isLoading.value = false
                        pageNav.value = 1
                    }, { error ->
                        error?.printStackTrace()
                        isLoading.value = false
                        errorData.value = "Error logging out"
                    }
                )
    }

    fun getUserData() {
        isLoading.value = true
        mCompositeDisposable +=
            appDataRepository.getUserData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        isLoading.value = false
                        _name.value = it.name
                        _email.value = it.email
                    }, { error ->
                        error?.printStackTrace()
                        isLoading.value = false
                    }
                )
    }

    /**
     * Turns on or off the alarm
     *
     * @param isChecked, alarm status to be set.
     */
    fun setAlarm(isChecked: Boolean) {
        when (isChecked) {
            true ->
                startTimer()
            false -> cancelNotification()
        }
    }

    /**
     * Creates a new alarm, notification and timer
     */
    private fun startTimer() {
        _alarmOn.value?.let {
            if (!it) {
                _alarmOn.value = true
                val triggerTime = SystemClock.elapsedRealtime() + (timerLengthOptions * minute)

                val notificationManager =
                    ContextCompat.getSystemService(
                        mContext,
                        NotificationManager::class.java
                    ) as NotificationManager
                notificationManager.cancelNotifications()

                AlarmManagerCompat.setExactAndAllowWhileIdle(
                    alarmManager,
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    triggerTime,
                    notifyPendingIntent
                )

                viewModelScope.launch {
                    saveTime(triggerTime)
                }
            }
        }
        createTimer()
    }

    /**
     * Creates a new timer
     */
    private fun createTimer() {
        viewModelScope.launch {
            val triggerTime = loadTime()
            timer = object : CountDownTimer(triggerTime, second) {
                override fun onTick(millisUntilFinished: Long) {
                    _elapsedTime.value = triggerTime - SystemClock.elapsedRealtime()
                    if (_elapsedTime.value!! <= 0) {
                        resetTimer()
                    }
                }

                override fun onFinish() {
                    resetTimer()
                }
            }
            timer.start()
        }
    }

    /**
     * Cancels the alarm, notification and resets the timer
     */
    private fun cancelNotification() {
        resetTimer()
        alarmManager.cancel(notifyPendingIntent)
    }

    /**
     * Resets the timer on screen and sets alarm value false
     */
    private fun resetTimer() {
        timer.cancel()
        _elapsedTime.value = 0
        _alarmOn.value = false
    }

    private suspend fun saveTime(triggerTime: Long) =
        withContext(Dispatchers.IO) {
            prefs.edit().putLong(TRIGGER_TIME, triggerTime).apply()
        }

    private suspend fun loadTime(): Long =
        withContext(Dispatchers.IO) {
            prefs.getLong(TRIGGER_TIME, 0)
        }
}