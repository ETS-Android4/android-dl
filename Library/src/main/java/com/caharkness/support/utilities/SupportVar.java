package com.caharkness.support.utilities;

public class SupportVar
{
	public static Object first(Object... objects)
	{
		for (Object o : objects)
			if (o != null)
				return o;

		return null;
	}
}
