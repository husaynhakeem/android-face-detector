package husaynhakeem.io.facedetectorapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import husaynhakeem.io.facedetector.CameraWrapper
import husaynhakeem.io.facedetector.Frame
import husaynhakeem.io.facedetector.Size
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val cameraWrapper: CameraWrapper by lazy {
        OtaliaStudiosCameraWrapper(otaliaStudiosCameraView, facesSurface)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupOtaliaStuiosCamera()
    }

    private fun setupOtaliaStuiosCamera() {
        otaliaStudiosCameraView.addFrameProcessor {
            cameraWrapper.processFrame(Frame(
                    data = it.data,
                    rotation = it.rotation,
                    size = Size(it.size.width, it.size.height),
                    format = it.format))
        }

        revertCameraButton.setOnClickListener {
            otaliaStudiosCameraView.toggleFacing()
        }
    }

    override fun onResume() {
        super.onResume()
        cameraWrapper.start()
    }

    override fun onPause() {
        super.onPause()
        cameraWrapper.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraWrapper.destroy()
    }
}
