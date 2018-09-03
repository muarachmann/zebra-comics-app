package com.level500.ub.zebracomics;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ZebraProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Button btnEditProfile;
    private ImageView changeProfile;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        final View view = inflater.inflate(R.layout.zebra_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnEditProfile = (Button) view.findViewById(R.id.edit_user_profile);
        changeProfile =  (ImageView) view.findViewById(R.id.change_profile_image);
        db.collection("users").document(Objects.requireNonNull(mAuth.getUid()))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String user_name = documentSnapshot.getString("username");
                        String email  = documentSnapshot.getString("email");
                        String phone  = documentSnapshot.getString("phone");
                        String pic  = documentSnapshot.getString("profile_pic");
                        String country  = documentSnapshot.getString("country");
                        String state  = documentSnapshot.getString("state");

                        final TextView userName = (TextView)view.findViewById(R.id.user_profile_name);
                        final TextView emailName = (TextView)view.findViewById(R.id.user_profile_short_bio);
                        final TextView phoneTxt = (TextView)view.findViewById(R.id.profilePhone);
                        final TextView countryTxt = (TextView)view.findViewById(R.id.profileCountry);
                        final TextView stateTxt = (TextView)view.findViewById(R.id.profileState);
                        final TextView emailTxt = (TextView)view.findViewById(R.id.profileEmail);
                        userName.setText(user_name);
                        emailName.setText(email);
                        phoneTxt.setText(phone);
                        countryTxt.setText(country);
                        stateTxt.setText(state);
                        emailTxt.setText(email);

                        final ImageView imageViewUser =  (ImageView)view.findViewById(R.id.user_profile_photo);
                        Picasso.with(getActivity())
                                .load(pic)
                                .transform(new RoundedTransformation(100, 0))
                                .fit()
                                .into(imageViewUser);
                    }
                });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                EditProfileFragment nextFrag = new EditProfileFragment();
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, nextFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });

        changeProfile.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                ChangePicFragment nextFrag = new ChangePicFragment();
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, nextFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });


        return view;
    }

    public class RoundedTransformation implements
            com.squareup.picasso.Transformation {
        private final int radius;
        private final int margin; // dp

        // radius is corner radii in dp
        // margin is the board in dp
        public RoundedTransformation(final int radius, final int margin) {
            this.radius = radius;
            this.margin = margin;
        }

        @Override
        public Bitmap transform(final Bitmap source) {
            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP));

            Bitmap output = Bitmap.createBitmap(source.getWidth(),
                    source.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            canvas.drawRoundRect(new RectF(margin, margin, source.getWidth()
                    - margin, source.getHeight() - margin), radius, radius, paint);

            if (source != output) {
                source.recycle();
            }

            return output;
        }

        @Override
        public String key() {
            return "rounded";
        }
    }


}
