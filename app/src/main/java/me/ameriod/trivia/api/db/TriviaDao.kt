package me.ameriod.trivia.api.db

import android.arch.persistence.room.*
import io.reactivex.Flowable

@Dao
interface TriviaDao {
    @Query("SELECT * FROM results")
    fun getAll(): Flowable<List<Result>>

    @Query("SELECT * FROM results WHERE id = :id LIMIT 1")
    fun byId(id: Long): Flowable<Result>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(results: Result): Long

    @Delete
    fun delete(result: Result)
}