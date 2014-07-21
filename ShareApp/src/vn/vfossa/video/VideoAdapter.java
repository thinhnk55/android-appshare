package vn.vfossa.video;

import java.util.ArrayList;

import vn.vfossa.music.MusicAdapter.ViewHolder;
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

public class VideoAdapter extends BaseAdapter {

	private ArrayList<Bitmap> listImage;
	private Activity activity;
	private int[] checkedState;

	public VideoAdapter(Activity activity, ArrayList<Bitmap> listImage,
			int[] checkedState) {
		super();
		this.listImage = listImage;
		this.activity = activity;
		this.checkedState = checkedState;
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
		
		if (checkedState[position] == 0) {
			view.checkBox.setChecked(false);
		} else {
			view.checkBox.setChecked(true);
		}
		
		view.imgViewItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int pos = (Integer) v.getTag();
				if (checkedState[pos] == 0) {
					checkedState[pos] = 1;
					view.checkBox.setChecked(true);
				} else {
					checkedState[pos] = 0;
					view.checkBox.setChecked(false);
				}
				notifyDataSetChanged();
			}
		});

		//notifyDataSetChanged();
		return convertView;
	}

}

