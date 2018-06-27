package husaynhakeem.io.facedetector.models

import android.graphics.Rect


data class FaceBounds(val id: Int, val box: Rect)

data class Frame(
        val data: ByteArray?,
        val rotation: Int,
        val size: Size,
        val format: Int,
        val isCameraFacingBack: Boolean)

data class Size(val width: Int, val height: Int)