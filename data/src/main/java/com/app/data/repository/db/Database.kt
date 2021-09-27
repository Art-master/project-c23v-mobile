package com.app.data.repository.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.data.configs.Config
import com.app.data.repository.db.entities.RepositoryUser
import com.app.domain.api.Repository
import com.app.domain.entities.User

@androidx.room.Database(entities = [RepositoryUser::class], version = Config.DATABASE_VERSION)
abstract class Database : RoomDatabase() {
    val data: User? = null


    companion object : Repository {
        var instance: Database? = null

        fun getInstance(context: Context): Database {
            synchronized(Database::class) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    Database::class.java,
                    Config.DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                return instance!!
            }
        }

        fun close() {
            if (instance?.isOpen == true) {
                instance?.close()
            }

            instance = null
        }
    }
}