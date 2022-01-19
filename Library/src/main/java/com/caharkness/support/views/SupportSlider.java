package com.caharkness.support.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.caharkness.support.R;
import com.caharkness.support.SupportApplication;
import com.caharkness.support.utilities.SupportDrawable;
import com.caharkness.support.utilities.SupportMath;
import com.caharkness.support.utilities.SupportColors;

import static android.widget.RelativeLayout.ALIGN_LEFT;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.CENTER_IN_PARENT;
import static android.widget.RelativeLayout.CENTER_VERTICAL;

public class SupportSlider extends LinearLayout
{
    final int track_margin = SupportMath.inches(2 / 25f);

    private RelativeLayout layout;

    public RelativeLayout getLayout()
    {
        if (layout == null)
        {
            layout = new RelativeLayout(getContext());
            layout.setLayoutParams(
                new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));

            layout.addView(getTrack());
            layout.addView(getTrackFill());
            layout.addView(getThumb());
            layout.setMinimumWidth(SupportMath.inches(0.32f));
            layout.setMinimumHeight(SupportMath.inches(0.16f));
        }

        return layout;
    }

    private LinearLayout track;

    public LinearLayout getTrack()
    {
        if (track == null)
        {
            track = new LinearLayout(getContext());
            track.setBackgroundDrawable(getTrackBackground());
            track.setMinimumWidth(SupportMath.inches(2f));
            track.setMinimumHeight(SupportMath.inches(1 / 32f));
            track.setLayoutParams(
                new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
                {{
                    addRule(CENTER_IN_PARENT);
                    setMargins(
                        track_margin, 0,
                        track_margin, 0);
                }});
        }

        return track;
    }

    private LinearLayout track_fill;

    public LinearLayout getTrackFill()
    {
        if (track_fill == null)
        {
            track_fill = new LinearLayout(getContext());
            track_fill.setBackgroundDrawable(getTrackFillBackground());
            track_fill.setMinimumWidth(0);
            track_fill.setMinimumHeight(SupportMath.inches(1 / 32f));
            track_fill.setLayoutParams(
                new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
                {{
                    addRule(CENTER_VERTICAL);
                    addRule(ALIGN_PARENT_LEFT);
                    setMargins(
                        track_margin, 0,
                        track_margin, 0);
                }});
        }

        return track_fill;
    }

    private ImageView thumb;

    public ImageView getThumb()
    {
        if (thumb == null)
        {
            thumb = new ImageView(getContext());
            thumb.setImageDrawable(getThumbDrawable());
            thumb.setLayoutParams(
                new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
                {{
                    addRule(ALIGN_LEFT);
                }});

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                thumb.setElevation(8f);
            }
        }

        return thumb;
    }

    public SupportSlider(Context context, final String preference, final Runnable runnable)
    {
        super(context);

        setLayoutParams(
            new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));

        addView(getLayout());
        setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent event)
            {
                float x = event.getX();

                int xmin = 0;
                int xmax = getTrack().getWidth() - (track_margin / 2);

                float                   target_x = x - getThumb().getWidth() / 2;
                if (target_x < xmin)    target_x = xmin;
                if (target_x > xmax)    target_x = xmax;

                getThumb()
                    .setTranslationX(target_x);

                float           perc = target_x / xmax;
                if (perc < 0)   perc = 0f;
                if (perc > 1)   perc = 1f;

                SupportApplication.setFloat(
                    preference,
                    perc);

                getTrackFill()
                    .setMinimumWidth(Math.round(target_x));

                if (runnable != null)
                    runnable.run();

                return true;
            }
        });

        addOnLayoutChangeListener(new OnLayoutChangeListener()
        {
            @Override
            public void onLayoutChange(
                View view,
                int left,       int top,        int right,      int bottom,
                int old_left,   int old_top,    int old_right,  int old_bottom)
            {
                float val   = SupportApplication.getFloat(preference, 0f);
                int xmax    = (getTrack().getWidth() - (track_margin / 2));

                getThumb().setTranslationX(val * xmax);
                getTrackFill().setMinimumWidth(Math.round(val * xmax));
            }
        });
    }

    private LayerDrawable getThumbDrawable()
    {
        int color = SupportColors.getAccentColor(getContext());

        Drawable[] layers = new Drawable[]
        {
            SupportDrawable.tint(
                SupportDrawable.fromResourceSmall(R.drawable.ic_brightness_1),
                SupportColors.opaque(color))
        };

        return
        new LayerDrawable(layers);
    }

    private GradientDrawable getTrackBackground()
    {
        int color = SupportColors.getForegroundColor(getContext());

        GradientDrawable
            d = new GradientDrawable();
            d.setCornerRadius(SupportMath.inches(1 / 16f));
            d.setColor(SupportColors.translucent(color));

        return d;
    }

    private GradientDrawable getTrackFillBackground()
    {
        int color = SupportColors.getAccentColor(getContext());

        GradientDrawable
            d = new GradientDrawable();
            d.setCornerRadius(SupportMath.inches(1 / 16f));
            d.setColor(SupportColors.subtract(color, 0x0F));

        return d;
    }
}
