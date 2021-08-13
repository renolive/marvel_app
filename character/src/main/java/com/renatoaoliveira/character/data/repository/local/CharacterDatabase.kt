package com.renatoaoliveira.character.data.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.renatoaoliveira.character.data.repository.local.dao.CharacterFavoriteDao
import com.renatoaoliveira.character.data.repository.local.entity.CharacterFavorite

@Database(entities = [CharacterFavorite::class], version = 1, exportSchema = false)
abstract class CharacterDatabase : RoomDatabase() {
    abstract val characterFavoriteDao: CharacterFavoriteDao
}