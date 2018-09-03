package com.level500.ub.zebracomics;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.github.florent37.tutoshowcase.TutoShowcase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.actionitembadge.library.ActionItemBadgeAdder;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.squareup.picasso.Picasso;

import java.util.Map;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("CommitTransaction")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadFragment(new SettingFragment());


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu sideNavMenu = navigationView.getMenu();
        MenuItem generalMenu = sideNavMenu.findItem(R.id.nav_camera);
        ActionItemBadge.update(this, sideNavMenu.findItem(R.id.nav_camera), FontAwesome.Icon.faw_android, ActionItemBadge.BadgeStyles.DARK_GREY, 5);
        navigationView.setNavigationItemSelectedListener(this);

        db.collection("users").document(Objects.requireNonNull(mAuth.getUid()))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String user_name = documentSnapshot.getString("username");
                String email  = documentSnapshot.getString("email");
                String pic  = documentSnapshot.getString("profile_pic");

                //place image, username, email into the navigation drawer activity
                View headerView = navigationView.getHeaderView(0);
                final TextView userName = (TextView)headerView.findViewById(R.id.user_name);
                final TextView emailName = (TextView)headerView.findViewById(R.id.user_email);
                userName.setText(user_name);
                emailName.setText(email);
                final ImageView imageViewUser =  (ImageView)headerView.findViewById(R.id.user_profile_image);
                Picasso.with(HomeActivity.this).load(pic).fit().centerCrop().into(imageViewUser);
            }
        });



    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        if(getSupportFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        }
        else if(getSupportFragmentManager().getBackStackEntryCount() == 1) {
            moveTaskToBack(false);
        }
        else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Fragment fragment = null;

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about_zebra_comics) {
            fragment =  new AboutZebraFragment();
            return loadFragment(fragment);
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_contact_us) {
            fragment =  new ContactFragment();
            return loadFragment(fragment);
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_rate_us) {
            fragment =  new RateAppFragment();
            return loadFragment(fragment);
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(HomeActivity.this, WelcomeActivity.class);
                            startActivity(intent);
                        }
                    });
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        int id = item.getItemId();

        if (id == R.id.nav_profile){
            fragment =  new ZebraProfileFragment();
        }
        if (id == R.id.nav_camera) {
            fragment = new SettingFragment();
        }
        if (id == R.id.nav_gallery) {
            fragment = new Zebratars();
        }
        if (id == R.id.nav_slideshow) {
            fragment = new AboutFragment();
        }
        if (id == R.id.nav_manage) {

        }
        if (id == R.id.nav_language) {
            fragment = new LanguageFragment();

        }
        if (id == R.id.nav_share) {

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Your body here";
            String shareSub = "Your subject here";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
        }
        if (id == R.id.nav_send) {
            fragment = new TitlesFragment();
        }

        if (id == R.id.nav_faqs) {
            fragment = new FaqsFragment();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return loadFragment(fragment);
    }


    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }

    protected void displayTuto() {
        TutoShowcase.from(this)
                .setListener(new TutoShowcase.Listener() {
                    @Override
                    public void onDismissed() {
                        Toast.makeText(HomeActivity.this, "Tutorial dismissed", Toast.LENGTH_SHORT).show();
                    }
                })
                .setContentView(R.layout.tuto_sample_navigation)
                .setFitsSystemWindows(true)
                .on(R.id.nav_camera)
                .addCircle()
                .withBorder()
                .onClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })

                .on(R.id.toolbar)
                .displaySwipableLeft()
                .delayed(399)
                .animated(true)
                .show();
    }

    }
