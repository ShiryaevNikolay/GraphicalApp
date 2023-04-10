package io.medicalvoice.graphicalapp.scene_2d

import android.content.Context
import android.opengl.GLSurfaceView

class Scene2DSurfaceView(context: Context) : GLSurfaceView(context) {

    private val renderer: Renderer

    init {
        setEGLContextClientVersion(2)
        renderer = SceneRenderer(context)
        setRenderer(renderer)
        renderMode = RENDERMODE_CONTINUOUSLY
    }
}