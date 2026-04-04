package com.noitacilppa.okonow.ui.onboarding

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class OnboardingViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    var name by mutableStateOf(savedStateHandle.get<String>("name") ?: "")
        private set
    var imageUri by mutableStateOf<Uri?>(savedStateHandle.get<Uri>("imageUri"))
        private set

    fun updateName(newName: String) {
        name = newName
        savedStateHandle["name"] = newName
    }

    fun updateImageUri(uri: Uri?) {
        imageUri = uri
        savedStateHandle["imageUri"] = uri
    }

    val isComplete get() = name.isNotBlank()
    val hasStartedEntry get() = name.isNotBlank() || imageUri != null
}
