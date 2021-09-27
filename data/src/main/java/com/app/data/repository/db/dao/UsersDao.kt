package com.app.data.repository.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.app.domain.api.UsersRepositoryApi
import com.app.domain.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao : UsersRepositoryApi {

    @Query("SELECT * FROM users WHERE id=:id")
    override fun findUserById(id: Long): Flow<User>
}