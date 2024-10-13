package com.example.fta.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fta.Interface.ChangeNumberItem;
import com.example.fta.MenuActivity;
import com.example.fta.Models.Food;
import com.example.fta.Models.ManagementCart;
import com.example.fta.R;

import java.util.ArrayList;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {
    ArrayList<Food> ListFoodSelected;
    private ManagementCart managementCart;
    ChangeNumberItem changeNumberItem;

    public CartListAdapter(ArrayList<Food> foodArrayList, Context context, ChangeNumberItem changeNumberItem) {
        this.ListFoodSelected = foodArrayList;
        managementCart=new ManagementCart(context);
        this.changeNumberItem = changeNumberItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Food food = ListFoodSelected.get(position);
        if (food == null){
            return;
        }
        holder.txtFood.setText(food.getTitle());
        holder.txtFeeEachItem.setText(food.getFee()+",000 đ");
        holder.txttotalEachItem.setText(Math.round((food.getNumberInCart()*food.getFee()))+",000 đ");
        holder.num.setText(String.valueOf(food.getNumberInCart()));
        Glide.with(holder.itemView.getContext()).load(food.getPicture()).into(holder.imgFood);

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managementCart.maxNumberFood(ListFoodSelected, position, new ChangeNumberItem() {
                    @Override
                    public void changed() {
                        notifyDataSetChanged();
                        changeNumberItem.changed();
                    }
                });
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managementCart.minNumberFood(ListFoodSelected, position, new ChangeNumberItem() {
                    @Override
                    public void changed() {
                        notifyDataSetChanged();
                        changeNumberItem.changed();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return ListFoodSelected.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtFood, txtFeeEachItem, txttotalEachItem, num;
        private ImageView imgFood, plus, minus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFood = itemView.findViewById(R.id.txtTitle);
            txtFeeEachItem = itemView.findViewById(R.id.feeEachItem);
            txttotalEachItem = itemView.findViewById(R.id.totalEachItem);
            num =itemView.findViewById(R.id.txtnum);
            imgFood = itemView.findViewById(R.id.img_cart_food);
            plus = itemView.findViewById(R.id.img_plus);
            minus = itemView.findViewById(R.id.img_minus);
        }
    }
}
