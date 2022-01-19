package com.caharkness.support.fragments;

import android.os.Environment;
import android.view.View;

import com.caharkness.support.R;
import com.caharkness.support.SupportApplication;
import com.caharkness.support.models.SupportBundle;
import com.caharkness.support.utilities.SupportFiles;
import com.caharkness.support.utilities.SupportColors;
import com.caharkness.support.views.SupportMenuItemView;
import com.caharkness.support.views.SupportMenuView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SupportFileBrowserFragment extends SupportAsyncFragment
{
	File file;
    SupportMenuView list_view;

	public SupportFileBrowserFragment()
	{
		super();
	}

	public File getHome()
	{
		return Environment.getExternalStorageDirectory();
	}

	public File getFile()
	{
		if (file == null)
		{
			file		= getHome();
			String path = getData().getString("path", "");

			if (path.length() > 0)
				file = new File(path);
		}

		return file;
	}

	public SupportMenuView getListView()
    {
        return list_view;
    }

	//
    //
    //
    //  Area: Behavior flags
    //
    //
    //

	public boolean getNavigationEnabled()
	{
		return true;
	}

	public boolean getShowParentDirectory()
	{
		return true;
	}

	public boolean getViewFileDetailsBeforeSelecting()
    {
        return false;
    }

	public boolean getRootBrowsingEnabled()
    {
        return false;
    }

    public boolean getShowHiddenFilesEnabled()
    {
        return false;
    }

    public boolean getSelectingEnabled()
	{
		return false;
	}

    public boolean isAtRoot()
	{
		if (getFile().getAbsolutePath().length() < 2)
			return true;

		if (!getRootBrowsingEnabled())
		{
			String current_path				= getFile().getAbsolutePath();
			String external_storage_path	= Environment.getExternalStorageDirectory().getAbsolutePath();

			if (current_path.equals(external_storage_path))
				return true;
		}

		return false;
	}

    //
    //
    //
    //  Area: Navigation
    //
    //
    //

	public void navigateTo(File file)
    {
        try
        {
			if (getMode() != Mode.BROWSING)
				return;

            replaceWith(
                getClass()
                    .newInstance()
                    .slideInRight()
					.setData(
						new SupportBundle()
							.set("path", file.getAbsolutePath())
							.getBundle()));
        }
        catch (Exception x) { throw new RuntimeException(x.getMessage()); }
    }

    public void navigateBackTo(File file)
    {
        try
        {
			if (getMode() != Mode.BROWSING)
				return;

            replaceWith(
                getClass()
                    .newInstance()
                    .slideInLeft()
					.setData(
						new SupportBundle()
							.set("path", file.getAbsolutePath())
							.getBundle()));
        }
        catch (Exception x) { throw new RuntimeException(x.getMessage()); }
    }

    public void navigateUp()
    {
		if (getMode() != Mode.BROWSING)
			return;

        navigateBackTo(getFile().getParentFile());
    }

    //
    //
    //
    //
    //
    //
    //

	public Runnable getFolderAction(final File file, SupportMenuItemView view)
	{
		return new Runnable()
		{
			@Override
			public void run()
			{
				if (getSelectingEnabled())
					if (getMode() == Mode.SELECTING)
					{
						if (getSelection().contains(file))
							deselect(file);
						else
							select(file);

						return;
					}

				if (getNavigationEnabled())
					navigateTo(file);
			}
		};
	}

	public Runnable getFolderAlternateAction(final File file, SupportMenuItemView view)
	{
		return new Runnable()
		{
			@Override
			public void run()
			{
				if (getSelectingEnabled())
					select(file);
			}
		};
	}

	public Runnable getFileAction(final File file, SupportMenuItemView view)
	{
		return new Runnable()
		{
			@Override
			public void run()
			{
				if (getSelectingEnabled())
					if (getMode() == Mode.SELECTING)
					{
						if (getSelection().contains(file))
							deselect(file);
						else
							select(file);

						return;
					}

				if (getViewFileDetailsBeforeSelecting())
				{
					if (getNavigationEnabled())
						navigateTo(file);
				}
				else
					onFileChosen(file);
			}
		};
	}

	public Runnable getFileAlternateAction(final File file, SupportMenuItemView view)
	{
		return new Runnable()
		{
			@Override
			public void run()
			{
				if (getSelectingEnabled())
					select(file);
			}
		};
	}

	public SupportMenuItemView getParentDirectoryListItemView()
	{
		return
			new SupportMenuItemView(getContext())
				.setLeftIcon(R.drawable.ic_reply)
				.setTitle("Parent directory")
				.setAction(new Runnable()
				{
					@Override
					public void run()
					{
						if (getNavigationEnabled())
							navigateUp();
					}
				});
	}

	public SupportMenuItemView getFileListItemView(final File file)
	{
		final SupportMenuItemView item = new SupportMenuItemView(getContext());

		if (file.isDirectory())
		{
			item.setLeftIcon(
				R.drawable.ic_folder,
				SupportColors.getAccentColor(getContext()));

			item.setAction(
				getFolderAction(
					file,
					item));

			item.setAlternateAction(
				getFolderAlternateAction(
					file,
					item));

			item.addTag("folder");
			item.addTag("directory");
			item.addTag("selectable");
			item.putMetadata("folder_path", file.getAbsolutePath());
			item.putMetadata("path",		file.getAbsolutePath());
			item.putMetadata("kind",		"folder");
			item.putMetadata("type",		"folder");
		}
		else
		{
			item.setLeftIcon(
				R.drawable.ic_insert_drive_file,
				SupportColors.getForegroundColor(getContext()));

			item.setAction(
				getFileAction(
					file,
					item));

			item.setAlternateAction(
				getFileAlternateAction(
					file,
					item));

			item.addTag("file");
			item.addTag("selectable");
			item.putMetadata("file_path",	file.getAbsolutePath());
			item.putMetadata("path",		file.getAbsolutePath());
			item.putMetadata("kind",		"file");
			item.putMetadata("type",		"file");
		}

		item.setTitle(file.getName());
		return item;
	}

	public List<SupportMenuItemView> getFileActionListItemViews(final File file)
    {
        ArrayList<SupportMenuItemView> items = new ArrayList<>();

        items.add(
            new SupportMenuItemView(getContext())
                .setTitle("Select")
                .setAction(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        onFileChosen(file);
                    }
                }));

        return items;
    }

    public SupportMenuItemView getListItemViewFromFile(File file)
	{
		for (SupportMenuItemView v : getListView().getItemsContainingMetadata("path"))
			if (v.getMetadata("path").equals(file.getAbsolutePath()))
				return v;

		return null;
	}

    //
	//
	//
	//	Area: Events
	//
	//
	//

    public void onFileChosen(File file)
    {
    }

    public void onFileSelected(File file)
	{
		if (!getSelectingEnabled())
			return;

		if (getSelection().contains(file))
			return;

		getSelection().add(file);

		SupportMenuItemView
			view = getListItemViewFromFile(file);
			view.addTag("selected");
			view.setRightIcon(
				R.drawable.ic_check,
				SupportColors.getAccentColor(getContext()));

		setMode(Mode.SELECTING);
		onSelectionChanged();
	}

	public void onFileDeselected(File file)
	{
		getSelection().remove(file);

		SupportMenuItemView
			view = getListItemViewFromFile(file);
			view.removeTag("selected");
			view.setRightIcon(null);

		onSelectionChanged();
	}

	public void onSelectionChanged()
	{
		if (getSelection().size() < 1)
			setMode(Mode.BROWSING);
	}

	public void onViewDirectory()
	{
	}

	public void onViewFile()
	{
	}

	public void onView()
	{
	}

    public void chooseFileByName()
	{
		getSupportActivity().showInputDialog(
			"File Name",
			new Runnable()
			{
				@Override
				public void run()
				{
					onFileChosen(
						new File(
							getFile().getAbsolutePath() + "/" +
							SupportApplication.getString("_input")));
				}
			});
	}

	//
	//
	//
	//	Area: File selection
	//
	//
	//

	public enum Mode
	{
		BROWSING,
		SELECTING
	}

	private Mode mode;
	private ArrayList<File> selection;

	public Mode getMode()
	{
		if (mode == null)
			mode = Mode.BROWSING;

		return mode;
	}

	public void setMode(Mode m)
	{
		mode = m;
	}

	public ArrayList<File> getSelection()
	{
		if (selection == null)
			selection = new ArrayList<>();

		return selection;
	}

	public void selectAll()
	{
		for (SupportMenuItemView view : getListView().getItemsContainingMetadata("path"))
			select(new File(view.getMetadata("path")));
	}

	public void select(File file)
	{
		onFileSelected(file);
	}

	public void deselect(File file)
	{
		onFileDeselected(file);
	}

	public void deselectAll()
	{
		while (getSelection().size() > 0)
			deselect(getSelection().get(0));
	}

	//
	//
	//
	//	Area: View Creation
	//
	//
	//

	@Override
	public View onCreateView()
	{
        list_view = new SupportMenuView(getContext());

		//
		//
		//

		if (getFile().isDirectory())
		{
			//
			//
			//
			//	Area: Directory view populating
			//
			//
			//

			if (!isAtRoot())
				if (getShowParentDirectory())
					list_view.addItem(getParentDirectoryListItemView());

			List<File> files = SupportFiles.directory(file);

			for (File file : files)
			{
				//
				//	If there's a search filter applied and the file name doesn't match it,
				//	Skip listing this file entirely.
				//
				if (getData().getString("search", "").length() > 0)
					if (!file.getName().contains(getData().getString("search", "")))
						continue;

				if (file.getName().startsWith("."))
					if (!getShowHiddenFilesEnabled())
						continue;

				list_view.addItem(getFileListItemView(file));
			}

			onViewDirectory();
            onView();
		}
		else
		{
			//
			//
			//
			//	Area: File view populating
			//
			//
			//

			list_view.addItem(
				new SupportMenuItemView(getContext())
					.setLeftIcon(R.drawable.ic_insert_drive_file)
					.setTitle(getFile().getName()));
					/*.setTable(
						new String[][]
						{
							{"Size", getFile().length() + " bytes"},
							{"Path", getFile().getAbsolutePath()},
						},
						SupportColors.getAccentColor(getContext())));*/

			list_view.addItem(
				new SupportMenuItemView(getContext())
					.setLabel("Actions"));

            for (SupportMenuItemView item : getFileActionListItemViews(getFile()))
                list_view.addItem(item);

			onViewFile();
            onView();
		}

		//
		//
		//

		int refresh_color =
			getSupportActivity()
				.getToolbar()
				.getToolbarBackgroundColor();

		if (SupportColors.isLight(refresh_color))
			refresh_color = SupportColors.getAccentColor(getContext());

		if (SupportColors.isLight(refresh_color))
			refresh_color = SupportColors.get("black");

        return list_view
			.getAsSwipeRefreshLayout(
				new Runnable()
				{
					@Override
					public void run()
					{
						if (getNavigationEnabled())
							recreate();
					}
				});
	}
}
