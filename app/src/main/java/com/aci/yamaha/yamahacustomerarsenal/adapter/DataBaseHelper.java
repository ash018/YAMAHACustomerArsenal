package com.aci.yamaha.yamahacustomerarsenal.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper extends SQLiteOpenHelper {
	private static String TAG = DataBaseHelper.class.getName();
	private static String DB_PATH = "";
	private static String DB_NAME = "yca.db"; // Database name
	private final Context mContext;

	private static DataBaseHelper sInstance;

	public static synchronized DataBaseHelper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new DataBaseHelper(context.getApplicationContext());
		}
		return sInstance;
	}

	public DataBaseHelper(Context context) {
		super(context, DB_NAME, null, 1);// 1? its Database Version
		//DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
		DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
		//DB_PATH = "mnt/sdcard/" + context.getPackageName() + "/databases/";
		this.mContext = context;
	}

	public void createDataBase() throws IOException {
		boolean mDataBaseExist = checkDataBase();
		if (!mDataBaseExist) {
			this.getWritableDatabase();
			this.close();
			try {
				copyDataBase();
				Log.e(TAG, "createDatabase database created");
			} catch (IOException mIOException) {
				throw new Error(mIOException.toString()+" : "+DB_PATH + DB_NAME);//"ErrorCopyingDataBase"
			}
		}
	}

	private boolean checkDataBase() {
		File dbFile = new File(DB_PATH + DB_NAME);
		return dbFile.exists();
	}

	private void copyDataBase() throws IOException {
		InputStream mInput = mContext.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream mOutput = new FileOutputStream(outFileName);
		byte[] mBuffer = new byte[1024];
		int mLength;
		while ((mLength = mInput.read(mBuffer)) > 0) {
			mOutput.write(mBuffer, 0, mLength);
		}
		mOutput.flush();
		mOutput.close();
		mInput.close();
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
