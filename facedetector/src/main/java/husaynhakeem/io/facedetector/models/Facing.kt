package husaynhakeem.io.facedetector.models


enum class Facing(val value: Int) {
    BACK(0),
    FRONT(1)
}

internal fun Boolean.convertToFacing() = when (this) {
    true -> Facing.BACK
    false -> Facing.FRONT
}