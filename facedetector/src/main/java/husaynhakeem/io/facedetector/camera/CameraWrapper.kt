package husaynhakeem.io.facedetector.camera

import husaynhakeem.io.facedetector.FaceBoundsOverlay
import husaynhakeem.io.facedetector.FaceDetector
import husaynhakeem.io.facedetector.models.Frame


abstract class CameraWrapper(facesSurface: FaceBoundsOverlay) {

    private val faceDetector = FaceDetector(facesSurface)

    fun processFrame(frame: Frame) = faceDetector.process(frame)

    abstract fun start()

    abstract fun stop()

    abstract fun destroy()
}