package vn.vfossa.additionalclass;

import java.util.List;

public interface CheckableAndFilterableActivity {
	abstract public List<?> getCheckedList();
	abstract public void filter(CharSequence strSearch);
}
