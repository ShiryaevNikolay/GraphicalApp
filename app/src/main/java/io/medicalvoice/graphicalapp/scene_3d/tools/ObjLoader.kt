package io.medicalvoice.graphicalapp.scene_3d.tools

import io.medicalvoice.graphicalapp.scene_3d.data.ObjectData
import io.medicalvoice.graphicalapp.scene_3d.tools.data.MaxIndexes
import java.util.*

class ObjLoader(
    private val name: String,
    scanner: Scanner,
    maxIndexes: MaxIndexes
) {

    private val vertexes: FloatArray
    private val normals: FloatArray
    private val textureCoordinates: FloatArray

    private val vertexIndexes: ShortArray
    private val normalIndexes: ShortArray
    private val textureIndexes: ShortArray

    private var stopLine: String? = null

    init {
        val vertexes = Vector<Float>()
        val textures = Vector<Float>()
        val normals = Vector<Float>()
        val faces = Vector<String>()

        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            val parts = line.split(" ")

            if (parts.first() == OBJECT_DATA) {
                stopLine = line
                break
            }

            when(parts.first()) {
                VERTEX_DATA -> {
                    vertexes.apply {
                        add(parts[1].toFloat())
                        add(parts[2].toFloat())
                        add(parts[3].toFloat())
                    }
                }
                TEXTURE_DATA -> {
                    textures.apply {
                        add(parts[1].toFloat())
                        add(parts[2].toFloat())
                    }
                }
                NORMAL_DATA -> {
                    normals.apply {
                        add(parts[1].toFloat())
                        add(parts[2].toFloat())
                        add(parts[3].toFloat())
                    }
                }
                FACE_DATA -> {
                    // faces: vertex/texture/normal
                    faces.apply {
                        add(parts[1])
                        add(parts[2])
                        add(parts[3])
                    }
                }
            }
        }

        val numFaces = faces.size

        this.textureCoordinates = FloatArray(numFaces * 2)
        this.vertexes = FloatArray(numFaces * 3)
        this.normals = FloatArray(numFaces * 3)

        this.vertexIndexes = ShortArray(numFaces * 3)
        this.normalIndexes = ShortArray(numFaces * 3)
        this.textureIndexes = ShortArray(numFaces * 2)

        var vertexIndex = 0
        var normalIndex = 0
        var textureIndex = 0

        var vertexIndexIndex = 0 // индекс для vertexIndex
        var normalIndexIndex = 0 // индекс для vertexIndex
        var textureIndexIndex = 0 // индекс для vertexIndex

        faces.forEach { face ->
            val parts = face.split("/").map { faceString -> faceString.toShort() }

            // Заполенение массива индексов для вертексов
            this.vertexIndexes[vertexIndexIndex++] = parts[0]
            this.textureIndexes[textureIndexIndex++] = parts[1]
            this.normalIndexes[normalIndexIndex++] = parts[2]

            // 3 * (i - 1) - формула для вычисления номер позиции первого элемента i-го вертекса.
            // `-1` нужен потому, что индексы в obj файле начинаются с 1
            var index = 3 * (parts[0] - maxIndexes.vertexIndex - 1)

            this.vertexes[vertexIndex++] = vertexes[index++]
            this.vertexes[vertexIndex++] = vertexes[index++]
            this.vertexes[vertexIndex++] = vertexes[index]

            index = 2 * (parts[1] - maxIndexes.textureIndex - 1)
            this.textureCoordinates[normalIndex++] = textures[index++]
            // NOTE: Bitmap gets y-inverted
            this.textureCoordinates[normalIndex++] = 1 - textures[index]

            index = 3 * (parts[2] - maxIndexes.normalIndex - 1)
            this.normals[textureIndex++] = normals[index++]
            this.normals[textureIndex++] = normals[index++]
            this.normals[textureIndex++] = normals[index]
        }
    }

    fun getData(): Triple<String?, ObjectData, MaxIndexes> {
        val maxIndexes = MaxIndexes(
            vertexIndex = vertexIndexes.max(),
            normalIndex = normalIndexes.max(),
            textureIndex = textureIndexes.max()
        )
        return Triple(stopLine, getObjectData(), maxIndexes)
    }

    private fun getObjectData() = ObjectData(
        name = name,
        vertexes = vertexes,
        textureCoordinates = textureCoordinates,
        normals = normals
    )

    private companion object {
        const val OBJECT_DATA = "o"
        const val VERTEX_DATA = "v"
        const val TEXTURE_DATA = "vt"
        const val NORMAL_DATA = "vn"
        const val FACE_DATA = "f"
    }
}