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

    private val _nurseState = MutableStateFlow<NurseState?>(null)
    val nurseState: StateFlow<NurseState?> get() = _nurseState.asStateFlow()

    // Variable for a list of nurse
    private val _nurses = MutableStateFlow<List<NurseState>>(listOf())
    private var _idNurse: Int = 1
    val nurses: StateFlow<List<NurseState>> = _nurses

    // Variable for search nurse
    private val _searchState = MutableStateFlow(SearchState())
    val searchState: StateFlow<SearchState> get() = _searchState.asStateFlow()

    init {
        loadNurses()
    }

    // Load the list of the Nurse
    private fun loadNurses() {
        viewModelScope.launch {
            _nurses.value = listOf(
                NurseState(
                    id = _idNurse++,
                    name = "Pedro",
                    surname = "Pascal",
                    age = "10/01/2000",
                    email = "pedropascal@gmail.com",
                    password = "PedroPass1",
                    speciality = "Cardiac Care"
                ), NurseState(
                    id = _idNurse++,
                    name = "Antonio",
                    surname = "Perez",
                    age = "10/01/1967",
                    email = "a.perez@gmail.com",
                    password = "AntonioPass1",
                    speciality = "Obstetrics"
                ), NurseState(
                    id = _idNurse++,
                    name = "Maria",
                    surname = "Lopez",
                    age = "10/01/1997",
                    email = "m.lopez@gmail.com",
                    password = "MariaPass1",
                    speciality = "Perioperative"
                ), NurseState(
                    id = _idNurse++,
                    name = "Sara",
                    surname = "Garcia",
                    age = "10/01/1999",
                    email = "s.garcia@gmail.com",
                    password = "SaraPass1",
                    speciality = "Mental Health"
                ),
                NurseState(
                    id = _idNurse++,
                    name = "Antonio",
                    surname = "Lopez",
                    age = "31/12/1987",
                    email = "a.lopez@gmail.com",
                    password = "AntonioPass1",
                    speciality = "Oncology"
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

    fun getNurseState(): NurseState? {
        return _nurseState.value
    }

    // Disconnect Nurse User
    fun disconnectNurse() {
        setLoginState(false)
        _nurseState.value = null
    }

    // Add new Nurse into the list
    fun addNurse(nurse: NurseState) {
        _nurseState.value = nurse
        setLoginState(true)
    }

    // If the data is on data base save the data of response in a variable
    fun loginNurse(nurse: NurseState) {
        setLoginState(true)
        _nurseState.value = nurse
    }

    // Update the search name
    fun updateSearchName(name: String) {
        _searchState.update { it.copy(nurseName = name) }
    }

    // Search nurse by name
    fun findNurseByName() {
        val results =
            // Filter help you to search all the nurse that equals with the name
            _nurses.value.filter { it.name.equals(_searchState.value.nurseName, ignoreCase = true) }

        if (results.isNotEmpty()) {
            _searchState.update {
                it.copy(
                    searchResults = results, resultMessage = "Found ${results.size} nurse(s)."
                )
            }
        } else {
            _searchState.update {
                it.copy(
                    searchResults = emptyList(), resultMessage = "Not Found"
                )
            }
        }
    }
}
