package io.medicalvoice.graphicalapp

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.medicalvoice.graphicalapp.cube.CubeFragment
import io.medicalvoice.graphicalapp.lighting.LightingFragment
import io.medicalvoice.graphicalapp.lighttexture.LightingTexturesFragment

class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = OpenGlFragments.values().size

    override fun createFragment(position: Int): Fragment {
        return when(OpenGlFragments.get(position)) {
            OpenGlFragments.CUBE -> CubeFragment()
            OpenGlFragments.LIGHTING -> LightingFragment()
            OpenGlFragments.LIGHTING_TEXTURES -> LightingTexturesFragment()
        }
    }
}