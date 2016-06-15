package com.hijames.babymagicshapes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class TextureLoader {
    public static int loadGLTexture(final Context context, final int resourceID) {
        final int[] textureHandle = new int[1];

        //generate one texture pointer
        GLES20.glGenTextures(1, textureHandle, 0);

        BitmapFactory.Options bo = new BitmapFactory.Options();
        bo.inScaled = false;
        //set Bitmap.Config to ARGB_8888 for transparent images
        bo.inPreferredConfig = Bitmap.Config.ARGB_8888;

        //loading texture
        android.util.Log.d("aa", String.valueOf(resourceID));
        android.util.Log.d("aa", String.valueOf(R.mipmap.square_skyblue));
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceID, bo);
        //bind it to array
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
        //Set filtering to nearest
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        //Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        //clean up
        bitmap.recycle();
        //error check
        if (textureHandle[0] == 0)
        {
            throw new RuntimeException("Error loading texture.");
        }
        return textureHandle[0];
    }
}
