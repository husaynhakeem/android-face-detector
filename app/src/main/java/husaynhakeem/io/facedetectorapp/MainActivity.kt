package husaynhakeem.io.facedetectorapp

import android.os.Bundle
import android.util.Log
import android.util.Size
import androidx.appcompat.app.AppCompatActivity
import com.otaliastudios.cameraview.Facing
import husaynhakeem.io.facedetector.FaceBounds
import husaynhakeem.io.facedetector.FaceDetector
import husaynhakeem.io.facedetector.FaceDetector.OnFaceDetectionResultListener
import husaynhakeem.io.facedetector.Frame
import husaynhakeem.io.facedetector.LensFacing
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lensFacing =
            savedInstanceState?.getSerializable(KEY_LENS_FACING) as Facing? ?: Facing.BACK
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
        faceDetector.setonFaceDetectionFailureListener(object : OnFaceDetectionResultListener {
            override fun onSuccess(faceBounds: List<FaceBounds>) {
                Log.e("MainActivity","Show faceBounds $faceBounds")
            }

            override fun onFailure(exception: Exception) {

            }

        })
        viewfinder.facing = lensFacing
        viewfinder.addFrameProcessor {
            faceDetector.process(
                Frame(
                    data = it.data,
                    rotation = it.rotation,
                    size = Size(it.size.width, it.size.height),
                    format = it.format,
                    lensFacing = if (viewfinder.facing == Facing.BACK) LensFacing.BACK else LensFacing.FRONT
                )
            )
        }

        toggleCameraButton.setOnClickListener {
            viewfinder.toggleFacing()
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val KEY_LENS_FACING = "key-lens-facing"
    }
}
