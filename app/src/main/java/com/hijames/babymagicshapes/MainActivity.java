package com.hijames.babymagicshapes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Color;

public class MainActivity extends AppCompatActivity {
    private MyGLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGLView = (MyGLSurfaceView) findViewById(R.id.glsurfaceview);
        //set color and shape names
        TextView textView = (TextView) findViewById(R.id.colorName);
        if (textView != null) {
            textView.setText(mGLView.getColorName());
            textView.setTextColor(Color.GRAY);
        }

        textView = (TextView) findViewById(R.id.shapeName);
        if (textView != null) {
            textView.setText(mGLView.getShapeName());
            textView.setTextColor(Color.GRAY);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }
    //will be called when touch color buttons
    public void changeColor(View v) {
        int curColorID = v.getId();
        String nColorName = (String) v.getTag();

        //change the text of the TextView for colors
        TextView textView = (TextView) findViewById(R.id.colorName);
        if (textView != null) {
            textView.setText(nColorName);
            textView.setTextColor(Color.GRAY);
        }

        //change the color of the shape
        mGLView.changeColor(nColorName, curColorID);

        //log
        //this.logging("\ncolor: " + colorName);
    }


    //will be called when touch shape buttons
    public void changeShape(View v) {
        int shapeID = v.getId();

        //set shape name
        String nShapeName = (String) v.getTag();
        //change the text of the TextView for shapes
        TextView textView = (TextView) findViewById(R.id.shapeName);
        if (textView != null) {
            textView.setText(nShapeName);
            textView.setTextColor(Color.GRAY);
        }

        //change the current shape showing on the screen
        int newColorID =  mGLView.changeShape(nShapeName, shapeID);

        //change the text of the TextView for colors
        textView = (TextView) findViewById(R.id.colorName);
        Button colorBtn = (Button) findViewById(newColorID);
        if (colorBtn != null && textView != null) {
            textView.setText((CharSequence) colorBtn.getTag());
            textView.setTextColor(Color.GRAY);
        }
    }
}
