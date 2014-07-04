package vn.vfossa.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "SongDatasManager";
	private static final String TABLE_MUSICSDATA = "MusicsData";

	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_PATH = "path";
	private static final String KEY_IMAGE = "image";
	private static final String KEY_SIZE = "size";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_MusicsData_TABLE = "CREATE TABLE " + TABLE_MUSICSDATA + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_PATH + " TEXT," + KEY_IMAGE + " BLOB, "+ KEY_SIZE + " FLOAT )";
		db.execSQL(CREATE_MusicsData_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MUSICSDATA);
		onCreate(db);
	}

	public void addSongData(FilesData songData) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, songData.getName());
		values.put(KEY_PATH, songData.getPath());
		values.put(KEY_IMAGE, songData.getImage());

		db.insert(TABLE_MUSICSDATA, null, values);
		db.close();
	}

	public FilesData getSongData(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_MUSICSDATA, new String[] { KEY_ID,
				KEY_NAME, KEY_PATH, KEY_IMAGE, KEY_SIZE}, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		FilesData songData = new FilesData(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2),cursor.getBlob(4), cursor.getFloat(5));
		return songData;
	}

	public List<FilesData> getAllSongDatas() {
		List<FilesData> SongDataList = new ArrayList<FilesData>();
		String selectQuery = "SELECT  * FROM " + TABLE_MUSICSDATA;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				FilesData songData = new FilesData();
				songData.setID(Integer.parseInt(cursor.getString(0)));
				songData.setName(cursor.getString(1));
				songData.setPath(cursor.getString(2));
				songData.setImage(cursor.getBlob(5));
				SongDataList.add(songData);
			} while (cursor.moveToNext());
		}
		db.close();
		return SongDataList;
	}

	public int updateSongData(FilesData songData) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, songData.getName());
		values.put(KEY_PATH, songData.getPath());
		values.put(KEY_IMAGE, songData.getImage());

		return db.update(TABLE_MUSICSDATA, values, KEY_ID + " = ?",
				new String[] { String.valueOf(songData.getID()) });
	}

	public void deleteSongData(FilesData songData) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_MUSICSDATA, KEY_ID + " = ?",
				new String[] { String.valueOf(songData.getID()) });
		db.close();
	}

	public int getSongDatasCount() {
		String countQuery = "SELECT * FROM " + TABLE_MUSICSDATA;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.moveToFirst();
		int result = cursor.getCount();
		cursor.close();
		db.close();

		return result;
	}

	public void deleteAll() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_MUSICSDATA, null, null);
		db.close();
	}


	public boolean checkSongPath(String songPath) {
		String checkQuery = "SELECT  * FROM " + TABLE_MUSICSDATA + " WHERE "
				+ KEY_PATH + " = \"" + songPath + "\"";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(checkQuery, null);
		cursor.moveToFirst();
		db.close();
		if (cursor.getCount() == 0) {
			cursor.close();
			return true;
		} else {
			cursor.close();
			return false;
		}
	}

}
