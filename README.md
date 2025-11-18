# 🏋️ SportHelper

A modern **Android application** for tracking body measurements and fitness progress.
<div align="center">

## 🚀 Features
A modern **Android application** for tracking body measurements and fitness progress, built with
Jetpack Compose and following clean architecture principles.

- **Body Measurements Tracking** - Monitor various body parts (weight, height, waist, biceps, chest,
  etc.)
- **Firebase Authentication** - Secure Google Sign-In integration
- **Cloud Sync** - Data stored in Firebase Firestore
- **Material Design 3** - Beautiful, modern UI with Jetpack Compose
- **Responsive Layout** - Adaptive design for different screen sizes
- **Dark Theme Support** - Comfortable viewing in any lighting condition
- **Progress History** - View your measurement history and track changes over time

  [![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0-blue.svg?style=flat&logo=kotlin)](http://kotlinlang.org)
  [![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.09.02-brightgreen)](https://developer.android.com/jetpack/compose)
  [![Firebase](https://img.shields.io/badge/Firebase-34.2.0-orange.svg?style=flat&logo=firebase)](https://firebase.google.com/)
  [![License](https://img.shields.io/badge/License-Educational-yellow.svg)](LICENSE)

</div>

## ✨ Features

- 📊 **Body Measurements Tracking** - Monitor various body parts (weight, height, waist, biceps,
  chest, etc.)
- 🔐 **Firebase Authentication** - Secure Google Sign-In integration
- ☁️ **Cloud Sync** - Data stored in Firebase Firestore for seamless access across devices
- 🎨 **Modern UI** - Beautiful Material 3 design with Jetpack Compose
- 📱 **Responsive Layout** - Adaptive design for different screen sizes
- 🌙 **Dark Theme Support** - Comfortable viewing in any lighting condition
- 📈 **Progress History** - View your measurement history and track changes over time
- 💾 **Persistent Data** - Never lose your fitness journey data

## 📸 Screenshots

<div align="center">

### Sign In Screen

<img src="screenshots/signin.png" alt="Sign In Screen" width="300"/>

Sign in with Google or continue as a guest to start tracking your fitness journey.

---

### Dashboard

<img src="screenshots/dashboard.png" alt="Dashboard" width="300"/>

View all your body measurements at a glance. Track multiple metrics like biceps, body fat, calves,
chest, height, waist, and weight.

---

### Add Measurements

<img src="screenshots/add_item.png" alt="Add New Item" width="300"/>

Easily select which body parts you want to track with a simple toggle interface.

---

### Progress Tracking with Charts

<img src="screenshots/details.png" alt="Details with Chart" width="300"/>

Visualize your progress over time with interactive charts. View your measurement history for the
last 7 days, 30 days, or all time. Track changes and see your fitness journey unfold with beautiful
graph visualizations.

</div>

## 🏗️ Architecture

This project follows **MVVM + Clean Architecture** principles with clear separation of concerns:

```
├── presentation/     # UI Layer (Compose UI, ViewModels)
│   ├── signin/      # Authentication screen
│   ├── dashboard/   # Main dashboard
│   ├── add_item/    # Add measurement screen
│   ├── details/     # Measurement details
│   └── theme/       # App theming
├── domain/          # Business Logic (Models, Repository Interfaces)
├── data/            # Data Layer (Repositories, Mappers)
│   ├── mapper/     # Data mappers
│   └── repository/ # Repository implementations
└── di/             # Dependency Injection (Hilt)
```

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: Dagger Hilt
- **Navigation**: Navigation Compose
- **Authentication**: Firebase Auth with Google Sign-In
- **Database**: Firebase Firestore
- **Image Loading**: Coil
- **Serialization**: Kotlinx Serialization
### Core Technologies

- **[Kotlin](https://kotlinlang.org/)** - Modern programming language for Android
- **[Jetpack Compose](https://developer.android.com/jetpack/compose)** - Modern declarative UI
  framework
- **[Material Design 3](https://m3.material.io/)** - Latest Material Design components

### Libraries & Frameworks

| Library | Purpose | Version |
|---------|---------|---------|
| [Jetpack Compose](https://developer.android.com/jetpack/compose) | Declarative UI framework | 2024.09.02 |
| [Dagger Hilt](https://dagger.dev/hilt/) | Dependency injection | 2.56.2 |
| [Firebase Auth](https://firebase.google.com/products/auth) | Authentication | 34.2.0 |
| [Firebase Firestore](https://firebase.google.com/products/firestore) | Cloud database | 34.2.0 |
| [Navigation Compose](https://developer.android.com/jetpack/compose/navigation) | In-app navigation | 2.9.3 |
| [Coil](https://coil-kt.github.io/coil/) | Image loading | 3.0.0 |
| [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization) | JSON parsing | 1.7.2 |
| [Credentials API](https://developer.android.com/training/sign-in/passkeys) | Credential management | 1.6.0-alpha05 |
| [Core Splashscreen](https://developer.android.com/develop/ui/views/launch/splash-screen) | Splash screen | 1.0.1 |

## 📋 Tracked Measurements

The app allows tracking of the following body parts:
The app allows tracking of the following body metrics:

- Weight (kg)
- Height (cm)
- Body Fat (%)
- Waist (cm)
- Chest (cm)
- Biceps - Left & Right (cm)
- Triceps - Left & Right (cm)
- Shoulders (cm)
- Hips (cm)
- Thighs - Left & Right (cm)
- Calves - Left & Right (cm)
- **Weight** (kg)
- **Height** (cm)
- **Body Fat** (%)
- **Waist** (cm)
- **Chest** (cm)
- **Biceps** - Left & Right (cm)
- **Triceps** - Left & Right (cm)
- **Shoulders** (cm)
- **Hips** (cm)
- **Thighs** - Left & Right (cm)
- **Calves** - Left & Right (cm)

## 🚀 Getting Started

### Prerequisites

- **Android Studio** Ladybug or later (2024.2.1+)
- **JDK** 11 or higher
- **Kotlin** 2.0.0
- **Android SDK** with API level 23+ (Android 6.0+)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/SportHelper.git
   cd SportHelper
   ```

2. **Open in Android Studio**
  - Open Android Studio
  - Select "Open an Existing Project"
  - Navigate to the cloned directory

3. **Configure Firebase**
  - Create a Firebase project in [Firebase Console](https://console.firebase.google.com/)
  - Enable Firebase Authentication with Google Sign-In provider
  - Enable Cloud Firestore
  - Download `google-services.json` file
  - Place it in the `app/` directory

4. **Run the app**
  - Select your device/emulator
  - Click Run ▶️

### Configuration

Create a `local.properties` file in the root directory if it doesn't exist:

```properties
sdk.dir=YOUR_ANDROID_SDK_PATH
```

## 📱 Requirements

- **Minimum SDK**: 23 (Android 6.0)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 35
  | Requirement | Version |
  |-------------|---------|
  | **Minimum SDK** | API 23 (Android 6.0) |
  | **Target SDK** | API 34 (Android 14) |
  | **Compile SDK** | API 35 |

## 🏗️ Project Structure
## 🏛️ Project Structure

```
app/
├── data/              # Data layer (repositories, mappers)
├── domain/            # Domain layer (models, repository interfaces)
├── presentation/      # UI layer (screens, components, viewmodels)
│   ├── signin/       # Authentication screen
│   ├── dashboard/    # Main dashboard
│   ├── add_item/     # Add measurement screen
│   ├── details/      # Measurement details
│   └── theme/        # App theming
└── di/               # Dependency injection modules
SportHelper/
├── app/
│   ├── src/
│   │   └── main/
│   │       └── java/com/example/sporthelper/
│   │           ├── data/              # Data layer
│   │           │   ├── mapper/       # Data mappers
│   │           │   └── repository/   # Repository implementations
│   │           ├── domain/            # Domain layer
│   │           │   ├── model/        # Domain models
│   │           │   └── repository/   # Repository interfaces
│   │           ├── presentation/      # UI layer
│   │           │   ├── signin/       # Authentication screen
│   │           │   ├── dashboard/    # Main dashboard
│   │           │   ├── add_item/     # Add measurement screen
│   │           │   ├── details/      # Measurement details
│   │           │   └── theme/        # App theming
│   │           └── di/               # Dependency injection
│   ├── build.gradle.kts
│   └── google-services.json          # Firebase config (add this)
├── gradle/                            # Gradle wrapper
└── build.gradle.kts                   # Root build file
```

## 🔧 Setup
## 🎯 Key Features Implementation

1. Clone the repository
2. Open the project in Android Studio
3. Add your `google-services.json` file from Firebase Console
4. Configure Google Sign-In in Firebase Console
5. Build and run the app
### 🔐 Authentication (Firebase Auth)

- Google Sign-In integration
- Secure credential management
- Persistent user sessions
- Automatic token refresh

### ☁️ Cloud Sync (Firestore)

- Real-time data synchronization
- Offline data persistence
- Efficient data queries
- User-specific data isolation

### 🎨 UI/UX (Jetpack Compose)

- Declarative UI components
- Material 3 theming
- Responsive layouts
- Smooth animations and transitions

### 💉 Dependency Injection (Hilt)

- Modular DI setup
- ViewModel injection
- Repository pattern implementation
- Lifecycle-aware components

## 🔑 Firebase Configuration

To run this app, you need to:

1. Create a Firebase project
2. Enable Firebase Authentication with Google Sign-In provider
3. Enable Cloud Firestore
4. Download and add `google-services.json` to the `app/` directory
1. **Create a Firebase project** at [Firebase Console](https://console.firebase.google.com/)
2. **Enable Firebase Authentication**
  - Go to Authentication → Sign-in method
  - Enable Google Sign-In provider
  - Add your SHA-1 and SHA-256 fingerprints
3. **Enable Cloud Firestore**
  - Go to Firestore Database
  - Create database in production mode
  - Set up security rules
4. **Download configuration**
  - Download `google-services.json`
  - Place it in the `app/` directory

## 📦 Dependencies
### Security Rules (Firestore)

Key dependencies used in this project:
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId}/{document=**} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

- Jetpack Compose BOM
- Material 3 Components
- Hilt (Dependency Injection)
- Navigation Compose
- Firebase BOM (Auth, Firestore)
- Coil (Image Loading)
- AndroidX Credentials & Google ID
- Core Splashscreen
## 🧪 Testing

## 📄 License
```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Run all tests
./gradlew testDebugUnitTest connectedAndroidTest
```

## 📦 Build

### Debug Build

```bash
./gradlew assembleDebug
```

### Release Build

```bash
./gradlew assembleRelease
```

The APK will be generated in `app/build/outputs/apk/release/`

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

For major changes, please open an issue first to discuss what you would like to change.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request


## 📝 License

This project is for educational and personal use.

## 🙏 Acknowledgments

- [Google](https://www.google.com/) for Firebase and Android ecosystem
- [JetBrains](https://www.jetbrains.com/) for Kotlin
- The amazing Android development community

## 📚 Resources

- [Android Developers](https://developer.android.com/)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Firebase Documentation](https://firebase.google.com/docs)
- [Material Design 3](https://m3.material.io/)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)


---

<div align="center">

Made with ❤️ using Jetpack Compose

</div>
