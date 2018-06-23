package husaynhakeem.io.facedetectorapp

import com.otaliastudios.cameraview.CameraView
import husaynhakeem.io.facedetector.camera.CameraWrapper
import husaynhakeem.io.facedetector.FaceBoundsOverlay


class OtaliaStudiosCameraWrapper(
        private val cameraView: CameraView,
        facesSurface: FaceBoundsOverlay) : CameraWrapper(facesSurface) {

    override fun start() = cameraView.start()

    override fun stop() = cameraView.stop()

    override fun destroy() = cameraView.destroy()
}