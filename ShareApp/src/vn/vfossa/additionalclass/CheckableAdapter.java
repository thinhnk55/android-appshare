/****************************************************************************** * 
* Copyright (C) 2013   Nguyen Khanh Thinh, Nguyen Van Dai and Contributors
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details.  
* You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
* Any further request, feel freely to mhst1024-10@googlegroups.com 
*************************************************************************************************/

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
