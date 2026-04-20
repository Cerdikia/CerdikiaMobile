# CerdikiaMobile

> Mobile application for quizzes that contribute to educating the nation

A modern Android mobile application built with **Kotlin** and **Jetpack Compose**, designed to provide an engaging educational quiz platform with secure authentication and real-time data synchronization.

## 🎯 Overview

CerdikiaMobile is a feature-rich quiz application that combines modern Android development practices with a user-friendly interface. The app is built entirely in Kotlin using the latest Android technologies to ensure optimal performance, maintainability, and scalability.

## 🛠️ Tech Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| **Language** | Kotlin | 2.1.20 |
| **Build Tool** | Gradle | AGP 8.9.1 |
| **Min SDK** | Android | 26 |
| **Target SDK** | Android | 35 |
| **JVM Target** | Java | 11 |
| **UI Framework** | Jetpack Compose | 2025.03.01 |
| **Architecture Pattern** | MVVM | - |
| **HTTP Client** | Retrofit | 2.9.0 |
| **Database** | Room | 2.7.1 |
| **Local Storage** | DataStore | 1.1.6 |
| **Authentication** | Firebase Auth | 23.2.0 |
| **Code Generation** | KSP | 2.1.20-2.0.1 |

## 📚 Dependencies Overview

### 🎨 UI & Compose
- **androidx.compose.ui** - Core Compose UI framework
- **androidx.compose.foundation** - Compose foundation layouts
- **androidx.compose.material3** - Material Design 3 components
- **androidx.compose.runtime** - Compose runtime
- **androidx.activity.compose** - Compose activity integration
- Material Design (`com.google.android.material` 1.12.0)
- ConstraintLayout (`androidx.constraintlayout` 2.2.1)
- RecyclerView (`androidx.recyclerview` 1.4.0)

### 🧭 Navigation
- **androidx.navigation.fragment-ktx** (2.8.9) - Fragment navigation
- **androidx.navigation.ui-ktx** (2.8.9) - Navigation UI
- **androidx.navigation.safeargs.kotlin** (2.9.0) - Type-safe navigation arguments

### 🔄 State Management & Lifecycle
- **androidx.lifecycle.livedata-ktx** (2.8.7) - LiveData for reactive data
- **androidx.lifecycle.viewmodel-ktx** (2.8.7) - ViewModel for state management
- **androidx.lifecycle.viewmodel-compose** (2.8.7) - Compose ViewModel integration
- **androidx.lifecycle.process** (2.9.0) - Process lifecycle awareness

### 💾 Data Persistence
- **Room Database** (2.7.1)
  - `androidx.room.runtime` - Database runtime
  - `androidx.room.ktx` - Kotlin extensions
  - `room.compiler` - Code generation (KSP)
- **DataStore** 
  - `androidx.datastore.preferences` (1.1.4) - Key-value storage
  - `androidx.datastore.core` (1.1.6) - Core DataStore functionality
- **Protocol Buffers** (3.24.4) - For efficient data serialization

### 🌐 Networking
- **Retrofit** (2.9.0) - RESTful API client
- **Gson Converter** (2.9.0) - JSON serialization/deserialization
- **OkHttp Logging Interceptor** (4.11.0) - HTTP request/response logging

### 🔐 Authentication & Security
- **Firebase Authentication** (23.2.0)
- **Google Play Services Auth** (21.0.0)
- **androidx.credentials** (1.5.0) - Credential management
- **androidx.credentials.play-services-auth** (1.5.0) - Play Services integration
- **Google ID** (1.1.1) - Google Sign-In

### 🖼️ Image & UI Effects
- **Glide** (4.16.0) - Image loading and caching
- **Shimmer** (0.5.0) - Loading skeleton screens

### 📦 Core Dependencies
- **androidx.core-ktx** (1.15.0) - Kotlin extensions for AndroidX
- **androidx.appcompat** (1.7.0) - App compatibility layer
- **androidx.fragment-ktx** (1.8.6) - Fragment Kotlin extensions
- **androidx.legacy.support-v4** (1.0.0) - Legacy support library

### 🧪 Testing
- **JUnit** (4.13.2) - Unit testing framework
- **androidx.test.ext.junit** (1.2.1) - AndroidX JUnit extensions
- **androidx.test.espresso.core** (3.6.1) - UI testing framework
- **AndroidJUnitRunner** - Test instrumentation runner

## ✨ Key Features

- 🎓 **Interactive Quiz Platform** - Engaging quiz experience with various question types
- 🔐 **Secure Authentication** - Firebase-powered authentication with Google Sign-In support
- 💾 **Offline Support** - Local data persistence with Room database
- 🚀 **Modern UI** - Built with Jetpack Compose for responsive and beautiful interfaces
- 📊 **Real-time Data Sync** - Seamless backend synchronization with Retrofit
- 🎨 **Material Design 3** - Modern and accessible Material Design components
- ⚡ **Optimized Performance** - Kotlin coroutines and efficient state management

## 🚀 Getting Started

### Prerequisites

- **Android Studio** Hedgehog or newer (or any recent version)
- **JDK 11** or higher
- **Android SDK** API Level 26 or higher
- **Git** for version control
- **Firebase Project** (for authentication setup)

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Cerdikia/CerdikiaMobile.git
   cd CerdikiaMobile
   ```

2. **Open in Android Studio:**
   - Launch Android Studio
   - Select `Open an existing Android Studio project`
   - Navigate to the cloned repository and select it
   - Wait for Gradle sync to complete

3. **Configure Firebase:**
   - Go to [Firebase Console](https://console.firebase.google.com/)
   - Create a new project or use an existing one
   - Add an Android app to your Firebase project
   - Download the `google-services.json` file
   - Place it in the `app/` directory
   - Enable Authentication method: Google Sign-In

4. **Build the project:**
   ```bash
   ./gradlew build
   ```

5. **Run the application:**
   ```bash
   ./gradlew installDebug
   ```
   Or use Android Studio's Run button (Shift + F10)

### Build Variants

- **Debug Build** - For development with debugging enabled
- **Release Build** - Optimized production build (minification disabled)

## 📁 Project Structure

```
CerdikiaMobile/
├── app/                          # Main application module
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/fhanafi/cerdikia/
│   │   │   │       ├── ui/                # UI layer (Composables, Screens)
│   │   │   │       ├── viewmodel/        # ViewModels for state management
│   │   │   │       ├── repository/       # Data repository layer
│   │   │   │       ├── database/         # Room database entities & DAOs
│   │   │   │       ├── network/          # Retrofit API services
│   │   │   │       ├── model/            # Data models & entities
│   │   │   │       └── utils/            # Utility functions & extensions
│   │   │   ├── res/
│   │   │   │   ├── drawable/            # Image assets
│   │   │   │   ├── values/              # String, color, dimension resources
│   │   │   │   └── layout/              # Legacy XML layouts (if any)
│   │   │   └── AndroidManifest.xml
│   │   ├── test/                        # Unit tests
│   │   └── androidTest/                 # Instrumented tests
│   ├── build.gradle.kts                 # App module build configuration
│   └── proguard-rules.pro               # ProGuard rules
├── build.gradle.kts                     # Root build configuration
├── gradle/
│   └── libs.versions.toml               # Gradle version catalog
├── settings.gradle.kts                  # Gradle settings
├── gradle.properties                    # Gradle properties
└── README.md                            # This file
```

## 🏗️ Architecture

CerdikiaMobile follows the **MVVM (Model-View-ViewModel)** architecture pattern combined with clean code principles:

### Architecture Layers

```
┌─────────────────────────────────────────┐
│   Presentation Layer (UI)               │
│   ├─ Composables (Jetpack Compose)     │
│   ├─ Screens                           │
│   └─ Navigation                        │
├─────────────────────────────────────────┤
│   ViewModel Layer (State Management)    │
│   ├─ ViewModels                        │
│   ├─ State holders                     │
│   └─ UI events                         │
├─────────────────────────────────────────┤
│   Domain Layer (Business Logic)         │
│   ├─ Use cases                         │
│   ├─ Interfaces                        │
│   └─ Entities                          │
├─────────────────────────────────────────┤
│   Data Layer (Data Management)          │
│   ├─ Repository implementations        │
│   ├─ Room Database                     │
│   ├─ Retrofit API services            │
│   ├─ DataStore preferences            │
│   └─ Model mappers                     │
└─────────────────────────────────────────┘
```

### Key Components

- **UI Layer** - Jetpack Compose screens and reusable components
- **ViewModel** - Manages UI state and handles business logic
- **Repository** - Abstract data access layer, handles local and remote data
- **Database** - Room for local data persistence
- **Network** - Retrofit for backend API communication
- **DataStore** - Preferences and protocol buffers for app configuration

## ⚙️ Build Configuration

### Android Configuration

- **Compile SDK:** 35
- **Min SDK:** 26
- **Target SDK:** 35
- **Java Compatibility:** VERSION_11
- **Kotlin Compiler Extension Version:** 1.5.1

### Build Features

- **View Binding** - Enabled for type-safe view access
- **Jetpack Compose** - Enabled for declarative UI

### ProGuard/R8

- **Release Build** - Minification enabled with ProGuard rules
- **Debug Build** - Minification disabled for faster builds

## 🧪 Testing

### Unit Testing

Run unit tests with:
```bash
./gradlew test
```

### Instrumented Tests

Run instrumented tests on a device or emulator:
```bash
./gradlew connectedAndroidTest
```

### Test Dependencies

- **JUnit** - Unit testing framework
- **Espresso** - UI testing framework
- **AndroidJUnitRunner** - Test instrumentation runner

## 🐛 Troubleshooting

### Gradle Sync Issues

**Problem:** Gradle sync fails with dependency resolution error

**Solution:**
```bash
# Clean build cache
./gradlew clean

# Sync again with verbose output
./gradlew --refresh-dependencies sync
```

### Firebase Configuration

**Problem:** `google-services.json` not found

**Solution:**
1. Ensure `google-services.json` is placed in the `app/` directory
2. Rebuild the project: `./gradlew clean build`

### Compose Compilation Issues

**Problem:** Compose UI not compiling

**Solution:**
1. Update Android Studio to latest stable version
2. Invalidate caches: `File → Invalidate Caches` in Android Studio
3. Rebuild project

### Build Variants

**Problem:** Project won't build after switching build variants

**Solution:**
```bash
./gradlew clean
./gradlew build
```

## 📖 Development Guidelines

### Code Style

- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Write self-documenting code with proper comments

### Compose Guidelines

- Break down large Composables into smaller, reusable components
- Use `@Preview` annotations for easier UI development
- Implement proper state management with ViewModels

### Database Best Practices

- Use Room entities and DAOs for type-safe database access
- Implement proper database migrations for schema changes
- Use transactions for related operations

### Network Requests

- Use Retrofit for all API calls
- Implement proper error handling with try-catch or Result types
- Log requests/responses using OkHttp interceptor

## 📝 Contributing

We welcome contributions! Please follow these guidelines:

1. **Fork the repository**
2. **Create a feature branch** - `git checkout -b feature/your-feature-name`
3. **Commit changes** - `git commit -am 'Add your feature'`
4. **Push to branch** - `git push origin feature/your-feature-name`
5. **Create a Pull Request** with a clear description

## 📚 Resources

- [Android Developers Documentation](https://developer.android.com/)
- [Jetpack Compose Documentation](https://developer.android.com/compose)
- [Room Persistence Library](https://developer.android.com/training/data-storage/room)
- [Retrofit Documentation](https://square.github.io/retrofit/)
- [Firebase Authentication](https://firebase.google.com/docs/auth)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)

## 📄 License

This project is open source and available under the MIT License. See the LICENSE file for more details.

---

## 👥 Authors & Contributors

**Cerdikia Organization** - [https://github.com/Cerdikia](https://github.com/Cerdikia)

---

**Made with ❤️ for education**
