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
![image](https://user-images.githubusercontent.com/25232443/60764654-18008c00-a08e-11e9-8d7e-f62b0d6cffea.gif)
![image](https://user-images.githubusercontent.com/25232443/60773071-0657cc80-a100-11e9-8744-62a8962ffffa.gif)

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


