package vn.vfossa.app;

import java.util.ArrayList;

import vn.vfossa.shareapp.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

public class AppAdapter extends BaseAdapter {

	private ArrayList<Bitmap> listImage;
	private Activity activity;
	private int[] checkedState;
	private boolean[] checkboxSelected;

	public AppAdapter(Activity activity, ArrayList<Bitmap> listImage,
			int[] checkedState) {
		super();
		this.listImage = listImage;
		this.activity = activity;
		this.checkedState = checkedState;
		this.checkboxSelected = new boolean[listImage.size()];
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listImage.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listImage.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static class ViewHolder {
		public ImageView imgViewItem;
		public CheckBox checkBox;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder view;
		LayoutInflater inflator = activity.getLayoutInflater();

		if (convertView == null) {
			view = new ViewHolder();
			convertView = inflator.inflate(R.layout.item_layout, null);

			view.imgViewItem = (ImageView) convertView
					.findViewById(R.id.imageItem);
			view.checkBox = (CheckBox) convertView
					.findViewById(R.id.checkBoxItem);

			convertView.setTag(view);
		} else {
			view = (ViewHolder) convertView.getTag();
		}

		view.imgViewItem.setImageBitmap(listImage.get(position));
		view.imgViewItem.setTag(position);
		view.checkBox.setTag(position);
		
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

		//notifyDataSetChanged();
		return convertView;
	}

}

