package com.example.tp2_capitales

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tp2_capitales.ui.theme.TP2CapitalesTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import com.example.tp2_capitales.data.AppDatabase
import com.example.tp2_capitales.data.Capital
import kotlinx.coroutines.flow.first

class CargarCapital : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TP2CapitalesTheme {
                CapitalForm()
            }
        }
    }
}

@Composable
fun CapitalForm(modifier: Modifier = Modifier) {
    var nombreCapital by remember { mutableStateOf("") }
    var paisOrigen by remember { mutableStateOf("") }
    var cantidadPoblacion by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = nombreCapital,
                    onValueChange = { nombreCapital = it },
                    label = { Text("Nombre de la capital") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = paisOrigen,
                    onValueChange = { paisOrigen = it },
                    label = { Text("País de origen") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = cantidadPoblacion,
                    onValueChange = { cantidadPoblacion = it },
                    label = { Text("Población") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (nombreCapital.isNotEmpty() && paisOrigen.isNotEmpty() && cantidadPoblacion.toIntOrNull() != null) {
                            val capital = Capital(0, nombreCapital, paisOrigen, cantidadPoblacion.toInt())
                            coroutineScope.launch {
                                val db = AppDatabase.getInstance(context)
                                val existingCapitalFlow = db.capitalDao().findByName(nombreCapital)

                                // Recoger el primer valor del flujo
                                val existingCapitals = existingCapitalFlow.first()

                                // Verificar si existe una capital en la lista con el mismo nombre y país
                                val matchingCapital = existingCapitals?.firstOrNull { it.paisOrigen == paisOrigen }

                                if (matchingCapital != null) {
                                    snackbarHostState.showSnackbar("Ya existe la capital en el país indicado")
                                } else {
                                    db.capitalDao().insertAll(capital)
                                    snackbarHostState.showSnackbar("Capital cargada correctamente")
                                }
                            }
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Por favor, completa todos los campos correctamente")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar capital")
                }
            }

            // Botón en el margen inferior
            Button(
                onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp) // Padding opcional
                    .fillMaxWidth()
            ) {
                Text("Volver al inicio")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CapitalFormPreview() {
    TP2CapitalesTheme {
        CapitalForm()
    }
}
