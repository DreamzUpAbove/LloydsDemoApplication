package com.lloyds.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.lloyds.common.NetworkResult
import com.lloyds.data.model.PhotosModel
import com.lloyds.ui.component.PhotosApp
import com.lloyds.viewmodel.PhotosViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalFoundationApi
class PhotosActivityTest {

    private lateinit var viewModel: PhotosViewModel

    @get:Rule
    val composeTestRule = createComposeRule()


    @Before
    fun setUp() {
    }


    @Test
    fun testLoadingState() {
        val stateFlow: StateFlow<NetworkResult<List<PhotosModel>>> =
            MutableStateFlow(NetworkResult.Loading())
        viewModel = mockk {
            every { photosData } returns stateFlow
        }
        composeTestRule.setContent {
            PhotosApp(viewModel.photosData.collectAsState())
        }

        composeTestRule.onNodeWithText("Loading...").assertIsDisplayed()
    }

    @Test
    fun testNetworkErrorState() {
        val stateFlow: StateFlow<NetworkResult<List<PhotosModel>>> =
            MutableStateFlow(NetworkResult.Error("Mock network error"))
        viewModel = mockk {
            every { photosData } returns stateFlow
        }
        composeTestRule.setContent {
            PhotosApp(viewModel.photosData.collectAsState())
        }
        composeTestRule.onNodeWithText("Mock network error").assertIsDisplayed()
    }

    @Test
    fun testSuccessState() {
        val dummyList = listOf(
            PhotosModel("1", "Title 1", "url1"),
            PhotosModel("2", "Title 2", "url2"),
            PhotosModel("3", "Title 3", "url3")
        )

        val stateFlow: StateFlow<NetworkResult<List<PhotosModel>>> =
            MutableStateFlow(NetworkResult.Success(dummyList))
        viewModel = mockk {
            every { photosData } returns stateFlow
        }
        composeTestRule.setContent {
            PhotosApp(viewModel.photosData.collectAsState())
        }
        composeTestRule.onNodeWithText(dummyList[0].url).assertIsDisplayed()
        composeTestRule.onNodeWithText(dummyList[1].url).assertIsDisplayed()

    }

    @Test
    fun testEmptyListState() {
        // Mocking the ViewModel
        val stateFlow: StateFlow<NetworkResult<List<PhotosModel>>> =
            MutableStateFlow(NetworkResult.Success(emptyList()))
        viewModel = mockk {
            every { photosData } returns stateFlow
        }
        // Launch the activity
        composeTestRule.setContent {
            PhotosApp(viewModel.photosData.collectAsState())
        }

        // Verify that the list is empty
        composeTestRule.onNodeWithText("No photos available").assertIsDisplayed()
    }

}

