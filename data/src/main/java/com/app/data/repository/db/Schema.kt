package com.app.data.repository.db

object Schema {
    object Users {
        const val TABLE_NAME = "users"

        object Columns {
            const val ID = "id"
            const val NAME = "name"
            const val PHONE_NUMBER = "phone_number"
            const val AVATAR_ID = "avatar_id"
            const val STATUS = "status"
        }
    }
}