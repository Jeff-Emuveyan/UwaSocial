package com.bellogate_caliphate.uwasocial

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FeedUiAutomationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun feedUi_fetchesScrollsAndDisplaysPostsSuccessfully() {
        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodes(hasText("His mother had always taught him", substring = true))
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule
            .onNodeWithText("His mother had always taught him", substring = true)
            .assertIsDisplayed()

        composeTestRule
            .onNode(hasScrollAction())
            .performScrollToNode(hasText("All he wanted was a candy bar.", substring = true))

        composeTestRule
            .onNodeWithText("All he wanted was a candy bar.", substring = true)
            .assertIsDisplayed()
    }
}
