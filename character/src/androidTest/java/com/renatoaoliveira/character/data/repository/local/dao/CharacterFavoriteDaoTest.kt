package com.renatoaoliveira.character.data.repository.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.renatoaoliveira.character.data.repository.local.CharacterDatabase
import com.renatoaoliveira.character.data.repository.local.entity.CharacterFavoriteEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterFavoriteDaoTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private lateinit var db: CharacterDatabase

    private lateinit var dao: CharacterFavoriteDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(context, CharacterDatabase::class.java).build()
        dao = db.characterFavoriteDao
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getCharacterFavorites_whenDatabaseHasNotData_shouldReturnEmptyList() = runBlocking {
        // ACT
        val favoritesList = dao.getCharacterFavorites().first()

        // ARRANGE
        assertEquals(emptyList<CharacterFavoriteEntity>(), favoritesList)
    }

    @Test
    fun insertCharacterFavorites_shouldInsertCharacter() = runBlocking {
        // ACT
        dao.insertCharacterFavorite(FAVORITE_ENTITY)

        // ASSERT
        val favoriteList = dao.getCharacterFavorites().first()
        assertEquals(listOf(FAVORITE_ENTITY), favoriteList)
    }

    @Test
    fun deleteCharacterFavorite_shouldRemoveCharacterFromFavorites() = runBlocking {
        // ARRANGE
        dao.insertCharacterFavorite(FAVORITE_ENTITY)

        // ACT
        dao.deleteCharacterFavorite(FAVORITE_ENTITY)

        // ASSERT
        val favoriteList = dao.getCharacterFavorites().first()
        assertEquals(emptyList<CharacterFavoriteEntity>(), favoriteList)
    }

    companion object {
        val FAVORITE_ENTITY = CharacterFavoriteEntity(
            1,
            "name",
            "description",
            "https:tumbnail.url.ext"
        )
    }
}