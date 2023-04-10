package io.medicalvoice.graphicalapp.scene_2d

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView.Renderer
import android.opengl.Matrix
import io.medicalvoice.graphicalapp.R
import io.medicalvoice.graphicalapp.scene_2d.data.Camera
import io.medicalvoice.graphicalapp.scene_2d.data.Coordinate
import io.medicalvoice.graphicalapp.scene_2d.data.Light
import io.medicalvoice.graphicalapp.scene_2d.data.scene.Ground
import io.medicalvoice.graphicalapp.scene_2d.data.scene.Roof
import io.medicalvoice.graphicalapp.scene_2d.data.scene.Sky
import io.medicalvoice.graphicalapp.scene_2d.data.scene.Wall
import io.medicalvoice.graphicalapp.scene_2d.texture.Texture
import io.medicalvoice.graphicalapp.utils.FileUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class SceneRenderer(private val context: Context) : Renderer {
    /** Координаты камеры */
    private val camera: Camera

    /** Координаты источника света */
    private val light: Light

    private val modelMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val modelViewMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val modelViewProjectionMatrix = FloatArray(16)

    private var normalBuffer: FloatBuffer? = null

    // Объекты
    private val sky: Sky
    private val ground: Ground
    private val wall: Wall
    private val roof: Roof

    // Шейдеры
    private var skyShader: Shader? = null
    private var groundShader: Shader? = null
    private var wallShader: Shader? = null
    private var roofShader: Shader? = null

    // Текстуры
    private var groundTexture: Texture? = null

    init {
        light = Light(
            position = Coordinate(
                x = 0.3f,
                y = 0.2f,
                z = 0.5f
            )
        )
        camera = Camera(
            position = Coordinate(
                x = 0.0f,
                y = 0.0f,
                z = 3.0f
            )
        )

        // мы не будем двигать объекты
        // поэтому сбрасываем модельную матрицу на единичную
        Matrix.setIdentityM(modelMatrix, 0)

        // пусть камера смотрит на начало координат
        // и верх у камеры будет вдоль оси Y
        // зная координаты камеры получаем матрицу вида
        Matrix.setLookAtM(
            viewMatrix,
            0,
            camera.position.x,
            camera.position.y,
            camera.position.z,
            0.0f,
            0.0f,
            0.0f,
            0.0f,
            1.0f,
            0.0f
        )
        // умножая матрицу вида на матрицу модели
        // получаем матрицу модели-вида
        Matrix.multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0)

        sky = Sky()
        ground = Ground()
        wall = Wall()
        roof = Roof()

        createNormalBuffer()
    }

    // метод, который срабатывает при создании экрана
    // здесь мы создаем шейдерный объект
    override fun onSurfaceCreated(glUnused: GL10, config: EGLConfig) {
        // включаем тест глубины
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        // включаем отсечение невидимых граней
        // Если какая-то часть грани находится на пределами экрана,
        // то НЕ будет отображаться вся грань
        // GLES20.glEnable(GLES20.GL_CULL_FACE)
        //включаем сглаживание текстур, это пригодится в будущем
        GLES20.glHint(GLES20.GL_GENERATE_MIPMAP_HINT, GLES20.GL_NICEST)

        //создаем текстурные объекты из картинок
        groundTexture = Texture(context, R.drawable.tex_ground)

        val vertexShaderString = FileUtils.readTextFromRaw(
            context,
            R.raw.scene_2d_vertex_shader
        )
        val fragmentShaderString = FileUtils.readTextFromRaw(
            context,
            R.raw.scene_2d_fragment_shader
        )
        val textureVertexShaderString = FileUtils.readTextFromRaw(
            context,
            R.raw.scene_2d_texture_vertex_shader
        )
        val textureFragmentShaderString = FileUtils.readTextFromRaw(
            context,
            R.raw.scene_2d_texture_fragment_shader
        )
        skyShader = Shader(vertexShaderString, fragmentShaderString).apply {
            normalBuffer?.let { linkNormalBuffer(it, "a_normal") }
            linkVertexBuffer(sky.getVertexBuffer(), "a_vertex")
            linkColorBuffer(sky.getColorBuffer(), "a_color")
        }
        groundShader = Shader(textureVertexShaderString, textureFragmentShaderString).apply {
            normalBuffer?.let { linkNormalBuffer(it, "a_normal") }
            linkVertexBuffer(ground.getVertexBuffer(), "a_vertex")
            linkColorBuffer(ground.getColorBuffer(), "a_color")
            // свяжем текстурные объекты с сэмплерами в фрагментном шейдере
            groundTexture?.let { linkTexture(it, "u_texture") }
        }
        wallShader = Shader(vertexShaderString, fragmentShaderString).apply {
            normalBuffer?.let { linkNormalBuffer(it, "a_normal") }
            linkVertexBuffer(wall.getVertexBuffer(), "a_vertex")
            linkColorBuffer(wall.getColorBuffer(), "a_color")
        }
        roofShader = Shader(vertexShaderString, fragmentShaderString).apply {
            normalBuffer?.let { linkNormalBuffer(it, "a_normal") }
            linkVertexBuffer(roof.getVertexBuffer(), "a_vertex")
            linkColorBuffer(roof.getColorBuffer(), "a_color")
        }
        // TODO: создание других шейдеров
    }

    // метод, который срабатывает при изменении размеров экрана
    // в нем мы получим матрицу проекции и матрицу модели-вида-проекции
    override fun onSurfaceChanged(glUnsed: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        val ratio = width.toFloat() / height.toFloat()
        val k = 0.055f
        val left = -k * ratio
        val right = k * ratio
        val bottom = -k
        val top = k
        val near = 0.1f
        val far = 10.0f
        // получаем матрицу проекции
        Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far)
        // матрица проекции изменилась,
        // поэтому нужно пересчитать матрицу модели-вида-проекции
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0)
    }

    // метод, в котором выполняется рисование кадра
    override fun onDrawFrame(glUnused: GL10) {
        // очищаем кадр
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)

        //передаем в шейдерный объект матрицу модели-вида-проекции
        skyShader?.apply {
            useProgram()
            linkVertexBuffer(sky.getVertexBuffer(), "a_vertex")
            linkColorBuffer(sky.getColorBuffer(), "a_color")
            linkModelViewProjectionMatrix(
                modelViewProjectionMatrix,
                "u_modelViewProjectionMatrix"
            )
            linkCamera(camera, "u_camera")
            linkLightSource(light, "u_lightPosition")
        }
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        //передаем в шейдерный объект матрицу модели-вида-проекции
        groundShader?.apply {
            useProgram()
            linkVertexBuffer(ground.getVertexBuffer(), "a_vertex")
            linkColorBuffer(ground.getColorBuffer(), "a_color")
            linkModelViewProjectionMatrix(
                modelViewProjectionMatrix,
                "u_modelViewProjectionMatrix"
            )
            linkCamera(camera, "u_camera")
            linkLightSource(light, "u_lightPosition")
        }
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        //передаем в шейдерный объект матрицу модели-вида-проекции
        wallShader?.apply {
            useProgram()
            linkVertexBuffer(wall.getVertexBuffer(), "a_vertex")
            linkColorBuffer(wall.getColorBuffer(), "a_color")
            linkModelViewProjectionMatrix(
                modelViewProjectionMatrix,
                "u_modelViewProjectionMatrix"
            )
            linkCamera(camera, "u_camera")
            linkLightSource(light, "u_lightPosition")
        }
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        //передаем в шейдерный объект матрицу модели-вида-проекции
        roofShader?.apply {
            useProgram()
            linkVertexBuffer(roof.getVertexBuffer(), "a_vertex")
            linkColorBuffer(roof.getColorBuffer(), "a_color")
            linkModelViewProjectionMatrix(
                modelViewProjectionMatrix,
                "u_modelViewProjectionMatrix"
            )
            linkCamera(camera, "u_camera")
            linkLightSource(light, "u_lightPosition")
        }
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3)
    }

    private fun createNormalBuffer() {
        // вектор нормали перпендикулярен плоскости квадрата
        // и направлен вдоль оси Z
        val normalCoordinate = Coordinate(
            x = 0.0f,
            y = 0.0f,
            z = 1.0f
        )
        // нормаль одинакова для всех вершин квадрата,
        // поэтому переписываем координаты вектора нормали в массив 4 раза
        val normalArray = floatArrayOf(
            normalCoordinate.x,
            normalCoordinate.y,
            normalCoordinate.z,

            normalCoordinate.x,
            normalCoordinate.y,
            normalCoordinate.z,

            normalCoordinate.x,
            normalCoordinate.y,
            normalCoordinate.z,

            normalCoordinate.x,
            normalCoordinate.y,
            normalCoordinate.z,
        )
        normalBuffer = createBuffer(normalArray)
    }

    private fun createBuffer(array: FloatArray): FloatBuffer {
        return ByteBuffer
            .allocateDirect(array.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer().apply {
                position(0)
                put(array)
                position(0)
            }
    }
}