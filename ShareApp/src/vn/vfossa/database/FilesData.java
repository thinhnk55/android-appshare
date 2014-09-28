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

public class FilesData {
	private int _id;
	private String _type;
	private String _name;
	private String _path;
	private byte[] _image;
	private float _size;

	public FilesData(String _type, String _name, String _path, byte[] _image,
			float _size) {
		this._type = _type;
		this._name = _name;
		this._path = _path;
		this._image = _image;
		this._size = _size;
	}

	public FilesData(int _id, String _type, String _name, String _path,
			byte[] _image, float _size) {
		this._id = _id;
		this._type = _type;
		this._name = _name;
		this._path = _path;
		this._image = _image;
		this._size = _size;
	}

	public FilesData() {

	}

	public int getID() {
		return this._id;
	}

	public void setID(int id) {
		this._id = id;
	}

	public String getType() {
		return _type;
	}

	public void setType(String _type) {
		this._type = _type;
	}

	public String getName() {
		return this._name;
	}

	public void setName(String name) {
		this._name = name;
	}

	public String getPath() {
		return this._path;
	}

	public void setPath(String path) {
		this._path = path;
	}

	public byte[] getImage() {
		return _image;
	}

	public void setImage(byte[] _image) {
		this._image = _image;
	}

	public float getSize() {
		return _size;
	}

	public void setSize(float _size) {
		this._size = _size;
	}

}
