package com.example.sbabba.neighbourhoodguidesp1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


/**
 * Created by sbabba on 2/4/16.
 */
public class NeighbourSQLiteOpenHelper extends SQLiteOpenHelper {
//    private static final String TAG = NeighbourSQLiteOpenHelper.class.getCanonicalName(); <---- Do I need this?????


    private static final int DATABASE_VERSION = 8;
    public static final String DATABASE_NAME = "NeighbourHood_Guides";
    public static final String NEIGHBOURHOOD_TABLE_NAME = "NeighbourHood_Table";

    public static final String COL_ID = "_id";
    public static final String COL_LOCATION_NAME = "LOCATION_NAME";
    public static final String COL_ADDRESS = "ADDRESS";
    public static final String COL_DESCRIPTION = "DESCRIPTION";
    public static final String COL_FAVORITES = "FAVORITES";
    public static final String COL_IMAGE = "IMAGE";
    public static final String COL_RATING = "RATING";


    public static final String[] NEIGHBOURHOOD_COLUMNS = {COL_ID, COL_LOCATION_NAME, COL_ADDRESS, COL_DESCRIPTION, COL_FAVORITES,COL_IMAGE,COL_RATING};

    private static final String CREATE_NEIGHBOURHOOD_GUIDES_TABLE =
            "CREATE TABLE " + NEIGHBOURHOOD_TABLE_NAME +
                    "(" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_LOCATION_NAME + " TEXT, " +
                    COL_ADDRESS + " TEXT, " +
                    COL_DESCRIPTION + " TEXT, " +
                    COL_FAVORITES + " TEXT, " +
                    COL_RATING + " TEXT, " +
                    COL_IMAGE + " TEXT )";


    private static NeighbourSQLiteOpenHelper instance;

    public static NeighbourSQLiteOpenHelper getInstance(Context context) {
        if (instance == null) {
            instance = new NeighbourSQLiteOpenHelper(context);
        }
        return instance;
    }


    public NeighbourSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NEIGHBOURHOOD_GUIDES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXITS " + NEIGHBOURHOOD_TABLE_NAME);
        this.onCreate(db);
    }

    //Add new itinerary list
    public long addItem(String locationName, String description, String address, String image, String favorites) {
        ContentValues values = new ContentValues();
        values.put(COL_LOCATION_NAME, locationName);
        values.put(COL_DESCRIPTION, description);
        values.put(COL_ADDRESS, address);
        values.put(COL_FAVORITES,favorites);
        values.put(COL_IMAGE, image);


        SQLiteDatabase db = this.getWritableDatabase();
        long returnId = db.insert(NEIGHBOURHOOD_TABLE_NAME, null, values);
        db.close();
        return returnId;
    }

    public Cursor getNeighbourHoodList() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(NEIGHBOURHOOD_TABLE_NAME, // a. table
                NEIGHBOURHOOD_COLUMNS, // b. column names
                null, // c. selections
                null, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        return cursor;
    }

    public int deleteItem(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int deleteNum = db.delete(NEIGHBOURHOOD_TABLE_NAME,
                COL_ID + " LIKE ?",
                new String[]{String.valueOf(id)});
        db.close();
        return deleteNum;
    }

    public Cursor searchNeighbourHoodList(String query) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(NEIGHBOURHOOD_TABLE_NAME,
                NEIGHBOURHOOD_COLUMNS,
                COL_LOCATION_NAME + " LIKE ? OR " + COL_ADDRESS + " LIKE ?",
                new String[]{"%" + query + "%", "%" + query + "%"},
                null,
                null,
                null,
                null);

        return cursor;
    }

    public String getLocationName(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(NEIGHBOURHOOD_TABLE_NAME,
                new String[]{COL_LOCATION_NAME},
                COL_ID + " LIKE ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(COL_LOCATION_NAME));
        } else {
            return "Not Found";
        }
    }

    public String getAddress(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(NEIGHBOURHOOD_TABLE_NAME,
                new String[]{COL_ADDRESS},
                COL_ID + " LIKE ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(COL_ADDRESS));
        } else {
            return "Not Found";
        }
    }

    public String getDescription(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(NEIGHBOURHOOD_TABLE_NAME,
                new String[]{COL_DESCRIPTION},
                COL_ID + " LIKE ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(COL_DESCRIPTION));
        } else {
            return "Not Found";
        }
    }

    public String getColFavorites(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(NEIGHBOURHOOD_TABLE_NAME,
                new String[]{COL_FAVORITES},
                COL_ID + " LIKE ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()){
            return cursor.getString(cursor.getColumnIndex(COL_FAVORITES));
        }else {
            return "Not Found";
        }
    }

    public Cursor getFavoritesList() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(NEIGHBOURHOOD_TABLE_NAME, // a. table
                NEIGHBOURHOOD_COLUMNS, // b. column names
                COL_FAVORITES,// c. selections
                null, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        return cursor;
    }

    public String getImage(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(NEIGHBOURHOOD_TABLE_NAME,
                new String[]{COL_IMAGE}, // b. column names
                COL_ID + " LIKE ?", // c. selections
                new String[]{String.valueOf(id)}, // d. selections args
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(COL_IMAGE));
        } else {
            return "Not Found";
        }
    }


        public String getRating(int id){
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(NEIGHBOURHOOD_TABLE_NAME,
                    new String[]{COL_RATING},
                    COL_ID + " LIKE ?",
                    new String[]{String.valueOf(id)},
                    null,
                    null,
                    null,
                    null
            );
            if (cursor.moveToFirst()){
                return cursor.getString(cursor.getColumnIndex(COL_RATING));
            }else {
                return "Not Found";
            }
        }
    }

