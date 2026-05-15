# 🎾 Premium Android TV Sports Streaming App

A modern, high-performance Android TV & Google TV sports streaming application built entirely with **Jetpack Compose for TV** and **Media3 (ExoPlayer)**. 

This project demonstrates a production-ready approach to building smart TV interfaces, featuring a cinematic dark-mode UI, fully custom video player controls, strict MVVM architecture, and integrated video playback analytics.

![Platform](https://img.shields.io/badge/Platform-Android%20TV%20%7C%20Google%20TV-3DDC84?logo=android)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-7F52FF?logo=kotlin)
![ComposeTV](https://img.shields.io/badge/Jetpack%20Compose-TV%20Material%203-4285F4)
![Media3](https://img.shields.io/badge/Media3-ExoPlayer-FF0000)

---

## ✨ Key Features

* **Cinematic Compose TV UI:** 100% native Jetpack Compose UI optimized for D-pad navigation. Features custom focus states, slick animations, and scalable vector graphics drawn directly on the `Canvas`.
* **MVVM Architecture:** Clean separation of concerns using ViewModels, `StateFlow`, and stateless composable UI functions.
* **Advanced ExoPlayer Integration:** * Supports Live HLS (`.m3u8`) and VOD streams. Currently utilizing the following test streams:
    * **Live (HLS):** `https://feeds.intoday.in/aajtak/api/aajtakhd/master.m3u8`
    * **VOD (MP4):** `https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4`
  * Completely custom player overlay (playback controls, seek bar, settings).
  * Seamless "Match Over" overlay with "Up Next" recommendations.
  * Graceful playback error handling with mock DRM error screens.
* **Mock Authentication Flow:** Beautiful TV login screen featuring native code-drawn QR codes and Auth0-style device activation flows.

---

## 📸 App Preview

<p align="center">
  <video src="https://github.com/user-attachments/assets/66569119-9a34-4952-8483-e4f0bab1a98f" width="100%" autoplay loop muted playsinline></video>
</p>

## 🏗️ Architecture & Folder Structure

The app follows standard Android architectural principles, ensuring the UI layer is purely reactive to the ViewModel state.

```text
com.demo.androidtv
 ┣ 📂 core
 ┃ ┗ 📂 analytics
 ┃   ┗ 📜 StreamAnalyticsListener.kt   # ExoPlayer TTFF & QoE tracking
 ┣ 📂 domain
 ┃ ┗ 📜 Match.kt                       # Data models
 ┣ 📂 presentation                     # ViewModels & StateFlows
 ┃ ┣ 📜 AuthViewModel.kt
 ┃ ┣ 📜 HomeViewModel.kt
 ┃ ┗ 📜 PlayerViewModel.kt
 ┣ 📂 ui
 ┃ ┗ 📂 theme                          # Colors, Typography, Shapes
 ┣ 📜 MainActivity.kt                  # Compose Navigation Graph
 ┣ 📜 AuthScreen.kt                    # Stateless UI
 ┣ 📜 HomeScreen.kt                    # Stateless UI
 ┗ 📜 PlayerScreen.kt                  # Stateless UI + ExoPlayer Surface
