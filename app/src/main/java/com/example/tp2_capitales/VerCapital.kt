package com.example.tp2_capitales

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tp2_capitales.ui.theme.TP2CapitalesTheme
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import com.example.tp2_capitales.data.AppDatabase
import com.example.tp2_capitales.data.Capital

class VerCapital : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TP2CapitalesTheme {
                VerCapitalForm()
            }
        }
    }
}

@Composable
fun VerCapitalForm(modifier: Modifier = Modifier) {
    var nombreCapital by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current // Contexto dentro del @Composable

    var capitalesEncontradas by remember { mutableStateOf<List<Capital>?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center) // Alinea el contenido centralmente
                .fillMaxWidth()
        ) {
            Text(
                text = "Consultar una Capital",
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )
            OutlinedTextField(
                value = nombreCapital,
                onValueChange = { nombreCapital = it },
                label = { Text("Nombre de la capital a consultar") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (nombreCapital.isNotEmpty()) {
                        coroutineScope.launch {
                            val db = AppDatabase.getInstance(context)
                            val capitalesFlow = db.capitalDao().findByName(nombreCapital)
                            val capitales = capitalesFlow.firstOrNull()
                            capitalesEncontradas = capitales
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Consultar capital")
            }
            Spacer(modifier = Modifier.height(16.dp))

            capitalesEncontradas?.let { listaCapitales ->
                if (listaCapitales.isNotEmpty()) {
                    listaCapitales.forEach { capital ->
                        Text("País: ${capital.paisOrigen}, Población: ${capital.cantidad_poblacion}")
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                } else {
                    Text("No se encontraron capitales con ese nombre")
                }
            } ?: Text("No se ha realizado ninguna búsqueda")
        }

        Button(
            onClick = {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Volver al inicio")
        }
    }
}