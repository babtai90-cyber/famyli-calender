package lt.babtai.familycalendar

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { FamilyCalendarApp() }
    }
}

data class CalendarEvent(val title: String, val time: String, val person: String)

@Composable
fun FamilyCalendarApp() {
    val bg = Color(0xFF0B0D10)
    val card = Color(0xFF15191F)
    val text = Color(0xFFF2F4F7)
    val muted = Color(0xFF9AA3AF)
    var showUpdate by remember { mutableStateOf(false) }
    var updateUrl by remember { mutableStateOf<String?>(null) }
    var status by remember { mutableStateOf("Prisijungta") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    MaterialTheme(colorScheme = darkColorScheme(background = bg, surface = card, primary = Color(0xFF7DD3FC))) {
        Surface(modifier = Modifier.fillMaxSize(), color = bg) {
            Column(Modifier.fillMaxSize().padding(20.dp)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Family Calendar", color = text, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        Text("Rugsėjo 2026", color = muted, fontSize = 15.sp)
                    }
                    TextButton(onClick = {
                        scope.launch {
                            status = "Tikrinama..."
                            val result = checkForUpdate()
                            updateUrl = result
                            showUpdate = result != null
                            status = if (result != null) "Yra nauja versija" else "Versija naujausia"
                        }
                    }) { Text("Atnaujinti") }
                }

                Spacer(Modifier.height(18.dp))
                Text("Šiandien • 8", color = muted, fontSize = 14.sp)
                Spacer(Modifier.height(8.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.weight(1f)) {
                    items(listOf(
                        CalendarEvent("Mokykla", "08:00", "Šeima"),
                        CalendarEvent("Pietūs", "12:30", "Visi"),
                        CalendarEvent("Šeimos susitikimas", "18:00", "Tėvai")
                    )) { event ->
                        Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = card)) {
                            Row(Modifier.fillMaxWidth().padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    Text(event.title, color = text, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                                    Text(event.person, color = muted, fontSize = 13.sp)
                                }
                                Text(event.time, color = Color(0xFF7DD3FC), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Text(status, color = muted, fontSize = 12.sp, modifier = Modifier.padding(vertical = 8.dp))
                Button(onClick = { /* event creation will be wired to backend */ }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                    Text("+ Naujas įvykis")
                }
            }
        }
    }

    if (showUpdate && updateUrl != null) {
        AlertDialog(
            onDismissRequest = { showUpdate = false },
            title = { Text("Nauja versija") },
            text = { Text("Rasta nauja Family Calendar versija. Atsisiųsk ją ir Android pasiūlys atnaujinti esamą programą.") },
            confirmButton = {
                TextButton(onClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl)))
                    showUpdate = false
                }) { Text("Atsisiųsti") }
            },
            dismissButton = { TextButton(onClick = { showUpdate = false }) { Text("Vėliau") } }
        )
    }
}

private suspend fun checkForUpdate(): String? {
    // Replace with the production HTTPS endpoint when the backend is deployed.
    // Expected response: JSON containing latestVersion and apkUrl.
    return try {
        val connection = (URL("https://example.com/family-calendar/latest.json").openConnection() as HttpURLConnection).apply {
            connectTimeout = 4000
            readTimeout = 4000
            requestMethod = "GET"
        }
        connection.disconnect()
        null
    } catch (_: Exception) {
        null
    }
}
