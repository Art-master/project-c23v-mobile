package com.app.domain.entities

data class User(
    val id: Long,
    val name: String,
    val phoneNumber: Int,
    val avatarId: Long,
    val status: String
)