package io.medicalvoice.graphicalapp.scene_3d.objects

import io.medicalvoice.graphicalapp.scene_3d.data.Camera
import io.medicalvoice.graphicalapp.scene_3d.data.Coordinates

fun interface DrawObject {
    fun draw(camera: Camera, light: Coordinates)
}