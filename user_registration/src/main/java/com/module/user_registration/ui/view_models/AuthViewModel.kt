package com.module.user_registration.ui.view_models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.app.data.network.services.AuthorizationNetworkService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authorizationService: AuthorizationNetworkService
) : ViewModel() {

    private val loadingState: MutableState<LoadingState> = mutableStateOf(LoadingState.None)

    private val phoneNumber = mutableStateOf("")

    private val authorized = mutableStateOf<Boolean?>(null)

    fun fetchConfirmationNumber(phone: String) {
        loadingState.value = LoadingState.Loading
        viewModelScope.launch {
            try {
                val data = authorizationService.getConfirmationNumber(phone)
                phoneNumber.value = data.body()!!
                loadingState.value = LoadingState.Complete

            } catch (e: SocketTimeoutException) {
                loadingState.value = LoadingState.Error(e.message!!)
                phoneNumber.value = ""
            }
        }
    }

    fun checkAuthStatus(callback: ((isLoggedIn: Boolean) -> Unit)? = null) {
        viewModelScope.launch {
            try {
                val checkAuth = authorizationService.checkAuth()
                val result = checkAuth.code() != 401 //UNAUTHORIZED
                authorized.value = result
                callback?.invoke(result)

            } catch (e: Exception) {
                authorized.value = false
                callback?.invoke(false)
            }
        }
    }

    fun getLoadingState() = loadingState.value

    fun setLoadingState(state: LoadingState) {
        loadingState.value = state
    }

    fun setPhoneNumber(state: String) {
        phoneNumber.value = state
    }

    fun clearPhoneNumber() {
        phoneNumber.value = ""
    }

    fun getPhoneNumber() = phoneNumber.value

    fun authorized() = authorized.value

}