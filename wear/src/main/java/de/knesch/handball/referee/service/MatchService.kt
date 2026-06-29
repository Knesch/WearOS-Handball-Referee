package de.knesch.handball.referee.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.LocusIdCompat
import androidx.wear.ongoing.OngoingActivity
import androidx.wear.ongoing.Status
import de.knesch.handball.referee.R
import de.knesch.handball.referee.presentation.MainActivity

class MatchService : Service() {

    private val binder = LocalBinder()
    private var isStarted = false

    companion object {
        private const val CHANNEL_ID = "match_channel"
        private const val NOTIFICATION_ID = 1
        private const val TAG = "MatchService"
    }

    inner class LocalBinder : Binder() {
        fun getService(): MatchService = this@MatchService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "MatchService created")
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "MatchService onStartCommand, isStarted=$isStarted, action=${intent?.action}")
        
        if (intent?.action == "STOP") {
            stopService()
            return START_NOT_STICKY
        }

        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
        isStarted = true

        return START_STICKY
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Handball Match",
            NotificationManager.IMPORTANCE_DEFAULT,
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        val spiel = MatchRepository.spiel
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val scoreText = "${spiel.goalsHomeTeam}:${spiel.goalsGuestTeam}"
        val startTime = System.currentTimeMillis() - spiel.stopWatch.getTimeElapsed()
        
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(scoreText)
            .setContentText("Handball Match")
            .setSmallIcon(R.drawable.ic_ongoing_activity)
            .setOngoing(true)
            .setCategory(NotificationCompat.CATEGORY_WORKOUT)
            .setContentIntent(pendingIntent)
            .setUsesChronometer(true)
            .setWhen(startTime)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)

        // Vereinfachter Status: Nur der Spielstand. 
        // Zeit wird durch 'setUsesChronometer' im System-UI oft automatisch ergänzt.
        val ongoingActivityStatus = Status.Builder()
            .addTemplate("#score#")
            .addPart("score", Status.TextPart(scoreText))
            .build()

        val ongoingActivity = OngoingActivity.Builder(
            applicationContext, NOTIFICATION_ID, notificationBuilder
        )
            .setStaticIcon(R.drawable.ic_ongoing_activity)
            .setTouchIntent(pendingIntent)
            .setStatus(ongoingActivityStatus)
            .build()

        ongoingActivity.apply(applicationContext)
        Log.i(TAG, "OngoingActivity updated: $scoreText")

        return notificationBuilder.build()
    }

    fun stopService() {
        Log.i(TAG, "stopService called")
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        isStarted = false
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "MatchService destroyed")
    }
}
