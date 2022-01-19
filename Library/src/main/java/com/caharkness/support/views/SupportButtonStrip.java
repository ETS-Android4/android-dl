package com.caharkness.support.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.caharkness.support.layouts.SupportFrameLayout;
import com.caharkness.support.utilities.SupportDrawable;
import com.caharkness.support.utilities.SupportMath;

public class SupportButtonStrip extends SupportFrameLayout
{
    public SupportButtonStrip(Context c)
    {
        super(c, false);

        getRightLayout().setDividerDrawable(
            SupportDrawable.fromBitmap(
                Bitmap.createBitmap(
                    SupportMath.inches(1 / 16f),
                    1,
                    Bitmap.Config.ARGB_8888)));

        //getRightLayout().setDividerDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getRightLayout().setDividerPadding(SupportMath.inches(1 / 16f));
        getRightLayout().setShowDividers(SHOW_DIVIDER_MIDDLE);
    }

    public SupportButtonStrip add(SupportButton... buttons)
    {
        for (SupportButton b : buttons)
            addViewRight(b);

        return this;
    }
}
