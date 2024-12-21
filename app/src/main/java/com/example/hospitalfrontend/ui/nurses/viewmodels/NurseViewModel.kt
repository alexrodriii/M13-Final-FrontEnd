package com.example.hospitalfrontend.ui.nurses.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hospitalfrontend.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NurseViewModel : ViewModel() {
    // Login variables
    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> get() = _loginState.asStateFlow()

    // Variable for a list of nurse
    private val _nurses = MutableStateFlow<List<Nurse>>(listOf())
    private var _idNurse: Int = 1;
    val nurses: StateFlow<List<Nurse>> = _nurses


    init {
        loadNurses()
    }

    // Load the list of the Nurse
    private fun loadNurses() {
        viewModelScope.launch {
            _nurses.value = listOf(
                Nurse(
                    id = _idNurse++,
                    name = "Pedro",
                    surname = "Pascal",
                    age = "10/01/2000",
                    email = "pedropascal@gmail.com",
                    password = "PedroPass1",
                    speciality = "Enfermero"
                ), Nurse(
                    id = _idNurse++,
                    name = "Antonio",
                    surname = "Perez",
                    age = "10/01/1967",
                    email = "a.perez@gmail.com",
                    password = "AntonioPass1",
                    speciality = "Ginecologia"
                ), Nurse(
                    id = _idNurse++,
                    name = "Maria",
                    surname = "Lopez",
                    age = "10/01/1997",
                    email = "m.lopez@gmail.com",
                    password = "MariaPass1",
                    speciality = "Pediatria"
                ), Nurse(
                    id = _idNurse++,
                    name = "Sara",
                    surname = "Garcia",
                    age = "10/01/1999",
                    email = "s.garcia@gmail.com",
                    password = "SaraPass1",
                    speciality = "Salut Mental"
                )
            )
        }
    }

    // Update the value of login
    private fun setLoginState(isLogin: Boolean) {
        _loginState.update { currentState ->
            currentState.copy(isLogin = isLogin)
        }
    }

    fun getLoginState(): Boolean {
        return _loginState.value.isLogin
    }

    // Disconnect Nurse User
    fun disconnectNurse() {
        setLoginState(false)
    }

    // Add new Nurse into the list
    fun addNurse(nurse: Nurse) {
        val nurseWithId = nurse.copy(id = _idNurse++) // Use _idNurse to auto-increment ID
        _nurses.value += nurseWithId // Add to the list
        setLoginState(true)
    }

    // Check if mail and password are in the list
    fun loginNurse(mail: String, password: String) {
        viewModelScope.launch {
            val nurse = nurses.value.find { it.email == mail && it.password == password }
            if (nurse != null) {
                setLoginState(true)
            } else {
                setLoginState(false)
            }
        }
    }
}
