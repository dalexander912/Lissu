package com.lissu.data.api.items
import com.lissu.data.models.Item
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ItemResponseDTO(
    val status: Int,
    @SerialName("product") val product: ItemProductDTO? = null
)

@Serializable
data class ItemProductDTO(
    // Mapear las llaves de OpenFoodFacts a nombres de Item
    @SerialName("product_name") val name: String? = null,
    @SerialName("categories") val categories: String? = null,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("brands") val brand: String? = null
)

fun ItemResponseDTO.toModel(barcode: String): Item {
    return Item(
        id = barcode,
        name = this.product?.name ?: "Producto Desconocido",
        category = this.product?.categories?.split(",")?.firstOrNull()?.trim() ?: "Sin categoría",
        imageUrl = this.product?.imageUrl ?: "",
        brand = this.product?.brand ?: "Genérico"
    )
}