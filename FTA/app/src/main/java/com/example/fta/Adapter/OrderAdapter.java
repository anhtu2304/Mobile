package com.example.fta.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fta.DetailActivity;
import com.example.fta.DetailOrderActivity;
import com.example.fta.Models.Food;
import com.example.fta.Models.Request;
import com.example.fta.R;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{
    private ArrayList<Request> requestArrayList;
    private Context context;

    public OrderAdapter(ArrayList<Request> requestArrayList, Context context) {
        this.requestArrayList = requestArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order, parent, false);
        return new OrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Request request = requestArrayList.get(position);
        if (request == null){
            return;
        }
        holder.txtname.setText(request.getName());
        holder.txtstatus.setText(convertCodeToStatus(request.getStatus()));

        holder.linear_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.itemView.getContext(), DetailOrderActivity.class);
                i.putExtra("request", requestArrayList.get(position));
                holder.itemView.getContext().startActivity(i);
            }
        });
    }

    private String convertCodeToStatus(String status) {
        if (status.equals("0")){
            return "Đơn hàng đang được thực hiện";
        } else if   (status.equals("1")){
            return "Đang giao hàng";
        }
        {
            return "Đã giao hàng";
        }
    }

    @Override
    public int getItemCount() {
        return requestArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtname, txtstatus;
        private LinearLayout linear_order;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtname = itemView.findViewById(R.id.order_name);
            txtstatus = itemView.findViewById(R.id.order_status);
            linear_order = itemView.findViewById(R.id.linear_order);
        }
    }
}
