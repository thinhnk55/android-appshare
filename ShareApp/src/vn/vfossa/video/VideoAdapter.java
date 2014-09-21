package vn.vfossa.video;

import java.text.DecimalFormat;
import java.util.ArrayList;

import vn.vfossa.database.FilesData;
import vn.vfossa.shareapp.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoAdapter extends ArrayAdapter<FilesData> {

	private LayoutInflater mInflator;

	private int[] checkedState;
	private boolean[] checkboxChecked;

	public VideoAdapter(Context context, ArrayList<FilesData> videos,
			int[] checkedState) {
		super(context, R.layout.media_item_layout, videos);
		this.checkedState = checkedState;
		this.checkboxChecked = new boolean[videos.size()];

		mInflator = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		View rowView = view;
		VideoHolder holder = null;

		if (rowView == null) {
			rowView = mInflator.inflate(R.layout.media_item_layout, null, true);
			holder = new VideoHolder();
			holder.videoTitle = (TextView) rowView
					.findViewById(R.id.mediaTitle);
			holder.size = (TextView) rowView.findViewById(R.id.mediaSize);
			holder.checkBox = (CheckBox) rowView
					.findViewById(R.id.checkBoxItem);
			holder.imageView = (ImageView) rowView
					.findViewById(R.id.image_media);
			rowView.setTag(holder);
		} else {
			holder = (VideoHolder) rowView.getTag();
		}

		holder.videoTitle.setText(getItem(position).getName());
		DecimalFormat dec = new DecimalFormat("0.00");
		holder.size.setText(dec.format(getItem(position).getSize()).concat(
				" MB"));

		if (getItem(position).getImage() != null) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPurgeable = true;

			Bitmap bitmap = BitmapFactory.decodeByteArray(getItem(position)
					.getImage(), 0, getItem(position).getImage().length,
					options);

			Bitmap itemImage = Bitmap
					.createScaledBitmap(bitmap, 100, 100, true);
			holder.imageView.setImageBitmap(itemImage);
		} else {
			holder.imageView.setImageResource(R.drawable.video);
		}

		holder.checkBox.setId(position);
		holder.imageView.setId(position);
		holder.checkBox.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				int id = cb.getId();
				if (checkboxChecked[id]) {
					cb.setChecked(false);
					checkboxChecked[id] = false;
				} else {
					cb.setChecked(true);
					checkboxChecked[id] = true;
				}
			}
		});

		holder.checkBox.setChecked(checkboxChecked[position]);

		return rowView;
	}

	class VideoHolder {
		CheckBox checkBox;
		TextView size;
		TextView videoTitle;
		ImageView imageView;
	}

}