package com.example.mova.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import com.example.mova.data.movie.Movie
import com.example.mova.data.tv.Tv

@Dao
interface SavedDao {

    @Insert(onConflict = IGNORE)
    suspend fun insertMovie(movie: Movie): Long

    @Query("select * from movies_table where id LIKE '%' || :movieId || '%'")
    fun getSavedMovie(movieId: Int): LiveData<Movie>

    @Delete
    suspend fun deleteMovie(movie: Movie)

    @Query("select * from movies_table")
    fun getSavedMovies(): LiveData<List<Movie>>

    @Insert
    suspend fun insertTv(tv: Tv): Long

    @Query("select * from tv_table where id LIKE '%' || :tvId || '%'")
    fun getSavedTv(tvId: Int): LiveData<Tv>

    @Delete
    suspend fun deleteTv(tv: Tv)

    @Query("select * from tv_table")
    fun getSavedTvShows(): LiveData<List<Tv>>



}