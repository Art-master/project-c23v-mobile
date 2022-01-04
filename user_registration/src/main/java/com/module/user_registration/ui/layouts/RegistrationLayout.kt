package com.module.user_registration.ui.layouts

import android.telephony.TelephonyManager
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.module.user_registration.ui.transform.PhoneNumberVisualTransformation
import com.module.user_registration.ui.transform.SmsPasswordVisualTransformation
import com.module.user_registration.ui.view_models.LoadingState
import com.module.user_registration.ui.view_models.AuthViewModel
import com.module.user_registration.R
import com.module.user_registration.managers.TelephonyManagerWrapper
import kotlinx.coroutines.delay

@Composable
fun RegistrationLayout(authViewModel: AuthViewModel = viewModel()) {
    var sendSmsButtonClicked by rememberSaveable { mutableStateOf(false) }
    var callButtonClicked by rememberSaveable { mutableStateOf(false) }
    var needSmsPasswordFieldShow by rememberSaveable { mutableStateOf(false) }
    var isPhoneValid by rememberSaveable { mutableStateOf(false) }
    var lastPhoneValue by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LoginTextField(
            enabled = sendSmsButtonClicked.not() && callButtonClicked.not(),
            isLoginValid = isPhoneValid,
            onSetLoginEditable = {
                needSmsPasswordFieldShow = false
                sendSmsButtonClicked = false
                callButtonClicked = false
            }) {
            isPhoneValid = isPhoneNumber(it)
            if (needSmsPasswordFieldShow && lastPhoneValue != it) {
                needSmsPasswordFieldShow = false
            }
            lastPhoneValue = it
        }
        Spacer(modifier = Modifier.padding(20.dp))

        if (needSmsPasswordFieldShow) PasswordTextField()

        Spacer(modifier = Modifier.padding(20.dp))

        SuccessButton(
            isTimerEnabled = sendSmsButtonClicked,
            isButtonActive = false, //NOTE in future can be isPhoneValid && actionRan.not()
            text = stringResource(id = if (needSmsPasswordFieldShow) R.string.re_send_sms else R.string.send_sms),
            initialValue = 30000F,
            totalTimeSec = 30,
            onTimerStateChange = {
                sendSmsButtonClicked = it
                needSmsPasswordFieldShow = true
            })

        Spacer(modifier = Modifier.padding(10.dp))

        PhoneCallButton(
            isButtonActive = isPhoneValid && callButtonClicked.not(),
            isShowLoading = callButtonClicked,
            requestPhoneNumber = {
                callButtonClicked = true
                authViewModel.setLoadingState(LoadingState.Loading)
                authViewModel.fetchConfirmationNumber("+79169057104")
            })

        if (callButtonClicked) {
            PhoneConfirmationAlertDialog(authViewModel) {
                needSmsPasswordFieldShow = false
                sendSmsButtonClicked = false
                callButtonClicked = false
            }
        }
    }
}

@Composable
fun PhoneConfirmationAlertDialog(model: AuthViewModel, onRequest: (Boolean) -> Unit) {

    val openDialog = remember { mutableStateOf(true) }
    val phoneNumber = model.getPhoneNumber()
    val context = LocalContext.current
    val prevCallState = remember { mutableStateOf(-1) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
                onRequest.invoke(false)
            },
            title = { Text(stringResource(R.string.phone_confirmation)) },
            text = {
                when {
                    model.getLoadingState() is LoadingState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.alpha(0.3f))
                    }
                    model.getLoadingState() is LoadingState.Error -> {
                        val error = (model.getLoadingState() as LoadingState.Error).message
                        Text(
                            text = "${stringResource(id = R.string.error)}: $error",
                            color = MaterialTheme.colors.error
                        )
                    }
                    else -> Text(stringResource(R.string.phone_confirmation, phoneNumber))
                }
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(0.5f),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.error,
                            contentColor = Color.White
                        ),
                        onClick = {
                            openDialog.value = false
                            model.clearPhoneNumber()
                            onRequest.invoke(false)
                        }

                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Clear,
                            contentDescription = stringResource(id = R.string.phone_description)
                        )
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth(1f),
                        enabled = phoneNumber.isEmpty().not(),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Green,
                            contentColor = Color.White
                        ),
                        onClick = {
                            openDialog.value = false
                            TelephonyManagerWrapper.also { mgr ->
                                mgr.makeCall(context, phoneNumber = phoneNumber) { state ->
                                    if (mgr.callEnded(prevCallState.value, state)) {
                                        onRequest.invoke(true)
                                        prevCallState.value = -1
                                        model.checkAuthStatus()
                                        return@makeCall true
                                    }
                                    prevCallState.value = state
                                    return@makeCall false
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Call,
                            contentDescription = stringResource(id = R.string.phone_description)
                        )
                    }
                }
            }

        )
    }
}


fun isPhoneNumber(str: String): Boolean {
    return str.matches("^[0-9]{3}[0-9]{7}\$".toRegex())
}

@Composable
fun LoginTextField(
    enabled: Boolean, isLoginValid: Boolean,
    onSetLoginEditable: (editable: Boolean) -> Unit,
    onNumberChange: (num: String) -> Unit
) {

    var text by rememberSaveable { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    if (enabled.not()) {
        focusManager.clearFocus()
    }

    Row(
        modifier = Modifier
            .height(50.dp)
            .padding(horizontal = 50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxHeight(),
            value = text,
            enabled = enabled,
            isError = text.isNotEmpty() && isLoginValid.not(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            onValueChange = {
                if (successForPhoneNumber(it)) {
                    text = it
                    onNumberChange(it)
                }
            },
            visualTransformation = PhoneNumberVisualTransformation("+7"),
            placeholder = { Text(text = stringResource(id = R.string.phone_number)) },
            trailingIcon = {
                if (enabled.not()) {
                    Icon(
                        modifier = Modifier.clickable { onSetLoginEditable(true) },
                        imageVector = Icons.Filled.Edit,
                        contentDescription = stringResource(id = R.string.phone_description)
                    )
                }
            }
        )
    }
}

fun successForPhoneNumber(str: String): Boolean {
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
            visualTransformation = SmsPasswordVisualTransformation()
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

    fun resetTimer() {
        onTimerStateChange(false)
        progress = initialValue
        currentTime = totalTimeSec
    }

    if (isTimerEnabled.not() && progress != initialValue) {
        progress = initialValue
        currentTime = totalTimeSec
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
                if (progress <= 0) resetTimer()
            }
        }
    }
}

@Composable
fun PhoneCallButton(
    isButtonActive: Boolean,
    isShowLoading: Boolean,
    requestPhoneNumber: () -> Unit
) {

    Box(
        modifier = Modifier
            .width(200.dp)
            .height(50.dp),
        contentAlignment = Alignment.Center

    ) {
        Button(
            modifier = Modifier.fillMaxSize(),
            enabled = isButtonActive,
            onClick = { requestPhoneNumber() },
            content = {
                Text(stringResource(R.string.call_on_phone))
            }
        )
        if (isShowLoading) {
            CircularProgressIndicator(modifier = Modifier.alpha(0.3f))
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