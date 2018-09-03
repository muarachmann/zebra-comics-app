package com.level500.ub.zebracomics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.cachapa.expandablelayout.ExpandableLayout;


public class FaqsFragment extends Fragment implements View.OnClickListener{

    private ExpandableLayout expandableLayout1,expandableLayout2 , expandableLayout3 , expandableLayout4, expandableLayout5;
    private ExpandableLayout expandableLayout6 , expandableLayout7, expandableLayout8, expandableLayout9, expandableLayout10;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        View view = inflater.inflate(R.layout.faqs_fragment, container, false);


        expandableLayout1 = view.findViewById(R.id.expandable_layout_1);
        expandableLayout2 = view.findViewById(R.id.expandable_layout_2);
        expandableLayout3 = view.findViewById(R.id.expandable_layout_3);
        expandableLayout4 = view.findViewById(R.id.expandable_layout_4);
        expandableLayout5 = view.findViewById(R.id.expandable_layout_5);

        expandableLayout1.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {
                Log.d("ExpandableLayout0", "State: " + state);
            }
        });

        expandableLayout2.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {
                Log.d("ExpandableLayout1", "State: " + state);
            }
        });

        view.findViewById(R.id.expand_button_1).setOnClickListener(this);
        view.findViewById(R.id.expand_button_2).setOnClickListener(this);
        view.findViewById(R.id.expand_button_3).setOnClickListener(this);
        view.findViewById(R.id.expand_button_4).setOnClickListener(this);
        view.findViewById(R.id.expand_button_5).setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.expand_button_1) {
            expandableLayout1.expand();
            expandableLayout2.collapse();
            expandableLayout3.collapse();
            expandableLayout4.collapse();
            expandableLayout5.collapse();
        }
        if (id == R.id.expand_button_2) {
            expandableLayout2.expand();
            expandableLayout1.collapse();
            expandableLayout3.collapse();
            expandableLayout4.collapse();
            expandableLayout5.collapse();
        }

        if (id == R.id.expand_button_3) {
            expandableLayout3.expand();
            expandableLayout1.collapse();
            expandableLayout2.collapse();
            expandableLayout4.collapse();
            expandableLayout5.collapse();
        }

        if (id == R.id.expand_button_4) {
            expandableLayout4.expand();
            expandableLayout1.collapse();
            expandableLayout2.collapse();
            expandableLayout3.collapse();
            expandableLayout5.collapse();
        }

        if (id == R.id.expand_button_5) {
            expandableLayout5.expand();
            expandableLayout1.collapse();
            expandableLayout2.collapse();
            expandableLayout3.collapse();
            expandableLayout4.collapse();
        }
    }
}
