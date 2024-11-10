package com.example.gestionmedico

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SendEmailScreen()
        }
    }
}

@Composable
fun SendEmailScreen() {
    var email by remember { mutableStateOf("") }
    var pressArt by remember { mutableStateOf("") }
    var temperature by remember { mutableStateOf("") }
    var medications by remember { mutableStateOf("") }
    var sleepHours by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        loadDataFromFirebase(context) { medicalData ->
            pressArt = medicalData["presion_arterial"] ?: ""
            temperature = medicalData["temperatura"] ?: ""
            medications = medicalData["medicamentos"] ?: ""
            sleepHours = medicalData["horas_sueño"] ?: ""
            name = medicalData["nombre"] ?: ""
            lastName = medicalData["apellido"] ?: ""
            age = medicalData["edad"] ?: ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Presión Arterial: $pressArt")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Temperatura: $temperature")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Medicamentos: $medications")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Horas de Sueño: $sleepHours")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Nombre: $name")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Apellido: $lastName")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Edad: $age")
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico del medico") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (email.isNotEmpty()) {
                    sendEmail(email, pressArt, temperature, medications, sleepHours, name, lastName, age, context)
                } else {
                    Toast.makeText(context, "Por favor, ingrese un correo electrónico.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar Datos por Correo")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                context.startActivity(Intent(context, MainActivity::class.java))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Almanecer datos")
        }
    }
}

fun loadDataFromFirebase(context: Context, onDataLoaded: (Map<String, String>) -> Unit) {
    val database = Firebase.database
    val myRef = database.getReference("datos_medicos")

    myRef.get().addOnSuccessListener { snapshot ->
        if (snapshot.exists()) {
            val data = snapshot.value as Map<String, String>
            onDataLoaded(data)
        } else {
            Toast.makeText(context, "Datos no encontrados.", Toast.LENGTH_SHORT).show()
        }
    }.addOnFailureListener {
        Toast.makeText(context, "Error al cargar los datos.", Toast.LENGTH_SHORT).show()
    }
}

fun sendEmail(
    email: String,
    pressArt: String,
    temperature: String,
    medications: String,
    sleepHours: String,
    name: String,
    lastName: String,
    age: String,
    context: Context
) {
    val subject = "Datos Médicos"
    val body = """
        Presión Arterial: $pressArt
        Temperatura: $temperature
        Medicamentos: $medications
        Horas de Sueño: $sleepHours
        Nombre: $name
        Apellido: $lastName
        Edad: $age
    """.trimIndent()

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "message/rfc822"
        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }

    try {
        context.startActivity(Intent.createChooser(intent, "Enviar correo"))
    } catch (e: android.content.ActivityNotFoundException) {
        Toast.makeText(context, "No se encontró ninguna aplicación de correo.", Toast.LENGTH_SHORT).show()
    }
}

