package io.medicalvoice.graphicalapp.scene_3d.tools

import android.content.Context
import android.util.Log
import io.medicalvoice.graphicalapp.scene_3d.data.ObjectData
import java.io.IOException
import java.util.*

class ObjectLoader(context: Context, fileName: String) {

    private val vertexes: FloatArray
    private val normals: FloatArray
    private val textureCoordinates: FloatArray
    private val vertexIndexes: ShortArray

    init {
        val vertexes = Vector<Float>()
        val textures = Vector<Float>()
        val normals = Vector<Float>()
        val faces = Vector<String>()

        var scanner: Scanner? = null
        try {
            scanner = Scanner(context.assets.open(fileName))
            while (scanner.hasNextLine()) {
                val line = scanner.nextLine()
                val parts = line.split(" ")
                when(parts.first()) {
                    VERTEX_DATA -> {
                        vertexes.add(parts[1].toFloat())
                        vertexes.add(parts[2].toFloat())
                        vertexes.add(parts[3].toFloat())
                    }
                    TEXTURE_DATA -> {
                        textures.add(parts[1].toFloat())
                        textures.add(parts[2].toFloat())
                    }
                    NORMAL_DATA -> {
                        normals.add(parts[1].toFloat())
                        normals.add(parts[2].toFloat())
                        normals.add(parts[3].toFloat())
                    }
                    FACE_DATA -> {
                        // faces: vertex/texture/normal
                        faces.add(parts[1])
                        faces.add(parts[2])
                        faces.add(parts[3])
                    }
                }
            }
        } catch (error: IOException) {
            // cannot load or read file
        } finally {
            scanner?.let {
                try {
                    it.close()
                } catch (error: IOException) {
                    Log.i("ObjectLoader", error.toString())
                }
            }
        }

        val numFaces = faces.size
        this.textureCoordinates = FloatArray(numFaces * 2)
        this.vertexes = FloatArray(numFaces * 3)
        this.normals = FloatArray(numFaces * 3)
        this.vertexIndexes = ShortArray(numFaces * 3)

        var vertexIndex = 0
        var normalIndex = 0
        var textureIndex = 0
        var vertexIndexIndex = 0 // индекс для vertexIndex

        faces.forEach { face ->
            val parts = face.split("/")

            // Заполенение массива индексов для вертексов
            this.vertexIndexes[vertexIndexIndex++] = (parts[0].toShort() - 1).toShort()

            // 3 * (i - 1) - формула для вычисления номер позиции первого элемента i-го вертекса.
            // `-1` нужен потому, что индексы в obj файле начинаются с 1
            var index = 3 * (parts[0].toShort() - 1)

            this.vertexes[vertexIndex++] = vertexes[index++]
            this.vertexes[vertexIndex++] = vertexes[index++]
            this.vertexes[vertexIndex++] = vertexes[index]

            index = 2 * (parts[1].toShort() - 1)
            this.textureCoordinates[normalIndex++] = textures[index++]
            // NOTE: Bitmap gets y-inverted
            this.textureCoordinates[normalIndex++] = 1 - textures[index]

            index = 3 * (parts[2].toShort() - 1)
            this.normals[textureIndex++] = normals[index++]
            this.normals[textureIndex++] = normals[index++]
            this.normals[textureIndex++] = normals[index]
        }
    }

    fun getData() = ObjectData(
        vertexes = vertexes,
        textureCoordinates = textureCoordinates,
        normals = normals,
        vertexIndexes = vertexIndexes
    )

    private companion object {
        const val VERTEX_DATA = "v"
        const val TEXTURE_DATA = "vt"
        const val NORMAL_DATA = "vn"
        const val FACE_DATA = "f"
    }
}