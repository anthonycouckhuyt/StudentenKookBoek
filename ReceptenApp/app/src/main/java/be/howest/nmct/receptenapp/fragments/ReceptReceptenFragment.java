package be.howest.nmct.receptenapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import be.howest.nmct.receptenapp.R;
import be.howest.nmct.receptenapp.contentprovider.ReceptenAppContentProvider;
import be.howest.nmct.receptenapp.data.CategoryData.Category;
import be.howest.nmct.receptenapp.data.CategoryData.CategoryTable;
import be.howest.nmct.receptenapp.data.ReceptData.Recept;
import be.howest.nmct.receptenapp.data.ReceptData.ReceptTable;
import be.howest.nmct.receptenapp.data.RecipeView;
import be.howest.nmct.receptenapp.data.helpers.ImageConverter;

/**
 * Created by Mattias on 17/11/2014.
 */
public class ReceptReceptenFragment extends ListFragment {

    Context context = getActivity();
    //ReceptenAdapter receptenAdapter;
    private TextView txvTitle;
    private ListView listView;

    //CURSOR
    private Cursor mCursor;
    private Cursor catCursor;
    MyCursorAdapter receptenAdapter;

    //global date here:
    Category category;
    ArrayList<Recept> arrRecipes;
    RecipeView data;

    //KEYS
    public static final String SELECTED_CATEGORIE = "";
    public static final String ARR_RECIPES = "";
    public static final String RECIPE_VIEW = "";

    private static View view;

    public ReceptReceptenFragment(){}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        //1. get ID van CATEGORY

        //2. get Cursor list van Recepten by ID


        /* TEMP IN COMMENTAAR
        Bundle bundle = this.getArguments();
        data = bundle.getParcelable(RECIPE_VIEW);
        category = data.getCategorie();
        arrRecipes = data.getArrRecipes();
        */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //getActivity().setTitle(category.getName());
        if(view != null){
            ViewGroup parent = (ViewGroup) view.getParent();
            if(parent != null){
                parent.removeView(view);
            }
        }
        try{
            view = inflater.inflate(R.layout.fragment_recepten, container, false);
        } catch(InflateException e){

        }

        /*((TextView) view.findViewById(R.id.Title)).setText("");
        txvTitle = (TextView) view.findViewById(R.id.Title);
        */return view;
    }

    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        listView = getListView();

        Bundle bundle = this.getArguments();
        Uri uri = bundle.getParcelable(ReceptenAppContentProvider.CONTENT_ITEM_REC);

        mCursor = context.getContentResolver().query(uri, null, null, null, null);

        if(mCursor != null){
            //display
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    receptenAdapter = new MyCursorAdapter(getActivity(), mCursor, 0);
                    listView.setAdapter(receptenAdapter);
                }

            });
        }

        Uri catUri = Uri.parse(ReceptenAppContentProvider.CONTENT_URI_CAT + "/" + uri.getLastPathSegment());

        catCursor = context.getContentResolver().query(catUri, null,null,null,null);
        if(catCursor.getCount() != 0){
            catCursor.moveToFirst();
            getActivity().getActionBar().setSubtitle(catCursor.getString(catCursor.getColumnIndex(CategoryTable.COLUMN_NAME)));
        }
    }


    public class MyCursorAdapter extends CursorAdapter {
        private LayoutInflater mInflater;
        // Default constructor
        public MyCursorAdapter(Context context, Cursor cursor, int flags) {
            super(context, cursor, flags);
                  mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }

        public void bindView(View view, Context context, Cursor cursor) {
            TextView naam = (TextView) view.findViewById(R.id.recept_naam);
            naam.setText(cursor.getString(cursor.getColumnIndex(ReceptTable.COLUMN_NAME)));

            ImageView image = (ImageView) view.findViewById(R.id.receptImage);
            image.setImageBitmap(ImageConverter.StringToBitmap(cursor.getString(cursor.getColumnIndex(ReceptTable.COLUMN_PICTURE))));
        }

        public View newView(Context context, Cursor cursor, ViewGroup parent) {
              return mInflater.inflate(R.layout.row_recept, parent, false);
        }
    }


           //CLICK ON LIST
    OnReceptenSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnReceptenSelectedListener {
        public void OnReceptenSelectedListener(int id);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnReceptenSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnReceptenSelectedListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mCursor.moveToPosition(position);

        mCallback.OnReceptenSelectedListener(mCursor.getInt(mCursor.getColumnIndex(ReceptTable.COLUMN_ID)));
    }


    private String grid_currentQuery = null; // holds the current query...

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.search, menu);
        SearchView searchView = (SearchView)menu.findItem(R.id.menu_item_search).getActionView();
        searchView.setOnQueryTextListener(queryListener);


    }
    private SearchView.OnQueryTextListener queryListener = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextChange(String newText) {
            if (TextUtils.isEmpty(newText)) {

                grid_currentQuery = null;
            } else {

                grid_currentQuery = newText;

            }
            //getLoaderManager().restartLoader(0, null, MyListFragment.this);
            return false;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            ReceptSearchFragment recSFrag = new ReceptSearchFragment();
            Bundle bundle = new Bundle();
            Uri uri = Uri.parse(ReceptenAppContentProvider.CONTENT_URI_REC + "/RecByQUERY/" + query);
            bundle.putParcelable(ReceptenAppContentProvider.CONTENT_ITEM_REC, uri);
            recSFrag.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.mainfragment, recSFrag).addToBackStack("SEARCH").commit();
            return false;
        }
    };


}
