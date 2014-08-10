package vn.vfossa.app;

import java.util.ArrayList;
import java.util.List;
import vn.vfossa.shareapp.R;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

public class ApplicationAdapter extends BaseAdapter {
	private ArrayList<ApplicationInfo> appsList = null;
	private Activity activity;
	private PackageManager packageManager;
	private boolean[] checkboxSelected;

	public ApplicationAdapter(Activity activity, int textViewResourceId,
			ArrayList<ApplicationInfo> appsList) {
		super();
		this.activity = activity;
		this.appsList = appsList;
		packageManager = activity.getPackageManager();
		this.checkboxSelected = new boolean[appsList.size()];
	}

	@Override
	public int getCount() {
		return ((null != appsList) ? appsList.size() : 0);
	}

	@Override
	public ApplicationInfo getItem(int position) {
		return ((null != appsList) ? appsList.get(position) : null);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public static class ViewHolder {
		public ImageView imgViewItem;
		public CheckBox checkBox;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder view;
		LayoutInflater inflator = activity.getLayoutInflater();

		if (convertView == null) {
			view = new ViewHolder();
			convertView = inflator.inflate(R.layout.item_layout, null);
			
			view.imgViewItem = (ImageView) convertView.findViewById(R.id.imageItem);
			view.checkBox = (CheckBox) convertView.findViewById(R.id.checkBoxItem);
			convertView.setTag(view);
		} else {
			view = (ViewHolder) convertView.getTag();
		}

		ApplicationInfo data = appsList.get(position);
		if (data != null) {
			view.imgViewItem.setImageDrawable(data.loadIcon(packageManager));
		}
		
		view.checkBox.setId(position);
		view.imgViewItem.setId(position);
		view.checkBox.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CheckBox cb = (CheckBox) v;
				int id = cb.getId();
				if (checkboxSelected[id]){
					cb.setChecked(false);
					checkboxSelected[id] = false;
				} else {
					cb.setChecked(true);
					checkboxSelected[id] = true;
				}
			}
		});
		
		view.checkBox.setChecked(checkboxSelected[position]);
		
		return convertView;
	}
};