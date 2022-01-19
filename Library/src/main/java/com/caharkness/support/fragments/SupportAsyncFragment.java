package com.caharkness.support.fragments;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.caharkness.support.R;
import com.caharkness.support.SupportApplication;
import com.caharkness.support.activities.SupportActivity;
import com.caharkness.support.models.SupportBundle;
import com.caharkness.support.utilities.SupportColors;
import com.caharkness.support.utilities.SupportView;
import com.caharkness.support.views.SupportLinearLayout;

public class SupportAsyncFragment extends Fragment
{
	//
	//
	//
	//
	//
	//
	//

	private Bundle			    data;
	private SupportLinearLayout layout;
	private Boolean             animate;

	public SupportAsyncFragment()
	{
		super();
		setRetainInstance(true);
	}

    /**
     *  Returns this fragment sliding in from the right.
     *  Use in conjunction with SupportActivity.setContentFragment()
     *  Or SupportActivity.setMenuFragment().
     */
	public SupportAsyncFragment slideInRight()
	{
		SupportActivity
            .setPendingAnimations(
			    R.animator.fragment_enter_right,
			    R.animator.fragment_exit_left);

		animate = false;
		return this;
	}

	/**
     *  Returns this fragment sliding in from the left.
     *  Use in conjunction with SupportActivity.setContentFragment()
     *  Or SupportActivity.setMenuFragment().
     */
	public SupportAsyncFragment slideInLeft()
	{
	    SupportActivity
		    .setPendingAnimations(
			    R.animator.fragment_enter_left,
			    R.animator.fragment_exit_right);

	    animate = false;
		return this;
	}

    /**
     *  Returns this fragment after setting the bundle data.
     */
	public SupportAsyncFragment setData(Bundle data)
	{
		this.data = data;
		return this;
	}

    /**
     *  Get the bundle of data associated with this fragment.
     *  If the fragment arguments bundle is not null, merge them.
     */
	public Bundle getData()
	{
		if (data == null)
			data = new Bundle();

		if (getArguments() != null)
			data.putAll(getArguments());

		return data;
	}

	//
	//
	//
	//
	//
	//
	//

	public SupportActivity getSupportActivity()
	{
		return
        (SupportActivity)
            getActivity();
	}

	public Context getContext()
	{
		return
		getSupportActivity()
		    .getContext();
	}

    /**
     *  Do not override this method.
     *  Override onCreateView() with zero parameters instead.
     */
	@SuppressLint("StaticFieldLeak")
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle bundle)
	{
		if (layout == null)
		{
			layout = new SupportLinearLayout(getContext());
			layout.setBackgroundColor(SupportColors.getBackgroundColor(getContext()));
			layout.setLayoutParams(
				new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT));

			View loading_view = onCreateLoadingView();

			if (loading_view != null)
				layout.addView(loading_view);
		}

		if (layout.getChildCount() > 0)
		{
			//
			//	Make the fragment asynchronous if there's a loading view.
			//
			new AsyncTask<Void, Void, Void>()
			{
				View view;
				Exception exception;

				@Override
				protected Void doInBackground(Void... v)
				{
					try
					{
						view =
							SupportAsyncFragment
								.this
								.onCreateView();
					}
					catch (Exception x) { exception = x; }
					return null;
				}

				@Override
				protected void onPostExecute(Void v)
				{
					if (exception != null)
						SupportApplication.log(exception);

					setContentView(view);
				}

			}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		else
		{
			//
			//	Otherwise, just create the fragment on the UI thread.
			//
			setContentView(onCreateView());
		}

		return layout;
	}

	public void setContentView(final View view)
	{
		if (view != null)
		{
			layout.removeAllViews();
			layout.addView(view);

			//
			//
			//

			view.setVisibility(View.INVISIBLE);
			view.post(new Runnable()
			{
				@Override
				public void run()
				{
				    view.setVisibility(View.VISIBLE);

				    /*
					if (pending_attach_animation != 0)
                    if (layout != null)
                    {
                        layout.startAnimation(
                            AnimationUtils.loadAnimation(
                                layout.getContext(),
                                pending_attach_animation));

                        pending_attach_animation = 0;
                        return;
                    }
                    */

				    if (animate != null && animate)
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
					{
						int cx = layout.getWidth() / 2;
						int cy = layout.getHeight() / 2;

						Animator a = ViewAnimationUtils.createCircularReveal(
							view,
							cx,
							cy,
							0f,
							(float) Math.hypot(cx, cy));

						a.setDuration(400);
						a.start();
					}
					else
					{
						/*
						drawer_layout.startAnimation(
							AnimationUtils.loadAnimation(
								getContext(),
								R.anim.fade_in));
						*/
					}

					onCreateFinished();
				}
			});
		}
		else
		{
		}
	}

    /**
     *  Override this method to create this fragment's view.
     *  All work is done in a background thread, network requests are allowed.
     */
	public View onCreateView()
	{
		return null;
	}

    /**
     *  Override this method to display a view while work is being done.
     */
	public View onCreateLoadingView()
	{
		return null;
	}

    /**
     *  Override this method to run code after the fragment's view has been created.
     */
	public void onCreateFinished()
	{
	}

	@Override
	public void onPause()
	{
	    /*
		if (pending_detach_animation != 0)
			if (layout != null)
			{
				layout.startAnimation(
					AnimationUtils.loadAnimation(
						layout.getContext(),
						pending_detach_animation));

				pending_detach_animation = 0;
			}
		*/

		super.onPause();
	}

	//
	//
	//
	//
	//
	//
	//

	public boolean hasView()
	{
		return
			layout != null &&
			layout.getChildCount() > 0;
	}

	//
	//
	//
	//
	//
	//
	//

    public Runnable getRouteBack(final SupportAsyncFragment fragment)
    {
        return
        new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    fragment
                        .getSupportActivity()
                        .setContentFragment(
                            SupportAsyncFragment.this.getClass()
                                .newInstance()
                                .slideInLeft());
                }
                catch (Exception x) { throw new RuntimeException(x.getMessage()); }
            }
        };
    }

    public void navigateTo(final SupportAsyncFragment fragment)
    {
        getSupportActivity().setBackAction(getRouteBack(fragment));
        getSupportActivity().setNavigationButtonAsBack();
        getSupportActivity().setContentFragment(fragment);
    }

    /**
     *  Replace this fragment with another fragment.
     */
	public void replaceWith(Fragment fragment)
	{
		getSupportActivity()
			.setFragment(
				getData().getInt("view_id"),
				fragment,
				getData().getString("fragment_name"));
	}

    /**
     *  Refresh this fragment using the data bundle in its current state.
     */
	public void recreate()
	{
		try
		{
			replaceWith(
				getClass()
					.newInstance()
					.setData(getData()));
		}
		catch (Exception x) { throw new RuntimeException(x.getMessage()); }
	}
}
