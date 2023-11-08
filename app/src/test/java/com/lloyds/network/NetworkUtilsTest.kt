package com.lloyds.network

import android.content.Context
import android.net.ConnectivityManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.lloyds.util.NetworkUtils
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowNetworkInfo


@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class NetworkUtilsTest {

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var shadowOfActiveNetworkInfo: ShadowNetworkInfo

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        shadowOfActiveNetworkInfo = Shadows.shadowOf(connectivityManager.activeNetworkInfo)
    }

    @Test
    fun getActiveNetworkInfo_shouldInitializeItself() {
        assertNotNull(connectivityManager.activeNetworkInfo)
    }

    @Test
    fun `test isNetworkAvailable() with connected network`() {
        // Simulate a connected network
        shadowOfActiveNetworkInfo!!.setConnectionStatus(true)

        assertTrue(NetworkUtils.isNetworkAvailable(getApplicationContext()))
    }

    @Test
    fun `test isNetworkAvailable() with disconnected network`() {
        // Simulate a disconnected network
        shadowOfActiveNetworkInfo!!.setConnectionStatus(false)

        assertFalse(NetworkUtils.isNetworkAvailable(getApplicationContext()))
    }
}