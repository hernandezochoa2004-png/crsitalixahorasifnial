# Firebase Authentication Integration Plan

Implement functional sign-up, login, and session persistence using Firebase Authentication and update ViewModels and SessionRepository.

## User Review Required

> [!IMPORTANT]
> This will integrate Firebase Auth (`FirebaseAuth`) so that user registration saves email, password, and display name, and login authenticates against Firebase.

## Proposed Changes

### [Dependencies & Gradle]
#### [MODIFY] [build.gradle.kts](file:///C:/Users/herna/AndroidStudioProjects/Cristalix/build.gradle.kts)
- Add Google Services plugin (version 4.4.2) as `apply false`.

#### [MODIFY] [app/build.gradle.kts](file:///C:/Users/herna/AndroidStudioProjects/Cristalix/app/build.gradle.kts)
- Apply Google Services plugin (`com.google.gms.google-services`).
- Add Firebase BoM (33.8.0), Firebase Auth, and Coroutines Play Services.

### [Session & Repositories]
#### [MODIFY] [Sessionrepository.kt](file:///C:/Users/herna/AndroidStudioProjects/Cristalix/app/src/main/java/me/mariahernandez/cristalia/Sessionrepository.kt)
- Add `SessionRepositoryFirebase` implementing `SessionRepository` using `FirebaseAuth.getInstance().currentUser != null`.
- Update default session implementations or keep `SessionRepositoryFirebase` as the standard implementation.

### [Authentication ViewModels]
#### [MODIFY] [RegistroViewModel.kt](file:///C:/Users/herna/AndroidStudioProjects/Cristalix/app/src/main/java/me/mariahernandez/cristalia/login/RegistroViewModel.kt)
- Implement `createUserWithEmailAndPassword` and update user profile display name with `usuario`.

#### [MODIFY] [LoginViewModel.kt](file:///C:/Users/herna/AndroidStudioProjects/Cristalix/app/src/main/java/me/mariahernandez/cristalia/login/LoginViewModel.kt)
- Implement `signInWithEmailAndPassword`.

## Verification Plan

### Automated Tests
- Build project with `gradle_build("app:assembleDebug")`.

### Manual Verification
- Deploy app to emulator/device, test creating a new account (name, email, password) and logging in.
