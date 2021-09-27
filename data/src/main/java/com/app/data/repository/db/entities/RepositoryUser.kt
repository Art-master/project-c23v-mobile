package com.app.data.repository.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.data.repository.db.Schema
import com.app.data.repository.db.Schema.Users.Columns
import com.app.domain.entities.User

@Entity(tableName = Schema.Users.TABLE_NAME)
class RepositoryUser(

    @PrimaryKey
    @ColumnInfo(name = Columns.ID)
    val id: Long,

    @ColumnInfo(name = Columns.NAME)
    val name: String,

    @ColumnInfo(name = Columns.PHONE_NUMBER)
    val phoneNumber: Int,

    @ColumnInfo(name = Columns.AVATAR_ID)
    val avatarId: Long,

    @ColumnInfo(name = Columns.STATUS)
    val status: String
) {

    companion object {
        fun fromUser(user: User): RepositoryUser {
            return RepositoryUser(
                id = user.id,
                name = user.name,
                phoneNumber = user.phoneNumber,
                avatarId = user.avatarId,
                status = user.status
            )
        }
    }
}