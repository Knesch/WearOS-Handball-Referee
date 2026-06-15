package de.knesch.handball.referee.mobile

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.remote.interactions.RemoteActivityHelper
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.Wearable
import de.knesch.handball.referee.R
import de.knesch.handball.referee.shared.WatchDevice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import androidx.core.net.toUri

import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.safeDrawingPadding
import de.knesch.handball.referee.mobile.ui.theme.HandballSchiedsrichterTheme
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import android.content.Context
import androidx.core.content.edit
import android.content.res.Configuration
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("settings", MODE_PRIVATE)
        val lang = prefs.getString("language", null)
        if (lang != null) {
            val locale = Locale.forLanguageTag(lang)
            Locale.setDefault(locale)
            val config = Configuration(newBase.resources.configuration)
            config.setLocale(locale)
            super.attachBaseContext(newBase.createConfigurationContext(config))
        } else {
            super.attachBaseContext(newBase)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        setContent {
            HandballSchiedsrichterTheme {
                var showLanguageDialog by remember {
                    mutableStateOf(prefs.getString("language", null) == null)
                }

                if (showLanguageDialog) {
                    LanguageSelectionDialog(
                        onLanguageSelected = { languageCode ->
                            // In SharedPreferences speichern
                            prefs.edit { putString("language", languageCode) }
                            
                            // Für das System setzen
                            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
                            AppCompatDelegate.setApplicationLocales(appLocale)
                            
                            // Activity neu erstellen, um die Ressourcen neu zu laden
                            recreate()
                        }
                    )
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    WatchStatusScreen()
                }
            }
        }
    }
}

@Composable
fun LanguageSelectionDialog(onLanguageSelected: (String) -> Unit) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text(text = stringResource(id = R.string.choose_language)) },
        text = {
            Column {
                TextButton(
                    onClick = { onLanguageSelected("de") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "\uD83C\uDDE9\uD83C\uDDEA " + stringResource(id = R.string.language_german))
                }
                TextButton(
                    onClick = { onLanguageSelected("en") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "\uD83C\uDDEC\uD83C\uDDE7 " + stringResource(id = R.string.language_english))
                }
            }
        },
        confirmButton = { }
    )
}

@Composable
fun WatchStatusScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var devices by remember { mutableStateOf<List<WatchDevice>>(emptyList()) }
    var isLoading by remember { mutableStateOf(value = true) }

    val remoteActivityHelper = remember {
        RemoteActivityHelper(context, Executors.newSingleThreadExecutor())
    }

    // Listener für die Antwort-Nachricht der Uhr
    DisposableEffect(context) {
        val messageClient = Wearable.getMessageClient(context)
        val listener =
            com.google.android.gms.wearable.MessageClient.OnMessageReceivedListener { event ->
                if (event.path == "/verify_install_response") {
                    devices = devices.map { device ->
                        if (device.id == event.sourceNodeId) device.copy(isAppInstalled = true) else device
                    }
                }
            }
        messageClient.addListener(listener)
        onDispose { messageClient.removeListener(listener) }
    }

    fun refreshDevices() {
        android.util.Log.i("HandballReferee", "Suche nach Geräten gestartet...")
        scope.launch {
            isLoading = true
            devices = getWatchDevices(context)
            isLoading = false

            // Ping an alle gefundenen Nodes senden, um Installation zu prüfen
            val messageClient = Wearable.getMessageClient(context)
            devices.forEach { device ->
                messageClient.sendMessage(device.id, "/verify_install", byteArrayOf(1))
            }
        }
    }

    LaunchedEffect(Unit) {
        refreshDevices()
    }

    DisposableEffect(context) {
        val capabilityClient = Wearable.getCapabilityClient(context)
        val listener = CapabilityClient.OnCapabilityChangedListener { _ ->
            // Bei jeder Änderung der Capabilities aktualisieren wir die gesamte Liste
            refreshDevices()
        }

        capabilityClient.addListener(listener, "handball_referee_app")

        onDispose {
            capabilityClient.removeListener(listener)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = de.knesch.handball.referee.shared.R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Image(
            painter = painterResource(id = de.knesch.handball.referee.shared.R.drawable.tile_preview),
            contentDescription = "Tile Preview",
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 24.dp)
        )

        if (isLoading) {
            CircularProgressIndicator()
        } else if (devices.isEmpty()) {
            Text(text = stringResource(id = R.string.no_watches_found))
            Button(onClick = { refreshDevices() }, modifier = Modifier.padding(top = 16.dp)) {
                Text(text = stringResource(id = R.string.search_again))
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = devices) { device ->
                    DeviceItem(device) {
                        val intent = Intent(Intent.ACTION_VIEW)
                            .addCategory(Intent.CATEGORY_BROWSABLE)
                            .setData("market://details?id=de.knesch.handball.referee".toUri())

                        remoteActivityHelper.startRemoteActivity(intent, device.id)
                    }
                }
            }

            Button(
                onClick = { refreshDevices() },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(stringResource(id = R.string.refresh_list))
            }
        }
    }
}

@Composable
fun DeviceItem(device: WatchDevice, onInstallClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        content = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = device.displayName, style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = if (device.isAppInstalled) stringResource(id = R.string.app_installed) else stringResource(
                                id = R.string.app_not_installed
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = if (device.isAppInstalled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                        )
                    }

                    if (!device.isAppInstalled) {
                        Button(onClick = onInstallClick) {
                            Text(stringResource(id = R.string.install_on_watch))
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = stringResource(id = R.string.installed_desc),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    )
}

private suspend fun getWatchDevices(context: Context): List<WatchDevice> =
    withContext(Dispatchers.IO) {
        val nodeClient = Wearable.getNodeClient(context)
        val capabilityClient = Wearable.getCapabilityClient(context)

        val nodes = try {
            Tasks.await(nodeClient.connectedNodes)
        } catch (_: Exception) {
            emptyList()
        }

        // Wir versuchen es zuerst über Capabilities
        val nodesWithApp = try {
            val capabilityInfo = Tasks.await(
                capabilityClient.getCapability("handball_referee_app", CapabilityClient.FILTER_ALL)
            )
            capabilityInfo.nodes.asSequence().map { it.id }.toSet()
        } catch (_: Exception) {
            emptySet()
        }

        nodes.map { node ->
            WatchDevice(
                id = node.id,
                displayName = node.displayName,
                isAppInstalled = nodesWithApp.contains(node.id)
            )
        }
    }
