package husaynhakeem.io.facedetector

import kotlin.properties.Delegates


class FaceBoundsOverlayOrientationHandler(private val faceBoundsOverlay: FaceBoundsOverlay) {

    private val min: Float by lazy {
        Math.min(faceBoundsOverlay.width, faceBoundsOverlay.height).toFloat()
    }

    private val max: Float by lazy {
        Math.max(faceBoundsOverlay.width, faceBoundsOverlay.height).toFloat()
    }

    var orientation: Int by Delegates.observable(0) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            val orientation = convertToOrientation(newValue)
            when(orientation) {
                Orientation.ANGLE_0, Orientation.ANGLE_180 -> {
                    faceBoundsOverlay.cameraPreviewWidth = max
                    faceBoundsOverlay.cameraPreviewHeight = min
                }
                Orientation.ANGLE_90, Orientation.ANGLE_270 -> {
                    faceBoundsOverlay.cameraPreviewWidth = min
                    faceBoundsOverlay.cameraPreviewHeight = max
                }
            }
            faceBoundsOverlay.cameraOrientation = orientation
        }
    }

    private fun convertToOrientation(angle: Int) = when (angle) {
        0 -> Orientation.ANGLE_0
        90 -> Orientation.ANGLE_90
        180 -> Orientation.ANGLE_180
        270 -> Orientation.ANGLE_270
        else -> Orientation.ANGLE_270
    }
}