package com.example.shoppinglist.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// HiltAndroidApp is a class-level annotation that triggers Hilt's code generation
@HiltAndroidApp
class ShoppingListApplication : Application()