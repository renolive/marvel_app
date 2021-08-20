package com.renatoaoliveira.character.presentation.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.renatoaoliveira.character.data.repository.local.CharacterDatabase
import com.renatoaoliveira.character.data.repository.local.dao.CharacterFavoriteDao
import com.renatoaoliveira.character.data.repository.remote.api.CharacterServiceAPI
import com.renatoaoliveira.character.di.characterModules
import com.renatoaoliveira.character.presentation.model.CharacterVO
import com.renatoaoliveira.character.presentation.ui.robot.characterDetailRobot
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
class CharacterDetailsActivityTest : KoinTest {

    private val context = InstrumentationRegistry.getInstrumentation().context

    private lateinit var koinApplication: KoinApplication

    @RelaxedMockK
    private lateinit var mockDatabase: CharacterDatabase

    @RelaxedMockK
    private lateinit var mockFavoriteDao: CharacterFavoriteDao

    @RelaxedMockK
    private lateinit var mockServiceApi: CharacterServiceAPI

    lateinit var activityScenario: ActivityScenario<CharacterDetailsActivity>

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        koinApplication = startKoin {
            androidContext(context)
            modules(
                characterModules,
                module(override = true) {
                    single { mockDatabase }
                    single { mockServiceApi }
                }
            )
        }

        setupMock()
        activityScenario = ActivityScenario.launch(getIntent())
    }

    private fun getIntent() = CharacterDetailsActivity.newIntent(context, CHARACTER_VO)

    private fun setupMock() {
        every { mockDatabase.characterFavoriteDao } returns mockFavoriteDao
        every { mockFavoriteDao.getCharacterFavorites() } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        mockDatabase.close()
        activityScenario.close()
        stopKoin()
    }

    @Test
    fun characterDetailScreen_validateSuccessLayout() {

        //ASSERT
        characterDetailRobot {
            checkImage()
            checkUpButton()
            checkFavIcon()
            checkName()
            checkDescription()
        }
    }

    companion object {
        val CHARACTER_VO = CharacterVO(
            1,
            "name one",
            "description one",
            "https:thumbnail.url.ext"
        )
    }
}