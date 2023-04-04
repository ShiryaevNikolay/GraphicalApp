package io.medicalvoice.graphicalapp.scene_3d.objects

import android.content.Context
import android.opengl.GLES20
import io.medicalvoice.graphicalapp.R
import io.medicalvoice.graphicalapp.scene_3d.Shader
import io.medicalvoice.graphicalapp.scene_3d.data.Camera
import io.medicalvoice.graphicalapp.scene_3d.data.Coordinates
import io.medicalvoice.graphicalapp.scene_3d.data.ObjectData
import io.medicalvoice.graphicalapp.scene_3d.tools.ObjectLoader
import io.medicalvoice.graphicalapp.scene_3d.tools.TextureLoader
import io.medicalvoice.graphicalapp.utils.FileUtils
import java.nio.*

class Torus(context: Context) : DrawObject, BindObject {

    private val objectData: ObjectData

    private val vertexesBuffer: FloatBuffer
    private val normalsBuffer: FloatBuffer
    private val texturesBuffer: FloatBuffer
    private val indexesBuffer: ShortBuffer

    private val shader: Shader

    private val matrixId: Int
    private val textureId: Int

    init {
        objectData = ObjectLoader(context, "torus.obj").getData()

        vertexesBuffer = createFloatBuffer(objectData.vertexes)
        normalsBuffer = createFloatBuffer(objectData.normals)
        texturesBuffer = createFloatBuffer(objectData.textureCoordinates)
        indexesBuffer = createShortBuffer(objectData.vertexIndexes)

        textureId = TextureLoader.load(context, R.drawable.brick_wall)
    }

    /**
     * Инициализирует шейдер
     */
    init {
        val vertexShader = FileUtils.readTextFromRaw(
            context,
            R.raw.scene_3d_vertex_shader
        )
        val fragmentShader = FileUtils.readTextFromRaw(
            context,
            R.raw.scene_3d_fragment_shader
        )
        shader = Shader(
            vertexShader = vertexShader,
            fragmentShader = fragmentShader
        ).apply {
            linkVertexBuffer(vertexesBuffer, "position")
            linkNormalBuffer(normalsBuffer, "a_normal")
            linkTextureCoordsBuffer(texturesBuffer, "a_texture_coords")
            bindTexture(textureId, "u_texture")
            matrixId = getUniformId("matrix")
        }
    }

    override fun draw(camera: Camera, light: Coordinates) = with(shader) {
        // TODO: понять, почему не работает drawElements
        // drawElements(indexesBuffer)
        drawArrays(vertexesBuffer)
        linkCamera(camera, "u_camera")
        linkLightSource(light, "u_light")
    }

    override fun bindMatrix(matrix: FloatArray) {
        shader.bindUniformMatrix4fv(matrixId, matrix)
    }

    private fun createFloatBuffer(array: FloatArray): FloatBuffer {
        return createByteBuffer(array.size * BYTES_PER_FLOAT)
            .asFloatBuffer().apply {
                put(array)
                position(0)
            }
    }

    private fun createShortBuffer(array: ShortArray): ShortBuffer {
        return createByteBuffer(array.size * BYTES_PER_SHORT)
            .asShortBuffer().apply {
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