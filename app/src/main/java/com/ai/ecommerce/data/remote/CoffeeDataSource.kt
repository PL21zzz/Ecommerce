package com.ai.ecommerce.data.remote

import com.ai.ecommerce.domain.model.Product
import com.ai.ecommerce.domain.model.Rating

object CoffeeDataSource {
    private val coffeeNames = listOf("Caffe Latte", "Cappuccino", "Macchiato", "Mocha", "Espresso", "Americano")
    private val coffeeExtras = listOf("with Chocolate", "with Oat Milk", "with Caramel", "with Deep Choc", "with Milk", "with Ice")
    private val coffeeImages = listOf(
        "https://res.cloudinary.com/dypm5avrx/image/upload/v1781495677/cf5_r62sil.jpg",
        "https://res.cloudinary.com/dypm5avrx/image/upload/v1781495677/cf6_mkiook.jpg",
        "https://res.cloudinary.com/dypm5avrx/image/upload/v1781492866/cf3_ulgy01.png",
        "https://res.cloudinary.com/dypm5avrx/image/upload/v1781492865/cf4_kflia0.jpg"
    )

    // Hàm nhận vào danh sách sản phẩm thô của API và trả ra danh sách Coffee xịn mịn
    fun mapToCoffeeList(rawProducts: List<Product>): List<Product> {
        return rawProducts.mapIndexed { index, product ->
            Product(
                id = product.id,
                title = coffeeNames[index % coffeeNames.size],
                description = coffeeExtras[index % coffeeExtras.size],
                price = (400..600).random() / 100.0,
                category = "Coffee",
                image = coffeeImages[index % coffeeImages.size],
                rating = Rating(
                    rate = (45..49).random() / 10.0,
                    count = product.rating.count
                )
            )
        }
    }
}