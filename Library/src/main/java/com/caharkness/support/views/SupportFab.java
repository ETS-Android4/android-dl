package com.caharkness.support.views;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.caharkness.support.R;
import com.caharkness.support.utilities.SupportDrawable;
import com.caharkness.support.utilities.SupportMath;
import com.caharkness.support.utilities.SupportColors;
import com.caharkness.support.utilities.SupportView;

import java.util.ArrayList;

public class SupportFab extends ImageView
{
    //
    //
    //
    //  Area: Instance
    //
    //
    //

    private Context context;
    private int foreground_color;
    private int background_color;
    private int icon;
    private ArrayList<String> tags;

    public SupportFab(final Context context)
    {
        super(context);
        this.context = context;

        setLayoutParams(
            new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            {{
                //leftMargin      = SupportMath.inches(1 / 8f);
                topMargin       = SupportMath.inches(1 / 8f);
                rightMargin     = SupportMath.inches(1 / 8f);
                bottomMargin    = SupportMath.inches(1 / 8f);
            }});

        //
        //
        //

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
			setElevation(16f);
    }

    //
    //
    //
    //  Area: Setup
    //
    //
    //

    public SupportFab setColors(int foreground_color, int background_color)
    {
        this.foreground_color = foreground_color;
        this.background_color = background_color;
        return this;
    }

    public SupportFab setColor(int color)
    {
        return setColors(
            SupportColors.isLight(color)?
                SupportColors.get("black") :
                SupportColors.get("white"),
            color);
    }

    public SupportFab setIcon(int icon)
    {
        this.icon = icon;

        GradientDrawable
            d = new GradientDrawable();
            d.setColor(background_color);
            d.setCornerRadius(125f);

        setBackgroundDrawable(d);
        setImageDrawable(
            SupportDrawable.tint(
                SupportDrawable.fromResourceBig(icon),
                foreground_color));

        setPadding(
            SupportMath.inches(0.10f),
            SupportMath.inches(0.10f),
            SupportMath.inches(0.10f),
            SupportMath.inches(0.10f));

        bringToFront();
        return this;
    }

    public SupportFab setAction(final Runnable runnable)
    {
        setOnClickListener(
            new OnClickListener()
            {
                @Override
                public void onClick(final View view)
                {
                    SupportView.animate(
                        view,
                        R.anim.bounce_in,
                        new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                view.postDelayed(runnable, 250);
                            }
                        });
                }
            });

        return this;
    }

    //
    //
    //
    //  Area: Category Management
    //
    //
    //

    /**
     *  Get all the applied tags to the fab as an array list.
     */
    public ArrayList<String> getTags()
    {
        if (tags == null)
            tags = new ArrayList<>();

        return tags;
    }

    /**
     *  Add a tag to the fab to catagorize it.
     */
    public SupportFab addTag(String tag)
    {
        getTags().add(tag);
        return this;
    }

    /**
     *  Check to see if the fab has a tag.
     */
    public boolean hasTag(String tag)
    {
        return getTags().contains(tag);
    }

    //
    //
    //
    //  Area: Visibility
    //
    //
    //

    /**
     *  Hide the fab with the option to animate the hide.
     */
    public void hide(boolean animate)
    {
        if (getVisibility() == View.GONE)
            return;

        if (animate)
        {
            SupportView.animate(
                this,
                R.anim.pop_out,
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        setVisibility(View.GONE);
                    }
                });
        }
        else
            setVisibility(View.GONE);
    }

    /**
     *  Hide the fab.
     */
    public void hide()
    {
        hide(false);
    }

    /**
     *  Show the fab with the option to animate the reveal.
     */
    public void show(boolean animate)
    {
        if (getVisibility() == View.VISIBLE)
            return;

        if (animate)
        {
            setVisibility(View.VISIBLE);
            SupportView.animate(this, R.anim.pop_in);
        }
        else
            setVisibility(View.VISIBLE);
    }

    /**
     *  Show the fab.
     */
    public void show()
    {
        show(false);
    }
}
