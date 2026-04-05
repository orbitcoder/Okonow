# Okonow

**Okonow** is a high-fidelity Android application built with Jetpack Compose. It utilizes modern Android development practices to deliver a sleek, atmospheric interface designed for personal task and space management.

## 🛠 Tech Stack

- **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material 3.
- **Navigation:** [Compose Navigation](https://developer.android.com/jetpack/compose/navigation).
- **Architecture:** MVVM (Model-View-ViewModel) with [Lifecycle ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel).
- **Image Loading:** [Coil](https://coil-kt.github.io/coil/) for efficient and modern image loading.
- **Local Persistence:** [Room Database](https://developer.android.com/training/data-storage/room) for offline-first data management.
- **Visual Effects:** [Haze](https://github.com/chrisbanes/haze) for advanced glassmorphism and blur effects.
- **Theme:** Custom high-fidelity dark theme with tonal layering and ambient glows.

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug or newer.
- Android SDK 35 (compileSdk).
- Java 17.

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/your-repo/okonow.git
   ```
2. Open the project in Android Studio.
3. Sync Gradle and run the `:app` module on an emulator or physical device.

## 📱 Key Features

- **Glassmorphic Task Management:** 
    - `AddTaskBottomSheet`: An immersive task creation interface featuring glassmorphic layers, ambient glows, and smart text synchronization.
    - **Smart Input Toggling**: Seamlessly switch between detailed description and subtask list modes without losing your data.
    - **Date Selection**: Integrated Material 3 DatePicker for precise task scheduling.
- **Immersive Onboarding:** A glossy introduction to the app, featuring:
    - `ProfileImageUploader`: A custom circular uploader with neon halo effects and ambient glows.
    - Glassmorphic input fields with state-aware feedback.
- **Space Management:** Manage your tasks with editorial precision.
- **Offline-First:** Built-in repository pattern using Room for seamless offline usage.

## 🎨 Component Previews
The project includes several Jetpack Compose Previews to visualize the "Ultra-Glossy" components in isolation:
- `AddTaskBottomSheet`
- `ProfileImageUploaderPreview`
- `ContinueButtonPreview`
- `OkonowCalendar`

---
*Created with focus on high-fidelity depth and atmospheric UI.*
