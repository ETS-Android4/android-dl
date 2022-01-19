package com.caharkness.support.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.caharkness.support.SupportApplication;

import java.util.concurrent.Callable;

public class SupportBitmap
{
    /**
     *  Combine ordered bitmaps into one bitmap.
     *  The last bitmap will be drawn last.
     */
	public static Bitmap merge(Bitmap... bitmaps)
	{
		if (bitmaps.length < 1)
			return null;

		Bitmap bitmap = bitmaps[0];
		Canvas canvas = new Canvas(bitmap);

		if (bitmaps.length > 1)
		{
			//
			//	For every additional bitmap in the bitmap array...
			//
			for (int i = 1; i < bitmaps.length; i++)
			{
				int width	= bitmaps[i].getWidth();
				int height	= bitmaps[i].getHeight();
				int x 		= (bitmap.getWidth() / 2) - (width / 2);
				int y		= (bitmap.getHeight() / 2) - (height / 2);

				canvas.drawBitmap(bitmaps[i], x, y, null);
			}
		}

		return bitmap;
	}

    /**
     *  Convert a square drawable resource into a bitmap sized in pixels.
     */
    public static Bitmap fromResource(int resource, int size)
	{
		Bitmap original =
			BitmapFactory.decodeResource(
				SupportApplication
					.getInstance()
					.getResources(),
				resource);

		Bitmap b = Bitmap.createScaledBitmap(
			original,
			size,
			size,
			false);

		return b;
	}

	/**
     *  Convert a square drawable resource into a bitmap sized in inches.
     */
    public static Bitmap fromResource(int resource, float inches)
	{
		return
		fromResource(
		    resource,
            SupportMath.inches(inches));
	}

	public static Bitmap fromResourceSmall(int resource)
	{
        //
        //  For use with the SupportListItemView.
        //
		return fromResource(
		    resource,
            4 / 25f);
	}

	public static Bitmap fromResourceBig(int resource)
	{
        //
        //  For use with the SupportToolbar and SupportFloatingActionButton.
        //
		return fromResource(
		    resource,
            1 / 5f);
	}

	/**
     *  Returns a resized bitmap (to scale) with size being the maximum size of either dimension.
     *  For example, a 1920x1080 scaled down to a size of 192 will still return a 16:9 bitmap.
     */
    public static Bitmap resize(Bitmap bitmap, int size)
    {
        int width	= bitmap.getWidth();
		int height	= bitmap.getHeight();

		if (bitmap.getWidth() > bitmap.getHeight())
		return Bitmap.createScaledBitmap(
			bitmap,
			size,
			Math.round(size * ((float) height / (float) width)),
			true);

		else if (bitmap.getHeight() > bitmap.getWidth())
		return Bitmap.createScaledBitmap(
			bitmap,
			Math.round(size * ((float) width / (float) height)),
			size,
			true);

		else
		return Bitmap.createScaledBitmap(
			bitmap,
			size,
			size,
			true);
	}

	public static Bitmap resize(Bitmap bitmap, float percent)
    {
        int width	= bitmap.getWidth();
		int height	= bitmap.getHeight();

		int w2 = Math.round(width * percent);
		int h2 = Math.round(height * percent);

		return
        Bitmap.createScaledBitmap(
			bitmap,
			w2,
			h2,
			true);
	}

    /**
     *  Returns a square bitmap by extending the shorter dimension with transparent pixels.
     */
    public static Bitmap pad(Bitmap bitmap)
    {
        if (bitmap.getWidth() >= bitmap.getHeight())
		{
			return Bitmap.createBitmap(
				bitmap,
				bitmap.getWidth() / 2 - bitmap.getHeight() / 2,
				0,
				bitmap.getHeight(),
				bitmap.getHeight()
			);
		}
		else
		{
			return Bitmap.createBitmap(
				bitmap,
				0,
				bitmap.getHeight() / 2 - bitmap.getWidth() / 2,
				bitmap.getWidth(),
				bitmap.getWidth()
			);
		}
    }

    /**
     *  Returns a square bitmap by cropping the larger dimension.
     */
    public static Bitmap crop(Bitmap bitmap)
    {
        int size =
			bitmap.getWidth() > bitmap.getHeight()?
				bitmap.getWidth() :
				bitmap.getHeight();

		Bitmap copy		= Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
		Canvas canvas	= new Canvas(copy);

		if (bitmap.getWidth() >= bitmap.getHeight())
		{
			canvas.drawBitmap(
				bitmap,
				((bitmap.getWidth() - size) / 2),
				0,
				null);
		}
		else
		{
			canvas.drawBitmap(
				bitmap,
				0,
				((bitmap.getHeight() - size) / 2),
				null);
		}

		return copy;
    }

    /**
     *  Returns a square bitmap either by cropping or padding.
     */
    public static Bitmap square(Bitmap bitmap, boolean crop)
    {
        if (crop)
                return crop(bitmap);
        else    return pad(bitmap);
    }

    /**
     *  Tints a bitmap with the specified color.
     */
    public static Bitmap tint(Bitmap bitmap, int color)
	{
		Canvas canvas = new Canvas(bitmap);

		Paint
			paint = new Paint();
			paint.setColor(color);
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));

		canvas.drawRect(
			0,
			0,
			bitmap.getWidth(),
			bitmap.getHeight(),
			paint);

		return bitmap;
	}

    /**
     *  Returns a bitmap from a drawable.
     */
    public static Bitmap fromDrawableOld(Drawable drawable)
	{
		if (drawable instanceof BitmapDrawable)
			return ((BitmapDrawable) drawable).getBitmap();

		return null;
	}

	/**
     *  Returns a bitmap from a drawable.
     */
    public static Bitmap fromDrawable(Drawable drawable, int size)
	{
	    if (drawable instanceof BitmapDrawable)
			return ((BitmapDrawable) drawable).getBitmap();
	    else
        {
            Bitmap b = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

            Canvas c = new Canvas(b);

            drawable.setBounds(0, 0, size, size);
            drawable.draw(c);

            return b;
        }
	}

    /**
     *  Returns a colored, circle bitmap with the diameter in pixels.
     */
	public static Bitmap fromCircle(int size, int color)
	{
		Bitmap bitmap =
			Bitmap.createBitmap(
				size,
				size,
				Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);

		Paint
			paint = new Paint();
			paint.setColor(color);
			paint.setFlags(Paint.ANTI_ALIAS_FLAG);

		canvas.drawCircle(
			size / 2f,
			size / 2f,
			size / 2f,
			paint);

		return bitmap;
	}

	/**
     *  Returns a colored, square bitmap with the side length in pixels.
     */
	public static Bitmap fromSquare(int size, int color)
	{
		Bitmap bitmap =
			Bitmap.createBitmap(
				size,
				size,
				Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);

		Paint
			paint = new Paint();
			paint.setColor(color);
			paint.setFlags(Paint.ANTI_ALIAS_FLAG);

		canvas.drawColor(color);

		return bitmap;
	}

	//
    //  Source: https://stackoverflow.com/questions/2067955/fast-bitmap-blur-for-android-sdk
    //  Provided by Yahel
    //
	public static Bitmap blur(Bitmap sentBitmap, float scale, int radius)
    {
        int width = Math.round(sentBitmap.getWidth() * scale);
        int height = Math.round(sentBitmap.getHeight() * scale);
        sentBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false);

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1)
            return null;

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];

        for (i = 0; i < 256 * divsum; i++)
            dv[i] = (i / divsum);

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++)
        {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;

            for (i = -radius; i <= radius; i++)
            {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;

                if (i > 0)
                {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                }
                else
                {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }

            stackpointer = radius;

            for (x = 0; x < w; x++)
            {
                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0)
                    vmin[x] = Math.min(x + radius + 1, wm);

                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }

            yw += w;
        }

        for (x = 0; x < w; x++)
        {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;

            for (i = -radius; i <= radius; i++)
            {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0)
                {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                }
                else
                {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm)
                    yp += w;
            }

            yi = x;
            stackpointer = radius;

            for (y = 0; y < h; y++)
            {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return bitmap;
    }
}
