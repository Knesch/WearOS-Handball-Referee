package de.knesch.handball.referee.service

import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.CountUpTimeReference
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.data.TimeDifferenceComplicationText
import androidx.wear.watchface.complications.data.TimeDifferenceStyle
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceService
import java.time.Instant

class TimeComplicationService : ComplicationDataSourceService() {
    override fun onComplicationRequest(
        request: ComplicationRequest,
        listener: ComplicationRequestListener
    ) {
        val spiel = MatchRepository.spiel
        
        val complicationText = if (spiel.stopWatch.isRunning) {
            // CountUpTimeReference zählt VON einem Zeitpunkt in der Vergangenheit HOCH.
            // Der Startzeitpunkt für das System ist: Jetzt minus bereits abgelaufene Zeit.
            val startTime = Instant.now().minusMillis(spiel.stopWatch.getTimeElapsed())
            TimeDifferenceComplicationText.Builder(
                style = TimeDifferenceStyle.STOPWATCH,
                countUpTimeReference = CountUpTimeReference(startTime)
            ).build()
        } else {
            val elapsedMillis = spiel.stopWatch.getTimeElapsed()
            val minutes = (elapsedMillis / 1000) / 60
            val seconds = (elapsedMillis / 1000) % 60
            PlainComplicationText.Builder(text = "%02d:%02d".format(minutes, seconds)).build()
        }

        val complicationData = ShortTextComplicationData.Builder(
            text = complicationText,
            contentDescription = PlainComplicationText.Builder(text = "Handball Time").build()
        ).build()
        
        listener.onComplicationData(complicationData)
    }

    override fun getPreviewData(type: ComplicationType): ComplicationData {
        return ShortTextComplicationData.Builder(
            text = PlainComplicationText.Builder(text = "00:00").build(),
            contentDescription = PlainComplicationText.Builder(text = "Handball Time").build()
        ).build()
    }
}
