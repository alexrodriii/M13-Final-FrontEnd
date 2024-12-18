package com.example.hospitalfrontend.ui.login.viewmodels

import androidx.lifecycle.ViewModel
import com.example.hospitalfrontend.model.Nurse
import com.example.hospitalfrontend.ui.nurses.viewmodels.NurseViewModel
import kotlinx.coroutines.flow.*

data class LoginState(
    val isLogin: Boolean = false, val isRegister: Boolean = false
)

class LoginViewModel(private val nurseViewModel: NurseViewModel) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> get() = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow(LoginState())
    val registerState: StateFlow<LoginState> get() = _registerState.asStateFlow()

    fun setLoginState(isLogin: Boolean) {
        _loginState.update { currentState ->
            currentState.copy(isLogin = isLogin)
        }
    }


    fun getLoginState(): Boolean {
        return _loginState.value.isLogin
    }

    private fun setCreateNurseState(isRegister: Boolean) {
        _registerState.update { currentState ->
            currentState.copy(isRegister = isRegister)
        }
    }

    fun getCreateNurseState(): Boolean {
        return _registerState.value.isRegister
    }

    fun saveNurse(nurse: Nurse) {
        nurseViewModel.addNurse(nurse) // Add nurse to the shared NurseViewModel
        setCreateNurseState(true) // Update registration state
        setLoginState(true)
    }
}
