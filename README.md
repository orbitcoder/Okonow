# Okonow

**Okonow** is a high-fidelity Android application built with Jetpack Compose. It delivers a sleek, atmospheric interface designed for personal task management with a focus on "Glassmorphism" and depth.

## 🛠 Tech Stack

- **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material 3.
- **Navigation:** [Compose Navigation](https://developer.android.com/jetpack/compose/navigation) for seamless screen transitions.
- **Architecture:** MVVM (Model-View-ViewModel) with [Lifecycle ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel).
- **Image Loading:** [Coil](https://coil-kt.github.io/coil/) for efficient and modern image loading.
- **Local Persistence:** [Room Database](https://developer.android.com/training/data-storage/room) for robust offline-first data management.
- **User Preferences:** [DataStore](https://developer.android.com/topic/libraries/architecture/datastore) for lightweight key-value storage.
- **Visual Effects:** [Haze](https://github.com/chrisbanes.haze) for advanced real-time glassmorphism and blur effects.
- **Theme:** Custom high-fidelity dark theme with tonal layering, ambient glows, and neon accents.

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
    - **Smart Input Toggling**: Seamlessly switch between detailed description and subtask list modes.
    - **Custom Calendar**: A beautiful, custom-built glassmorphic calendar for date selection.
    - **Time Reminders**: Integrated time picker for scheduling task notifications.
- **Home & Focused Tasks:** 
    - Dashboard showing daily progress with a circular progress indicator.
    - "Focused Tasks" section grouping tasks by date (Today, Tomorrow, Upcoming).
    - Glossy task cards with priority indicators and subtask progress.
- **Focus Mode:** A dedicated space for deep work with a minimalist timer.
- **Profile & Personalization:** 
    - User profile management with custom image uploading.
    - Neon-halo avatar effects and ambient background glows.
- **Offline-First:** All data is persisted locally using Room, ensuring a snappy experience even without connectivity.

## 🎨 Design Philosophy
Okonow focuses on **Depth** and **Atmosphere**:
- **Layering**: Using `Haze` to create a sense of hierarchy through blurred backgrounds.
- **Glows**: Subtle ambient glows (Primary Purple and Secondary Teal) that move or react to the UI.
- **Typography**: Bold, high-contrast labels using Material 3 typography.

## 🎨 Component Previews
The project includes several Jetpack Compose Previews to visualize the components:
- `OkonowCalendar`: Custom date picker.
- `GlossyTaskCard`: The main task representation unit.
- `AddTaskBottomSheet`: The full task creation flow.
- `ProfileImageUploader`: Neon-style profile setup.

---
*Created with focus on high-fidelity depth and atmospheric UI.*
