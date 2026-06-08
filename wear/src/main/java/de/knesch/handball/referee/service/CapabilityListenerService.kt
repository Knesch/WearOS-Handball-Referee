package de.knesch.handball.referee.service

import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class CapabilityListenerService : WearableListenerService() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path == "/verify_install") {
            val nodeId = messageEvent.sourceNodeId
            scope.launch {
                try {
                    // Wir schicken eine Antwort zurück an den Absender
                    Tasks.await(
                        Wearable.getMessageClient(this@CapabilityListenerService)
                            .sendMessage(nodeId, "/verify_install_response", byteArrayOf(1))
                    )
                } catch (e: Exception) {
                    // Fehler beim Antworten
                }
            }
        }
    }
}