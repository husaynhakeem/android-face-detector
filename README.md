# Face detector
[![](https://jitpack.io/v/husaynhakeem/android-face-detector.svg)](https://jitpack.io/#husaynhakeem/android-face-detector)


![alt text](https://github.com/husaynhakeem/android-face-detector/blob/master/app/src/main/res/drawable/ic_launcher.png)

Face detector is a face detection Android library which can be easily plugged into any camera API (given it provides a way to process its frames).

Face detector is built on top of Firebase ML Kit's face detection API.


## Related article


## Usage

Three easy steps:

1. Add a `FaceBoundsOverlay` on top of your camera view.
```xml
<FrameLayout
    ...>
    
    // Any other views

    <CameraView
        ... />

    <husaynhakeem.io.facedetector.FaceBoundsOverlay
        ... />

    // Any other views
    
</FrameLayout>
```

2. Define a `FaceDetection` instance and connect it to your camera.
```kotlin
private val faceDetector: FaceDetector by lazy {
    FaceDetector(facesBoundsOverlay)
}

...

cameraView.addFrameProcessor {
    faceDetector.process(Frame(
            data = it.data,
            rotation = it.rotation,
            size = Size(it.size.width, it.size.height),
            format = it.format,
            isCameraFacingBack = cameraView.facing))
}
```

3. [Setup firebase](https://firebase.google.com/docs/android/setup) in your Android project


## Download
1. Add the code below in your root build.gradle at the end of repositories
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
2. Add the dependency
```groovy
implementation 'com.github.husaynhakeem:android-face-detector:v1.0'
```


## Demo
