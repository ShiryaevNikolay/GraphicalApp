package io.medicalvoice.graphicalapp.scene_3d.objects

import android.content.Context
import android.opengl.GLES20
import io.medicalvoice.graphicalapp.R
import io.medicalvoice.graphicalapp.scene_3d.data.Camera
import io.medicalvoice.graphicalapp.scene_3d.data.Coordinates
import io.medicalvoice.graphicalapp.scene_3d.data.TextureData
import io.medicalvoice.graphicalapp.scene_3d.tools.SceneLoader

class Scene(context: Context) {

    private val sceneObjects: List<VisualObject>

    init {
        val textures = listOf(
            R.drawable.tex_ground,
            R.drawable.shingles_1,
            R.drawable.shingles_2,
            R.drawable.shingles_3,
            R.drawable.shingles_4,
            R.drawable.shingles_5,
            R.drawable.shingles_6
        )

        val sceneLoader = SceneLoader(context, "scene.obj")
        val objects = sceneLoader.getSceneObjects()

        sceneObjects = objects.mapIndexed { index, objectData ->
            VisualObject(
                context = context,
                objectData = objectData,
                textureRes = textures[index]
            )
        }
    }

    fun bindViewModelProjectionMatrix(matrix: FloatArray) {
        sceneObjects.forEach { sceneObject ->
            sceneObject.bindMatrix(matrix)
        }
    }

    fun draw(camera: Camera, light: Coordinates) {
        sceneObjects.forEach { sceneObject ->
            sceneObject.draw(camera, light)
        }
    }
}