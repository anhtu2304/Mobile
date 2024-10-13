package com.example.fta;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.fta.Models.Food;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class AdminActivity extends AppCompatActivity {
    private Button btn_choose_file, btn_list_file, btn_add_pro;
    private EditText txtid, txttitle, txtdes, txtprice, txtstar, txttime, txtcalo;
    private ImageView imgadmin;
    private ProgressDialog progressDialog;
    private Uri mUri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK){
                if (result.getData() != null) {
                    mUri = result.getData().getData();
                    btn_add_pro.setEnabled(true);
                    Glide.with(getApplicationContext()).load(mUri).into(imgadmin);
                }
            }else {
                Toast.makeText(AdminActivity.this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);

        FirebaseApp.initializeApp(AdminActivity.this);
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        addControls();
        addEvents();

    }

    private void addEvents() {
        btn_choose_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        btn_add_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               uploadFile();
            }
        });

        btn_list_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminActivity.this, ListFoodActivity.class);
                startActivity(i);
            }
        });
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mUri != null){
            progressDialog.show();
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(mUri));
            fileReference.putFile(mUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            progressDialog.dismiss();
                            Toast.makeText(AdminActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();

                            int id = Integer.parseInt(txtid.getText().toString());
                            String title = txttitle.getText().toString().trim();
                            String des = txtdes.getText().toString().trim();
                            int price = Integer.parseInt(txtprice.getText().toString());
                            int star = Integer.parseInt(txtstar.getText().toString());
                            int time = Integer.parseInt(txttime.getText().toString());
                            int calo = Integer.parseInt(txtcalo.getText().toString());

                            Food food = new Food(id, title,uri.toString() ,des, price, star, time, calo);
                            String pathObject = String.valueOf(food.getId());
                            databaseReference.child(pathObject).setValue(food);

                            txtid.setText("");
                            txttitle.setText("");
                            txtdes.setText("");
                            txtprice.setText("");
                            txtstar.setText("");
                            txttime.setText("");
                            txtcalo.setText("");

                        }
                    });
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }else {
            Toast.makeText(this, "Chưa chọn ảnh", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFileChooser() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        activityResultLauncher.launch(i);
    }

    @SuppressLint("WrongViewCast")
    private void addControls() {
        btn_choose_file = findViewById(R.id.btn_choose_img);
        btn_list_file = findViewById(R.id.btn_list_img);
        btn_add_pro = findViewById(R.id.btn_add_pro);
        txtid = findViewById(R.id.edtid_admin);
        txttitle = findViewById(R.id.edttitle_admin);
        txtdes = findViewById(R.id.edtdes_admin);
        txtprice = findViewById(R.id.edtprice_admin);
        txtstar = findViewById(R.id.edtstar_admin);
        txttime = findViewById(R.id.edttime_admin);
        txtcalo = findViewById(R.id.edtcalo_admin);
        imgadmin = findViewById(R.id.img_admin);
        progressDialog = new ProgressDialog(this);
    }
}