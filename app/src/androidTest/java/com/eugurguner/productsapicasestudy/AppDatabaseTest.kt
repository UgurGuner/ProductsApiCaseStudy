package com.eugurguner.productsapicasestudy

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eugurguner.productsapicasestudy.data.local.db.AppDatabase
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {
    private lateinit var appDatabase: AppDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        appDatabase =
            Room
                .inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    fun database_should_be_created_successfully() {
        assertThat(appDatabase).isNotNull()
    }

    @Test
    fun productDao_should_not_be_null() {
        val productDao = appDatabase.productDao()
        assertThat(productDao).isNotNull()
    }

    @Test
    fun cartDao_should_not_be_null() {
        val cartDao = appDatabase.cartDao()
        assertThat(cartDao).isNotNull()
    }
}