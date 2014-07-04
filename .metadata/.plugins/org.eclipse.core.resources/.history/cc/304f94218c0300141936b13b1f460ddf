package com.nvdai.database;

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
	private static final String TABLE_SONGSDATA = "SongsData";

	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_PATH = "path";
	private static final String KEY_ARTIST = "artist";
	private static final String KEY_ALBUM = "album";
	private static final String KEY_IMAGE = "image";
	private static final String KEY_FAVORITE = "favorite";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_SONGSDATA_TABLE = "CREATE TABLE " + TABLE_SONGSDATA + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_PATH + " TEXT," + KEY_ARTIST + " TEXT," + KEY_ALBUM
				+ " TEXT," + KEY_IMAGE + " BLOB, " + KEY_FAVORITE + " INTEGER "
				+ ")";
		db.execSQL(CREATE_SONGSDATA_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONGSDATA);
		onCreate(db);
	}

	public void addSongData(SongData songData) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, songData.getName());
		values.put(KEY_PATH, songData.getPath());
		values.put(KEY_ARTIST, songData.getArtist());
		values.put(KEY_ALBUM, songData.getAlbum());
		values.put(KEY_IMAGE, songData.getImage());
		values.put(KEY_FAVORITE, 0);

		db.insert(TABLE_SONGSDATA, null, values);
		db.close();
	}

	public SongData getSongData(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_SONGSDATA, new String[] { KEY_ID,
				KEY_NAME, KEY_PATH, KEY_ARTIST, KEY_ALBUM, KEY_IMAGE,
				KEY_FAVORITE }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		SongData songData = new SongData(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), cursor.getString(3),
				cursor.getString(4), cursor.getBlob(5), cursor.getInt(6));
		return songData;
	}

	public List<SongData> getAllSongDatas() {
		List<SongData> SongDataList = new ArrayList<SongData>();
		String selectQuery = "SELECT  * FROM " + TABLE_SONGSDATA;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				SongData songData = new SongData();
				songData.setID(Integer.parseInt(cursor.getString(0)));
				songData.setName(cursor.getString(1));
				songData.setPath(cursor.getString(2));
				songData.setArtist(cursor.getString(3));
				songData.setAlbum(cursor.getString(4));
				songData.setImage(cursor.getBlob(5));
				songData.setFavorite(cursor.getInt(6));
				SongDataList.add(songData);
			} while (cursor.moveToNext());
		}
		db.close();
		return SongDataList;
	}

	public int updateSongData(SongData songData) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, songData.getName());
		values.put(KEY_PATH, songData.getPath());
		values.put(KEY_ARTIST, songData.getArtist());
		values.put(KEY_ALBUM, songData.getAlbum());
		values.put(KEY_IMAGE, songData.getImage());
		values.put(KEY_FAVORITE, songData.getFavorite());

		return db.update(TABLE_SONGSDATA, values, KEY_ID + " = ?",
				new String[] { String.valueOf(songData.getID()) });
	}

	public void deleteSongData(SongData songData) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_SONGSDATA, KEY_ID + " = ?",
				new String[] { String.valueOf(songData.getID()) });
		db.close();
	}

	public int getSongDatasCount() {
		String countQuery = "SELECT * FROM " + TABLE_SONGSDATA;
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
		db.delete(TABLE_SONGSDATA, null, null);
		db.close();
	}

	public List<SongData> Order(String type) {
		List<SongData> SongDataList = new ArrayList<SongData>();
		String selectQuery;
		;
		if (type.equals("only favorite")) {
			selectQuery = "SELECT  * FROM " + TABLE_SONGSDATA + " WHERE "
					+ KEY_FAVORITE + " = 1" + " ORDER BY name ASC";

		} else {
			if (type.equals(KEY_FAVORITE)) {
				selectQuery = "SELECT  * FROM " + TABLE_SONGSDATA
						+ " ORDER BY " + type + " DESC";
			} else {
				selectQuery = "SELECT  * FROM " + TABLE_SONGSDATA
						+ " ORDER BY " + type + " ASC";
			}
		}
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				SongData songData = new SongData();
				songData.setID(Integer.parseInt(cursor.getString(0)));
				songData.setName(cursor.getString(1));
				songData.setPath(cursor.getString(2));
				songData.setArtist(cursor.getString(3));
				songData.setAlbum(cursor.getString(4));
				songData.setImage(cursor.getBlob(5));
				songData.setFavorite(cursor.getInt(6));
				SongDataList.add(songData);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return SongDataList;
	}

	public void editFavorite(int ID, int favorite, String type) {
		int oldValue = 1;
		if (favorite == 1) {
			oldValue = 0;
		}
		String selectQuery = "UPDATE " + TABLE_SONGSDATA + " SET "
				+ KEY_FAVORITE + " = REPLACE (" + KEY_FAVORITE + "," + oldValue
				+ "," + favorite + ") WHERE " + KEY_ID + " = " + ID;

		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(selectQuery);
		db.close();

	}

	public boolean checkSongPath(String songPath) {
		String checkQuery = "SELECT  * FROM " + TABLE_SONGSDATA + " WHERE "
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
