package com.lissu.data.repository

import com.lissu.data.models.Item

interface ItemInterface {

    suspend fun fetchItemByBarcode(barcode: String): Result<Item>


}