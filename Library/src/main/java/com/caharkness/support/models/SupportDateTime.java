package com.caharkness.support.models;

import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class SupportDateTime
{
	//
	//	Time is stored in milliseconds
	//
	private long time		= 0;
	private long time_ex	= 0;

	public SupportDateTime()
	{
	}

	//
	//	Convert a YYYY-MM-DD string into a date time object
	//
	public SupportDateTime fromString(String in)
	{
		Calendar calendar = Calendar.getInstance();

		int day		= Integer.parseInt(in.split("-")[2]);
		int month	= Integer.parseInt(in.split("-")[1]);
		int year	= Integer.parseInt(in.split("-")[0]);

		calendar.setTimeInMillis(0);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.YEAR, year);

		this.time = calendar.getTimeInMillis();
		return this;
	}

	//
	// Convert the date picker selection into a date time object
	//
	public SupportDateTime fromDatePicker(DatePicker picker)
	{
		Calendar calendar = Calendar.getInstance();

		int day = picker.getDayOfMonth();
		int month = picker.getMonth();
		int year =  picker.getYear();

		calendar.setTimeInMillis(0);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);

		this.time = calendar.getTimeInMillis();
		return this;
	}

	//
	//	Convert the time picker selection into a date time object
	//
	public SupportDateTime fromTimePicker(TimePicker picker)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(0);
		calendar.set(Calendar.HOUR_OF_DAY, picker.getCurrentHour());
		calendar.set(Calendar.MINUTE, picker.getCurrentMinute());

		this.time = calendar.getTimeInMillis();
		return this;
	}

	//
	//	Convert a long into a date time object
	//
	public SupportDateTime fromLong(long milliseconds)
	{
		this.time = milliseconds;
		return this;
	}

	//
	//	Convert a string containing a long into a date time object
	//	Example: .NET sends "/Date( ... )/"
	//
	public SupportDateTime fromLong(String milliseconds)
	{
		String new_time = "";

		for (char c : milliseconds.toCharArray())
		{
			if (Character.isDigit(c))
				new_time += c;
		}

		this.time = Long.parseLong(new_time);
		return this;
	}

	//
	//	Convert a string containing a long into a date time object
	//	Accepts null and flags toDateStringOr to return replacement string
	//
	public SupportDateTime fromLongOrNull(String input)
	{
		try
		{
			if (input == null || input.isEmpty())
				throw new NullPointerException();

			String new_time = "";

			for (char c : input.toCharArray())
			{
				if (Character.isDigit(c))
					new_time += c;
			}

			this.time = Long.parseLong(new_time);
			return this;
		}
		catch (Exception x)
		{
			this.time_ex = -1;
			return this;
		}
	}

	//
	//	Convert today's date into a date time object
	//
	public SupportDateTime fromToday()
	{
		this.time =
			Calendar
				.getInstance()
				.getTimeInMillis();

		return this;
	}

	public SupportDateTime subtractMonths(int months)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(this.time);
		calendar.add(Calendar.MONTH, -months);

		this.time = calendar.getTimeInMillis();
		return this;
	}

	//
	//	Set the date time object to the first of January of that year
	//
	public SupportDateTime toEarlyJanuary()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(this.time);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);

		this.time = calendar.getTimeInMillis();
		return this;
	}

	//
	//	Set the date time object to the thirty first of December of that year
	//
	public SupportDateTime toLateDecember()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(this.time);
		calendar.set(Calendar.MONTH, 11);
		calendar.set(Calendar.DAY_OF_MONTH, 31);

		this.time = calendar.getTimeInMillis();
		return this;
	}

	//
	//	Formatted output methods
	//
	public String toShortDateString()
	{
		Calendar calendar = Calendar.getInstance();

		calendar.setTimeInMillis(this.time);

		return new
			SimpleDateFormat(
				"yyyy-MM-dd",
				Locale.ENGLISH)
			.format(calendar.getTime());
	}

	//
	//	Formatted output methods
	//
	public String toShortDateStringUTC()
	{
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

		calendar.setTimeInMillis(this.time);

		SimpleDateFormat f =
			new SimpleDateFormat(
				"yyyy-MM-dd",
				Locale.ENGLISH);

		f.setTimeZone(TimeZone.getTimeZone("UTC"));

		return f.format(calendar.getTime());
	}

	public String toDateString()
	{
		//Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		Calendar calendar = Calendar.getInstance();

		calendar.setTimeInMillis(this.time);

		return new
			SimpleDateFormat(
				"MMM d, yyyy",
				Locale.ENGLISH)
			.format(calendar.getTime());
	}

	public String toDateStringOr(String replacement)
	{
		if (this.time_ex < 0)
			return replacement;

		Calendar calendar = Calendar.getInstance();

		calendar.setTimeInMillis(this.time);

		return new
			SimpleDateFormat(
				"MMM d, yyyy",
				Locale.ENGLISH)
			.format(calendar.getTime());
	}

	public String toFullDateString()
	{
		Calendar calendar = Calendar.getInstance();

		calendar.setTimeInMillis(this.time);

		return new
			SimpleDateFormat(
				"EEEE, MMMM d, yyyy",
				Locale.ENGLISH)
			.format(calendar.getTime());
	}

	public String toFullDateStringUTC()
	{
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

		calendar.setTimeInMillis(this.time);

		SimpleDateFormat f =
			new SimpleDateFormat(
				"EEEE, MMMM d, yyyy",
				Locale.ENGLISH);

		f.setTimeZone(TimeZone.getTimeZone("UTC"));

		return f.format(calendar.getTime());
	}

	public String toFullDateStringWithTime()
	{
		Calendar calendar = Calendar.getInstance();

		calendar.setTimeInMillis(this.time);

		String date =
			new SimpleDateFormat(
				"EEEE, MMMM d, yyyy",
				Locale.ENGLISH)
			.format(calendar.getTime());

		String time =
			new SimpleDateFormat(
				"h:mm a",
				Locale.ENGLISH)
			.format(calendar.getTime());

		return String.format("%s at %s", date, time);
	}

	public String toUTCTime()
	{
		Calendar calendar = Calendar.getInstance();

		calendar.setTimeInMillis(this.time);

		return new
			SimpleDateFormat(
				"h:mm a",
				Locale.ENGLISH)
			.format(calendar.getTime());
	}

	public String toLocalTime()
	{
		Calendar calendar = Calendar.getInstance();

		calendar.setTimeInMillis(this.time);

		return new
			SimpleDateFormat(
				"h:mm a",
				Locale.ENGLISH)
			.format(calendar.getTime());
	}

	public int getMinutesSinceMidnight()
	{
		Calendar calendar = Calendar.getInstance();

		calendar.setTimeInMillis(this.time);

		return
			(calendar.get(Calendar.HOUR) * 60) +
			(calendar.get(Calendar.MINUTE));
	}

	public SupportDateTime fromMinutes(int minutes)
	{
		this.time = minutes * 60 * 1000;
		return this;
	}

	public String toTimespan()
	{
		final int total_minutes     = (int) this.time / 60 / 1000;
		final int hours             = total_minutes / 60;
		final int leftover_minutes  = total_minutes % 60;

		String hours_word   = hours > 1?                hours + " hours"                : hours + " hour";
		String minutes_word = leftover_minutes > 1?     leftover_minutes + " minutes"   : leftover_minutes + " minute";
		String caption;

		if (leftover_minutes > 0)
			if (hours > 0)
				caption = hours_word + " and " + minutes_word;
			else
				caption = minutes_word;
		else
			caption = hours_word;

		return caption;
	}
}
