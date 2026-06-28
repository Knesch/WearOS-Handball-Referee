package de.knesch.handball.referee.service

class StopWatch(private val timeProvider: () -> Long = { System.currentTimeMillis() }) {

    private var currentStartTime = 0L
    private var previousTimeElapsed = 0L
    var isRunning = false
        private set

    fun start() {
        if (!isRunning) {
            currentStartTime = timeProvider()
            isRunning = true
        }
    }

    fun stop() {
        if (isRunning) {
            previousTimeElapsed += timeProvider() - currentStartTime
            isRunning = false
        }
    }

    fun reset() {
        isRunning = false
        previousTimeElapsed = 0L
        currentStartTime = 0L
    }

    fun getTimeElapsed(): Long {
        if (isRunning) {
            return previousTimeElapsed + (timeProvider() - currentStartTime)
        }
        return previousTimeElapsed
    }

}
