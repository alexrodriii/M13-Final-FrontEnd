package com.example.hospitalfrontend.ui.nurses.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

   data class InfoUiState(
    val nurseName: String = "",
    val resultMessage: String = ""
)
   class FindByNameViewModel : ViewModel() {
     private val _uiState = MutableStateFlow(InfoUiState())
     val uiState: StateFlow<InfoUiState> get()= _uiState.asStateFlow()

      fun updateNurse(name: String) {
        _uiState.update { it.copy( nurseName =name)}
     }
      fun findNurseByName (nurses: Array<String>) {
         val result = if (nurses.any {
             it.equals(_uiState.value.nurseName, ignoreCase = true)
            })
        {
            "Nurse found!"
        }
         else {
            "Not Found"
        }
         _uiState.update { it.copy(resultMessage =result) }
    }
}








