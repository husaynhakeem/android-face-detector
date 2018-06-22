package husaynhakeem.io.facedetectorapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.otaliastudios.cameraview.Frame
import husaynhakeem.io.facedetector.FaceDetector
import husaynhakeem.io.facedetector.Size
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val facesProcessor = FaceDetector(facesSurface)
        cameraView.addFrameProcessor {
            facesProcessor.process(it.toFaceDetectionFrame())
        }
        revertCameraButton.setOnClickListener { cameraView.toggleFacing() }
    }

    override fun onResume() {
        super.onResume()
        cameraView.start()
    }

    override fun onPause() {
        super.onPause()
        cameraView.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraView.destroy()
    }

    private fun Frame.toFaceDetectionFrame(): husaynhakeem.io.facedetector.Frame {
        return husaynhakeem.io.facedetector.Frame(
                data = data,
                rotation = rotation,
                size = Size(size.width, size.height),
                format = format)
    }
}
