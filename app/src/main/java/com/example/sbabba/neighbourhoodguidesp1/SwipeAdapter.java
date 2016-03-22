package com.example.sbabba.neighbourhoodguidesp1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by sbabba on 2/11/16.
 */
public class SwipeAdapter extends SimpleCursorAdapter {
    public SwipeAdapter(Context context, Cursor cursor) {
        super(context, R.layout.item, cursor, new String[]{NeighbourSQLiteOpenHelper.COL_LOCATION_NAME}, new int[]{R.id.textSurfaceView}, 0);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        super.bindView(view, context, cursor);

        final ImageView deleteFavoriteButton = (ImageView)view.findViewById(R.id.deleteFavoriteButton);

        deleteFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NeighbourSQLiteOpenHelper helper = NeighbourSQLiteOpenHelper.getInstance(context);
                int id = cursor.getInt(cursor.getColumnIndex(NeighbourSQLiteOpenHelper.COL_ID));
                SQLiteDatabase db = helper.getWritableDatabase();
                db.execSQL("UPDATE NeighbourHood_Table SET FAVORITES = 0 WHERE _id = " + id);
                Cursor cursor1 = helper.getFavoritesList();
                swapCursor(cursor1);
                notifyDataSetChanged();

            }
        });
    }
}

