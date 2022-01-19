package com.caharkness.support.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class SupportLinearLayout extends LinearLayout
{

    public SupportLinearLayout(Context context)
    {
        super(context);
    }

    public SupportLinearLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public float getXFraction()
    {
        final int width = getWidth();
        if (width != 0) return getX() / getWidth();
        else return getX();
    }

    public void setXFraction(float xFraction)
    {
        final int width = getWidth();
        float newWidth = (width > 0) ? (xFraction * width) : -9999;
        setX(newWidth);
    }
}