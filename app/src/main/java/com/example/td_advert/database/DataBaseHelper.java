package com.example.td_advert.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {
	private static String TAG = "DataBaseHelper"; // Tag just for the LogCat
													// window
	// destination path (location) of our database on device
	private static String DB_PATH = "";
	private static String DB_NAME = "TDAdvertDataBase.sqlite";// Database name
	private SQLiteDatabase mDataBase;
	private final Context mContext;

	private static final int dbVersion = 6;

	public final static String TABLE_CONFIGURATION = "Configuration";
	public final static String COLUMN_CONFIGURATION_CONFIGURATION_ID = "configuration_id";
	public final static String COLUMN_CONFIGURATION_PROPERTY_NAME = "property_name";
	public final static String COLUMN_CONFIGURATION_PROPERTY_VALUE = "property_value";

	public final static String TABLE_EVENT = "Event";
	public final static String COLUMN_EVENT_EVENT_ID = "event_id";
	public final static String COLUMN_EVENT_EVENT_ACTION = "event_action";
	public final static String COLUMN_EVENT_EVENT_VALUE = "event_value";
	public final static String COLUMN_EVENT_EVENT_DATE = "event_date";
	public final static String COLUMN_EVENT_EVENT_STATE = "event_state";
	public final static String COLUMN_EVENT_EXTRAS = "extras";

	public DataBaseHelper(Context context) {
		super(context, DB_NAME, null, dbVersion);// 1? its Database Version
		if (android.os.Build.VERSION.SDK_INT >= 17) {
			DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
		} else {
			DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
		}
		this.mContext = context;
	}

	public void createDataBase() throws IOException {
		// If database not exists copy it from the assets

		boolean mDataBaseExist = checkDataBase();
		if (!mDataBaseExist) {
			this.getReadableDatabase();
			this.close();
			try {
				// Copy the database from assests
				copyDataBase();
				openDataBase();
				createConfigurationTable(mDataBase);
				createEventTable(mDataBase);
				close();
				Log.e(TAG, "createDatabase database created");
			} catch (IOException mIOException) {
				throw new Error("ErrorCopyingDataBase");
			}
		}
	}

	// Check that the database exists here: /data/data/your package/databases/Da
	// Name
	private boolean checkDataBase() {
		File dbFile = new File(DB_PATH + DB_NAME);
		// Log.v("dbFile", dbFile + "   "+ dbFile.exists());
		return dbFile.exists();
	}

	// Copy the database from assets
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

	// Open the database, so we can query it
	public boolean openDataBase() throws SQLException {
		String mPath = DB_PATH + DB_NAME;
		// Log.v("mPath", mPath);
		mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
		// mDataBase = SQLiteDatabase.openDatabase(mPath, null,
		// SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		return mDataBase != null;
	}

	@Override
	public synchronized void close() {
		if (mDataBase != null)
			mDataBase.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		switch (oldVersion) {
		case 2:
			this.createConfigurationTable(db);
			this.createEventTable(db);
		case 3:
        case 4:
//			alterEventExtrasColumn(db);
//			alterEventStateColumn(db);
            alterCompanyExtraColumn(db);
            alterTabletInformationColumn(db);
        case 5:
            createStationLocationTable(db);
             break;
		default:
			break;
		}

	}

	private void createEventTable(SQLiteDatabase db) {
		try {
			String sql = "CREATE TABLE IF NOT EXISTS   " + TABLE_EVENT + " ("
					+ COLUMN_EVENT_EVENT_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ COLUMN_EVENT_EVENT_ACTION + " TEXT, "
					+ COLUMN_EVENT_EVENT_DATE + " TEXT, "
					+ COLUMN_EVENT_EVENT_STATE + " TEXT, "
					+ COLUMN_EVENT_EXTRAS + " TEXT, "
					+ COLUMN_EVENT_EVENT_VALUE + " INTEGER)";
			db.execSQL(sql);

			Log.d(TAG, "Done");
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			throw new RuntimeException(e);
		}
	}

    private void createStationLocationTable(SQLiteDatabase db) {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS TaxiStation ( id INTEGER, stationName TEXT)";
            db.execSQL(sql);

            sql = "ALTER TABLE TabletInformation ADD COLUMN taxiStationID INTEGER";
            db.execSQL(sql);

            sql = "ALTER TABLE TabletInformation ADD COLUMN hasUpdate INTEGER";
            db.execSQL(sql);

            Log.d(TAG, "Done");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            throw new RuntimeException(e);
        }
    }

	private void createConfigurationTable(SQLiteDatabase db) {
		try {
			String sql = "CREATE TABLE IF NOT EXISTS  " + TABLE_CONFIGURATION
					+ " (" + COLUMN_CONFIGURATION_CONFIGURATION_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ COLUMN_CONFIGURATION_PROPERTY_NAME + " TEXT,"
					+ COLUMN_CONFIGURATION_PROPERTY_VALUE + " TEXT)";
			db.execSQL(sql);

			Log.d(TAG, "Done");
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			throw new RuntimeException(e);
		}
	}

	private void alterEventExtrasColumn(SQLiteDatabase db){
		try {

			String sql = "ALTER TABLE " + TABLE_EVENT + " ADD COLUMN "
					+  COLUMN_EVENT_EXTRAS
					+ " TEXT ";
			db.execSQL(sql);
			Log.d(TAG, "Done");
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			throw new RuntimeException(e);
		}
	}

	private void alterEventStateColumn(SQLiteDatabase db){
		try {
			String sql = "ALTER TABLE " + TABLE_EVENT + " ADD COLUMN "
					+  COLUMN_EVENT_EVENT_STATE
					+ " TEXT ";
			db.execSQL(sql);
			Log.d(TAG, "Done");
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			throw new RuntimeException(e);
		}
	}

    private void alterCompanyExtraColumn(SQLiteDatabase db){
        try {
            String sql = "ALTER TABLE TabConfig ADD COLUMN placeholder INTEGER";
            db.execSQL(sql);
            Log.d(TAG, "Done");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            //throw new RuntimeException(e);
        }
    }

    private void alterTabletInformationColumn(SQLiteDatabase db){
        try {
            String sql = "ALTER TABLE TabletInformation ADD COLUMN testDevice INTEGER";
            db.execSQL(sql);
            Log.d(TAG, "Done");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            //throw new RuntimeException(e);
        }
    }
}
