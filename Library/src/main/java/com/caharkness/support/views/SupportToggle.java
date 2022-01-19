package com.caharkness.support.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.caharkness.support.R;
import com.caharkness.support.utilities.SupportDrawable;
import com.caharkness.support.utilities.SupportMath;
import com.caharkness.support.utilities.SupportColors;

import static android.widget.RelativeLayout.ALIGN_LEFT;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.ALIGN_PARENT_TOP;
import static android.widget.RelativeLayout.CENTER_IN_PARENT;

public class SupportToggle extends LinearLayout
{
    private Context         context;
    private RelativeLayout  layout;
    private LinearLayout    track;
    private ImageView       thumb;
    private boolean         locked;
    private boolean         checked;
    private Runnable        on_change;

    public SupportToggle(Context context)
    {
        super(context);
        this.context = context;

        //
        //
        //

        track = new LinearLayout(context);
        track.setBackgroundDrawable(getTrackBackground());
        track.setMinimumWidth(SupportMath.inches(0.12f));
        track.setMinimumHeight(SupportMath.inches(1 / 12f));
        track.setLayoutParams(
            new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            {{
                addRule(CENTER_IN_PARENT);
            }});

        thumb = new ImageView(context);
        thumb.setImageDrawable(getThumbDrawable());
        thumb.setLayoutParams(
            new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            {{
                addRule(ALIGN_LEFT);
            }});

        //
        //
        //

        layout = new RelativeLayout(context);
        layout.setLayoutParams(
            new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));

        layout.addView(track);
        layout.addView(thumb);

        layout.setMinimumWidth(SupportMath.inches(0.32f));
        layout.setMinimumHeight(SupportMath.inches(0.16f));

        //
        //
        //

        setLayoutParams(
            new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));

        addView(layout);
        setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                toggle();
            }
        });
    }

    private LayerDrawable getThumbDrawable()
    {
        int color = checked?
            SupportColors.getAccentColor(context) :
            SupportColors.getForegroundColor(context);

        Drawable[] layers = new Drawable[]
        {
            SupportDrawable.tint(
                SupportDrawable.fromResourceSmall(R.drawable.ic_brightness_1),
                color)
        };

        return new LayerDrawable(layers);
    }

    private GradientDrawable getTrackBackground()
    {
        int color = checked?
            SupportColors.getAccentColor(context) :
            SupportColors.getForegroundColor(context);

        GradientDrawable
            d = new GradientDrawable();
            d.setCornerRadius(SupportMath.inches(1 / 16f));
            d.setColor(SupportColors.translucent(color));

        return d;
    }

    private float getThumbTranslation()
    {
        return SupportMath.inches(0.16f);
    }

    public void setOnCheckChanged(Runnable runnable)
    {
        on_change = runnable;
    }

    public void setLocked(boolean locked)
    {
        this.locked = locked;
    }

    public boolean getLocked()
    {
        return locked;
    }

    public void setChecked(boolean checked, boolean animate)
    {
        this.checked = checked;

        track.setBackgroundDrawable(getTrackBackground());
        thumb.setImageDrawable(getThumbDrawable());

        if (checked)
        {
            TranslateAnimation a =
                new TranslateAnimation(
                    0,              getThumbTranslation(),
                    thumb.getY(),   thumb.getY());

            a.setFillAfter(true);
            a.setDuration(1);

            if (animate)
                a.setDuration(250);

            thumb.startAnimation(a);
            this.checked = true;
        }
        else
        {
            TranslateAnimation a =
                new TranslateAnimation(
                    getThumbTranslation(),  0,
                    thumb.getY(),           thumb.getY());

            a.setFillAfter(true);
            a.setDuration(1);

            if (animate)
                a.setDuration(250);

            thumb.startAnimation(a);
            this.checked = false;
        }

        if (on_change != null)
            postDelayed(on_change, 250);
    }

    public void setChecked(boolean checked)
    {
        setChecked(checked, false);
    }

    public boolean getChecked()
    {
        return checked;
    }

    public void toggle()
    {
        if (!locked)
        {
            if (checked)
                setChecked(false, true);
            else
                setChecked(true, true);
        }
    }
}
