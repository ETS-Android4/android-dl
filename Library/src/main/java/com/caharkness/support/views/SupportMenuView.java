package com.caharkness.support.views;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.caharkness.support.R;
import com.caharkness.support.utilities.SupportColors;
import com.caharkness.support.utilities.SupportDevice;
import com.caharkness.support.utilities.SupportInput;
import com.caharkness.support.utilities.SupportMath;

import java.util.ArrayList;

public class SupportMenuView extends ScrollView
{
    private LinearLayout layout;

    public LinearLayout getLinearLayout()
    {
        if (layout == null)
        {
            layout = new LinearLayout(getContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setLayoutParams(
                new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
        }

        return layout;
    }

    public SupportMenuView(Context c)
    {
        super(c);

        setLayoutParams(
            new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        addView(getLinearLayout());
        setFillViewport(true);
    }

    public SupportMenuView addItem(SupportMenuItemView... items)
    {
        for (SupportMenuItemView i : items)
            getLinearLayout()
                .addView(i);

        return this;
    }


    public ArrayList<SupportMenuItemView> getItems(ViewGroup group)
	{
		ArrayList<SupportMenuItemView> items = new ArrayList<>();

		for (int i = 0; i < group.getChildCount(); i++)
		{
			View child = group.getChildAt(i);

			if (child instanceof SupportMenuItemView)
				items.add((SupportMenuItemView) child);

			if (child instanceof ViewGroup)
				items.addAll(getItems((ViewGroup) child));
		}

		return items;
	}

	public ArrayList<SupportMenuItemView> getItems()
	{
		return
		getItems(getLinearLayout());
	}

	public SupportMenuItemView getLastAddedItem()
	{
		if (getItems().size() > 0)
			return getItems().get(getItems().size() - 1);

		return null;
	}

	public void removeAllItems()
	{
		for (SupportMenuItemView view : getItems())
		{
			ViewParent parent = view.getParent();
			if (parent instanceof ViewGroup)
			{
				ViewGroup container = (ViewGroup) parent;
				container.removeView(view);
			}
		}

		getLinearLayout()
		    .removeAllViews();
	}

	public ArrayList<SupportMenuItemView> getItemsContainingTag(String tag)
	{
		ArrayList<SupportMenuItemView> items = new ArrayList<>();

		for (SupportMenuItemView item : getItems())
			if (item.hasTag(tag))
				items.add(item);

		return items;
	}

	public ArrayList<SupportMenuItemView> getItemsContainingMetadata(String key, String... value)
	{
		ArrayList<SupportMenuItemView> items = new ArrayList<>();

		for (SupportMenuItemView item : getItems())
			if (item.hasMetadata(key))
			{
				if (value != null && value.length > 0)
				{
					if (item.getMetadata(key).equals(value[0]))
						items.add(item);
				}
				else
				{
					items.add(item);
				}
			}

		return items;
	}




    public SwipeRefreshLayout getAsSwipeRefreshLayout(final Runnable runnable)
	{
		final SwipeRefreshLayout refresh_layout = new SwipeRefreshLayout(getContext());

		refresh_layout.addView(this);
		refresh_layout.setColorSchemeColors(SupportColors.getAccentColor(getContext()));
		refresh_layout.setOnRefreshListener(
			new SwipeRefreshLayout.OnRefreshListener()
			{
				@Override
				public void onRefresh()
				{
					//
					//	Remove the circle gracefully.
					//
					postDelayed(
						new Runnable()
						{
							@Override
							public void run()
							{
								refresh_layout.setRefreshing(false);
							}
						},
						500);

					//
					//	Then, run the runnable.
					//
					if (runnable != null)
						postDelayed(runnable, 750);
				}
			});

		return refresh_layout;
	}





    public PopupWindow showAsPopup(View anchor)
    {
        RelativeLayout lyt = new RelativeLayout(getContext());
        //lyt.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        lyt.addView(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            lyt.setPadding(
                SupportMath.inches(1/16f),
                SupportMath.inches(1/16f),
                SupportMath.inches(1/16f),
                SupportMath.inches(1/16f));

            lyt.setClipToPadding(false);

            this.setBackgroundColor(SupportColors.get("white"));
            this.setElevation(12f);
        }
        else
        {
        }

        lyt.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        int width = lyt.getMeasuredWidth();
        int height = lyt.getMeasuredHeight();



        float mw = 0.67f;
        float mh = 0.67f;

        if (width > Math.round(SupportDevice.screenWidth() * mw))
            width = Math.round(SupportDevice.screenWidth() * mw);

        if (height > Math.round(SupportDevice.screenHeight() * mh))
            height = Math.round(SupportDevice.screenHeight() * mh);

        if (width > SupportMath.inches(2f))
            width = SupportMath.inches(2f);

        if (height > SupportMath.inches(3f))
            height = SupportMath.inches(3f);



        final PopupWindow
            win = new PopupWindow(lyt);
            win.setWidth(width);
            win.setHeight(height);
            win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            win.setOutsideTouchable(true);
            win.setFocusable(true);
            win.showAsDropDown(
                anchor,
                0,
                -anchor.getHeight() / 2
                -lyt.getPaddingTop());

        for (SupportMenuItemView i : getItems())
        {
            if (i.hasAction())
                i.addAction(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        win.dismiss();
                    }
                });
        }

        return win;
    }







    @SuppressLint("all")
	public Dialog showAsMenu(boolean cancellable)
	{
		try
		{
			ViewParent parent = this.getParent();
			if (parent instanceof ViewGroup)
			{
				ViewGroup group = (ViewGroup) parent;
				group.removeView(this);
			}
		}
		catch (Exception x) {}

		//
		//
		//
		//
		//
		//
		//

		final Dialog dialog = new Dialog(getContext());

		GradientDrawable
			background = new GradientDrawable();
			background.setColor(SupportColors.getBackgroundColor(getContext()));

		setBackgroundDrawable(background);

		//
		//
		//
		//
		//
		//
		//

		int flags = 0;

		int x = SupportInput.getLastTouchX();
		int y = SupportInput.getLastTouchY();
		int w = SupportDevice.screenWidth();
		int h = SupportDevice.screenHeight();

		if (y < h / 3)						flags |= Gravity.TOP;
		if (y >= h / 3 && y <= (h * 2/3))	flags |= Gravity.CENTER_VERTICAL;
		if (y > (h * 2/3))					flags |= Gravity.BOTTOM;

		if (x < w / 3)						flags |= Gravity.LEFT;
		if (x >= w / 3 && x <= (w * 2/3))	flags |= Gravity.CENTER_HORIZONTAL;
		if (x > (w * 2/3))					flags |= Gravity.RIGHT;

		WindowManager.LayoutParams p =
			new WindowManager
				.LayoutParams();

		try
		{
			p.copyFrom(
				dialog
					.getWindow()
					.getAttributes());
		}
		catch (Exception e) {}

		p.gravity			= flags;
		p.width				= 100;
		p.height			= 100;
		p.horizontalMargin 	= 0f;
		p.verticalMargin 	= 0f;

		//
		//
		//
		//
		//
		//
		//

		dialog.getWindow().setAttributes(p);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(this);
		dialog.setCancelable(cancellable);
		dialog.show();

		for (SupportMenuItemView item : getItems())
			if (item.hasAction())
				item.addAction(new Runnable()
				{
					@Override
					public void run()
					{
						dialog.dismiss();
					}
				});

		//
		//
		//
		//	Area:
		//
		//
		//

		final int gravity_flags = flags;

		addOnLayoutChangeListener(
			new OnLayoutChangeListener()
			{
				@Override
				public void onLayoutChange(View view, int a, int b, int c, int d, int e, int f, int g, int h)
				{
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
					{
						//
						//	Animate the dialog in a material-like fashion.
						//
						int cx = SupportInput.getLastTouchX();
						int cy = SupportInput.getLastTouchY();

						Animator animation = ViewAnimationUtils.createCircularReveal(
							dialog.getWindow().getDecorView(),
							cx,
							cy,
							0f,
							(float) Math.hypot(cx, cy));

						animation.setDuration(400);
						animation.start();
					}
					else
					{
						//
						//	Otherwise, animate it as growing from the part of the screen it came from.
						//
						dialog
							.getWindow()
							.setBackgroundDrawable(
								new ColorDrawable(Color.TRANSPARENT));

						int resource = 0;

						switch (gravity_flags)
						{
							case Gravity.TOP | Gravity.LEFT:							resource = R.anim.dialog_top_left;		break;
							case Gravity.TOP | Gravity.CENTER_HORIZONTAL:				resource = R.anim.dialog_top_center;	break;
							case Gravity.TOP | Gravity.RIGHT:							resource = R.anim.dialog_top_right;		break;

							case Gravity.CENTER_VERTICAL | Gravity.LEFT:				resource = R.anim.dialog_middle_left;	break;
							case Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL:	resource = R.anim.dialog_middle_center;	break;
							case Gravity.CENTER_VERTICAL | Gravity.RIGHT:				resource = R.anim.dialog_middle_right;	break;

							case Gravity.BOTTOM | Gravity.LEFT:							resource = R.anim.dialog_bottom_left;	break;
							case Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL:			resource = R.anim.dialog_bottom_center;	break;
							case Gravity.BOTTOM | Gravity.RIGHT:						resource = R.anim.dialog_bottom_right;	break;
						}

						view.startAnimation(
							AnimationUtils.loadAnimation(
								getContext(),
								resource));
					}

					//
					//	We only need this once.
					//
					view.removeOnLayoutChangeListener(this);
				}
			});

		this.getLastAddedItem().setMinimumWidth(SupportMath.inches(2f));
		return dialog;
	}

	public Dialog showAsMenu()
	{
		return showAsMenu(true);
	}
}
