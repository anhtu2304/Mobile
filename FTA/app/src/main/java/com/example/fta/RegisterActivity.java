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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class RegisterActivity extends AppCompatActivity {

    EditText edtEmailDK, edtPSDK, edtPSCF;
    Button btnRegister;
    ImageView imgback;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        onControl();

        ImageView visionpsdk = findViewById(R.id.iconpasssworddk);
        edtPSDK = findViewById(R.id.edtPSDK);
        visionpsdk.setImageResource(R.drawable.vision_off);
        visionpsdk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtPSDK.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    edtPSDK.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    visionpsdk.setImageResource(R.drawable.vision_on);
                }else {
                    edtPSDK.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    visionpsdk.setImageResource(R.drawable.vision_off);
                }
            }
        });

        ImageView visionpscf = findViewById(R.id.iconpassswordcf);
        edtPSCF = findViewById(R.id.edtPSCF);
        visionpscf.setImageResource(R.drawable.vision_off);

        visionpscf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtPSCF.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    edtPSCF.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    visionpscf.setImageResource(R.drawable.vision_on);
                }else {
                    edtPSCF.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    visionpscf.setImageResource(R.drawable.vision_off);
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private void register() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String EmailDK, PSDK, PSCF;
        EmailDK = edtEmailDK.getText().toString().trim();
        PSDK = edtPSDK.getText().toString().trim();
        PSCF = edtPSCF.getText().toString().trim();

        if(TextUtils.isEmpty(EmailDK)){
            Toast.makeText(this,"Vui lòng nhập Email",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(PSDK)){
            Toast.makeText(this,"Vui lòng nhập mật khẩu",Toast.LENGTH_LONG).show();
            return;
        }
        if (PSDK.length() < 6){
            Toast.makeText(this,"Mật khẩu phải dài hơn 6 ký tự",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(PSCF)){
            Toast.makeText(this,"Vui lòng nhập lại mật khẩu",Toast.LENGTH_LONG).show();
            return;
        }
        if (PSCF.length() < 6){
            Toast.makeText(this,"Mật khẩu phải dài hơn 6 ký tự",Toast.LENGTH_LONG).show();
            return;
        }

        if (!PSDK.equals(PSCF)) {
            Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_LONG).show();
        }else {
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(EmailDK, PSDK).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "Đăng ký thành công, vui lòng kiểm tra Email của bạn",Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(i);
                                }
                            }
                        });
                    }else {
                        Toast.makeText(getApplicationContext(), "Đăng ký thất bại",Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }
            });
        }
    }

    public void onControl(){
        edtEmailDK = findViewById(R.id.edtEmailDK);
        btnRegister = findViewById(R.id.btnRegister);
        imgback = findViewById(R.id.img_backlogin);
        progressDialog = new ProgressDialog(this);
    }
}