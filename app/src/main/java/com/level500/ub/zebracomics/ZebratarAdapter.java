package com.level500.ub.zebracomics;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

class ZebratarAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int[] mResources = {
            R.drawable.b1,
            R.drawable.b2,
            R.drawable.b3,
            R.drawable.b4,
            R.drawable.b5,
            R.drawable.b6,
            R.drawable.b7,
            R.drawable.b8,
            R.drawable.b9
    };

    private String[] mNames = {
            "Aliya",
            "Tumbu",
            "Mua",
            "Rachmann",
            "Macoze",
            "MASALATI",
            "Pisoh",
            "Rasom",
            "Marvin",
    };

    private String[] mColors = {
            "#000000",
            "#34495e",
            "#d35400",
            "#239b56",
            "#c0392b",
            "#2874a6",
            "#ad17ab",
            "#918c1f",
            "#da6072",
            "#039BE5",
    };

    public ZebratarAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.zebratar_pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.zebratar_images);
        TextView textView = (TextView) itemView.findViewById(R.id.zebratar_names);
        imageView.setImageResource(mResources[position]);
        textView.setText(mNames[position].toUpperCase());
        textView.setTextColor(Color.parseColor(mColors[generateNum()]));
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }

    private int generateNum(){
        int max = 9;
        int min = 0;
        Random r = new Random();
        return (min + r.nextInt((max - min) + 1));
    }
}