package com.hijames.babymagicshapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.content.Context;
import android.opengl.GLES20;

public class ShapeHolder {
    private final int mColorID;
    private final int mShapeID;
    private static final AllShapes mAllShapes = new AllShapes();

    private static final String mVertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 a_Position;" +

                    //for texture
                    "attribute vec2 a_TexCoordinate;" + // Per-vertex texture coordinate information we will pass in.
                    "varying vec2 v_TexCoordinate;" +  // This will be passed into the fragment shader.

                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    //"  gl_Position = vPosition * uMVPMatrix;" +
                    "  gl_Position = a_Position * uMVPMatrix;" +

                    //Pass through the texture coordinate.
                    "v_TexCoordinate = a_TexCoordinate;" +

                    "}";

    private static final String mFragmentShaderCode =
            "precision mediump float;" +
                    "uniform sampler2D u_Texture;" +  // The input texture.
                    "varying vec2 v_TexCoordinate;" + // Interpolated texture coordinate per fragment.
                    "void main() {" +
                    "  gl_FragColor = texture2D(u_Texture, v_TexCoordinate);" +
                    "}";

    private final FloatBuffer mVertexBuffer;
    private final FloatBuffer mTextBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mTextureHandle;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    private static final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    private static final float squareCoords[] = {
            0.5f, -0.5f, 0.0f,
            0.5f, 0.5f, 0.0f,
            -0.5f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
    };

    private static final float textureCoords[] = {
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f
    };

    public int getShapeID() {
        return mShapeID;
    }

    public int getColorID() {
        return mColorID;
    }

    public ShapeHolder(Context context, int nshapeID, int ncolorID) {
        //choose a texture
        mShapeID = nshapeID;
        mColorID = ncolorID;
        int textureID = mAllShapes.getColor(mShapeID, mColorID);

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        mVertexBuffer = bb.asFloatBuffer();
        mVertexBuffer.put(squareCoords);
        mVertexBuffer.position(0);

        // initialize texture byte buffer for texture coordinates
        bb = ByteBuffer.allocateDirect(textureCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        mTextBuffer = bb.asFloatBuffer();
        mTextBuffer.put(textureCoords);
        mTextBuffer.position(0);

        // prepare shaders and OpenGL program
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                mVertexShaderCode);

        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                mFragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables

        mTextureHandle = TextureLoader.loadGLTexture(context, textureID);
    }

    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // Enable blending using premultiplied alpha.
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        // Prepare the square coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, mVertexBuffer);

        // Enable a handle to the square vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Apply texture
        int textureUniformHandle = GLES20.glGetUniformLocation(mProgram, "u_Texture");
        int textureCoordinateHandle = GLES20.glGetAttribLocation(mProgram, "a_TexCoordinate");
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureHandle);
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        android.util.Log.d("aa","textureUniformHandle = " + String.valueOf(textureUniformHandle));
        GLES20.glUniform1i(textureUniformHandle, 0);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "a_Position");
        android.util.Log.d("aa", "textureCoordinateHandle = " + String.valueOf(textureCoordinateHandle));

        GLES20.glEnableVertexAttribArray(textureCoordinateHandle);

        // Pass in the texture coordinate information
        GLES20.glVertexAttribPointer(textureCoordinateHandle, 2,
                GLES20.GL_FLOAT, false,
                0, mTextBuffer);

        // get handle to shape's transformation matrix
        int MVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGLRenderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(MVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");

        // Draw the square
        //GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
        MyGLRenderer.checkGlError("glDrawElements");

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
