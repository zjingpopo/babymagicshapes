package com.hijames.babymagicshapes;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";

    private ShapeHolder mShape;
    private final Context mContext;

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjMatrix = new float[16];
    private final float[] mVMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];

    // Declare as volatile because we are updating it from another thread
    private float mAngle;
    private volatile String mColorName;
    private volatile String mShapeName;
    private volatile int mShapeID;
    private volatile int mColorID;

    public MyGLRenderer (final Context c, String shapeName, String colorName, int shapeID, int colorID) {
        mContext = c;
        mShapeName = shapeName;
        mColorName = colorName;
        mShapeID = shapeID;
        mColorID = colorID;
    }

    public void setAngle(float angle) { mAngle = angle; }

    public float getAngle() { return mAngle; }

    public void increaseAngle(float increasement) { mAngle += increasement; }

    public String getColorName() { return mColorName; }

    public void setColorName(String colorName) { mColorName = colorName; }

    public void setShapeName(String shapeName) { mShapeName = shapeName; }

    public String getShapeName() { return mShapeName; }

    public void setShapeID(int shapeID) { mShapeID = shapeID; }

    public int getShapeID() { return mShapeID; }

    public void setColorID(int colorID) { mColorID = colorID; }

    public int getColorID() { return mColorID; }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        //GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);

        // Set the background frame color
        GLES20.glClearColor(0f, 0f, 0f, 0f);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -4, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        mShape = new ShapeHolder(mContext, mShapeID, mColorID);

    }

    @Override
    public void onDrawFrame(GL10 unused) {
        android.util.Log.d("aa", "onDrawFrame");
        // Draw background color and Depth buffer
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);

        //set rotate angle
        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1.0f);

        // Combine the rotation matrix with the projection and camera view
        Matrix.multiplyMM(mMVPMatrix, 0, mRotationMatrix, 0, mMVPMatrix, 0);

        //Create shape object
        if (mShapeID != mShape.getShapeID() || mColorID != mShape.getColorID()) {
            mShape = new ShapeHolder(mContext, mShapeID, mColorID);
        }

        //Draw shape
        mShape.draw(mMVPMatrix);

        //debug
        checkGlError("onDrawFrame");
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    public static int loadShader(int type, String shaderCode){
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     *
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
     *
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation) {
        android.util.Log.d("aa", "checkGlError");
        int error;
        if ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
}
