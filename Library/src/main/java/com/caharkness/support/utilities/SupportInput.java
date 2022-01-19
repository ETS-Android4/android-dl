package com.caharkness.support.utilities;

public class SupportInput
{
    private static int last_touch_x;
    private static int last_touch_y;

    public static void setLastTouchX(int x)
    {
        last_touch_x = x;
    }

    public static void setLastTouchY(int y)
    {
        last_touch_y = y;
    }

    public static int getLastTouchX()
    {
        return last_touch_x;
    }

    public static int getLastTouchY()
    {
        return last_touch_y;
    }
}
