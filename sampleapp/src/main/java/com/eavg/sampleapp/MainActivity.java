package com.eavg.sampleapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.eavg.sampleapp.adapter.DrawerListAdapter;
import com.eavg.sampleapp.model.DrawerItem;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private String[] mDrawerTitles;
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerContainer;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<DrawerItem> mDrawerItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instantiateViews();
        prepareList();
        prepareDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Open the Home screen
        changeContentView(2);
    }

    private void instantiateViews() {
        mDrawerContainer = (RelativeLayout)findViewById(R.id.drawer_container);
        mDrawerTitles = getResources().getStringArray(R.array.drawer_strings);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.drawer_list);
        mDrawerItems = new ArrayList<DrawerItem>(mDrawerTitles.length);
    }

    private void prepareList() {
        for (String title : mDrawerTitles) {
            // Ignore separators
            if (!title.equalsIgnoreCase("separator")) {
                mDrawerItems.add(new DrawerItem(title, R.drawable.ic_launcher));
            }
        }
        mDrawerItems.add(0, new DrawerItem());
        mDrawerItems.add(4, new DrawerItem());

        mDrawerList.setAdapter(new DrawerListAdapter(getApplicationContext(), mDrawerItems));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View headerView = layoutInflater.inflate(R.layout.drawer_profile, mDrawerList, false);
        mDrawerList.addHeaderView(headerView);
    }

    private void prepareDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.app_name,
                R.string.app_name
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            changeContentView(i);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_language).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    private void changeContentView(int viewIndex) {
        Fragment fragment = null;
        Intent intent = null;
        switch (viewIndex) {
            case 2:
                fragment = new HomeFragment(566921);
                break;
            case 3:
                fragment = new HomeFragment(470265);
                break;
            case 4:
                fragment = new SearchFragment();
                break;
            case 6:
                fragment = new HomeFragment(695684);
                break;
            case 7:
                intent = new Intent(this, SettingsActivity.class);
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            mDrawerList.setItemChecked(viewIndex, true);
            mDrawerList.setSelection(viewIndex);
            getSupportActionBar().setTitle(mDrawerTitles[viewIndex]);
            mDrawerLayout.closeDrawer(mDrawerContainer);
        }
        else if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
