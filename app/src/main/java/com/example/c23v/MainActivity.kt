package com.example.c23v

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
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
            .fillMaxHeight()
            .padding(top = 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LoginTextField()
        Spacer(modifier = Modifier.padding(20.dp))
        PasswordTextField()
        Spacer(modifier = Modifier.padding(20.dp))
        SuccessButton()
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
        leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = "Localized description") },
        trailingIcon = { Icon(Icons.Filled.Info, contentDescription = "Localized description") }
    )
}


@Composable
fun PasswordTextField() {
    var text by rememberSaveable { mutableStateOf("") }
    val maxPasswordLength = 4
    OutlinedTextField(
        modifier = Modifier
            .width(150.dp)
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(5.dp))
            .alpha(0.9f),
        value = text,
        textStyle = TextStyle(
            fontSize = 25.sp,
            textAlign = TextAlign.Center
        ),
        //letterSpacing = TextUnit(5f, TextUnitType.Em),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        onValueChange = {
            if (it.length <= maxPasswordLength) text = it
        },
        //placeholder = { Text(text = "Password") },
    )
}

@Composable
fun SuccessButton() {
    Button(
        modifier = Modifier.width(200.dp).height(50.dp),
        onClick = {},
        content = { Text(text = "Next") }
    )
}