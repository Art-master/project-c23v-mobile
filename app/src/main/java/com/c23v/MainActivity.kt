package com.c23v

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.*
import com.module.user_registration.ui.layouts.RegistrationLayout
import com.c23v.ui.theme.ApplicationsTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.c23v.ui.layouts.UsersListLayout
import com.module.user_registration.ui.view_models.AuthViewModel


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ApplicationsTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainLayout()
                }
            }
        }
    }

    @Composable
    fun MainLayout(authViewModel: AuthViewModel = viewModel()) {
        authViewModel.checkAuthStatus()

        when (authViewModel.authorized()) {
            false -> RegistrationLayout(authViewModel)
            true -> UsersListLayout()
        }
    }
}


