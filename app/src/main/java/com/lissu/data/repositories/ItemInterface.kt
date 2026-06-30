package com.lissu.data.repositories

import com.lissu.data.models.Item

interface ItemInterface {

    suspend fun fetchItemByBarcode(barcode: String): Result<Item>


}