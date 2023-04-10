package io.medicalvoice.graphicalapp

enum class OpenGlFragments {
    SCENE_3D,
    SCENE_2D,
    SHIP,
    CUBE,
    LIGHTING,
    LIGHTING_TEXTURES;

    companion object {
        fun get(position: Int): OpenGlFragments = values()[position]
    }
}