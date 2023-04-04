package io.medicalvoice.graphicalapp.scene_3d.data

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils
import androidx.annotation.DrawableRes

class Texture(
    context: Context,
    @DrawableRes textureRes: Int
) {

    init {
        // создание объекта текстуры
        val textureIds = IntArray(1)
        GLES20.glGenTextures(1, textureIds, 0)
        if (textureIds[0] == 0) {
            // TODO: прокинуть ошибку или вообще создать статический метод, который будет возвращать 0
        }

        // получение Bitmap
        val bitmapOptions = BitmapFactory.Options().apply {
            inScaled = false
        }
        val bitmap = BitmapFactory.decodeResource(
            context.resources,
            textureRes,
            bitmapOptions
        )
        if (bitmap == null) {
            GLES20.glDeleteTextures(1, textureIds, 0)
            // TODO: прокинуть ошибку или вообще создать статический метод, который будет возвращать 0
        }

        // настройка объекта текстуры
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0])

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

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)

        bitmap.recycle()

        // сброс target
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
    }
}