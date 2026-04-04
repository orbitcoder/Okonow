package com.noitacilppa.okonow.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.noitacilppa.okonow.OkonowApplication
import com.noitacilppa.okonow.ui.onboarding.OnboardingViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            TodoViewModel(okonowApplication().todoRepository)
        }
        
        initializer {
            OnboardingViewModel(
                application = okonowApplication(),
                savedStateHandle = createSavedStateHandle(),
                todoRepository = okonowApplication().todoRepository
            )
        }
    }
}

fun CreationExtras.okonowApplication(): OkonowApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as OkonowApplication)
