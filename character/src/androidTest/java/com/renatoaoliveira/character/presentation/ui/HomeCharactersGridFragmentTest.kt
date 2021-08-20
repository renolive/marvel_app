package com.renatoaoliveira.character.presentation.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.platform.app.InstrumentationRegistry
import com.renatoaoliveira.character.R
import com.renatoaoliveira.character.data.model.CharacterResponse
import com.renatoaoliveira.character.data.model.CharactersListResponse
import com.renatoaoliveira.character.data.model.DataResponse
import com.renatoaoliveira.character.data.model.ThumbnailResponse
import com.renatoaoliveira.character.data.repository.local.CharacterDatabase
import com.renatoaoliveira.character.data.repository.local.dao.CharacterFavoriteDao
import com.renatoaoliveira.character.data.repository.remote.api.CharacterServiceAPI
import com.renatoaoliveira.character.di.characterModules
import com.renatoaoliveira.character.presentation.ui.robot.characterItemRobot
import com.renatoaoliveira.character.presentation.ui.robot.characterListRobot
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.flowOf
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import retrofit2.Response

class HomeCharactersGridFragmentTest : KoinTest {

    private val context = InstrumentationRegistry.getInstrumentation().context

    private lateinit var koinApplication: KoinApplication

    @RelaxedMockK
    private lateinit var mockDatabase: CharacterDatabase

    @RelaxedMockK
    private lateinit var mockFavoriteDao: CharacterFavoriteDao

    @RelaxedMockK
    private lateinit var mockServiceApi: CharacterServiceAPI

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
    }

    private fun setupMock() {
        every { mockDatabase.characterFavoriteDao } returns mockFavoriteDao
        every { mockFavoriteDao.getCharacterFavorites() } returns flowOf(emptyList())
    }

    private fun getFragment() =
        launchFragmentInContainer<HomeCharactersGridFragment>(
            themeResId = R.style.Theme_MaterialComponents_DayNight_DarkActionBar
        )

    @After
    fun tearDown() {
        mockDatabase.close()
        stopKoin()
    }

    @Test
    fun charactersScreen_validateSuccessLayout() {
        // ARRANGE
        coEvery { mockServiceApi.getCharactersList(any()) } returns CHARACTER_REMOTE_RESPONSE_SUCCESS
        getFragment()

        //ASSERT
        characterListRobot {
            checkListScreenVisible()
            checkSearchViewVisible()
        }
        characterItemRobot {
            0.until(CHARACTER_DATA_RESPONSE.results.size).forEach {
                with(getCharacterViewHolder(it)) {
                    checkImage(this)
                    checkText(this)
                    checkFavIcon(this)
                }
            }
        }
    }

    @Test
    fun charactersScreen_validateEmptyLayout() {
        coEvery { mockServiceApi.getCharactersList(any()) } returns CHARACTER_REMOTE_RESPONSE_SUCCESS_EMPTY
        getFragment()

        characterListRobot {
            checkListScreenNotVisible()
            checkSearchViewVisible()
            checkEmptyScreenVisible()
        }
    }

    @Test
    fun charactersScreen_validateErrorLayout() {
        coEvery { mockServiceApi.getCharactersList(any()) } returns CHARACTER_REMOTE_RESPONSE_ERROR
        getFragment()

        characterListRobot {
            checkListScreenNotVisible()
            checkSearchViewVisible()
            checkErrorScreenVisible()
        }
    }

    companion object {
        val CHARACTER_RESPONSE = CharacterResponse(
            1,
            "name",
            "description",
            ThumbnailResponse("http:thumbnail.url", "ext")
        )

        val CHARACTER_DATA_RESPONSE = DataResponse(
            0,
            4,
            4,
            4,
            listOf(
                CHARACTER_RESPONSE,
                CHARACTER_RESPONSE,
                CHARACTER_RESPONSE,
                CHARACTER_RESPONSE,
            )
        )

        val CHARACTER_DATA_RESPONSE_EMPTY = DataResponse(
            0,
            0,
            0,
            0,
            emptyList()
        )

        val CHARACTER_REMOTE_RESPONSE_SUCCESS =
            Response.success(CharactersListResponse(CHARACTER_DATA_RESPONSE))

        val CHARACTER_REMOTE_RESPONSE_SUCCESS_EMPTY =
            Response.success(CharactersListResponse(CHARACTER_DATA_RESPONSE_EMPTY))

        val CHARACTER_REMOTE_RESPONSE_ERROR =
            Response.error<CharactersListResponse>(
                500,
                "{ \"error\": \"Internal Server Error\" }".toResponseBody("application/json".toMediaTypeOrNull())
            )
    }
}