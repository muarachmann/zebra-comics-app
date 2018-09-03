package com.level500.ub.zebracomics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class RateAppFragment extends Fragment {
    private static String FACEBOOK_URL = "https://www.facebook.com/ZebraComicsInc";
    private static String FACEBOOK_PAGE_ID = "ZebraComicsInc";
    private static String TWITTER_USERNAME = "zebracomicsinc";
    private static String INSTAGRAM_USERNAME = "zebra.comics.inc";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        View view = inflater.inflate(R.layout.rate_app, container, false);

        Button btnWebsite = (Button) view.findViewById(R.id.rate_app_btn);
        btnWebsite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                String url = "https://www.google.com/";
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                startActivity(i);
                //placeholder
                Toast.makeText(getActivity(), "Rate feature will be possible when app is on play store", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


}
