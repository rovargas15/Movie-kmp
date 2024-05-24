package local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import local.entity.MovieEntity

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<MovieEntity>)

    @Query("SELECT count(*) FROM MovieEntity WHERE category = :category")
    suspend fun countByCategory(category: String): Int

    @Query("SELECT * FROM MovieEntity WHERE category = :category")
    fun getMovieByCategory(category: String): Flow<List<MovieEntity>>

    @Query("SELECT * FROM MovieEntity WHERE id = :id")
    fun getMovieById(id: Int): Flow<MovieEntity>

    @Query("SELECT * FROM MovieEntity WHERE isFavorite = true")
    fun getAllAsFlow(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM MovieEntity")
    fun findBySearch(): Flow<List<MovieEntity>>

    @Update
    suspend fun updateMovie(movieEntity: MovieEntity): Int
}
