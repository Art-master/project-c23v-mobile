package com.c23v.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.data.network.services.AuthorizationNetworkService
import com.c23v.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.reflect.KProperty

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authorizationService: AuthorizationNetworkService) : ViewModel() {

    private val getConfirmationNumber: MutableLiveData<String> by lazy {
        MutableLiveData<String>().also {
            fetchConfirmationNumber("+79169057104")
        }
    }

    fun getConfirmationNumber(phone: String): LiveData<String> {
        return getConfirmationNumber
    }

    private fun fetchConfirmationNumber(phoneNumber: String) {
        authorizationService.getConfirmationNumber(phoneNumber)!!.execute()
    }

    operator fun getValue(mainActivity: MainActivity, property: KProperty<*>): Any {
        TODO("Not yet implemented")
    }
}