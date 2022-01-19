package com.caharkness.support.utilities;

import com.caharkness.support.SupportApplication;

public class SupportRunnable
{
    /**
     *  Create a single runnable instance from multiple runnables.
     *  @param runnables The runnables to run in order
     */
    public static Runnable join(final Runnable... runnables)
    {
        return
        new Runnable()
        {
            @Override
            public void run()
            {
                for (Runnable r : runnables)
                    if (r != null)
                        r.run();
            }
        };
    }

    /**
     *  Create an instance of a runnable that calls a method of an object.
     *  @param object The object containing the method you wish to call
     *  @param name The case-sensitive name of the method you wish to call
     */
    public static Runnable fromMethod(final Object object, final String name)
    {
        return
        new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    object
                        .getClass()
                        .getMethod(name)
                        .invoke(object);
                }
                catch (Exception x) { SupportApplication.log(x); }
            }
        };
    }
}
