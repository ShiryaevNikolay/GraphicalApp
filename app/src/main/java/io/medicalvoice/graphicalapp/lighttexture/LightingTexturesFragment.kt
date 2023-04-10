package io.medicalvoice.graphicalapp.lighttexture

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.medicalvoice.graphicalapp.lighttexture.renderer.OpenGLRenderer

class LightingTexturesFragment : Fragment() {

    private var glSurfaceView: GLSurfaceView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        glSurfaceView = GLSurfaceView(requireContext()).apply {
            setEGLContextClientVersion(2)
            setRenderer(OpenGLRenderer(requireContext()))
        }
        return glSurfaceView
    }

    override fun onPause() {
        super.onPause()
        glSurfaceView!!.onPause()
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView!!.onResume()
    }
}