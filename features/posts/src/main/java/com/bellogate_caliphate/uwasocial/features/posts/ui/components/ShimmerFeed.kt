package com.bellogate_caliphate.uwasocial.features.posts.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
internal fun ShimmerFeed(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        repeat(3) {
            ShimmerPostCard()
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Preview
@Composable
private fun ShimmerFeedPreview() {
    ShimmerFeed()
}
