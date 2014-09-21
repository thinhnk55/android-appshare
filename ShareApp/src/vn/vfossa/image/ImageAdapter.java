package vn.vfossa.image;

import java.util.ArrayList;

import vn.vfossa.shareapp.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

public class ImageAdapter extends ArrayAdapter<Bitmap> {

	private LayoutInflater mInflator;

	private int[] checkedState;
	private boolean[] checkboxSelected;

	public ImageAdapter(Context context, ArrayList<Bitmap> listImage,
			int[] checkedState) {
		super(context, R.layout.item_layout, listImage);
		this.checkedState = checkedState;
		this.checkboxSelected = new boolean[listImage.size()];

		mInflator = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
	}

	public class ViewHolder {
		public ImageView imgViewItem;
		public CheckBox checkBox;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder view;

		if (convertView == null) {
			view = new ViewHolder();
			convertView = mInflator.inflate(R.layout.item_layout, null);

			view.imgViewItem = (ImageView) convertView
					.findViewById(R.id.imageItem);
			view.checkBox = (CheckBox) convertView
					.findViewById(R.id.checkBoxItem);

			convertView.setTag(view);
		} else {
			view = (ViewHolder) convertView.getTag();
		}

		view.imgViewItem.setImageBitmap(getItem(position));
		view.imgViewItem.setId(position);
		view.checkBox.setId(position);

		view.checkBox.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				int id = cb.getId();
				if (checkboxSelected[id]) {
					cb.setChecked(false);
					checkboxSelected[id] = false;
				} else {
					cb.setChecked(true);
					checkboxSelected[id] = true;
				}
			}
		});

		view.checkBox.setChecked(checkboxSelected[position]);

		// notifyDataSetChanged();
		return convertView;
	}

}
