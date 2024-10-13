package com.example.fta.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fta.DetailActivity;
import com.example.fta.Models.Food;
import com.example.fta.R;

import java.util.ArrayList;

public class ListAdminAdapter extends RecyclerView.Adapter<ListAdminAdapter.ViewHolder> {
    private ArrayList<Food> foodArrayAdmin;
    private Context context;
    private IClickListener clickListener;
    public interface IClickListener{
        void onClickUpdateItem(Food food);
        void onClickDeleteItem(Food food);
    }

    public ListAdminAdapter(ArrayList<Food> foodArrayAdmin, Context context, IClickListener clickListener) {
        this.foodArrayAdmin = foodArrayAdmin;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Food food = foodArrayAdmin.get(position);
        if (food == null){
            return;
        }
        holder.txtFood.setText(food.getTitle());
        holder.txtDes.setText(food.getDescription());
        holder.txtFee.setText(food.getFee()+",000 đ");
        Glide.with(context).load(food.getPicture()).into(holder.imgFood);

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClickUpdateItem(food);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClickDeleteItem(food);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodArrayAdmin.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtFood, txtFee, txtDes;
        private ImageView imgFood;
        private Button btnUpdate, btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFood = itemView.findViewById(R.id.txtfood_admin);
            txtFee = itemView.findViewById(R.id.txtfee_admin);
            txtDes = itemView.findViewById(R.id.txtdes_admin);
            imgFood = itemView.findViewById(R.id.img_list_admin);
            btnUpdate = itemView.findViewById(R.id.btnUpdate_admin);
            btnDelete = itemView.findViewById(R.id.btnDelete_admin);
        }
    }
}
