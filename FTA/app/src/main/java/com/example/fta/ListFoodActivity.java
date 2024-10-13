package com.example.fta;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Carousel;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fta.Adapter.FoodAdapter;
import com.example.fta.Adapter.ListAdminAdapter;
import com.example.fta.Models.Food;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListFoodActivity extends AppCompatActivity {
    private RecyclerView recy_admin;
    private ImageView img_back;
    private ArrayList<Food> foodArrayList;
    private RecyclerView.Adapter adapter;
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_food);

        recy_admin = findViewById(R.id.recy_admin);
        img_back = findViewById(R.id.img_back_admin);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListFoodActivity.this, AdminActivity.class);
                startActivity(i);
                finish();
            }
        });

        recy_admin.setHasFixedSize(true);
        recy_admin.setLayoutManager(new LinearLayoutManager(this));
        foodArrayList = new ArrayList<>();
        adapter = new ListAdminAdapter(foodArrayList, this, new ListAdminAdapter.IClickListener() {
            @Override
            public void onClickUpdateItem(Food food) {
                openDialogUpdate(food);
            }

            @Override
            public void onClickDeleteItem(Food food) {
                openDialogDelete(food);
            }
        });
        recy_admin.setAdapter(adapter);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Food food = snapshot.getValue(Food.class);
                if (food != null){
                    foodArrayList.add(food);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Food food = snapshot.getValue(Food.class);
                if (foodArrayList == null || foodArrayList.isEmpty() || food == null){
                    return;
                }

                for (int i = 0; i < foodArrayList.size(); i++){
                    if (food.getTitle() == foodArrayList.get(i).getTitle()){
                        foodArrayList.set(i, food);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Food food = snapshot.getValue(Food.class);
                if (foodArrayList == null || foodArrayList.isEmpty() || food == null){
                    return;
                }

                for (int i = 0; i < foodArrayList.size(); i++){
                    if (food.getTitle() == foodArrayList.get(i).getTitle()){
                        foodArrayList.remove(foodArrayList.get(i));
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void openDialogDelete(Food food) {
        new AlertDialog.Builder(this).setTitle("Xóa Sản Phẩm").setMessage("Bạn chắc chắn muốn xóa?").setPositiveButton("Đồng Ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseReference.child(String.valueOf(food.getId())).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(ListFoodActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).setNegativeButton("Hủy", null).show();
    }

    private void openDialogUpdate(Food food) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_update);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        EditText edtname = dialog.findViewById(R.id.edtname_update);
        EditText edtdes = dialog.findViewById(R.id.edtdes_update);
        EditText edtfee = dialog.findViewById(R.id.edtfee_update);
        Button btnupdate = dialog.findViewById(R.id.btn_done_update);
        Button btncancel = dialog.findViewById(R.id.btn_cancel_update);

        edtname.setText(food.getTitle());
        edtdes.setText(food.getDescription());
        edtfee.setText(String.valueOf(food.getFee()));

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = edtname.getText().toString().trim();
                String newDes = edtdes.getText().toString().trim();
                int newFee = Integer.parseInt(edtfee.getText().toString().trim());

                food.setTitle(newName);
                food.setDescription(newDes);
                food.setFee(newFee);

                databaseReference.child(String.valueOf(food.getId())).updateChildren(food.toMap(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(ListFoodActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialog.show();
    }
}