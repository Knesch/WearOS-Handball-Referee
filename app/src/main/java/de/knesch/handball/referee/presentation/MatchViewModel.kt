package de.knesch.handball.referee.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.knesch.handball.referee.service.HandballMatch
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MatchViewModel : ViewModel() {
    private val spiel = HandballMatch()

    var toreHeim by mutableStateOf(0)
        private set
    var toreGast by mutableStateOf(0)
        private set
    var elapsedMillis by mutableLongStateOf(0L)
        private set
    var isRunning by mutableStateOf(false)
        private set

    var colorHeim by mutableStateOf(Color.Black)
    var colorGast by mutableStateOf(Color.Black)

    init {
        viewModelScope.launch {
            while (isActive) {
                if (isRunning) {
                    elapsedMillis = spiel.stopWatch.getTimeElapsed()
                }
                delay(100)
            }
        }
    }

    fun addTorHeim() {
        toreHeim = spiel.increaseGoalsHomeTeam()
    }

    fun removeTorHeim() {
        toreHeim = spiel.decreaseGoalsHomeTeam()
    }

    fun addTorGast() {
        toreGast = spiel.increaseGoalsGuestTeam()
    }

    fun removeTorGast() {
        toreGast = spiel.decreaseGoalsGuestTeam()
    }

    fun toggleStopWatch() {
        if (isRunning) {
            spiel.stopWatch.stop()
            isRunning = false
        } else {
            spiel.stopWatch.start()
            isRunning = true
        }
        elapsedMillis = spiel.stopWatch.getTimeElapsed()
    }

    fun reset() {
        spiel.reset()
        toreHeim = 0
        toreGast = 0
        elapsedMillis = 0
        isRunning = false
    }

    fun halftime() {
        spiel.resetStopWatch()
        elapsedMillis = 0
        isRunning = false
    }
}
