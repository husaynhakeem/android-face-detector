package husaynhakeem.io.facedetectorapp

import android.os.Bundle
import android.util.Log
import android.util.Size
import androidx.appcompat.app.AppCompatActivity
import com.otaliastudios.cameraview.Facing
import husaynhakeem.io.facedetector.FaceBounds
import husaynhakeem.io.facedetector.FaceDetector
import husaynhakeem.io.facedetector.Frame
import husaynhakeem.io.facedetector.LensFacing
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val onFaceDetectionResultListener = object : FaceDetector.OnFaceDetectionResultListener{
        override fun onFailure(exception: Exception) {
            super.onFailure(exception)
            exception.printStackTrace()
            Log.e(TAG, "error ${exception.message}")
        }

        override fun onSuccess(faceBounds: List<FaceBounds>) {
            super.onSuccess(faceBounds)
            Log.e(TAG,"total faces ${faceBounds.size}")
            for (face in faceBounds) {
                Log.d(TAG, "face ${face.id} ${face.box.width()} ${face.box.height()}")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lensFacing = Facing.FRONT
        setupCamera(lensFacing)
    }

    override fun onResume() {
        super.onResume()
        viewfinder.start()
    }

    override fun onPause() {
        super.onPause()
        viewfinder.stop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(KEY_LENS_FACING, viewfinder.facing)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewfinder.destroy()
    }

    private fun setupCamera(lensFacing: Facing) {
        val faceDetector = FaceDetector(faceBoundsOverlay)
        faceDetector.setonFaceDetectionFailureListener(onFaceDetectionResultListener)
        viewfinder.facing = lensFacing
        viewfinder.rotation = 180f
        viewfinder.addFrameProcessor {
            faceDetector.process(
                Frame(
                    data = it.data,
                    rotation = it.rotation,
                    size = Size(it.size.width, it.size.height),
                    format = it.format,
                    lensFacing = LensFacing.FRONT
                )
            )
        }



    }

    //111.375 338.5481

    companion object {
        private const val TAG = "MainActivity"
        private const val KEY_LENS_FACING = "key-lens-facing"
    }
}
