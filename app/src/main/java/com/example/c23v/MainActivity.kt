package com.example.c23v

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Password
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.c23v.ui.theme.ApplicationsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ApplicationsTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    View()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ApplicationsTheme {
        View()
    }
}

@Composable
fun View() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginTextField()
        Spacer(modifier = Modifier.padding(20.dp))
        PasswordTextField()
    }
}

@Composable
fun LoginTextField() {
    var text by rememberSaveable { mutableStateOf("") }

    TextField(
        value = text,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        onValueChange = { text = it },
        placeholder = { Text(text = "Phone number") },
        leadingIcon = { Icon(Icons.Filled.Login, contentDescription = "Localized description") },
        trailingIcon = { Icon(Icons.Filled.Info, contentDescription = "Localized description") }
    )
}


@Composable
fun PasswordTextField() {
    var text by rememberSaveable { mutableStateOf("") }

    TextField(
        value = text,
        singleLine = true,
        onValueChange = { text = it },
        placeholder = { Text(text = "Password") },
    )
}