package com.caharkness.support.layouts;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

public class SupportFrameLayout extends LinearLayout
{
    private LinearLayout top_layout;

    public LinearLayout getTopLayout()
    {
        if (top_layout == null)
        {
            top_layout = new LinearLayout(getContext());
            top_layout.setOrientation(LinearLayout.VERTICAL);
            top_layout.setLayoutParams(
                new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
        }

        return top_layout;
    }

    private LinearLayout left_layout;

    public LinearLayout getLeftLayout()
    {
        if (left_layout == null)
        {
            left_layout = new LinearLayout(getContext());
            left_layout.setLayoutParams(
                new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT));
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
            center_layout.setLayoutParams(
                new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT, 1f));
        }

        return center_layout;
    }

    private LinearLayout right_layout;

    public LinearLayout getRightLayout()
    {
        if (right_layout == null)
        {
            right_layout = new LinearLayout(getContext());
            right_layout.setLayoutParams(
                new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT));
        }

        return right_layout;
    }

    private LinearLayout middle_layout;

    public LinearLayout getMiddleLayout()
    {
        if (middle_layout == null)
        {
            middle_layout = new LinearLayout(getContext());
            middle_layout.setOrientation(LinearLayout.HORIZONTAL);
            middle_layout.setLayoutParams(
                new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT, 1f));

            middle_layout.addView(getLeftLayout());
            middle_layout.addView(getCenterLayout());
            middle_layout.addView(getRightLayout());
        }

        return middle_layout;
    }

    private LinearLayout bottom_layout;

    public LinearLayout getBottomLayout()
    {
        if (bottom_layout == null)
        {
            bottom_layout = new LinearLayout(getContext());
            bottom_layout.setOrientation(LinearLayout.VERTICAL);
            bottom_layout.setLayoutParams(
                new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
        }

        return bottom_layout;
    }

    public SupportFrameLayout(Context c, boolean expand)
    {
        super(c);
        setOrientation(LinearLayout.VERTICAL);

        if (expand)
        {
            setLayoutParams(
                new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
        }
        else
        {
            setLayoutParams(
                new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
        }

        addView(getTopLayout());
        addView(getMiddleLayout());
        addView(getBottomLayout());
    }

    public SupportFrameLayout addViewTop(View... views)
    {
        for (View v : views)
            getTopLayout().addView(v);

        return this;
    }

    public SupportFrameLayout setViewTop(View... views)
    {
        getTopLayout()
            .removeAllViews();

        return addViewTop(views);
    }

    public SupportFrameLayout addViewLeft(View... views)
    {
        for (View v : views)
            getLeftLayout().addView(v);

        return this;
    }

    public SupportFrameLayout setViewLeft(View... views)
    {
        getLeftLayout()
            .removeAllViews();

        return addViewLeft(views);
    }

    public SupportFrameLayout addViewCenter(View... views)
    {
        for (View v : views)
            getCenterLayout().addView(v);

        return this;
    }

    public SupportFrameLayout setViewCenter(View... views)
    {
        getCenterLayout()
            .removeAllViews();

        return addViewCenter(views);
    }

    public SupportFrameLayout addViewRight(View... views)
    {
        for (View v : views)
            getRightLayout().addView(v);

        return this;
    }

    public SupportFrameLayout setViewRight(View... views)
    {
        getRightLayout()
            .removeAllViews();

        return addViewRight(views);
    }

    public SupportFrameLayout addViewBottom(View... views)
    {
        for (View v : views)
            getBottomLayout().addView(v);

        return this;
    }

    public SupportFrameLayout setViewBottom(View... views)
    {
        getBottomLayout()
            .removeAllViews();

        return addViewBottom(views);
    }
}
