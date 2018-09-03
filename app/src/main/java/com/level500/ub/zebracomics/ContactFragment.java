package com.level500.ub.zebracomics;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends Fragment {
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
        View view = inflater.inflate(R.layout.contact_us, container, false);




        Button btnFacebook = (Button) view.findViewById(R.id.zebra_facebook_btn);
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(getContext());
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
            }
        });

        Button btnGmail = (Button) view.findViewById(R.id.zebra_gmail_btn);
        btnGmail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL  , new String[] { "zebracomics@gmail.com" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "Contact Zebra Comics");

                startActivity(Intent.createChooser(intent, "Email via..."));
            }
        });

        Button btnInstagram = (Button) view.findViewById(R.id.zebra_instagram_btn);
        btnInstagram.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = getOpenTwitterIntent(getActivity(), INSTAGRAM_USERNAME);
                startActivity(i);
            }
        });

        Button btnTwitter = (Button) view.findViewById(R.id.zebra_twitter_btn);
        btnTwitter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = getOpenTwitterIntent(getActivity(), TWITTER_USERNAME);
                startActivity(i);
            }
        });

        Button btnWhatsapp = (Button) view.findViewById(R.id.zebra_whatsapp_btn);
        btnWhatsapp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String phone = "+237 682 70 96 76";
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
                        "tel", phone, null));
                startActivity(phoneIntent);
            }
        });

        return view;
    }

    public static Intent getOpenTwitterIntent(Context c, String Username) {

        try {
            c.getPackageManager().getPackageInfo("com.twitter.android", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name="+ Username));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/" + Username));
        }
    }

    public static Intent getOpenInstagramIntent(Context c, String Username) {

        try {
            c.getPackageManager().getPackageInfo("com.instagram.android", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/"+ Username));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/" + Username));
        }
    }


    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

}
