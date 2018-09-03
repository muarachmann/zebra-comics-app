package com.level500.ub.zebracomics;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TitlesFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    TitlesAdapter mAdapter;
    private List<Titles> titlesList;
    private SearchView comicsSearch = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String nameHolder;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        View view = inflater.inflate(R.layout.fragment_titles, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.mytitles);
        Objects.requireNonNull(getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Bundle bundle = this.getArguments();

        if(bundle != null){
            String uid = bundle.getString("uid");
            nameHolder = bundle.getString("name");

            Toast.makeText(getContext(), uid, Toast.LENGTH_SHORT).show();
        }

        titlesList = new ArrayList<>();
        mAdapter = new TitlesAdapter(getContext(), titlesList);



        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Retrieving titles...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        db.collection("comics").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getId();
                        String comicsnum = document.getString("actNumber");
                        String name = document.getString("name");
                        String img = document.getString("img");
                        prepareAlbums(name, comicsnum, img, id);
                        Toast.makeText(getContext(), comicsnum, Toast.LENGTH_SHORT).show();
                    }
                    addBluredImage(nameHolder+"#(...)", "Coming soon","https://firebasestorage.googleapis.com/v0/b/zebracomics-4aede.appspot.com/o/logo_icon.png?alt=media&token=b25c1852-0925-479c-98c2-1d6453226317", "dummy_id" );

                } else {
                    Toast.makeText(getContext(), "Error getting documents: "+ task.getException(), Toast.LENGTH_SHORT).show();
                }
            }

        });

        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) view.findViewById(R.id.thumbnail));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    /**
     * Adding few albums for testing
     * @param name
     * @param comicsnum
     */
    private void prepareAlbums(String name, String comicsnum, String img, String id) {

        Titles a = new Titles(name, comicsnum, img, id);
        titlesList.add(a);

        mAdapter.notifyDataSetChanged();
    }

    private void addBluredImage(String name, String comicsnum, String img, String id){
        Titles a = new Titles(name, comicsnum, img, id);
        titlesList.add(a);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.searchBar);
        SearchManager searchManager = (SearchManager) Objects.requireNonNull(getActivity()).getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            comicsSearch = (SearchView) searchItem.getActionView();
        }
        if (comicsSearch != null) {
            assert searchManager != null;
            comicsSearch.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Toast.makeText(getContext(), newText, Toast.LENGTH_SHORT).show();
                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Toast.makeText(getContext(), query, Toast.LENGTH_SHORT).show();
                    return true;
                }
            };
            comicsSearch.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchBar:
                // Not implemented here
                return false;
            default:
                break;
        }
        comicsSearch.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }
}
