package com.eugurguner.productsapicasestudy.core

import android.app.Activity
import android.content.res.Resources
import android.os.Build
import android.view.Window
import androidx.core.content.ContextCompat
import com.eugurguner.productsapicasestudy.R
import com.eugurguner.productsapicasestudy.core.extensions.changeNavigationBarColor
import com.eugurguner.productsapicasestudy.core.extensions.changeStatusBarColor
import com.eugurguner.productsapicasestudy.core.extensions.formatPrice
import com.google.common.truth.Truth
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlin.test.Test

class CoreTests {
    @Mock
    private lateinit var mockActivity: Activity

    @Mock
    private lateinit var mockWindow: Window

    @Mock
    private lateinit var mockResources: Resources

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        `when`(mockActivity.window).thenReturn(mockWindow)
        `when`(mockActivity.applicationContext).thenReturn(mockActivity)
        `when`(mockActivity.resources).thenReturn(mockResources)
    }

    @Test
    fun `status bar color changes correctly with the extension function invocation`() {
        val color = R.color.accentColor
        val expectedColor = 0x2A59FE
        `when`(ContextCompat.getColor(mockActivity, color)).thenReturn(expectedColor)

        mockActivity.changeStatusBarColor(color)

        Mockito.verify(mockWindow).statusBarColor = expectedColor
    }

    @Test
    fun `navigation bar color changes correctly with the extension function invocation`() {
        val color = R.color.accentColor
        val expectedColor = 0x2A59FE
        `when`(ContextCompat.getColor(mockActivity, color)).thenReturn(expectedColor)
        `when`(ContextCompat.getColor(mockActivity, R.color.soft_gray)).thenReturn(expectedColor)

        mockActivity.changeNavigationBarColor(color)

        Mockito.verify(mockWindow).navigationBarColor = expectedColor

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Mockito.verify(mockWindow).navigationBarDividerColor = expectedColor
        }
    }

    @Test
    fun `formatPrice formats valid integer price`() {
        val price = 1234567
        val expected = "1.234.567"

        val result = price.formatPrice()

        Truth.assertThat(expected).isEqualTo(result)
    }

    @Test
    fun `formatPrice formats valid double price`() {
        val price = 9876543.21
        val expected = "9.876.543"

        val result = price.formatPrice()

        Truth.assertThat(expected).isEqualTo(result)
    }

    @Test
    fun `formatPrice formats zero`() {
        val price = 0
        val expected = "0"

        val result = price.formatPrice()

        Truth.assertThat(expected).isEqualTo(result)
    }

    @Test
    fun `formatPrice formats negative price`() {
        val price = -12345
        val expected = "-12.345"

        val result = price.formatPrice()

        Truth.assertThat(expected).isEqualTo(result)
    }

    @Test
    fun `formatPrice handles Double NaN`() {
        val price = Double.NaN
        val expected = "0"

        val result = price.formatPrice()

        Truth.assertThat(expected).isEqualTo(result)
    }
}