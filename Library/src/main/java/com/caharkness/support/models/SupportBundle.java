package com.caharkness.support.models;

import android.os.Bundle;

public class SupportBundle
{
    private Bundle bundle;

    public SupportBundle()
    {
        this.bundle = new Bundle();
    }

    public Bundle getBundle()
    {
        if (bundle == null)
            bundle = new Bundle();

        return bundle;
    }

    public SupportBundle set(String key, String value)
    {
        getBundle()
            .putString(
                key,
                value);

        return this;
    }
}
