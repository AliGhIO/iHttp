package io.aligh.ihttp.classes;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

public class CacheSQLite extends SQLiteOpenHelper {

    public interface DataReceived {
        void data(String string);
    }

    private static final String DB_NAME = "iHttp";
    private static final int DB_VERSION = 1;
//    private static HashMap<String, String> fast = new HashMap<>();
    private final String TAG = this.getClass().getName();
    private Handler handler;


    private String key;

    CacheSQLite(Context context, String key) {
        super(context, DB_NAME, null, DB_VERSION);
        this.key = key;
        handler = new Handler(context.getMainLooper());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String SQL_COMMAND_CREATE_POSTS_TABLE = "CREATE TABLE IF NOT EXISTS iHttp" +
                    "(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "request_key TEXT, " +
                    "request_value TEXT, " +
                    "request_time DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    "response TEXT" +
                    ");";

            db.execSQL(SQL_COMMAND_CREATE_POSTS_TABLE);
        } catch (SQLException e) {
            Log.e(TAG, "Error On Creating io.aligh.iHttp.CacheSQLite.Table: " + e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private void runOnUiThread(Runnable r) {
        handler.post(r);
    }

    @SuppressLint("StaticFieldLeak")
    public void get(final DataReceived dataReceived) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                final SQLiteDatabase sqLiteDatabase = CacheSQLite.this.getReadableDatabase();

                CheckExist checkExist = new CheckExist(sqLiteDatabase, "request_key=" + key);

                checkExist.isExist(new CheckExist.IsExist() {
                    @Override
                    public void isExist(Boolean exist) {
                        if (exist) {
                            Cursor cursor = null;
                            try {
                                cursor = sqLiteDatabase.rawQuery("SELECT * FROM iHttp WHERE request_key ='" + key + "'", null);
                                if (cursor != null) {
                                    cursor.moveToFirst();
                                    final String text = cursor.getString(2);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                dataReceived.data((text));
                                            } catch (Exception e) {
                                                Log.e(TAG, "Error on io.aligh.iHttp.CacheSQLite.Get.receive1: " + e.getMessage());
                                            }
                                        }
                                    });
                                    cursor.close();
                                    sqLiteDatabase.close();
                                }
                            } catch (Exception e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            dataReceived.data("");
                                        } catch (Exception e) {
                                            Log.e(TAG, "Error on io.aligh.iHttp.CacheSQLite.Get.receive2: " + e.getMessage());
                                        }
                                    }
                                });
                                sqLiteDatabase.close();
                                if (cursor != null)
                                    cursor.close();
                            }
                        } else
                            sqLiteDatabase.close();
                    }
                });

                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }

    @SuppressLint("StaticFieldLeak")
    void put(final String value) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                final SQLiteDatabase sqLiteDatabase = CacheSQLite.this.getReadableDatabase();

                final ContentValues contentValues;
                contentValues = new ContentValues();
                contentValues.put("request_key", key);
                contentValues.put("request_value", value);

                CheckExist checkExist = new CheckExist(sqLiteDatabase, "request_key=" + key);

                checkExist.isExist(new CheckExist.IsExist() {
                    @Override
                    public void isExist(Boolean exist) {
                        try {
                            if (!exist)
                                sqLiteDatabase.insert("iHttp", null, contentValues);
                            else {
                                String where = "request_key=?";
                                String[] whereArgs = new String[]{String.valueOf(key)};
                                sqLiteDatabase.update("iHttp", contentValues, where, whereArgs);
                            }
                            sqLiteDatabase.close();
                        } catch (Exception e) {
                            sqLiteDatabase.close();
                        }
                    }
                });
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }

    public static class CheckExist {

        interface IsExist {
            void isExist(Boolean exist);
        }

        private SQLiteDatabase sqLiteDatabase;
        String clause;

        CheckExist(SQLiteDatabase sqLiteDatabase, String clause) {
            this.sqLiteDatabase = sqLiteDatabase;
            this.clause = clause;
        }

        @SuppressLint("StaticFieldLeak")
        void isExist(final IsExist isExist) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    Cursor cursor = null;
                    try {
                        if (clause != null) clause += " WHERE " + clause;
                        String selectString = "SELECT * FROM iHttp" + clause;
                        cursor = sqLiteDatabase.rawQuery(selectString, null);
                        if (cursor.getCount() > 0)
                            isExist.isExist(true);
                        else isExist.isExist(false);
                    } catch (Exception e) {
                        isExist.isExist(false);
                    } finally {
                        if (cursor != null)
                            cursor.close();
                    }
                    return null;
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }
}
