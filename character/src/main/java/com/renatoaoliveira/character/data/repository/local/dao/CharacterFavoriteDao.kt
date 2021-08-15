package com.renatoaoliveira.character.data.repository.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.renatoaoliveira.character.data.repository.local.entity.CharacterFavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterFavoriteDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertCharacterFavorite(characterFavoriteEntity: CharacterFavoriteEntity)

    @Delete
    suspend fun deleteCharacterFavorite(characterFavoriteEntity: CharacterFavoriteEntity)

    @Query("SELECT * FROM CharacterFavoriteEntity ORDER BY name")
    fun getCharacterFavorites(): Flow<List<CharacterFavoriteEntity>>
}