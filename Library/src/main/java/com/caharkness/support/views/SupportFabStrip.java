package com.caharkness.support.views;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class SupportFabStrip extends LinearLayout
{
    private Context context;
    private LinearLayout container;

    public SupportFabStrip(Context context)
    {
        super(context);
        this.context = context;

        container = new LinearLayout(context);
        container.setGravity(Gravity.RIGHT);
        container.setLayoutParams(
            new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        addView(container);
        setLayoutParams(
            new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public ArrayList<SupportFab> getActionButtons()
    {
        ArrayList<SupportFab> buttons = new ArrayList<>();

        for (int i = 0; i < container.getChildCount(); i++)
            if (container.getChildAt(i) instanceof SupportFab)
                buttons.add((SupportFab) container.getChildAt(i));

        return buttons;
    }

    public ArrayList<SupportFab> getActionButtonsWithTag(String tag)
    {
        ArrayList<SupportFab> buttons = new ArrayList<>();

        for (SupportFab button : getActionButtons())
            if (button.hasTag(tag))
                buttons.add(button);

        return buttons;
    }

    public SupportFabStrip addAction(SupportFab button)
    {
        container.addView(button);

        bringToFront();
        return this;
    }
}
