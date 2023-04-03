package io.medicalvoice.graphicalapp

import android.app.Activity
import android.app.ActivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import io.medicalvoice.graphicalapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewPagerAdapter: FragmentStateAdapter = ViewPagerAdapter(this)

    private val viewPagerListener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            val title = when(OpenGlFragments.get(position)) {
                OpenGlFragments.SCENE_3D -> "2 лаба"
                OpenGlFragments.SCENE_2D -> "1 лаба"
                OpenGlFragments.SHIP -> "Корабль"
                OpenGlFragments.CUBE -> "Куб"
                OpenGlFragments.LIGHTING -> "Освещение"
                OpenGlFragments.LIGHTING_TEXTURES -> "Текстурное освещение"
            }
            binding.toolbar.title = title
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!supportES2()) {
            Toast.makeText(this, "OpenGL ES 2.0 is not supported", Toast.LENGTH_LONG).show()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() = with(binding) {
        viewPager2.apply {
            adapter = viewPagerAdapter
            registerOnPageChangeCallback(viewPagerListener)
        }
    }

    private fun supportES2(): Boolean {
        val activityManager = getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = activityManager.deviceConfigurationInfo
        return configurationInfo.reqGlEsVersion >= 0x20000
    }
}