# Face detector
[![](https://jitpack.io/v/husaynhakeem/android-face-detector.svg)](https://jitpack.io/#husaynhakeem/android-face-detector)


![alt text](https://github.com/husaynhakeem/android-face-detector/blob/master/app/src/main/res/drawable/ic_launcher.png)

Face detector is a face detection Android library which can be easily plugged into any camera API (given it provides a way to process its frames).

Face detector is built on top of MLKit's face detection API.

## Related article

I wrote [the following article](https://heartbeat.fritz.ai/building-a-real-time-face-detector-in-android-with-ml-kit-f930eb7b36d9) in order to explain why this project was built, how it's designed and how one can use it in a real-world Android application.

## Usage

Only 2 easy steps:

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
val faceDetector = FaceDetector(facesBoundsOverlay)
cameraView.addFrameProcessor {
    faceDetector.process(Frame(
            data = it.data,
            rotation = it.rotation,
            size = Size(it.size.width, it.size.height),
            format = it.format,
            lensFacing = cameraView.facing))
}
```

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
implementation 'com.github.husaynhakeem:android-face-detector:2.0'
```

## Demo

![alt text](https://github.com/husaynhakeem/android-face-detector/blob/master/app/art/demo.png)
