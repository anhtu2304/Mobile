package com.example.fta.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fta.DetailActivity;
import com.example.fta.Models.Food;
import com.example.fta.R;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> implements Filterable {
    private ArrayList<Food> foodArrayList;
    private ArrayList<Food> listFood;
    private Context context;

    public FoodAdapter(ArrayList<Food> foodArrayList, Context context) {
        this.foodArrayList = foodArrayList;
        this.listFood = foodArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Food food = foodArrayList.get(position);
        if (food == null){
            return;
        }
        holder.txtFood.setText(food.getTitle());
        holder.txtFee.setText(food.getFee()+",000 đ");
        holder.txtId.setText(String.valueOf(food.getId()));
        Glide.with(context).load(food.getPicture()).into(holder.imgFood);

        holder.linear_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.itemView.getContext(), DetailActivity.class);
                i.putExtra("food", foodArrayList.get(position));
                holder.itemView.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()){
                    foodArrayList = listFood;
                }else {
                    ArrayList<Food> foods = new ArrayList<>();
                    for (Food food : listFood){
                        if (food.getTitle().toLowerCase().contains(strSearch.toLowerCase())){
                            foods.add(food);
                        }
                    }

                    foodArrayList = foods;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = foodArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                foodArrayList = (ArrayList<Food>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtFood, txtFee, txtId;
        private ImageView imgFood;
        private LinearLayout linear_add;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFood = itemView.findViewById(R.id.txtfood);
            txtFee = itemView.findViewById(R.id.txtfee);
            txtId = itemView.findViewById(R.id.txtid);
            imgFood = itemView.findViewById(R.id.img_food);
            linear_add = itemView.findViewById(R.id.linear_add);

        }
    }
}
