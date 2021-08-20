package com.renatoaoliveira.character.presentation.ui.robot

import android.view.View
import androidx.cardview.widget.CardView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.renatoaoliveira.character.R
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*

fun characterItemRobot(func: CharacterItemRobot.() -> Unit) = CharacterItemRobot().apply { func() }

class CharacterItemRobot : BaseRobot() {

    fun checkText(parentMatcher: Matcher<View>) {
        onView(
            allOf(
                isDescendantOfA(parentMatcher),
                withId(R.id.character_name)
            )
        ).run {
            isVisible()
            check(matches(withText(not(isEmptyString()))))
        }
    }

    fun checkImage(parentMatcher: Matcher<View>) {
        onView(
            allOf(
                isDescendantOfA(parentMatcher),
                withId(R.id.character_image)
            )
        ).isVisible()
    }

    fun checkFavIcon(parentMatcher: Matcher<View>) {
        onView(
            allOf(
                isDescendantOfA(parentMatcher),
                withId(R.id.favorite_icon)
            )
        ).isVisible()
    }

    fun getCharacterViewHolder(itemPosition: Int) =
        allOf(
            isAssignableFrom(CardView::class.java),
            withParentIndex(itemPosition)
        )
}