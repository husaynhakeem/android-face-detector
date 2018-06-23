package husaynhakeem.io.facedetector

import android.util.Log
import android.widget.Toast
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import husaynhakeem.io.facedetector.camera.CameraOrientationHandler
import husaynhakeem.io.facedetector.models.FaceBounds
import husaynhakeem.io.facedetector.models.Frame

class FaceDetector(private val faceBoundsOverlay: FaceBoundsOverlay) {

    private val cameraOrientationHandler = CameraOrientationHandler(faceBoundsOverlay)

    private val faceDetectorOptions: FirebaseVisionFaceDetectorOptions by lazy {
        FirebaseVisionFaceDetectorOptions.Builder()
                .setModeType(FirebaseVisionFaceDetectorOptions.ACCURATE_MODE)
                .setLandmarkType(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setClassificationType(FirebaseVisionFaceDetectorOptions.NO_CLASSIFICATIONS)
                .setMinFaceSize(MIN_FACE_SIZE)
                .setTrackingEnabled(true)
                .build()
    }

    private val faceDetector: FirebaseVisionFaceDetector by lazy {
        FirebaseVision.getInstance().getVisionFaceDetector(faceDetectorOptions)
    }

    fun process(frame: Frame) {
        cameraOrientationHandler.orientation = frame.rotation
        faceDetector.detectInImage(convertFrameToImage(frame))
                .addOnSuccessListener {
                    faceBoundsOverlay.updateFaces(convertToListOfFaceBounds(it))
                }
                .addOnFailureListener {
                    Toast.makeText(faceBoundsOverlay.context, "Error processing images: $it", Toast.LENGTH_LONG).show()
                    Log.e(TAG, "Error processing images: $it")
                }
    }

    private fun convertFrameToImage(frame: Frame) =
            FirebaseVisionImage.fromByteArray(frame.data, extractFrameMetadata(frame))

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
        private val TAG = FaceDetector::class.java.simpleName
        private const val MIN_FACE_SIZE = 0.15f
        private const val RIGHT_ANGLE = 90
    }
}