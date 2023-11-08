package com.lloyds.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.lloyds.common.NetworkResult
import com.lloyds.data.model.PhotosModel

@Composable
fun PhotosApp(photosData: State<NetworkResult<List<PhotosModel>>?>) {
    when (val result = photosData.value) {
        is NetworkResult.Success -> {
            if (result.data.isNullOrEmpty()) {
                // Show empty list message
                ShowError(message = "No photos available")
            } else {
                val snapshotStateList = SnapshotStateList<PhotosModel>()
                result.data?.let {
                    snapshotStateList.addAll(it)
                }
                MainContentWithAnimation((snapshotStateList))
            }
        }

        is NetworkResult.Error -> {
            ShowError(message = result.message ?: "Unknown Network error")
        }

        is NetworkResult.Loading -> {
            LoadingStateWithAnimation()
        }

        else -> {
            ShowError(message = "Unknown error")
        }
    }
}