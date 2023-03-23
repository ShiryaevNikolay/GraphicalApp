package io.medicalvoice.graphicalapp

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.medicalvoice.graphicalapp.cube.CubeFragment
import io.medicalvoice.graphicalapp.lighting.LightingFragment
import io.medicalvoice.graphicalapp.lighttexture.LightingTexturesFragment
import io.medicalvoice.graphicalapp.scene_2d.Scene2DFragment
import io.medicalvoice.graphicalapp.ship.ShipFragment

class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = OpenGlFragments.values().size

    override fun createFragment(position: Int): Fragment {
        return when(OpenGlFragments.get(position)) {
            OpenGlFragments.SCENE_2D -> Scene2DFragment()
            OpenGlFragments.SHIP -> ShipFragment()
            OpenGlFragments.CUBE -> CubeFragment()
            OpenGlFragments.LIGHTING -> LightingFragment()
            OpenGlFragments.LIGHTING_TEXTURES -> LightingTexturesFragment()
        }
    }
}