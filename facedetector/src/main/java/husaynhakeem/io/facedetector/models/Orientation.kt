package husaynhakeem.io.facedetector.models


enum class Orientation {
    ANGLE_0,
    ANGLE_90,
    ANGLE_180,
    ANGLE_270
}

internal fun Int.convertToOrientation() = when (this) {
    0 -> Orientation.ANGLE_0
    90 -> Orientation.ANGLE_90
    180 -> Orientation.ANGLE_180
    270 -> Orientation.ANGLE_270
    else -> Orientation.ANGLE_270
}