package com.caharkness.support.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.caharkness.support.activities.SupportActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

public class SupportMapFragment extends MapFragment
{
	private GoogleMap google_map;

	public SupportMapFragment()
	{
		super();
		this.setRetainInstance(true);
	}

	public Context getContext()
	{
		return this.getActivity();
	}

	public SupportActivity getSupportActivity()
	{
		return (SupportActivity) getActivity();
	}

	public GoogleMap getMap()
	{
		return google_map;
	}

	@Override
	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		this.getMapAsync(new OnMapReadyCallback()
		{
			@Override
			public void onMapReady(GoogleMap map)
			{
				SupportMapFragment
					.this
					.google_map = map;

				SupportMapFragment
					.this
					.onMapReady();
			}
		});
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle bundle)
	{
		View view = super.onCreateView(inflater, group, bundle);

		view.setLayoutParams(
			new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT));

		return view;
	}

	@Override
	public void onPause()
	{
		super.onPause();
	}

	//
	//
	//

	public void onMapReady()
	{
	}
}
