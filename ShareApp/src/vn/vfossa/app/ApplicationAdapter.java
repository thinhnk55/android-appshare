package vn.vfossa.app;

import java.util.ArrayList;
import java.util.List;

import vn.vfossa.database.DatabaseHandler;
import vn.vfossa.database.FilesData;
import vn.vfossa.shareapp.R;
import vn.vfossa.util.Utils;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.Toast;

public class ApplicationAdapter extends ArrayAdapter<ApplicationInfo> {

	private Context context;
	private ArrayList<ApplicationInfo> appsList;
	private List<ApplicationInfo> checkedList = new ArrayList<ApplicationInfo>();
	private LayoutInflater mInflator;
	private static final String type = "app";

	public ApplicationAdapter(Context context,
			ArrayList<ApplicationInfo> appsList) {
		super(context, R.layout.item_layout, appsList);
		this.context = context;
		this.appsList = appsList;
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

	public List<ApplicationInfo> getCheckedList() {
		return checkedList;
	}

	@Override
	public Filter getFilter() {
		return new Filter() {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				DatabaseHandler db = new DatabaseHandler(context);
				ArrayList<ApplicationInfo> apps = checkForLaunchIntent(context
						.getPackageManager().getInstalledApplications(
								PackageManager.GET_META_DATA));
				FilterResults results = new FilterResults();
				ArrayList<ApplicationInfo> filter = new ArrayList<ApplicationInfo>();
				constraint = constraint.toString().toLowerCase();
				Log.e("search", (String) constraint);

				if (constraint != null && constraint.toString().length() > 0) {
					for (int i = 0; i < apps.size(); i++) {
						String strName = (String) apps.get(i).loadLabel(
								getContext().getPackageManager());
						if (strName.toLowerCase().contains(
								constraint.toString())) {
							filter.add(apps.get(i));
						}
					}
				}
				if (constraint == null || constraint.toString().length() == 0) {
					for (int i = 0; i < apps.size(); i++) {
						filter.add(apps.get(i));
					}
				}

				results.count = filter.size();
				results.values = filter;
				return results;
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				if (results.count != 0) {
					appsList.clear();
					@SuppressWarnings("unchecked")
					ArrayList<ApplicationInfo> items = new ArrayList<ApplicationInfo>(
							(ArrayList<ApplicationInfo>) results.values);

					if (items.size() > 0) {
						for (ApplicationInfo item : items) {
							appsList.add(item);
						}
					}

					notifyDataSetChanged();
				}

			}

		};
	}

	private ArrayList<ApplicationInfo> checkForLaunchIntent(
			List<ApplicationInfo> list) {
		ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
		for (ApplicationInfo info : list) {
			try {
				if (context.getPackageManager().getLaunchIntentForPackage(
						info.packageName) != null) {
					applist.add(info);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return applist;
	}
};