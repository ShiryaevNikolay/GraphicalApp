package io.medicalvoice.graphicalapp

enum class OpenGlFragments {
    CUBE,
    LIGHTING,
    LIGHTING_TEXTURES;

    companion object {
        fun get(position: Int): OpenGlFragments = values()[position]
    }
}