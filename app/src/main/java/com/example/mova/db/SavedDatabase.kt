package com.example.mova.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mova.data.movie.Movie
import com.example.mova.data.tv.Tv

@Database(entities = [Movie::class, Tv::class], version = 1)
@TypeConverters(Converters::class)
abstract class SavedDatabase : RoomDatabase() {

    abstract fun dao(): SavedDao

    companion object {
        private var INSTANCE: SavedDatabase? = null

        fun getInstance(context: Context): SavedDatabase {
            val temp = INSTANCE
            if (temp != null)
                return temp

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SavedDatabase::class.java,
                    "saved_db.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}