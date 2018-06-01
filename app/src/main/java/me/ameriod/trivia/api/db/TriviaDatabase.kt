package me.ameriod.trivia.api.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Result::class], version = 1, exportSchema = false)
abstract class TriviaDatabase : RoomDatabase() {

    abstract fun resultDao(): TriviaDao
}