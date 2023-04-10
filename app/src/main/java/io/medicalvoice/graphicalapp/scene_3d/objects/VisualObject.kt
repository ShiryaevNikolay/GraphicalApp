package io.medicalvoice.graphicalapp.scene_3d.objects

import android.content.Context
import androidx.annotation.DrawableRes
import io.medicalvoice.graphicalapp.R
import io.medicalvoice.graphicalapp.scene_3d.Shader
import io.medicalvoice.graphicalapp.scene_3d.data.Camera
import io.medicalvoice.graphicalapp.scene_3d.data.Coordinates
import io.medicalvoice.graphicalapp.scene_3d.data.ObjectData
import io.medicalvoice.graphicalapp.scene_3d.tools.TextureLoader
import io.medicalvoice.graphicalapp.utils.FileUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class VisualObject(
    context: Context,
    objectData: ObjectData,
    @DrawableRes textureRes: Int
) {

    private val vertexesBuffer: FloatBuffer
    private val normalsBuffer: FloatBuffer
    private val texturesBuffer: FloatBuffer

    private val viewModelProjectionMatrixId: Int

    private val shader: Shader

    private val textureId: Int

    init {
        vertexesBuffer = createFloatBuffer(objectData.vertexes)
        normalsBuffer = createFloatBuffer(objectData.normals)
        texturesBuffer = createFloatBuffer(objectData.textureCoordinates)
    }

    init {
        val vertexShader = FileUtils.readTextFromRaw(
            context,
            R.raw.scene_3d_vertex_shader
        )
        val fragmentShader = FileUtils.readTextFromRaw(
            context,
            R.raw.scene_3d_fragment_shader
        )
        shader = Shader(vertexShader, fragmentShader).apply {
            viewModelProjectionMatrixId = getUniformId("matrix")

            linkVertexBuffer(vertexesBuffer, "position")
            linkNormalBuffer(normalsBuffer, "a_normal")
            linkTextureCoordsBuffer(texturesBuffer, "a_texture_coords")

            val textureBitmap = TextureLoader.load(context, textureRes)
            textureId = loadTexture(textureBitmap)
            // bindTexture(textureBitmap, "u_texture")
        }
    }

    fun bindMatrix(matrix: FloatArray) {
        shader.bindUniformMatrix4fv(viewModelProjectionMatrixId, matrix)
    }

    fun draw(camera: Camera, light: Coordinates) = with(shader) {
        linkBuffers()
        linkCamera(camera, "u_camera")
        linkLightSource(light, "u_light")
        bindTexture(textureId, "u_texture")
        drawArrays(vertexesBuffer)
    }

    private fun Shader.linkBuffers() {
        linkVertexBuffer(vertexesBuffer, "position")
        linkNormalBuffer(normalsBuffer, "a_normal")
        linkTextureCoordsBuffer(texturesBuffer, "a_texture_coords")
    }

    private fun createFloatBuffer(array: FloatArray): FloatBuffer {
        return createByteBuffer(array.size * BYTES_PER_FLOAT)
            .asFloatBuffer().apply {
                put(array)
                position(0)
            }
    }

    private fun createByteBuffer(size: Int): ByteBuffer {
        return ByteBuffer.allocateDirect(size)
            .order(ByteOrder.nativeOrder())
    }

    private companion object {
        const val BYTES_PER_FLOAT = Float.SIZE_BYTES // Float занимает 4 байта
        const val BYTES_PER_SHORT = Short.SIZE_BYTES // Short занимает 2 байта
    }
}