package io.medicalvoice.graphicalapp

import android.app.Activity
import android.app.ActivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.medicalvoice.graphicalapp.databinding.ActivityOnlyOneFragmentBinding

class OnlyOneSceneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnlyOneFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!supportES2()) {
            Toast.makeText(this, "OpenGL ES 2.0 is not supported", Toast.LENGTH_LONG).show()
            return
        }

        binding = ActivityOnlyOneFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun supportES2(): Boolean {
        val activityManager = getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = activityManager.deviceConfigurationInfo
        return configurationInfo.reqGlEsVersion >= 0x20000
    }
}