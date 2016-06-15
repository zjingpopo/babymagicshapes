package com.hijames.babymagicshapes;

import java.util.HashMap;

public class ShapeColors {
    private HashMap<Integer, Integer> mShapeColor = new HashMap<>();

    public ShapeColors() {
        //shape button color
        mShapeColor.put(R.id.shape_btn_circle, R.id.color_btn_orange);
        mShapeColor.put(R.id.shape_btn_ring, R.id.color_btn_black);
        mShapeColor.put(R.id.shape_btn_triangle, R.id.color_btn_pink);
        mShapeColor.put(R.id.shape_btn_moon, R.id.color_btn_yellow);
        mShapeColor.put(R.id.shape_btn_square, R.id.color_btn_skyblue);
        mShapeColor.put(R.id.shape_btn_diamond, R.id.color_btn_purple);
        mShapeColor.put(R.id.shape_btn_rectangle, R.id.color_btn_green);
        mShapeColor.put(R.id.shape_btn_heart, R.id.color_btn_red);
        mShapeColor.put(R.id.shape_btn_oval, R.id.color_btn_white);
        mShapeColor.put(R.id.shape_btn_star, R.id.color_btn_blue);
    }

    public int getShapeColor(int shapeID) {
        if(mShapeColor.containsKey(shapeID)) {
            return mShapeColor.get(shapeID);
        } else {
            return R.id.color_btn_orange;
        }
    }
}
