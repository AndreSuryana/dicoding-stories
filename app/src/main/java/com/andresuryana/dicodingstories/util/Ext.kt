package com.andresuryana.dicodingstories.util

object Ext {

    fun String.isValidEmail(): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return this.matches(emailRegex)
    }
}