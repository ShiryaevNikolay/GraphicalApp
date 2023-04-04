package io.medicalvoice.graphicalapp.scene_3d.data

/**
 * Камера
 *
 * @property position точка положения камеры
 * @property targetLook точка направления камеры
 * @property rotate up-вектор (angle)
 */
class Camera(
    val position: Coordinates,
    val targetLook: Coordinates,
    val rotate: Angle
)