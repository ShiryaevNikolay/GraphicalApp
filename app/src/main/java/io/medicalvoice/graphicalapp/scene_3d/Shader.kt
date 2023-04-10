package io.medicalvoice.graphicalapp.scene_3d

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLUtils
import io.medicalvoice.graphicalapp.scene_3d.data.Camera
import io.medicalvoice.graphicalapp.scene_3d.data.Coordinates
import java.nio.Buffer
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Shader(
    private val vertexShader: String,
    private val fragmentShader: String
) {

    private val programId: Int

    init {
        programId = createProgram()
    }

    /**
     * Связывает буфер координат вершин с аттрибутом
     *
     * @param vertexBuffer буфер координат вершин
     * @param attribute название атрибута
     */
    fun linkVertexBuffer(vertexBuffer: FloatBuffer, attribute: String) {
        linkBuffer(vertexBuffer, attribute, 3)
    }

    fun linkTextureCoordsBuffer(
        textureCoordsBuffer: FloatBuffer,
        attribute: String
    ) = withCurrentProgram {
        linkBuffer(textureCoordsBuffer, attribute, 2)
    }

    /**
     * Связывает буфер координат векторов нормалей с аттрибутом
     *
     * @param normalBuffer буфер координат вершин нормалей
     * @param attribute название атрибута
     */
    fun linkNormalBuffer(normalBuffer: FloatBuffer, attribute: String) {
        linkBuffer(normalBuffer, attribute, 3)
    }

    /**
     * Связывает координаты камеры с униформой
     *
     * @param camera камера
     * @param uniform униформа
     */
    fun linkCamera(camera: Camera, uniform: String) = withCurrentProgram {
        // получаем ссылку на униформу
        val uniformId = GLES20.glGetUniformLocation(programId, uniform)
        // связываем координаты камеры с униформой
        GLES20.glUniform3f(uniformId, camera.position.x, camera.position.y, camera.position.z)
    }

    /**
     * Связывает координаты источника света с униформой
     *
     * @param light источник света
     * @param uniform униформа
     */
    fun linkLightSource(light: Coordinates, uniform: String) = withCurrentProgram {
        // получаем ссылку на униформу
        val uniformId = GLES20.glGetUniformLocation(programId, uniform)
        // связываем координаты источника света с униформой
        GLES20.glUniform3f(uniformId, light.x, light.y, light.z)
    }

    fun getUniformId(uniform: String): Int = withCurrentProgram {
        GLES20.glGetUniformLocation(programId, uniform)
    }

    fun bindUniformMatrix4fv(
        objectMatrixId: Int,
        matrix: FloatArray
    ) = withCurrentProgram {
        GLES20.glUniformMatrix4fv(objectMatrixId, 1, false, matrix, 0)
    }

    fun loadTexture(bitmap: Bitmap): Int = withCurrentProgram {
        val textureIds = IntArray(1)
        val textureIndex = 0
        GLES20.glGenTextures(textureIds.size, textureIds, textureIndex) // 0 - индекс в массиве
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[textureIndex])

        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MIN_FILTER,
            GLES20.GL_LINEAR
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MAG_FILTER,
            GLES20.GL_LINEAR
        )
        // GLES20.glTexParameteri(
        //     GLES20.GL_TEXTURE_2D,
        //     GLES20.GL_TEXTURE_WRAP_S,
        //     GLES20.GL_CLAMP_TO_EDGE
        // )
        // GLES20.glTexParameteri(
        //     GLES20.GL_TEXTURE_2D,
        //     GLES20.GL_TEXTURE_WRAP_T,
        //     GLES20.GL_CLAMP_TO_EDGE
        // )

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)

        bitmap.recycle()

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)

        return@withCurrentProgram textureIds[textureIndex]
    }

    fun bindTexture(
        textureIndex: Int,
        uniform: String
    ) = withCurrentProgram {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIndex)

        val uniformId = GLES20.glGetUniformLocation(programId, uniform)
        // юнит текстуры
        GLES20.glUniform1i(uniformId, 0)
    }

    fun drawArrays(vertexes: FloatBuffer) = withCurrentProgram {
        GLES20.glDrawArrays(
            GLES20.GL_TRIANGLES,
            0,
            vertexes.limit() / 3 // Нужно делить на 3, т.к. вертекс состоит из 3 элементов: https://gamedev.stackexchange.com/questions/85924/obj-model-not-being-rendered-properly-in-opengl-es-2-android-app
        )
    }

    /**
     * Создает шейдерную програму, вызывается в конструкторе
     *
     * @return ссылку на шейдерную программу
     */
    private fun createProgram(): Int {
        // ссылка на вершинный шейдер
        val vertexShaderId = createShader(GLES20.GL_VERTEX_SHADER, vertexShader)
        // ссылка на фрагментный шейдер
        val fragmentShaderId = createShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader)

        // ссылка на шейдерную программу
        val program = GLES20.glCreateProgram()
        // присоединение к шейдерной программе вершинный шейдер
        GLES20.glAttachShader(program, vertexShaderId)
        // присоединение к шейдерной программе фрагментный шейдер
        GLES20.glAttachShader(program, fragmentShaderId)
        // компиляция шейдерной программы
        GLES20.glLinkProgram(program)
        GLES20.glUseProgram(program)
        return program
    }

    /**
     * Создается шейдер на основе его кода
     *
     * @param type тип шейдера (вершинный, фрагментный)
     * @param shaderCode код шейдера в виде строки
     *
     * @return ссылку на шейдер
     */
    private fun createShader(type: Int, shaderCode: String): Int {
        // ссылка на шейдер
        val shaderId = GLES20.glCreateShader(type)
        // присоединяет к шейдеру его код
        GLES20.glShaderSource(shaderId, shaderCode)
        // компилируется шейдер
        GLES20.glCompileShader(shaderId)
        return shaderId
    }

    /**
     * Связывает буффер с аттрибутом
     *
     * @param buffer буфер цветов/координат вершин
     * @param attribute аттрибут шейдера
     * @param size количество данных для одного элемента
     */
    private fun linkBuffer(
        buffer: FloatBuffer,
        attribute: String,
        size: Int
    ) = withCurrentProgram {
        // ссылка на аттрибут
        val attributeId = GLES20.glGetAttribLocation(programId, attribute)
        // включается использование аттрибута
        GLES20.glEnableVertexAttribArray(attributeId)
        // связывает буфер с аттрибутом
        GLES20.glVertexAttribPointer(
            attributeId,
            size,
            GLES20.GL_FLOAT,
            false,
            0, // strideBytes = 3 (float) * 4 (bytes) или 0 - плотно упакованы
            buffer
        )
    }

    private fun <T> withCurrentProgram(block: () -> T): T {
        // устанавливается активная программа
        GLES20.glUseProgram(programId)
        return block()
    }
}