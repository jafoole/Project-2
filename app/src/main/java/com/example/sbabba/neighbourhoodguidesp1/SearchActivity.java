package com.example.sbabba.neighbourhoodguidesp1;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.support.design.widget.FloatingActionButton;


import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.SimpleCursorSwipeAdapter;
import com.example.sbabba.neighbourhoodguidesp1.setup.DBAssetHelper;


import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ListView mFavoritesListView;
    private NeighbourSQLiteOpenHelper mHelper;
    private int mRequestCode;
    private Button mSwipeDelete;
    private SwipeAdapter mSwipeAdapter;

  /*         /\     /\      __        _____              __                /\      /\
            \ \    \ \     |__|____ _/ ____\____   ____ |  |   ____       / /     / /
            \ \    \ \     |  \__  \\   __\/  _ \ /  _ \|  | _/ __ \     / /     / /
            \ \    \ \     |  |/ __ \|  | (  <_> |  <_> )  |_\  ___/    / /     / /
            \ \    \ \ /\__|  (____  /__|  \____/ \____/|____/\___  >  / /     / /
            \/     \/  \______|    \/                             \/   \/      \/ */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Ignore the two lines below, they are for setup
        DBAssetHelper dbSetup = new DBAssetHelper(SearchActivity.this);
        dbSetup.getReadableDatabase();


        //Reference to the open helper where the database is.
        mHelper = NeighbourSQLiteOpenHelper.getInstance(SearchActivity.this);

        mFavoritesListView = (ListView)findViewById(R.id.favoritesListView);

        //This onItemClickListener is for when you press a location on the list. It will direct you to the description of the list.
        //We are passing the cursor over which includes the details of the location.
        mFavoritesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = mSwipeAdapter.getCursor();
                Intent intent = new Intent(SearchActivity.this, DescriptionActivity.class);
                cursor.moveToPosition(position);
                intent.putExtra("_id", cursor.getInt(cursor.getColumnIndex(NeighbourSQLiteOpenHelper.COL_ID)));
                startActivity(intent);

            }
        });
    }

    //Creating the searchView
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView)menu.findItem(R.id.search).getActionView();



        ComponentName component = new ComponentName(this, SearchActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(component));
        searchView.getSuggestionsAdapter();




        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Cursor cursor = mHelper.searchNeighbourHoodList(query);
                mSwipeAdapter.swapCursor(cursor);
                searchView.clearFocus();



                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }
        });

        return true;
    }

    //This onResume is when we return to the Home Page (SearchActivity.this) it will display the favorite's list.
    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor = mHelper.getFavoritesList();
        mSwipeAdapter = new SwipeAdapter(SearchActivity.this, cursor);
        mFavoritesListView.setAdapter(mSwipeAdapter);
        mSwipeAdapter.swapCursor(cursor);

    }
}

