package com.cs446.kluster.views.fragments;

import retrofit.RestAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

import com.cs446.kluster.R;
import com.cs446.kluster.data.PhotoProvider;
import com.cs446.kluster.data.PhotoProvider.PhotoOpenHelper;
import com.cs446.kluster.net.AuthKlusterRestAdapter;
import com.cs446.kluster.net.KlusterService;
import com.cs446.kluster.net.PhotosCallback;

public class PhotoGridFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
	PhotoGridAdapter mAdapter;
	String mEventId;
	String mEventName;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.photogrid_layout, container, false);
		
		String[] cols = new String[] { "location" };
		int[]   views = new int[]   { R.id.photogrid_imgBackground };
		
		mAdapter = new PhotoGridAdapter(getActivity(), R.layout.photogridcell_layout, null, cols, views, 0);
		
		GridView gridView=(GridView)view.findViewById(R.id.photoGrid);
		gridView.setAdapter(mAdapter);
		
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				PhotoViewerFragment fragment = new PhotoViewerFragment();
				Cursor cursor=mAdapter.getCursor();
				cursor.moveToPosition(position);
				Bundle bundle = new Bundle();
				bundle.putString("url", cursor.getString(cursor.getColumnIndex(PhotoOpenHelper.COLUMN_URL)));
				bundle.putString("up", cursor.getString(cursor.getColumnIndex(PhotoOpenHelper.COLUMN_RATING_UP)));
				bundle.putString("down", cursor.getString(cursor.getColumnIndex(PhotoOpenHelper.COLUMN_RATING_DOWN)));
				bundle.putString("photoid", cursor.getString(cursor.getColumnIndex(PhotoOpenHelper.COLUMN_PHOTO_ID)));
				bundle.putString("userid", cursor.getString(cursor.getColumnIndex(PhotoOpenHelper.COLUMN_USER_ID)));
				
				fragment.setArguments(bundle);
				getFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack(fragment.toString()).commit();
			}
			
		});
		
        /* Start loader */  
        getLoaderManager().initLoader(0, null, this);  
        
        mEventName = getArguments().getString("eventname");
        getActivity().getActionBar().setTitle(mEventName);
        
        
		RestAdapter restAdapter = new AuthKlusterRestAdapter()
		.build();	
		KlusterService service = restAdapter.create(KlusterService.class);
		
		service.getPhotos(getArguments().getString("eventid"), new PhotosCallback(getActivity()));
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		if (getActivity().getFragmentManager().getBackStackEntryCount() > 0) {
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
		mEventId = getArguments().getString("eventid");
		
		String[] selectionArgs = {mEventId};
		return new CursorLoader(getActivity(), PhotoProvider.CONTENT_URI, null, "eventid = ?", selectionArgs, null);
	}
		
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mAdapter.changeCursor(cursor);
	}
	
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.changeCursor(null);
	}
}
