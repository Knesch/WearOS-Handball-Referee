package de.knesch.handball.referee.service

class HandballMatch {

    var goalsHomeTeam = 0
        private set
    var goalsGuestTeam = 0
        private set
    val stopWatch = StopWatch()

    fun increaseGoalsHomeTeam(): Int {
        goalsHomeTeam++
        return goalsHomeTeam
    }

    fun decreaseGoalsHomeTeam(): Int {
        if (goalsHomeTeam > 0) goalsHomeTeam--
        return goalsHomeTeam
    }

    fun increaseGoalsGuestTeam(): Int {
        goalsGuestTeam++
        return goalsGuestTeam
    }

    fun decreaseGoalsGuestTeam(): Int {
        if (goalsGuestTeam > 0) goalsGuestTeam--
        return goalsGuestTeam
    }

    fun reset() {
        goalsHomeTeam = 0
        goalsGuestTeam = 0
        stopWatch.reset()
    }

    fun resetStopWatch() {
        stopWatch.reset()
    }
}
