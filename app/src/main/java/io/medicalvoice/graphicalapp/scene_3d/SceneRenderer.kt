package io.medicalvoice.graphicalapp.scene_3d

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView.Renderer
import android.opengl.Matrix
import android.os.SystemClock
import io.medicalvoice.graphicalapp.scene_3d.data.Angle
import io.medicalvoice.graphicalapp.scene_3d.data.Camera
import io.medicalvoice.graphicalapp.scene_3d.data.Coordinates
import io.medicalvoice.graphicalapp.scene_3d.objects.Scene
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class SceneRenderer(private val context: Context) : Renderer {

    // @Volatile
    // private var angle: Float = 0.0f

    private val projectionMatrix = FloatArray(MATRIX_SIZE)
    private val viewModelProjectionMatrix = FloatArray(MATRIX_SIZE)
    private val viewMatrix = FloatArray(MATRIX_SIZE)
    private val modelMatrix = FloatArray(MATRIX_SIZE)
    private val viewModelMatrix = FloatArray(MATRIX_SIZE)

    private val camera = Camera(
        position = Coordinates(0.0f, 1.0f, 4.0f),
        targetLook = Coordinates(0.0f, 1.0f, 0.0f),
        rotate = Angle(0.0f, 1.0f, 0.0f)
    )
    private val light = Coordinates(1.0f, 1.0f, 1.0f)

    private var scene: Scene? = null

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
        // setModelMatrix()

        scene?.draw(camera, light)
    }

    // fun rotate(angle: Float) {
    //     this.angle += angle
    // }

    private fun createObjects() {
        scene = Scene(context)
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
        Matrix.multiplyMM(viewModelMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(viewModelProjectionMatrix, 0, projectionMatrix, 0, viewModelMatrix, 0)
        scene?.bindViewModelProjectionMatrix(viewModelProjectionMatrix)
    }

    private fun createViewMatrix() {
        with(camera) {
            Matrix.setLookAtM(
                viewMatrix,
                0,
                position.x, position.y, position.z,
                targetLook.x, targetLook.y, targetLook.z,
                rotate.x, rotate.y, rotate.z
            )
        }
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