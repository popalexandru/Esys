package com.example.leagueapp.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.leagueapp.Constants
import com.example.leagueapp.Constants.ACTION_PAUSE_SERVICE
import com.example.leagueapp.Constants.ACTION_START_OR_RESUME
import com.example.leagueapp.Constants.ACTION_START_PAUSE
import com.example.leagueapp.Constants.ACTION_STOP_SERVICE
import com.example.leagueapp.Constants.NOTIFICAITON_CHANNEL_ID
import com.example.leagueapp.Constants.NOTIFICATION_CHANNEL_AME
import com.example.leagueapp.Constants.NOTIFICATION_ID
import com.example.leagueapp.Constants.SPAWN_BARON
import com.example.leagueapp.Constants.SPAWN_BLUE
import com.example.leagueapp.Constants.SPAWN_DRAGON
import com.example.leagueapp.Constants.SPAWN_GROMP
import com.example.leagueapp.Constants.SPAWN_KRUGS
import com.example.leagueapp.Constants.SPAWN_RAPTORS
import com.example.leagueapp.Constants.SPAWN_RED
import com.example.leagueapp.Constants.SPAWN_SCUTTER
import com.example.leagueapp.Constants.SPAWN_WOLVES
import com.example.leagueapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class JungleService : LifecycleService() {
    lateinit var currentNotification: NotificationCompat.Builder
    lateinit var nManager: NotificationManager

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

    var isServiceKilled = false
    var isOngoing = false;

    var redStarted = 0L
    var blueStarted = 0L
    var dragonStarted = 0L
    var baronStarted = 0L
    var raptorsStarted = 0L
    var wolvesStarted = 0L
    var krugsStarted = 0L
    var grompStarted = 0L
    var scutter1Started = 0L
    var scutter2Started = 0L

    var campsCounting = 0L

    companion object {
        var isFirstClick = true
        var isRedCounting = false
        var isBlueCounting = false
        var isDragonCounting = false
        var isBaronCounting = false
        var isRaptorsCounting = false
        var isWolvesCounting = false
        var isKrugsCounting = false
        var isGrompCounting = false
        var isScutter1Counting = false
        var isScutter2Counting = false

        val countdownRed = MutableLiveData<Int>(SPAWN_RED)
        val countdownBlue = MutableLiveData<Int>(SPAWN_BLUE)
        val countdownDragon = MutableLiveData<Int>(SPAWN_DRAGON)
        val countdownBaron = MutableLiveData<Int>(SPAWN_BARON)
        val countdownRaptors = MutableLiveData<Int>(SPAWN_RAPTORS)
        val countdownWolves = MutableLiveData<Int>(SPAWN_WOLVES)
        val countdownKrugs = MutableLiveData<Int>(SPAWN_KRUGS)
        val countdownGromp = MutableLiveData<Int>(SPAWN_GROMP)
        val countdownScutter1 = MutableLiveData<Int>(SPAWN_SCUTTER)
        val countdownScutter2 = MutableLiveData<Int>(SPAWN_SCUTTER)
    }

    override fun onCreate() {
        super.onCreate()

        currentNotification = baseNotificationBuilder

        updateNotification()

    }

    private fun killService() {
        isServiceKilled = true
        isOngoing = false
        stopForeground(true)
        stopSelf()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME -> {
                    val param = it.extras?.getString("param")

                    Timber.d("Extra $param")
                    when (param) {
                        "red" -> {
                            isRedCounting = !isRedCounting
                            if (isRedCounting) {
                                campsCounting++
                                redStarted = System.currentTimeMillis()
                            }else campsCounting--

                        }
                        "blue" -> {
                            isBlueCounting = !isBlueCounting
                            if (isBlueCounting){
                                blueStarted = System.currentTimeMillis()
                                campsCounting++
                            }else campsCounting--
                        }
                        "dragon" -> {
                            isDragonCounting = !isDragonCounting
                            if (isDragonCounting) {
                                dragonStarted = System.currentTimeMillis()
                                campsCounting++
                            }else campsCounting--
                        }
                        "baron" -> {
                            isBaronCounting = !isBaronCounting
                            if (isBaronCounting) {
                                baronStarted = System.currentTimeMillis()
                                campsCounting++
                            }else campsCounting--
                        }
                        "raptors" -> {
                            isRaptorsCounting = !isRaptorsCounting
                            if (isRaptorsCounting) {
                                raptorsStarted = System.currentTimeMillis()
                                campsCounting++
                            }else campsCounting--
                        }
                        "wolves" -> {
                            isWolvesCounting = !isWolvesCounting
                            if (isWolvesCounting) {
                                wolvesStarted = System.currentTimeMillis()
                                campsCounting++
                            }else campsCounting--
                        }
                        "krugs" -> {
                            isKrugsCounting = !isKrugsCounting
                            if (isKrugsCounting) {
                                krugsStarted = System.currentTimeMillis()
                                campsCounting++
                            }else campsCounting--
                        }
                        "gromp" -> {
                            isGrompCounting = !isGrompCounting
                            if (isGrompCounting) {
                                grompStarted = System.currentTimeMillis()
                                campsCounting++
                            }else campsCounting--
                        }
                        "scutter1" -> {
                            isScutter1Counting = !isScutter1Counting
                            if (isScutter1Counting) {
                                scutter1Started = System.currentTimeMillis()
                                campsCounting++
                            }else campsCounting--
                        }
                        "scutter2" -> {
                            isScutter2Counting = !isScutter2Counting
                            if (isScutter2Counting) {
                                scutter2Started = System.currentTimeMillis()
                                campsCounting++
                            }else campsCounting--
                        }
                    }

                    if(!isOngoing){
                        startForegroundService()
                    }

                }

                ACTION_PAUSE_SERVICE -> {
                    //pauseService()
                    Timber.d("Paused service")
                }

                ACTION_STOP_SERVICE -> {
                    Timber.d("Stopped service")
                    killService()
                }

                ACTION_START_PAUSE -> {
                    //pauseStart()
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startCentralTimer() = CoroutineScope(Dispatchers.IO).launch {
        var timeStarted = System.currentTimeMillis()
        isOngoing = true;
        updateNotification()
        while (!isServiceKilled) {
            val timeDifference = System.currentTimeMillis() - timeStarted

            if (timeDifference > TimeUnit.SECONDS.toMillis(1)) {
                timeStarted = System.currentTimeMillis()

                checkIfUpdate()
                updateNotificationText()
            }
        }
    }

    private fun updateNotificationText() {
        var notificationText = ""
        var notificationTitle = ""
        var campsShowed = 0L

        if(isRedCounting){
            if(campsShowed < 4){
                campsShowed++;
                notificationTitle += "Red: ${countdownRed.value!!} \n"
            }else{
                notificationText += "Red: ${countdownRed.value!!} \n"
            }
        }
        if(isBlueCounting){
            if(campsShowed < 4){
                campsShowed++;
                notificationTitle += "Blue: ${countdownBlue.value!!} \n"
            }else{
                notificationText += "Blue: ${countdownBlue.value!!} \n"
            }

        }
        if(isBaronCounting){
            if(campsShowed < 4){
                campsShowed++;
                notificationTitle += "Baron: ${countdownBaron.value!!} \n"
            }else{
                notificationText += "Baron: ${countdownBaron.value!!} \n"
            }

        }
        if(isDragonCounting){
            if(campsShowed < 4){
                campsShowed++;
                notificationTitle += "Dragon: ${countdownDragon.value!!} \n"
            }else{
                notificationText += "Dragon: ${countdownDragon.value!!} \n"
            }

        }
        if(isRaptorsCounting){
            if(campsShowed < 4){
                campsShowed++;
                notificationTitle += "Raptors: ${countdownRaptors.value!!} \n"
            }else{
                notificationText += "Raptors: ${countdownRaptors.value!!} \n"
            }

        }
        if(isWolvesCounting){
            if(campsShowed < 4){
                campsShowed++;
                notificationTitle += "Wolves: ${countdownWolves.value!!} \n"
            }else{
                notificationText += "Wolves: ${countdownWolves.value!!} \n"
            }

        }
        if(isKrugsCounting){
            if(campsShowed < 4){
                campsShowed++;
                notificationTitle += "Krugs: ${countdownKrugs.value!!} \n"
            }else{
                notificationText += "Krugs: ${countdownKrugs.value!!} \n"
            }

        }
        if(isGrompCounting){
            if(campsShowed < 4){
                campsShowed++;
                notificationTitle += "Gromp: ${countdownGromp.value!!} \n"
            }else{
                notificationText += "Gromp: ${countdownGromp.value!!} \n"
            }

        }
        if(isScutter1Counting){
            if(campsShowed < 4){
                campsShowed++;
                notificationTitle += "Scutter1: ${countdownScutter1.value!!} \n"
            }else{
                notificationText += "Scutter1: ${countdownScutter1.value!!} \n"
            }

        }
        if(isScutter2Counting){
            if(campsShowed < 4){
                campsShowed++;
                notificationTitle += "Scutter2: ${countdownScutter2.value!!} \n"
            }else{
                notificationText += "Scutter2: ${countdownScutter2.value!!} \n"
            }

        }

        val notification = currentNotification
            .setContentText(notificationText)
            .setContentTitle(notificationTitle)

        nManager.notify(NOTIFICATION_ID, notification.build())
    }

    private fun checkIfUpdate() {
        if (isScutter2Counting) {
            val countDown =
                SPAWN_SCUTTER - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - scutter2Started)
                    .toInt()
            if (countDown >= 0) countdownScutter2.postValue(countDown)
            else if (countDown < 0) {
                isScutter2Counting = false
                countdownScutter2.postValue(SPAWN_SCUTTER)
            }
        }
        if (isScutter1Counting) {
            val countDown =
                SPAWN_SCUTTER - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - scutter1Started)
                    .toInt()
            if (countDown >= 0) countdownScutter1.postValue(countDown)
            else if (countDown < 0) {
                isScutter1Counting = false
                countdownScutter1.postValue(SPAWN_SCUTTER)
            }
        }
        if (isRedCounting) {
            val countDown =
                SPAWN_RED - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - redStarted)
                    .toInt()
            if (countDown >= 0) countdownRed.postValue(countDown)
            else if (countDown < 0) {
                isRedCounting = false
                countdownRed.postValue(SPAWN_RED)
            }
        }
        if (isBlueCounting) {
            val countDown =
                SPAWN_BLUE - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - blueStarted)
                    .toInt()
            if (countDown >= 0) countdownBlue.postValue(countDown)
            else if (countDown < 0) {
                isBlueCounting = false
                countdownBlue.postValue(SPAWN_BLUE)
            }
        }
        if (isDragonCounting) {
            val countDown =
                SPAWN_DRAGON - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - dragonStarted)
                    .toInt()
            if (countDown >= 0) countdownDragon.postValue(countDown)
            else if (countDown < 0) {
                isDragonCounting = false
                countdownDragon.postValue(SPAWN_DRAGON)
            }
        }
        if (isBaronCounting) {
            val countDown =
                SPAWN_BARON - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - baronStarted)
                    .toInt()
            if (countDown >= 0) countdownBaron.postValue(countDown)
            else if (countDown < 0) {
                isBaronCounting = false
                countdownBaron.postValue(SPAWN_BARON)
            }
        }
        if (isRaptorsCounting) {
            val countDown =
                SPAWN_RAPTORS - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - raptorsStarted)
                    .toInt()
            if (countDown >= 0) countdownRaptors.postValue(countDown)
            else if (countDown < 0) {
                isRaptorsCounting = false
                countdownRaptors.postValue(SPAWN_RAPTORS)
            }
        }
        if (isWolvesCounting) {
            val countDown =
                SPAWN_WOLVES - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - wolvesStarted)
                    .toInt()
            if (countDown >= 0) countdownWolves.postValue(countDown)
            else if (countDown < 0) {
                isWolvesCounting = false
                countdownWolves.postValue(SPAWN_WOLVES)
            }
        }
        if (isKrugsCounting) {
            val countDown =
                SPAWN_KRUGS - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - krugsStarted)
                    .toInt()
            if (countDown >= 0) countdownKrugs.postValue(countDown)
            else if (countDown < 0) {
                isKrugsCounting = false
                countdownWolves.postValue(SPAWN_KRUGS)
            }
        }
        if (isGrompCounting) {
            val countDown =
                SPAWN_GROMP - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - grompStarted)
                    .toInt()
            if (countDown >= 0) countdownGromp.postValue(countDown)
            else if (countDown < 0) {
                isGrompCounting = false
                countdownWolves.postValue(SPAWN_GROMP)
            }
        }
    }

    private fun startForegroundService() {
        startCentralTimer()
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())


        val notification = currentNotification.setContentText("Jungling")

        notificationManager.notify(NOTIFICATION_ID, notification.build())

    }

    private fun updateNotification() {
        if (isOngoing) {
            val notificationActionText = "Stop"
            val stopIntent = Intent(this, JungleService::class.java).apply {
                action = ACTION_STOP_SERVICE
            }
            val pendingIntent =
                PendingIntent.getService(this, 1, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            nManager = notificationManager

            currentNotification.javaClass.getDeclaredField("mActions").apply {
                isAccessible = true
                set(currentNotification, ArrayList<NotificationCompat.Action>())
            }

            if (!isServiceKilled) {
                currentNotification = baseNotificationBuilder
                    .addAction(R.drawable.ic_favorite_border, notificationActionText, pendingIntent)

                notificationManager.notify(NOTIFICATION_ID, currentNotification.build())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICAITON_CHANNEL_ID,
            NOTIFICATION_CHANNEL_AME,
            NotificationManager.IMPORTANCE_LOW
        )

        notificationManager.createNotificationChannel(channel)
    }
}