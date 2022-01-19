package com.caharkness.support.utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class SupportJSON
{
	public static JSONObject parse(String s)
    {
        try
        {
            return new JSONObject(s);
        }
        catch (Exception x)
        {
            try
            {
                return new JSONObject("{}");
            }
            catch (Exception y) {}
            return null;
        }
    }

    public static JSONArray parseArray(String s)
    {
        try
        {
            return new JSONArray(s);
        }
        catch (Exception x)
        {
            try
            {
                return new JSONArray("[]");
            }
            catch (Exception y) {}
            return null;
        }
    }

    public static boolean isEmpty(JSONObject o)
    {
        if (o == null)
            return true;

        if (o.length() < 1)
            return true;

        return false;
    }

	public static Object getValue(JSONObject o, String key, Object other)
	{
		try
		{
            if (o != null && !o.isNull(key))
			    return o.get(key);
		}
		catch (Exception x) {}
		return other;
	}

	public static String getString(JSONObject o, String key, String other)
	{
		try
		{
            if (o != null && !o.isNull(key))
			    return o.getString(key);
		}
		catch (Exception x) {}
		return other;
	}

	public static Boolean getBoolean(JSONObject o, String key, Boolean other)
	{
		try
		{
            if (o != null && !o.isNull(key))
			    return o.getBoolean(key);
		}
		catch (Exception x) {}
		return other;
	}

	public static Integer getInt(JSONObject o, String key, Integer other)
    {
        try
        {
            if (o != null && !o.isNull(key))
                return o.getInt(key);
        }
        catch (Exception x) {}
        return other;
    }

    public static Long getLong(JSONObject o, String key, Long other)
    {
        try
        {
            if (o != null && !o.isNull(key))
                return o.getLong(key);
        }
        catch (Exception x) {}
        return other;
    }

    public static Double getDouble(JSONObject o, String key, Double other)
    {
        try
        {
            if (o != null && !o.isNull(key))
                return o.getDouble(key);
        }
        catch (Exception x) {}
        return other;
    }

    public static void putValue(JSONObject o, String key, Object value)
    {
        try
        {
            o.put(key, value);
        }
        catch (Exception x) {}
        return;
    }

    public static void putValue(JSONArray a, Object value)
    {
        try
        {
            a.put(value);
        }
        catch (Exception x) {}
        return;
    }

    public static JSONObject getJSONObject(String[]... pairs)
    {
        JSONObject o = parse("{}");

        for (String[] pair : pairs)
            putValue(o, pair[0], pair[1]);

        return o;
    }

    public static Object getValue(JSONObject o, String key)
	{
	    return getValue(o, key, null);
	}

	public static List<JSONObject> getJSONObjects(JSONArray a)
	{
		ArrayList<JSONObject> list = new ArrayList<JSONObject>();

		try
		{
			for (int i = 0; i < a.length(); i++)
				list.add(a.getJSONObject(i));
		}
		catch (Exception x) {}
		return list;
	}

	public static List<Object> getJSONTokens(JSONArray a)
	{
		ArrayList<Object> list = new ArrayList<Object>();

		try
		{
			for (int i = 0; i < a.length(); i++)
				list.add(a.get(i));
		}
		catch (Exception x) {}
		return list;
	}

	public static JSONArray getJSONArray(JSONObject o, String key, JSONArray other)
	{
		try
        {
            if (o != null && !o.isNull(key))
                return o.getJSONArray(key);
        }
        catch (Exception x) {}
        return other;
	}
}
