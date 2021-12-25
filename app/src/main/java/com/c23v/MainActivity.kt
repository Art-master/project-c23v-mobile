package com.c23v

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import com.app.data.network.RetrofitBuilder
import com.app.data.network.services.AuthorizationNetworkService
import com.c23v.ui.layouts.registration.RegistrationLayout
import com.c23v.ui.theme.ApplicationsTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authorizationService: AuthorizationNetworkService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //network.connection.create(Objects::class.java)

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


