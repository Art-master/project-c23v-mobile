package com.app.domain.api

import com.app.domain.entities.User
import kotlinx.coroutines.flow.Flow

interface UsersRepositoryApi : Repository {

    fun findUserById(id: Long): Flow<User>
}