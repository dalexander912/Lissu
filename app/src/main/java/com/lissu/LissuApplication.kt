package com.lissu

import android.app.Application
import com.lissu.data.AppProvider

class LissuApplication : Application() {
  val appProvider by lazy { AppProvider(this) }
}