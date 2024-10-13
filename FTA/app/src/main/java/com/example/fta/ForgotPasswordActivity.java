package com.example.fta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText edtForgotPS;
    Button btnForgotPS;
    ImageView img_back;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        onControl();
        mAuth = FirebaseAuth.getInstance();
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Intent i = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                        startActivity(i);
            }
        });

        btnForgotPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPS();
            }

            private void ForgotPS() {
                String emailAddress;
                emailAddress = edtForgotPS.getText().toString();
                if(TextUtils.isEmpty(emailAddress)){
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập Email", Toast.LENGTH_LONG).show();
                    return;
                }
                progressDialog.show();
                mAuth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPasswordActivity.this, "Đã gửi", Toast.LENGTH_SHORT).show();
                                    edtForgotPS.setText("");
                                }
                            }
                        });
            }
        });
    }

    private void onControl() {
        edtForgotPS = findViewById(R.id.edtForgotPS);
        btnForgotPS = findViewById(R.id.btnForgotPS);
        img_back = findViewById(R.id.img_back_forgot);
        progressDialog = new ProgressDialog(this);
    }
}