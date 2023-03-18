package io.medicalvoice.graphicalapp

import android.os.Bundle
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
                OpenGlFragments.CUBE -> "Куб"
                OpenGlFragments.LIGHTING -> "Освещение"
                OpenGlFragments.LIGHTING_TEXTURES -> "Текстурное освещение"
            }
            binding.toolbar.title = title
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}