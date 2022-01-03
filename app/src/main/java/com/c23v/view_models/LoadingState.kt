package com.c23v.view_models

sealed class LoadingState(var message: String = "") {
    object Error : LoadingState()
    object Complete : LoadingState()
    object Loading : LoadingState()
}