# 🏋️ SportHelper

A modern **Android application** for tracking body measurements and fitness progress.

## 🚀 Features

- **Body Measurements Tracking** - Monitor various body parts (weight, height, waist, biceps, chest,
  etc.)
- **Firebase Authentication** - Secure Google Sign-In integration
- **Cloud Sync** - Data stored in Firebase Firestore
- **Material Design 3** - Beautiful, modern UI with Jetpack Compose
- **Responsive Layout** - Adaptive design for different screen sizes
- **Dark Theme Support** - Comfortable viewing in any lighting condition
- **Progress History** - View your measurement history and track changes over time

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

## 📋 Tracked Measurements

The app allows tracking of the following body parts:

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

## 📱 Requirements

- **Minimum SDK**: 23 (Android 6.0)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 35

## 🏗️ Project Structure

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
```

## 🔧 Setup

1. Clone the repository
2. Open the project in Android Studio
3. Add your `google-services.json` file from Firebase Console
4. Configure Google Sign-In in Firebase Console
5. Build and run the app

## 🔑 Firebase Configuration

To run this app, you need to:

1. Create a Firebase project
2. Enable Firebase Authentication with Google Sign-In provider
3. Enable Cloud Firestore
4. Download and add `google-services.json` to the `app/` directory

## 📦 Dependencies

Key dependencies used in this project:

- Jetpack Compose BOM
- Material 3 Components
- Hilt (Dependency Injection)
- Navigation Compose
- Firebase BOM (Auth, Firestore)
- Coil (Image Loading)
- AndroidX Credentials & Google ID
- Core Splashscreen

## 📄 License

This project is for educational and personal use.
