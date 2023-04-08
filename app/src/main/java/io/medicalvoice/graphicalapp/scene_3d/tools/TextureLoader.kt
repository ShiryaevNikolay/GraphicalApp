package io.medicalvoice.graphicalapp.scene_3d.tools

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils
import androidx.annotation.DrawableRes

object TextureLoader {

    fun load(
        context: Context,
        @DrawableRes textureRes: Int
    ): Bitmap {
        // получение Bitmap
        val bitmapOptions = BitmapFactory.Options().apply {
            inScaled = false
        }
        return BitmapFactory.decodeResource(
            context.resources,
            textureRes,
            bitmapOptions
        )
    }
}