package com.renatoaoliveira.character.presentation.ui.robot

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.renatoaoliveira.character.R
import org.hamcrest.Matchers.isEmptyString
import org.hamcrest.Matchers.not

fun characterDetailRobot(func: CharacterDetailRobot.() -> Unit) =
    CharacterDetailRobot().apply { func() }

class CharacterDetailRobot : BaseRobot() {

    fun checkImage() = onView(withId(R.id.character_detail_image)).isVisible()

    fun checkUpButton() = onView(withId(R.id.character_detail_up)).isVisible()

    fun checkFavIcon() = onView(withId(R.id.favorite_icon)).isVisible()

    fun checkName() = onView(withId(R.id.character_detail_title)).run {
        isVisible()
        check(matches(withText(not(isEmptyString()))))
    }

    fun checkDescription() = onView(withId(R.id.character_detail_description)).run {
        isVisible()
        check(matches(withText(not(isEmptyString()))))
    }
}