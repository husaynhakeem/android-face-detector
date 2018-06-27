package husaynhakeem.io.facedetector

import android.widget.Toast
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import husaynhakeem.io.facedetector.models.FaceBounds
import husaynhakeem.io.facedetector.models.Frame

class FaceDetector(private val faceBoundsOverlay: FaceBoundsOverlay) {

    private val faceBoundsOverlayHandler = FaceBoundsOverlayHandler()
    private val firebaseFaceDetectorWrapper = FirebaseFaceDetectorWrapper()

    fun process(frame: Frame) {
        updateOverlayAttributes(frame)
        detectFacesIn(frame)
    }

    private fun updateOverlayAttributes(frame: Frame) {
        faceBoundsOverlayHandler.updateOverlayAttributes(
                overlayWidth = frame.size.width,
                overlayHeight = frame.size.height,
                rotation = frame.rotation,
                isCameraFacingBack = frame.isCameraFacingBack,
                callback = { newWidth, newHeight, newOrientation, newFacing ->
                    faceBoundsOverlay.cameraPreviewWidth = newWidth
                    faceBoundsOverlay.cameraPreviewHeight = newHeight
                    faceBoundsOverlay.cameraOrientation = newOrientation
                    faceBoundsOverlay.cameraFacing = newFacing
                })
    }

    private fun detectFacesIn(frame: Frame) {
        frame.data?.let {
            firebaseFaceDetectorWrapper.process(
                    image = convertFrameToImage(frame),
                    onSuccess = {
                        faceBoundsOverlay.updateFaces(convertToListOfFaceBounds(it))
                    },
                    onError = {
                        Toast.makeText(faceBoundsOverlay.context, "Error processing images: $it", Toast.LENGTH_LONG).show()
                    })
        }
    }

    private fun convertFrameToImage(frame: Frame) =
            FirebaseVisionImage.fromByteArray(frame.data!!, extractFrameMetadata(frame))

    private fun extractFrameMetadata(frame: Frame): FirebaseVisionImageMetadata =
            FirebaseVisionImageMetadata.Builder()
                    .setWidth(frame.size.width)
                    .setHeight(frame.size.height)
                    .setFormat(frame.format)
                    .setRotation(frame.rotation / RIGHT_ANGLE)
                    .build()

    private fun convertToListOfFaceBounds(faces: MutableList<FirebaseVisionFace>): List<FaceBounds> =
            faces.map { FaceBounds(it.trackingId, it.boundingBox) }

    companion object {
        private const val RIGHT_ANGLE = 90
    }
}