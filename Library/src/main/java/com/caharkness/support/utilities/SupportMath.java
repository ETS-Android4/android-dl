package com.caharkness.support.utilities;

import android.util.TypedValue;

import com.caharkness.support.SupportApplication;
import com.caharkness.support.SupportLibrary;

import java.util.Random;

public class SupportMath
{
    /**
     *  A convenience method for converting inches to actual pixels using the device's display metrics.
     *  Note: Most features in this library use this method to create uniformly sized UI elements across all devices.
     */
	public static int inches(float inches)
	{
		int output = 0;

		if (SupportLibrary.SPOOF_DPI_VALUE)
        {
            output = (int) (SupportLibrary.SPOOF_DPI * inches);
        }
        else
        {
            output = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_IN,
                inches,
                SupportApplication
                    .getInstance()
                    .getResources()
                    .getDisplayMetrics());
        }

		return output;
	}

	public static int dp(float dp)
	{
		int output = 0;

		output = (int) TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_DIP,
			dp,
			SupportApplication
				.getInstance()
				.getResources()
				.getDisplayMetrics());

		return output;
	}

	public static int percent(float percent)
	{
		int output = 0;

		output = (int) TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_FRACTION_PARENT,
			percent,
			SupportApplication
				.getInstance()
				.getResources()
				.getDisplayMetrics());

		return output;
	}

    /**
     *  A convenience method for returning a random integer using a new instance of random.
     */
	public static int randomInt()
	{
		return
		new Random()
		    .nextInt();
	}

    /**
     *  A convenience method for returning a random integer from 0 to the specified max value, inclusive.
     */
	public static int randomInt(int max)
	{
		return
		(int) Math.round(Math.random() * max);
	}
}
