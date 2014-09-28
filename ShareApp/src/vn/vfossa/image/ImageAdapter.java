package vn.vfossa.image;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import vn.vfossa.additionalclass.CheckableAdapter;
import vn.vfossa.database.DatabaseHandler;
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
import android.widget.Filter;
import android.widget.ImageView;

public class ImageAdapter extends CheckableAdapter<FilesData> {

	private LayoutInflater mInflator;

	private Context context;
	private ArrayList<FilesData> listImage;

	private static final String type = "image";

	public ImageAdapter(Context context, ArrayList<FilesData> listImage) {
		super(context, R.layout.item_layout, listImage);
		this.context = context;
		this.listImage = listImage;

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
			convertView = mInflator.inflate(R.layout.item_layout, parent,
					false);

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

	@Override
	public Filter getFilter() {
		return new Filter() {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				DatabaseHandler db = new DatabaseHandler(context);
				ArrayList<FilesData> images = new ArrayList<FilesData>();
				List<FilesData> allImages = db.getAllFileWithType(type);
				for (FilesData sd : allImages) {
					FilesData image = new FilesData();
					image.setID(sd.getID());
					image.setName(sd.getName());
					image.setPath(sd.getPath());
					image.setSize(sd.getSize());
					byte[] data = sd.getImage();
					if (data != null) {
						image.setImage(data);
					} else {
						image.setImage(null);
					}

					images.add(image);
				}
				FilterResults results = new FilterResults();
				ArrayList<FilesData> filter = new ArrayList<FilesData>();
				constraint = constraint.toString().toLowerCase(Locale.getDefault());

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPurgeable = true;

				if (constraint != null && constraint.toString().length() > 0) {
					for (int i = 0; i < images.size(); i++) {
						String strName = images.get(i).getName();
						if (strName.toLowerCase(Locale.getDefault()).contains(
								constraint.toString())) {
							filter.add(images.get(i));
						}
					}
				}
				if (constraint == null || constraint.toString().length() == 0) {
					for (int i = 0; i < images.size(); i++) {
						filter.add(images.get(i));
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
					listImage.clear();
					@SuppressWarnings("unchecked")
					ArrayList<FilesData> items = new ArrayList<FilesData>(
							(ArrayList<FilesData>) results.values);

					if (items.size() > 0) {
						for (FilesData item : items) {
							listImage.add(item);
						}
					}
					notifyDataSetChanged();
				}

			}

		};
	}

}
