package com.example.fta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fta.Models.Request;

public class DetailOrderActivity extends AppCompatActivity {
    private TextView txtnameorder, txtphoneorder, txtaddressorder, txttotalorder;
    private ImageView img_back;
    private Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_order);

        addControls();
        addEvents();
    }

    private void addEvents() {
        request=(Request) getIntent().getSerializableExtra("request");

        txtnameorder.setText(request.getName());
        txtphoneorder.setText(String.valueOf(request.getPhone()));
        txtaddressorder.setText(request.getAddress());
        txttotalorder.setText(request.getTotal()+"");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailOrderActivity.this, MenuActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void addControls() {
        txtnameorder = findViewById(R.id.txtname_order);
        txtphoneorder = findViewById(R.id.txtphone_order);
        txtaddressorder = findViewById(R.id.txtaddress_order);
        txttotalorder = findViewById(R.id.txttotal_order);
        img_back = findViewById(R.id.img_back_order);
    }
}