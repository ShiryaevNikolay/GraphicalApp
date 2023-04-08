package io.medicalvoice.graphicalapp.scene_3d.tools

import android.content.Context
import io.medicalvoice.graphicalapp.scene_3d.data.ObjectData
import io.medicalvoice.graphicalapp.scene_3d.tools.data.MaxIndexes
import java.util.*

class SceneLoader(context: Context, filePath: String) {

    private val scene: MutableList<ObjectData> = mutableListOf()

    init {
        var maxIndexes = MaxIndexes()
        var scanner: Scanner? = null
        kotlin.runCatching {
            scanner = Scanner(context.assets.open(filePath)).apply {
                var currentLine: String? = null
                while (hasNextLine()) {
                    val line = currentLine ?: nextLine()
                    val parts = line.split(" ")
                    if (parts.first() == OBJECT_DATA) {
                        val (stopLine, objectData, indexes) = ObjLoader(
                            name = parts[1],
                            scanner = this,
                            maxIndexes = maxIndexes
                        ).getData()
                        scene.add(objectData)
                        currentLine = stopLine
                        maxIndexes = indexes
                    } else {
                        currentLine = null
                        continue
                    }
                }
            }
        }
        scanner?.close()
    }

    fun getSceneObjects(): List<ObjectData> = scene

    private companion object {
        const val OBJECT_DATA = "o"
    }
}