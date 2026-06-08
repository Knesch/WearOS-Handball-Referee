package de.knesch.handball.referee

import de.knesch.handball.referee.service.StopWatch
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class StopWatchTest {

    private var currentTime = 1000L
    private lateinit var stopWatch: StopWatch

    @Before
    fun setUp() {
        stopWatch = StopWatch(timeProvider = { currentTime })
    }

    @Test
    fun getTimeElapsed_returns_0_initially() {
        assertEquals(0L, stopWatch.getTimeElapsed())
    }

    @Test
    fun getTimeElapsed_returns_correct_value_after_start_and_time_passing() {
        stopWatch.start()
        currentTime += 500

        assertEquals(500L, stopWatch.getTimeElapsed())
    }

    @Test
    fun getTimeElapsed_stays_constant_after_stop() {
        stopWatch.start()
        currentTime += 500
        stopWatch.stop()

        val elapsedAtStop = stopWatch.getTimeElapsed()
        assertEquals(500L, elapsedAtStop)

        currentTime += 500
        assertEquals(500L, stopWatch.getTimeElapsed())
    }

    @Test
    fun getTimeElapsed_resumes_after_second_start() {
        // First run: 500ms
        stopWatch.start()
        currentTime += 500
        stopWatch.stop()

        // Second run: 300ms
        stopWatch.start()
        currentTime += 300

        assertEquals(800L, stopWatch.getTimeElapsed())
    }

    @Test
    fun reset_clears_elapsed_time() {
        stopWatch.start()
        currentTime += 500
        stopWatch.stop()

        stopWatch.reset()
        assertEquals(0L, stopWatch.getTimeElapsed())
    }

    @Test
    fun starting_multiple_times_does_nothing_if_already_running() {
        stopWatch.start()
        currentTime += 200
        val elapsedBeforeSecondStart = stopWatch.getTimeElapsed()

        stopWatch.start() // Should not reset or change anything

        assertEquals(elapsedBeforeSecondStart, stopWatch.getTimeElapsed())

        currentTime += 100
        assertEquals(300L, stopWatch.getTimeElapsed())
    }

    @Test
    fun stop_multiple_times_does_nothing_if_not_running() {
        stopWatch.start()
        currentTime += 500
        stopWatch.stop()

        val elapsedAfterFirstStop = stopWatch.getTimeElapsed()
        currentTime += 100
        stopWatch.stop() // Should not add more time

        assertEquals(elapsedAfterFirstStop, stopWatch.getTimeElapsed())
    }
}
