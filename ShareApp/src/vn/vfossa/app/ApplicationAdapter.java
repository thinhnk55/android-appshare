package vn.vfossa.app;

import java.util.ArrayList;
import java.util.List;

import vn.vfossa.shareapp.R;
import vn.vfossa.util.Utils;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

public class ApplicationAdapter extends ArrayAdapter<ApplicationInfo> {
	
	private List<ApplicationInfo> checkedList = new ArrayList<ApplicationInfo>();
	private LayoutInflater mInflator;

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
			convertView = mInflator.inflate(R.layout.item_layout, parent, false);

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
			holder.imgViewItem.setImageDrawable(appInfo
					.loadIcon(getContext().getPackageManager()));
		}

		holder.checkBox.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				if (!cb.isChecked()) {
					checkedList.remove(getItem(position));
					Utils.showToast(getContext(), "remove an item " + getItem(position).name);
				} else {
					checkedList.add(getItem(position));
					Utils.showToast(getContext(), "add an item " + getItem(position).name);
				}
			}
		});
		
		holder.checkBox.setChecked(checkedList.contains(appInfo));

		return convertView;
	}

	private Drawable resize(Drawable image) {
		Bitmap b = ((BitmapDrawable) image).getBitmap();
		Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 50, 50, false);
		return new BitmapDrawable(getContext().getResources(), bitmapResized);
	}
	
	public List<ApplicationInfo> getCheckedList() {
		return checkedList;
	}
};