package com.arindo.ketagiahn.constanta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "database.db";

    public static final String TABLE_SQLite = "sqliteperatul";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_BULAN = "bulan";
    public static final String COLUMN_UNITUP = "unitup";
    public static final String COLUMN_IDPEL = "idpel";
    public static final String COLUMN_NAMA = "nama";
    public static final String COLUMN_ALAMAT = "alamat";
    public static final String COLUMN_TARIF = "tarif";
    public static final String COLUMN_DAYA = "daya";
    public static final String COLUMN_KOKED = "koked";
    public static final String COLUMN_LANGKAH = "langkah";
    public static final String COLUMN_LEMBAR = "lembar";
    public static final String COLUMN_TAGIHAN = "tagihan";
    public static final String COLUMN_SPINERKET = "spinerket";
    public static final String COLUMN_LATITUD = "latitud";
    public static final String COLUMN_LANGITUD = "langitud";
    public static final String COLUMN_KETERANGAN1 = "keterangan1";
    public static final String COLUMN_KETERANGAN2 = "keterangan2";
    public static final String COLUMN_KETERANGAN3 = "keterangan3";
    public static final String COLUMN_RENCANABAYAR = "rencanabayar";
    public static final String COLUMN_NOTELEPON = "notelepon";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_TANGGALSURVEY = "tanggalsurvey";
    //buat save gambar di database

    private SQLiteDatabase mDb;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + TABLE_SQLite + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY autoincrement, " +
                COLUMN_BULAN + " TEXT NOT NULL, " +
                COLUMN_UNITUP + " TEXT NOT NULL, " +
                COLUMN_IDPEL + " TEXT NOT NULL, " +
                COLUMN_NAMA + " TEXT NOT NULL, " +
                COLUMN_ALAMAT + " TEXT NOT NULL, " +
                COLUMN_TARIF + " TEXT NOT NULL, " +
                COLUMN_DAYA + " TEXT NOT NULL, " +
                COLUMN_KOKED + " TEXT NOT NULL, " +
                COLUMN_LANGKAH + " INTEGER NOT NULL, " +
                COLUMN_LEMBAR + " TEXT NOT NULL, " +
                COLUMN_TAGIHAN + " TEXT NOT NULL, " +
                COLUMN_SPINERKET + " TEXT NOT NULL, " +
                COLUMN_LATITUD + " TEXT NOT NULL, " +
                COLUMN_LANGITUD + " TEXT NOT NULL, " +
                COLUMN_KETERANGAN1 + " TEXT NOT NULL, " +
                COLUMN_KETERANGAN2 + " TEXT NOT NULL, " +
                COLUMN_KETERANGAN3 + " TEXT NOT NULL, " +
                COLUMN_RENCANABAYAR + " TEXT NOT NULL, " +
                COLUMN_NOTELEPON + " TEXT NOT NULL, " +
                COLUMN_STATUS + " TEXT NOT NULL, " +
                COLUMN_TANGGALSURVEY + " TEXT NOT NULL )";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SQLite);
        onCreate(db);
    }

    public int getNotesCount00() {
        String countQuery = "SELECT  * FROM " + TABLE_SQLite + " WHERE " + COLUMN_STATUS + "=" + "'" + "00" + "'";
        mDb = this.getReadableDatabase();
        Cursor cursor = mDb.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int getNotesCount01(String koked) {
        String countQuery = "SELECT  * FROM " + TABLE_SQLite + " WHERE " + COLUMN_STATUS + "=" + "'" + "01" + "'" + " AND " + COLUMN_KOKED + "=" + "'" + koked + "'";
        mDb = this.getReadableDatabase();
        Cursor cursor = mDb.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int getNotesCount02(String koked) {
        String countQuery = "SELECT  * FROM " + TABLE_SQLite + " WHERE " + COLUMN_STATUS + "=" + "'" + "02" + "'" + " AND " + COLUMN_KOKED + "=" + "'" + koked + "'";
        mDb = this.getReadableDatabase();
        Cursor cursor = mDb.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //tampil data pertanggal 21
    public ArrayList<HashMap<String, String>> getdatatanggal21(String koked) {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM " + TABLE_SQLite + " WHERE " + COLUMN_TANGGALSURVEY +  " LIKE "  + "'%21%'"  + " AND " + COLUMN_KOKED + "=" + "'" + koked + "'" + " ORDER BY " + COLUMN_LANGKAH + " ASC " ;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(COLUMN_ID, cursor.getString(0));
                map.put(COLUMN_BULAN, cursor.getString(1));
                map.put(COLUMN_UNITUP, cursor.getString(2));
                map.put(COLUMN_IDPEL, cursor.getString(3));
                map.put(COLUMN_NAMA, cursor.getString(4));
                map.put(COLUMN_ALAMAT, cursor.getString(5));
                map.put(COLUMN_TARIF, cursor.getString(6));
                map.put(COLUMN_DAYA, cursor.getString(7));
                map.put(COLUMN_KOKED, cursor.getString(8));
                map.put(COLUMN_LANGKAH, cursor.getString(9));
                map.put(COLUMN_LEMBAR, cursor.getString(10));
                map.put(COLUMN_TAGIHAN, cursor.getString(11));
                map.put(COLUMN_SPINERKET, cursor.getString(12));
                map.put(COLUMN_LATITUD, cursor.getString(13));
                map.put(COLUMN_LANGITUD, cursor.getString(14));
                map.put(COLUMN_KETERANGAN1, cursor.getString(15));
                map.put(COLUMN_KETERANGAN2, cursor.getString(16));
                map.put(COLUMN_KETERANGAN3, cursor.getString(17));
                map.put(COLUMN_RENCANABAYAR, cursor.getString(18));
                map.put(COLUMN_NOTELEPON, cursor.getString(19));
                map.put(COLUMN_STATUS, cursor.getString(20));
                map.put(COLUMN_TANGGALSURVEY, cursor.getString(21));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        Log.e("hasil sqlite ", "" + wordList);

        database.close();
        return wordList;
    }

    //tampil data yang statusnya 00
    public ArrayList<HashMap<String, String>> getAllData(String koked) {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM " + TABLE_SQLite + " WHERE " + COLUMN_STATUS + "=" + "'" + "00" + "'" + " AND " + COLUMN_KOKED + "=" + "'" + koked + "'" + " ORDER BY " + COLUMN_LANGKAH + " ASC " ;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(COLUMN_ID, cursor.getString(0));
                map.put(COLUMN_BULAN, cursor.getString(1));
                map.put(COLUMN_UNITUP, cursor.getString(2));
                map.put(COLUMN_IDPEL, cursor.getString(3));
                map.put(COLUMN_NAMA, cursor.getString(4));
                map.put(COLUMN_ALAMAT, cursor.getString(5));
                map.put(COLUMN_TARIF, cursor.getString(6));
                map.put(COLUMN_DAYA, cursor.getString(7));
                map.put(COLUMN_KOKED, cursor.getString(8));
                map.put(COLUMN_LANGKAH, cursor.getString(9));
                map.put(COLUMN_LEMBAR, cursor.getString(10));
                map.put(COLUMN_TAGIHAN, cursor.getString(11));
                map.put(COLUMN_SPINERKET, cursor.getString(12));
                map.put(COLUMN_LATITUD, cursor.getString(13));
                map.put(COLUMN_LANGITUD, cursor.getString(14));
                map.put(COLUMN_KETERANGAN1, cursor.getString(15));
                map.put(COLUMN_KETERANGAN2, cursor.getString(16));
                map.put(COLUMN_KETERANGAN3, cursor.getString(17));
                map.put(COLUMN_RENCANABAYAR, cursor.getString(18));
                map.put(COLUMN_NOTELEPON, cursor.getString(19));
                map.put(COLUMN_STATUS, cursor.getString(20));
                map.put(COLUMN_TANGGALSURVEY, cursor.getString(21));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        Log.e("hasil sqlite ", "" + wordList);

        database.close();
        return wordList;
    }

    //tampil data yang statusnya 01
    public ArrayList<HashMap<String, String>> getStatus01(String koked01) {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM " + TABLE_SQLite + " WHERE " + COLUMN_STATUS + "=" + "'" + "01" + "'" + " AND " + COLUMN_KOKED + "=" + "'" + koked01 + "'" + " ORDER BY " + COLUMN_LANGKAH + " ASC ";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(COLUMN_ID, cursor.getString(0));
                map.put(COLUMN_BULAN, cursor.getString(1));
                map.put(COLUMN_UNITUP, cursor.getString(2));
                map.put(COLUMN_IDPEL, cursor.getString(3));
                map.put(COLUMN_NAMA, cursor.getString(4));
                map.put(COLUMN_ALAMAT, cursor.getString(5));
                map.put(COLUMN_TARIF, cursor.getString(6));
                map.put(COLUMN_DAYA, cursor.getString(7));
                map.put(COLUMN_KOKED, cursor.getString(8));
                map.put(COLUMN_LANGKAH, cursor.getString(9));
                map.put(COLUMN_LEMBAR, cursor.getString(10));
                map.put(COLUMN_TAGIHAN, cursor.getString(11));
                map.put(COLUMN_SPINERKET, cursor.getString(12));
                map.put(COLUMN_LATITUD, cursor.getString(13));
                map.put(COLUMN_LANGITUD, cursor.getString(14));
                map.put(COLUMN_KETERANGAN1, cursor.getString(15));
                map.put(COLUMN_KETERANGAN2, cursor.getString(16));
                map.put(COLUMN_KETERANGAN3, cursor.getString(17));
                map.put(COLUMN_RENCANABAYAR, cursor.getString(18));
                map.put(COLUMN_NOTELEPON, cursor.getString(19));
                map.put(COLUMN_STATUS, cursor.getString(20));
                map.put(COLUMN_TANGGALSURVEY, cursor.getString(21));
                wordList.add(map);
            } while (cursor.moveToNext());
        }

        Log.e("select sqlite ", "" + wordList);

        database.close();
        return wordList;
    }

    //tampil data yang statusnya 02
    public ArrayList<HashMap<String, String>> getStatus02(String koked02) {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM " + TABLE_SQLite + " WHERE " + COLUMN_STATUS + "=" + "'" + "02" + "'" + " AND " + COLUMN_KOKED + "=" + "'" + koked02 + "'" + " ORDER BY " + COLUMN_LANGKAH + " ASC ";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(COLUMN_ID, cursor.getString(0));
                map.put(COLUMN_BULAN, cursor.getString(1));
                map.put(COLUMN_UNITUP, cursor.getString(2));
                map.put(COLUMN_IDPEL, cursor.getString(3));
                map.put(COLUMN_NAMA, cursor.getString(4));
                map.put(COLUMN_ALAMAT, cursor.getString(5));
                map.put(COLUMN_TARIF, cursor.getString(6));
                map.put(COLUMN_DAYA, cursor.getString(7));
                map.put(COLUMN_KOKED, cursor.getString(8));
                map.put(COLUMN_LANGKAH, cursor.getString(9));
                map.put(COLUMN_LEMBAR, cursor.getString(10));
                map.put(COLUMN_TAGIHAN, cursor.getString(11));
                map.put(COLUMN_SPINERKET, cursor.getString(12));
                map.put(COLUMN_LATITUD, cursor.getString(13));
                map.put(COLUMN_LANGITUD, cursor.getString(14));
                map.put(COLUMN_KETERANGAN1, cursor.getString(15));
                map.put(COLUMN_KETERANGAN2, cursor.getString(16));
                map.put(COLUMN_KETERANGAN3, cursor.getString(17));
                map.put(COLUMN_RENCANABAYAR, cursor.getString(18));
                map.put(COLUMN_NOTELEPON, cursor.getString(19));
                map.put(COLUMN_STATUS, cursor.getString(20));
                map.put(COLUMN_TANGGALSURVEY, cursor.getString(21));
                wordList.add(map);
            } while (cursor.moveToNext());
        }

        Log.e("select sqlite ", "" + wordList);

        database.close();
        return wordList;
    }

    //tampil data pelunasan
    public ArrayList<HashMap<String, String>> getdatapelunasan(String koked03) {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM " + TABLE_SQLite + " WHERE " + COLUMN_SPINERKET + "=" + "'" + "J" + "'" + " AND " + COLUMN_KOKED + "=" + "'" + koked03 + "'" + " ORDER BY " + COLUMN_LANGKAH + " ASC ";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(COLUMN_ID, cursor.getString(0));
                map.put(COLUMN_BULAN, cursor.getString(1));
                map.put(COLUMN_UNITUP, cursor.getString(2));
                map.put(COLUMN_IDPEL, cursor.getString(3));
                map.put(COLUMN_NAMA, cursor.getString(4));
                map.put(COLUMN_ALAMAT, cursor.getString(5));
                map.put(COLUMN_TARIF, cursor.getString(6));
                map.put(COLUMN_DAYA, cursor.getString(7));
                map.put(COLUMN_KOKED, cursor.getString(8));
                map.put(COLUMN_LANGKAH, cursor.getString(9));
                map.put(COLUMN_LEMBAR, cursor.getString(10));
                map.put(COLUMN_TAGIHAN, cursor.getString(11));
                map.put(COLUMN_SPINERKET, cursor.getString(12));
                map.put(COLUMN_LATITUD, cursor.getString(13));
                map.put(COLUMN_LANGITUD, cursor.getString(14));
                map.put(COLUMN_KETERANGAN1, cursor.getString(15));
                map.put(COLUMN_KETERANGAN2, cursor.getString(16));
                map.put(COLUMN_KETERANGAN3, cursor.getString(17));
                map.put(COLUMN_RENCANABAYAR, cursor.getString(18));
                map.put(COLUMN_NOTELEPON, cursor.getString(19));
                map.put(COLUMN_STATUS, cursor.getString(20));
                map.put(COLUMN_TANGGALSURVEY, cursor.getString(21));
                wordList.add(map);
            } while (cursor.moveToNext());
        }

        Log.e("select sqlite ", "" + wordList);

        database.close();
        return wordList;
    }

    public ArrayList<HashMap<String, String>> tampilsemua() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM " + TABLE_SQLite;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(COLUMN_ID, cursor.getString(0));
                map.put(COLUMN_BULAN, cursor.getString(1));
                map.put(COLUMN_UNITUP, cursor.getString(2));
                map.put(COLUMN_IDPEL, cursor.getString(3));
                map.put(COLUMN_NAMA, cursor.getString(4));
                map.put(COLUMN_ALAMAT, cursor.getString(5));
                map.put(COLUMN_TARIF, cursor.getString(6));
                map.put(COLUMN_DAYA, cursor.getString(7));
                map.put(COLUMN_KOKED, cursor.getString(8));
                map.put(COLUMN_LANGKAH, cursor.getString(9));
                map.put(COLUMN_LEMBAR, cursor.getString(10));
                map.put(COLUMN_TAGIHAN, cursor.getString(11));
                map.put(COLUMN_SPINERKET, cursor.getString(12));
                map.put(COLUMN_LATITUD, cursor.getString(13));
                map.put(COLUMN_LANGITUD, cursor.getString(14));
                map.put(COLUMN_KETERANGAN1, cursor.getString(15));
                map.put(COLUMN_KETERANGAN2, cursor.getString(16));
                map.put(COLUMN_KETERANGAN3, cursor.getString(17));
                map.put(COLUMN_RENCANABAYAR, cursor.getString(18));
                map.put(COLUMN_NOTELEPON, cursor.getString(19));
                map.put(COLUMN_STATUS, cursor.getString(20));
                map.put(COLUMN_TANGGALSURVEY, cursor.getString(21));
                wordList.add(map);
            } while (cursor.moveToNext());
        }

        Log.e("select sqlite ", "" + wordList);

        database.close();
        return wordList;
    }

    public void insert( String bulan, String unitup, String idpel, String nama, String alamat, String tarif, String daya, String koked, int langkah,
                        String lembar, String tagihan, String spinerket, String latitud, String langitud, String keterangan1, String keterangan2, String keterangan3,
                        String rencanabayar, String notelepon, String status, String tanggalsurvey) {
        SQLiteDatabase database = this.getWritableDatabase();
        String queryValues = "INSERT INTO " + TABLE_SQLite + " (bulan, unitup, idpel, nama, alamat, tarif, daya, koked, langkah, lembar, tagihan," +
                "spinerket, latitud, langitud, keterangan1, keterangan2, keterangan3, rencanabayar, notelepon, status, tanggalsurvey) " +
                "VALUES ('" + bulan + "','" + unitup  + "','" + idpel  + "' ,'" + nama  + "','" + alamat  + "','" + tarif + "', '" + daya +
                "','" + koked + "','" + langkah + "' ,'" + lembar + "','" + tagihan + "', " +
                " '" + spinerket + "', '" + latitud + "', '" + langitud + "', '" + keterangan1 + "', " +
                " '" + keterangan2 + "', '" + keterangan3 + "', '" + rencanabayar + "', '" + notelepon + "', '" + status + "', '" + tanggalsurvey + "')";

        Log.e("insert sqlite ", "" + queryValues);
        database.execSQL(queryValues);
        database.close();
    }

    public void update(int id, String bulan, String unitup, String idpel, String nama, String alamat, String tarif,
                       String daya, String koked, int langkah, String lembar,
                        String tagihan, String spinerket, String latitud, String langitud,
                        String keterangan1, String keterangan2, String keterangan3,
                       String rencanabayar, String notelepon, String status, String tanggalsurvey) {
        SQLiteDatabase database = this.getWritableDatabase();

        String updateQuery = "UPDATE " + TABLE_SQLite + " SET "
                + COLUMN_BULAN + "='" + bulan + "', "
                + COLUMN_UNITUP + "='" + unitup + "', "
                + COLUMN_IDPEL + "='" + idpel + "', "
                + COLUMN_NAMA + "='" + nama + "', "
                + COLUMN_ALAMAT + "='" + alamat + "', "
                + COLUMN_TARIF + "='" + tarif + "', "
                + COLUMN_DAYA + "='" + daya + "', "
                + COLUMN_KOKED + "='" + koked + "', "
                + COLUMN_LANGKAH + "='" + langkah + "', "
                + COLUMN_LEMBAR + "='" + lembar + "', "
                + COLUMN_TAGIHAN + "='" + tagihan + "', "
                + COLUMN_SPINERKET + "='" + spinerket + "', "
                + COLUMN_LATITUD + "='" + latitud + "', "
                + COLUMN_LANGITUD + "='" + langitud + "', "
                + COLUMN_KETERANGAN1 + "='" + keterangan1 + "', "
                + COLUMN_KETERANGAN2 + "='" + keterangan2 + "', "
                + COLUMN_KETERANGAN3 + "='" + keterangan3 + "', "
                + COLUMN_RENCANABAYAR + "='" + rencanabayar + "', "
                + COLUMN_NOTELEPON + "='" + notelepon + "', "
                + COLUMN_STATUS + "='" + status + "', "
                + COLUMN_TANGGALSURVEY + "='" + tanggalsurvey + "'"
                + " WHERE " + COLUMN_ID + "=" + "'" + id + "'";
        Log.e("update sqlite nya ", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

    public void deleteall() {
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "DELETE FROM " + TABLE_SQLite;
        Log.e("update sqlite ", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

    public void deletebykoked(String koked) {
        SQLiteDatabase database = this.getWritableDatabase();

        String updateQuery = "DELETE FROM " + TABLE_SQLite + " WHERE " + COLUMN_KOKED + " = '" + koked + "'";
        Log.e("delete sqlite ", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

    public void hapusperstatus(String koked) {
        SQLiteDatabase database = this.getWritableDatabase();

        String updateQuery = "DELETE FROM " + TABLE_SQLite + " WHERE " + COLUMN_KOKED + " = '" + koked + "'" + "AND " + COLUMN_STATUS + "= '" + "02" +"'" + "AND " + COLUMN_SPINERKET + "= '" + "L" + "'" + "OR " + COLUMN_KOKED + " = '" + koked + "'" + "AND " + COLUMN_STATUS + "= '" + "02" +"'" + "AND " + COLUMN_SPINERKET + "= '" + "L" + "'" ;
        Log.e("delete sqlite ", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

    public void hapusdatakunjungan(String koked) {
        SQLiteDatabase database = this.getWritableDatabase();

        String updateQuery = "DELETE FROM " + TABLE_SQLite + " WHERE " + COLUMN_KOKED + " = '" + koked + "'" + "AND " + COLUMN_STATUS + "= '" + "00" +"'" ;
        Log.e("delete sqlite ", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

    public int cekidpel(String idpel) {
        String countQuery = "SELECT  * FROM " + TABLE_SQLite + " WHERE " + COLUMN_IDPEL + " = '" + idpel + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        Log.e(" idpelquery ", countQuery);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int cekkoked(String cekkoked) {
        String countQuery = "SELECT  * FROM " + TABLE_SQLite + " WHERE " + COLUMN_KOKED + " = '" + cekkoked + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        Log.e(" kokedquery ", countQuery);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int ceksemuadata() {
        String countQuery = "SELECT  * FROM " + TABLE_SQLite;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        Log.e(" ceksemuadata ", countQuery);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}

/*
    public void count(){
        SQLiteDatabase db = table.getWritableDatabase();
        String count = "SELECT count(*) FROM table";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int count = mcursor.getInt(0);
        System.out.println("NUMBER IN DB: " + icount);
    }
 */

