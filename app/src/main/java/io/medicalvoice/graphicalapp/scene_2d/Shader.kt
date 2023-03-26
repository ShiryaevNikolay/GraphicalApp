package io.medicalvoice.graphicalapp.scene_2d

import android.opengl.GLES20
import io.medicalvoice.graphicalapp.scene_2d.data.Camera
import io.medicalvoice.graphicalapp.scene_2d.data.Light
import java.nio.FloatBuffer

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
     * Связывает буфер цветов с аттрибутом
     *
     * @param colorBuffer буфер цветов
     * @param attribute название атрибута
     */
    fun linkColorBuffer(colorBuffer: FloatBuffer, attribute: String) {
        linkBuffer(colorBuffer, attribute, 4)
    }

    /**
     * Связывает матрицу модели-вида-проекции с униформой
     *
     * @param matrix матрица модели-вида-проекции
     * @param uniform униформа
     */
    fun linkModelViewProjectionMatrix(matrix: FloatArray, uniform: String) {
        // устанавливаем активную программу
        GLES20.glUseProgram(programId)
        // получаем ссылку на униформу
        val uniformId = GLES20.glGetUniformLocation(programId, uniform)
        // связываем массив с униформой
        GLES20.glUniformMatrix4fv(uniformId, 1, false, matrix, 0)
    }

    /**
     * Связывает координаты камеры с униформой
     *
     * @param camera камера
     * @param uniform униформа
     */
    fun linkCamera(camera: Camera, uniform: String) {
        // устанавливаем активную программу
        GLES20.glUseProgram(programId)
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
    fun linkLightSource(light: Light, uniform: String) {
        // устанавливаем активную программу
        GLES20.glUseProgram(programId)
        // получаем ссылку на униформу
        val uniformId = GLES20.glGetUniformLocation(programId, uniform)
        // связываем координаты источника света с униформой
        GLES20.glUniform3f(uniformId, light.position.x, light.position.y, light.position.z)
    }

    /**
     * Делает шейдерную программу активной
     */
    fun useProgram() {
        GLES20.glUseProgram(programId)
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
    private fun linkBuffer(buffer: FloatBuffer, attribute: String, size: Int) {
        // устанавливается активная программа
        GLES20.glUseProgram(programId)
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
            0, // strideBytes = 7*4
            buffer
        )
    }
}