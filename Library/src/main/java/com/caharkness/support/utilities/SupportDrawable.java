package com.caharkness.support.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.caharkness.support.R;
import com.caharkness.support.SupportApplication;
import com.caharkness.support.models.SupportIcon;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SupportDrawable
{
    public static Drawable fromResource(int resource, int width, int height)
	{
		Bitmap original =
			BitmapFactory
				.decodeResource(
					SupportApplication
						.getInstance()
						.getResources(),
					resource);

		Bitmap b = Bitmap.createScaledBitmap(
			original,
			width,
			height,
			true);

		return
        new BitmapDrawable(
			SupportApplication
				.getInstance()
				.getResources(),
			b);
	}

    public static Drawable fromResource(int resource, int width)
	{
		Bitmap original =
			BitmapFactory
				.decodeResource(
					SupportApplication
						.getInstance()
						.getResources(),
					resource);

		float ratio =
            (original.getWidth() * 1.0f) /
            (original.getHeight() * 1.0f);

		int target_width = width;
		int target_height = Math.round(width / ratio);

		return
        fromResource(
            resource,
            target_width,
            target_height);
	}

	public static Drawable fromResource(int resource, float width, float height)
	{
		return
		fromResource(
		    resource,
            SupportMath.inches(width),
            SupportMath.inches(height));
	}

    public static Drawable fromResource(int resource, float width)
	{
		return
		fromResource(
		    resource,
            SupportMath.inches(width));
	}

	public static Drawable fromResourceSmall(int resource)
    {
        //
        //  For use with the SupportListItemView.
        //
        return fromResource(
            resource,
            SupportMath.inches(4 / 25f));
    }

    public static Drawable fromResourceBig(int resource)
    {
        //
        //  For use with the SupportToolbar and SupportFloatingActionButton.
        //
        return fromResource(
            resource,
            SupportMath.inches(1 / 5f));
    }

	public static Drawable fromBitmap(Bitmap bitmap)
	{
		return
		new BitmapDrawable(
			SupportApplication
				.getInstance()
				.getResources(),
			bitmap);
	}

	public static Drawable tint(Drawable drawable, int color)
	{
		drawable.setColorFilter(
			color,
			PorterDuff.Mode.MULTIPLY);

		return drawable;
	}

	public static int fromString(String name)
	{
		Field[] fields = (R.drawable.class).getFields();

		for (Field field : fields)
		{
			try
			{
				if (field.getName().equals(name))
					return field.getInt(null);
			}
			catch (Exception x) {}
		}

		return 0;
	}

	public static List<SupportIcon> getIcons(String query)
    {
        ArrayList<SupportIcon> icons = new ArrayList<>();
        ArrayList<Field> fields = new ArrayList<>();

        for (final Field field : (R.drawable.class).getDeclaredFields())
        try
        {
            final String field_id = field.getName();
            final String field_name =
                field
                    .getName()
                    .substring(3)
                    .replace("_", " ")
                    .toLowerCase();

            final Integer field_resource = field.getInt(null);

            if (!field_id.toLowerCase().startsWith("ic_")) continue;
            if (field_id.toLowerCase().startsWith("ic_launcher"))
                continue;

            query = query.toLowerCase();

            if (field_name.contains(query))
                fields.add(field);

            SupportIcon
                icon = new SupportIcon();
                icon.setId(field_id);
                icon.setName(field_name);
                icon.setResource(field_resource);

            icons.add(icon);
        }
        catch (Exception x) {}
        return icons;
    }


}
