# Fixer
Fixer app. Written in Kotlin and implements the Jetpack libraries.
The application fetches movie data using https://fixer.io.


### Used Tech
* [Kotlin](https://kotlinlang.org/)
* [MVVM](https://developer.android.com/jetpack/docs/guide)
* [Data Binding](https://developer.android.com/topic/libraries/data-binding/) - Declaratively bind observable data to UI elements.
* [Lifecycles](https://developer.android.com/topic/libraries/architecture/lifecycle) - Create a UI that automatically responds to lifecycle events.
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Build data objects that notify views when the underlying database changes.
* [Navigation](https://developer.android.com/guide/navigation/) - Handle everything needed for in-app navigation.
* [Paging](https://developer.android.com/topic/libraries/architecture/paging/) - Load and display small chunks of data at a time.
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Store UI-related data that isn't destroyed on app rotations. Easily schedule asynchronous tasks.
* [Retrofit](https://github.com/square/retrofit) - Handle REST api communication.
* [Moshi](https://github.com/square/moshi) - Serialize Kotlin objects and deserialize JSON objects.
* [ktlint](https://ktlint.github.io/) - Enforce Kotlin coding styles.

### Screenshots
<p float="left">
<img alt='Gif 1' src='https://user-images.githubusercontent.com/25232443/77852027-af89aa00-71dc-11ea-8cfd-94a44536f46d.gif' width="auto" height="480"/>
<img alt='Gif 2' src='https://user-images.githubusercontent.com/25232443/77852036-b6182180-71dc-11ea-9b9c-db5d614f99a5.gif' width="480" height="auto"/>
</p>

### How to run the project in development mode
* Clone or download repository as a zip file.
* Open project in Android Studio.
* Set FixerAPI token in build.gradle.
```
             buildTypes.each {
                 it.buildConfigField 'String', 'FIXER_API_TOKEN', keystoreProperties['FixerApiToken']
             }
```
* Run 'app' `SHIFT+F10`.


