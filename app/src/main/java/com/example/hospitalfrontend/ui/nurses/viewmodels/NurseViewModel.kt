package com.example.hospitalfrontend.ui.nurses.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hospitalfrontend.model.Nurse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class NurseViewModel @Inject constructor() : ViewModel() {

    private val _nurses = MutableStateFlow<List<Nurse>>(listOf())
    private var _idNurse: Int = 1;
    val nurses: StateFlow<List<Nurse>> = _nurses


    init {
        loadNurses()
    }

    private fun loadNurses() {
        viewModelScope.launch {
            _nurses.value = listOf(
                Nurse(
                    id = _idNurse++,
                    name = "Pedro",
                    surname = "Pascal",
                    age = "10/1/2000",
                    email = "pedropascal@gmail.com",
                    password = "PedroPass1",
                    speciality = "Enfermero"
                ), Nurse(
                    id = _idNurse++,
                    name = "Antonio",
                    surname = "Perez",
                    age = "10/1/1967",
                    email = "a.perez@gmail.com",
                    password = "AntonioPass1",
                    speciality = "Ginecologia"
                ), Nurse(
                    id = _idNurse++,
                    name = "Maria",
                    surname = "Lopez",
                    age = "10/1/1997",
                    email = "m.lopez@gmail.com",
                    password = "MariaPass1",
                    speciality = "Pediatria"
                ), Nurse(
                    id = _idNurse++,
                    name = "Sara",
                    surname = "Garcia",
                    age = "10/1/1999",
                    email = "s.garcia@gmail.com",
                    password = "SaraPass1",
                    speciality = "Salut Mental"
                )
            )
        }
    }

    fun addNurse(nurse: Nurse) {
        val nurseWithId = nurse.copy(id = _idNurse++) // Use _idNurse to auto-increment ID
        _nurses.value += nurseWithId // Add to the list
    }
}

annotation class Inject
annotation class HiltViewModel
