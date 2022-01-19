package com.caharkness.support.layouts;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class SupportRelativeLayout extends RelativeLayout
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
                    LayoutParams.WRAP_CONTENT)
                {{
                    addRule(RelativeLayout.ALIGN_PARENT_TOP);
                }});
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
                    LayoutParams.MATCH_PARENT)
                {{
                    addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                }});
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
                    LayoutParams.MATCH_PARENT));
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
                    LayoutParams.MATCH_PARENT)
                {{
                    addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                }});
        }

        return right_layout;
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
                    LayoutParams.WRAP_CONTENT)
                {{
                    addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                }});
        }

        return bottom_layout;
    }

    public SupportRelativeLayout(Context c, boolean expand)
    {
        super(c);

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

        addView(getCenterLayout());
        addView(getTopLayout());
        addView(getLeftLayout());
        addView(getRightLayout());
        addView(getBottomLayout());
    }



    public SupportRelativeLayout addViewTop(View... views)
    {
        for (View v : views)
            getTopLayout().addView(v);

        return this;
    }

    public SupportRelativeLayout setViewTop(View... views)
    {
        getTopLayout()
            .removeAllViews();

        return addViewTop(views);
    }

    public SupportRelativeLayout addViewLeft(View... views)
    {
        for (View v : views)
            getLeftLayout().addView(v);

        return this;
    }

    public SupportRelativeLayout setViewLeft(View... views)
    {
        getLeftLayout()
            .removeAllViews();

        return addViewLeft(views);
    }

    public SupportRelativeLayout addViewCenter(View... views)
    {
        for (View v : views)
            getCenterLayout().addView(v);

        return this;
    }

    public SupportRelativeLayout setViewCenter(View... views)
    {
        getCenterLayout()
            .removeAllViews();

        return addViewCenter(views);
    }

    public SupportRelativeLayout addViewRight(View... views)
    {
        for (View v : views)
            getRightLayout().addView(v);

        return this;
    }

    public SupportRelativeLayout setViewRight(View... views)
    {
        getRightLayout()
            .removeAllViews();

        return addViewRight(views);
    }

    public SupportRelativeLayout addViewBottom(View... views)
    {
        for (View v : views)
            getBottomLayout().addView(v);

        return this;
    }

    public SupportRelativeLayout setViewBottom(View... views)
    {
        getBottomLayout()
            .removeAllViews();

        return addViewBottom(views);
    }
}
