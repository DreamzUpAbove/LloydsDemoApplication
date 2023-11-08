package com.lloyds.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lloyds.common.NetworkResult
import com.lloyds.data.model.PhotosModel
import com.lloyds.data.repository.MainRepository
import com.lloyds.util.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _photosData: MutableStateFlow<NetworkResult<List<PhotosModel>>?> =
        MutableStateFlow(NetworkResult.Loading())
    val photosData: StateFlow<NetworkResult<List<PhotosModel>>?> = _photosData

    init {
        fetchPhotos()
    }


    fun fetchPhotos() {
        viewModelScope.launch {
            _photosData.value = NetworkResult.Loading()
            try {
                if (NetworkUtils.isNetworkAvailable(getApplication())) {
                    val result = mainRepository.getPhotos()
                    _photosData.value = result
                } else {
                    _photosData.value = NetworkResult.Error("No internet connection")
                }
            } catch (e: IOException) {
                _photosData.value = NetworkResult.Error("Network exception: ${e.message}")
            } catch (e: Exception) {
                _photosData.value = NetworkResult.Error("Unexpected error: ${e.message}")
            }
        }
    }

    // Function to update photosData
    fun updatePhotosData(networkResult: NetworkResult<List<PhotosModel>>) {
        _photosData.value = networkResult
    }
}