package ru.mrmarvel.camoletapp.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class LoginError {
    WRONG_PASSWORD,
    USERNAME_NOT_FOUND,
}

class LoginScreenViewModel: ViewModel() {
    var loginError = MutableLiveData<LoginError?>(null)
}