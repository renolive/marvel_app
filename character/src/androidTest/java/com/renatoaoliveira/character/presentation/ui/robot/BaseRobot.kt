package com.renatoaoliveira.character.presentation.ui.robot

import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matchers

open class BaseRobot {
    fun ViewInteraction.isVisible(): ViewInteraction =
        this.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    fun ViewInteraction.isNotVisible(): ViewInteraction =
        this.check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))
}