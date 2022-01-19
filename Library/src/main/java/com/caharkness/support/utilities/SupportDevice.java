package com.caharkness.support.utilities;

import android.content.Context;
import android.os.Vibrator;
import android.view.Display;
import android.view.WindowManager;

import com.caharkness.support.SupportApplication;

public class SupportDevice
{
    public static void vibrate(long ms)
    {
        try
        {
            Vibrator v = (Vibrator)
                SupportApplication
                    .getInstance()
                    .getSystemService(Context.VIBRATOR_SERVICE);

            v.vibrate(ms);
        }
        catch (Exception x) {}
    }

    public static int screenWidth()
	{
	    try
        {
            WindowManager manager =
                (WindowManager) SupportApplication
                    .getInstance()
                    .getSystemService(Context.WINDOW_SERVICE);

            Display display = manager.getDefaultDisplay();

            return display.getWidth();
        }
        catch (Exception x) {}
        return 0;
	}

	public static int screenHeight()
	{
	    try
        {
            WindowManager manager =
                (WindowManager) SupportApplication
                    .getInstance()
                    .getSystemService(Context.WINDOW_SERVICE);

            Display display = manager.getDefaultDisplay();

            return display.getHeight();
        }
        catch (Exception x) {}
        return 0;
	}
}
