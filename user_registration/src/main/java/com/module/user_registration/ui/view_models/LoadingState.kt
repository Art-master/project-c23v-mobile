package com.module.user_registration.ui.view_models

sealed class LoadingState {
    object None : LoadingState()
    class Error(var message: String = "") : LoadingState()
    object Complete : LoadingState()
    object Loading : LoadingState()
}