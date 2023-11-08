package com.lloyds.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lloyds.MainDispatcherRule
import com.lloyds.common.NetworkResult
import com.lloyds.data.api.ApiService
import com.lloyds.data.model.PhotosModel
import com.lloyds.data.repository.MainRepository
import com.lloyds.util.NetworkUtils
import com.lloyds.viewmodel.PhotosViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class PhotosViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    private lateinit var application: Application
    private var mainRepository: MainRepository = mockk()

    private val connectivityManager = mockk<ConnectivityManager>(relaxed = true)

    @Before
    fun setup() {
        application = ApplicationProvider.getApplicationContext()

    }

    @Test
    fun testGetCurrentIpInfo() {
        // Mock the response for the getPhotos function
        coEvery { mainRepository.getPhotos() } coAnswers { NetworkResult.Error("Error from API") }

        // Mock the response for the getPhotos function
        coEvery { mainRepository.getPhotos() } returns NetworkResult.Error("Error from API")

        val viewModel = PhotosViewModel(mainRepository, application)

        // Call the method that internally calls fetchPhotos
        viewModel.fetchPhotos()
        val result = viewModel.photosData.value
        assert(result is NetworkResult.Error)
        assert((result as NetworkResult.Error).message == "Error from API")
    }

    @Test
    fun testFetchPhotosNoInternet() {
        val network = mockk<Network>()
        val capabilities = mockk<NetworkCapabilities>()

        every { capabilities.hasCapability(any()) } returns false
        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns false
        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns false
        every { connectivityManager.getNetworkCapabilities(network) } returns capabilities
        every { connectivityManager.activeNetwork } returns network

        // Mock the response for the getPhotos function
        coEvery { mainRepository.getPhotos() } returns NetworkResult.Error("No internet connection")

        val viewModel = PhotosViewModel(mainRepository, application)

        // Call the method that internally calls fetchPhotos
        viewModel.fetchPhotos()

        val result = viewModel.photosData.value
        assert(result is NetworkResult.Error)
        assert((result as NetworkResult.Error).message == "No internet connection")
    }


    @Test
    fun testFetchPhotosWithNoNetworkAvailable() = runBlockingTest {
        val context = mockk<Context>()
        val connectivityManager = mockk<ConnectivityManager>()
        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager
        every { NetworkUtils.isNetworkAvailable(context) } returns false

        val apiService = mockk<ApiService>()
        val mainRepository = MainRepository(apiService, TestCoroutineDispatcher())
        val application = mockk<Application>()
        val viewModel = PhotosViewModel(mainRepository, application)

        // Call the method that internally calls fetchPhotos
        viewModel.fetchPhotos()

        // Check the result of the public method that utilizes fetchPhotos
        val result = viewModel.photosData.value
        assertTrue(result is NetworkResult.Error)
    }

    @Test
    fun testFetchPhotosSuccess() {
        val photosList = listOf(
            PhotosModel("1", "photo1.jpg", "Description 1"),
            PhotosModel("2", "photo2.jpg", "Description 2"),
            PhotosModel("3", "photo3.jpg", "Description 3")
            // Add more items as needed
        )

        // Mock the response for the getPhotos function
        coEvery { mainRepository.getPhotos() } returns NetworkResult.Success(photosList)
        val viewModel = PhotosViewModel(mainRepository, application)

        // Call the method that internally calls fetchPhotos
        viewModel.fetchPhotos()

        val result = viewModel.photosData.value
        if (result is NetworkResult.Success) {
            assert(result.data == photosList)
        } else {
            assert(false) { "Expected NetworkResult.Success" }
        }
    }

}
