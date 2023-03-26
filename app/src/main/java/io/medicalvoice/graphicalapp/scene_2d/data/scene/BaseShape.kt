package io.medicalvoice.graphicalapp.scene_2d.data.scene

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

abstract class BaseShape {

    abstract val vertexArray: FloatArray
    abstract val colorArray: FloatArray

    fun getVertexBuffer(): FloatBuffer = createBuffer(vertexArray)

    fun getColorBuffer(): FloatBuffer = createBuffer(colorArray)

    private fun createBuffer(array: FloatArray): FloatBuffer = ByteBuffer
        .allocateDirect(array.size * 4)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer().apply {
            position(0)
            put(array)
            position(0)
        }
}