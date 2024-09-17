package com.example.tp2_capitales

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.tp2_capitales.data.Capital

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TP2CapitalesTheme {
                HomePage()
            }
        }
    }

    @Composable
    fun HomePage() {
        var capitals by remember { mutableStateOf<List<Capital>>(emptyList()) }
        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            coroutineScope.launch {
                val db = AppDatabase.getInstance(context)
                db.capitalDao().getAll().collect { capitalsList ->
                    capitals = capitalsList
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Trabajo Práctico 2 - Capitales",
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 32.dp)
            )
            Button(
                onClick = {
                    val intent = Intent(this@MainActivity, CargarCapital::class.java)
                    startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cargar nueva capital")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val intent = Intent(this@MainActivity, VerCapital::class.java)
                    startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Consultar capital")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val intent = Intent(this@MainActivity, EliminarCapital::class.java)
                    startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Eliminar capital")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Lista de Capitales", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                items(capitals) { capital ->
                    CapitalItem(capital)
                }
            }
        }
    }

    @Composable
    fun CapitalItem(capital: Capital) {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            Text("Nombre: ${capital.nombreCapital}")
            Text("País: ${capital.paisOrigen}")
            Text("Población: ${capital.cantidad_poblacion}")
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
