package io.medicalvoice.graphicalapp.scene_3d.objects

import android.content.Context
import io.medicalvoice.graphicalapp.R
import io.medicalvoice.graphicalapp.scene_3d.data.Camera
import io.medicalvoice.graphicalapp.scene_3d.data.Coordinates
import io.medicalvoice.graphicalapp.scene_3d.tools.SceneLoader

class Scene(context: Context) {

    private val sceneObjects: List<VisualObject>

    init {
        val textures = listOf(
            R.drawable.wallpaper,
            R.drawable.back1,
            R.drawable.top3,
            R.drawable.right5,
            R.drawable.left4,
            R.drawable.texture2,
            R.drawable.shingles_6,
            R.drawable.texture_1
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