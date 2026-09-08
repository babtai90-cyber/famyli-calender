# Family Calendar

Dark, Skylight-inspired (but original) family calendar Android app.

## Current scope
- Kotlin + Jetpack Compose
- Dark family-calendar UI for phone and tablet
- Two account roles: owner/admin and parent
- Shared calendar foundation
- In-app update check foundation

## App updates
The app checks a server endpoint for the latest app version. When an update is available, the user can download the APK and Android handles the installation/update flow. The existing app is not deleted; the APK must use the same application ID and be signed with the same key for a normal update.

## Important
No passwords, API keys, signing keys, or server secrets belong in this repository.
