package com.caharkness.support.utilities;

import android.content.Context;
import android.util.TypedValue;
import java.util.LinkedHashMap;

@SuppressWarnings("all")
public class SupportColors
{
	//
	//
	//
	//	Area: Context styles
	//
	//
	//

	private static int foreground_color	= 0;
	private static int background_color	= 0;
	private static int accent_color		= 0;

    /**
     *  Get the foreground color from a context.
     */
	public static int getForegroundColor(Context context)
	{
		if (foreground_color != 0)
			return foreground_color;

		final TypedValue value = new TypedValue();

		context
			.getTheme()
			.resolveAttribute(
				android.R.attr.colorForeground,
				value,
				true);

		return value.data;
	}

	public static void setForegroundColor(int color)
	{
		foreground_color = color;
	}

	/**
     *  Get the background color from a context.
     */
	public static int getBackgroundColor(Context context)
	{
		if (background_color != 0)
			return background_color;

		final TypedValue value = new TypedValue();

		context
			.getTheme()
			.resolveAttribute(
				android.R.attr.colorBackground,
				value,
				true);

		return value.data;
	}

	public static void setBackgroundColor(int color)
	{
		background_color = color;
	}

	public static int getAccentColor(Context context)
	{
		if (accent_color != 0)
			return accent_color;

		final TypedValue value = new TypedValue();

		context
			.getTheme()
			.resolveAttribute(
				android.R.attr.colorAccent,
				value,
				true);

		return value.data;
	}

	public static void setAccentColor(int color)
	{
		accent_color = color;
	}

    /**
     *  A context wrapper that assigns application-wide theme colors at the time the context is referenced.
     *  For example, all view instantiations that access a call to a wrapped getContext() will have these colors.
     */
	public static Context context(Context context, int foreground_color, int background_color, int accent_color)
	{
		setForegroundColor(foreground_color);
		setBackgroundColor(background_color);
		setAccentColor(accent_color);
		return context;
	}

	//
	//
	//
	//	Area: Color getting and setting
	//
	//
	//

	private static LinkedHashMap<String, Integer> color_map = new LinkedHashMap<>();



    /**
     *  Returns a linked hash map of all colors and their names known to your app and this library.
     */
	public static LinkedHashMap<String, Integer> getColorMap()
	{
	    if (color_map == null)
             color_map = new LinkedHashMap<>();

		return color_map;
	}

    /**
     *  Returns the integer value of a color by its name found in the linked hash map of colors.
     */
	public static int get(String name)
	{
		if (getColorMap().containsKey(name))
			return getColorMap().get(name);

		return 0;
	}

	/**
     *  Assign a name to a color and its integer value to later be accessed anywhere in your app.
     */
	public static void set(String name, int value)
	{
		getColorMap()
            .put(
                name,
                value);
	}

	//
	//
	//
	//	Area: Color mixing
	//
	//
	//

    /**
     *  Brighten a color by its integer value and the amount to increase each color by.
     */
	public static int add(int color, int level)
	{
		int a = (color >> 24)	& 0xFF;
		int r = (color >> 16)	& 0xFF;
		int g = (color >> 8)	& 0xFF;
		int b = color			& 0xFF;

		r = r >= (0xFF - level)? 0xFF : r + level;
		g = g >= (0xFF - level)? 0xFF : g + level;
		b = b >= (0xFF - level)? 0xFF : b + level;

		return (a << 24) + (r << 16) + (g << 8) + b;
	}

    /**
     *  Brighten a named color by the specified level.
     */
	public static int add(String name, int level)
	{
		return add(get(name), level);
	}

    /**
     *  Brighten a color by the default level.
     */
	public static int add(int color)
	{
		return add(color, 0x07);
	}

    /**
     *  Brighten a named color by the default level.
     */
	public static int add(String name)
	{
		return add(get(name));
	}

    /**
     *  Darken a color by the specified level.
     */
	public static int subtract(int color, int level)
	{
		int a = (color >> 24)	& 0xFF;
		int r = (color >> 16)	& 0xFF;
		int g = (color >> 8)	& 0xFF;
		int b = color			& 0xFF;

		r = r >= level? r - level : 0;
		g = g >= level? g - level : 0;
		b = b >= level? b - level : 0;

		return (a << 24) + (r << 16) + (g << 8) + b;
	}

    /**
     *  Darken a named color by the specified level.
     */
	public static int subtract(String name, int level)
	{
		return subtract(get(name), level);
	}

    /**
     *  Darken a color by the default level.
     */
	public static int subtract(int color)
	{
		return subtract(color, 0x07);
	}

    /**
     *  Darken a named color by the default level.
     */
	public static int subtract(String name)
	{
		return subtract(get(name));
	}

    /**
     *  Make a color opaque.
     */
	public static int opaque(int color)
	{
		int a = 0xFF;
		int r = (color >> 16)	& 0xFF;
		int g = (color >> 8)	& 0xFF;
		int b = color			& 0xFF;

		return (a << 24) + (r << 16) + (g << 8) + b;
	}

    /**
     *  Make a named color opaque.
     */
	public static int opaque(String name)
	{
		return opaque(get(name));
	}

	/**
     *  Make a color translucent by halving its opacity.
     */
	public static int translucent(int color, int level)
	{
		int a = (color >> 24)	& 0xFF;
		int r = (color >> 16)	& 0xFF;
		int g = (color >> 8)	& 0xFF;
		int b = color			& 0xFF;

		a = a >= level? a - level : 0;

		return (a << 24) + (r << 16) + (g << 8) + b;
	}

    /**
     *  Make a color translucent by halving its opacity.
     */
	public static int translucent(int color)
	{
		int a = (color >> 24)	& 0xFF;
		int r = (color >> 16)	& 0xFF;
		int g = (color >> 8)	& 0xFF;
		int b = color			& 0xFF;

		a = a / 2;

		return (a << 24) + (r << 16) + (g << 8) + b;
	}

	public static int translucent(String name)
	{
		return translucent(get(name));
	}

	//
	//
	//
	//	Area: Conditions
	//
	//
	//

    /**
     *  Determine if a color is too light for white text to be read on.
     *  @param color The integer value of the color to be tested
     */
	public static boolean isLight(int color)
	{
		int a = (color >> 24)	& 0xFF;
		int r = (color >> 16)	& 0xFF;
		int g = (color >> 8)	& 0xFF;
		int b = color			& 0xFF;

		double result = (r * 0.299) + (g * 0.587) + (b * 0.114);
		return result > 186;
	}

    /**
     *  A convenience method for determining if a color is dark enough for white text to be read on.
     *  @param color The integer value of the color to be tested
     */
	public static boolean isDark(int color)
    {
        return !isLight(color);
    }
}
