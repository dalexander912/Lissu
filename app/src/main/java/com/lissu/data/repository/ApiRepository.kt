package com.lissu.data.repository

import com.lissu.data.api.KtorClient
import com.lissu.data.api.items.ItemResponseDTO
import com.lissu.data.api.items.toModel
import com.lissu.data.models.Item
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ApiRepository(private val client: HttpClient) : ItemInterface {

    override suspend fun fetchItemByBarcode(barcode: String): Result<Item> {
        return try {

            val endpoint = "api/v0/product/$barcode.json"

            //GET con cliente
            val response: ItemResponseDTO = KtorClient.client.get(endpoint).body()

            if (response.status == 1) {
                // Usamos el mapeador .toModel() como querías
                Result.success(response.toModel(barcode))
            } else {
                Result.failure(Exception("El producto con código $barcode no existe en el servidor."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}