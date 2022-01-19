package com.caharkness.support.utilities;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

import com.caharkness.support.SupportApplication;

import java.util.HashMap;
import java.util.Map;

public class SupportSound
{
    private static HashMap<String, MediaPlayer> sound_map;

    public static HashMap<String, MediaPlayer> getSoundMap()
    {
        if (sound_map == null)
            sound_map = new HashMap<String, MediaPlayer>();

        return sound_map;
    }

	public static void play(final String name)
	{
		try
		{
			if (getSoundMap().containsKey(name))
			{
			    MediaPlayer player = getSoundMap().get(name);

				player.seekTo(0);
				player.start();
			}
			else
			{
				AssetManager assets = SupportApplication.getInstance().getAssets();
				MediaPlayer player = new MediaPlayer();

				AssetFileDescriptor descriptor = assets.openFd(name);

				player.setDataSource(
					descriptor.getFileDescriptor(),
					descriptor.getStartOffset(),
					descriptor.getLength());

				getSoundMap().put(name, player);

				player.prepare();
				play(name);
			}
		}
		catch (Exception x) { x.printStackTrace(); }
	}

	public static void loop(String name)
	{
		try
		{
			if (getSoundMap().containsKey(name))
			{

				MediaPlayer player = getSoundMap().get(name);

				if (player.isPlaying())
					return;

				player.setLooping(true);

				player.seekTo(0);
				player.start();
			}
			else
			{
				AssetManager assets = SupportApplication.getInstance().getAssets();
				MediaPlayer player = new MediaPlayer();

				AssetFileDescriptor descriptor = assets.openFd(name);

				player.setDataSource(
					descriptor.getFileDescriptor(),
					descriptor.getStartOffset(),
					descriptor.getLength());

				getSoundMap().put(name, player);

				player.prepare();
				loop(name);
			}
		}
		catch (Exception x) {}
	}

	public static void pauseAll()
	{
		for (Map.Entry<String, MediaPlayer> item : getSoundMap().entrySet())
			item.getValue().pause();
	}

	public static void resumeAll()
	{
		for (Map.Entry<String, MediaPlayer> item : getSoundMap().entrySet())
		{
			MediaPlayer player = item.getValue();

			if (player.isLooping())
				player.start();
		}
	}

	public static void stopAll()
	{
		for (Map.Entry<String, MediaPlayer> item : getSoundMap().entrySet())
			item.getValue().stop();

		getSoundMap().clear();
	}

	public static void stop(String name)
	{
		if (getSoundMap().containsKey(name))
		{
			getSoundMap().get(name).stop();
			getSoundMap().remove(name);
		}
	}
}
