package io.medicalvoice.graphicalapp.scene_2d.data

/**
 * Координаты
 *
 * @property x координата по оси X
 * @property y координата по оси Y
 * @property z координата по оси Z
 */
class Coordinate(
    val x: Float = 0.0f,
    val y: Float = 0.0f,
    val z: Float = 0.0f,
) {
    companion object {
        val DEFAULT = Coordinate()
    }
}
