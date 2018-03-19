package me.ameriod.trivia.api.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [Result::class], version = 1, exportSchema = false)
abstract class TriviaDatabase : RoomDatabase() {

    abstract fun resultDao(): TriviaDao
}