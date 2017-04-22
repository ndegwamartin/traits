package com.olympuskid.tr8ts.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.olympuskid.tr8ts.R;
import com.olympuskid.tr8ts.models.SharelistItem;
import com.olympuskid.tr8ts.utils.Utils;

public class SharelistAdapter extends ArrayAdapter<SharelistItem> {
	private final Context mContext;
	private final int layoutResourceId;
	private List<SharelistItem> data;

	public SharelistAdapter(Context context, int resource) {
		super(context, resource);
		mContext = context;
		layoutResourceId = resource;
	}

	public SharelistAdapter(Context context, int resource, List<SharelistItem> objects) {
		super(context, resource, objects);
		mContext = context;
		layoutResourceId = resource;
		data = objects;
	}

	public SharelistAdapter(Context context, int resource, int textViewResourceId, List<SharelistItem> objects) {
		super(context, resource, textViewResourceId, objects);
		mContext = context;
		layoutResourceId = resource;
		data = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolderItem viewHolder;

		if (convertView == null) {
			// inflate the layout
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			convertView = inflater.inflate(layoutResourceId, parent, false);

			viewHolder = new ViewHolderItem();
			viewHolder.shareIconTitle = (TextView) convertView.findViewById(R.id.shareTitle);
			viewHolder.shareIconImage = (ImageView) convertView.findViewById(R.id.shareIcon);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolderItem) convertView.getTag();
		}

		viewHolder.shareIconTitle.setText(data.get(position).getShareTitle());
		viewHolder.shareIconImage.setImageResource(data.get(position).getImageResourceId());
		viewHolder.shareIconTitle.setTag(Utils.getString(position));

		return convertView;

	}

	static class ViewHolderItem {
		TextView shareIconTitle;
		ImageView shareIconImage;
	}

}
