package io.medicalvoice.graphicalapp.scene_2d.data.scene

/**
 * Данные для земли
 */
class Ground : BaseShape() {
    override val vertexArray = floatArrayOf(
        -1.0f, -0.35f, 0.0f,
        1.0f, -0.35f, 0f,
        -1.0f, -2.0f, 0.0f,
        1.0f, -2.0f, 0f
    )
    override val colorArray = floatArrayOf(
        0.25f, 0.7f, 0.17f, 1.0f,
        0.25f, 0.7f, 0.17f, 1.0f,
        0.25f, 0.7f, 0.17f, 1.0f,
        0.25f, 0.7f, 0.17f, 1.0f
    )
}