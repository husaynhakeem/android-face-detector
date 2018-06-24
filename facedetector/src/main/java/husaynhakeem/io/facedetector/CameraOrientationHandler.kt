package husaynhakeem.io.facedetector

import husaynhakeem.io.facedetector.models.Orientation
import husaynhakeem.io.facedetector.models.convertToOrientation


internal class CameraOrientationHandler {

    private var previousMin: Float = -1f
    private var previousMax: Float = -1f
    private var previousOrientation: Orientation = Orientation.ANGLE_0

    fun updateOrientation(overlayWidth: Int,
                          overlayHeight: Int,
                          rotation: Int,
                          callback: (Float, Float, Orientation) -> Unit) {

        val min = Math.min(overlayWidth, overlayHeight).toFloat()
        val max = Math.max(overlayWidth, overlayHeight).toFloat()
        val orientation = rotation.convertToOrientation()

        if (previousMin == min && previousMax == max && previousOrientation == orientation) {
            return
        }

        previousMin = min
        previousMax = max
        previousOrientation = orientation

        when (orientation) {
            Orientation.ANGLE_0, Orientation.ANGLE_180 -> {
                callback(max, min, orientation)
            }
            Orientation.ANGLE_90, Orientation.ANGLE_270 -> {
                callback(min, max, orientation)
            }
        }
    }
}