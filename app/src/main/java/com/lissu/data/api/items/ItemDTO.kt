package com.lissu.data.api.items
import com.lissu.data.model.Item
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ItemResponseDTO(
    val status: Int,
    @SerialName("product") val product: ItemProductDTO?
)

@Serializable
data class ItemProductDTO(
    // Mapeamos las llaves de OpenFoodFacts a nombres de Item
    @SerialName("product_name") val name: String?,
    @SerialName("categories") val categories: String?,
    @SerialName("image_url") val imageUrl: String?,
    @SerialName("brands") val brand: String?
)

fun ItemResponseDTO.toModel(barcode: String): Item {
    return Item(
        id = barcode,
        name = this.product?.name ?: "Producto Desconocido",
        category = this.product?.categories?.split(",")?.firstOrNull() ?: "Sin categoría",
        imageUrl = this.product?.imageUrl ?: "",
        brand = this.product?.brand ?: "Genérico"
    )
}