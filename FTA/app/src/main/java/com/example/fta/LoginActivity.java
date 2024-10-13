package com.example.fta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPS;
    Button btnLogin, btnDK;
    ImageView imgback;
    TextView txtforgotPS;
    LinearLayout linearLayout;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        ImageView visionps = findViewById(R.id.iconpasssword);
        visionps.setImageResource(R.drawable.vision_off);
        visionps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtPS.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    edtPS.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    visionps.setImageResource(R.drawable.vision_on);
                }else {
                    edtPS.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    visionps.setImageResource(R.drawable.vision_off);
            }
            }
        });

        addControls();

        btnDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, MenuActivity.class);
                startActivity(i);
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
            }
        });
    }

    private void login() {
        String Email, PS;
        Email = edtEmail.getText().toString().trim();
        PS = edtPS.getText().toString().trim();
        if(TextUtils.isEmpty(Email)){
            Toast.makeText(this,"Vui lòng nhập Email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(PS)){
            Toast.makeText(this,"Vui lòng nhập mật khẩu",Toast.LENGTH_LONG).show();
            return;
        }
      progressDialog.show();
        mAuth.signInWithEmailAndPassword(Email, PS).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            progressDialog.dismiss();
                if(task.isSuccessful()){
                    if(mAuth.getCurrentUser().isEmailVerified()){
                        Toast.makeText(getApplicationContext(),"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplicationContext(),"Vui lòng xác minh tài khoản qua Email",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Đăng nhập thất bại",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addControls(){
        btnLogin = findViewById(R.id.btnLogin);
        btnDK = findViewById(R.id.btnDK);
        imgback = findViewById(R.id.img_backmenu);
        edtEmail = findViewById(R.id.edtEmail);
        edtPS = findViewById(R.id.edtPS);
        txtforgotPS = findViewById(R.id.txtforgotPS);
        linearLayout = findViewById(R.id.linearForgotPS);
        progressDialog = new ProgressDialog(this);
    }
}