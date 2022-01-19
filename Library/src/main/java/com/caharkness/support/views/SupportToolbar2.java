package com.caharkness.support.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caharkness.support.R;
import com.caharkness.support.SupportApplication;
import com.caharkness.support.utilities.SupportDrawable;
import com.caharkness.support.utilities.SupportMath;
import com.caharkness.support.utilities.SupportColors;

public class SupportToolbar2 extends LinearLayout
{
    private Integer foreground_color;

    public Integer getToolbarForegroundColor()
    {
        if (foreground_color == null)
            foreground_color = SupportColors.getForegroundColor(getContext());

        return foreground_color;
    }

    public void setToolbarForegroundColor(int color)
    {
        foreground_color = color;
    }

	private Integer background_color;

	public Integer getToolbarBackgroundColor()
    {
        if (background_color == null)
            background_color = SupportColors.getBackgroundColor(getContext());

        return background_color;
    }

    public void setToolbarBackgroundColor(int color)
    {
        background_color = color;
    }

    private LinearLayout left_layout;

    public LinearLayout getLeftLayout()
    {
        if (left_layout == null)
        {
            left_layout = new LinearLayout(getContext());
		    left_layout.setPadding(
			    SupportMath.inches(1 / 16f), 0,
			    SupportMath.inches(1 / 16f), 0);
        }

        return left_layout;
    }

    private LinearLayout center_layout;

    public LinearLayout getCenterLayout()
    {
        if (center_layout == null)
        {
            center_layout = new LinearLayout(getContext());
            center_layout.setOrientation(LinearLayout.VERTICAL);
            center_layout.setGravity(Gravity.CENTER | Gravity.LEFT);
            center_layout.setLayoutParams(
                new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT, 1f));

            center_layout.setPadding(
                0, SupportMath.inches(1 / 16f),
                0, SupportMath.inches(1 / 16f));
        }

        return center_layout;
    }

    private LinearLayout right_layout;

    public LinearLayout getRightLayout()
    {
        if (right_layout == null)
        {
            right_layout = new LinearLayout(getContext());
            right_layout.setPadding(
                SupportMath.inches(1 / 16f), 0,
                SupportMath.inches(1 / 16f), 0);
        }

        return right_layout;
    }

	private LinearLayout master_toolbar;

	public LinearLayout getMasterToolbar()
    {
        if (master_toolbar == null)
        {
            master_toolbar = new LinearLayout(getContext());
            master_toolbar.setMinimumHeight(SupportMath.inches(3 / 8f));
            master_toolbar.setGravity(Gravity.CENTER);
            master_toolbar.setLayoutParams(
                new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));

            master_toolbar.addView(getLeftLayout());
            master_toolbar.addView(getCenterLayout());
            master_toolbar.addView(getRightLayout());
        }

        return master_toolbar;
    }

    private LinearLayout container;

	public LinearLayout getContainer()
    {
        if (container == null)
        {
            container = new LinearLayout(getContext());
            container.setOrientation(LinearLayout.VERTICAL);
            container.setLayoutParams(
                new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));

            container.addView(getMasterToolbar());
        }

        return container;
    }


    public SupportToolbar2(Context context)
    {
        super(context);
        addView(getContainer());
    }

    //
	//
	//
	//	Temporary toolbars
	//
	//
	//

    public void addTemporaryToolbar(SupportToolbar2 temporary)
	{
		for (int i = 0; i < container.getChildCount(); i++)
		{
			View view = container.getChildAt(i);
			view.setVisibility(View.GONE);
		}

		container.addView(temporary);
	}

	public void removeTemporaryToolbar()
	{
		if (container.getChildCount() > 1)
		{
			int remove_index	= container.getChildCount() - 1;
			int show_index		= container.getChildCount() - 2;
			View remove_view	= container.getChildAt(remove_index);
			View show_view		= container.getChildAt(show_index);

			container.removeView(remove_view);

			show_view.setVisibility(View.VISIBLE);
		}
	}

	public void removeAllTemporaryToolbars()
	{
		while (container.getChildCount() > 1)
		{
			View view =
				container.getChildAt(container.getChildCount() - 1);
				container.removeView(view);
		}

		container
			.getChildAt(0)
			.setVisibility(View.VISIBLE);
	}

	public void setTemporaryToolbar(SupportToolbar2 temporary)
	{
		removeAllTemporaryToolbars();

		container.getChildAt(0).setVisibility(View.GONE);
		container.addView(temporary);
	}

	//
	//
	//
	//	Toolbar appearance
	//
	//
	//

    /**
     *  Adds a title to the menu item at 16pt using the full opacity of the foreground color.
     */
    public SupportToolbar2 setTitle(String text)
    {
        TextView v =
            getCenterLayout()
                .findViewWithTag("title");

        if (v == null)
        {
            v = new TextView(getContext());

            v.setTextSize(20f);
            v.setTypeface(Typeface.DEFAULT_BOLD);
            v.setTag("title");

            getCenterLayout()
                .addView(v);
        }

        v.setText(text);
        v.setTextColor(getToolbarForegroundColor());

        if (text == null || text.length() < 1)
            v.setVisibility(GONE);
        else v.setVisibility(VISIBLE);

        return this;
    }

    public String getTitle()
    {
        try
        {
            TextView v =
                getCenterLayout().findViewWithTag("title");

            return
            v.getText()
                .toString();
        }
        catch (Exception x) {}
        return "";
    }

    /**
     *  Adds a subtitle to the menu item at 14pt using the foreground color with translucency.
     */
    public SupportToolbar2 setSubtitle(String text)
    {
        TextView v =
            getCenterLayout()
                .findViewWithTag("subtitle");

        if (v == null)
        {
            v = new TextView(getContext());
            v.setTextSize(14f);
            v.setTypeface(Typeface.DEFAULT_BOLD);
            v.setTag("subtitle");

            getCenterLayout()
                .addView(v);
        }

        v.setText(text);
        v.setTextColor(
            SupportColors.translucent(
                getToolbarForegroundColor(),
                0x60));

        if (text == null || text.length() < 1)
            v.setVisibility(GONE);
        else v.setVisibility(VISIBLE);

        return this;
    }

    public String getSubtitle()
    {
        try
        {
            TextView v =
                getCenterLayout().findViewWithTag("subtitle");

            return
            v.getText()
                .toString();
        }
        catch (Exception x) {}
        return "";
    }

	private LinearLayout createToolbarButtonView(final String name, final int resource, final Runnable runnable)
	{
		ImageView
			icon = new ImageView(getContext());
			icon.setImageDrawable(
				SupportDrawable.tint(
					SupportDrawable.fromResourceSmall(resource),
					getToolbarForegroundColor()));

		//
		//
		//

		LinearLayout button = new LinearLayout(getContext());

		button.addView(icon);
		button.setPadding(
			SupportMath.inches(1 / 16f), SupportMath.inches(1 / 16f),
			SupportMath.inches(1 / 16f), SupportMath.inches(1 / 16f));

		button.setClickable(true);
		button.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                view.startAnimation(
                    AnimationUtils.loadAnimation(
                        getContext(),
                        R.anim.bounce_in));

                if (runnable != null)
                    post(runnable);
            }
        });

		button.setOnLongClickListener(new OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                Toast.makeText(
                    getContext(),
                    name,
                    Toast.LENGTH_SHORT
                ).show();

                return true;
            }
        });

		return button;
	}

	public SupportToolbar2 setNavigationButton(int icon, Runnable runnable)
	{
		getLeftLayout().removeAllViews();
		getLeftLayout().addView(
			createToolbarButtonView(
				"Navigation",
				icon,
				runnable));

		return this;
	}

	public SupportToolbar2 setNavigationButtonAsBack()
	{
		getLeftLayout().removeAllViews();
		getLeftLayout().addView(
			createToolbarButtonView(
				"Back",
				R.drawable.ic_arrow_back,
				new Runnable()
				{
					@Override
					public void run()
					{
						dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,	KeyEvent.KEYCODE_BACK));
						dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,	KeyEvent.KEYCODE_BACK));
					}
				}));

		return this;
	}

	public SupportToolbar2 removeActions()
    {
        getRightLayout()
            .removeAllViews();

        return this;
    }

	public SupportToolbar2 addAction(String name, int icon, final Runnable runnable)
	{
		getRightLayout().addView(
			createToolbarButtonView(
				name,
				icon,
				runnable));

		return this;
	}

    public SupportToolbar2 setColor(int color)
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

    public SupportToolbar2 setColors(int foreground_color, int background_color)
	{
		setToolbarForegroundColor(foreground_color);
		setToolbarBackgroundColor(background_color);

		setBackgroundColor(background_color);
		setTitle(getTitle());
		setSubtitle(getSubtitle());

		return this;
	}

	public SupportToolbar2 setStringPreference(final String hint, final String pref, final Runnable runnable, final boolean focus)
	{
		final EditText edit = new EditText(getContext());

		edit.setText(SupportApplication.getString(pref));
		edit.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence charSequence, int x, int y, int z) {}

			@Override
			public void onTextChanged(CharSequence charSequence, int x, int y, int z) {}

			@Override
			public void afterTextChanged(Editable editable)
			{
				SupportApplication.setString(
					pref,
					editable.toString());

				if (runnable != null)
					post(runnable);
			}
		});

		edit.setBackgroundColor(Color.TRANSPARENT);
		edit.setTextColor(getToolbarForegroundColor());
		edit.setHint(hint);
		edit.setHintTextColor(SupportColors.translucent(getToolbarForegroundColor()));
		edit.setPadding(0, 0, 0, 0);
		edit.setFocusable(true);
		edit.setFocusableInTouchMode(true);
		edit.clearFocus();
		edit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				edit.requestLayout();
				edit.requestFocus();
				edit.setInputType(edit.getInputType());
				edit.setRawInputType(edit.getInputType());

				((InputMethodManager) edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
					.showSoftInput(edit, InputMethodManager.SHOW_IMPLICIT);
			}
		});

		if (focus)
		{
			edit.addOnAttachStateChangeListener(
            new OnAttachStateChangeListener()
            {
                @Override
                public void onViewAttachedToWindow(View view)
                {
                    view.performClick();
                }

                @Override
                public void onViewDetachedFromWindow(View view)
                {
                }
            });
		}

		getCenterLayout()
		    .addView(edit);

		return this;
	}
}
