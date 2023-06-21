package com.andresuryana.dicodingstories.util

sealed class Resource<out T> {

    data class Success<out T>(val data: T) : Resource<T>()

    data class Failed<out T>(val message: String) : Resource<T>()

    data class Error<out T>(val message: String? = "An error occurred") : Resource<T>()
}
