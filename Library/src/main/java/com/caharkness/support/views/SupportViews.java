package com.caharkness.support.views;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.caharkness.support.R;
import com.caharkness.support.utilities.SupportDrawable;
import com.caharkness.support.utilities.SupportMath;

public class SupportViews
{
    public static RelativeLayout getLoadingView(final Context context, int resource, int color, String message)
    {
        ImageView
            image_view = new ImageView(context);
            image_view.setImageDrawable(
                SupportDrawable.tint(
                    SupportDrawable.fromResource(resource, 0.5f),
                    color));
            image_view.addOnAttachStateChangeListener(
                new View.OnAttachStateChangeListener()
                {
                    @Override
                    public void onViewAttachedToWindow(View view)
                    {
                        view.startAnimation(
                            AnimationUtils.loadAnimation(
                                context,
                                R.anim.rotate_indefinitely));
                    }

                    @Override
                    public void onViewDetachedFromWindow(View view)
                    {
                    }
                });

        TextView
            text_view = new TextView(context);
            text_view.setGravity(Gravity.CENTER);
            text_view.setText(message.toUpperCase());
            text_view.setTextSize(12f);
            text_view.setTextColor(color);
            text_view.setTypeface(Typeface.DEFAULT_BOLD);
            text_view.setLayoutParams(
                new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            text_view.setPadding(
                SupportMath.inches(1 / 16f),
                SupportMath.inches(1 / 16f),
                SupportMath.inches(1 / 16f),
                SupportMath.inches(1 / 16f));

		//
		//
		//

		LinearLayout
            layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setGravity(Gravity.CENTER);
            layout.addView(image_view);
            layout.addView(text_view);
            layout.setLayoutParams(
                new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));

		//
		//
		//

		RelativeLayout
            rel_layout = new RelativeLayout(context);
            rel_layout.setGravity(Gravity.CENTER);
            rel_layout.addView(layout);
            rel_layout.setLayoutParams(
                new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));

        return rel_layout;
    }
}
