package com.caharkness.support.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.caharkness.support.R;
import com.caharkness.support.SupportApplication;
import com.caharkness.support.fragments.SupportAsyncFragment;
import com.caharkness.support.layouts.SupportFrameLayout;
import com.caharkness.support.layouts.SupportRelativeLayout;
import com.caharkness.support.utilities.SupportDevice;
import com.caharkness.support.utilities.SupportMath;
import com.caharkness.support.utilities.SupportColors;
import com.caharkness.support.views.SupportButton;
import com.caharkness.support.views.SupportFab;
import com.caharkness.support.utilities.SupportInput;
import com.caharkness.support.views.SupportFabStrip;
import com.caharkness.support.views.SupportMenuItemView;
import com.caharkness.support.views.SupportMenuView;
import com.caharkness.support.views.SupportShadow;
import com.caharkness.support.views.SupportToolbar2;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("all")
public class SupportActivity extends Activity
{
	/**
     *  Returns a context. Can be overridden to return a themed context.
     */
	public Context getContext()
	{
		return this;
	}

	//
    //
    //
    //
    //
    //
    //

    protected SupportRelativeLayout layout;

    /**
     *  Get the activity's layout.
     */
	protected RelativeLayout getLayout()
    {
        if (layout == null)
        {
            layout = new SupportRelativeLayout(getContext(), true)
            {
                @Override
                public boolean onInterceptTouchEvent(MotionEvent event)
                {
                    if (event.getAction() == MotionEvent.ACTION_DOWN)
                    {
                        SupportInput.setLastTouchX((int) event.getRawX());
                        SupportInput.setLastTouchY((int) event.getRawY());
                    }

                    if (event.getAction() == MotionEvent.ACTION_MOVE)
                        onInteract();

                    return false;
                }

                @Override
                public boolean onTouchEvent(MotionEvent event)
                {
                    return false;
                }
            };

            layout.addView(getDrawerLayout());
            layout.setLayoutParams(
                new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT));
        }

        return layout;
    }

	protected DrawerLayout drawer_layout;

    /**
     *  Get the activity's drawer layout.
     */
	protected DrawerLayout getDrawerLayout()
    {
        if (drawer_layout == null)
        {
            drawer_layout = new DrawerLayout(getContext());

            drawer_layout.addView(getContentLayoutContainer());
            drawer_layout.addView(getMenuLayout());
            drawer_layout.setDrawerShadow(new ColorDrawable(Color.TRANSPARENT), Gravity.LEFT);

            drawer_layout.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
            drawer_layout.setFocusable(false);
            drawer_layout.setFocusableInTouchMode(false);
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            drawer_layout.setLayoutParams(
                new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT));
        }

        return drawer_layout;
    }

    protected LinearLayout menu_layout;

    /**
     *  Get the activity's menu layout.
     */
	protected LinearLayout getMenuLayout()
    {
        if (menu_layout == null)
        {
            menu_layout = new LinearLayout(getContext());
            menu_layout.setLayoutParams(
                new DrawerLayout.LayoutParams(
                    DrawerLayout.LayoutParams.MATCH_PARENT,
                    DrawerLayout.LayoutParams.MATCH_PARENT)
                {{
                    gravity = Gravity.LEFT;
                }});

            menu_layout.addView(getMenuFragmentLayout());
            menu_layout.setClickable(true);
        }

        return menu_layout;
    }

    protected LinearLayout menu_fragment_layout;

	/**
     *  Get the layout that is replaced with the menu fragment.
     *  Do not addItem children to this layout.
     */
	protected LinearLayout getMenuFragmentLayout()
    {
        if (menu_fragment_layout == null)
        {
            menu_fragment_layout = new LinearLayout(getContext());
            menu_fragment_layout.setId(SupportMath.randomInt(65535));
            menu_fragment_layout.setMinimumWidth(SupportMath.inches(2f));
            menu_fragment_layout.setLayoutParams(
                new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1f));
        }

        return menu_fragment_layout;
    }

    protected SupportRelativeLayout content_layout_container;

    /**
     *  Get the content layout's container.
     */
	protected SupportRelativeLayout getContentLayoutContainer()
    {
        if (content_layout_container == null)
        {
            content_layout_container = new SupportRelativeLayout(getContext(), true);
            content_layout_container.setLayoutParams(
                new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));

            content_layout_container.addView(getContentLayout());
            content_layout_container.addView(getFabStrip());
        }

        return content_layout_container;
    }

    protected SupportFrameLayout content_layout;

    /**
     *  Get the activity's content layout.
     */
    protected SupportFrameLayout getContentLayout()
    {
        if (content_layout == null)
        {
            content_layout = new SupportFrameLayout(getContext(), true);
            content_layout.setOrientation(LinearLayout.VERTICAL);
            content_layout.setLayoutParams(
                new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));

            //content_layout.addView(getToolbar());
            content_layout.addViewTop(getToolbar());
            //content_layout.addView(getTopAccessoryView());
            content_layout.addViewTop(getTopAccessoryView());
            //content_layout.addView(getContentFragmentLayoutContainer());
            content_layout.addViewCenter(getContentFragmentLayoutContainer());
            //content_layout.addView(getBottomAccessoryView());
            content_layout.addViewBottom(getBottomAccessoryView());


        }

        return content_layout;
    }

    protected LinearLayout content_fragment_layout;

    /**
     *  Get the layout that is replaced with the content fragment.
     *  Do not addItem children to this layout.
     */
	protected LinearLayout getContentFragmentLayout()
    {
        if (content_fragment_layout == null)
        {
            content_fragment_layout = new LinearLayout(getContext());
            content_fragment_layout.setId(SupportMath.randomInt(65535));
            content_fragment_layout.setLayoutParams(
                new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
        }

        return content_fragment_layout;
    }

    protected RelativeLayout content_fragment_layout_container;

    /**
     *  Get the layout that is replaced with the content fragment.
     *  Do not addItem children to this layout.
     */
	protected RelativeLayout getContentFragmentLayoutContainer()
    {
        if (content_fragment_layout_container == null)
        {
            content_fragment_layout_container = new RelativeLayout(getContext());
            content_fragment_layout_container.setLayoutParams(
                new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));

            content_fragment_layout_container.addView(getContentFragmentLayout());
        }

        return content_fragment_layout_container;
    }

    protected SupportToolbar2 toolbar;

    /**
     *  Get the activity's toolbar.
     */
    public SupportToolbar2 getToolbar()
    {
        if (toolbar == null)
        {
            toolbar = new SupportToolbar2(getContext());
        }

        return toolbar;
    }

    protected LinearLayout top_accessory_view;

    /**
     *  Get the activity's top accessory view.
     */
    public LinearLayout getTopAccessoryView()
    {
        if (top_accessory_view == null)
        {
            top_accessory_view = new LinearLayout(getContext());
            top_accessory_view.setOrientation(LinearLayout.VERTICAL);
            top_accessory_view.setLayoutParams(
                new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
        }

        return top_accessory_view;
    }

    protected LinearLayout bottom_accessory_view;

    /**
     *  Get the activity's top accessory view.
     */
    public LinearLayout getBottomAccessoryView()
    {
        if (bottom_accessory_view == null)
        {
            bottom_accessory_view = new LinearLayout(getContext());
            bottom_accessory_view.setOrientation(LinearLayout.VERTICAL);
            bottom_accessory_view.setLayoutParams(
                new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
        }

        return bottom_accessory_view;
    }

    protected SupportFabStrip fab_strip;

    /**
     *  Get the activity's floating action button strip.
     */
    public SupportFabStrip getFabStrip()
    {
        if (fab_strip == null)
        {
            fab_strip = new SupportFabStrip(getContext());
            fab_strip.setLayoutParams(
                new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT) {{
                        this.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        this.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                }});
        }

        return fab_strip;
    }

    //
    //
    //
    //
    //
    //
    //

	@Override
	public void onCreate(Bundle b)
	{
		super.onCreate(b);
		new AsyncTask<Void, Void, Void>()
		{
			@Override
			public Void doInBackground(Void... v)
			{
				//
				//	Do something in the background first.
				//
				SupportActivity
					.this
					.doInBackground();

				return null;
			}

			@Override
			public void onPostExecute(Void v)
			{
				//
				//	Continue with the creating.
				//
				onCreate();
			}
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

    /**
     *  Can be overridden to do work in the background before the activity's UI is built.
     */
	public void doInBackground()
	{
	}

    /**
     *  Called after the background work to resume building the activity's UI.
     *  Should be overridden instead of onCreate(Bundle).
     */
	public void onCreate()
	{
		getWindow().setSoftInputMode(
			WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
			WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		//
        //
        //
		
		setContentView(getLayout());

		getFragmentManager()
			.beginTransaction()
			.add(getMenuFragmentLayout().getId(),       new Fragment(), "menu")
			.add(getContentFragmentLayout().getId(),	new Fragment(), "content")
			.commit();

		setBackgroundColor(SupportColors.getBackgroundColor(getContext()));
	}

	//
	//
	//
	//	Area: Behavior
	//
	//
	//

    /**
     *  Tells this activity to either respect or ignore orientation changes.
     */
	public void setOrientationLocked(boolean value)
	{
		if (value)	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		else 		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
	}

    /**
     *  Sets whether or not this activity should be used in fullscreen mode or not.
     */
	public void setFullscreen(boolean value)
	{
		if (value)
		{
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

			getWindow()
				.getDecorView()
				.setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
					View.SYSTEM_UI_FLAG_FULLSCREEN);
		}
		else
		{
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
	}

    /**
     *  Changes the visibility of the activity's top toolbar.
     */
	public void setToolbarHidden(boolean value)
	{
		if (value)
		{
			Animation a = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_top);

			a.setAnimationListener(
				new Animation.AnimationListener()
				{
					@Override
					public void onAnimationEnd(Animation animation)
					{
						getToolbar().setVisibility(View.GONE);
					}

					@Override public void onAnimationStart(Animation animation)		{}
					@Override public void onAnimationRepeat(Animation animation)	{}
				});

			getToolbar().startAnimation(a);
		}
		else
		{
			getToolbar().setVisibility(View.VISIBLE);
			getToolbar().startAnimation(
				AnimationUtils.loadAnimation(
						getContext(),
						R.anim.slide_in_top));
		}
	}

    /**
     *  Sets whether or not the keyboard resizes or overlaps the activity's content view.
     */
	public void setResizeOnKeyboardShown(boolean value)
	{
		if (value)
			getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		else
			getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	//
	//
	//
	//	Area: Back button behavior
	//
	//
	//

	public enum BackButtonBehavior
	{
		FINISH_ACTIVITY,
		MINIMIZE_ACTIVITY,
		DEFAULT_BEHAVIOR
	}

	private Runnable back_action;
	private BackButtonBehavior back_button_behavior;

    /**
     *  Gets the runnable that is called when the back button is pressed.
     */
	public Runnable getBackAction()
	{
		return back_action;
	}

    /**
     *  Sets the runnable to be called before the back button returns to its default behavior.
     */
	public void setBackAction(Runnable runnable)
	{
		back_action = runnable;
	}

    /**
     *  Determines if the activity's back action is not null.
     */
	public boolean hasBackAction()
	{
		return back_action != null;
	}

    /**
     *  Gets the behavior of the back button when no back action is assigned.
     */
	public BackButtonBehavior getBackButtonBehavior()
	{
		if (back_button_behavior == null)
			back_button_behavior = BackButtonBehavior.DEFAULT_BEHAVIOR;

		return back_button_behavior;
	}

    /**
     *  Sets the behavior of the back button when no back action is assigned.
     */
	public void setBackButtonBehavior(BackButtonBehavior behavior)
	{
		back_button_behavior = behavior;
	}

	@Override
	public void onBackPressed()
	{
		if (isMenuOpen())
		{
			closeMenu();
		}
		else
		{
			if (hasBackAction())
			{
				getBackAction().run();
				setBackAction(null);
			}
			else
			{
				switch (getBackButtonBehavior())
				{
					case FINISH_ACTIVITY:
						finish();
						break;

					case MINIMIZE_ACTIVITY:
						moveTaskToBack(false);
						break;

					case DEFAULT_BEHAVIOR:
						super.onBackPressed();
						break;
				}
			}
		}
	}

	//
	//
	//
	//	Area: Appearance
	//
	//
	//

    /**
     *  Sets the activity's toolbar title.
     */
	public void setTitle(final String title)
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				getToolbar().setTitle(title);
			}
		});
	}

    /**
     *  Sets the activity's subtitle that appears below the toolbar's title.
     */
	public void setSubtitle(final String subtitle)
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				getToolbar().setSubtitle(subtitle);
			}
		});
	}

    /**
     *  Sets the desired background color of the activity.
     */
	public void setBackgroundColor(final int background_color)
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				int color = background_color;
				if (SupportColors.isLight(color))
						color = SupportColors.subtract(color);
				else 	color = SupportColors.subtract(color);
				getLayout().setBackgroundColor(color);
			}
		});
	}

    /**
     *  Sets the desired text color, background color, and appearance of the activity's toolbar.
     */
	public void setToolbarColor(final int foreground_color, final int background_color, final boolean flat)
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				getToolbar().setColors(
					foreground_color,
					background_color);

				if (flat)
				{
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
						getWindow().setStatusBarColor(background_color);
				}
				else
				{
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
						getWindow().setStatusBarColor(SupportColors.subtract(background_color, 0x0F));
				}

				if (SupportColors.isLight(background_color))
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
						getWindow()
							.getDecorView()
							.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
			}
		});
	}

    /**
     *  Sets the desired background color and the appearance of the activity's toolbar.
     */
	public void setToolbarColor(int color, boolean flat)
	{
		setToolbarColor(
			SupportColors.isLight(color)?
				SupportColors.subtract(color, 0x7F) :
				SupportColors.get("white"),
			color,
			flat);
	}

    /**
     *  Sets the desired background color of the activity's toolbar.
     */
	public void setToolbarColor(int color)
	{
		setToolbarColor(
			color,
			false);
	}

    /**
     *  Sets the activity's toolbar elevation. Supports pre-material design versions of Android.
     */
	public void setToolbarElevation(final float elevation)
	{
        SupportShadow.addTo(
            getContentFragmentLayoutContainer(),
            null,
            SupportShadow.ShadowDirection.DOWN,
            elevation,
            "toolbarshadow");
    }

	/**
     *  Sets the activity's drawer elevation. Supports pre-material design versions of Android.
     */
	public void setDrawerElevation(final float elevation)
	{
		SupportShadow.addTo(
            getMenuLayout(),
            null,
            SupportShadow.ShadowDirection.RIGHT,
            elevation,
            "drawershadow");
	}

	//
	//
	//
	//	Area: Interaction
	//
	//
	//

    /**
     *  Configure the navigation button to open and close the activity's drawer.
     */
	public void setNavigationButtonAsMenuToggle()
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				getToolbar().setNavigationButton(
					R.drawable.ic_menu,
					new Runnable()
					{
						@Override
						public void run()
						{
							if (isMenuOpen())
							{
								closeMenu();
								return;
							}

							openMenu();
						}
					});
			}
		});
	}

    /**
     *  Configure the navigation button to act as the back button in Android.
     */
	public void setNavigationButtonAsBack()
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				getToolbar().setNavigationButton(
					R.drawable.ic_arrow_back,
					new Runnable()
					{
						@Override
						public void run()
						{
							onBackPressed();
						}
					});
			}
		});
	}

	/**
     *  Configure the navigation button to close the activity.
     */
	public void setNavigationButtonAsClose()
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				getToolbar().setNavigationButton(
					R.drawable.ic_close,
					new Runnable()
					{
						@Override
						public void run()
						{
							finish();
						}
					});
			}
		});
	}

    /**
     *  Configure the navigation button to do something else.
     */
	public void setNavigationButton(final int icon, final Runnable runnable)
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				getToolbar().setNavigationButton(
					icon,
					runnable);
			}
		});
	}

    /**
     *  Adds a toolbar action docked to the right of the activity's toolbar.
     */
	public void addAction(final String name, final int icon, final Runnable runnable)
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				getToolbar().addAction(
					name,
					icon,
					runnable);
			}
		});
	}

    /**
     *  Adds a floating action button docked above and to the bottom right of the activity's content view.
     */
	public SupportFab addFloatingAction(int icon, int foreground_color, int background_color, final Runnable runnable)
	{
		SupportFab button =
			new SupportFab(getContext())
				.setColors(foreground_color, background_color)
				.setIcon(icon)
				.setAction(runnable);

		getFabStrip().addAction(button);
		return button;
	}

	/**
     *  Adds a floating action button docked above and to the bottom right of the activity's content view.
     */
	public SupportFab addFloatingAction(int icon, int color, final Runnable runnable)
	{
		SupportFab button =
			new SupportFab(getContext())
				.setColor(color)
				.setIcon(icon)
				.setAction(runnable);

		getFabStrip().addAction(button);
		return button;
	}

	/**
     *  Adds a floating action button docked above and to the bottom right of the activity's content view.
     *  This method uses the same color palette as the activity's toolbar.
     */
	public SupportFab addFloatingAction(int icon, final Runnable runnable)
	{
		SupportFab button =
			new SupportFab(getContext())
				.setColors(
					getToolbar().getToolbarForegroundColor(),
					getToolbar().getToolbarBackgroundColor())
				.setIcon(icon)
				.setAction(runnable);

		getFabStrip().addAction(button);
		return button;
	}

	//
	//
	//
	//	Area: Interaction cooldown
	//
	//
	//

	private boolean interacted			= false;
	private Handler interaction_handler = null;
	private int interaction_threshold	= 0;

	public void onInteract()
	{
		interaction_threshold++;
		if (interaction_threshold < 5)
			return;

		//
		//	Set up handler for interaction cooldown.
		//
		if (interaction_handler == null)
		{
			interaction_handler = new Handler()
			{
				public void handleMessage(Message message)
				{
					if (message.what == 0)
					{
						onInteractStop();
						interacted = false;
					}
				}
			};
		}

		interaction_handler.removeMessages(0);
		interaction_handler.sendEmptyMessageDelayed(0, 3000);

		//
		//
		//

		if (!interacted)
		{
			onInteractStart();
			interacted				= true;
			interaction_threshold	= 0;
		}
	}

	public void onInteractStart()
	{
	}

	public void onInteractStop()
	{
	}

	//
	//
	//
	//	Area: Fragment management
	//
	//
	//

    private static int pending_attach_animation = 0;
	private static int pending_detach_animation = 0;

	public static void setPendingAnimations(int attach_animation, int detach_animation)
	{
		pending_attach_animation = attach_animation;
		pending_detach_animation = detach_animation;
	}

	public Fragment setFragment(int id, Fragment fragment, String name)
	{
		//
		//	Pass the intent extras to this fragment if the arguments are not already set.
		//
		if (fragment.getArguments() == null)
			if (getIntent() != null && getIntent().getExtras() != null)
				fragment.setArguments(getIntent().getExtras());

		//
		//	Pass the view ID and fragment name to the instance of this fragment.
		//
		if (fragment instanceof SupportAsyncFragment)
		{
			SupportAsyncFragment async_fragment = (SupportAsyncFragment) fragment;
			Bundle data							= async_fragment.getData();

			data.putInt("view_id",			id);
			data.putString("fragment_name", name);

			async_fragment.setData(data);
		}

		//
		//	Then, actually replace the view with the fragment.
		//
		getFragmentManager()
			.beginTransaction()
            .setCustomAnimations(
                pending_attach_animation,
                pending_detach_animation)
			.replace(id, fragment, name)
			.commit();

		pending_attach_animation = 0;
		pending_detach_animation = 0;
		return fragment;
	}

    /**
     *  Sets and applies a fragment to the insets of the activity's drawer menu.
     */
	public Fragment setMenuFragment(Fragment fragment)
	{
		return setFragment(
			getMenuFragmentLayout().getId(),
			fragment,
			"menu");
	}

	/**
     *  Sets and applies a fragment to the insets of the activity's content view.
     */
	public Fragment setContentFragment(Fragment fragment)
	{
		return setFragment(
			getContentFragmentLayout().getId(),
			fragment,
			"content");
	}

	//
	//
	//
	//	Area: Menu
	//
	//
	//

    /**
     *  Forcibly opens the activity's drawer.
     */
	public void openMenu()
	{
		if (!isMenuOpen())
		{
			//clearFocus();
			//getDrawerLayout().requestFocus();

			getDrawerLayout()
                .openDrawer(Gravity.START);
		}
	}

	/**
     *  Forcibly closes the activity's drawer.
     */
	public void closeMenu()
	{
	    if (isMenuOpen())
		{
			//clearFocus();
			//getDrawerLayout().requestFocus();

			getDrawerLayout()
                .closeDrawer(Gravity.START);
		}
	}

	/**
     *  Determines if either the activity's drawer is open or closed.
     */
	public boolean isMenuOpen()
	{
	    return
        getDrawerLayout()
            .isDrawerOpen(Gravity.START);
	}

	//
	//
	//
	//	Area: Other
	//
	//
	//

	public void requestFocus()
	{
		getDrawerLayout()
			.requestFocus();
	}

	public void clearFocus()
	{
		//
		//	If the keyboard is showing, close it!
		//
		View v = getCurrentFocus();

		if (v != null)
		{
			v.clearFocus();
			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle bundle)
	{
	}

	@Override
	public void onRestoreInstanceState(Bundle bundle)
	{
	}

	@Override
	public void onConfigurationChanged(Configuration conf)
	{
		super.onConfigurationChanged(conf);
	}

	@Override
	public void onPause()
	{
		/*
		overridePendingTransition(
			R.anim.stay_put,
			R.anim.slide_out_bottom);
		*/

		clearFocus();
		super.onPause();
	}

	//
	//
	//
	//	Area: Dialogs
	//
	//
	//

	private Dialog loading_dialog;

	public Dialog getLoadingDialog()
	{
		if (loading_dialog == null)
		{
			loading_dialog = new Dialog(getContext());
			loading_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			loading_dialog.setCancelable(false);
			loading_dialog.setContentView(getLoadingDialogView());
		}

		return loading_dialog;
	}

	private SupportMenuView loading_dialog_view;

	public SupportMenuView getLoadingDialogView()
	{
	    if (loading_dialog_view == null)
        {
            loading_dialog_view	= new SupportMenuView(getContext());

            GradientDrawable
                d = new GradientDrawable();
			    d.setColor(SupportColors.getBackgroundColor(getContext()));

			loading_dialog_view.setBackgroundDrawable(d);
			loading_dialog_view.addItem(
				new SupportMenuItemView(getContext())
					.setLeftIcon(R.drawable.ic_loading)
                    .setLeftIconAnimation(R.anim.rotate_indefinitely));
        }

		return loading_dialog_view;
	}

	public void setLoadingMessage(final String message)
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
			    if (message == null)
                {
                    if (getLoadingDialog().isShowing())
                    {
                        getLoadingDialog().dismiss();
                    }

                    return;
                }

				getLoadingDialogView()
                    .getLastAddedItem()
                    .setTitle(message);

				getLoadingDialog()
                    .show();
			}
		});
	}

	public void removeLoadingMessage()
	{
		setLoadingMessage(null);
	}

	public void showDialog(
	    final int icon,
        final String title,
        final String message,
        final String negative_answer,
        final Runnable negative_action,
        final String positive_answer,
        final Runnable positive_action,
        final boolean allow_input,
        final boolean force)
	{
		SupportInput.setLastTouchX(SupportDevice.screenWidth() / 2);
		SupportInput.setLastTouchY(SupportDevice.screenHeight() / 2);

		runOnUiThread(new Runnable()
		{
			Dialog d;

			@Override
			public void run()
			{
				SupportMenuView list_view = new SupportMenuView(getContext());

				//
				//
				//

				if (title != null && title.length() > 0)
				{
					SupportMenuItemView message_toolbar = new SupportMenuItemView(getContext());

					message_toolbar.setTitle(title);

					if (icon != 0)
                        message_toolbar.setLeftIcon(icon);

					list_view.addItem(message_toolbar);
				}

				if (allow_input)
				{
                    SupportApplication.setString("_input", "");

                    list_view.addItem(
						new SupportMenuItemView(getContext())
							.setSubtitle(message));

					list_view.addItem(
						new SupportMenuItemView(getContext())
							.setStringPreference("_input")
							.setHint("Enter text"));
				}
				else
				{
					list_view.addItem(
						new SupportMenuItemView(getContext())
							.setSubtitle(message));
				}

				//
				//
				//

				SupportMenuItemView
					answers = new SupportMenuItemView(getContext());

				ArrayList<SupportButton> buttons = new ArrayList<SupportButton>();

				if (negative_answer != null && negative_answer.length() > 0)
				{
				    buttons.add(
				        new SupportButton(getContext())
                            .setLabel(negative_answer)
                            .setColors(
                                SupportColors.getForegroundColor(getContext()),
                                SupportColors.getBackgroundColor(getContext()))
                            .setAction(
                                new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        if (negative_action != null)
                                            negative_action.run();

                                        d.dismiss();
                                    }
                                }));
				}
				else if (negative_action != null)
				{
					d.setOnDismissListener(
						new DialogInterface.OnDismissListener()
						{
							@Override
							public void onDismiss(DialogInterface di)
							{
								negative_action.run();
							}
						});
				}

				if (positive_answer != null && positive_answer.length() > 0)
				{
					buttons.add(
				        new SupportButton(getContext())
                            .setLabel(positive_answer)
                            .setColors(
                                SupportColors.getAccentColor(getContext()),
                                SupportColors.getBackgroundColor(getContext()))
                            .setAction(
                                new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        if (positive_action != null)
                                            positive_action.run();

                                        d.dismiss();
                                    }
                                }));
				}
				else
				{
					buttons.add(
				        new SupportButton(getContext())
                            .setLabel("Close")
                            .setColors(
                                SupportColors.getAccentColor(getContext()),
                                SupportColors.getBackgroundColor(getContext()))
                            .setAction(
                                new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        if (positive_action != null)
                                            positive_action.run();

                                        d.dismiss();
                                    }
                                }));
				}

				answers.setButtonActions(buttons.toArray(new SupportButton[buttons.size()]));
				list_view.addItem(answers);

				//
				//
				//

				d = list_view.showAsMenu(!force);
			}
		});
	}

	public void showDialog(String message)
	{
		showDialog(
			R.drawable.ic_info_outline,
			null,
			message,
			null,
			null,
			null,
			null,
			false,
			false);
	}

	public void showDialog(String message, Runnable runnable)
	{
		showDialog(
			R.drawable.ic_info_outline,
			null,
			message,
			null,
			null,
			"Continue",
			runnable,
			false,
			true);
	}

	public void showRetryDialog(String message, Runnable runnable)
	{
		showDialog(
			R.drawable.ic_warning,
			"Attention",
			message,
			null,
			null,
			"Retry",
			runnable,
			false,
			true);
	}

	public void showRetryDialog(String message, Runnable runnable, boolean cancellable)
	{
		showDialog(
			R.drawable.ic_warning,
			"Attention",
			message,
			null,
			null,
			"Retry",
			runnable,
			false,
			!cancellable);
	}

	public void showInputDialog(String message, final Runnable runnable)
	{
		showDialog(
			R.drawable.ic_keyboard,
			"Input",
			message,
			null,
			null,
			"Submit",
			runnable,
			true,
			false);
	}

	//
	//	Shows a toast to the user for four (4) lines less.
	//
	public void showToast(final String message)
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				Toast.makeText(
					getContext(),
					message,
					Toast.LENGTH_LONG
				).show();
			}
		});
	}

	//
	//
	//
	//	Area: Permissions
	//
	//
	//

	protected Runnable on_permission_granted;
    protected Runnable on_permission_denied;

	public void requestPermissions(String[] permissions, Runnable on_granted, Runnable on_denied)
    {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
		{
			if (on_granted != null)
				getView().postDelayed(on_granted, 250);

			return;
		}

		boolean skip = true;

		for (String s : permissions)
			if (ContextCompat.checkSelfPermission(this, s) == PackageManager.PERMISSION_DENIED)
				skip = false;

		if (skip)
		{
			if (on_granted != null)
				getView().postDelayed(on_granted, 250);

			return;
		}

        this.on_permission_granted = on_granted;
        this.on_permission_denied = on_denied;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        	requestPermissions(permissions, 0);
    }

    @Override
    public void onRequestPermissionsResult(int code, String[] permissions, int[] values)
    {
        boolean granted = true;

        for (int i = 0; i < values.length; i++)
        {
			//
			//	If any one of our answers were negative
			//	We will simply insult the user by treating them
			//	All as denied.
			//
            if (values[i] == PackageManager.PERMISSION_DENIED)
                granted = false;
        }

        if (granted)
        {
			//
			//	Actually do what we want our app to do when 100% of the
			//	Requested permissions are granted.
			//
            if (on_permission_granted != null)
                getView().postDelayed(on_permission_granted, 250);
        }
        else
        {
			//
			//	Otherwise, treat it as denying.
			//
            if (on_permission_denied != null)
                getView().postDelayed(on_permission_denied, 250);
        }
    }

    //
	//
	//
	//	Area: Results
	//
	//
	//

    private HashMap<Integer, Runnable> result_success_actions	= new HashMap<>();
	private HashMap<Integer, Runnable> result_failure_actions	= new HashMap<>();
	private HashMap<Integer, Bundle> result_bundle				= new HashMap<>();
	private int last_request_code								= 0;

	public Bundle getResultBundle()
	{
		//
		//	Get the last result bundle
		//
		return
        result_bundle
            .get(last_request_code);
	}

    @Override
	public void onActivityResult(int request_code, int result_code, Intent data)
	{
		switch (result_code)
		{
			case RESULT_OK:
				if (result_success_actions.containsKey(request_code))
				{
					if (data != null && data.getExtras() != null)
						result_bundle.put(request_code, data.getExtras());

					result_success_actions.get(request_code).run();
				}
				break;

			case RESULT_CANCELED:
				if (result_failure_actions.containsKey(request_code))
				{
					if (data != null && data.getExtras() != null)
						result_bundle.put(request_code, data.getExtras());

					result_failure_actions.get(request_code).run();
				}
				break;
		}
	}

	public void startActivityForResult(Intent intent, Object bundle, Runnable success_action, Runnable failure_action)
	{
		last_request_code = SupportMath.randomInt(65535);

		if (bundle != null)
			intent.putExtras(SupportApplication.toBundle(bundle));

		if (success_action != null) result_success_actions.put(last_request_code, success_action);
		if (failure_action != null) result_failure_actions.put(last_request_code, failure_action);

		this.startActivityForResult(intent, last_request_code);
	}

    public void startActivityForResult(Class<?> activity_class, Object bundle, Runnable success_action, Runnable failure_action)
	{
		startActivityForResult(
			new Intent(this, activity_class),
			SupportApplication.toBundle(bundle),
			success_action,
			failure_action);
	}

	public void startActivityForResult(Class<?> activity_class, Runnable success_action)
	{
		startActivityForResult(
			new Intent(this, activity_class),
			null,
			success_action,
			null);
	}

	public void finishWithResult(Object bundle)
	{
		Intent intent = getIntent();

		if (bundle != null)
			intent.putExtras(SupportApplication.toBundle(bundle));

		setResult(RESULT_OK, intent);
		finish();
	}

	//
	//
	//
	//	Area: Views
	//
	//
	//

	public DrawerLayout getView()
	{
		return drawer_layout;
	}

	public void post(Runnable runnable)
	{
		//
		//	Quick access to a post method.
		//
		getView().post(runnable);
	}

	//
	//
	//
	//	Area: Intent data
	//
	//
	//

    protected Bundle data;

	public Bundle getData()
	{
		if (data == null)
		{
			data = new Bundle();

			if (getIntent() != null)
				if (getIntent().getExtras() != null)
					data = getIntent().getExtras();
		}

		return data;
	}

	public void setData(Bundle data)
	{
		this.data = data;
	}

	public String getIntentAction()
	{
		return
			getIntent() != null &&
			getIntent().getAction() != null &&
			getIntent().getAction().length() > 0?
				getIntent().getAction() :
				"";
	}

	public boolean isIntent(String action)
	{
		return getIntentAction().equals(action);
	}

	public File getFileFromIntent(Intent intent)
	{
		File file = null;

		if (intent.hasExtra("path"))
		{
			//
			//	Prefer getting the file from the path
			//
			file = new File(intent.getStringExtra("path"));
		}
		else
		{
			try
			{
				//
				//	Otherwise, copy it and work from there
				//
				InputStream i = getContentResolver().openInputStream(intent.getData());

				file = File.createTempFile("cached", ".tmp");
				file.deleteOnExit();

				IOUtils.copy(
					i,
					new FileOutputStream(file));
			}
			catch (Exception x) {}
		}

		return file;
	}
}
