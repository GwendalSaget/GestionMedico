package com.example.gestionmedico

import android.content.Context
import android.content.Intent
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.firebase.database.FirebaseDatabase


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MedicalDataScreenContent()
        }
    }
}

@Composable
fun MedicalDataScreenContent() {
    var pressArt by remember { mutableStateOf("") }
    var temperature by remember { mutableStateOf("") }
    var medications by remember { mutableStateOf("") }
    var sleepHours by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        TextField(
            value = pressArt,
            onValueChange = { pressArt = it },
            label = { Text("Presión Arterial (número con coma)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))


        TextField(
            value = temperature,
            onValueChange = { temperature = it },
            label = { Text("Temperatura (número con coma)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = medications,
            onValueChange = { medications = it },
            label = { Text("Medicamentos (opcional)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = sleepHours,
            onValueChange = { sleepHours = it },
            label = { Text("Horas de sueño") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Apellido") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Edad") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                saveMedicalData(
                    pressArt, temperature, medications,
                    sleepHours, name, lastName, age, context
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Datos Médicos")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                context.startActivity(Intent(context, MailActivity::class.java))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar datos")
        }
    }
}

fun saveMedicalData(
    pressArt: String,
    temperature: String,
    medications: String,
    sleepHours: String,
    name: String,
    lastName: String,
    age: String,
    context: Context
) {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("datos_medicos")

    val medicalData = hashMapOf(
        "presion_arterial" to pressArt,
        "temperatura" to temperature,
        "medicamentos" to medications,
        "horas_sueño" to sleepHours,
        "nombre" to name,
        "apellido" to lastName,
        "edad" to age
    )

    myRef.setValue(medicalData)
        .addOnSuccessListener {
            Toast.makeText(context, "Datos médicos guardados correctamente.", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener {
            Toast.makeText(context, "Error al guardar los datos médicos.", Toast.LENGTH_SHORT).show()
        }
}
