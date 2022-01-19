package com.caharkness.support.utilities;

import android.content.Intent;
import android.net.Uri;

import androidx.core.content.FileProvider;

import com.caharkness.support.SupportApplication;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("all")
public class SupportFiles
{
	public static void clone(File original, String name, File directory)
	{
		try
		{
			if (!directory.exists())
				directory.mkdir();

			File target = new File(directory, name);

			FileChannel inc = new FileInputStream(original).getChannel();
			FileChannel outc = new FileOutputStream(target).getChannel();

			inc.transferTo(0, inc.size(), outc);

			inc.close();
			outc.close();
		}
		catch (Exception x) {}
	}

	public static void copy(File a, File b)
	{
		try
		{
			if (a.isDirectory())
				FileUtils.copyDirectory(a, b, true);
			else
				FileUtils.copyFile(a, b, true);
		}
		catch (Exception x) {}
	}

	public static void move(File a, File b)
	{
		try
		{
			if (a.isDirectory())
				FileUtils.moveDirectory(a, b);
			else
				FileUtils.moveFile(a, b);
		}
		catch (Exception x) {}
	}

	public static void view(File file)
	{
		Uri uri =
			FileProvider.getUriForFile(
				SupportApplication.getInstance(),
				SupportApplication
					.getInstance()
					.getApplicationContext()
					.getPackageName() + ".provider",
				file);

		Intent intent = new Intent(Intent.ACTION_VIEW);

		intent.setData(uri);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

		intent.putExtra("path",			file.getAbsolutePath());
		intent.putExtra("file",			file.getAbsolutePath());
		intent.putExtra("directory",	file.getParentFile().getAbsolutePath());
		intent.putExtra("folder",		file.getParentFile().getAbsolutePath());
		intent.putExtra("location", 	file.getParentFile().getAbsolutePath());
		intent.putExtra("target",		file.getAbsolutePath());
		intent.putExtra("name",			file.getName());

		SupportApplication
			.getInstance()
			.startActivity(intent);
	}

	public static String read(File file)
	{
		String text = "";

		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;

			while ((line = reader.readLine()) != null)
			{
				text += line;
				text += "\n";
			}

			text = text.substring(0, text.length() - 1);
			reader.close();
		}
		catch (Exception x) {}
		return text;
	}

	public static String read(String fname)
    {
        return
        read(new File(fname));
    }

	public static boolean write(File file, String data)
	{
		try
		{
			if (file.exists())
				delete(file);

			OutputStreamWriter
				writer = new OutputStreamWriter(new FileOutputStream(file));
				writer.write(data);
				writer.close();

			return true;
		}
		catch (Exception x) {}
		return false;
	}

	public static boolean write(String fname, String data)
    {
        return
        write(
            new File(fname),
            data);
    }

	public static boolean append(File file, String data)
	{
		try
		{
			if (!file.exists())
				file.createNewFile();

			OutputStreamWriter
				writer = new OutputStreamWriter(new FileOutputStream(file));
				writer.append(data);
				writer.close();

			return true;
		}
		catch (Exception x) {}
		return false;
	}

	public static boolean append(String fname, String data)
    {
        return
        append(
            new File(fname),
            data);
    }

	public static void delete(File file)
	{
		try
		{
			if (file.isDirectory())
				FileUtils.deleteDirectory(file);
			else
				file.delete();
		}
		catch (Exception x)  {}
	}

	public static void delete(String fname)
    {
        delete(new File(fname));
    }

	public static List<File> directory(File file, boolean root)
	{
		ArrayList<File> files = new ArrayList();

		try
		{
			Process process			= Runtime.getRuntime().exec("ls -Aa " + file.getAbsolutePath());
			BufferedReader reader	= new BufferedReader(new InputStreamReader(process.getInputStream()));

			int read;
			char[] buffer		= new char[4096];
			StringBuffer output	= new StringBuffer();

			while ((read = reader.read(buffer)) > 0)
				output.append(buffer, 0, read);

			reader.close();
			process.waitFor();

			//
			//
			//

			String[] file_names =
				output
					.toString()
					.split("\\n");

			for (String file_name : file_names)
				if (file_name.length() > 0)
					files.add(new File(file.getAbsolutePath() + "/" + file_name));

			if (files.get(0).getName().equals("."))
				files.remove(0);

			if (files.get(0).getName().equals(".."))
				files.remove(0);

			return files;
		}
		catch (Exception x) {}
		return files;
	}

	public static List<File> directory(File location)
	{
		ArrayList<File> files = new ArrayList<>();

		//
		//
		//

		File[] list_attempt = location.listFiles();

		if (list_attempt != null && list_attempt.length > 0)
			for (File f : list_attempt)
				files.add(f);

		if (files.size() < 1)
			files.addAll(directory(location, true));

		if (files.size() < 0)
			return files;

		//
		//
		//

		ArrayList<String> directory = new ArrayList<>();

		for (File file : files)
			if (file != null)
				directory.add(file.getAbsolutePath());

		Collections.sort(
			directory,
			new Comparator<String>()
			{
				@Override
				public int compare(String a, String b)
				{
					return a.compareTo(b);
				}
			});

		//
		//
		//

		ArrayList<String> directory_clone = new ArrayList<>(directory);

		directory.clear();

		for (String s : directory_clone)
			if (new File(s).isDirectory())
				directory.add(s);

		for (String s : directory_clone)
			if (!new File(s).isDirectory())
				directory.add(s);

        files.clear();
        for (String item : directory)
            files.add(new File(item));

		//
		//
		//

		return files;
	}

	public static String size(File file)
	{
		String result = "";

		if (file.isDirectory())
		{
			List<File> files	= directory(file);
			int count			= files.size();
			String word			= count != 1? "items" : "item";

			if (count > 0)
			{
				result = String.format(
					"%s %s",
					count,
					word);
			}
			else
			{
				result = "Empty";
			}
		}
		else
		{
			long size				= file.length();
			ArrayList<String> units	= new ArrayList<>();

			units.addAll(
				Arrays.asList(
					new String[]
						{
							"bytes",
							"KB",
							"MB",
							"GB",
							"TB"
						}));

			result = size + " " + units.get(0);

			while (size > 1024 && units.size() > 0)
			{
				units.remove(0);
				size	= size / 1024;
				result	= size + " " + units.get(0);
			}
		}

		return result;
	}
}
