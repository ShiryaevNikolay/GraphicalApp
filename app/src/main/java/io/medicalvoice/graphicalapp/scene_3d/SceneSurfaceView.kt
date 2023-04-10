package io.medicalvoice.graphicalapp.scene_3d

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent

class SceneSurfaceView(context: Context) : GLSurfaceView(context) {

    private val renderer: SceneRenderer

    private var previousX: Float = 0f
    private var previousY: Float = 0f

    init {
        setEGLContextClientVersion(2)
        renderer = SceneRenderer(context)
        setRenderer(renderer)
        renderMode = RENDERMODE_CONTINUOUSLY

        requestFocus()
        isFocusableInTouchMode = true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // val currentX: Float = event.x
        // val currentY: Float = event.y
        //
        // when(event.action) {
        //     MotionEvent.ACTION_MOVE -> {
        //         var deltaX: Float = currentX - previousX
        //         var deltaY: Float = currentY - previousY
        //
        //         // reverse direction of rotation above the mid-line
        //         if (currentY > height / 2) {
        //             deltaX *= -1
        //         }
        //
        //         // reverse direction of rotation to left of the mid-line
        //         if (currentX < width / 2) {
        //             deltaY *= -1
        //         }
        //
        //         val angle = (deltaX * deltaY) * TOUCH_SCALE_FACTOR
        //         renderer.apply {
        //             rotate(angle)
        //             requestRender()
        //         }
        //     }
        // }
        //
        // previousX = currentX
        // previousY = currentY
        return super.onTouchEvent(event)
    }

    private companion object {
        const val TOUCH_SCALE_FACTOR = 180.0f / 320.0f
    }
}