package io.medicalvoice.graphicalapp.scene_3d.objects

import android.content.Context
import android.opengl.GLES20
import io.medicalvoice.graphicalapp.R
import io.medicalvoice.graphicalapp.scene_3d.Shader
import io.medicalvoice.graphicalapp.scene_3d.data.ObjectData
import io.medicalvoice.graphicalapp.scene_3d.tools.ObjectLoader
import io.medicalvoice.graphicalapp.utils.FileUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Torus(context: Context) : DrawObject, BindObject {

    private val objectData: ObjectData

    private val vertexesBuffer: FloatBuffer
    private val indexesBuffer: ShortBuffer

    private val shader: Shader

    private val matrixId: Int

    init {
        objectData = ObjectLoader(context, "torus.obj").getData()

        vertexesBuffer = ByteBuffer.allocateDirect(objectData.vertexes.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer().apply {
                put(objectData.vertexes)
                position(0)
            }
        indexesBuffer = ByteBuffer.allocateDirect(objectData.vertexIndexes.size * BYTES_PER_SHORT)
            .order(ByteOrder.nativeOrder())
            .asShortBuffer().apply {
                put(objectData.vertexIndexes)
                position(0)
            }
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
            matrixId = getUniformId("matrix")
        }
    }

    override fun draw() {
        // TODO: понять, почему не работает drawElements
        // shader.drawElements(indexesBuffer)
        shader.drawArrays(vertexesBuffer)
    }

    override fun bindMatrix(matrix: FloatArray) {
        shader.bindUniformMatrix4fv(matrixId, matrix)
    }

    private companion object {
        const val BYTES_PER_FLOAT = Float.SIZE_BYTES // Float занимает 4 байта
        const val BYTES_PER_SHORT = Short.SIZE_BYTES // Short занимает 2 байта
    }
}