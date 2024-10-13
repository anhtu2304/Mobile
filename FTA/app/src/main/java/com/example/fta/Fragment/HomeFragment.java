package com.example.fta.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fta.Adapter.FoodListAdapter;
import com.example.fta.Adapter.PhotoAdapter;
import com.example.fta.Models.Food;
import com.example.fta.Models.Photo;
import com.example.fta.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class HomeFragment extends Fragment {
    private View mview;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private PhotoAdapter photoAdapter;
    private List<Photo> photoList;
    private Timer timer;
    private RecyclerView rcyMoi, rcyBanChay, rcyCombo;
    private FoodListAdapter adapter1, adapter2, adapter3;
    private ArrayList<Food> arr_spbanchay, arr_spitcalo, arr_sphoatoc;
    final private DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("uploads");
    final private DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("uploads");
    final private DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("uploads");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.fragment_home, container, false);
        addControls();
        return mview;
    }

    private void addControls() {
        viewPager = mview.findViewById(R.id.viewpager);
        circleIndicator = mview.findViewById(R.id.circle_indicator);

        photoList = getListPhoto();
        photoAdapter = new PhotoAdapter(this.getActivity(), photoList);
        viewPager.setAdapter(photoAdapter);

        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        autoSlideImage();

        rcyMoi = mview.findViewById(R.id.rcyMoi);
        rcyBanChay = mview.findViewById(R.id.rcyBanChay);
        rcyCombo = mview.findViewById(R.id.rcyCombo);

        StaggeredGridLayoutManager staggeredGridLayoutManager1 = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        rcyBanChay.setLayoutManager(staggeredGridLayoutManager1);
        arr_spbanchay=new ArrayList<>();
        adapter1=new FoodListAdapter(arr_spbanchay, this.getActivity());
        rcyBanChay.setAdapter(adapter1);
        Query query1 = databaseReference1.orderByChild("star");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Food food = dataSnapshot.getValue(Food.class);
                    if (food.getStar() >= 4){
                        arr_spbanchay.add(0, food);
                    }
                }

                adapter1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        StaggeredGridLayoutManager staggeredGridLayoutManager2 = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        rcyMoi.setLayoutManager(staggeredGridLayoutManager2);
        arr_sphoatoc=new ArrayList<>();
        adapter2=new FoodListAdapter(arr_sphoatoc, this.getActivity());
        rcyMoi.setAdapter(adapter2);
        Query query2 = databaseReference2.orderByChild("time");
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Food food = dataSnapshot.getValue(Food.class);
                        if (food.getTime() <= 10){
                            arr_sphoatoc.add(food);
                        }
                }

                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        StaggeredGridLayoutManager staggeredGridLayoutManager3 = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        rcyCombo.setLayoutManager(staggeredGridLayoutManager3);
        arr_spitcalo=new ArrayList<>();
        adapter3=new FoodListAdapter(arr_spitcalo, this.getActivity());
        rcyCombo.setAdapter(adapter3);
        Query query3 = databaseReference3.orderByChild("calo");
        query3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Food food = dataSnapshot.getValue(Food.class);
                    if (food.getCalories() <= 1000){
                        arr_spitcalo.add(food);
                    }
                }

                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private List<Photo> getListPhoto(){
        List<Photo> list = new ArrayList<>();

        list.add(new Photo(R.drawable.a));
        list.add(new Photo(R.drawable.b));
        list.add(new Photo(R.drawable.c));
        list.add(new Photo(R.drawable.d));

        return list;
    }

    private void autoSlideImage(){
        if (photoList == null || photoList.isEmpty() || viewPager == null){
            return;
        }
        if (timer == null){
            timer = new Timer();
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager.getCurrentItem();
                        int totalItem = photoList.size() - 1;
                        if (currentItem < totalItem){
                            currentItem ++;
                            viewPager.setCurrentItem(currentItem);
                        }else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 1000, 3000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null){
            timer.cancel();
            timer = null;
        }
    }
}