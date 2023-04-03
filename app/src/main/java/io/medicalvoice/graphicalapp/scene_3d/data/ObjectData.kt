package io.medicalvoice.graphicalapp.scene_3d.data

/**
 * Хранит данные об объекте
 *
 * @property vertexes массив координат вертексов
 * @property textureCoordinates массив текстурных координат
 * @property normals массив координат нормалей
 * @property vertexIndexes массив индексов для вертексов
 */
class ObjectData(
    val vertexes: FloatArray,
    val textureCoordinates: FloatArray,
    val normals: FloatArray,
    val vertexIndexes: ShortArray
)