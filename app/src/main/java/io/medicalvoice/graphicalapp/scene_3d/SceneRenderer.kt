package io.medicalvoice.graphicalapp.scene_3d

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView.Renderer
import android.opengl.Matrix
import android.os.SystemClock
import io.medicalvoice.graphicalapp.scene_3d.objects.Torus
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class SceneRenderer(private val context: Context) : Renderer {

    private val projectionMatrix = FloatArray(MATRIX_SIZE)
    private val viewMatrix = FloatArray(MATRIX_SIZE)
    private val modelMatrix = FloatArray(MATRIX_SIZE)
    private val matrix = FloatArray(MATRIX_SIZE)

    // Объекты
    private var torus: Torus? = null

    override fun onSurfaceCreated(unused: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        // включаем тест глубины
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        // включаем отсечение невидимых граней
        GLES20.glEnable(GLES20.GL_CULL_FACE)

        createObjects()
        createViewMatrix()
        Matrix.setIdentityM(modelMatrix, 0)
    }

    override fun onSurfaceChanged(unused: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        createProjectionMatrix(width, height)
        bindMatrix()
    }

    override fun onDrawFrame(unused: GL10?) {
        GLES20.glClearColor(0.2f, 0.3f, 0.3f, 1.0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        Matrix.setIdentityM(modelMatrix, 0)

        // вращение
        setModelMatrix()

        torus?.draw()
    }

    private fun createObjects() {
        torus = Torus(context)
    }

    private fun createProjectionMatrix(width: Int, height: Int) {
        val ratio: Float
        var left = -1f
        var right = 1f
        var bottom = -1f
        var top = 1f
        val near = 2f
        val far = 12f
        if (width > height) {
            ratio = (width / height).toFloat()
            left *= ratio
            right *= ratio
        } else {
            ratio = (height / width).toFloat()
            bottom *= ratio
            top *= ratio
        }

        Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far)
    }

    private fun bindMatrix() {
        Matrix.multiplyMM(matrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(matrix, 0, projectionMatrix, 0, matrix, 0)
        torus?.bindMatrix(matrix)
    }

    private fun createViewMatrix() {
        // точка положения камеры
        val eyeX = 0f
        val eyeY = 2f
        val eyeZ = 4f

        // точка направления камеры
        val targetX = 0f
        val targetY = 0f
        val targetZ = 0f

        // up-вектор (angle)
        val upX = 0f
        val upY = 1f
        val upZ = 0f

        Matrix.setLookAtM(
            viewMatrix,
            0,
            eyeX, eyeY, eyeZ,
            targetX, targetY, targetZ,
            upX, upY, upZ
        )
    }

    /**
     * Вращение объекта (не камеры)
     */
    private fun setModelMatrix() {
        val angle = (SystemClock.uptimeMillis() % TIME).toFloat() / TIME * 360
        Matrix.rotateM(modelMatrix, 0, angle, 0f, 1f, 0f)
        bindMatrix()
    }

    private companion object {
        const val MATRIX_SIZE = 4 * 4
        const val TIME = 10000L
    }
}