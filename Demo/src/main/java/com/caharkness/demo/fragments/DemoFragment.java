package com.caharkness.demo.fragments;

import android.view.View;

import com.caharkness.demo.DemoApplication;
import com.caharkness.demo.R;
import com.caharkness.demo.activities.DemoActivity;
import com.caharkness.support.fragments.SupportAsyncFragment;
import com.caharkness.support.utilities.SupportColors;
import com.caharkness.support.utilities.SupportMath;
import com.caharkness.support.views.SupportButton;
import com.caharkness.support.views.SupportMenuItemView;
import com.caharkness.support.views.SupportMenuView;

import java.util.Map;

public class DemoFragment extends SupportAsyncFragment
{

    public DemoFragment()
    {
        super();
    }

	@Override
	public View onCreateView()
	{
        SupportMenuView list = new SupportMenuView(getContext());

        list.addItem(
            new SupportMenuItemView(getContext())
                .setLabel("About", SupportColors.getAccentColor(getContext())));

        list.addItem(
            new SupportMenuItemView(getContext())
                .setSubtitle("Demo Library is a Beginner's Beautiful Android Application Learning Library (BBAALL for short) designed to rapidly develop Android applications in code."));

        list.addItem(
            new SupportMenuItemView(getContext())
                .setTitle("Navigate Left")
                .setSubtitle("Tap to slide a new instance of this fragment from the left")
                .setLeftIcon(R.drawable.ic_chevron_left)
                .setAction(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        navigateTo(new DemoFragment().slideInLeft());
                    }
                }));

        list.addItem(
            new SupportMenuItemView(getContext())
                .setTitle("Navigate Right")
                .setSubtitle("Tap to slide a new instance of this fragment from the right")
                .setRightIcon(R.drawable.ic_chevron_right)
                .setAction(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        navigateTo(new DemoFragment().slideInRight());
                    }
                }));

        list.addItem(
            new SupportMenuItemView(getContext())
                .setLabel("Preferences", SupportColors.getAccentColor(getContext())));

        list.addItem(
            new SupportMenuItemView(getContext())
                .setSubtitle("Menu items can be set up to store persistent values in a variety of ways."));

        SupportMenuItemView string =
            new SupportMenuItemView(getContext())
                .setTitle("String")
                .setSubtitle("String preferences can be used to store persistent string values")
                .setLeftIcon(R.drawable.ic_edit)
                .addSpacer()
                .setStringPreference("string_example")
                .setHint("Enter string value");

        list.addItem(string);

        SupportMenuItemView password =
            new SupportMenuItemView(getContext())
                .setTitle("Password")
                .setSubtitle("Like string preferences, but the value is hidden from users")
                .setLeftIcon(R.drawable.ic_lock)
                .addSpacer()
                .setStringPreferenceHidden("password_example")
                .setHint("Enter password")
                .addTag("password");

        list.addItem(password);

        list.addItem(
            new SupportMenuItemView(getContext())
                .setButtonActions(
                new SupportButton(getContext())
                    .setLabel("View")
                    .setColors(
                        SupportColors.getAccentColor(getContext()),
                        SupportColors.getBackgroundColor(getContext()))
                    .setAction(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            getSupportActivity()
                                .showDialog(DemoApplication.getString("password_example"));
                        }
                    }),
                new SupportButton(getContext())
                    .setLabel("Clear")
                    .setColors(
                        SupportColors.getForegroundColor(getContext()),
                        SupportColors.getBackgroundColor(getContext()))
                    .setAction(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            password.getFocusableEditText().setText("");
                        }
                    })));

        list.addItem(
            new SupportMenuItemView(getContext())
                .setTitle("Boolean")
                .setSubtitle("Boolean preferences can be used to store persistent boolean values")
                .setLeftIcon(R.drawable.ic_power_settings_new)
                .setBooleanPreference("boolean_example"));

        list.addItem(
            new SupportMenuItemView(getContext())
                .setTitle("Color")
                .setSubtitle("Color preferences can be pressed to select a common color from a list or long pressed to mix a color manually")
                .setLeftIcon(R.drawable.ic_palette)
                .setColorPreference("color_example"));

        final SupportMenuItemView slider = new SupportMenuItemView(getContext());

        slider
            .setTitle("Slider")
            .setSubtitle("")
            .setLeftIcon(R.drawable.ic_adjust)
            .setSliderPreference("slider_example", new Runnable()
            {
                @Override
                public void run()
                {
                    slider.setSubtitle(DemoApplication.getFloat("slider_example") + "f");
                }
            });

        list.addItem(slider);
        slider.setSubtitle(DemoApplication.getFloat("slider_example", 0.0f) + "f");

        list.addItem(
            new SupportMenuItemView(getContext())
                .setLabel("More", SupportColors.getAccentColor(getContext())));

        list.addItem(
            new SupportMenuItemView(getContext())
                .setTitle("Rotating Icons")
                .setSubtitle("You can specify icon animations of menu items to suggest activity")
                .setLeftIcon(R.drawable.ic_loading)
                .setLeftIconAnimation(R.anim.rotate_indefinitely));

        SupportMenuItemView cicons = new SupportMenuItemView(getContext());

        cicons
            .setTitle("Colorful Icons")
            .setLeftIcon(R.drawable.ic_home, SupportColors.getAccentColor(getContext()), SupportColors.getBackgroundColor(getContext()));

        list.addItem(cicons);

        list.addItem(
            new SupportMenuItemView(getContext())
                .setLeftIcon(R.drawable.ic_undo)
                .setTitle("Reset Application")
                .setAction(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        getSupportActivity().showDialog(
                            R.drawable.ic_warning,
                            "Warning",
                            "Are you sure you want to restore default settings? This will erase all stored data and return this app to its original state.",
                            "Cancel",
                            null,
                            "Reset",
                            new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    DemoApplication.resetPreferences();
                                    DemoApplication.forceRestart(DemoActivity.class);
                                }
                            },
                            false,
                            false
                        );
                    }
                }));

        list.addItem(
            new SupportMenuItemView(getContext())
                .setTable(
                    new String[] {"Name",       "Conner Harkness"},
                    new String[] {"E-mail",     "caHarkness@gmail.com"},
                    new String[] {"Website",    "caharkness.com"}));


        return list;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        getSupportActivity()
            .setSubtitle(null);
    }
}
