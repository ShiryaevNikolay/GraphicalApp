package io.medicalvoice.graphicalapp

enum class OpenGlFragments {
    LIGHTING,
    LIGHTING_TEXTURES;

    companion object {
        fun get(position: Int): OpenGlFragments = values()[position]
    }
}