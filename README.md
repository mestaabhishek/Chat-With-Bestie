# 📘 Chat-With-Bestie

**Chat-With-Bestie** is a modern, real-time **Android chat application** built with **Firebase** as the backend. The app provides seamless messaging features that let users chat instantly, store messages securely in the cloud, and enjoy a smooth user experience similar to popular chat apps.

This project is perfect for developers learning Android + Firebase, those building chat functionality, or anyone who needs a solid real-time messaging foundation.

---

## 🚀 Key Features

✔ Real-time text messaging
✔ Firebase Authentication (Email/Phone)
✔ Firebase Realtime Database storage
✔ Firebase Storage for media (images & files)
✔ Material UI with modern AndroidX components
✔ Easy to customize and extend

> ⚡ The app leverages AndroidX libraries, Firebase Auth, Realtime Database, and Storage for a full-featured chat experience.

---

## 🧱 Tech Stack

| Layer          | Tech                          |
| -------------- | ----------------------------- |
| Platform       | Android (Java)                |
| UI             | AndroidX, Material Components |
| Backend        | Firebase Realtime Database    |
| Authentication | Firebase Auth                 |
| Storage        | Firebase Storage              |
| Gradle         | Modern Android Gradle Plugin  |

---

## 🏁 Getting Started

### Prerequisites

✔ Android Studio (latest stable version)
✔ A Firebase project
✔ Google Services JSON file (`google-services.json`)

---

### 📦 Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/mestaabhishek/Chat-With-Bestie.git
   cd Chat-With-Bestie
   ```

2. **Open in Android Studio**

3. **Set up Firebase**

   * Go to [Firebase console](https://console.firebase.google.com)
   * Create a new project
   * Add an Android app
   * Download `google-services.json`
   * Place it in `app/`

4. **Enable in Firebase**

   * Authentication (Email / Phone)
   * Realtime Database
   * Storage

5. **Sync & Run**

   * `File > Sync Project`
   * `Run > App`

---

## 🧠 How It Works

1. **User Authentication**

   * Users sign in or register securely
   * Firebase Auth handles accounts

2. **Real-Time Messaging**

   * Messages are stored and updated instantly via Realtime Database

3. **Media Uploads**

   * Images / files can be uploaded to Storage and displayed in chats

---

## 🛠 Project Structure

```
app/
├── src/main/java/
│   ├─ activities/
│   ├─ adapters/
│   ├─ models/
│   └─ utils/
├── res/
│   ├─ layout/
│   ├─ drawable/
│   └─ values/
└── google-services.json
```

---

## 📸 Screenshots (Optional)

> Add screenshots here to make this README look prettier and help users visualize the app.

---

## 🧩 Dependencies

The app uses modern AndroidX and Firebase libraries:

```gradle
implementation 'androidx.appcompat:appcompat:1.7.1'
implementation 'androidx.constraintlayout:constraintlayout:2.2.1'
implementation 'com.google.firebase:firebase-database:22.0.1'
implementation 'com.google.firebase:firebase-auth:24.0.1'
implementation 'com.google.firebase:firebase-storage:22.0.1'
implementation 'com.firebaseui:firebase-ui-database:8.0.2'
```

Make sure your `repositories` include:

```gradle
google()
mavenCentral()
```

---

## 🧪 Testing

You can test using:

✔ JUnit for unit tests
✔ Espresso for UI tests
✔ Real devices or emulators

---

## 📝 Contributing

Thank you for considering contributing!

1. Fork the project
2. Create a new branch (`feature/XYZ`)
3. Commit your changes
4. Create a pull request

---

## 📜 License

This project is open-source and free to use.
(*If you want to choose a specific license like MIT, Apache, add it here*)

---

## ⭐ Stay in Touch

Made with ❤️ by **mestaabhishek**
