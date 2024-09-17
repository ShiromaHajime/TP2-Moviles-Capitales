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
import kotlinx.coroutines.launch
import com.example.tp2_capitales.data.AppDatabase
import kotlinx.coroutines.flow.firstOrNull

class EliminarCapital : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TP2CapitalesTheme {
                EliminarCapitalForm()
            }
        }
    }
}

@Composable
fun EliminarCapitalForm(modifier: Modifier = Modifier) {
    var nombreCapital by remember { mutableStateOf("") }
    var nombrePais by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val db = remember { AppDatabase.getInstance(context) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Eliminar una Capital",
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp)
                )
                OutlinedTextField(
                    value = nombreCapital,
                    onValueChange = { nombreCapital = it },
                    label = { Text("Nombre de la capital a eliminar") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (nombreCapital.isNotEmpty()) {
                            coroutineScope.launch {
                                isProcessing = true

                                val capitalExists = db.capitalDao().findByName(nombreCapital).firstOrNull() != null

                                if (capitalExists) {
                                    db.capitalDao().deleteByName(nombreCapital)
                                    snackbarHostState.showSnackbar("Capital eliminada correctamente")
                                } else {
                                    snackbarHostState.showSnackbar("No existe la capital")
                                }

                                isProcessing = false
                            }
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Ingrese una capital para eliminar")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isProcessing
                ) {
                    Text("Eliminar por nombre de la capital")
                }

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = nombrePais,
                    onValueChange = { nombrePais = it },
                    label = { Text("Nombre del país para eliminar sus capitales") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (nombrePais.isNotEmpty()) {
                            coroutineScope.launch {
                                isProcessing = true

                                val capitalsInCountry = db.capitalDao().loadAllByCountry(nombrePais).firstOrNull()

                                if (!capitalsInCountry.isNullOrEmpty()) {
                                    db.capitalDao().deleteAllByCountry(nombrePais)
                                    snackbarHostState.showSnackbar("Capitales en el país eliminadas correctamente")
                                } else {
                                    snackbarHostState.showSnackbar("No hay capitales en el país indicado")
                                }

                                isProcessing = false
                            }
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Ingrese un país para eliminar todas las capitales")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isProcessing
                ) {
                    Text("Eliminar por nombre del país")
                }
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
}