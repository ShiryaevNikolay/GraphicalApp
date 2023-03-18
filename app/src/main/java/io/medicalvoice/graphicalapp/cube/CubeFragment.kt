package io.medicalvoice.graphicalapp.cube

import android.app.Activity
import android.app.ActivityManager
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

class CubeFragment : Fragment() {

    private var glSurfaceView: GLSurfaceView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!supportES2()) {
            Toast.makeText(requireContext(), "OpenGL ES 2.0 is not supported", Toast.LENGTH_LONG).show()
            return null
        }
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

    private fun supportES2(): Boolean {
        val activityManager = requireActivity().getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = activityManager.deviceConfigurationInfo
        return configurationInfo.reqGlEsVersion >= 0x20000
    }
}