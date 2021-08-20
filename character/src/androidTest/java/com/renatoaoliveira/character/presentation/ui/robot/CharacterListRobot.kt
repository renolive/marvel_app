package com.renatoaoliveira.character.presentation.ui.robot

import androidx.appcompat.widget.AppCompatImageView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.renatoaoliveira.character.R
import org.hamcrest.Matchers.*

fun characterListRobot(func: CharacterListRobot.() -> Unit) = CharacterListRobot().apply { func() }

class CharacterListRobot : BaseRobot() {

    fun checkListScreenVisible() =
        onView(withId(R.id.swipe_refresh)).isVisible()

    fun checkListScreenNotVisible() =
        onView(withId(R.id.swipe_refresh)).isNotVisible()

    fun checkSearchViewVisible() =
        onView(withId(R.id.search_view)).isVisible()

    fun checkErrorScreenVisible() {
        onView(withId(R.id.error_screen_include)).isVisible()
        onView(
            allOf(
                isDescendantOfA(withId(R.id.error_screen_include)),
                isAssignableFrom(AppCompatImageView::class.java)
            )
        ).isVisible()
        onView(withId(R.id.error_title)).run {
            isVisible()
            check(matches(withText(not(isEmptyString()))))
        }
    }

    fun checkLoadingScreenVisible() =
        onView(withId(R.id.loading_screen_include)).isVisible()

    fun checkOfflineScreenVisible() {
        onView(withId(R.id.error_offline_screen_include)).isVisible()
        onView(isAssignableFrom(AppCompatImageView::class.java)).isVisible()
        onView(withId(R.id.offline_title)).run {
            isVisible()
            check(matches(withText(not(isEmptyString()))))
        }
    }

    fun checkEmptyScreenVisible() {
        onView(withId(R.id.empty_screen_include)).isVisible()
        onView(
            allOf(
                isDescendantOfA(withId(R.id.empty_screen_include)),
                isAssignableFrom(AppCompatImageView::class.java)
            )
        ).isVisible()
        onView(withId(R.id.empty_title)).run {
            isVisible()
            check(matches(withText(not(isEmptyString()))))
        }
    }

}
