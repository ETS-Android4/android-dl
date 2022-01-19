package com.caharkness.support;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import com.caharkness.support.models.SupportBundle;
import com.caharkness.support.utilities.SupportColors;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@SuppressWarnings("all")
public class SupportApplication extends MultiDexApplication
{
	//
	//
	//
	//	Area: Singleton
	//
	//
	//

	private static SupportApplication self;
	private static ArrayList<String> log_lines = new ArrayList<>();

    /**
     *  Returns the instance of this application.
     */
	public static SupportApplication getInstance()
	{
		if (self == null)
			throw new RuntimeException();

		return self;
	}

	//
	//
	//
	//	Area: Instance
	//
	//
	//

	@Override
	public void onCreate()
	{
		super.onCreate();
		self = this;

		Thread.currentThread().setUncaughtExceptionHandler(
			new Thread.UncaughtExceptionHandler()
			{
				@Override
				public void uncaughtException(Thread t, Throwable e)
				{
					if (e instanceof Exception)
						onUncaughtException((Exception) e);
				}
			});

		//
		//  Source: https://www.materialui.co/colors
        //
		SupportColors.set("white",					0xFFFFFFFF);
		SupportColors.set("deep orange",			0xFFFF5722);
		SupportColors.set("red",					0xFFF44336);
		SupportColors.set("pink",					0xFFE91E63);
		SupportColors.set("purple",					0xFF9C27B0);
		SupportColors.set("deep purple",			0xFF673AB7);
		SupportColors.set("indigo",					0xFF3F51B5);
		SupportColors.set("blue",					0xFF2196F3);
		SupportColors.set("light blue",				0xFF03A9F4);
		SupportColors.set("cyan",					0xFF00BCD4);
		SupportColors.set("teal",					0xFF009688);
		SupportColors.set("green",					0xFF4CAF50);
		SupportColors.set("light green",			0xFF8BC34A);
		SupportColors.set("lime",					0xFFCDDC39);
		SupportColors.set("yellow",					0xFFFFEB3B);
		SupportColors.set("amber",					0xFFFFC107);
		SupportColors.set("orange",					0xFFFF9800);
		SupportColors.set("brown",					0xFF795548);
		SupportColors.set("grey",					0xFF9E9E9E);
		SupportColors.set("blue grey",				0xFF607D8B);
		SupportColors.set("material dark blue",		0xFF212A31);
		SupportColors.set("material dark",			0xFF303030);
		SupportColors.set("black",					0xFF000000);

		MultiDex.install(this);
	}

	//
	//
	//
	//	Area: Exceptions
	//
	//
	//

    /**
     *  Called when a fatal exception is thrown, causing a crash.
     *  Override to handle what happens when an uncaught exception is thrown.
     */
	public void onUncaughtException(Exception x)
	{
		log(x);
	}

	//
	//
	//
	//	Area: Logging
	//
	//
	//

	public static void log(String line)
	{
		Log.i("support_application", line);
		log_lines.add(line);
	}

	public static String log(Exception x)
	{
		String stack = "[Exception] " + x.getMessage() + "\n";

		for (StackTraceElement e : x.getStackTrace())
		{
			stack +=
				"Line " + e.getLineNumber()   		+ "\n" +
				"    in " + e.getMethodName()       + "()\n" +
				"    of class " + e.getClassName()  + "\n" +
				"    in file " + e.getFileName()    + "\n";
		}

		Log.e("support_application", stack);
		log_lines.add(stack);

		return stack;
	}

	public static String getLogANSI()
	{
		String out = "";

		for (String line : log_lines)
			out += line + "\r\n";

		return out;
	}

	public static String getLogUTF8()
	{
		String out = "";

		for (String line : log_lines)
			out += line + "\n";

		return out;
	}

	public static String getLogHTML()
	{
		String out = "";

		for (String line : log_lines)
			out += line + "<br />\n";

		return out;
	}

	public static ArrayList<String> getLogLines()
	{
		return log_lines;
	}

	//
	//
	//
	//	Area: Fonts
	//
	//
	//

	public static void setDefaultFont(Context context, String name, String asset)
	{
		final Typeface regular =
			Typeface
				.createFromAsset(
					context.getAssets(),
					asset);

		replaceFont(name, regular);
	}

	public static void replaceFont(String name, final Typeface face)
	{
		try
		{
			//
			// Handle material design font override
			//
			Map<String, Typeface> map = new HashMap<>();

			map.put(
				name.replace("_", "-").toLowerCase(),
				face);

			final Field field =
				Typeface
					.class
					.getDeclaredField("sSystemFontMap");

			field.setAccessible(true);
			field.set(null, map);
		}
		catch (Exception x) { log(x); }

		try
		{
			//
			//	Handle non-material design font override
			//
			final Field field =
				Typeface
					.class
					.getDeclaredField(name);

			field.setAccessible(true);
			field.set(null, face);
		}
		catch (Exception x) { log(x); }
	}

	public void useNotoSansFont()
	{
		setDefaultFont(this, "DEFAULT",			"fonts/NotoSans-Regular.ttf");
		setDefaultFont(this, "DEFAULT_BOLD",	"fonts/NotoSans-Bold.ttf");
		setDefaultFont(this, "MONOSPACE",		"fonts/NotoSans-Regular.ttf");
		setDefaultFont(this, "SERIF",			"fonts/NotoSans-Regular.ttf");
		setDefaultFont(this, "SANS_SERIF",		"fonts/NotoSans-Regular.ttf");
	}

	public void useOpenSansFont()
	{
		setDefaultFont(this, "DEFAULT",			"fonts/OpenSans-Regular.ttf");
		setDefaultFont(this, "DEFAULT_BOLD",	"fonts/OpenSans-Bold.ttf");
		setDefaultFont(this, "MONOSPACE",		"fonts/OpenSans-Regular.ttf");
		setDefaultFont(this, "SERIF",			"fonts/OpenSans-Regular.ttf");
		setDefaultFont(this, "SANS_SERIF",		"fonts/OpenSans-Regular.ttf");
	}

	public void useGothamRoundedFont()
	{
		setDefaultFont(this, "DEFAULT",			"fonts/GothamRounded-Book.otf");
		setDefaultFont(this, "DEFAULT_BOLD",	"fonts/GothamRounded-Bold.otf");
		setDefaultFont(this, "MONOSPACE",		"fonts/GothamRounded-Book.otf");
		setDefaultFont(this, "SERIF",			"fonts/GothamRounded-Book.otf");
		setDefaultFont(this, "SANS_SERIF",		"fonts/GothamRounded-Book.otf");
	}

	public void useShinGoProFont()
	{
		setDefaultFont(this, "DEFAULT",			"fonts/ShinGoPro-Light.otf");
		setDefaultFont(this, "DEFAULT_BOLD",	"fonts/ShinGoPro-Bold.otf");
		setDefaultFont(this, "MONOSPACE",		"fonts/ShinGoPro-Light.otf");
		setDefaultFont(this, "SERIF",			"fonts/ShinGoPro-Light.otf");
		setDefaultFont(this, "SANS_SERIF",		"fonts/ShinGoPro-Light.otf");
	}

	//
	//
	//
	//	Area: Preferences
	//
	//
	//

    public static enum SettingType
    {
        STRING,
        BOOLEAN,
        INTEGER,
        DOUBLE,
        FLOAT,
        JSON_OBJECT,
        JSON_ARRAY
    }

	public static String getString(String key)
	{
		SharedPreferences preferences =
			getInstance()
				.getSharedPreferences(
					getInstance().getPackageName(),
					Context.MODE_PRIVATE);

		return preferences.getString(key, null);
	}

	public static String getString(String key, String def)
	{
		if (getString(key) == null)
			setString(key, def);

		return getString(key);
	}

	public static void setString(String key, String value)
	{
		SharedPreferences preferences =
			getInstance()
				.getSharedPreferences(
					getInstance().getPackageName(),
					Context.MODE_PRIVATE);

		if (value == null)
		{
			preferences
				.edit()
				.remove(key)
				.commit();

			return;
		}

		SharedPreferences.Editor
			editor = preferences.edit();
			editor.putString(key, value);
			editor.commit();
	}

	public static Boolean getBoolean(String key)
	{
		try
		{
			if (getString(key) != null)
			{
				if (getString(key).equalsIgnoreCase("true"))	return true;
				if (getString(key).equalsIgnoreCase("false"))	return false;
				return null;
			}

			return null;
		}
		catch (Exception x)
		{
			return null;
		}
	}

	public static Boolean getBoolean(String key, Boolean def)
	{
		if (getBoolean(key) == null)
			setBoolean(key, def);

		return getBoolean(key);
	}

	public static void setBoolean(String key, Boolean value)
	{
		if (value != null)
			setString(
				key,
				value.booleanValue()? "true" : "false");
		else
			setString(key, null);
	}

	public static Integer getInt(String key)
	{
		try
		{
			return Integer.parseInt(getString(key));
		}
		catch (Exception x)
		{
			return null;
		}
	}

	public static Integer getInt(String key, Integer def)
	{
		if (getInt(key) == null)
			setInt(key, def);

		return getInt(key);
	}

	public static void setInt(String key, Integer value)
	{
		setString(
			key,
			value != null?
				value.toString() :
				null);
	}

	public static Double getDouble(String key)
	{
		try
		{
			return Double.parseDouble(getString(key));
		}
		catch (Exception x)
		{
			return null;
		}
	}

	public static Double getDouble(String key, Double def)
	{
		if (getDouble(key) == null)
			setDouble(key, def);

		return getDouble(key);
	}

	public static void setDouble(String key, Double value)
	{
		setString(
			key,
			value != null?
				value.toString() :
				null);
	}

	public static Float getFloat(String key)
	{
		try
		{
			return Float.parseFloat(getString(key));
		}
		catch (Exception x)
		{
			return null;
		}
	}

	public static float getFloat(String key, Float def)
	{
		if (getFloat(key) == null)
			setFloat(key, def);

		return getFloat(key);
	}

	public static void setFloat(String key, Float value)
	{
		setString(
			key,
			value != null?
				value.toString() :
				null);
	}

	public static JSONObject getJSONObject(String key)
	{
		JSONObject output = null;

		try
		{
			output = new JSONObject(getString(key, "{}"));
		}
		catch (Exception x) {}
		return output;
	}

	public static void setJSONObject(String key, JSONObject value)
	{
		setString(
			key,
			value == null?
				null :
				value.toString());
	}

	public static JSONArray getJSONArray(String key)
	{
		JSONArray output = null;

		try
		{
			output = new JSONArray(getString(key, "[]"));
		}
		catch (Exception x) {}
		return output;
	}

	public static void setJSONArray(String key, JSONArray value)
	{
		setString(
			key,
			value == null?
				null :
				value.toString());
	}

	public static SortedMap<String, String> getPreferences()
	{
		SharedPreferences preferences =
			getInstance()
				.getSharedPreferences(
					getInstance().getPackageName(),
					Context.MODE_PRIVATE);

		SortedMap<String, String> map = new TreeMap<>(
			new Comparator<String>()
			{
				@Override
				public int compare(String o1, String o2)
				{
					return o1.compareTo(o2);
				}
			});

		for (Map.Entry<String, ?> entry : preferences.getAll().entrySet())
			map.put(entry.getKey(), (String) entry.getValue());

		return map;
	}

	public static JSONObject getPreferencesAsJSON()
	{
		try
		{
			JSONObject o = new JSONObject();

			for (Map.Entry<String, String> entry : getPreferences().entrySet())
				o.put(entry.getKey(), entry.getValue());

			return o;
		}
		catch (Exception x) {}
		return null;
	}

	public static void setPreferencesFromJSON(JSONObject o)
	{
		SharedPreferences preferences =
			getInstance()
				.getSharedPreferences(
					getInstance().getPackageName(),
					Context.MODE_PRIVATE);

		preferences
			.edit()
			.clear()
			.commit();

		try
		{
			Iterator<String> s = o.keys();

			while (s.hasNext())
			{
				String key = s.next();
				String value = o.getString(key);

				setString(key, value);
			}

			return;
		}
		catch (Exception x) {}
	}

	public static void resetPreferences()
	{
		try
		{
			//
			//	Clear the existing stored preferences.
			//
			setPreferencesFromJSON(new JSONObject("{}"));
		}
		catch (Exception x) {}
	}

	//
	//
	//
	//	Area: Control
	//
	//
	//

    public static Bundle toBundle(Object o)
    {
        if (o instanceof Bundle)
            return (Bundle) o;

        if (o instanceof SupportBundle)
            return ((SupportBundle) o).getBundle();

        if (o instanceof String)
        {
            SupportBundle b =
                new SupportBundle()
                    .set("default", (String) o);

            return
            b.getBundle();
        }

        return
        new SupportBundle()
            .getBundle();
    }

	public static void startActivity(Class<?> activity_class, Object bundle)
	{
		Intent intent = new Intent(getInstance(), activity_class);

		if (bundle != null)
            intent.putExtras(toBundle(bundle));

		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		getInstance().startActivity(intent);
	}

	public static void startActivity(Class<?> activity_class)
	{
		startActivity(activity_class, null);
	}

	public static void forceQuit()
	{
		Process.killProcess(Process.myPid());
	}

	public static void forceRestart(Class<?> activity_class)
	{
		Intent intent = new Intent(getInstance(), activity_class);

        intent.setAction(Intent.ACTION_MAIN);

		PendingIntent pending_intent =
			PendingIntent.getActivity(
				getInstance(),
				0,
				intent,
				PendingIntent.FLAG_CANCEL_CURRENT);

		AlarmManager manager = (AlarmManager) getInstance().getSystemService(Context.ALARM_SERVICE);

		manager.set(
			AlarmManager.RTC,
			System.currentTimeMillis() + 2000,
			pending_intent);

		forceQuit();
	}

	public static void createNotification(String title, String content, String info, int large_icon, int small_icon)
	{
		NotificationManager manager = (NotificationManager)
			getInstance()
				.getSystemService(Context.NOTIFICATION_SERVICE);

		manager.notify(
			0,
			new Notification.Builder(getInstance())
				.setContentTitle(title)
				.setContentText(content)
				.setContentInfo(info)
				.setLargeIcon(
					BitmapFactory.decodeResource(
						getInstance().getResources(),
						large_icon))
				.setSmallIcon(small_icon)
				.build());
	}

	//
	//
	//
	//	Area: Info
	//
	//
	//

	public static boolean isAuthentic()
	{
		boolean result = false;

		try
		{
			result = !TextUtils.isEmpty(
				getInstance()
					.getPackageManager()
					.getInstallerPackageName(getInstance().getPackageName()));
		}
		catch (Exception x) {}
		return result;
	}

	public static int getAPILevel()
	{
		return Build.VERSION.SDK_INT;
	}

	public static String getAndroidVersion()
	{
		return Build.VERSION.RELEASE;
	}

	public static String getDeviceModel()
	{
		return Build.MODEL;
	}

	public static String getDeviceProductName()
	{
		String manufacturer	= "Unknown";
		String product		= "product";

		if (Build.MANUFACTURER.length() > 0)
			manufacturer = Build.MANUFACTURER;

		if (Build.PRODUCT.length() > 0)
			product = Build.PRODUCT;

		return manufacturer +  " " + product;
	}

	public static long getInstallTime()
	{
		long result = 0;

		try
		{
			result = getInstance()
				.getPackageManager()
    			.getPackageInfo(getInstance().getPackageName(), 0)
    			.firstInstallTime;
		}
		catch (Exception x) {}
		return result;
	}

	public static String getDeviceId()
	{
		return Settings.Secure.getString(
			getInstance().getContentResolver(),
			Settings.Secure.ANDROID_ID);
	}

	public static String getApplicationPackageName()
	{
		return getInstance()
			.getApplicationContext()
			.getPackageName();
	}

	public static String getApplicationVersionName()
	{
		String result = "";

		try
		{
			result = getInstance()
				.getPackageManager()
    			.getPackageInfo(getInstance().getPackageName(), 0)
    			.versionName;
		}
		catch (Exception x) {}
		return result;
	}

	public static int getApplicationVersionCode()
	{
		int result = -1;

		try
		{
			result = getInstance()
				.getPackageManager()
    			.getPackageInfo(getInstance().getPackageName(), 0)
    			.versionCode;
		}
		catch (Exception x) {}
		return result;
	}

	public static String getApplicationName()
	{
		String result = "Application";

		try
		{
			result = getInstance()
				.getPackageManager()
    			.getApplicationLabel(
    				getInstance()
						.getPackageManager()
						.getPackageInfo(getInstance().getPackageName(), 0)
						.applicationInfo) + "";
		}
		catch (Exception x) {}
		return result;
	}
}
