package com.caharkness.demo;

import com.caharkness.demo.DemoApplication;
import com.caharkness.demo.DemoApplication;
import com.caharkness.demo.R;
import com.caharkness.support.utilities.SupportColors;

public enum Setting
{
    DEMO_TITLE_BG(R.drawable.ic_style,                   "Title bar background color",   SupportColors.get("red")),
    DEMO_TITLE_FG(R.drawable.ic_format_color_text,       "Title bar text color",         SupportColors.get("white")),
    DEMO_TITLE_FLAT(R.drawable.ic_publish,               "Flatten title bar",            true),
    DEMO_TITLE_SHADOW(R.drawable.ic_flip_to_front,       "Title bar drop shadow",        true),
    DEMO_UI_BG(R.drawable.ic_format_paint,               "Interface background color",   SupportColors.get("white")),
    DEMO_UI_FG(R.drawable.ic_format_color_text,          "Interface text color",         SupportColors.get("material dark")),
    DEMO_UI_TINT(R.drawable.ic_palette,                  "Interface accent color",       SupportColors.get("red"));

    private Integer icon;
    private String desc;
    private Object def;

    private Setting(Integer icon, String desc, Object def)
    {
        this.icon = icon;
        this.desc = desc;
        this.def = def;

        DemoApplication.getString(
            getId(),
            def.toString());
    }

    public String getId()
    {
        return
        this
            .name()
            .toLowerCase();
    }

    public Integer getIcon()
    {
        return icon;
    }

    public String getDescription()
    {
        return desc;
    }

    public Object getDefaultValue()
    {
        return def;
    }

    public String getValueAsString()
    {
        return
        DemoApplication.getString(
            getId(),
            (String) getDefaultValue());
    }

    public Boolean getValueAsBoolean()
    {
        return
        DemoApplication.getBoolean(
            getId(),
            (Boolean) getDefaultValue());
    }

    public Integer getValueAsInteger()
    {
        return
        DemoApplication.getInt(
            getId(),
            (Integer) getDefaultValue());
    }

    public Double getValueAsDouble()
    {
        return
        DemoApplication.getDouble(
            getId(),
            (Double) getDefaultValue());
    }

    public Float getValueAsFloat()
    {
        return
        DemoApplication.getFloat(
            getId(),
            (Float) getDefaultValue());
    }
}