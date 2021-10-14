package com.example.c23v

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.c23v.ui.transform.PhoneNumberVisualTransformation
import com.example.c23v.ui.theme.ApplicationsTheme
import kotlinx.coroutines.delay

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
    var isTimerEnabled by rememberSaveable { mutableStateOf(false) }
    var needPasswordShow by rememberSaveable { mutableStateOf(false) }
    var isPhoneValid by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LoginTextField { isPhoneValid = isPhoneNumber(it) }
        Spacer(modifier = Modifier.padding(20.dp))

        if (needPasswordShow) PasswordTextField()

        Spacer(modifier = Modifier.padding(20.dp))

        SuccessButton(
            isTimerEnabled = isTimerEnabled,
            isButtonActive = isPhoneValid,
            text = stringResource(id = if (needPasswordShow) R.string.re_send_sms else R.string.send_sms),
            initialValue = 30000F,
            totalTimeSec = 30,
            onTimerStateChange = {
                isTimerEnabled = it
                needPasswordShow = true
            })
    }
}

fun isPhoneNumber(str: String): Boolean {
    return str.matches("^[0-9]{3}[0-9]{7}\$".toRegex())
}

@Composable
fun LoginTextField(onNumberChange: (num: String) -> Unit) {
    var text by rememberSaveable { mutableStateOf("") }

    Row(modifier = Modifier.height(50.dp), verticalAlignment = Alignment.CenterVertically) {
        PhoneCountryCode()
        TextField(
            modifier=Modifier.fillMaxHeight(),
            value = text,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            onValueChange = {
                if (successForNumber(it)) {
                    text = it
                    onNumberChange(it)
                }
            },
            visualTransformation = PhoneNumberVisualTransformation(),
            placeholder = { Text(text = stringResource(id = R.string.phone_number)) },
            trailingIcon = {
                Icon(
                    Icons.Filled.Info,
                    contentDescription = stringResource(id = R.string.phone_description)
                )
            }
        )
    }
}

@Composable
fun PhoneCountryCode() {
    Box(
        modifier=Modifier.background(Color.LightGray).width(70.dp).fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            textAlign = TextAlign.Center,
            fontSize = 22.sp,
            color= Color.Gray,
            text = "+7"
        )
        Text(
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            text = "+7"
        )
    }
}

fun successForNumber(str: String): Boolean {
    if (str.isEmpty()) return true

    val onlyNumbersRegex = "[0-9]+".toRegex()
    if (str.length <= 10 && str.matches(onlyNumbersRegex)) return true
    return false
}


@Composable
fun PasswordTextField() {
    var text by rememberSaveable { mutableStateOf("") }
    val maxPasswordLength = 4
    Column(
        modifier = Modifier.padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        stringResource(id = R.string.app_name)
        Text(text = stringResource(id = R.string.enter_you_sms_password))
        Spacer(modifier = Modifier.padding(7.dp))
        OutlinedTextField(
            modifier = Modifier
                .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(5.dp))
                .width(130.dp)
                .alpha(0.9f),
            value = text,
            textStyle = TextStyle(
                fontSize = 25.sp,
                textAlign = TextAlign.Center
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            onValueChange = {
                if (it.length <= maxPasswordLength) text = it
            },
            placeholder = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "_ _ _ _",
                    style = TextStyle(
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center
                    )
                )
            },
        )
    }
}

@Composable
fun SuccessButton(
    isTimerEnabled: Boolean,
    isButtonActive: Boolean,
    onTimerStateChange: (enabled: Boolean) -> Unit,
    text: String,
    initialValue: Float,
    totalTimeSec: Int
) {

    var currentTime by rememberSaveable {
        mutableStateOf(totalTimeSec)
    }

    var progress by rememberSaveable {
        mutableStateOf(initialValue)
    }

    Box(
        modifier = Modifier
            .width(200.dp)
            .height(50.dp)
    ) {
        Button(
            modifier = Modifier.fillMaxSize(),
            enabled = isTimerEnabled.not() && isButtonActive,
            onClick = { onTimerStateChange(true) },
            content = {
                if (isTimerEnabled) {
                    val seconds = if (currentTime / 10 > 0) "$currentTime" else "0$currentTime"
                    TimeText("00:$seconds")
                } else Text(text)
            }
        )
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.1f),
            progress = progress
        )
        LaunchedEffect(key1 = currentTime, key2 = isTimerEnabled) {
            if (currentTime > 0 && isTimerEnabled) {
                delay(1000L)
                currentTime -= 1
                progress = currentTime / totalTimeSec.toFloat()
                if (progress <= 0) {
                    onTimerStateChange(false)
                    progress = initialValue
                    currentTime = totalTimeSec
                }
            }
        }
    }
}

@Composable
fun TimeText(time: String) {
    Text(
        text = time,
        color = Color.White,
        style = MaterialTheme.typography.h6,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold
    )
}
