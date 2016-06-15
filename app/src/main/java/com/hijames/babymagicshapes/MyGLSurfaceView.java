package com.hijames.babymagicshapes;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;
    private final ShapeColors mShapeColors;

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        //Always use super class
        super(context, attrs);

        //
        mShapeColors = new ShapeColors();

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);
        setZOrderOnTop(true);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);

        // Set the Renderer for drawing on the GLSurfaceView
        //set the default shape and color
        String colorName = context.getString(R.string.colorName);
        String shapeName = context.getString(R.string.shapeName);
        mRenderer = new MyGLRenderer(context, shapeName, colorName, R.id.shape_btn_circle, R.id.color_btn_orange);
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    private float mPreviousX;
    private float mPreviousY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        final float TOUCH_SCALE_FACTOR = 180.0f / 560;
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            //for rotating shape
            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                // reverse direction of rotation above the mid-line
                if (y > getHeight() / 2) {
                    dx = dx * -1 ;
                }

                // reverse direction of rotation to left of the mid-line
                if (x < getWidth() / 2) {
                    dy = dy * -1 ;
                }

                //mRenderer.mAngle += (dx + dy) * TOUCH_SCALE_FACTOR;
                mRenderer.increaseAngle((dx + dy) * TOUCH_SCALE_FACTOR);
                requestRender();
        }
        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

    //reset shape color and render again
    public void changeColor(String colorName, int colorID) {
        if (mRenderer.getColorID() != colorID) {
            mRenderer.setColorName(colorName);
            mRenderer.setColorID(colorID);
            requestRender();
        }
    }

    //reset shape and render again, return color ID of the new shape
    public int changeShape(String shapeName, int shapeID) {

        mRenderer.setShapeID(shapeID);
        mRenderer.setShapeName(shapeName);
        mRenderer.setColorID(mShapeColors.getShapeColor(shapeID));
        requestRender();
        return mRenderer.getColorID();
    }

    public String getShapeName() {
        return mRenderer.getShapeName();
    }

    public String getColorName() {
        return mRenderer.getColorName();
    }

    /*
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	int width = View.MeasureSpec.getSize(widthMeasureSpec);
    	int height = View.MeasureSpec.getSize(heightMeasureSpec);
    	this.setMeasuredDimension(width, height);
    }
    */
}

