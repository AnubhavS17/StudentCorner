# Student Corner

## Overview

**Student Corner** is a mobile application designed to assist students with academic needs by combining a real-time chat interface with an AI assistant and a subject-wise PDF resource section. The application enables students to ask doubts, practice coding problems, and access study materials—all within a secure and responsive mobile environment.

## Features

- Real-time chat system for interacting with a personalized AI.
- AI assistant integrated using the OpenAI ChatGPT API.
- Firebase Realtime Database used for storing and syncing chat messages.
- PDF section for browsing and downloading subject materials.
- Notifications implemented using Firebase Cloud Messaging (FCM).
- User authentication via mobile number using Firebase Authentication.
- Clean and responsive UI using RelativeLayout and ConstraintLayout.
- Developed entirely in Java using Android Studio.

## Technology Stack

- **Frontend**: Java, Android Studio
- **UI Layouts**: RelativeLayout, ConstraintLayout
- **Backend Services**:
  - Firebase Realtime Database (Chat)
  - Firebase Authentication (Phone Sign-In)
  - Firebase Storage (PDF files)
  - Firebase Cloud Messaging (Notifications)
- **AI Integration**: OpenAI ChatGPT API

## Architecture

- Firebase Realtime Database handles live chat communication.
- Firebase Authentication provides secure login via phone number.
- Firebase Storage hosts and serves subject-wise PDF materials.
- OpenAI ChatGPT API handles responses to user queries.
- FCM (Firebase Cloud Messaging) delivers push notifications for updates or announcements.

## Security

- Firebase Authentication ensures verified user access.
- Firebase Security Rules protect database and storage access.
- All communication is secured via HTTPS.

## Notifications

- Push notifications are powered by **Firebase Cloud Messaging (FCM)**.
- Used to alert users about:
  - New study materials
  - System announcements
  - Updates or feature releases
- Also provides push notification for your incoming chat messages
