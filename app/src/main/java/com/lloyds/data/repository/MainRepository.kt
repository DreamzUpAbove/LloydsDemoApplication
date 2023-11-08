package com.lloyds.data.repository

import com.lloyds.common.BaseApiResponse
import com.lloyds.common.NetworkResult
import com.lloyds.data.api.ApiService
import com.lloyds.data.model.PhotosModel
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityRetainedScoped
class MainRepository @Inject constructor(
    private val apiService: ApiService,
    private val defaultDispatcher: CoroutineDispatcher
) : BaseApiResponse() {
    suspend fun getPhotos(): NetworkResult<List<PhotosModel>> {
        return withContext(defaultDispatcher) { safeApiCall { apiService.getPhotos() } }
    }

}