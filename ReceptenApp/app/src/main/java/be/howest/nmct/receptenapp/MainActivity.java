package be.howest.nmct.receptenapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import data.Category;
import data.Ingredient;
import data.Recept;
import fragments.ReceptCategoriesFragment;
import fragments.ReceptNavigationFragment;
import fragments.ReceptReceptenFragment;

public class MainActivity extends FragmentActivity
        implements ReceptNavigationFragment.OnNavigationSelectedListener,
        ReceptCategoriesFragment.OnCategorieSelectedListener,
        ReceptReceptenFragment.OnReceptenSelectedListener{

    private String[] arrNavigation;

    //NAVIGATION
    private DrawerLayout navigationDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private View navigationView;

    //globale vars here:
    private ArrayList<Category> arrCategories;

    //Globale vars
    //  Boodschappenlijstje
    public static ArrayList<Ingredient> BOODSCHAPPENLIJSTJE = new ArrayList<Ingredient>();
    public static ArrayList<Category> ARRCATEGORIES = new ArrayList<Category>();
    //tijdelijk


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrNavigation = getResources().getStringArray(R.array.MenuBasic);

        navigationView = (View) findViewById(R.id.navigation);
        mTitle = mDrawerTitle = getTitle();
        navigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, navigationDrawer,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        navigationDrawer.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);


        if(navigationDrawer.isDrawerOpen(navigationView)){
            navigationDrawer.closeDrawer(navigationView);}


        if (savedInstanceState == null) {
            //get cats
            arrCategories = ReceptCategoriesFragment.GetCategorie();

            ReceptCategoriesFragment catFrag = new ReceptCategoriesFragment();
            Bundle args = new Bundle();
            args.putParcelableArrayList(catFrag.ARR_CATEGORIE, arrCategories);
            catFrag.setArguments(args);
            getSupportFragmentManager().beginTransaction().add(R.id.mainfragment, catFrag).commit();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();

        switch (id){
            case R.id.action_TestRecepi:
                Intent intent = new Intent(MainActivity.this, ReceptDetailActivity.class);
                intent.putExtra("selectedRecipe", new Recept());
                startActivity(intent);
                return true;
            case R.id.action_TestFavorite:
                Intent intent2 = new Intent(MainActivity.this, FavoriteActivity.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void onNavigationSelected(int position, boolean isLogin) {
        //case 0-2 (blijft hetzelfde)
        if(navigationDrawer.isDrawerOpen(navigationView)){
            navigationDrawer.closeDrawer(navigationView);}

        switch (position){
            case 0: //Categorien
                ReceptCategoriesFragment catFrag = new ReceptCategoriesFragment();
                Bundle args = new Bundle();
                args.putParcelableArrayList(catFrag.ARR_CATEGORIE, arrCategories );
                catFrag.setArguments(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment, catFrag).commit();
                break;

            case 1: //Boodschappenlijstje
                Toast.makeText(MainActivity.this, "Boodschappenlijstje", Toast.LENGTH_SHORT).show();
                break;

            case 2: //Favorieten
                Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
                startActivity(intent);
                break;
        }

        //indien normal user
        //indien ingelogd
        if(isLogin){
            switch (position){
                case 3:
                    Toast.makeText(MainActivity.this, "Recept toevoegen", Toast.LENGTH_SHORT).show();
                    break;

                case 4:
                    Toast.makeText(MainActivity.this, "Vrienden", Toast.LENGTH_SHORT).show();
                    break;

                case 5:
                    Toast.makeText(MainActivity.this, "Profiel", Toast.LENGTH_SHORT).show();
                    break;

                case 6:
                    //USER UITLOGGEN
                    FragmentManager fm = getSupportFragmentManager();

                    //if you added fragment via layout xml
                    ReceptNavigationFragment fragment = (ReceptNavigationFragment) fm.findFragmentById(R.id.fragment_navigation);
                    fragment.ShowNavigation();
                    break;
            }
        }else {
            if (position == 3) {
                FragmentManager fm = getSupportFragmentManager();

                //if you added fragment via layout xml
                ReceptNavigationFragment fragment = (ReceptNavigationFragment) fm.findFragmentById(R.id.fragment_navigation);
                fragment.ShowNavigation();
            }
        }
        //uiteindelijk

    }


    public void OnCategorieSelectedListener(Category category) {
        //if position == ...

        ReceptReceptenFragment recFrag = new ReceptReceptenFragment();
        Bundle args = new Bundle();
        args.putParcelable(recFrag.SELECTED_CATEGORIE, category);
        recFrag.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment, recFrag).addToBackStack(null).commit();

    }

    public void OnReceptenSelectedListener(Recept recept) {
        Intent intent = new Intent(MainActivity.this, ReceptDetailActivity.class);
        intent.putExtra("selectedRecipe", recept);
        startActivity(intent);
    }
}
