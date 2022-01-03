package com.c23v.view_models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.app.data.network.services.AuthorizationNetworkService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authorizationService: AuthorizationNetworkService
) : ViewModel() {

    private var loadingState: MutableState<LoadingState> = mutableStateOf(LoadingState.Loading)

    var phoneNumber = mutableStateOf("")
        private set

    fun fetchConfirmationNumber(phone: String) {
        viewModelScope.launch {
            try {
                val data = authorizationService.getConfirmationNumber(phone)
                phoneNumber.value = data.body()!!

            } catch (e: SocketTimeoutException) {
                loadingState.value = LoadingState.Error
                loadingState.value.message = e.message!!
                phoneNumber.value = ""
            }
        }
    }

    fun getLoadingState(): LoadingState {
        return loadingState.value
    }

    fun setLoadingState(state: LoadingState) {
        loadingState.value = state
    }

}