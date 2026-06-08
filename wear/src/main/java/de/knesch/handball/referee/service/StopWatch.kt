package de.knesch.handball.referee.service

class StopWatch(private val timeProvider: () -> Long = { System.currentTimeMillis() }) {

    private var currentStartTime = 0L
    private var previousTimeElapsed = 0L
    private var running = false

    fun start() {
        if (!running) {
            currentStartTime = timeProvider()
            running = true
        }
    }

    fun stop() {
        if (running) {
            previousTimeElapsed += timeProvider() - currentStartTime
            running = false
        }
    }

    fun reset() {
        running = false
        previousTimeElapsed = 0L
        currentStartTime = 0L
    }

    fun getTimeElapsed(): Long {
        if (running) {
            return previousTimeElapsed + (timeProvider() - currentStartTime)
        }
        return previousTimeElapsed
    }

}
