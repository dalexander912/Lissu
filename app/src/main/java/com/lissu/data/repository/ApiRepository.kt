package com.lissu.data.repository

import com.lissu.data.api.items.ItemResponseDTO
import com.lissu.data.api.items.toModel
import com.lissu.data.model.Item
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ApiRepository(private val client: HttpClient) : ItemInterface {

    override suspend fun fetchItemByBarcode(barcode: String): Result<Item> {
        return try {
            val url = "https://world.openfoodfacts.org/api/v0/product/$barcode.json"

            val response: ItemResponseDTO = client.get(url).body()

            if (response.status == 1) {
                Result.success(response.toModel(barcode))
            } else {
                Result.failure(Exception("Item no encontrado en el servidor externo"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}