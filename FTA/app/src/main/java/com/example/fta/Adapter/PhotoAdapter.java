package com.example.fta.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.fta.Models.Photo;
import com.example.fta.R;

import java.util.List;

public class PhotoAdapter extends PagerAdapter {
    private Context context;
    private List<Photo> photoList;

    public PhotoAdapter(Context context, List<Photo> photoList) {
        this.context = context;
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_slider, container, false);
        ImageView imgPhoto = view.findViewById(R.id.img_slider);

        Photo photo = photoList.get(position);
        if (photo != null){
            Glide.with(context).load(photo.getId()).into(imgPhoto);
        }

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        if (photoList != null){
            return photoList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
