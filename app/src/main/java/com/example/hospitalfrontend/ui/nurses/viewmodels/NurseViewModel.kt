package com.example.hospitalfrontend.ui.nurses.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hospitalfrontend.model.Diagnosis
import com.example.hospitalfrontend.model.LoginState
import com.example.hospitalfrontend.model.NurseState
import com.example.hospitalfrontend.model.PatientState
import com.example.hospitalfrontend.network.ApiService
import com.example.hospitalfrontend.network.RemoteViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NurseViewModel : ViewModel() {
    private val apiService: ApiService = Retrofit.Builder()
        .baseUrl("http://10.118.3.45:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
    // Login variables
    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> get() = _loginState.asStateFlow()

    private val _nurseState = MutableStateFlow<NurseState?>(null)
    val nurseState: StateFlow<NurseState?> get() = _nurseState.asStateFlow()

    // Variable for a list of nurse
    private val _nurses = MutableStateFlow<List<NurseState>>(listOf())
    val nurses: StateFlow<List<NurseState>> = _nurses

    // List of nurse specialties
    private val _specialityNurse = MutableStateFlow<List<String>>(emptyList())
    val specialityNurse: StateFlow<List<String>> = _specialityNurse

   private val _patientstate = MutableStateFlow<List<PatientState>>(emptyList())
    val patientState: StateFlow<List<PatientState>> = _patientstate

    private val _diagnosisState = mutableStateOf<List<Diagnosis>>(emptyList())
    val diagnosisState: List<Diagnosis>
        get() = _diagnosisState.value




    // Variable for search nurse
    private val _currentSearchName = MutableStateFlow("")
    val currentSearchName: StateFlow<String> get() = _currentSearchName.asStateFlow()

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: String?
        get() = _errorMessage.value

    init {
        loadSpeciality()
    }

    private fun loadSpeciality() {
        viewModelScope.launch {
            val sortedSpecialities = listOf(
                "Pediatrics Nursing",
                "Cardiology Nursing",
                "Neurology Nursing",
                "Oncology Nursing",
                "Surgical Nursing",
                "Neonatal Nursing",
                "Emergency Nursing",
                "Critical Care Nursing",
                "Psychiatric Nursing",
                "Obstetrics and Gynecology Nursing",
                "Orthopedic Nursing",
                "Anesthesia Nursing",
                "Palliative Care Nursing",
                "Nephrology Nursing",
                "Transplant Nursing",
                "Forensic Nursing",
                "Research Nursing"
            ).sorted()
            _specialityNurse.value = sortedSpecialities
        }
    }

    // Load the list of the Nurse
    fun loadNurses(nurse: List<NurseState>) {
        _nurses.value = nurse
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
        _currentSearchName.value = ""
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

    fun updateCurrentSearchName(name: String) {
        _currentSearchName.value = name
    }

    fun deleteNurse() {
        disconnectNurse()
    }

    fun loadDiagnosis(patientId: Int) {
        viewModelScope.launch {
            try {
                val result = apiService.getDiagnosis(patientId)
                _diagnosisState.value = result
                Log.d("Diagnosis", "Fetched: $result")
            } catch (e: Exception) {
                Log.e("Diagnosis", "Failed to fetch diagnosis", e)
            }
        }
    }
}