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

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// tạo biến static cho cơ sở dữ liệu
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "FileDatasManager";
	private static final String TABLE_FILESDATA = "FilesData";

	// tạo các key cho các trường của bảng cơ sở dữ liệu
	private static final String KEY_ID = "id"; // id của từng dữ liệu
	private static final String KEY_TYPE = "type"; // loại của dữ liệu (app,
													// music, image..)
	private static final String KEY_NAME = "name"; // tên của tệp
	private static final String KEY_PATH = "path"; // đường dẫn đến tệp
	private static final String KEY_IMAGE = "image"; // ảnh đặc trưng của từng
														// tệp
	private static final String KEY_SIZE = "size"; // kích cỡ của tệp

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/*
	 * hàm tạo cơ sở dữ liệu cho ứng dụng
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {

		// tạo chuỗi lệnh SQLite
		String CREATE_FILESDATA_TABLE = "CREATE TABLE " + TABLE_FILESDATA + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_TYPE + " CHAR,"
				+ KEY_NAME + " TEXT," + KEY_PATH + " TEXT," + KEY_IMAGE
				+ " BLOB, " + KEY_SIZE + " FLOAT )";

		// thực thi lệnh SQLite
		db.execSQL(CREATE_FILESDATA_TABLE);
	}

	/*
	 * Hàm tạo lại bảng khi bảng đã tồn tại trong cơ sở dữ liệu đó
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILESDATA);
		onCreate(db);
	}

	/*
	 * Hàm thêm một hàng mới vào bảng dữ liệu
	 */
	public void addFileData(FilesData fileData) {
		// lấy quyền ghi vào cơ sở dữ liệu
		SQLiteDatabase db = this.getWritableDatabase();

		// Tạo biến để lưu dữ liệu và đưa dữ liệu vào bảng
		ContentValues values = new ContentValues();
		values.put(KEY_TYPE, fileData.getType());
		values.put(KEY_NAME, fileData.getName());
		values.put(KEY_PATH, fileData.getPath());
		values.put(KEY_IMAGE, fileData.getImage());
		values.put(KEY_SIZE, fileData.getSize());

		// Thêm biến mới được tạo ra vào bảng
		db.insert(TABLE_FILESDATA, null, values);

		// Đóng cơ sở dữ liệu
		db.close();
	}

	/*
	 * Hàm lấy một hàng từ bảng dữ liệu bằng ID của nó
	 */
	public FilesData getFileData(int id) {

		// Lấy quyền đọc từ cơ sở dữ liệu
		SQLiteDatabase db = this.getReadableDatabase();

		// Tạo ra một con trỏ chỉ đến tất cả các kết quả được lọc ra từ cơ sỡ dữ
		// liệu với ID là ID cho trước
		Cursor cursor = db.query(TABLE_FILESDATA, new String[] { KEY_ID,
				KEY_TYPE, KEY_NAME, KEY_PATH, KEY_IMAGE, KEY_SIZE }, KEY_ID
				+ "=?", new String[] { String.valueOf(id) }, null, null, null,
				null);

		// Kiểm tra kết quả trả về có giá trị hay không
		if (cursor != null)
			cursor.moveToFirst();

		// Tạo biến chứa hàng trả về
		FilesData fileData = new FilesData(
				Integer.parseInt(cursor.getString(0)), cursor.getString(1),
				cursor.getString(2), cursor.getString(3), cursor.getBlob(4),
				cursor.getFloat(5));
		return fileData;
	}

	/*
	 * Hàm để lấy tất cả các dữ liệu hiện có trong cơ sở dữ liệu
	 */
	public List<FilesData> getAllFileDatas() {

		// Tạo list để chứa dữ liệu
		List<FilesData> fileDataList = new ArrayList<FilesData>();

		// Tạo lệnh SQLite
		String selectQuery = "SELECT  * FROM " + TABLE_FILESDATA;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// Xét dữ liệu thu được và đưa từng hàng vào list
		if (cursor.moveToFirst()) {
			do {
				FilesData fileData = new FilesData();
				fileData.setID(Integer.parseInt(cursor.getString(0)));
				fileData.setType(cursor.getString(1));
				fileData.setName(cursor.getString(2));
				fileData.setPath(cursor.getString(3));
				fileData.setImage(cursor.getBlob(4));
				fileData.setSize(cursor.getFloat(5));
				
				fileDataList.add(fileData);
			} while (cursor.moveToNext());
		}

		// đóng cơ sở dữ liệu
		db.close();
		return fileDataList;
	}
	
	/*
	 * Hàm để lấy tất cả các dữ liệu hiện có trong cơ sở dữ liệu
	 */
	public ArrayList<FilesData> getAllFileWithType(String type) {

		// Tạo list để chứa dữ liệu
		ArrayList<FilesData> fileDataList = new ArrayList<FilesData>();

		// Tạo lệnh SQLite
		String selectQuery = "SELECT  * FROM " + TABLE_FILESDATA +" WHERE " + KEY_TYPE +" = '" + type+"'";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// Xét dữ liệu thu được và đưa từng hàng vào list
		if (cursor.moveToFirst()) {
			do {
				FilesData fileData = new FilesData();
				fileData.setID(Integer.parseInt(cursor.getString(0)));
				fileData.setType(cursor.getString(1));
				fileData.setName(cursor.getString(2));
				fileData.setPath(cursor.getString(3));
				fileData.setImage(cursor.getBlob(4));
				fileData.setSize(cursor.getFloat(5));
				
				fileDataList.add(fileData);
			} while (cursor.moveToNext());
		}

		// đóng cơ sở dữ liệu
		db.close();
		return fileDataList;
	}

	/*
	 * Hàm để sửa đổi dữ liệu của một hàng
	 */
	public int updateFileData(FilesData fileData) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TYPE, fileData.getType());
		values.put(KEY_NAME, fileData.getName());
		values.put(KEY_PATH, fileData.getPath());
		values.put(KEY_IMAGE, fileData.getImage());
		values.put(KEY_SIZE, fileData.getSize());

		return db.update(TABLE_FILESDATA, values, KEY_ID + " = ?",
				new String[] { String.valueOf(fileData.getID()) });
	}

	/*
	 * Hàm để xóa một hàng
	 */
	public void deleteFileData(FilesData fileData) {
		SQLiteDatabase db = this.getWritableDatabase();
		//Xóa hàng bằng ID của dữ liệu
		db.delete(TABLE_FILESDATA, KEY_ID + " = ?",
				new String[] { String.valueOf(fileData.getID()) });
		db.close();
	}

	/*
	 * Hàm đếm số hàng trong bảng dữ liệu
	 */
	public int getFileDatasCount() {
		String countQuery = "SELECT * FROM " + TABLE_FILESDATA;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.moveToFirst();

		// tạo biến result và tính toán bằng hàm getCount()
		int result = cursor.getCount();
		cursor.close();
		db.close();

		return result;
	}

	/*
	 * Hàm xóa toàn bộ bảng dữ liệu
	 */
	public void deleteAll() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_FILESDATA, null, null);
		db.close();
	}

	/*
	 * Hàm kiểm tra một file đã tồn tại trong cơ sở dữ liệu hay chưa. Sử dụng
	 * đường dẫn của file để kiểm tra
	 */
	public boolean checkPath(String path) {
		String checkQuery = "SELECT  * FROM " + TABLE_FILESDATA + " WHERE "
				+ KEY_PATH + " = \"" + path + "\"";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(checkQuery, null);
		cursor.moveToFirst();
		db.close();
		
		//Kiểm tra xem nó có tồn tại hay không
		if (cursor.getCount() == 0) {
			cursor.close();
			return true;
		} else {
			cursor.close();
			return false;
		}
	}

}
