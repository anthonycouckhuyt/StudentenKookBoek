package fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import be.howest.nmct.receptenapp.R;
import data.Recept;
import data.helpers.SwipeDismissListViewTouchListener;

/**
 * Created by Toine on 25/11/2014.
 */
public class ReceptFavoriteFragment extends ListFragment {
    private ArrayList<Recept> arrFavoriteRecipes;
    private FavoriteAdapter mAdapter;

    //GLOBAL
    public static final String ARR_FAVORITE_RECIPES = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle bundle = this.getArguments();
        arrFavoriteRecipes = bundle.getParcelableArrayList(ARR_FAVORITE_RECIPES);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_favorite, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            /*case R.id.action_TestRecepi:
                return false;
            case R.id.action_TestFavorite:
                return false;*/
            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Alles verwijderen");
                builder.setMessage("Bent u zeker dat u alle items in favorieten wilt verwijderen?");
                builder.setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mAdapter.clearAdapter();

                            }
                        })
                        .setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new FavoriteAdapter();
        setListAdapter(mAdapter);

        ListView listView = getListView();
        listView.setAdapter(mAdapter);
        SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(
                listView,
                new SwipeDismissListViewTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {return true; }

                    @Override
                    public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                        for(int position : reverseSortedPositions){
                            mAdapter.remove(mAdapter.getItem(position));
                        }
                        mAdapter.notifyDataSetChanged();

                    }
                });
        listView.setOnTouchListener(touchListener);
        listView.setOnScrollListener(touchListener.makeScrollListener());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getActivity(), favorietenLijst.get(i).getName(), Toast.LENGTH_SHORT).show();

                ReceptDetailFragment fragment = new ReceptDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("MYSELECTEDRECIPE", arrFavoriteRecipes.get(i));
                fragment.setArguments(bundle);

                getFragmentManager().beginTransaction().replace(R.id.mainfragment,fragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, null, false);
    }

    public class FavoriteAdapter extends ArrayAdapter<Recept> {

        public FavoriteAdapter(){
            super(getActivity(), R.layout.row_favorites, R.id.recept_naam, arrFavoriteRecipes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Recept recept = arrFavoriteRecipes.get(position);
            View row = super.getView(position, convertView, parent);

            ImageView imageView = (ImageView) row.findViewById(R.id.receptImage);
            int img;
            if(recept.getPicture() != null){
                img = Integer.parseInt(recept.getPicture());
            } else {
                img = R.drawable.ic_noimage;
            }
            imageView.setImageResource(img);



            TextView textView = (TextView) row.findViewById(R.id.recept_naam);
            textView.setText(recept.getName());

            return row;
        }

        public void clearAdapter(){
            arrFavoriteRecipes.clear();
            notifyDataSetChanged();
        }
        public void NotifyAdapter(){
            notifyDataSetChanged();
        }
    }
}