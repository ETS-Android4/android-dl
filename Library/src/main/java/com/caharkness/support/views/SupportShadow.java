package com.caharkness.support.views;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.caharkness.support.utilities.SupportMath;

public class SupportShadow
{
    public static enum ShadowDirection
    {
        LEFT,
        UP,
        ABOVE,
        RIGHT,
        DOWN,
        BELOW
    }

    public static void addTo(
        ViewGroup container,
        final View view,
        final ShadowDirection direction,
        float elevation,
        String tag)
    {
        View existing;
		if ((existing = container.findViewWithTag(tag)) != null)
			container.removeView(existing);

		//
        //
        //

        int w = ViewGroup.LayoutParams.WRAP_CONTENT;
		int h = ViewGroup.LayoutParams.WRAP_CONTENT;
        Bitmap b = null;

        switch (direction)
        {
            case LEFT:
                h = ViewGroup.LayoutParams.MATCH_PARENT;
                b = Bitmap.createBitmap(2, 1, Bitmap.Config.ARGB_8888);
                b.setPixel(0, 0, 0x00000000);
		        b.setPixel(1, 0, 0x2F000000);
                break;

            case UP:
            case ABOVE:
                w = ViewGroup.LayoutParams.MATCH_PARENT;
                b = Bitmap.createBitmap(1, 2, Bitmap.Config.ARGB_8888);
                b.setPixel(0, 0, 0x00000000);
		        b.setPixel(0, 1, 0x2F000000);
                break;

            case RIGHT:
                h = ViewGroup.LayoutParams.MATCH_PARENT;
                b = Bitmap.createBitmap(2, 1, Bitmap.Config.ARGB_8888);
                b.setPixel(0, 0, 0x2F000000);
		        b.setPixel(1, 0, 0x00000000);
                break;

            case DOWN:
            case BELOW:
                w = ViewGroup.LayoutParams.MATCH_PARENT;
                b = Bitmap.createBitmap(1, 2, Bitmap.Config.ARGB_8888);
                b.setPixel(0, 0, 0x2F000000);
		        b.setPixel(0, 1, 0x00000000);
                break;
        }

		ImageView
            v = new ImageView(container.getContext());
            v.setScaleType(ImageView.ScaleType.FIT_XY);
            v.setMinimumWidth(SupportMath.inches(elevation));
            v.setMinimumHeight(SupportMath.inches(elevation));
            v.setImageBitmap(b);
            v.setTag(tag);
            v.setLayoutParams(new ViewGroup.LayoutParams(w, h));

        if (view != null)
        if (container instanceof RelativeLayout)
        {
            v.setLayoutParams(
                new RelativeLayout.LayoutParams(w, h)
                {{
                    switch (direction)
                    {
                        case LEFT:
                            addRule(RelativeLayout.LEFT_OF, view.getId());
                            break;

                        case DOWN:
                            addRule(RelativeLayout.ALIGN_PARENT_TOP);
                            break;

                        case ABOVE:
                            addRule(RelativeLayout.ABOVE, view.getId());
                            break;

                        case RIGHT:
                            addRule(RelativeLayout.RIGHT_OF, view.getId());
                            break;

                        case UP:
                            addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                            break;

                        case BELOW:
                            addRule(RelativeLayout.BELOW, view.getId());
                            break;
                    }
                }});
        }

		//
        //
        //

        container.addView(v);
    }
}
