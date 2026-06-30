package de.knesch.handball.referee.presentation

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.wear.tiles.TileService
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester
import de.knesch.handball.referee.service.MainComplicationService
import de.knesch.handball.referee.service.MainTileService
import de.knesch.handball.referee.service.MatchRepository
import de.knesch.handball.referee.service.MatchService
import de.knesch.handball.referee.service.TimeComplicationService
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class MatchViewModel(application: Application) : AndroidViewModel(application) {
    private val spiel = MatchRepository.spiel
    private val prefs = application.getSharedPreferences("handball_prefs", Context.MODE_PRIVATE)

    var toreHeim by mutableIntStateOf(spiel.goalsHomeTeam)
        private set
    var toreGast by mutableIntStateOf(spiel.goalsGuestTeam)
        private set
    var elapsedMillis by mutableLongStateOf(spiel.stopWatch.getTimeElapsed())
        private set
    var isRunning by mutableStateOf(spiel.stopWatch.isRunning)
        private set

    var colorHeim by mutableStateOf(Color.Black)
    var colorGast by mutableStateOf(Color.Black)

    var useOngoingActivity by mutableStateOf(
        prefs.getBoolean("use_ongoing_activity", false),
    )

    fun updateOngoingActivity(enabled: Boolean) {
        useOngoingActivity = enabled
        prefs.edit { putBoolean("use_ongoing_activity", enabled) }
    }

    private fun startMatchService() {
        Log.i("MatchViewModel", "startMatchService called, useOngoingActivity=$useOngoingActivity")
        val intent = Intent(getApplication(), MatchService::class.java).apply {
            action = "START"
        }
        getApplication<Application>().startForegroundService(intent)
    }

    private fun stopMatchService() {
        Log.i("MatchViewModel", "stopMatchService called")
        val intent = Intent(getApplication(), MatchService::class.java).apply {
            action = "STOP"
        }
        getApplication<Application>().startForegroundService(intent)
    }

    private fun updateMatchService() {
        if (isRunning && useOngoingActivity) {
            val intent = Intent(getApplication(), MatchService::class.java).apply {
                action = "UPDATE"
            }
            getApplication<Application>().startForegroundService(intent)
        }
        updateTileAndComplication()
    }

    private fun updateTileAndComplication() {
        TileService.getUpdater(getApplication())
            .requestUpdate(MainTileService::class.java)

        val componentName = ComponentName(getApplication(), MainComplicationService::class.java)
        ComplicationDataSourceUpdateRequester.create(getApplication(), componentName)
            .requestUpdateAll()

        val timeComponentName = ComponentName(getApplication(), TimeComplicationService::class.java)
        ComplicationDataSourceUpdateRequester.create(getApplication(), timeComponentName)
            .requestUpdateAll()
    }

    init {
        viewModelScope.launch {
            while (isActive) {
                if (isRunning) {
                    elapsedMillis = spiel.stopWatch.getTimeElapsed()
                }
                delay(100.milliseconds)
            }
        }
    }

    fun addTorHeim() {
        toreHeim = spiel.increaseGoalsHomeTeam()
        updateMatchService()
    }

    fun removeTorHeim() {
        toreHeim = spiel.decreaseGoalsHomeTeam()
        updateMatchService()
    }

    fun addTorGast() {
        toreGast = spiel.increaseGoalsGuestTeam()
        updateMatchService()
    }

    fun removeTorGast() {
        toreGast = spiel.decreaseGoalsGuestTeam()
        updateMatchService()
    }

    fun toggleStopWatch() {
        if (isRunning) {
            spiel.stopWatch.stop()
            isRunning = false
            if (useOngoingActivity) {
                stopMatchService()
            }
        } else {
            spiel.stopWatch.start()
            isRunning = true
            if (useOngoingActivity) {
                startMatchService()
            }
        }
        elapsedMillis = spiel.stopWatch.getTimeElapsed()
        updateTileAndComplication()
    }

    fun reset() {
        spiel.reset()
        toreHeim = 0
        toreGast = 0
        elapsedMillis = 0
        isRunning = false
        if (useOngoingActivity) {
            stopMatchService()
        }
        updateTileAndComplication()
    }

    fun halftime() {
        spiel.resetStopWatch()
        elapsedMillis = 0
        isRunning = false
        if (useOngoingActivity) {
            stopMatchService()
        }
        updateTileAndComplication()
    }
}
