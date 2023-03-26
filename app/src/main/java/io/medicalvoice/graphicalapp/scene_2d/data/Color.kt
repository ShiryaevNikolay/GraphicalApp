package io.medicalvoice.graphicalapp.scene_2d.data

/**
 * Цвет
 *
 * @param red значение КРАСНОГО цвета
 * @param green значение ЗЕЛЕНОГО цвета
 * @param blue значение СИНЕГО цвета
 * @param alpha альфа канал/прозрачность
 */
class Color(
    val red: Float = 0.0f,
    val green: Float = 0.0f,
    val blue: Float = 0.0f,
    val alpha: Float = 1.0f
)

fun Color.toCollection(): Collection<Float> = listOf(red, green, blue, alpha)
