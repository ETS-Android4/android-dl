package com.caharkness.support.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caharkness.support.utilities.SupportMath;
import com.caharkness.support.utilities.SupportColors;

import java.util.ArrayList;

public class SupportTabGroup extends LinearLayout
{
	private Context context;

	int fg;
	int bg;
	int color_state_up;
	int color_state_down;

	public SupportTabGroup(Context context)
	{
		super(context);
		this.context = context;

		fg = SupportColors.getForegroundColor(context);
		bg = SupportColors.getBackgroundColor(context);

		if (SupportColors.isLight(bg))
		{
			color_state_up		= SupportColors.subtract(bg, 0x0F);
			color_state_down	= SupportColors.subtract(bg, 0x1F);
		}
		else
		{
			color_state_up		= SupportColors.add(bg, 0x1F);
			color_state_down	= SupportColors.add(bg, 0x0F);
		}

		setLayoutParams(
			new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
	}

	public ArrayList<TextView> getTabs()
	{
		ArrayList<TextView> text_views = new ArrayList<>();

		for (int i = 0; i < getChildCount(); i++)
			if (getChildAt(i) instanceof TextView)
				text_views.add((TextView) getChildAt(i));

		return text_views;
	}

	public void resetTabs()
	{
		for (TextView view : getTabs())
			view.setBackgroundColor(color_state_up);
	}

	@SuppressLint("ClickableViewAccessibility")
    public SupportTabGroup addTab(String name, final Runnable runnable, final boolean stay)
	{
		final TextView tab = new TextView(context);

		tab.setText(name.toUpperCase());
		tab.setTextColor(fg);
		tab.setBackgroundColor(color_state_up);
		tab.setTextSize(10f);
		tab.setTypeface(Typeface.DEFAULT_BOLD);
		tab.setPadding(
			SupportMath.inches(1 / 16f),
			SupportMath.inches(1 / 16f),
			SupportMath.inches(1 / 16f),
			SupportMath.inches(1 / 16f));

		tab.setOnClickListener(
			new OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					if (runnable != null)
						postDelayed(runnable, 250);
				}
			});

		tab.setOnTouchListener(
			new OnTouchListener()
			{
				@Override
				public boolean onTouch(View view, MotionEvent event)
				{
					switch (event.getAction())
					{
						case MotionEvent.ACTION_DOWN:
						case MotionEvent.ACTION_MOVE:
							resetTabs();
							tab.setBackgroundColor(color_state_down);
							return false;

						case MotionEvent.ACTION_UP:
							if (!stay)
								resetTabs();
							return false;
					}

					return false;
				}
			});

		tab.setGravity(Gravity.CENTER);
		tab.setLayoutParams(
			new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT, 1f));

		addView(tab);
		setWeightSum(1f * getChildCount());
		return this;
	}

	public SupportTabGroup select(int index)
	{
		getChildAt(index).setBackgroundColor(color_state_down);
		return this;
	}
}
