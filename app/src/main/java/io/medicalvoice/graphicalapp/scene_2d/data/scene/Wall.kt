package io.medicalvoice.graphicalapp.scene_2d.data.scene

class Wall : BaseShape() {
    override val vertexArray = floatArrayOf(
        -0.75f, -0.5f, 0.1f,
        -0.75f, -1.0f, 0.1f,
        0f, -0.5f, 0.1f,
        0f, -1.0f, 0.1f
    )
    override val colorArray = floatArrayOf(
        -0.3f, 0.2f, 0.5f, 1f,
        -0.3f, 0.2f, 0.5f, 1f,
        -0.3f, 0.2f, 0.5f, 1f,
        -0.3f, 0.2f, 0.5f, 1f,
    )
}