package com.caharkness.support.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.caharkness.support.R;
import com.caharkness.support.SupportApplication;
import com.caharkness.support.layouts.SupportFrameLayout;
import com.caharkness.support.utilities.SupportBitmap;
import com.caharkness.support.utilities.SupportColors;
import com.caharkness.support.utilities.SupportDrawable;
import com.caharkness.support.utilities.SupportMath;
import com.caharkness.support.utilities.SupportRunnable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class SupportMenuItemView extends SupportFrameLayout
{
    private Integer padding_size;

    public Integer getPaddingSize()
    {
        if (padding_size == null)
            padding_size = SupportMath.inches(1 / 8f);

        return padding_size;
    }

    private ArrayList<View> padded_elements;

    public ArrayList<View> getPaddedElements()
    {
        if (padded_elements == null)
            padded_elements = new ArrayList<>();

        return padded_elements;
    }

    public SupportMenuItemView setPaddingSize(int size)
    {
        padding_size = size;

        for (View v : getPaddedElements())
        {
            v.setPadding(
                v.getPaddingLeft() > 0? getPaddingSize() : 0,
                v.getPaddingTop() > 0? getPaddingSize() : 0,
                v.getPaddingRight() > 0? getPaddingSize() : 0,
                v.getPaddingBottom() > 0? getPaddingSize() : 0);
        }

        return this;
    }

    public SupportMenuItemView(Context c)
    {
        super(c, false);

        setBackgroundColor(SupportColors.getBackgroundColor(getContext()));
        setLayoutParams(
            new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

        getLeftLayout().setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

        getCenterLayout().setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        getCenterLayout()
            .setPadding(
                getPaddingSize(),
                getPaddingSize(),
                getPaddingSize(),
                getPaddingSize());

        getPaddedElements()
            .add(getCenterLayout());

        getRightLayout().setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
    }

    /**
     *  Adds a banner graphic to the center of the menu item.
     *  @param drawable The drawable to use for this graphic
     */
    public SupportMenuItemView setImageDrawable(Drawable drawable)
    {
        if (drawable == null)
        {
            getCenterLayout().removeAllViews();
            getCenterLayout().setPadding(0, 0, 0, 0);
            return this;
        }

        ImageView
            v = new ImageView(getContext());
            v.setImageDrawable(drawable);

        getCenterLayout().removeAllViews();
        getCenterLayout().addView(v);
        getCenterLayout().setPadding(
            getPaddingSize(),
            getPaddingSize(),
            getPaddingSize(),
            getPaddingSize());

        getPaddedElements()
            .add(getCenterLayout());

        return this;
    }

    /**
     *  Adds an icon on the left side of the menu item.
     *  @param drawable The drawable to use for this icon
     */
    public SupportMenuItemView setLeftIcon(Drawable drawable)
    {
        if (drawable == null)
        {
            getLeftLayout().removeAllViews();
            getLeftLayout().setPadding(0, 0, 0, 0);
            return this;
        }

        ImageView
            v = new ImageView(getContext());
            v.setImageDrawable(drawable);
            v.setTag("left icon");

        getLeftLayout().removeAllViews();
        getLeftLayout().addView(v);
        getLeftLayout().setPadding(
            getPaddingSize(),
            getPaddingSize(),
            0,
            getPaddingSize());

        getPaddedElements()
            .add(getLeftLayout());

        return this;
    }

    /**
     *  Adds an icon on the left side of the menu item.
     *  @param resource The resource ID of the icon, e.g. R.drawable.ic_settings
     *  @param colors One or more colors used to generate an icon
     *  Usage:
     *  colors[0] - the primary color of the icon or the icon's circle (required)
     *  colors[1] - the foreground color of the icon to be drown over the circle (optional)
     */
	public SupportMenuItemView setLeftIcon(int resource, int... colors)
	{
		Drawable drawable =
			SupportDrawable.tint(
				SupportDrawable.fromResourceSmall(resource),
				colors[0]);

		if (colors.length > 1)
		{
			drawable =
				SupportDrawable.fromBitmap(
					SupportBitmap.merge(
						SupportBitmap.fromCircle(SupportMath.inches(0.26f), colors[0]),
						SupportBitmap.tint(
								SupportBitmap.fromResource(resource, 0.16f),
								colors[1])));
		}

		return setLeftIcon(drawable);
	}

    /**
     *  Adds an icon on the left side of the menu item.
     *  Note: When no color is specified, the default foreground color is used.
     *  @param resource The resource ID of the icon
     */
	public SupportMenuItemView setLeftIcon(int resource)
	{
		return setLeftIcon(
			resource,
			SupportColors.translucent(
                SupportColors.getForegroundColor(getContext()),
                0x60));
    }

    /**
     *  Animates the left icon using the specified animation resource.
     *  @param resource The resource ID of the animation
     */
	public SupportMenuItemView setLeftIconAnimation(int resource)
	{
	    final ImageView v = findViewWithTag("left icon");

	    v.addOnAttachStateChangeListener(
			new OnAttachStateChangeListener()
			{
				@Override
				public void onViewAttachedToWindow(View view)
				{
					v.startAnimation(
						AnimationUtils.loadAnimation(
							getContext(),
							R.anim.rotate_indefinitely));
				}

				@Override
				public void onViewDetachedFromWindow(View view)
				{
				}
			});

		return this;
    }

    /**
     *  Adds an icon on the right side of the menu item.
     *  @param drawable The drawable to use for this icon
     */
    public SupportMenuItemView setRightIcon(Drawable drawable)
    {
        if (drawable == null)
        {
            getRightLayout().removeAllViews();
            getRightLayout().setPadding(0, 0, 0, 0);
            return this;
        }

        ImageView
            v = new ImageView(getContext());
            v.setImageDrawable(drawable);
            v.setTag("right icon");

        getRightLayout().removeAllViews();
        getRightLayout().addView(v);
        getRightLayout().setPadding(
            0,
            getPaddingSize(),
            getPaddingSize(),
            getPaddingSize());

        getPaddedElements()
            .add(getRightLayout());

        return this;
    }

    /**
     *  Adds an icon on the right side of the menu item.
     *  @param resource The resource ID of the icon, e.g. R.drawable.ic_chevron_right
     *  @param colors One or more colors used to generate an icon
     *  Usage:
     *  colors[0] - the primary color of the icon or the icon's circle (required)
     *  colors[1] - the foreground color of the icon to be drown over the circle (optional)
     */
	public SupportMenuItemView setRightIcon(int resource, int... colors)
	{
		Drawable drawable =
			SupportDrawable.tint(
				SupportDrawable.fromResourceSmall(resource),
				colors[0]);

		if (colors.length > 1)
		{
			drawable =
				SupportDrawable.fromBitmap(
					SupportBitmap.merge(
						SupportBitmap.fromCircle(SupportMath.inches(0.26f), colors[0]),
						SupportBitmap.tint(
								SupportBitmap.fromResource(resource, 0.16f),
								colors[1])));
		}

		return setRightIcon(drawable);
	}

    /**
     *  Adds an icon on the right side of the menu item.
     *  Note: When no color is specified, the default foreground color is used.
     *  @param resource The resource ID of the icon
     */
	public SupportMenuItemView setRightIcon(int resource)
	{
		return setRightIcon(
			resource,
			SupportColors.translucent(
                SupportColors.getForegroundColor(getContext()),
                0x60));
    }

    /**
     *  Adds a title to the menu item at 16pt using the full opacity of the foreground color.
     */
    public SupportMenuItemView setLabel(String text, int color)
    {
        TextView v =
            getCenterLayout()
                .findViewWithTag("label");

        if (v == null)
        {
            v = new TextView(getContext());
            v.setTextColor(color);
            v.setTextSize(14f);
            v.setTypeface(Typeface.DEFAULT_BOLD);
            v.setAllCaps(true);
            v.setTag("label");

            getCenterLayout()
                .addView(v);
        }

        v.setText(text);

        return this;
    }

    public SupportMenuItemView setLabel(String text)
    {
        return
        setLabel(text, SupportColors.getForegroundColor(getContext()));
    }

    /**
     *  Adds a title to the menu item at 16pt using the full opacity of the foreground color.
     */
    public SupportMenuItemView setTitle(String text)
    {
        TextView v =
            getCenterLayout()
                .findViewWithTag("title");

        if (v == null)
        {
            v = new TextView(getContext());
            v.setTextColor(SupportColors.getForegroundColor(getContext()));
            v.setTextSize(16f);
            v.setTag("title");

            getCenterLayout()
                .addView(v);
        }

        v.setText(text);

        return this;
    }

    /**
     *  Adds a subtitle to the menu item at 14pt using the foreground color with translucency.
     */
    public SupportMenuItemView setSubtitle(String text)
    {
        if (getFocusableEditText() != null)
        {
            getFocusableEditText().setHint(text);
            return this;
        }

        TextView v =
            getCenterLayout()
                .findViewWithTag("subtitle");

        if (v == null)
        {
            v = new TextView(getContext());
            v.setTextColor(
                SupportColors.translucent(
                    SupportColors.getForegroundColor(getContext()),
                    0x60));
            v.setTextSize(14f);
            v.setTag("subtitle");

            getCenterLayout()
                .addView(v);
        }

        v.setText(text);

        return this;
    }

    /**
     *  Adds a hint to the focusable edit text.
     */
    public SupportMenuItemView setHint(String text)
    {
        if (getFocusableEditText() != null)
        {
            getFocusableEditText().setHint(text);
            return this;
        }

        return this;
    }
    
    public SupportMenuItemView addTopDivider()
    {
        LinearLayout v = new LinearLayout(getContext());
        v.setTag("top_divider");
        v.setMinimumHeight(SupportMath.dp(1f));
        v.setLayoutParams(
            new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        v.setBackgroundColor(
            SupportColors.translucent(
                SupportColors.getForegroundColor(getContext())));

        getBottomLayout().addView(v);
        return this;
    }

    public SupportMenuItemView addBottomDivider()
    {
        LinearLayout v = new LinearLayout(getContext());
        v.setTag("bottom_divider");
        v.setMinimumHeight(SupportMath.dp(1f));
        v.setLayoutParams(
            new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        v.setBackgroundColor(
            SupportColors.translucent(
                SupportColors.getForegroundColor(getContext())));

        getBottomLayout().addView(v);
        return this;
    }

    public SupportMenuItemView addInsideDivider()
    {
        LinearLayout v = new LinearLayout(getContext());
        v.setTag("inside_divider");
        v.setMinimumHeight(SupportMath.dp(1f));
        v.setLayoutParams(
            new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        v.setBackgroundColor(
            SupportColors.translucent(
                SupportColors.getForegroundColor(getContext())));

        getCenterLayout().addView(v);
        return this;
    }

    public SupportMenuItemView addSpacer()
    {
        LinearLayout
            v = new LinearLayout(getContext());

            v.setTag("spacer");
            v.setMinimumHeight(getPaddingSize());
            v.setLayoutParams(
                new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            v.setBackgroundColor(Color.TRANSPARENT);

        getCenterLayout()
            .addView(v);

        return this;
    }

    public SupportMenuItemView setTable(String[]... rows)
    {
        TableLayout
            t = new TableLayout(getContext());
            t.setLayoutParams(
                new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));

        for (String[] r : rows)
        {
            TableRow tr = new TableRow(getContext());

            String key = r[0];
            String value = r[1];

            TextView
                kv = new TextView(getContext());
                kv.setTypeface(Typeface.DEFAULT_BOLD);
                kv.setTextSize(12f);
                kv.setTextColor(SupportColors.getAccentColor(getContext()));
                kv.setText(key);
                kv.setGravity(Gravity.RIGHT);
                kv.setPadding(0, 0, SupportMath.inches(1 / 16f), 0);
                kv.setAllCaps(true);

            TextView
                vv = new TextView(getContext());
                vv.setTextSize(12f);
                vv.setTextColor(SupportColors.getForegroundColor(getContext()));
                vv.setText(value);

            tr.addView(kv);
            tr.addView(vv);
            t.addView(tr);
        }

        getCenterLayout()
            .addView(t);

        return this;
    }

    private Runnable action;

    /**
     *  Sets a runnable to be called when the menu item is tapped or clicked.
     */
    public SupportMenuItemView setAction(final Runnable runnable)
    {
        action = runnable;

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
                                SupportColors.getBackgroundColor(getContext()),
                                0x10));
                        return false;

                    default:
                        setBackgroundColor(
                            SupportColors.getBackgroundColor(getContext()));
                        return false;
                }
            }
        });

        return this;
    }

    /**
     *  Add a subsequent runnable to be called when the menu item is tapped or clicked.
     *  Note: The runnables will be called in the order they were defined.
     */
    public SupportMenuItemView addAction(final Runnable runnable)
    {
        if (runnable == null)
            return this;

        if (action == null)
            return setAction(runnable);

        return
        setAction(
            SupportRunnable.join(
                action,
                runnable));
    }

    public boolean hasAction()
    {
        return
            action != null &&
            isClickable();
    }

    private Runnable alternate_action;

    /**
     *  Sets a runnable to be called when the menu item is long pressed.
     */
    public SupportMenuItemView setAlternateAction(final Runnable runnable)
    {
        alternate_action = runnable;

        if (runnable == null)
        {
            setLongClickable(false);
            setOnLongClickListener(null);
            return this;
        }

        setLongClickable(true);
        setOnLongClickListener(new OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                post(runnable);
                return true;
            }
        });

        return this;
    }

    /**
     *  Add a subsequent runnable to be called when the menu item is long pressed.
     *  Note: The runnables will be called in the order they were defined.
     */
    public SupportMenuItemView addAlternateAction(final Runnable runnable)
    {
        if (runnable == null)
            return this;

        if (alternate_action == null)
            return setAlternateAction(runnable);

        return
        setAlternateAction(
            SupportRunnable.join(
                alternate_action,
                runnable));
    }

    public boolean hasAlternateAction()
    {
        return
            alternate_action != null &&
            isLongClickable();
    }

    public SupportMenuItemView setButtonActions(SupportButton... buttons)
    {
        SupportButtonStrip strip = new SupportButtonStrip(getContext());

        for (SupportButton b : buttons)
            strip.add(b);

        getCenterLayout()
            .addView(strip);

        return this;
    }

    private ArrayList<String> tags;

    /**
     *  Gets an array list of tags applied to this menu item.
     */
    public ArrayList<String> getTags()
	{
		if (tags == null)
			tags = new ArrayList<>();

		return tags;
	}

    /**
     *  Applies a tag to this menu item.
     */
	public SupportMenuItemView addTag(String tag)
	{
		getTags().add(tag);
		return this;
	}

    /**
     *  Remove a tag applied to this menu item.
     */
	public SupportMenuItemView removeTag(String tag)
	{
		while (getTags().contains(tag))
			getTags().remove(tag);

		return this;
	}

    /**
     *  Checks whether or not a tag has been applied to this menu item.
     */
	public boolean hasTag(String tag)
	{
		return
		getTags()
		    .contains(tag);
	}

	private LinkedHashMap<String, String> metadata;

    /**
     *  Gets a linked hash map of metadata associated with this menu item.
     */
	public LinkedHashMap<String, String> getMetadata()
	{
		if (metadata == null)
			metadata = new LinkedHashMap<>();

		return metadata;
	}

    /**
     *  Returns the value of stored metadata via its key.
     */
	public String getMetadata(String key)
	{
		if (hasMetadata(key))
			return getMetadata().get(key);

		return "";
	}

    /**
     *  Store a key-value pair as metadata with this menu item.
     */
	public SupportMenuItemView putMetadata(String key, String value)
	{
		getMetadata().put(key, value);
		return this;
	}

    /**
     *  Checks whether or not this menu item contains metadata by its key.
     */
	public boolean hasMetadata(String key)
	{
		return
		getMetadata()
		    .containsKey(key);
	}

	private View focusable;

	public View getFocusableView()
    {
        return focusable;
    }

    public SupportToggle getFocusableToggle()
    {
        if (focusable != null)
        if (focusable instanceof SupportToggle)
            return (SupportToggle) focusable;

        return null;
    }

    public EditText getFocusableEditText()
    {
        if (focusable != null)
        if (focusable instanceof EditText)
            return (EditText) focusable;

        return null;
    }

    /**
     *  Adds a toggle to this menu item that controls the value of the preference.
     *  @param pref The name of the preference being toggled
     *  @param def The value this preference will be set to when this view is shown
     *  @param runnable The runnable called when the toggle's value is changed
     */
	public SupportMenuItemView setBooleanPreference(final String pref, Boolean def, final Runnable runnable)
	{
		if (def != null)
			SupportApplication.setBoolean(pref, def);

		//
		//
		//

		final SupportToggle toggle = new SupportToggle(getContext());

		toggle.setChecked(SupportApplication.getBoolean(pref, false));
		toggle.setOnCheckChanged(new Runnable()
		{
			@Override
			public void run()
			{
				SupportApplication.setBoolean(
					pref,
					toggle.getChecked());

				if (runnable != null)
					post(runnable);
			}
		});

		setAction(new Runnable()
        {
            @Override
            public void run()
            {
                toggle.toggle();
            }
        });

		getRightLayout().removeAllViews();
		getRightLayout().addView(toggle);
		getRightLayout().setPadding(
			0,
			getPaddingSize(),
			getPaddingSize(),
			getPaddingSize());

		getPaddedElements()
            .add(getRightLayout());

		focusable = toggle;
		return this;
	}

    /**
     *  Adds a toggle to this menu item that controls the value of the preference.
     *  @param pref The name of the preference being toggled
     */
	public SupportMenuItemView setBooleanPreference(String pref)
	{
		return
		setBooleanPreference(
			pref,
			null,
			null);
	}

    /**
     *  Adds a toggle to this menu item that controls the value of the preference.
     *  @param pref The name of the preference being toggled
     *  @param runnable The runnable called when the toggle's value is changed
     */
	public SupportMenuItemView setBooleanPreference(String pref, Runnable runnable)
	{
		return
		setBooleanPreference(
			pref,
			null,
			runnable);
	}

    /**
     *  Adds a toggle to this menu item that controls the value of the preference.
     *  @param pref The name of the preference being toggled
     *  @param def The value this preference will be set to when this view is shown
     */
	public SupportMenuItemView setBooleanPreference(String pref, boolean def)
	{
		return
		setBooleanPreference(
			pref,
			def,
			null);
	}

	//
	//
	//
	//	Area: String preferences
	//
	//
	//

    /**
     *  Adds an edit text to this menu item that controls the string value of a preference.
     *  @param pref The name of the string preference being modified
     *  @param def The value this preference will be set to when this view is shown
     *  @param runnable The runnable called when the edit text's value is changed
     *  @param type the edit text's desired input type
     */
	public SupportMenuItemView setStringPreference(final String pref, String def, final Runnable runnable, int type)
	{
		if (def != null)
			SupportApplication.setString(pref, def);

		//
		//
		//

		final EditText edit_text = new EditText(getContext());

		if (type != 0)
			edit_text.setInputType(type);

		edit_text.setBackgroundColor(SupportColors.getBackgroundColor(getContext()));
		edit_text.setTextColor(SupportColors.getForegroundColor(getContext()));
		edit_text.setPadding(0, 0, 0, 0);
		edit_text.setHintTextColor(
		    SupportColors.translucent(
		        SupportColors.getForegroundColor(getContext())));

		edit_text.setId(SupportMath.randomInt());
		edit_text.setGravity(Gravity.TOP | Gravity.LEFT);
		edit_text.setText(SupportApplication.getString(pref, ""));
		edit_text.setSelection(edit_text.getText().length());
		edit_text.setFocusable(true);
		edit_text.setFocusableInTouchMode(true);
		edit_text.addTextChangedListener(new TextWatcher()
        {
            @Override public void beforeTextChanged(CharSequence charSequence, int x, int y, int z) {}
            @Override public void onTextChanged(CharSequence charSequence, int x, int y, int z) {}

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

		//
		//
		//

		edit_text.clearFocus();
		edit_text.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				edit_text.requestLayout();
				edit_text.requestFocus();
				edit_text.setInputType(edit_text.getInputType());
				edit_text.setRawInputType(edit_text.getInputType());

				((InputMethodManager) edit_text.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
					.showSoftInput(edit_text, InputMethodManager.SHOW_IMPLICIT);
			}
		});

		getCenterLayout()
		    .addView(edit_text);

		focusable = edit_text;
		return this;
	}

    /**
     *  Adds an edit text to this menu item that controls the string value of a preference.
     *  @param pref The name of the string preference being modified
     */
	public SupportMenuItemView setStringPreference(String pref)
	{
		return
		setStringPreference(
			pref,
			null,
			null,
			0);
	}

    /**
     *  Adds an edit text to this menu item that controls the string value of a preference.
     *  @param pref The name of the string preference being modified
     *  @param runnable The runnable called when the edit text's value is changed
     */
	public SupportMenuItemView setStringPreference(String pref, Runnable runnable)
	{
		return
		setStringPreference(
			pref,
			null,
			runnable,
			0);
	}

    /**
     *  Adds an edit text to this menu item that controls the string value of a preference.
     *  @param pref The name of the string preference being modified
     *  @param def The value this preference will be set to when this view is shown
     */
	public SupportMenuItemView setStringPreference(String pref, String def)
	{
		return
		setStringPreference(
			pref,
			def,
			null,
			0);
	}

    /**
     *  Adds an edit text to this menu item that controls the string value of a preference.
     *  @param pref The name of the string preference being modified
     *  @param def The value this preference will be set to when this view is shown
     *  @param lines The minimum height this edit text should have in lines
     */
	public SupportMenuItemView setStringPreference(String pref, String def, int lines)
	{
		setStringPreference(
			pref,
			def,
			null,
			0);

		((EditText) focusable).setLines(lines);
		return this;
	}

    /**
     *  Adds an edit text to this menu item that controls the string value of a preference.
     *  @param pref The name of the string preference being modified
     *  @param type the edit text's desired input type
     */
	public SupportMenuItemView setStringPreference(String pref, int type)
	{
		return
		setStringPreference(
			pref,
			null,
			null,
			type);
	}

    /**
     *  Adds a MASKED edit text to this menu item that controls the string value of a preference.
     *  Note: It is strongly encouraged to erase this preference's value after it is done being used!
     *  @param pref The name of the string preference being modified
     */
	public SupportMenuItemView setStringPreferenceHidden(String pref)
	{
		setStringPreference(
			pref,
			null,
			null,
			InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

		((EditText) focusable).setTransformationMethod(PasswordTransformationMethod.getInstance());
		return this;
	}

	public SupportMenuItemView setRightIconFromColor(String pref)
	{
		if (SupportApplication.getString(pref, "").length() > 0)
		{
			int color = SupportApplication.getInt(pref, SupportColors.get("grey"));
			if (color == SupportColors.getBackgroundColor(getContext()))
			{
				if (SupportColors.isLight(color))
					setRightIcon(
						SupportDrawable.fromBitmap(
							SupportBitmap.merge(
								SupportBitmap.fromCircle(SupportMath.inches(3 / 25f), SupportColors.subtract(color, 0x1F)),
								SupportBitmap.fromCircle(SupportMath.inches(1 / 10f), color))));
				else
                setRightIcon(
                    SupportDrawable.fromBitmap(
                        SupportBitmap.merge(
                            SupportBitmap.fromCircle(SupportMath.inches(3 / 25f), SupportColors.add(color, 0x1F)),
                            SupportBitmap.fromCircle(SupportMath.inches(1 / 10f), color))));
			}
			else
            setRightIcon(
                SupportDrawable.fromBitmap(
                    SupportBitmap.fromCircle(
                        SupportMath.inches(3 / 25f),
                        color)));
		}
		else
		setRightIcon(R.drawable.ic_colorize);

		return this;
	}

	public SupportMenuItemView setColorPreference(final String pref, final Runnable on_change)
	{
		setRightIconFromColor(pref);
		setAction(new Runnable()
		{
			PopupWindow w;

			@Override
			public void run()
			{
				final SupportMenuView colors = new SupportMenuView(getContext());

				for (final Map.Entry<String, Integer> color : SupportColors.getColorMap().entrySet())
				{
					SupportApplication.setInt("_c", color.getValue());

					colors.addItem(
						new SupportMenuItemView(getContext())
							.setTitle(color.getKey())
							.setRightIcon(R.drawable.ic_brightness_1)
							.setRightIconFromColor("_c")
							.setAction(new Runnable()
							{
								@Override
								public void run()
								{
									SupportApplication.setInt(
										pref,
										color.getValue());

									setRightIconFromColor(pref);
									w.dismiss();

									if (on_change != null)
									    on_change.run();
								}
							}));
				}

				w = colors.showAsPopup(SupportMenuItemView.this.getRightLayout());
			}
		});

		setAlternateAction(new Runnable()
		{
			Dialog d;

			@Override
			public void run()
			{
				final SupportMenuView menu	= new SupportMenuView(getContext());
				float[] hsv					= new float[3];

				Color.colorToHSV(
					SupportApplication.getInt(pref, SupportColors.get("grey")),
					hsv);

				SupportApplication.setFloat("_h", hsv[0] / 360f);
				SupportApplication.setFloat("_s", hsv[1]  * 1f);
				SupportApplication.setFloat("_v", hsv[2] * 1f);

				final SupportMenuItemView item = new SupportMenuItemView(getContext());

				item.setMinimumHeight(SupportMath.inches(1 / 2f));


				menu.addItem(item);

				final Runnable update_hsv = new Runnable()
				{
					@Override
					public void run()
					{
						int col = Color.HSVToColor(
							new float[]
							{
								SupportApplication.getFloat("_h", 0f) * 360,
								SupportApplication.getFloat("_s", 0.5f),
								SupportApplication.getFloat("_v", 0.5f)
							});

						SupportApplication.setInt("_c", col);

						//item.setRightIconFromColor("_c");
						item.setBackgroundColor(col);
					}
				};

				post(update_hsv);

				menu.addItem(
					new SupportMenuItemView(getContext())
						.setLeftIcon(R.drawable.ic_palette)
						.setLabel("Hue")
						.setSliderPreference("_h", update_hsv));

				menu.addItem(
					new SupportMenuItemView(getContext())
						.setLeftIcon(R.drawable.ic_gradient)
						.setLabel("Saturation")
						.setSliderPreference("_s", update_hsv));

				menu.addItem(
					new SupportMenuItemView(getContext())
						.setLeftIcon(R.drawable.ic_gradient)
						.setLabel("Value")
						.setSliderPreference("_v", update_hsv));

				menu.addItem(
					new SupportMenuItemView(getContext())
					    .setButtonActions(
					        new SupportButton(getContext())
					            .setColors(SupportColors.get("grey"), SupportColors.get("white"))
					            .setLabel("Close")
					            .setAction(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        d.dismiss();
                                    }
                                }),

						    new SupportButton(getContext())
					            .setColors(SupportColors.getAccentColor(getContext()), SupportColors.get("white"))
					            .setLabel("Use")
					            .setAction(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        SupportApplication.setInt(
                                            pref,
                                            SupportApplication.getInt("_c", SupportColors.get("grey")));

                                        setRightIconFromColor(pref);
                                        d.dismiss();

                                        if (on_change != null)
                                            on_change.run();
                                    }
                                })));

				d = menu.showAsMenu();
			}
		});

		return this;
	}

	public SupportMenuItemView setColorPreference(String pref)
    {
        return
        setColorPreference(pref, null);
    }

	public SupportMenuItemView setSliderPreference(final String pref, final Runnable runnable)
	{
		SupportSlider slider =
			new SupportSlider(
				getContext(),
				pref,
				runnable);

		getCenterLayout().addView(slider);
		return this;
	}
}
