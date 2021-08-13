package com.renatoaoliveira.character.data.repository.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.renatoaoliveira.character.data.repository.local.entity.CharacterFavorite
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterFavoriteDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertCharacterFavorite(characterFavorite: CharacterFavorite)

    @Delete
    suspend fun deleteCharacterFavorite(characterFavorite: CharacterFavorite)

    @Query("SELECT * FROM CharacterFavorite ORDER BY name")
    fun getCharacterFavorites(): Flow<List<CharacterFavorite>>
}