package com.noitacilppa.okonow.ui.onboarding

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.noitacilppa.okonow.data.User
import com.noitacilppa.okonow.data.TodoRepository
import com.noitacilppa.okonow.data.UserPreferences
import kotlinx.coroutines.launch

class OnboardingViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val todoRepository: TodoRepository
) : AndroidViewModel(application) {
    private val userPreferences = UserPreferences(application)

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

    fun completeOnboarding(onComplete: () -> Unit) {
        viewModelScope.launch {
            // Save to DataStore for quick access
            userPreferences.saveUserName(name)
            
            // Save to Room database as the primary user (ID 1)
            todoRepository.insertUser(
                User(
                    id = 1L,
                    name = name
                )
            )
            onComplete()
        }
    }

    val isComplete get() = name.isNotBlank()
    val hasStartedEntry get() = name.isNotBlank() || imageUri != null
}
