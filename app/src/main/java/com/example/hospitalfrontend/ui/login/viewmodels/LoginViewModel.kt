package com.example.hospitalfrontend.ui.login.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.*

data class LoginState(
    val isLogin: Boolean = false,
    val isRegister: Boolean = false
)

class LoginViewModel : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())

    val loginState: StateFlow<LoginState> get()= _loginState.asStateFlow()


    fun setLoginState(isLogin: Boolean) {
        _loginState.update { currentState ->
            currentState.copy(isLogin = isLogin)
        }
    }

    fun getLoginState():Boolean{
        return _loginState.value.isLogin
    }
}
