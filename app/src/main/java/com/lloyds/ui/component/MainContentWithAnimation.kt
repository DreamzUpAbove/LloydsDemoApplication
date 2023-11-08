package com.lloyds.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lloyds.data.model.PhotosModel


@Composable
fun MainContentWithAnimation(photoList: SnapshotStateList<PhotosModel>) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        ) {
            items(
                items = photoList,
                itemContent = { photo ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + expandIn(),
                        exit = fadeOut() + shrinkOut()
                    ) {
                        PhotoCardWithAnimation(photo)
                    }
                }
            )
        }
    }
}