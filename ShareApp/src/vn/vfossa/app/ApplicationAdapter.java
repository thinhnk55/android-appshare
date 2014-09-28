package vn.vfossa.app;

import java.util.ArrayList;

import vn.vfossa.additionalclass.CheckableAdapter;
import vn.vfossa.shareapp.R;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

public class ApplicationAdapter extends CheckableAdapter<ApplicationInfo> {
	private LayoutInflater mInflator;

	// private static final String type = "app";

	public ApplicationAdapter(Context context,
			ArrayList<ApplicationInfo> appsList) {
		super(context, R.layout.item_layout, appsList);
		mInflator = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
	}

	public static class ViewHolder {
		public ImageView imgViewItem;
		public CheckBox checkBox;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflator
					.inflate(R.layout.item_layout, parent, false);

			holder.imgViewItem = (ImageView) convertView
					.findViewById(R.id.imageItem);
			holder.checkBox = (CheckBox) convertView
					.findViewById(R.id.checkBoxItem);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ApplicationInfo appInfo = getItem(position);
		if (appInfo != null) {
			holder.imgViewItem.setImageDrawable(appInfo.loadIcon(getContext()
					.getPackageManager()));
		}

		holder.checkBox.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				if (!cb.isChecked()) {
					checkedList.remove(getItem(position));
				} else {
					checkedList.add(getItem(position));
				}
			}
		});

		holder.checkBox.setChecked(checkedList.contains(appInfo));

		return convertView;
	}
};