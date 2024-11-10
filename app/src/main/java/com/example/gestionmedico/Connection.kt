package com.example.gestionmedico

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference

class Connexion : ComponentActivity() {

    private lateinit var database: DatabaseReference
    private var isDatabaseEmpty by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa la base de datos de Firebase
        database = FirebaseDatabase.getInstance().reference.child("usuarios")

        // Verifica si la base de datos está vacía
        checkIfDatabaseIsEmpty()

        setContent {
            ConnexionScreen(
                isDatabaseEmpty = isDatabaseEmpty,
                onCheckDatabaseEmpty = { checkIfDatabaseIsEmpty() }
            )
        }
    }

    private fun checkIfDatabaseIsEmpty() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                isDatabaseEmpty = !snapshot.exists() || snapshot.childrenCount == 0L
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Connexion, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

@Composable
fun ConnexionScreen(isDatabaseEmpty: Boolean, onCheckDatabaseEmpty: () -> Unit) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val buttonText = if (isDatabaseEmpty) "Nuevas Credenciales" else "Conectar"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Nombre de usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (isDatabaseEmpty) {
                    createNewCredentials(context, username, password, onCheckDatabaseEmpty)
                } else {
                    validateCredentials(context, username, password)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = buttonText)
        }
    }
}

fun createNewCredentials(
    context: android.content.Context,
    username: String,
    password: String,
    onCheckDatabaseEmpty: () -> Unit
) {
    val database = FirebaseDatabase.getInstance().reference.child("usuarios")
    if (username.isNotEmpty() && password.isNotEmpty()) {
        val userMap = mapOf("username" to username, "password" to password)
        database.child("0").setValue(userMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Usuario creado con éxito", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(context, "Error al crear el usuario", Toast.LENGTH_SHORT).show()
            }
            onCheckDatabaseEmpty()
        }
    } else {
        Toast.makeText(context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
    }
}

fun validateCredentials(context: android.content.Context, enteredUsername: String, enteredPassword: String) {
    val database = FirebaseDatabase.getInstance().reference.child("usuarios").child("0")
    if (enteredUsername.isNotEmpty() && enteredPassword.isNotEmpty()) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val storedUsername = snapshot.child("username").value.toString()
                val storedPassword = snapshot.child("password").value.toString()

                if (enteredUsername == storedUsername && enteredPassword == storedPassword) {
                    context.startActivity(Intent(context, MainActivity::class.java))
                } else {
                    Toast.makeText(context, "Malas credenciales", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    } else {
        Toast.makeText(context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
    }
}
