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
    val nurses: StateFlow<List<Nurse>> = _nurses

    init {
        loadNurses()
    }

    private fun loadNurses() {
        viewModelScope.launch {
            _nurses.value = listOf(
                Nurse(
                    id = 1,
                    name = "Pedro",
                    surname = "Pascal",
                    age = 24,
                    email = "pedropascal@gmail.com",
                    password = "PedroPass1",
                    speciality = "Enfermero"
                ), Nurse(
                    id = 2,
                    name = "Antonio",
                    surname = "Perez",
                    age = 40,
                    email = "a.perez@gmail.com",
                    password = "AntonioPass1",
                    speciality = "Ginecologia"
                ), Nurse(
                    id = 3,
                    name = "Maria",
                    surname = "Lopez",
                    age = 36,
                    email = "m.lopez@gmail.com",
                    password = "MariaPass1",
                    speciality = "Pediatria"
                ), Nurse(
                    id = 4,
                    name = "Sara",
                    surname = "Garcia",
                    age = 28,
                    email = "s.garcia@gmail.com",
                    password = "SaraPass1",
                    speciality = "Salut Mental"
                )
            )
        }
    }
}

annotation class Inject
annotation class HiltViewModel
