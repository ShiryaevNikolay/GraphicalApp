package io.medicalvoice.graphicalapp.scene_3d

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent

class SceneSurfaceView(context: Context) : GLSurfaceView(context) {

    private val renderer: Renderer

    // private val TOUCH_SCALE_FACTOR = 180.0f / 320.0f
    // private var previousX: Float? = null
    // private var previousY: Float? = null

    init {
        setEGLContextClientVersion(2)
        renderer = SceneRenderer(context)
        setRenderer(renderer)
        renderMode = RENDERMODE_CONTINUOUSLY

        requestFocus()
        isFocusableInTouchMode = true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // val currentX = event.x
        // val currentY = event.y
        //
        // val deltaX: Float
        // val deltaY: Float
        // if (event.action == MotionEvent.ACTION_MOVE) {
        //     deltaX = currentX - previousX
        //     deltaY = currentY - previousY
        // }
        // previousX = currentX
        // previousY = currentY
        return super.onTouchEvent(event)
    }
}