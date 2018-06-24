package husaynhakeem.io.facedetector

import android.util.Log
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions


internal class FirebaseFaceDetectorWrapper {

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

    fun process(image: FirebaseVisionImage,
                onSuccess: (MutableList<FirebaseVisionFace>) -> Unit,
                onError: (Exception) -> Unit) {
        faceDetector.detectInImage(image)
                .addOnSuccessListener {
                    onSuccess(it)
                }
                .addOnFailureListener {
                    onError(it)
                    Log.e(TAG, "Error processing images: $it")
                }
    }

    companion object {
        private val TAG = FirebaseFaceDetectorWrapper::class.java.simpleName
        private const val MIN_FACE_SIZE = 0.15f
    }
}