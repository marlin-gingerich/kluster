package com.cs446.kluster.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.cs446.kluster.MainActivity;
import com.cs446.kluster.R;
import com.cs446.kluster.R.id;
import com.cs446.kluster.R.layout;
import com.cs446.kluster.cache.StorageAdapter;

public class EventGridAdapter extends SimpleCursorAdapter {
	
	private final LayoutInflater mInflator;
	private MainActivity mActivity;
	
	public EventGridAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		
		mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mActivity = (MainActivity)context;
	}
	
	@Override
    public void bindView(View view, Context context, Cursor cursor) { 
        TextView txtThumbnailText = (TextView)view.getTag(R.id.eventgrid_txtTitle);
        ImageView imgThumbnail = (ImageView)view.getTag(R.id.gridImageThumbnail);
        
        //TODO: Event name
       //txtThumbnailText.setText(cursor.getString(cursor.getColumnIndex("location")));
        
        String remoteurl = cursor.getString(cursor.getColumnIndex("remoteurl"));
        String local = cursor.getString(cursor.getColumnIndex("localurl"));
        
        if (!remoteurl.equals("")) {
	        StorageAdapter.getCache().loadBitmapfromUrl(remoteurl, imgThumbnail, mActivity);
        }
        else {
	        StorageAdapter.getCache().loadBitmapfromFile(local, imgThumbnail, mActivity);
        }

		imgThumbnail.invalidate();
	}
	
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
    	View view = mInflator.inflate(R.layout.eventgridcell_layout, parent, false);
    	
        view.setTag(R.id.gridImageThumbnail, view.findViewById(R.id.gridImageThumbnail));
        view.setTag(R.id.eventgrid_txtTitle, view.findViewById(R.id.eventgrid_txtTitle));
        
    	return view;
    }
}