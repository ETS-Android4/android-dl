package com.caharkness.support.utilities;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.caharkness.support.SupportApplication;

public class SupportView
{
    public static void animate(View view, int resource, final Runnable on_finish)
    {
        Animation a =
            AnimationUtils.loadAnimation(
                SupportApplication.getInstance(),
                resource);

        a.setAnimationListener(
            new Animation.AnimationListener()
            {
                @Override
                public void onAnimationStart(Animation animation)
                {
                    //
                    //
                    //
                }

                @Override
                public void onAnimationEnd(Animation animation)
                {
                    if (on_finish != null)
                        on_finish.run();
                }

                @Override
                public void onAnimationRepeat(Animation animation)
                {
                    //
                    //  Nothing
                    //
                }
            });

        view.startAnimation(a);
    }

    public static void animate(View view, int resource)
    {
        animate(view, resource, null);
    }
}
