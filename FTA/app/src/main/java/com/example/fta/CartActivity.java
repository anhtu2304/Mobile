package com.example.fta;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fta.Adapter.CartListAdapter;
import com.example.fta.Interface.ChangeNumberItem;
import com.example.fta.Models.ManagementCart;
import com.example.fta.Models.Request;
import com.google.android.gms.common.internal.service.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CartActivity extends AppCompatActivity {
private CartListAdapter adapter;
private RecyclerView recy_tt;
private ManagementCart managementCart;
private TextView txtitem, txtdelivery, txttax, txttotal;
private ImageView img_back;
private Button btnthanhtoan;
private ScrollView scrollView;
final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("request");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        managementCart=new ManagementCart(this);
        
        addControls();
        addEvents();
        initList();
        caculateCard();
        btnthanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickThanhToan(Gravity.CENTER);
            }
        });
    }

    private void addEvents() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CartActivity.this, MenuActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void onClickThanhToan(int center) {

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_info_user);

        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = center;
        window.setAttributes(windowAttributes);

        if (Gravity.CENTER == center){
            dialog.setCancelable(true);
        }

        EditText edtphone = dialog.findViewById(R.id.edtphoneuser);
        EditText edtaddress = dialog.findViewById(R.id.edtaddressuser);
        Button btnhuy = dialog.findViewById(R.id.btnhuy);
        Button btndongy = dialog.findViewById(R.id.btndongy);

        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btndongy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                int Phone = Integer.parseInt(edtphone.getText().toString().trim());
                String Address = edtaddress.getText().toString().trim();
                Request request = new Request(Phone, user.getDisplayName(), Address, txttotal.getText().toString(), managementCart.getListCart());

                String pathObject = String.valueOf(request.getPhone());
                databaseReference.child(pathObject).setValue(request);

                managementCart.clearListCart();

                Toast.makeText(CartActivity.this, "Đơn hàng của bạn đã được xác nhận", Toast.LENGTH_SHORT).show();
                finish();

            }
        });

        dialog.show();
    }

    private void initList() {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recy_tt.setLayoutManager(linearLayoutManager);
        adapter=new CartListAdapter(managementCart.getListCart(), this, new ChangeNumberItem() {
            @Override
            public void changed() {
                caculateCard();
            }
        });

        recy_tt.setAdapter(adapter);
        if (managementCart.getListCart().isEmpty()){
            scrollView.setVisibility(View.GONE);
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.layout_cart_success);

            Window window = dialog.getWindow();
            if (window == null){
                return;
            }
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            window.setAttributes(windowAttributes);

            Button btndongy = dialog.findViewById(R.id.btn_success);
            btndongy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(CartActivity.this, MenuActivity.class);
                    startActivity(i);
                    finish();
                }
            });

            dialog.show();

        }else {
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    private void caculateCard() {
        int tax = 1;
        int delivery = 10;

        int total= (Math.round((managementCart.getTotalFee()+tax+delivery)*100)/100);
        int itemtotal= (Math.round(managementCart.getTotalFee()*100)/100);

        txtitem.setText(itemtotal+",000 đ");
        txttax.setText(tax+",000 đ");
        txtdelivery.setText(delivery+",000 đ");
        txttotal.setText(total+",000 đ");
    }

    private void addControls() {
        txtitem = findViewById(R.id.txtItemtotal);
        txtdelivery = findViewById(R.id.txtDelivery);
        txttax = findViewById(R.id.txtTax);
        txttotal = findViewById(R.id.txtTotal);
        recy_tt = findViewById(R.id.recycleViewItemTotal);
        scrollView = findViewById(R.id.mainScroll);
        btnthanhtoan = findViewById(R.id.btnThanhToan);
        img_back = findViewById(R.id.img_back);
    }
}