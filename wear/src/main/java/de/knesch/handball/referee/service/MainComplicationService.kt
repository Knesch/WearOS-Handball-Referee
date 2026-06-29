package de.knesch.handball.referee.service

import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceService

class MainComplicationService : ComplicationDataSourceService() {
    override fun onComplicationRequest(
        request: ComplicationRequest,
        listener: ComplicationRequestListener
    ) {
        val spiel = MatchRepository.spiel
        val scoreText = "${spiel.goalsHomeTeam}:${spiel.goalsGuestTeam}"
        
        val complicationData = ShortTextComplicationData.Builder(
            text = PlainComplicationText.Builder(text = scoreText).build(),
            contentDescription = PlainComplicationText.Builder(text = "Handball Score").build()
        ).build()
        
        listener.onComplicationData(complicationData)
    }

    override fun getPreviewData(type: ComplicationType): ComplicationData {
        return ShortTextComplicationData.Builder(
            text = PlainComplicationText.Builder(text = "0:0").build(),
            contentDescription = PlainComplicationText.Builder(text = "Handball Score").build()
        ).build()
    }
}
