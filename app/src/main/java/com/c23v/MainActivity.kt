package com.c23v

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import com.app.data.network.RetrofitBuilder
import com.app.data.network.entities.Login
import com.app.data.network.services.AuthorizationNetworkService
import com.c23v.ui.layouts.registration.RegistrationLayout
import com.c23v.ui.theme.ApplicationsTheme
import com.c23v.view_models.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val confirmationNumber = loginViewModel.getConfirmationNumber("")
        val data = confirmationNumber.value

        setContent {
            ApplicationsTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    RegistrationLayout()
                }
            }
        }
    }
}


