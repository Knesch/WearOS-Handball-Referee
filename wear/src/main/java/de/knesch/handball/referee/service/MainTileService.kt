package de.knesch.handball.referee.service

import androidx.wear.protolayout.ActionBuilders
import androidx.wear.protolayout.ColorBuilders
import androidx.wear.protolayout.DimensionBuilders
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.TileService
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import de.knesch.handball.referee.R

class MainTileService : TileService() {
    override fun onTileRequest(requestParams: RequestBuilders.TileRequest): ListenableFuture<TileBuilders.Tile> {
        val spiel = MatchRepository.spiel
        val scoreText = "${spiel.goalsHomeTeam}:${spiel.goalsGuestTeam}"
        
        val isRunning = spiel.stopWatch.isRunning
        val statusText = if (isRunning) {
            getString(R.string.tile_status_running)
        } else {
            getString(R.string.tile_status_paused)
        }
        val statusColor = if (isRunning) 0xFF00FF00.toInt() else 0xFFFF0000.toInt()

        val root = LayoutElementBuilders.Column.Builder()
            .addContent(
                LayoutElementBuilders.Text.Builder()
                    .setText("Handball")
                    .setFontStyle(LayoutElementBuilders.FontStyle.Builder()
                        .setSize(DimensionBuilders.sp(12f))
                        .setColor(ColorBuilders.argb(0xFFBDBDBD.toInt()))
                        .build())
                    .build()
            )
            .addContent(
                LayoutElementBuilders.Text.Builder()
                    .setText(scoreText)
                    .setFontStyle(LayoutElementBuilders.FontStyle.Builder()
                        .setSize(DimensionBuilders.sp(38f))
                        .setWeight(LayoutElementBuilders.FONT_WEIGHT_BOLD)
                        .build())
                    .build()
            )
            .addContent(
                LayoutElementBuilders.Text.Builder()
                    .setText(statusText)
                    .setFontStyle(LayoutElementBuilders.FontStyle.Builder()
                        .setSize(DimensionBuilders.sp(16f))
                        .setColor(ColorBuilders.argb(statusColor))
                        .build())
                    .build()
            )
            .setModifiers(
                ModifiersBuilders.Modifiers.Builder()
                    .setClickable(
                        ModifiersBuilders.Clickable.Builder()
                            .setOnClick(
                                ActionBuilders.LaunchAction.Builder()
                                    .setAndroidActivity(
                                        ActionBuilders.AndroidActivity.Builder()
                                            .setPackageName(packageName)
                                            .setClassName("de.knesch.handball.referee.presentation.MainActivity")
                                            .addKeyToExtraMapping(
                                                "target",
                                                ActionBuilders.AndroidStringExtra.Builder()
                                                    .setValue("match")
                                                    .build()
                                            )
                                            .build()
                                    )
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .build()

        val tile = TileBuilders.Tile.Builder()
            .setResourcesVersion("1")
            .setTileTimeline(
                TimelineBuilders.Timeline.Builder()
                    .addTimelineEntry(
                        TimelineBuilders.TimelineEntry.Builder()
                            .setLayout(
                                LayoutElementBuilders.Layout.Builder()
                                    .setRoot(root)
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .build()
        return Futures.immediateFuture(tile)
    }

    override fun onTileResourcesRequest(requestParams: RequestBuilders.ResourcesRequest): ListenableFuture<ResourceBuilders.Resources> {
        return Futures.immediateFuture(
            ResourceBuilders.Resources.Builder()
                .setVersion("1")
                .build()
        )
    }
}
