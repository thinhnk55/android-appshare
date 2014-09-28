package vn.vfossa.additionalclass;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

abstract public class CheckableAdapter<T> extends ArrayAdapter<T>{

	protected List<T> checkedList = new ArrayList<T>();
	
	public CheckableAdapter(Context context, int resource, List<T> objects) {
		super(context, resource, objects);
	}
	
	public List<T> getCheckedList() {
		return checkedList;
	}
}
