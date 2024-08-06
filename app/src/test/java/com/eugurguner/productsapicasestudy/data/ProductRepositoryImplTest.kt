package com.eugurguner.productsapicasestudy.data

import com.eugurguner.productsapicasestudy.data.dataSource.ProductDataSource
import com.eugurguner.productsapicasestudy.data.local.dao.CartDao
import com.eugurguner.productsapicasestudy.data.local.dao.ProductDao
import com.eugurguner.productsapicasestudy.data.model.CartProductDTO
import com.eugurguner.productsapicasestudy.data.model.ProductDTO
import com.eugurguner.productsapicasestudy.data.repository.ProductRepositoryImpl
import com.eugurguner.productsapicasestudy.domain.model.Product
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.Test

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ProductRepositoryImplTest {
    @Mock
    private lateinit var productDao: ProductDao

    @Mock
    private lateinit var cartDao: CartDao

    @Mock
    private lateinit var productDataSource: ProductDataSource

    private lateinit var productRepository: ProductRepositoryImpl

    private val productDTO =
        ProductDTO(
            id = "1",
            name = "Product A",
            imageUrl = "image_url",
            price = "10.0",
            description = "Description",
            model = "Model A",
            brand = "Brand A",
            createdAt = "2024-08-05T14:52:00Z"
        )
    private val product =
        Product(
            id = "1",
            name = "Product A",
            imageUrl = "image_url",
            price = 10.0,
            description = "Description",
            model = "Model A",
            brand = "Brand A",
            createdAt = "2024-08-05T14:52:00Z",
            quantity = 2
        )
    private val cartProductDTO =
        CartProductDTO(
            id = "1",
            name = "Product A",
            imageUrl = "image_url",
            price = "10.0",
            description = "Description",
            model = "Model A",
            brand = "Brand A",
            createdAt = "2024-08-05T14:52:00Z"
        )

    @Before
    fun setup() {
        productRepository = ProductRepositoryImpl(productDao = productDao, cartDao = cartDao, productDataSource = productDataSource)
    }

    @Test
    fun `fetchProducts returns products from data source`() =
        runTest {
            Mockito.`when`(productDataSource.fetchProducts(limit = 10, page = 1)).thenReturn(listOf(productDTO))

            val products = productRepository.fetchProducts(limit = 10, page = 1)

            Mockito.verify(productDataSource).fetchProducts(limit = 10, page = 1)
            assertThat(products).containsExactly(product.copy(quantity = 1))
        }

    @Test
    fun `getFavoriteProducts returns products from productDao`() =
        runTest {
            Mockito.`when`(productDao.getAll()).thenReturn(listOf(productDTO))

            val products = productRepository.getFavoriteProducts()

            Mockito.verify(productDao).getAll()
            assertThat(products).containsExactly(product.copy(quantity = 1))
        }

    @Test
    fun `getCartProducts returns products from cartDao`() =
        runTest {
            Mockito.`when`(cartDao.getAllCartItems()).thenReturn(listOf(cartProductDTO))

            val products = productRepository.getCartProducts()

            Mockito.verify(cartDao).getAllCartItems()
            assertThat(products).containsExactly(product.copy(quantity = 1))
        }

    @Test
    fun `getCartProductById returns product if found`() =
        runTest {
            Mockito.`when`(cartDao.getCartItemById(productId = "1")).thenReturn(cartProductDTO)

            val product = productRepository.getCartProductById(productId = "1")

            Mockito.verify(cartDao).getCartItemById(productId = "1")
            assertThat(product).isEqualTo(product?.copy(quantity = 1))
        }

    @Test
    fun `getCartProductById returns null if not found`() =
        runTest {
            Mockito.`when`(cartDao.getCartItemById(productId = "2")).thenReturn(null)

            val product = productRepository.getCartProductById(productId = "2")

            Mockito.verify(cartDao).getCartItemById(productId = "2")
            assertThat(product).isNull()
        }

    @Test
    fun `saveProduct inserts product into productDao`() =
        runTest {
            productRepository.saveProduct(product = product)

            Mockito.verify(productDao).insert(product = productDTO)
        }

    @Test
    fun `addProductToCart inserts product into cartDao`() =
        runTest {
            productRepository.addProductToCart(product = product.copy(quantity = 1))

            Mockito.verify(cartDao).insertCartItem(cartItem = cartProductDTO)
        }

    @Test
    fun `removeProduct deletes product from productDao`() =
        runTest {
            productRepository.removeProduct(productId = "1")

            Mockito.verify(productDao).deleteById(productId = "1")
        }

    @Test
    fun `removeProductFromCart deletes product from cartDao`() =
        runTest {
            productRepository.removeProductFromCart(productId = "1")

            Mockito.verify(cartDao).deleteCartItem(productId = "1")
        }

    @Test
    fun `increaseCartProductQuantity updates quantity and calls cartDao`() =
        runTest {
            Mockito.`when`(cartDao.getCartItemById(productId = "1")).thenReturn(cartProductDTO)

            productRepository.increaseCartProductQuantity(product)

            assertThat(product.quantity).isEqualTo(3)
            Mockito.verify(cartDao).updateCartItemQuantity(cartProductDTO.copy(quantity = 3))
        }

    @Test
    fun `decreaseCartProductQuantity updates quantity and calls cartDao`() =
        runTest {
            Mockito.`when`(cartDao.getCartItemById(productId = "1")).thenReturn(cartProductDTO)

            productRepository.decreaseCartProductQuantity(product)

            assertThat(product.quantity).isEqualTo(1)
            Mockito.verify(cartDao).updateCartItemQuantity(cartProductDTO)
        }
}