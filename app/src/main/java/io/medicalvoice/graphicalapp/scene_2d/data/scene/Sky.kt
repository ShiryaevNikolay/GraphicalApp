package io.medicalvoice.graphicalapp.scene_2d.data.scene

/**
 * Данные для неба
 */
class Sky : BaseShape() {
    override val vertexArray = floatArrayOf(
        -1.0f, 2.0f, 0.0f,
        -1.0f, -0.35f, 0.0f,
        1.0f, 2.0f, 0.0f,
        1.0f, -0.35f, 0.0f
    )
    override val colorArray = floatArrayOf(
        0.68f, 0.85f, 0.90f, 1.0f,
        0.68f, 0.85f, 0.90f, 1.0f,
        0.68f, 0.85f, 0.90f, 1.0f,
        0.68f, 0.85f, 0.90f, 1.0f
    )
}