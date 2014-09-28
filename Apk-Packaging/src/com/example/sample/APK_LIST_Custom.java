package com.example.sample;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class APK_LIST_Custom extends ArrayAdapter<String> {

	private final ArrayList<String> Applist;
	private final ArrayList<Bitmap> Iconlist1;
	LayoutInflater inflater;

	public APK_LIST_Custom(Context context, ArrayList<String> Applist) {

		super(context, R.layout.apk_list_custom, Applist);
		this.Applist = Applist;

		Iconlist1 = APK_list.AppIcon;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.apk_list_custom, parent,
					false);
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.label);
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			convertView.setTag(holder);
		}

		else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.text.setTextColor(Color.BLACK);
		holder.text.setText(Applist.get(position));
		holder.icon.setImageBitmap(Iconlist1.get(position));
		return convertView;
	}

	private static class ViewHolder {
		TextView text;
		ImageView icon;
	}

}
