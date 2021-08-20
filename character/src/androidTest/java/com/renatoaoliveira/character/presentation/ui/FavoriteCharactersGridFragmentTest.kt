package com.renatoaoliveira.character.presentation.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.platform.app.InstrumentationRegistry
import com.renatoaoliveira.character.R
import com.renatoaoliveira.character.data.repository.local.CharacterDatabase
import com.renatoaoliveira.character.data.repository.local.dao.CharacterFavoriteDao
import com.renatoaoliveira.character.data.repository.local.entity.CharacterFavoriteEntity
import com.renatoaoliveira.character.di.characterModules
import com.renatoaoliveira.character.presentation.ui.robot.characterItemRobot
import com.renatoaoliveira.character.presentation.ui.robot.characterListRobot
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

class FavoriteCharactersGridFragmentTest : KoinTest {

    private val context = InstrumentationRegistry.getInstrumentation().context

    private lateinit var koinApplication: KoinApplication

    @RelaxedMockK
    private lateinit var mockDatabase: CharacterDatabase

    @RelaxedMockK
    private lateinit var mockFavoriteDao: CharacterFavoriteDao

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        koinApplication = startKoin {
            androidContext(context)
            modules(
                characterModules,
                module(override = true) {
                    single { mockDatabase }
                }
            )
        }

        setupMock()
    }

    private fun setupMock() {
        every { mockDatabase.characterFavoriteDao } returns mockFavoriteDao
    }


    private fun getFragment() =
        launchFragmentInContainer<FavoriteCharactersGridFragment>(
            themeResId = R.style.Theme_MaterialComponents_DayNight_DarkActionBar
        )

    @After
    fun tearDown() {
        mockDatabase.close()
        stopKoin()
    }

    @Test
    fun favoriteScreen_validateSuccessLayout() {
        // ARRANGE
        every { mockFavoriteDao.getCharacterFavorites() } returns flowOf(CHARACTER_FAVORITE_LIST)
        getFragment()

        //ASSERT
        characterListRobot {
            checkListScreenVisible()
        }
        characterItemRobot {
            0.until(CHARACTER_FAVORITE_LIST.size).forEach {
                with(getCharacterViewHolder(it)) {
                    checkImage(this)
                    checkText(this)
                    checkFavIcon(this)
                }
            }
        }
    }

    @Test
    fun favoriteScreen_validateEmptyLayout() {
        every { mockFavoriteDao.getCharacterFavorites() } returns flowOf(emptyList())
        getFragment()

        characterListRobot {
            checkListScreenNotVisible()
            checkEmptyScreenVisible()
        }
    }

    @Test
    fun favoriteScreen_validateErrorLayout() {
        every { mockFavoriteDao.getCharacterFavorites() } returns flow { throw Exception() }
        getFragment()

        characterListRobot {
            checkListScreenNotVisible()
            checkErrorScreenVisible()
        }
    }

    companion object {
        val CHARACTER_FAVORITE_ENTITY_ONE = CharacterFavoriteEntity(
            1,
            "name one",
            "description one",
            "https:thumbnail.url.ext"
        )

        val CHARACTER_FAVORITE_LIST = listOf(
            CHARACTER_FAVORITE_ENTITY_ONE,
            CHARACTER_FAVORITE_ENTITY_ONE,
            CHARACTER_FAVORITE_ENTITY_ONE,
            CHARACTER_FAVORITE_ENTITY_ONE
        )
    }
}