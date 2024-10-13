package com.example.fta;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.fta.Models.Food;
import com.example.fta.Models.ManagementCart;

public class DetailActivity extends AppCompatActivity {
private LinearLayout add_cart;
private TextView title_detail, txtnum_detail, price_detail, total_detail, txtdescription, star, time, calo, id;
private ImageView plus, minus, img_detail, img_back;
private Food food;
private int numberOder = 1;
private ManagementCart managementCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);

        managementCart = new ManagementCart(this);

        addControls();
        getBundle();
    }

    @SuppressLint("SetTextI18n")
    private void getBundle() {
        food=(Food)getIntent().getSerializableExtra("food");

        Glide.with(this).load(food.getPicture()).into(img_detail);
        title_detail.setText(food.getTitle());
        price_detail.setText(food.getFee()+",000 đ");
        txtdescription.setText(food.getDescription());
        txtnum_detail.setText(String.valueOf(numberOder));
        id.setText(food.getId()+"");
        calo.setText(food.getCalories()+"calo");
        star.setText(food.getStar()+"");
        time.setText(food.getTime()+"min");
        total_detail.setText(Math.round(numberOder*food.getFee())+",000 đ");

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberOder=numberOder+1;
                txtnum_detail.setText(String.valueOf(numberOder));
                total_detail.setText(Math.round(numberOder*food.getFee())+",000 đ");
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberOder > 1){
                    numberOder=numberOder-1;
                }
                txtnum_detail.setText(String.valueOf(numberOder));
                total_detail.setText(Math.round(numberOder*food.getFee())+",000 đ");
            }
        });

        add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food.setNumberInCart(numberOder);
                managementCart.insertFood(food);
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailActivity.this, MenuActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void addControls() {
        add_cart = findViewById(R.id.add_cart);
        title_detail = findViewById(R.id.title_detail);
        txtnum_detail = findViewById(R.id.txtnum_detail);
        price_detail = findViewById(R.id.price_detail);
        total_detail = findViewById(R.id.total_detail);
        plus = findViewById(R.id.img_plus_detail);
        minus = findViewById(R.id.img_minus_detail);
        img_detail = findViewById(R.id.img_detail);
        img_back = findViewById(R.id.img_back_detail);
        txtdescription = findViewById(R.id.txtdescription);
        star = findViewById(R.id.txtstar);
        time = findViewById(R.id.txttime);
        calo = findViewById(R.id.txtcalo);
        id = findViewById(R.id.txtid);
    }
}