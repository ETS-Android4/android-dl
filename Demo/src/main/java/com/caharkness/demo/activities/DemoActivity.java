package com.caharkness.demo.activities;

import android.content.Context;
import android.os.Bundle;

import com.caharkness.demo.DemoApplication;
import com.caharkness.demo.R;
import com.caharkness.demo.fragments.DemoFragment;
import com.caharkness.demo.Setting;
import com.caharkness.support.activities.SupportActivity;
import com.caharkness.support.utilities.SupportColors;
import com.caharkness.support.views.SupportMenuItemView;
import com.caharkness.support.views.SupportMenuView;

import org.json.JSONObject;

@SuppressWarnings("ConstantConditions")
public class DemoActivity extends SupportActivity
{
    @Override
	public Context getContext()
	{
		return
        SupportColors.context(
			super.getContext(),
            Setting.DEMO_UI_FG.getValueAsInteger(),
			Setting.DEMO_UI_BG.getValueAsInteger(),
			Setting.DEMO_UI_TINT.getValueAsInteger());
	}

	private DemoFragment demo_fragment;

	public DemoFragment getDemoFragment()
    {
        if (demo_fragment == null)
            demo_fragment = new DemoFragment();

        return demo_fragment;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        this.setToolbarColor(
            Setting.DEMO_TITLE_FG.getValueAsInteger(),
            Setting.DEMO_TITLE_BG.getValueAsInteger(),
            Setting.DEMO_TITLE_FLAT.getValueAsBoolean());

        this.setTitle("Demo");
        this.setNavigationButtonAsBack();
        this.setContentFragment(getDemoFragment());
        this.setResizeOnKeyboardShown(true);

        if (Setting.DEMO_TITLE_SHADOW.getValueAsBoolean())
            this.setToolbarElevation(1 / 32f);

        this.addAction("Options", R.drawable.ic_more_vert, new Runnable()
        {
            @Override
            public void run()
            {
                SupportMenuView options = new SupportMenuView(getContext());

                options.addItem(
                    new SupportMenuItemView(getContext())
                        .setLeftIcon(R.drawable.ic_format_clear)
                        .setTitle("Menus")
                        .setSubtitle("The same menus that you see below in fragments can exist here, too"));

                for (int i = 0; i < 50; i++)
                {
                    final int ii = i;

                    options.addItem(
                        new SupportMenuItemView(getContext())
                            .setTitle("Item " + i)
                            .setRightIcon(R.drawable.ic_chevron_right)
                            .setAction(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    DemoActivity.this.showDialog("You selected item " + ii);
                                }
                            }));
                }

                options.showAsPopup(
                    DemoActivity
                        .this
                        .getToolbar()
                        .getRightLayout());
            }
        });

        this.addFloatingAction(R.drawable.ic_question_answer, new Runnable()
        {
            @Override
            public void run()
            {
                DemoActivity.this.showDialog("Activities, independent from the fragments inside them, can have fixed \"floating\" action buttons.");
            }
        });
    }

    @Override
    public void onRestoreInstanceState(Bundle b)
    {
        int i = 0;
    }
}
