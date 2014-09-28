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

package vn.vfossa.database;

import android.graphics.Bitmap;

public class AppData {
	private int _id;
	private String _name;
	private String _path;
	private Bitmap _image;
	private float _size;
	
	
	public AppData(int _id, String _name, String _path, Bitmap _image,
			float _size) {
		super();
		this._id = _id;
		this._name = _name;
		this._path = _path;
		this._image = _image;
		this._size = _size;
	}


	public AppData(String _name, String _path, Bitmap _image, float _size) {
		super();
		this._name = _name;
		this._path = _path;
		this._image = _image;
		this._size = _size;
	}


	public AppData() {
		
	}


	public int get_id() {
		return _id;
	}


	public void set_id(int _id) {
		this._id = _id;
	}


	public String get_name() {
		return _name;
	}


	public void set_name(String _name) {
		this._name = _name;
	}


	public String get_path() {
		return _path;
	}


	public void set_path(String _path) {
		this._path = _path;
	}


	public Bitmap get_image() {
		return _image;
	}


	public void set_image(Bitmap _image) {
		this._image = _image;
	}


	public float get_size() {
		return _size;
	}


	public void set_size(float _size) {
		this._size = _size;
	}

	
	
}
