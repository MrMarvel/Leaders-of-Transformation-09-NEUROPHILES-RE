package ru.mrmarvel.camoletapp.data

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class LoginError {
    WRONG_PASSWORD,
    USERNAME_NOT_FOUND,
}

class LoginScreenViewModel: ViewModel() {
    var loginError = MutableLiveData<LoginError?>(null)
    var username = mutableStateOf("")
    var password = mutableStateOf("")

    fun verifyLogin(): Boolean {
        loginError.value = LoginError.WRONG_PASSWORD
        return false
    }
}