package com.lloyds.data

import com.lloyds.common.NetworkResult
import com.lloyds.data.api.ApiService
import com.lloyds.data.model.PhotosModel
import com.lloyds.data.repository.MainRepository
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Response

class MainRepositoryTest {

    @Test
    fun `test getPhotos() with successful response`() = runBlocking {
        val apiService = mock(ApiService::class.java)
        val defaultDispatcher = TestCoroutineDispatcher()
        val mainRepository = MainRepository(apiService, defaultDispatcher)

        // Define a sample response
        val sampleData = listOf(
            PhotosModel("1", "photo1", "https://example.com/photos"),
            PhotosModel("2", "photo2", "https://example.com/photos")
        )
        `when`(apiService.getPhotos()).thenReturn(Response.success(sampleData))

        // Call the method and check the result
        val result = mainRepository.getPhotos()
        assertTrue(result is NetworkResult.Success)
        assertEquals(sampleData, (result as NetworkResult.Success).data)
    }


    @Test
    fun `test getPhotos() with network error`() = runBlocking {
        val apiService = mock(ApiService::class.java)
        val defaultDispatcher = TestCoroutineDispatcher()
        val mainRepository = MainRepository(apiService, defaultDispatcher)

        // Simulate a network error
        `when`(apiService.getPhotos()).thenReturn(
            Response.error(
                404,
                ResponseBody.create("application/json".toMediaTypeOrNull(), "Not Found")
            )
        )

        // Call the method and check the result
        val result = mainRepository.getPhotos()
        assertTrue(result is NetworkResult.Error)
    }

}

