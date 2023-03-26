package io.medicalvoice.graphicalapp.scene_2d.data.scene

class Roof : BaseShape() {
    override val vertexArray = floatArrayOf(
        -1.0f, -0.5f, 0.1f,
        -0.375f, 0.0f, 0.1f,
        0.25f, -0.5f, 0.1f
    )
    override val colorArray = floatArrayOf(
        0.60f, 0.40f, 0.0f, 1.0f,
        0.70f, 0.47f, 0.0f, 1.0f,
        0.70f, 0.47f, 0.0f, 1.0f
    )
}