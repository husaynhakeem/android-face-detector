package husaynhakeem.io.facedetector

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import husaynhakeem.io.facedetector.models.FaceBounds
import husaynhakeem.io.facedetector.models.Facing
import husaynhakeem.io.facedetector.models.Orientation


/**
 * A view that renders the results of a face detection process. It contains a list of faces
 * bounds which it draws using a set of attributes provided by the camera: Its width, height,
 * orientation and facing. These attributes impact how the face bounds are drawn, especially
 * the scaling factor between this view and the camera view, and the mirroring of coordinates.
 */
class FaceBoundsOverlay @JvmOverloads constructor(
        ctx: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : View(ctx, attrs, defStyleAttr) {

    private val facesBounds: MutableList<FaceBounds> = mutableListOf()

    private val anchorPaint = Paint()
    private val idPaint = Paint()
    private val boundsPaint = Paint()

    var cameraPreviewWidth: Float = 0f
    var cameraPreviewHeight: Float = 0f
    var cameraOrientation: Orientation = Orientation.ANGLE_270
    var cameraFacing: Facing = Facing.BACK

    init {
        anchorPaint.color = ContextCompat.getColor(context, android.R.color.holo_blue_dark)

        idPaint.color = ContextCompat.getColor(context, android.R.color.holo_blue_dark)
        idPaint.textSize = 40f

        boundsPaint.style = Paint.Style.STROKE
        boundsPaint.color = ContextCompat.getColor(context, android.R.color.holo_blue_dark)
        boundsPaint.strokeWidth = 4f
    }

    /**
     * Repopulates the face bounds list, and calls for a redraw of the view.
     */
    fun updateFaces(bounds: List<FaceBounds>) {
        facesBounds.clear()
        facesBounds.addAll(bounds)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        facesBounds.forEach {
            val centerX = computeFaceBoundsCenterX(canvas.width.toFloat(), scaleX(it.box.exactCenterX(), canvas))
            val centerY = computeFaceBoundsCenterY(canvas.height.toFloat(), scaleY(it.box.exactCenterY(), canvas))
            drawAnchor(canvas, centerX, centerY)
            drawId(canvas, it.id.toString(), centerX, centerY)
            drawBounds(it.box, canvas, centerX, centerY)
        }
    }

    /**
     * Calculates the center of the face bounds's X coordinate depending on the camera's facing
     * and orientation. A change in the facing results in mirroring the coordinate.
     */
    private fun computeFaceBoundsCenterX(viewWidth: Float, scaledCenterX: Float) = when {
        cameraFacing == Facing.FRONT && cameraOrientation == Orientation.ANGLE_270 -> viewWidth - scaledCenterX
        cameraFacing == Facing.FRONT && cameraOrientation == Orientation.ANGLE_90 -> scaledCenterX
        cameraFacing == Facing.BACK && cameraOrientation == Orientation.ANGLE_270 -> viewWidth - scaledCenterX
        cameraFacing == Facing.BACK && cameraOrientation == Orientation.ANGLE_90 -> scaledCenterX
        else -> scaledCenterX
    }

    /**
     * Calculates the center of the face bounds's Y coordinate depending on the camera's facing
     * and orientation. A change in the facing results in mirroring the coordinate.
     */
    private fun computeFaceBoundsCenterY(viewHeight: Float, scaledCenterY: Float) = when {
        cameraFacing == Facing.FRONT && cameraOrientation == Orientation.ANGLE_270 -> scaledCenterY
        cameraFacing == Facing.FRONT && cameraOrientation == Orientation.ANGLE_90 -> viewHeight - scaledCenterY
        cameraFacing == Facing.BACK && cameraOrientation == Orientation.ANGLE_270 -> viewHeight - scaledCenterY
        cameraFacing == Facing.BACK && cameraOrientation == Orientation.ANGLE_90 -> scaledCenterY
        else -> scaledCenterY
    }

    /**
     * Draws an anchor/dot at the center of a face
     */
    private fun drawAnchor(canvas: Canvas, centerX: Float, centerY: Float) {
        canvas.drawCircle(
                centerX,
                centerY,
                ANCHOR_RADIUS,
                anchorPaint)
    }

    /**
     * Draws/Writes the face's id
     */
    private fun drawId(canvas: Canvas, id: String, centerX: Float, centerY: Float) {
        canvas.drawText(
                "face id $id",
                centerX - ID_OFFSET,
                centerY + ID_OFFSET,
                idPaint)
    }

    /**
     * Draws bounds around a face as a rectangle
     */
    private fun drawBounds(box: Rect, canvas: Canvas, centerX: Float, centerY: Float) {
        val xOffset = scaleX(box.width() / 2.0f, canvas)
        val yOffset = scaleY(box.height() / 2.0f, canvas)
        val left = centerX - xOffset
        val right = centerX + xOffset
        val top = centerY - yOffset
        val bottom = centerY + yOffset
        canvas.drawRect(
                left,
                top,
                right,
                bottom,
                boundsPaint)
    }

    /**
     * Adjusts a horizontal value x from the camera preview scale to the view scale
     */
    private fun scaleX(x: Float, canvas: Canvas) =
            x * (canvas.width.toFloat() / cameraPreviewWidth)

    /**
     * Adjusts a vertical value y from the camera preview scale to the view scale
     */
    private fun scaleY(y: Float, canvas: Canvas) =
            y * (canvas.height.toFloat() / cameraPreviewHeight)

    companion object {
        private const val ANCHOR_RADIUS = 10f
        private const val ID_OFFSET = 50f
    }
}