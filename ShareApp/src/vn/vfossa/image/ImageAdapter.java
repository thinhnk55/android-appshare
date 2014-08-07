package vn.vfossa.image;

import java.util.ArrayList;
import java.util.List;

import vn.vfossa.database.DatabaseHandler;
import vn.vfossa.database.FilesData;
import vn.vfossa.shareapp.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

	private ArrayList<Bitmap> listImage;
	private Activity activity;
	private int[] checkedState;
	private boolean[] checkboxSelected;

	public ImageAdapter(Activity activity, ArrayList<Bitmap> listImage,
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
		view.imgViewItem.setId(position);
		view.checkBox.setId(position);
		
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

