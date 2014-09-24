package vn.vfossa.image;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.ImageView;

public class ImageAdapter extends ArrayAdapter<Bitmap> {

	private LayoutInflater mInflator;

	private Context context;
	private ArrayList<Bitmap> listImage;

	private int[] checkedState;
	private boolean[] checkboxSelected;
	private static final String type = "image";

	public ImageAdapter(Context context, ArrayList<Bitmap> listImage,
			int[] checkedState) {
		super(context, R.layout.item_layout, listImage);
		this.checkedState = checkedState;
		this.context = context;
		this.listImage = listImage;
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
				ArrayList<Bitmap> filter = new ArrayList<Bitmap>();
				constraint = constraint.toString().toLowerCase();

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPurgeable = true;

				if (constraint != null && constraint.toString().length() > 0) {
					for (int i = 0; i < images.size(); i++) {
						String strName = images.get(i).getName();
						if (strName.toLowerCase().contains(
								constraint.toString())) {
							if (images.get(i).getImage() != null) {

								Bitmap bitmap = BitmapFactory.decodeByteArray(
										images.get(i).getImage(), 0, images
												.get(i).getImage().length,
										options);

								Bitmap itemImage = Bitmap.createScaledBitmap(
										bitmap, 100, 100, true);
								filter.add(itemImage);
							} else {
								filter.add(null);
							}
						}
					}
				}
				if (constraint == null || constraint.toString().length() == 0) {
					for (int i = 0; i < images.size(); i++) {
						Bitmap bitmap = BitmapFactory.decodeByteArray(images
								.get(i).getImage(), 0,
								images.get(i).getImage().length, options);

						Bitmap itemImage = Bitmap.createScaledBitmap(bitmap,
								100, 100, true);
						filter.add(itemImage);
					}
				}

				results.count = filter.size();
				results.values = filter;
				return results;
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				if (results != null) {
					listImage.clear();
					@SuppressWarnings("unchecked")
					ArrayList<Bitmap> items = new ArrayList<Bitmap>(
							(ArrayList<Bitmap>) results.values);

					if (items.size() > 0) {
						for (Bitmap item : items) {
							listImage.add(item);
						}
					}
					notifyDataSetChanged();
				}

			}

		};
	}

}
