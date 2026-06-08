package de.knesch.handball.referee

import de.knesch.handball.referee.service.HandballMatch
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HandballMatchTest {

    private lateinit var match: HandballMatch

    @Before
    fun setUp() {
        match = HandballMatch()
    }

    @Test
    fun initial_scores_are_zero() {
        assertEquals(0, match.goalsHomeTeam)
        assertEquals(0, match.goalsGuestTeam)
    }

    @Test
    fun increaseGoalsHomeTeam_adds_one() {
        match.increaseGoalsHomeTeam()
        assertEquals(1, match.goalsHomeTeam)
    }

    @Test
    fun decreaseGoalsHomeTeam_subtracts_one_but_not_below_zero() {
        match.increaseGoalsHomeTeam()
        match.increaseGoalsHomeTeam()
        match.decreaseGoalsHomeTeam()
        assertEquals(1, match.goalsHomeTeam)

        match.decreaseGoalsHomeTeam()
        match.decreaseGoalsHomeTeam() // Should stay at 0
        assertEquals(0, match.goalsHomeTeam)
    }

    @Test
    fun increaseGoalsGuestTeam_adds_one() {
        match.increaseGoalsGuestTeam()
        assertEquals(1, match.goalsGuestTeam)
    }

    @Test
    fun decreaseGoalsGuestTeam_subtracts_one_but_not_below_zero() {
        match.increaseGoalsGuestTeam()
        match.decreaseGoalsGuestTeam()
        assertEquals(0, match.goalsGuestTeam)

        match.decreaseGoalsGuestTeam() // Should stay at 0
        assertEquals(0, match.goalsGuestTeam)
    }

    @Test
    fun reset_clears_scores_and_stopwatch() {
        match.increaseGoalsHomeTeam()
        match.increaseGoalsGuestTeam()
        match.stopWatch.start()

        match.reset()

        assertEquals(0, match.goalsHomeTeam)
        assertEquals(0, match.goalsGuestTeam)
        assertEquals(0L, match.stopWatch.getTimeElapsed())
    }

    @Test
    fun resetStopWatch_only_resets_time() {
        match.increaseGoalsHomeTeam()
        match.stopWatch.start()

        match.resetStopWatch()

        assertEquals(1, match.goalsHomeTeam)
        assertEquals(0L, match.stopWatch.getTimeElapsed())
    }
}
