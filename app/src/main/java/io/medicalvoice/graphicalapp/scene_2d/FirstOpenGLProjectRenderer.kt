package io.medicalvoice.graphicalapp.scene_2d

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import io.medicalvoice.graphicalapp.R
import io.medicalvoice.graphicalapp.utils.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class FirstOpenGLProjectRenderer(private val context: Context) : GLSurfaceView.Renderer {
    private val mModelMatrix = FloatArray(16)
    private val mMVPMatrix = FloatArray(16)

    /** Store our model data in a float buffer.  */
    private val mTriangle1Vertices: FloatBuffer
    private var mColorHandle = 0

    /** How many bytes per float.  */
    private val mBytesPerFloat = 4
    private var mPositionHandle = 0

    /** How many elements per vertex.  */
    private val mStrideBytes = 7 * mBytesPerFloat

    /** Offset of the position data.  */
    private val mPositionOffset = 0

    /** Size of the position data in elements.  */
    private val mPositionDataSize = 3

    /** Offset of the color data.  */
    private val mColorOffset = 3

    /** Size of the color data in elements.  */
    private val mColorDataSize = 4
    private val x = 0f

    init {
        // Define points for equilateral triangles.

        // This triangle is white_blue.First sail is mainsail
        val triangle1VerticesData = floatArrayOf( // X, Y, Z,
            // R, G, B, A
            -0.5f, -0.25f, 0.0f,
            1.0f, 1.0f, 0.65f, 1.0f,

            0.0f, -0.5f, 0.0f,
            0.8f, 0.13f, 1.0f, 1.0f,

            0.3f, 0.56f, 0.0f,
            0.1f, 0.6f, 0.31f, 1.0f,
        )
        mTriangle1Vertices = ByteBuffer.allocateDirect(triangle1VerticesData.size * mBytesPerFloat)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
        mTriangle1Vertices.put(triangle1VerticesData).position(0)
    }

    override fun onSurfaceCreated(glUnused: GL10, config: EGLConfig) {
        GLES20.glClearColor(0.5f, 0.0f, 0.5f, 1.0f)
        val vertexShader = ShaderUtils.createShader(
            context,
            GLES20.GL_VERTEX_SHADER,
            R.raw.scene_2d_vertex_shader
        ) // normalized screen coordinates.
        val fragmentShader = ShaderUtils.createShader(
            context,
            GLES20.GL_FRAGMENT_SHADER,
            R.raw.scene_2d_fragment_shader
        )

        // Create a program object and store the handle to it.
        var programHandle = GLES20.glCreateProgram()
        if (programHandle != 0) {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programHandle, vertexShader)

            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programHandle, fragmentShader)

            // Bind attributes
            GLES20.glBindAttribLocation(programHandle, 0, "a_Position")
            GLES20.glBindAttribLocation(programHandle, 1, "a_Color")

            // Link the two shaders together into a program.
            GLES20.glLinkProgram(programHandle)

            // Get the link status.
            val linkStatus = IntArray(1)
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0)

            // If the link failed, delete the program.
            if (linkStatus[0] == 0) {
                GLES20.glDeleteProgram(programHandle)
                programHandle = 0
            }
        }
        if (programHandle == 0) {
            throw RuntimeException("Error creating program.")
        }
        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position")
        mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color")

        // Tell OpenGL to use this program when rendering.
        GLES20.glUseProgram(programHandle)
    }

    // Set the OpenGL viewport to fill the entire surface.
    override fun onSurfaceChanged(glUnused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    // Clear the rendering surface.
    override fun onDrawFrame(glUnused: GL10) {
        //   glClear(GL_COLOR_BUFFER_BIT);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)
        // Draw the triangle facing straight on.
        //Matrix.setIdentityM(mModelMatrix, 0);
        // Matrix.translateM(mModelMatrix, 0, x, 0.0f, 0.0f);
        drawTriangle(mTriangle1Vertices)
    }

    /**
     * Draws a triangle from the given vertex data.
     *
     * @param aTriangleBuffer The buffer containing the vertex data.
     */
    private fun drawTriangle(aTriangleBuffer: FloatBuffer) {
        // Pass in the position information
        aTriangleBuffer.position(mPositionOffset)
        GLES20.glVertexAttribPointer(
            mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
            mStrideBytes, aTriangleBuffer
        )
        GLES20.glEnableVertexAttribArray(mPositionHandle)

        // Pass in the color information
        aTriangleBuffer.position(mColorOffset)
        GLES20.glVertexAttribPointer(
            mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,
            mStrideBytes, aTriangleBuffer
        )
        GLES20.glEnableVertexAttribArray(mColorHandle)

        /* This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
            // (which currently contains model * view).
            Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
            // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
            // (which now contains model * view * projection).
            Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0); */

        // GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3)
    }
}