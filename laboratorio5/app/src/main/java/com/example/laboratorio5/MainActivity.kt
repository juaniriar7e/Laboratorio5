package com.example.laboratorio5

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TextToSpeechScreen()
                }
            }
        }
    }
}

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextToSpeechScreen() {
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var text by remember { mutableStateOf("Escribe el texto aquí") }
    var selectedLanguage by remember { mutableStateOf("Español") }
    var selectedVoice by remember { mutableStateOf("Femenina") }
    var showLanguages by remember { mutableStateOf(false) }
    var showVoices by remember { mutableStateOf(false) }

    // Inicializar TextToSpeech
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Configuración inicial
                tts?.language = Locale.getDefault()
                // Determinar idioma inicial
                selectedLanguage = when (tts?.language?.language) {
                    "es" -> "Español"
                    "en" -> "Inglés"
                    "fr" -> "Francés"
                    "de" -> "Alemán"
                    else -> "Español"
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        // Campo de texto
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            label = { Text("Texto a pronunciar") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de reproducción
        Button(
            onClick = {
                tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reproducir Audio")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Selector de idioma
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Idioma: $selectedLanguage")
            Button(onClick = { showLanguages = true }) {
                Text("Seleccionar")
            }
        }

        DropdownMenu(
            expanded = showLanguages,
            onDismissRequest = { showLanguages = false }
        ) {
            listOf("Español", "Inglés", "Francés", "Alemán").forEach { language ->
                DropdownMenuItem(
                    text = { Text(language) },
                    onClick = {
                        selectedLanguage = language
                        when (language) {
                            "Español" -> tts?.language = Locale("es", "ES")
                            "Inglés" -> tts?.language = Locale.US
                            "Francés" -> tts?.language = Locale.FRANCE
                            "Alemán" -> tts?.language = Locale.GERMANY
                        }
                        showLanguages = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Selector de voz
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Voz: $selectedVoice")
            Button(onClick = { showVoices = true }) {
                Text("Seleccionar")
            }
        }

        DropdownMenu(
            expanded = showVoices,
            onDismissRequest = { showVoices = false }
        ) {
            listOf("Femenina", "Masculina", "Niño", "Niña").forEach { voice ->
                DropdownMenuItem(
                    text = { Text(voice) },
                    onClick = {
                        selectedVoice = voice
                        when (voice) {
                            "Femenina" -> tts?.setPitch(1.1f)
                            "Masculina" -> tts?.setPitch(0.9f)
                            "Niño" -> tts?.setPitch(1.4f)
                            "Niña" -> tts?.setPitch(1.3f)
                        }
                        showVoices = false
                    }
                )
            }
        }
    }
}