package com.lissu.data.repository

import com.lissu.data.model.Item

interface ItemInterface {

    suspend fun fetchItemByBarcode(barcode: String): Result<Item>


}