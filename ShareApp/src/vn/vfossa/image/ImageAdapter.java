package vn.vfossa.image;

import java.util.ArrayList;

import vn.vfossa.additionalclass.CheckableAdapter;
import vn.vfossa.database.FilesData;
import vn.vfossa.shareapp.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

public class ImageAdapter extends CheckableAdapter<FilesData> {

	private LayoutInflater mInflator;

	public ImageAdapter(Context context, ArrayList<FilesData> listImage) {
		super(context, R.layout.item_layout, listImage);

		mInflator = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
	}

	public class ViewHolder {
		public ImageView imgViewItem;
		public CheckBox checkBox;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder view;

		if (convertView == null) {
			view = new ViewHolder();
			convertView = mInflator
					.inflate(R.layout.item_layout, parent, false);

			view.imgViewItem = (ImageView) convertView
					.findViewById(R.id.imageItem);
			view.checkBox = (CheckBox) convertView
					.findViewById(R.id.checkBoxItem);

			convertView.setTag(view);
		} else {
			view = (ViewHolder) convertView.getTag();
		}

		if (getItem(position).getImage() != null) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPurgeable = true;

			Bitmap bitmap = BitmapFactory.decodeByteArray(getItem(position)
					.getImage(), 0, getItem(position).getImage().length,
					options);

			if (bitmap != null) {
				Bitmap itemImage = Bitmap.createScaledBitmap(bitmap, 100, 100,
						true);
				view.imgViewItem.setImageBitmap(itemImage);
			} else {
				view.imgViewItem.setImageResource(R.drawable.image);
			}
		} else {
			view.imgViewItem.setImageResource(R.drawable.image);
		}
		view.checkBox.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				if (!cb.isChecked()) {
					checkedList.remove(getItem(position));
				} else {
					checkedList.add(getItem(position));
				}
			}
		});

		view.checkBox.setChecked(checkedList.contains(getItem(position)));

		// notifyDataSetChanged();
		return convertView;
	}
}
