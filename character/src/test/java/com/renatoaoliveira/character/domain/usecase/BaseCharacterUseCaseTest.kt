package com.renatoaoliveira.character.domain.usecase

import com.renatoaoliveira.character.data.repository.CharacterRepository
import com.renatoaoliveira.character.domain.model.Character
import com.renatoaoliveira.character.domain.model.CharacterList
import com.renatoaoliveira.character.domain.repository.CharacterResult
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before

abstract class BaseCharacterUseCaseTest {

    @RelaxedMockK
    val mockCharacterRepository = mockk<CharacterRepository>()

    @Before
    fun commonSetup() {
        MockKAnnotations.init(this)
    }

    companion object {
        val CHARACTER = Character(
            1,
            "name",
            "description",
            "https:tumbnail.url.ext"
        )

        val CHARACTER_LIST = CharacterList(
            20,
            20,
            1000,
            listOf(CHARACTER)
        )

        val CHARACTER_RESULT_SUCCESS = CharacterResult(
            CHARACTER_LIST,
            200,
            "message",
            true
        )
    }
}