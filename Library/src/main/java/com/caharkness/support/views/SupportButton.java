package com.caharkness.support.views;

import android.content.Context;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caharkness.support.utilities.SupportColors;
import com.caharkness.support.utilities.SupportMath;

public class SupportButton extends LinearLayout
{
    private Integer foreground_color;

    public Integer getButtonForegroundColor()
    {
        if (foreground_color == null)
            foreground_color = SupportColors.getForegroundColor(getContext());

        return foreground_color;
    }

    public void setButtonForegroundColor(int color)
    {
        foreground_color = color;
    }

	private Integer background_color;

	public Integer getButtonBackgroundColor()
    {
        if (background_color == null)
            background_color = SupportColors.getBackgroundColor(getContext());

        return background_color;
    }

    public void setButtonBackgroundColor(int color)
    {
        background_color = color;
    }

    public SupportButton(Context c)
    {
        super(c);
        addView(getLabel());
        setPadding(
            SupportMath.inches(1 / 8f),
            SupportMath.inches(1 / 16f),
            SupportMath.inches(1 / 8f),
            SupportMath.inches(1 / 16f));
    }

    private TextView label;

    public TextView getLabel()
    {
        if (label == null)
        {
            label = new TextView(getContext());
            label.setTypeface(Typeface.DEFAULT_BOLD);
            label.setAllCaps(true);
        }

        return label;
    }

    public SupportButton setLabel(String text)
    {
        getLabel()
            .setText(text);

        return this;
    }

    public SupportButton setAction(final Runnable runnable)
    {
        if (runnable == null)
        {
            setClickable(false);
            setOnClickListener(null);
            setOnTouchListener(null);
            return this;
        }

        setClickable(true);
        setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                post(runnable);
            }
        });

        setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        setBackgroundColor(
                            SupportColors.subtract(
                                getButtonBackgroundColor(),
                                0x10));
                        return false;

                    default:
                        setBackgroundColor(getButtonBackgroundColor());
                        return false;
                }
            }
        });

        return this;
    }

    public SupportButton setColor(int color)
	{
		setColors(
			//
			//	Determine foreground color to use
			//
			SupportColors.isLight(color)?
				SupportColors.subtract(color, 0x7F) :
				SupportColors.get("white"),

			//
			//	Background color
			//
			color);

		return this;
	}

    public SupportButton setColors(int foreground_color, int background_color)
	{
		setButtonForegroundColor(foreground_color);
		setButtonBackgroundColor(background_color);

		setBackgroundColor(background_color);
		getLabel().setTextColor(getButtonForegroundColor());

		return this;
	}
}
